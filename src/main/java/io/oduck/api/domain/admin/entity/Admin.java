package io.oduck.api.domain.admin.entity;

import io.oduck.api.domain.inquiry.entity.Answer;
import io.oduck.api.domain.inquiry.entity.FeedbackType;
import io.oduck.api.domain.inquiry.service.AnswerHolder;
import io.oduck.api.domain.inquiry.service.AnswerUpdateHolder;
import io.oduck.api.global.exception.BadRequestException;
import io.oduck.api.global.exception.NotFoundException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "member")
@Entity
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "admin", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<Answer> answers = new ArrayList<>();

    public void answer(AnswerHolder holder) {
        Answer answer = Answer.builder()
            .content(holder.getContent())
            .helpful(FeedbackType.NONE)
            .check(false)
            .contact(holder.getContact())
            .admin(this)
            .build();
        answers.add(answer);

        answer.answer();
    }

    public void updateAnswer(AnswerUpdateHolder holder) {
        Answer answer = answers.stream()
            .filter(a -> a.getId() == holder.getAnswerId())
            .findFirst()
            .orElseThrow(() -> new NotFoundException("answer"));

        if(answer.isCheck() == true) {
            throw new BadRequestException("이미 고객이 확인한 문의입니다.");
        }

        answer.update(holder.getContent());
    }
}
