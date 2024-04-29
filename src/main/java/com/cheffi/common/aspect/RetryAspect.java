package com.cheffi.common.aspect;

import java.util.Random;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
@Aspect
@Component
public class RetryAspect {

	private static final int MAX_RETRIES = 10;
	private static final int RETRY_DELAY_MS = 50;

	@Pointcut("@annotation(com.cheffi.common.aspect.annotation.NamedLock)")
	public void namedLock() {
	}

	@Pointcut("execution(* *(Long, ..))")
	private void getLong() {
	}

	@Pointcut("execution(* *.*ViewCountingFailure(..))")
	private void viewCountFailure() {
	}

	@Around("namedLock() && getLong() && viewCountFailure()")
	public Object retry(ProceedingJoinPoint joinPoint) throws Throwable {
		Exception exceptionHolder = null;
		Random random = new Random();
		for (int attempt = 0; attempt < MAX_RETRIES; attempt++) {
			try {
				Object result = joinPoint.proceed();
				log.info("로직 완료");
				return result;
			} catch (Exception e) {
				log.info("재시도");
				exceptionHolder = e;
				Thread.sleep(RETRY_DELAY_MS + random.nextInt(100));
			}
		}
		throw exceptionHolder;
	}

}
