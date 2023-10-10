package io.oduck.api.e2e.auth;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import io.oduck.api.domain.member.entity.Role;
import io.oduck.api.global.initializer.TestDataInitializer;
import io.oduck.api.global.MockMember.WithCustomMockMember;
import io.oduck.api.global.security.auth.dto.LocalAuthDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@AutoConfigureRestDocs
@AutoConfigureMockMvc
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest
@ActiveProfiles("test")
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    private final String BASE_URL = "/auth";

    @BeforeAll
    public static void setUp(
        @Autowired TestDataInitializer testDataInitializer) {
        testDataInitializer.saveTestMember();
    }

    @Nested
    @DisplayName("로컬 회원 인증")
    class PostLogin {

        @DisplayName("회원 인증 성공시 302 Redirect 및 세션 생성")
        @Test
        void postLogin() throws Exception {
            // given
            LocalAuthDto localAuthDto = LocalAuthDto.builder()
                .email("bob@gmail.com")
                .password("Qwer!234")
                .build();

            String content = gson.toJson(localAuthDto);

            // when
            ResultActions actions = mockMvc.perform(post(BASE_URL + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content)
            );

            // then
            actions
                .andExpect(status().is3xxRedirection())
                .andDo(
                    document("postLogin/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                    )
                );
        }
    }

    @Nested
    @DisplayName("현재 인증 상태")
    class GetStatus {

        @DisplayName("인증된 회원일시 200 OK 및 회원 정보 반환")
        @Test
        @WithCustomMockMember(id = 1L, email = "bob", password = "Qwer!234", role = Role.MEMBER)
        void getAuthStatus() throws Exception {
            // given

            // when
            ResultActions actions = mockMvc.perform(get(BASE_URL + "/status")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
            );

            // then
            actions.andExpect(status().isOk())
                    .andDo(
                        document("getStatus/success",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            responseFields(
                                fieldWithPath("item")
                                    .type(JsonFieldType.OBJECT)
                                    .description("조회 데이터"),
                                fieldWithPath("item.name")
                                    .type(JsonFieldType.STRING)
                                    .description("회원 이름"),
                                fieldWithPath("item.description")
                                    .type(JsonFieldType.STRING)
                                    .description("자기 소개"),
                                fieldWithPath("item.thumbnail")
                                    .type(JsonFieldType.STRING)
                                    .description("프로필 이미지"),
                                fieldWithPath("item.point")
                                    .type(JsonFieldType.NUMBER)
                                    .description("회원 포인트")
                            )
                        )
                    );
        }
    }
}
