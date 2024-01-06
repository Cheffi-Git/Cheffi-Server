package com.cheffi.notification.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cheffi.notification.domain.Notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Repository
public class NotificationJdbcRepository {

	private final JdbcTemplate jdbcTemplate;

	/**
	 * Batch Insert를 위해 JDBC 사용
	 */
	public Integer saveAll(final List<Notification> notifications, final String inserter) {
		String sql = "INSERT INTO NOTIFICATION "
			+ "(category, checked, content, avatar_id, created_date, updated_date, created_by, modified_by) "
			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		int[] result = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Notification notification = notifications.get(i);
				ps.setString(1, notification.getCategory().toString());
				ps.setBoolean(2, false);
				ps.setString(3, notification.getContent());
				ps.setLong(4, notification.getTarget().getId());
				ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
				ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
				ps.setString(7, inserter);
				ps.setString(8, inserter);
			}

			@Override
			public int getBatchSize() {
				return notifications.size();
			}
		});

		return result.length;
	}

}
