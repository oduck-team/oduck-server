package io.oduck.api.domain.member.controller;

import io.oduck.api.domain.member.dto.MemberReqDto.CreateReq;
import io.oduck.api.domain.member.dto.MemberReqDto.PatchReq;
import io.oduck.api.domain.member.dto.MemberResDto.MemberProfileRes;
import io.oduck.api.domain.member.service.MemberService;
import io.oduck.api.global.common.SingleResponse;
import io.oduck.api.global.security.auth.dto.AuthUser;
import io.oduck.api.global.security.auth.dto.LoginUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RequestMapping("/members")
@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;

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
        MemberProfileRes res = memberService.getProfileByName(name);

        return ResponseEntity.ok(res);
    }

    // 회원 프로필 수정
    @PatchMapping
    public ResponseEntity<?> patchProfile(
            @RequestBody @Valid PatchReq body
    // TODO: 인증 정보 추가
    ) {
        // TODO: 회원 정보 수정 로직 구현
        // memberService.updateProfile(body);

        return ResponseEntity.noContent().build();
    }

    // 회원이 작성한 리뷰 목록

    // 회원 북마크 애니 목록
}
