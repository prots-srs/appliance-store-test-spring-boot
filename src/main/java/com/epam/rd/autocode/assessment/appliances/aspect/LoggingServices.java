package com.epam.rd.autocode.assessment.appliances.aspect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;

@Component
@Aspect
public class LoggingServices {
  private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

  @Pointcut("@annotation(Loggable)")
  public void executeLogging() {
  }

  @Pointcut("@annotation(TrackTime)")
  public void executeTrackTime() {
  }

  @Around("executeLogging()")
  public void incrementCount(ProceedingJoinPoint joinPoint) throws Throwable {
    String methodName = joinPoint.getSignature().getName();
    Object result = joinPoint.proceed();

    LOGGER.info("<== Exiting: {} | returned: {}", methodName, result);
  }

  @Around("executeTrackTime()")
  public Object monitorTime(ProceedingJoinPoint joinPoint) throws Throwable {
    // Use Spring's StopWatch for easy timing
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();

    Object result = joinPoint.proceed(); // Execute the method

    stopWatch.stop();

    long executionTime = stopWatch.getTotalTimeMillis();
    String methodName = joinPoint.getSignature().toShortString();

    LOGGER.info("Performance: {} executed in {} ms", methodName, executionTime);

    return result;
  }
}
