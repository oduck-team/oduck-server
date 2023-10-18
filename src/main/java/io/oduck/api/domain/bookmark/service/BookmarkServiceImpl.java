package io.oduck.api.domain.bookmark.service;

import io.oduck.api.domain.bookmark.dto.BookmarkDslDto.BookmarkDsl;
import io.oduck.api.domain.bookmark.dto.BookmarkResDto.BookmarkRes;
import io.oduck.api.domain.bookmark.repository.BookmarkRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    @Override
    public List<BookmarkRes> getBookmarksByMemberId(Long memberId) {
        List<BookmarkDsl> bookmarks = bookmarkRepository.selectBookmarks(memberId);
        List<BookmarkRes> bookmarkRes = bookmarks.stream()
            .map(BookmarkRes::of)
            .toList();
        return bookmarkRes;
    }
}
