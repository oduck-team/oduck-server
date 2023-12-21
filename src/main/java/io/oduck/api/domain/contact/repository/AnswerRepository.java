package io.oduck.api.domain.contact.repository;

import io.oduck.api.domain.contact.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

}