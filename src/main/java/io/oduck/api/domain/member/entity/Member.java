package io.oduck.api.domain.member.entity;

import io.oduck.api.domain.attractionPoint.entity.AttractionPoint;
import io.oduck.api.domain.bookmark.entity.Bookmark;
import io.oduck.api.domain.review.entity.ShortReview;
import io.oduck.api.domain.reviewLike.entity.ShortReviewLike;
import io.oduck.api.domain.starRating.entity.StarRating;
import io.oduck.api.global.audit.BaseEntity;
import io.oduck.api.global.security.auth.entity.AuthLocal;
import io.oduck.api.global.security.auth.entity.AuthSocial;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  private LoginType loginType;

  @Enumerated(EnumType.STRING)
  @Builder.Default
  private Role role = Role.MEMBER;

  @OneToOne(mappedBy = "member", cascade = CascadeType.PERSIST)
  private AuthSocial authSocial;

  @OneToOne(mappedBy = "member", cascade = CascadeType.PERSIST)
  private AuthLocal authLocal;

  @OneToOne(mappedBy = "member", cascade = CascadeType.PERSIST)
  private MemberProfile memberProfile;

  @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST)
  private List<Bookmark> bookMarks;

  @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST)
  private List<ShortReview> shortReviews;

  @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST)
  private List<ShortReviewLike> shortReviewLikes;

  @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST)
  private List<StarRating> starRatings;

  @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST)
  private List<AttractionPoint> attractionPoints;

  @Builder
  public Member(Long id, Role role, LoginType loginType, AuthSocial authSocial,  AuthLocal authLocal, MemberProfile memberProfile) {
    this.id = id;
    this.role = role;
    this.loginType = loginType;
    this.authSocial = authSocial;
    this.authLocal = authLocal;
    this.memberProfile = memberProfile;
  }

  // TODO: set 말고 다른 이름으로 변경하기
  public void relateAuthSocial(AuthSocial authSocial) {
    this.authSocial = authSocial;
    if(authSocial != null) {
      authSocial.relateMember(this);
    }
  }

  public void relateAuthLocal(AuthLocal authLocal) {
    this.authLocal = authLocal;
    if(authLocal != null) {
      authLocal.relateMember(this);
    }
  }

  public void relateMemberProfile(MemberProfile memberProfile) {
    this.memberProfile = memberProfile;
    if(memberProfile != null) {
      memberProfile.relateMember(this);
    }
  }

  public void delete() {
    this.deletedAt = LocalDateTime.now();
    this.memberProfile.delete();

    if(this.authLocal != null) {
      this.authLocal.delete();
    }

    if (this.authSocial != null) {
      this.authSocial.delete();
    }

    this.role = Role.WITHDRAWAL;
  }
}
