package io.oduck.api.domain.inquiry.repository;

import io.oduck.api.domain.inquiry.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

}