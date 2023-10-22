package io.oduck.api.domain.bookmark.service;

import static io.oduck.api.global.utils.PagingUtils.applyPageableForNonOffset;

import io.oduck.api.domain.bookmark.dto.BookmarkDslDto.BookmarkDsl;
import io.oduck.api.domain.bookmark.dto.BookmarkReqDto;
import io.oduck.api.domain.bookmark.dto.BookmarkResDto.BookmarkRes;
import io.oduck.api.domain.bookmark.repository.BookmarkRepository;
import io.oduck.api.global.common.OrderDirection;
import io.oduck.api.global.common.SliceResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {
    private final BookmarkRepository bookmarkRepository;

    @Override
    public SliceResponse<BookmarkRes> getBookmarksByMemberId(Long memberId, String cursor, BookmarkReqDto.Sort sort, OrderDirection order, int size) {
        Sort sortList = Sort.by(
            Direction.fromString(order.getOrder()),
            sort.getSort()
        );

        if (sort == BookmarkReqDto.Sort.SCORE) {
            sortList = sortList.and(Sort.by(Direction.DESC, "createdAt"));
        }

        Slice<BookmarkDsl> bookmarks = bookmarkRepository.selectBookmarks(
            memberId,
            cursor,
            applyPageableForNonOffset(
                size,
                sortList
            )
        );

        List<BookmarkRes> bookmarkRes = bookmarks.getContent().stream()
            .map(BookmarkRes::of)
            .toList();

        return SliceResponse.of(bookmarks, bookmarkRes, sort.getSort());
    }
}
