package io.oduck.api.domain.member.entity;

import io.oduck.api.global.audit.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class MemberProfile extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @Column(nullable = false, length = 15, unique = true)
  private String name;

  @Column(nullable = false, length = 50)
  private String info;

  @Enumerated(EnumType.STRING)
  private Role role;

  @Column(length = 100)
  private String thumbnail;

  @Column(length = 100)
  private String backgroundImage;

  @Column(nullable = false)
  @ColumnDefault("0")
  private Long point;
}
