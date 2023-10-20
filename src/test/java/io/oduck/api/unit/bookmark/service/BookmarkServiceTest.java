package io.oduck.api.unit.bookmark.service;

import static io.oduck.api.global.utils.PagingUtils.applyPageableForNonOffset;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import io.oduck.api.domain.bookmark.dto.BookmarkDslDto.BookmarkDsl;
import io.oduck.api.domain.bookmark.dto.BookmarkReqDto.Sort;
import io.oduck.api.domain.bookmark.dto.BookmarkResDto.BookmarkRes;
import io.oduck.api.domain.bookmark.repository.BookmarkRepository;
import io.oduck.api.domain.bookmark.service.BookmarkServiceImpl;
import io.oduck.api.global.common.OrderDirection;
import io.oduck.api.global.common.SliceResponse;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

@ExtendWith(MockitoExtension.class)
public class BookmarkServiceTest {
    @InjectMocks
    private BookmarkServiceImpl bookmarkService;

    @Mock
    BookmarkRepository bookmarkRepository;

    @DisplayName("회원의 북마크 목록 조회")
    @Nested
    class GetBookmarksByMemberId {
        @DisplayName("회원의 북마크 목록 조회 성공")
        @Test
        void getBookmarksByMemberIdSuccess() {
            Long memberId = 1L;
            String cursor = null;
            Sort sort = Sort.LATEST;
            OrderDirection order = OrderDirection.ASC;
            int size = 10;

            List<BookmarkDsl> sampleBookmarks = new ArrayList<>();

            Slice<BookmarkDsl> sampleSlice = new SliceImpl<>(sampleBookmarks);

            when(bookmarkRepository.selectBookmarks(
                memberId,
                cursor,
                applyPageableForNonOffset(sort.getSort(), order.getOrder(), size)
            )).thenReturn(sampleSlice);

            SliceResponse<BookmarkRes> result = bookmarkService.getBookmarksByMemberId(memberId, cursor, sort, order, size);

            assertEquals(sampleSlice.getSize(), result.getItems().size());
            assertNotNull(result.getLastId());
            assertFalse(result.isHasNext());
        }
    }
}
