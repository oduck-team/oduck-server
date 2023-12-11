package io.oduck.api.domain.inquiry.entity;

import io.oduck.api.domain.admin.entity.Admin;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "answer")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    private FeedbackType helpful;

    @Column(name = "check_answer")
    private boolean check = false;

    @OneToOne(mappedBy = "answer", cascade = CascadeType.PERSIST)
    private Contact contact;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Admin admin;

    public void check() {
        check = true;
    }

    public void feedback(FeedbackType helpful) {
        this.helpful = helpful;
    }

    public void update(String content) {
        this.content = content;
    }

    public void answer() {
        contact.answer();
    }
}
