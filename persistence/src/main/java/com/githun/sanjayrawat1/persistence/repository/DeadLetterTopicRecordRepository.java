package com.githun.sanjayrawat1.persistence.repository;

import com.github.sanjayrawat1.domain.DeadLetterTopicRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link DeadLetterTopicRecord} entity.
 *
 * @author sanjayrawat1
 */
@Repository
public interface DeadLetterTopicRecordRepository extends JpaRepository<DeadLetterTopicRecord, Long> {}
