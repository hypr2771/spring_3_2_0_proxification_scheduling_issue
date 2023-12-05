package org.example;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import reactor.core.publisher.Mono;

@Aspect
@Configuration
@EnableAspectJAutoProxy
public class LoggingAspect {

  @Around("@annotation(org.springframework.scheduling.annotation.Scheduled)")
  public Object aroundScheduled(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

    System.out.println("Running scheduled %s".formatted(proceedingJoinPoint.getSignature()));

    var mono = (Mono<String>) proceedingJoinPoint.proceed();

    mono.subscribe(result -> System.out.println("Ended scheduled %s with result %s".formatted(proceedingJoinPoint.getSignature(), result)));

    return mono;
  }

}
