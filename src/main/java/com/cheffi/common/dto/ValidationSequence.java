package com.cheffi.common.dto;

import static com.cheffi.common.dto.ValidationGroups.*;

import jakarta.validation.GroupSequence;
import jakarta.validation.groups.Default;

/**
 * Validation 순서를 정하기 위한 클래스
 */
@GroupSequence({Default.class, NotEmptyGroup.class, AfterEmptyCheckGroup.class,})
public interface ValidationSequence {
}
