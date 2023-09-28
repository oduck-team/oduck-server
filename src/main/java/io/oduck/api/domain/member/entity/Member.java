package io.oduck.api.domain.member.entity;

import io.oduck.api.domain.bookmark.entity.Bookmark;
import io.oduck.api.domain.review.entity.ShortReview;
import io.oduck.api.domain.reviewLike.entity.ShortReviewLike;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Member {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  private LoginType loginType;

  @OneToOne(mappedBy = "member", cascade = CascadeType.PERSIST)
  private AuthSocial authSocial;

  @OneToOne(mappedBy = "member", cascade = CascadeType.PERSIST)
  private AuthLocal authLocal;

  @OneToOne(mappedBy = "member", cascade = CascadeType.PERSIST)
  private MemberProfile memberProfile;

  @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST)
  private Set<Bookmark> bookMarks;

  @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST)
  private Set<ShortReview> shortReviews;

  @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST)
  private Set<ShortReviewLike> shortReviewLikes;
}
