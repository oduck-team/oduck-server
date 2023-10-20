package io.oduck.api.domain.bookmark.service;

import io.oduck.api.domain.bookmark.dto.BookmarkResDto.BookmarkRes;
import io.oduck.api.domain.bookmark.dto.BookmarkReqDto.Sort;
import io.oduck.api.global.common.OrderDirection;
import io.oduck.api.global.common.SliceResponse;

public interface BookmarkService {

    SliceResponse<BookmarkRes> getBookmarksByMemberId(Long memberId, String cursor, Sort sort, OrderDirection order, int size);

}
