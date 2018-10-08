package cz.muni.fi.pa165;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import javax.inject.Named;

@Named
@Aspect
public class LoggingAspect {
    @Around("execution(public * *(..))")
    public Object logMethodCall(ProceedingJoinPoint joinPoint) throws Throwable {
        System.err.println("Calling method: "
                + joinPoint.getSignature());
        long startTime = System.nanoTime();
        Object result = joinPoint.proceed();
        long estimatedTime = System.nanoTime() - startTime;
        System.err.println("Method finished: "
                + joinPoint.getSignature() + " in " + estimatedTime + " ns");

        return result;
    }
}
