package io.oduck.api.domain.bookmark.service;

import io.oduck.api.domain.bookmark.dto.BookmarkResDto.BookmarkCountRes;
import io.oduck.api.domain.bookmark.dto.BookmarkResDto.BookmarkRes;
import io.oduck.api.domain.bookmark.dto.BookmarkReqDto.Sort;
import io.oduck.api.domain.bookmark.dto.BookmarkResDto.BookmarkedDateTimeRes;
import io.oduck.api.global.common.OrderDirection;
import io.oduck.api.global.common.SliceResponse;

public interface BookmarkService {
    boolean toggleBookmark(Long memberId, Long animeId);
    BookmarkedDateTimeRes checkBookmarked(Long memberId, Long animeId);
    BookmarkCountRes getBookmarksCountByMemberId(Long memberId);
    SliceResponse<BookmarkRes> getBookmarksByMemberId(Long memberId, String cursor, Sort sort, OrderDirection order, int size);

}
