package io.oduck.api.domain.review.entity;

import io.oduck.api.domain.anime.entity.Anime;
import io.oduck.api.domain.member.entity.Member;
import io.oduck.api.domain.reviewLike.entity.ShortReviewLike;
import io.oduck.api.global.audit.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class ShortReview extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "anime_id")
  private Anime anime;

  @Column(nullable = false, length = 100)
  private String content;

  @Column(nullable = false)
  private boolean hasSpoiler;

  @OneToMany(mappedBy = "shortReview", cascade = CascadeType.PERSIST)
  private List<ShortReviewLike> shortReviewLikes;
}
