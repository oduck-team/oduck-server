package io.oduck.api.domain.bookmark.repository;

import io.oduck.api.domain.bookmark.entity.Bookmark;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark,Long>, BookmarkRepositoryCustom {
    Long countByMemberId(Long memberId);
    Optional<Bookmark> findByMemberIdAndAnimeId(Long memberId, Long animeId);

}
