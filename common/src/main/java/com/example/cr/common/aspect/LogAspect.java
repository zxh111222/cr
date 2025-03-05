package com.example.cr.common.aspect;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.*;

@Aspect
@Component
public class LogAspect {
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void restControllerPointcut() {}

    @Pointcut("within(@org.springframework.stereotype.Controller *)")
    public void controllerPointcut() {}

    @Around("restControllerPointcut() || controllerPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        // Get HTTP request details
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        // Log request details
        logRequest(joinPoint, request);

        // Proceed with the method execution
        long startTime = System.currentTimeMillis();
        Object result = null;
        try {
            result = joinPoint.proceed();
            return result;
        } catch (Throwable throwable) {
            logger.error("Exception in {}.{}() with message = {} and cause = {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                throwable.getMessage(),
                throwable.getCause() != null ? throwable.getCause() : "NULL");
            throw throwable;
        } finally {
            // Log response details
            logResponse(joinPoint, request, result, startTime);
        }
    }

    private void logRequest(ProceedingJoinPoint joinPoint, HttpServletRequest request) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        Map<String, Object> logDetails = new HashMap<>();
        logDetails.put("method", request.getMethod());
        logDetails.put("url", request.getRequestURL().toString());
        logDetails.put("controller", method.getDeclaringClass().getSimpleName());
        logDetails.put("action", method.getName());
        logDetails.put("client_ip", request.getRemoteAddr());

        // Log request parameters
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0) {
            try {
                List<Object> safeArgs = new ArrayList<>();
                for (Object arg : args) {
                    Object maskedArg = maskSensitiveInfo(arg);
                    if (maskedArg != null) {
                        safeArgs.add(maskedArg);
                    }
                }

                if (!safeArgs.isEmpty()) {
                    logDetails.put("parameters", objectMapper.writeValueAsString(safeArgs));
                }
            } catch (Exception e) {
                logger.warn("Could not serialize request parameters", e);
            }
        }

        logger.info("Request Details: {}", logDetails);
    }

    private void logResponse(ProceedingJoinPoint joinPoint, HttpServletRequest request, Object result, long startTime) {
        long duration = System.currentTimeMillis() - startTime;

        Map<String, Object> logDetails = new HashMap<>();
        logDetails.put("method", request.getMethod());
        logDetails.put("url", request.getRequestURL().toString());
        logDetails.put("duration_ms", duration);

        // Log response result
        if (result != null) {
            try {
                Object maskedResult = maskSensitiveInfo(result);
                if (maskedResult != null) {
                    logDetails.put("response", objectMapper.writeValueAsString(maskedResult));
                }
            } catch (Exception e) {
                logger.warn("Could not serialize response", e);
            }
        }

        logger.info("Response Details: {}", logDetails);
    }

    private boolean isSafeToLog(Object obj) {
        if (obj == null) {
            return false;
        }

        // Exclude MultipartFile and its subclasses
        if (obj instanceof org.springframework.web.multipart.MultipartFile) {
            return false;
        }

        // Exclude file-related classes
        Class<?> objClass = obj.getClass();
        if (isFileRelatedClass(objClass)) {
            return false;
        }

        // Exclude large collections or arrays
        if (obj instanceof Collection && ((Collection<?>) obj).size() > 1000) {
            return false;
        }
        if (obj.getClass().isArray() && Array.getLength(obj) > 1000) {
            return false;
        }

        return true;
    }

    private boolean isFileRelatedClass(Class<?> clazz) {
        // Add more file-related classes as needed
        String[] excludedClassPrefixes = {
            "java.io.File",
            "java.nio.file.Path",
            "org.springframework.core.io.Resource",
            "org.springframework.web.multipart.MultipartFile"
        };

        while (clazz != null) {
            String className = clazz.getName();
            for (String prefix : excludedClassPrefixes) {
                if (className.startsWith(prefix)) {
                    return true;
                }
            }
            clazz = clazz.getSuperclass();
        }
        return false;
    }

    private Object maskSensitiveInfo(Object obj) {
        try {
            // 如果是null或不是安全的对象，直接返回
            if (!isSafeToLog(obj)) {
                return null;
            }

            // 将对象转换为JSON
            JsonNode jsonNode = objectMapper.valueToTree(obj);

            if (jsonNode.isObject()) {
                return maskSensitiveFields((ObjectNode) jsonNode);
            } else if (jsonNode.isArray()) {
                // 处理数组情况
                List<JsonNode> maskedArray = new ArrayList<>();
                for (JsonNode element : jsonNode) {
                    if (element.isObject()) {
                        maskedArray.add(maskSensitiveFields((ObjectNode) element));
                    } else {
                        maskedArray.add(element);
                    }
                }
                return maskedArray;
            }

            return obj;
        } catch (Exception e) {
            logger.warn("脱敏处理失败", e);
            return obj;
        }
    }

    private ObjectNode maskSensitiveFields(ObjectNode node) {
        // 敏感字段关键词列表
        String[] sensitiveKeywords = {
            "password", "idCard"
        };

        // 深度克隆节点
        ObjectNode maskedNode = node.deepCopy();

        // 遍历并脱敏匹配的字段
        Iterator<Map.Entry<String, JsonNode>> fields = maskedNode.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            String fieldName = entry.getKey().toLowerCase();

            // 检查是否包含敏感关键词
            for (String keyword : sensitiveKeywords) {
                if (fieldName.contains(keyword)) {
                    // 对敏感字段进行脱敏
                    maskedNode.put(entry.getKey(), "******");
                    break;
                }
            }
        }

        return maskedNode;
    }
}
