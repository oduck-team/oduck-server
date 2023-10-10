package io.oduck.api.e2e.member;

import static io.oduck.api.global.config.RestDocsConfig.field;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.attributes;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import io.oduck.api.domain.member.dto.MemberReqDto.CreateReq;
import io.oduck.api.domain.member.dto.MemberReqDto.PatchReq;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@AutoConfigureRestDocs
@AutoConfigureMockMvc
@ExtendWith({ RestDocumentationExtension.class, SpringExtension.class })
@SpringBootTest
@ActiveProfiles("test")
public class MemberControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    private final String BASE_URL = "/members";

    @Nested
    @DisplayName("회원 가입")
    class PostMembers {

        @DisplayName("회원 가입 성공시 200 OK 반환")
        @Test
        void postMember() throws Exception {
            // given
            // TODO: 회원 가입에 필요한 데이터
            CreateReq body = CreateReq.builder()
                    .email("john@gmail.com")
                    .password("Qwer1234!")
                    .build();

            String content = gson.toJson(body);

            // when
            // 요청 실행
            ResultActions actions = mockMvc.perform(
                    post("/members")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(content)
            );

            // then
            // 응답 결과 검증 후 문서화
            actions
                    .andExpect(status().isCreated())
                    .andDo(document("postMember/success",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            requestFields(
                                    attributes(key("title")
                                            .value("Fields for member creation")),
                                    fieldWithPath("email")
                                            .type(JsonFieldType.STRING)
                                            .attributes(field("constraints", "이메일 형식"))
                                            .description("회원 가입에 필요한 이메일"),
                                    fieldWithPath("password")
                                            .type(JsonFieldType.STRING)
                                            .attributes(field("constraints",
                                                    "숫자, 영문대소문자, 특수문자(!@#$%^&*()-_=+)를 포함한 8~20자리"))
                                            .description("회원 가입에 필요한 비밀 번호"))));
        }

        // TODO: 회원 가입 실패시
    }

    @Nested
    @DisplayName("회원 프로필 조회")
    class GetProfileByName {

        @DisplayName("회원 이름으로 프로필 조회 성공시 200 OK 반환")
        @Test
        void getProfileByName() throws Exception {
            // given
            // 회원 프로필 조회에 필요한 데이터
            String name = "이름";

            // when
            // 요청 실행
            ResultActions actions = mockMvc.perform(
                    get("/members" + "/{name}", name)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.COOKIE, "oDuckio.sid={SESSION_VALUE}")
            );

            // then
            // 응답 결과 검증 후 문서화
            actions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.item.name").exists())
                    .andExpect(jsonPath("$.item.isMine").exists())
                    .andExpect(jsonPath("$.item.description").exists())
                    .andExpect(jsonPath("$.item.thumbnail").exists())
                    .andExpect(jsonPath("$.item.backgroundImage").exists())
                    .andExpect(jsonPath("$.item.point").exists())
                    .andExpect(jsonPath("$.item.activity").hasJsonPath())
                    .andExpect(jsonPath("$.item.activity.reviews").exists())
                    .andExpect(jsonPath("$.item.activity.threads").exists())
                    .andExpect(jsonPath("$.item.activity.likes").exists())
                    .andDo(document("getProfileByName/success",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            pathParameters(
                                    parameterWithName("name")
                                            .description("회원 이름")),
                            requestHeaders(
                                headerWithName(HttpHeaders.COOKIE)
                                    .attributes(field("constraints", "oDuckio.sid={SESSION_VALUE}"))
                                    .optional()
                                    .description("Header Cookie, 세션 쿠키")
                            ),
                            responseFields(
                                    fieldWithPath("item")
                                            .type(JsonFieldType.OBJECT)
                                            .description("조회 데이터"),
                                    fieldWithPath("item.name")
                                            .type(JsonFieldType.STRING)
                                            .description("회원 이름"),
                                    fieldWithPath("item.isMine")
                                            .type(JsonFieldType.BOOLEAN)
                                            .description("본인 여부(본인 프로필 조회시 true)"),
                                    fieldWithPath("item.description")
                                            .type(JsonFieldType.STRING)
                                            .description("자기 소개"),
                                    fieldWithPath("item.thumbnail")
                                            .type(JsonFieldType.STRING)
                                            .description("프로필 이미지"),
                                    fieldWithPath("item.backgroundImage")
                                            .type(JsonFieldType.STRING)
                                            .description("프로필 배경 이미지"),
                                    fieldWithPath("item.point")
                                            .type(JsonFieldType.NUMBER)
                                            .description("회원 포인트"),
                                    fieldWithPath("item.activity")
                                            .type(JsonFieldType.OBJECT)
                                            .description("회원 활동"),
                                    fieldWithPath("item.activity.reviews")
                                            .type(JsonFieldType.NUMBER)
                                            .description("작성한 리뷰 갯수"),
                                    fieldWithPath("item.activity.threads")
                                            .type(JsonFieldType.NUMBER)
                                            .description("작성한 쓰레드 갯수"),
                                    fieldWithPath("item.activity.likes")
                                            .type(JsonFieldType.NUMBER)
                                            .description("받은 좋아요 갯수"))));
        }

        // TODO: 회원 프로필 조회 실패시
    }

    @Nested
    @DisplayName("회원 정보 수정")
    class PatchProfile {

        @DisplayName("회원 가입 성공시 201 Created 반환")
        @Test
        void patchProfile() throws Exception {
            // given
            // 회원 정보 수정에 필요한 데이터
            PatchReq body = PatchReq.builder()
                    .name("bob")
                    .description("hello, world!")
                    .build();

            String content = gson.toJson(body);

            // when
            ResultActions actions = mockMvc.perform(
                    patch("/members")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(content));

            // then
            actions
                    .andExpect(status().isNoContent())
                    .andDo(document("patchProfile/success",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            requestFields(
                                    attributes(key("title")
                                            .value("Fields for member creation")),
                                    fieldWithPath("name")
                                            .type(JsonFieldType.STRING)
                                            .attributes(
                                                    field("constraints", "한,영소문자, 숫자 포함 2-10자. ^[0-9A-Za-z가-힣]{2,10}$"))
                                            .description("이름 변경시 필요한 이름"),
                                    fieldWithPath("description")
                                            .type(JsonFieldType.STRING)
                                            .attributes(field("constraints", "문자열 0-100자"))
                                            .description("자기 소개 변경시 필요한 내용"))));
        }

        // TODO: 회원 프로필 수정 실패시
    }
}
