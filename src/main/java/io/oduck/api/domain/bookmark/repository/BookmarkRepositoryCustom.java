package io.oduck.api.domain.bookmark.repository;

import static io.oduck.api.domain.anime.entity.QAnime.anime;
import static io.oduck.api.domain.bookmark.entity.QBookmark.bookmark;
import static io.oduck.api.domain.member.entity.QMember.member;
import static io.oduck.api.domain.starRating.entity.QStarRating.starRating;

import com.querydsl.core.types.Projections;
import io.oduck.api.domain.bookmark.dto.BookmarkDslDto.BookmarkDsl;
import java.util.List;

public interface BookmarkRepositoryCustom {
    List<BookmarkDsl> selectBookmarks(Long memberId) ;
}
