package io.oduck.api.domain.bookmark.repository;

import io.oduck.api.domain.bookmark.dto.BookmarkDslDto.BookmarkDsl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface BookmarkRepositoryCustom {
    Slice<BookmarkDsl> selectBookmarks(Long memberId, String cursor, Pageable pageable);
}
