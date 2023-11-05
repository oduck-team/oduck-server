package io.oduck.api.domain.attractionPoint.entity;

import io.oduck.api.domain.anime.entity.Anime;
import io.oduck.api.domain.member.entity.Member;
import io.oduck.api.global.audit.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class AttractionPoint extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "anime_id")
  private Anime anime;

  @Enumerated(EnumType.STRING)
  private AttractionElement attractionElement;

  @Builder
  public AttractionPoint(Long id, Member member, Anime anime, AttractionElement attractionElement) {
    this.id = id;
    this.member = member;
    this.anime = anime;
    this.attractionElement = attractionElement;
  }

  public void relateMember(Member member){
    this.member = member;
  }
  public void relateAnime(Anime anime){
    this.anime = anime;
  }

  public void updateElement(AttractionElement attractionElement){
    this.attractionElement = attractionElement;
  }
}
