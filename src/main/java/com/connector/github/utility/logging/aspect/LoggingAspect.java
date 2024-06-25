package com.connector.github.utility.logging.aspect;

import com.connector.github.utility.logging.annotation.Logging;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Log4j2
@Aspect
@Component
public class LoggingAspect {

  @Around("@annotation(logExecutionTime)")
  public Object logMethod(ProceedingJoinPoint joinPoint, Logging logExecutionTime) throws Throwable {
    long startTime = System.currentTimeMillis();

    beforeMethod(joinPoint);

    Object result = joinPoint.proceed();

    long endTime = System.currentTimeMillis();
    long executionTime = endTime - startTime;
    afterMethod(joinPoint, result, executionTime);

    return result;
  }

  private void beforeMethod(ProceedingJoinPoint joinPoint) {
    String methodName = joinPoint.getSignature().toShortString();
    Object[] args = joinPoint.getArgs();
    log.info("Execute method: {} with arguments: {}", methodName, args);
  }

  private void afterMethod(ProceedingJoinPoint joinPoint, Object result, long executionTime) {
    String methodName = joinPoint.getSignature().toShortString();
    log.info("Method: {} returned: {} and took {} ms", methodName, result, executionTime);
  }
}
