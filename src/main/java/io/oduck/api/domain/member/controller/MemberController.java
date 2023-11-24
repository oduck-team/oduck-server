package io.oduck.api.domain.member.controller;

import io.oduck.api.domain.bookmark.dto.BookmarkResDto.BookmarkRes;
import io.oduck.api.domain.bookmark.service.BookmarkService;
import io.oduck.api.domain.member.dto.MemberReqDto.CreateReq;
import io.oduck.api.domain.member.dto.MemberReqDto.PatchReq;
import io.oduck.api.domain.bookmark.dto.BookmarkReqDto.Sort;
import io.oduck.api.domain.member.dto.MemberResDto.MemberProfileRes;
import io.oduck.api.domain.member.service.MemberService;
import io.oduck.api.domain.review.dto.ShortReviewReqDto;
import io.oduck.api.domain.review.dto.ShortReviewResDto.ShortReviewRes;
import io.oduck.api.domain.review.service.ShortReviewService;
import io.oduck.api.global.common.OrderDirection;
import io.oduck.api.global.common.SliceResponse;
import io.oduck.api.global.security.auth.dto.AuthUser;
import io.oduck.api.global.security.auth.dto.LoginUser;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RequestMapping("/members")
@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;
    private final BookmarkService bookmarkService;
    private final ShortReviewService shortReviewService;

    // 로컬 회원 가입
    @PostMapping
    public ResponseEntity<?> PostMember(
            @RequestBody @Valid CreateReq body) {
        // TODO: 회원 가입 로직 구현
        memberService.signUpByLocal(body);

        return ResponseEntity.created(null).build();
    }

    // 이름으로 회원 프로필 조회
    @GetMapping("/{name}")
    public ResponseEntity<MemberProfileRes> getProfileByName(
            @PathVariable("name") String name,
            @LoginUser AuthUser user
    ) {
        // TODO: 회원 프로필 조회 로직 구현
        MemberProfileRes res = memberService.getProfileByName(name, user == null ? 0L : user.getId());

        return ResponseEntity.ok(res);
    }


    @GetMapping("/{id}/bookmarks")
    public ResponseEntity<?> getBookmaks(
        @PathVariable("id") @Positive Long id,
        @RequestParam(required = false) String cursor,
        @RequestParam(required = false, defaultValue = "created_at") Sort sort,
        @RequestParam(required = false, defaultValue = "DESC") OrderDirection order,
        @RequestParam(required = false, defaultValue = "10") @Min(1) @Max(100) int size
        ) {

        SliceResponse<BookmarkRes> res = bookmarkService.getBookmarksByMemberId(id, cursor, sort, order, size);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{id}/bookmarks/count")
    public ResponseEntity<?> getBookmarksCount(
        @PathVariable("id") @Positive Long id
    ) {
        return ResponseEntity.ok(bookmarkService.getBookmarksCountByMemberId(id));
    }

    @GetMapping("/{id}/short-reviews")
    public ResponseEntity<?> getShortReviews(
        @PathVariable("id") @Positive Long id,
        @RequestParam(required = false) String cursor,
        @RequestParam(required = false, defaultValue = "created_at") ShortReviewReqDto.Sort sort,
        @RequestParam(required = false, defaultValue = "DESC") OrderDirection order,
        @RequestParam(required = false, defaultValue = "10") @Min(1) @Max(100) int size
    ) {
        SliceResponse<ShortReviewRes> res = shortReviewService.getShortReviewsByMemberId(id, cursor, sort, order, size);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{id}/short-reviews/count")
    public ResponseEntity<?> getShoertReviewsCount(
        @PathVariable("id") @Positive Long id
    ) {
        return ResponseEntity.ok(shortReviewService.getShortReviewCountByMemberId(id));
    }

    // 회원 프로필 수정
    @PatchMapping
    public ResponseEntity<?> patchProfile(
        @RequestBody @Valid PatchReq body,
        @LoginUser AuthUser user
    ) {
        // TODO: 회원 정보 수정 로직 구현
        memberService.updateProfile(body, user.getId());

        return ResponseEntity.noContent().build();
    }

    // 회원 탈퇴
    @DeleteMapping()
    public ResponseEntity<?> deleteMember(
        @LoginUser AuthUser user
    ) {
        memberService.withdrawMember(user.getId());
        return ResponseEntity.noContent().build();
    }
}
