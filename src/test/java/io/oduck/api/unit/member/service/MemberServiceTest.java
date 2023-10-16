package io.oduck.api.unit.member.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import io.oduck.api.domain.member.dto.MemberDslDto.ProfileWithoutActivity;
import io.oduck.api.domain.member.dto.MemberResDto.MemberProfileRes;
import io.oduck.api.domain.member.entity.Member;
import io.oduck.api.domain.member.entity.MemberProfile;
import io.oduck.api.domain.member.repository.MemberRepository;
import io.oduck.api.domain.member.service.MemberServiceImpl;
import io.oduck.api.global.exception.NotFoundException;
import io.oduck.api.global.stub.MemberStub;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    @InjectMocks
    private MemberServiceImpl memberService;

    @Mock
    MemberRepository memberRepository;

    // TODO: 회원 가입
    // TODO: 회원 가입 실패(이메일 중복, 비밀번호 유효성 등)

    @Nested
    @DisplayName("회원 프로필 조회")
    class GetProfileByName {
        // given
        Member member = new MemberStub().getMember();
        MemberProfile memberProfile = member.getMemberProfile();
        String name = memberProfile.getName();

        ProfileWithoutActivity profileWithoutActivity = ProfileWithoutActivity.builder()
            .memberId(1L)
            .name(name)
            .description(memberProfile.getInfo())
            .thumbnail(memberProfile.getThumbnail())
            .backgroundImage(memberProfile.getBackgroundImage())
            .build();

        @DisplayName("본인 프로필 조회 성공시 오류 없이 회원 프로필 반환")
        @Test
        void getMemberByNameIfMine() {
            // given
            given(memberRepository.selectProfileByName(anyString())).willReturn(Optional.ofNullable(profileWithoutActivity));
            given(memberRepository.countReviewsByMemberId(anyLong())).willReturn(1L);
            given(memberRepository.countLikesByMemberId(anyLong())).willReturn(1L);

            // when
            MemberProfileRes res = memberService.getProfileByName(name, 1L);

            // then
            assertDoesNotThrow(() -> memberService.getProfileByName(name, 1L)); // 오류 없이 회원 프로필 반환
            assertEquals(res.getName(), name);                              // 회원 프로필의 이름이 요청한 이름과 같은지 확인
            assertTrue(res.getIsMine());
            assertNotNull(res.getActivity());                               // 회원 프로필의 활동 정보가 null이 아닌지 확인
        }

        @DisplayName("타 회원 프로필 조회 성공시 오류 없이 회원 프로필 반환, isMine = false")
        @Test
        void getMemberByNameIfOthers() {
            // given
            given(memberRepository.selectProfileByName(anyString())).willReturn(Optional.ofNullable(profileWithoutActivity));
            given(memberRepository.countReviewsByMemberId(anyLong())).willReturn(1L);
            given(memberRepository.countLikesByMemberId(anyLong())).willReturn(1L);

            // when
            MemberProfileRes res = memberService.getProfileByName(name, 2L);

            // then
            assertDoesNotThrow(() -> memberService.getProfileByName(name, 1L)); // 오류 없이 회원 프로필 반환
            assertEquals(res.getName(), name);                              // 회원 프로필의 이름이 요청한 이름과 같은지 확인
            assertFalse(res.getIsMine());
            assertNotNull(res.getActivity());                               // 회원 프로필의 활동 정보가 null이 아닌지 확인
        }

        @DisplayName("존재하지 않는 회원 프로필 조회시 NotFoundException 발생")
        @Test
        void getMemberByNameIfNotFound() {
            // given
            String notExistName = "notExistName";
            given(memberRepository.selectProfileByName(anyString())).willReturn(Optional.empty());

            // when
            // then
            assertThrows(NotFoundException.class,
                () -> memberService.getProfileByName(notExistName, 1L)
            );
        }
    }

    // TODO: 회원 정보 수정
    // TODO: 회원 정보 수정 실패(닉네임 중복 등)

    // TODO: 회원이 작성한 리뷰 목록

    // TODO: 회원 북마크 애니 목록
}
