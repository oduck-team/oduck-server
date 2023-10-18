package io.oduck.api.domain.bookmark.service;

import io.oduck.api.domain.bookmark.dto.BookmarkResDto.BookmarkRes;
import java.util.List;

public interface BookmarkService {

    List<BookmarkRes> getBookmarksByMemberId(Long memberId);

}
