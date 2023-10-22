package io.oduck.api.domain.bookmark.service;

import static io.oduck.api.global.utils.PagingUtils.applyPageableForNonOffset;

import io.oduck.api.domain.anime.entity.Anime;
import io.oduck.api.domain.anime.repository.AnimeRepository;
import io.oduck.api.domain.bookmark.dto.BookmarkDslDto.BookmarkDsl;
import io.oduck.api.domain.bookmark.dto.BookmarkReqDto;
import io.oduck.api.domain.bookmark.dto.BookmarkResDto.BookmarkRes;
import io.oduck.api.domain.bookmark.dto.BookmarkResDto.BookmarkedDateTimeRes;
import io.oduck.api.domain.bookmark.entity.Bookmark;
import io.oduck.api.domain.bookmark.repository.BookmarkRepository;
import io.oduck.api.domain.member.entity.Member;
import io.oduck.api.domain.member.repository.MemberRepository;
import io.oduck.api.global.common.OrderDirection;
import io.oduck.api.global.common.SliceResponse;
import io.oduck.api.global.exception.NotFoundException;
import java.util.List;
import java.util.Optional;
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
    private final MemberRepository memberRepository;
    private final AnimeRepository animeRepository;

    @Override
    public boolean toggleBookmark(Long memberId, Long animeId) {
        Optional<Bookmark> optionalBookmark = getBookmark(memberId, animeId);

        if (optionalBookmark.isPresent()) {
            bookmarkRepository.delete(optionalBookmark.get());
            return false;
        }

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 회원입니다."));

        Anime anime = animeRepository.findById(animeId)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 애니메이션입니다."));

        bookmarkRepository.save(
            Bookmark.builder()
                .member(member)
                .anime(anime)
                .build()
        );
        return true;
    }

    public BookmarkedDateTimeRes checkBookmarked(Long memberId, Long animeId) {
        Optional<Bookmark> optionalBookmark = getBookmark(memberId, animeId);

        if (optionalBookmark.isPresent()) {
            return BookmarkedDateTimeRes.builder()
                .createdAt(optionalBookmark.get().getCreatedAt().toString())
                .build();
        }

        throw new NotFoundException("bookmark");
    }

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

    private Optional<Bookmark> getBookmark(Long memberId, Long animeId) {
        return bookmarkRepository.findByMemberIdAndAnimeId(memberId, animeId);
    }
}
