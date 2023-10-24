package io.oduck.api.unit.bookmark.service;

import static io.oduck.api.global.utils.PagingUtils.applyPageableForNonOffset;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import io.oduck.api.domain.anime.entity.Anime;
import io.oduck.api.domain.anime.repository.AnimeRepository;
import io.oduck.api.domain.bookmark.dto.BookmarkDslDto.BookmarkDsl;
import io.oduck.api.domain.bookmark.dto.BookmarkReqDto.Sort;
import io.oduck.api.domain.bookmark.dto.BookmarkResDto.BookmarkRes;
import io.oduck.api.domain.bookmark.dto.BookmarkResDto.BookmarkedDateTimeRes;
import io.oduck.api.domain.bookmark.entity.Bookmark;
import io.oduck.api.domain.bookmark.repository.BookmarkRepository;
import io.oduck.api.domain.bookmark.service.BookmarkServiceImpl;
import io.oduck.api.domain.member.entity.Member;
import io.oduck.api.domain.member.repository.MemberRepository;
import io.oduck.api.global.common.OrderDirection;
import io.oduck.api.global.common.SliceResponse;
import io.oduck.api.global.config.QueryDslConfig;
import io.oduck.api.global.exception.NotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

@ExtendWith(MockitoExtension.class)
@Import(QueryDslConfig.class)
public class BookmarkServiceTest {
    @InjectMocks
    private BookmarkServiceImpl bookmarkService;

    @Mock
    MemberRepository memberRepository;

    @Mock
    AnimeRepository animeRepository;

    @Mock
    BookmarkRepository bookmarkRepository;

    @DisplayName("북마크 토글")
    @Nested
    class ToggleBookmark {
        Member member = Member.builder()
            .id(1L)
            .build();
        Anime anime = Anime.builder()
            .id(1L)
            .build();

        Bookmark bookmark = Bookmark.builder()
            .member(member)
            .anime(anime)
            .build();
        @DisplayName("북마크 저장 성공")
        @Test
        void toggleBookmarkSaveSuccess() {
            given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
            given(animeRepository.findById(anyLong())).willReturn(Optional.of(anime));
            given(bookmarkRepository.findByMemberIdAndAnimeId(anyLong(), anyLong()))
                .willReturn(Optional.empty());
            given(bookmarkRepository.save(any(Bookmark.class))).willReturn(bookmark);

            boolean result = bookmarkService.toggleBookmark(member.getId(), anime.getId());

            assertDoesNotThrow(() -> bookmarkService.toggleBookmark(member.getId(), anime.getId()));
            assertTrue(result);
        }

        @DisplayName("북마크 삭제 성공")
        @Test
        void toggleBookmarkDeleteSuccess() {
            given(bookmarkRepository.findByMemberIdAndAnimeId(anyLong(), anyLong()))
                .willReturn(Optional.of(bookmark));
            doNothing().when(bookmarkRepository).delete(any(Bookmark.class));

            boolean result = bookmarkService.toggleBookmark(member.getId(), anime.getId());

            assertDoesNotThrow(() -> bookmarkService.toggleBookmark(member.getId(), anime.getId()));
            assertFalse(result);
        }
    }

    @DisplayName("회원의 북마크 목록 조회")
    @Nested
    class GetBookmarksByMemberId {
        @DisplayName("회원의 북마크 목록 조회 성공")
        @Test
        void getBookmarksByMemberIdSuccess() {
            Long memberId = 1L;
            String cursor = null;
            Sort sort = Sort.CREATED_AT;
            OrderDirection order = OrderDirection.ASC;
            int size = 10;

            List<BookmarkDsl> sampleBookmarks = new ArrayList<>();

            Slice<BookmarkDsl> sampleSlice = new SliceImpl<>(sampleBookmarks);

            when(bookmarkRepository.selectBookmarks(
                memberId,
                cursor,
                applyPageableForNonOffset(size, sort.getSort(), order.getOrder())
            )).thenReturn(sampleSlice);

            SliceResponse<BookmarkRes> result = bookmarkService.getBookmarksByMemberId(memberId, cursor, sort, order, size);

            assertEquals(sampleSlice.getSize(), result.getItems().size());
            assertNotNull(result.getCursor());
            assertFalse(result.isHasNext());
        }
    }

    @DisplayName("북마크 체크")
    @Nested
    class checkBookmarked {
        @DisplayName("북마크 체크시 북마크가 존재할 경우")
        @Test
        void checkBookmarkedExist() {
            // given
            Long memberId = 1L;
            Long animeId = 1L;

            Bookmark bookmark = Bookmark.builder()
                .member(Member.builder().id(memberId).build())
                .anime(Anime.builder().id(animeId).build())
                .createdAt(LocalDateTime.now())
                .build();

            given(bookmarkRepository.findByMemberIdAndAnimeId(memberId, animeId))
                .willReturn(Optional.of(bookmark));

            // when
            BookmarkedDateTimeRes result = bookmarkService.checkBookmarked(memberId, animeId);

            // then
            assertNotNull(result);
            assertNotNull(result.getCreatedAt());
        }

        @DisplayName("북마크 체크시 북마크가 존재하지 않을 경우")
        @Test
        void checkBookmarkedNotExist() {
            // given
            Long memberId = 1L;
            Long animeId = 1L;

            given(bookmarkRepository.findByMemberIdAndAnimeId(memberId, animeId))
                .willReturn(Optional.empty());

            // when
            // then
            assertThrows(NotFoundException.class,
                () -> bookmarkService.checkBookmarked(memberId, animeId)
            );
        }
    }
}
