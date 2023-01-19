package com.mithwick93.stocks.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mithwick93.stocks.util.JsonUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

/**
 * Aspect for logging execution of service and repository Spring components.
 *
 * @author mithwick93
 */
@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("within(com.mithwick93.stocks..*)" + " && within(@org.springframework.web.bind.annotation.RestController *)")
    public void controllerPointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    @Pointcut("within(com.mithwick93.stocks..*)" + " && within(@org.springframework.web.bind.annotation.ControllerAdvice *)")
    public void controllerAdvisorPointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    /**
     * Advice that logs when a controller method is entered.
     *
     * @param joinPoint Join point for advice
     */
    @Before("controllerPointcut()")
    public void beforeEndpoint(JoinPoint joinPoint) throws JsonProcessingException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        log.info(
                "[Request] Endpoint: [{}] {}, Params: {}, Args: {}",
                request.getMethod(),
                request.getRequestURI(),
                JsonUtil.getJsonString(request.getParameterMap()),
                Arrays.toString(joinPoint.getArgs())
        );
    }

    /**
     * Advice that logs when a controller method is exited.
     *
     * @param joinPoint Join point for advice
     */
    @AfterReturning(pointcut = "controllerPointcut()", returning = "returnValue")
    public void afterEndpoint(JoinPoint joinPoint, Object returnValue) throws JsonProcessingException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        log.info(
                "[Response] Endpoint: [{}] {}, Returned: {}",
                request.getMethod(),
                request.getRequestURI(),
                JsonUtil.getJsonString(returnValue)
        );
    }

    /**
     * Advice that logs methods throwing exceptions.
     *
     * @param joinPoint Join point for advice
     * @param e         Exception
     */
    @AfterThrowing(pointcut = "controllerPointcut()", throwing = "e")
    public void afterThrow(JoinPoint joinPoint, Exception e) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        log.error(
                "[Response Exception] Endpoint: [{}] {}, Exception: {}, Message: {}",
                request.getMethod(),
                request.getRequestURI(),
                e.getClass().getSimpleName(),
                e.getMessage(),
                e
        );
    }

    /**
     * Advice that logs when a controller advice method is exited.
     *
     * @param joinPoint Join point for advice
     */
    @AfterReturning(pointcut = "controllerAdvisorPointcut()", returning = "returnValue")
    public void afterExceptionHandle(JoinPoint joinPoint, Object returnValue) throws JsonProcessingException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        log.error(
                "[Exception Handler] Endpoint: [{}] {}, Returned: {}",
                request.getMethod(),
                request.getRequestURI(),
                JsonUtil.getJsonString(returnValue)
        );
    }

}
