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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Builder
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
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
  @Builder.Default
  private String info = "";

  @Enumerated(EnumType.STRING)
  @Builder.Default
  private Role role = Role.MEMBER;

  @Column(length = 100)
  @Builder.Default
  private String thumbnail = "";

  @Column(length = 100)
  @Builder.Default
  private String backgroundImage = "";

  @Column(nullable = false)
  @ColumnDefault("0")
  @Builder.Default
  private Long point = 0L;

  @Builder
  public MemberProfile(Long id, String name, String info, Role role, String thumbnail,
      String backgroundImage, Long point) {
    this.id = id;
    this.name = name;
    this.info = info;
    this.role = role;
    this.thumbnail = thumbnail;
    this.backgroundImage = backgroundImage;
    this.point = point;
  }

  public void setMember(Member member) {
    this.member = member;
  }
}
