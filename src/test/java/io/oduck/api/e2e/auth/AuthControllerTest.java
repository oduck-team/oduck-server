package io.oduck.api.e2e.auth;

import static io.oduck.api.global.config.RestDocsConfig.field;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.snippet.Attributes.attributes;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import io.oduck.api.domain.member.entity.LoginType;
import io.oduck.api.domain.member.entity.Role;
import io.oduck.api.global.initializer.TestDataInitializer;
import io.oduck.api.global.MockMember.WithCustomMockMember;
import io.oduck.api.global.security.auth.dto.AuthUser;
import io.oduck.api.global.security.auth.dto.LocalAuthDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
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
                        preprocessResponse(prettyPrint()),
                        requestFields(
                            attributes(key("title")
                                .value("Fields for member creation")),
                            fieldWithPath("email")
                                .type(JsonFieldType.STRING)
                                .attributes(field("constraints", "이메일 형식"))
                                .description("로그인에 필요한 이메일"),
                            fieldWithPath("password")
                                .type(JsonFieldType.STRING)
                                .attributes(field("constraints",
                                    "숫자, 영문대소문자, 특수문자(!@#$%^&*()-_=+)를 포함한 8~20자리"))
                                .description("로그인에 필요한 비밀 번호")),
                        responseHeaders(
                            headerWithName(HttpHeaders.LOCATION) // 헤더 이름
                                .description("Header Location, 리소스의 URL"),
                            headerWithName(HttpHeaders.SET_COOKIE) // 헤더 이름
                                .description("Header Set-Cookie, 세션 쿠키")
                        )
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
            ResultActions actions = mockMvc.perform(
                get(BASE_URL + "/status")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.COOKIE, "oDuckio.sid={SESSION_VALUE}")
            );

            // then
            actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.thumbnail").exists())
                .andExpect(jsonPath("$.point").exists())
                .andDo(
                    document("getStatus/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                            headerWithName(HttpHeaders.COOKIE)
                                .attributes(field("constraints", "oDuckio.sid={SESSION_VALUE}"))
                                .description("Header Cookie, 세션 쿠키")
                        ),
                        responseFields(
                            fieldWithPath("name")
                                .type(JsonFieldType.STRING)
                                .description("회원 이름"),
                            fieldWithPath("description")
                                .type(JsonFieldType.STRING)
                                .description("자기 소개"),
                            fieldWithPath("thumbnail")
                                .type(JsonFieldType.STRING)
                                .description("프로필 이미지"),
                            fieldWithPath("point")
                                .type(JsonFieldType.NUMBER)
                                .description("회원 포인트")
                        )
                    )
                );
        }
    }
}
