package com.epam.gym.metric;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MethodTimeMeasureAspect {

    private final MeterRegistry meterRegistry;

    public MethodTimeMeasureAspect(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Around("@annotation(timed)")
    public Object timeDaoMethodExecution(ProceedingJoinPoint joinPoint, Timed timed) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        Timer.Sample sample = Timer.start(meterRegistry);
        try {
            return joinPoint.proceed();
        } finally {
            sample.stop(Timer.builder("method_execution_time")
                    .tag("method", methodName)
                    .description("Time taken for method execution")
                    .register(meterRegistry));
        }
    }
}

//TODO test code
