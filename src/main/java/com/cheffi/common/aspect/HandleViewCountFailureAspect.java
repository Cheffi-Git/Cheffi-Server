package com.cheffi.common.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.hibernate.StaleObjectStateException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cheffi.view.service.ViewCountingFailureService;

import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
@Aspect
@Component
public class HandleViewCountFailureAspect {

	private final ViewCountingFailureService viewCountingFailureService;

	@Pointcut("@annotation(com.cheffi.common.aspect.annotation.HandleFailure)")
	private void handleFailure() {
	}

	@Pointcut("execution(* *(Long, ..))")
	private void getLong() {
	}

	@Transactional
	@Around("handleFailure() && getLong()")
	public Object handleViewCountFailure(ProceedingJoinPoint joinPoint) throws Throwable {
		try {
			return joinPoint.proceed();
		} catch (OptimisticLockException | ObjectOptimisticLockingFailureException | StaleObjectStateException e) {
			log.info("조회수 증가 요청 실패");
			viewCountingFailureService.saveViewCountingFailure((Long)joinPoint.getArgs()[0]);
		} catch (Exception e) {
			log.info(e.getMessage());
		}
		return null;
	}
}
