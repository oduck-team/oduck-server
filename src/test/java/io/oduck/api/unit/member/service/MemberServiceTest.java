package io.oduck.api.unit.member.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.oduck.api.domain.member.dto.MemberResDto.MemberProfileRes;
import io.oduck.api.domain.member.service.MemberServiceStub;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    @InjectMocks
    private MemberServiceStub memberService;

    // TODO: 회원 가입

    @Nested
    @DisplayName("회원 프로필 조회")
    class GetProfileByName {

        @DisplayName("이름으로 회원 프로필 조회 성공시 오류 없이 회원 프로필 반환")
        @Test
        void getMemberByName() {
            // given
            String name = "bob";

            // when
            MemberProfileRes res = memberService.getProfileByName(name);

            // then
            assertDoesNotThrow(() -> memberService.getProfileByName(name)); // 오류 없이 회원 프로필 반환
            assertEquals(res.getName(), name);                              // 회원 프로필의 이름이 요청한 이름과 같은지 확인
        }

        // TODO: 회원 프로필 조회 실패시 오류 반환
    }

    // TODO: 회원 정보 수정
}
