package com.cheffi.common.aspect;

import java.util.Random;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cheffi.common.service.NamedLockRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE + 2)
@Aspect
@Component
public class NamedLockAspect {

	public static final int TIME_OUT_SECONDS = 1;
	private final NamedLockRepository namedLockRepository;

	@Pointcut("@annotation(com.cheffi.common.aspect.annotation.NamedLock)")
	public void namedLock() {
	}

	@Pointcut("execution(* *(Long, ..))")
	private void getLong() {
	}

	@Pointcut("execution(* *.*ViewCountingFailure(..))")
	private void viewCountFailure() {
	}

	@Transactional
	@Around("namedLock() && getLong() && viewCountFailure()")
	public Object saveViewCountingFailure(ProceedingJoinPoint joinPoint) throws Throwable {
		Object result = null;
		Long reviewId = (Long)joinPoint.getArgs()[0];
		Random random = new Random();
		namedLockRepository.getNamedLock("ViewCountFailure" + reviewId, TIME_OUT_SECONDS);
		try {
			result = joinPoint.proceed();
		} catch (Exception e) {
			// log.info(e.toString());
			throw e;
		} finally {
			namedLockRepository.releaseNamedLock("ViewCountFailure" + reviewId);
		}
		return result;
	}

}
