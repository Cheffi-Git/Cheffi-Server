package com.cheffi.common.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cheffi.common.code.ErrorCode;
import com.cheffi.common.config.exception.business.GetLockFailedException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Repository
public class NamedLockRepository {

	private static final String GET_LOCK = "SELECT GET_LOCK(?, ?)";
	private static final String RELEASE_LOCK = "SELECT RELEASE_LOCK(?)";
	private static final String EXCEPTION_MESSAGE = "LOCK을 수행하는 중에 오류가 발생하였습니다.";

	private final EntityManager em;
	private final JdbcTemplate jdbcTemplate;

	public void getNamedLock(String lockName, int timeoutSeconds) {
		Object[] params = new Object[] {lockName, timeoutSeconds};
		Integer result = jdbcTemplate.queryForObject(GET_LOCK, params, Integer.class);
		checkResult(result);
	}

	private void checkResult(Integer result) {
		if (result == null) {
			log.error("USER LEVEL LOCK 쿼리 결과 값이 없습니다.");
			throw new GetLockFailedException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
		if (result != 1) {
			log.error("USER LEVEL LOCK 쿼리 결과 값이 1이 아닙니다.");
			throw new GetLockFailedException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}

	public void releaseNamedLock(String lockName) {
		Object[] params = new Object[] {lockName};
		Integer result = jdbcTemplate.queryForObject(RELEASE_LOCK, params, Integer.class);
		// if (result != 1) {
		// 	log.info("ReleaseLock 실패 ReleaseLock 실패 ReleaseLock 실패 ReleaseLock 실패 ReleaseLock 실패");
		// 	throw new ReleaseFailedException(ErrorCode.INTERNAL_SERVER_ERROR);
		// }
	}

	public int performCriticalSection(String userLockName, int timeoutSeconds) {
		// Acquire the named lock
		Query query = em.createNativeQuery("SELECT GET_LOCK(:userLockName, :timeoutSeconds)")
			.setParameter("userLockName", userLockName)
			.setParameter("timeoutSeconds", timeoutSeconds);
		return ((Long)query.getSingleResult()).intValue();
	}

	public Long releaseLock(String userLockName) {
		// Release the named lock
		Query query = em.createNativeQuery("SELECT RELEASE_LOCK(:userLockName)")
			.setParameter("userLockName", userLockName);
		Long singleResult = (Long)query.getSingleResult();
		return singleResult;
	}

}
