package io.oduck.api.domain.member.controller;

import io.oduck.api.domain.bookmark.dto.BookmarkResDto.BookmarkRes;
import io.oduck.api.domain.bookmark.service.BookmarkService;
import io.oduck.api.domain.member.dto.MemberReqDto.CreateReq;
import io.oduck.api.domain.member.dto.MemberReqDto.PatchReq;
import io.oduck.api.domain.bookmark.dto.BookmarkReqDto.Sort;
import io.oduck.api.domain.member.dto.MemberResDto.MemberProfileRes;
import io.oduck.api.domain.member.service.MemberService;
import io.oduck.api.global.common.OrderDirection;
import io.oduck.api.global.common.SliceResponse;
import io.oduck.api.global.security.auth.dto.AuthUser;
import io.oduck.api.global.security.auth.dto.LoginUser;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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

    @GetMapping("/{id}/bookmarks")
    public ResponseEntity<?> getBookmaks(
        @PathVariable("id") Long id,
        @RequestParam(required = false) String cursor,
        @RequestParam(required = false, defaultValue = "latest") Sort sort,
        @RequestParam(required = false, defaultValue = "DESC") OrderDirection order,
        @RequestParam(required = false, defaultValue = "10") @Min(1) @Max(100) int size
        ) {
        // TODO: slice 및 정렬 구현
        SliceResponse<BookmarkRes> res = bookmarkService.getBookmarksByMemberId(id, cursor, sort, order, size);
        return ResponseEntity.ok(res);
    }
//
//    @GetMapping("/{name}/short-reviews")
//    public ResponseEntity<?> getShortReviews(
//        @LoginUser AuthUser user
//    ) {
//        return ResponseEntity.ok(SliceResponse.of());
//    }
}
