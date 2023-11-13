package io.oduck.api.unit.bookmark.repository;

import static io.oduck.api.global.utils.PagingUtils.applyPageableForNonOffset;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.oduck.api.domain.bookmark.dto.BookmarkDslDto.BookmarkDsl;
import io.oduck.api.domain.bookmark.repository.BookmarkRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class BookmarkRepositoryTest {
    @Autowired
    BookmarkRepository bookmarkRepository;

    @DisplayName("회원 북마크 애니 목록 조회")
    @Nested
    class selectBookmarks {

        @DisplayName("회원 ID로 회원이 북마크한 애니 목록 조회 성공")
        @Test
        void selectBookmarksSuccess() {
            // given
            Long memberId = 1L;
            Pageable pageable = applyPageableForNonOffset(10, "createdAt", "desc");

            // when
            Slice<BookmarkDsl> bookmarks = bookmarkRepository.selectBookmarks(memberId, null, pageable);

            assertNotNull(bookmarks);
            assertNotNull(bookmarks.getContent().get(0).getAnimeId());
            assertNotNull(bookmarks.getContent().get(0).getTitle());
            assertNotNull(bookmarks.getContent().get(0).getThumbnail());
        }
    }

    @DisplayName("회원 북마크 애니 갯수 조회")
    @Nested
    class selectBookmarkCount {
        @DisplayName("회원 ID로 회원이 북마크한 애니 갯수 조회 성공")
        @Test
        void selectBookmarkCountSuccess() {
            // given
            Long memberId = 1L;

            // when
            Long bookmarkCount = bookmarkRepository.countByMemberId(memberId);

            // then
            assertNotNull(bookmarkCount);
            assertEquals(3L, bookmarkCount);
        }
    }
}
