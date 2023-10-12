package io.oduck.api.global.security.auth.entity;

import io.oduck.api.domain.member.entity.Member;
import io.oduck.api.global.audit.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class AuthLocal extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // TODO
  @OneToOne
  @JoinColumn(name = "member_id")
  private Member member;

  @Column(nullable = false, length = 100, unique = true)
  private String email;

  @Column(nullable = false, length = 75)
  private String password;

  @Builder
  public AuthLocal(Member member, String email, String password) {
    this.member = member;
    this.email = email;
    this.password = password;
  }

  public void setMember(Member member) {
    this.member = member;
  }
}
