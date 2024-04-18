package org.example.librarymanagementsystem.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("execution(* org.example.librarymanagementsystem.services.*.*(..))") // Pointcut for all methods in services package
    public void applicationPackagePointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    @Before("applicationPackagePointcut()")
    public void logMethodCall(JoinPoint joinPoint) {
        // Log method call with method name and arguments
        logger.info("Entering method: {} with arguments: {}", joinPoint.getSignature().getName(), joinPoint.getArgs());
    }

    @AfterThrowing(pointcut = "applicationPackagePointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        logger.error("Exception in method: {} with arguments: {}. Exception: {}", joinPoint.getSignature().getName(), joinPoint.getArgs(), e.getMessage());
    }

    @Around("applicationPackagePointcut()") // Around advice for all methods in services package to log method execution time
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        logger.info("Execution time of method: {} with arguments: {} is {} ms", joinPoint.getSignature().getName(), joinPoint.getArgs(), endTime - startTime);
        return result;
    }
}
