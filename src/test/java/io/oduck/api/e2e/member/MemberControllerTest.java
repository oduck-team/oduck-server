package io.oduck.api.e2e.member;

import static io.oduck.api.global.config.RestDocsConfig.field;
import static org.hamcrest.Matchers.equalTo;
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
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.restdocs.snippet.Attributes.attributes;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import io.oduck.api.domain.member.dto.MemberReqDto.CreateReq;
import io.oduck.api.domain.member.dto.MemberReqDto.PatchReq;
import io.oduck.api.domain.member.entity.Role;
import io.oduck.api.global.mockMember.WithCustomMockMember;
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
                    .email("bob@gmail.com")
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

        @DisplayName("본인 프로필 조회 성공시 200 OK 반환, isMine = true")
        @Test
        @WithCustomMockMember(id = 2L, email = "john", password = "Qwer!234", role = Role.MEMBER)
        void getProfileByNameIfMine() throws Exception {
            // given
            // 회원 프로필 조회에 필요한 데이터
            String name = "john";

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
                    .andExpect(jsonPath("$.isMine").value(true))
                    .andExpect(jsonPath("$.memberId").exists())
                    .andExpect(jsonPath("$.name").exists())
                    .andExpect(jsonPath("$.description").exists())
                    .andExpect(jsonPath("$.thumbnail").exists())
                    .andExpect(jsonPath("$.backgroundImage").value(equalTo(null)))
                    .andExpect(jsonPath("$.activity").hasJsonPath())
                    .andExpect(jsonPath("$.activity.reviews").exists())
                    .andExpect(jsonPath("$.activity.bookmarks").exists())
                    .andExpect(jsonPath("$.activity.likes").exists())
                    .andExpect(jsonPath("$.activity.point").exists())
                    .andDo(document("getProfileByName/successIfMine",
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
                                fieldWithPath("isMine")
                                    .type(JsonFieldType.BOOLEAN)
                                    .description("본인 여부(본인 프로필 조회시 true)"),
                                fieldWithPath("memberId")
                                    .type(JsonFieldType.NUMBER)
                                    .description("회원 id"),
                                fieldWithPath("name")
                                    .type(JsonFieldType.STRING)
                                    .description("회원 이름"),
                                fieldWithPath("description")
                                    .type(JsonFieldType.STRING)
                                    .description("자기 소개"),
                                fieldWithPath("thumbnail")
                                    .type(JsonFieldType.STRING)
                                    .description("프로필 이미지"),
                                fieldWithPath("backgroundImage")
                                    .type(JsonFieldType.NULL)
                                    .description("프로필 배경 이미지"),
                                fieldWithPath("activity")
                                    .type(JsonFieldType.OBJECT)
                                    .description("회원 활동"),
                                fieldWithPath("activity.reviews")
                                    .type(JsonFieldType.NUMBER)
                                    .description("작성한 리뷰 갯수"),
                                fieldWithPath("activity.bookmarks")
                                    .type(JsonFieldType.NUMBER)
                                    .description("입덕 애니 갯수"),
                                fieldWithPath("activity.likes")
                                    .type(JsonFieldType.NUMBER)
                                    .description("받은 좋아요 갯수"),
                                fieldWithPath("activity.point")
                                    .type(JsonFieldType.NUMBER)
                                    .description("회원 포인트")
                            )
                        )
                    );
        }

        @DisplayName("타인 프로필 조회 성공시 200 OK 반환, isMine = false")
        @Test
        @WithCustomMockMember(id = 2L, email = "john", password = "Qwer!234", role = Role.MEMBER)
        void getProfileByNameIfOthers() throws Exception {
            // given
            // 회원 프로필 조회에 필요한 데이터
            String name = "admin";

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
                .andExpect(jsonPath("$.isMine").value(false))
                .andExpect(jsonPath("$.memberId").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.thumbnail").exists())
                .andExpect(jsonPath("$.backgroundImage").value(equalTo(null)))
                .andExpect(jsonPath("$.activity").hasJsonPath())
                .andExpect(jsonPath("$.activity.reviews").exists())
                .andExpect(jsonPath("$.activity.bookmarks").exists())
                .andExpect(jsonPath("$.activity.likes").exists())
                .andExpect(jsonPath("$.activity.point").exists())
                .andDo(document("getProfileByName/successIfOthers",
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
                        fieldWithPath("isMine")
                            .type(JsonFieldType.BOOLEAN)
                            .description("본인 여부(본인 프로필 조회시 true)"),
                        fieldWithPath("memberId")
                            .type(JsonFieldType.NUMBER)
                            .description("회원 id"),
                        fieldWithPath("name")
                            .type(JsonFieldType.STRING)
                            .description("회원 이름"),
                        fieldWithPath("description")
                            .type(JsonFieldType.STRING)
                            .description("자기 소개"),
                        fieldWithPath("thumbnail")
                            .type(JsonFieldType.STRING)
                            .description("프로필 이미지"),
                        fieldWithPath("backgroundImage")
                            .type(JsonFieldType.NULL)
                            .description("프로필 배경 이미지"),
                        fieldWithPath("activity")
                            .type(JsonFieldType.OBJECT)
                            .description("회원 활동"),
                        fieldWithPath("activity.reviews")
                            .type(JsonFieldType.NUMBER)
                            .description("작성한 리뷰 갯수"),
                        fieldWithPath("activity.bookmarks")
                            .type(JsonFieldType.NUMBER)
                            .description("입덕한 애니 갯수"),
                        fieldWithPath("activity.likes")
                            .type(JsonFieldType.NUMBER)
                            .description("받은 좋아요 갯수"),
                        fieldWithPath("activity.point")
                            .type(JsonFieldType.NUMBER)
                            .description("회원 포인트")
                    )
                )
            );
        }

        // TODO: 회원 프로필 조회 실패시

        @DisplayName("존재하지 않는 회원 프로필 조회 성공시 404 NotFound 반환")
        @Test
        @WithCustomMockMember(id = 2L, email = "john", password = "Qwer!234", role = Role.MEMBER)
        void getProfileByName() throws Exception {
            // given
            // 회원 프로필 조회에 필요한 데이터
            String name = "notExistName";

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
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.fieldErrors").value(equalTo(null)))
                .andExpect(jsonPath("$.violationErrors").value(equalTo(null)))
                .andDo(document("getProfileByName/failure",
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
                        fieldWithPath("message")
                            .type(JsonFieldType.STRING)
                            .description("응답 메시지"),
                        fieldWithPath("fieldErrors")
                            .type(JsonFieldType.NULL)
                            .description("api 요청 필드 오류"),
                        fieldWithPath("violationErrors")
                            .type(JsonFieldType.NULL)
                            .description("api 요청 규칙 위반 오류")
                    )
                )
            );
        }
    }

    @Nested
    @DisplayName("회원 정보 수정")
    class PatchProfile {

        @DisplayName("회원 프로필 수정 성공시 204 NoContent 응답")
        @Test
        @WithCustomMockMember(id = 3L, email = "david", password = "Qwer!234", role = Role.MEMBER)
        void patchProfileSuccess() throws Exception {
            // given
            // 회원 정보 수정에 필요한 데이터
            PatchReq body = PatchReq.builder()
                    .name("데이비드")
                    .description("new david description")
                    .build();

            String content = gson.toJson(body);

            // when
            ResultActions actions = mockMvc.perform(
                    patch("/members")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header(HttpHeaders.COOKIE, "oDuckio.sid={SESSION_VALUE}")
                            .content(content));

            // then
            actions
                    .andExpect(status().isNoContent())
                    .andDo(
                        document("patchProfile/success",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            requestHeaders(
                                headerWithName(HttpHeaders.COOKIE)
                                    .attributes(field("constraints", "oDuckio.sid={SESSION_VALUE}"))
                                    .description("Header Cookie, 세션 쿠키")
                            ),
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
                                            .description("자기 소개 변경시 필요한 내용")
                            )
                        )
                    );
        }

        @DisplayName("회원 프로필 수정 기존 이름과 같을 시 400 BadRequest 응답")
        @Test
        @WithCustomMockMember(id = 1L, email = "admin", password = "Qwer!234", role = Role.MEMBER)
        void updateProfileFailureWhenSameNameAsBefore() throws Exception {
            PatchReq body = PatchReq.builder()
                .name("admin")
                .description("new admin description")
                .build();

            String content = gson.toJson(body);

            // when
            ResultActions actions = mockMvc.perform(
                patch("/members")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.COOKIE, "oDuckio.sid={SESSION_VALUE}")
                    .content(content));

            // then
            actions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.fieldErrors").value(equalTo(null)))
                .andExpect(jsonPath("$.violationErrors").value(equalTo(null)))
                .andDo(
                    document("patchProfile/failureWhenSameNameAsBefore",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                            headerWithName(HttpHeaders.COOKIE)
                                .attributes(field("constraints", "oDuckio.sid={SESSION_VALUE}"))
                                .description("Header Cookie, 세션 쿠키")
                        ),
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
                                .description("자기 소개 변경시 필요한 내용")
                        ),
                        responseFields(
                            attributes(key("title")
                                .value("Fields for member creation")),
                            fieldWithPath("message")
                                .type(JsonFieldType.STRING)
                                .description("예외 메시지"),
                            fieldWithPath("fieldErrors")
                                .type(JsonFieldType.NULL)
                                .description("api 요청 필드 오류"),
                            fieldWithPath("violationErrors")
                                .type(JsonFieldType.NULL)
                                .description("api 요청 규칙 위반 오류")
                        )
                    )
                );
        }

        @DisplayName("회원 프로필 수정 이름 중복시 409 Confilct 응답")
        @Test
        @WithCustomMockMember(id = 3L, email = "david", password = "Qwer!234", role = Role.MEMBER)
        void updateProfileFailureWhenSameNameAsAlreadyExist() throws Exception {
            PatchReq body = PatchReq.builder()
                .name("admin")
                .description("new david description")
                .build();

            String content = gson.toJson(body);

            // when
            ResultActions actions = mockMvc.perform(
                patch("/members")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.COOKIE, "oDuckio.sid={SESSION_VALUE}")
                    .content(content));

            // then
            actions
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.fieldErrors").value(equalTo(null)))
                .andExpect(jsonPath("$.violationErrors").value(equalTo(null)))
                .andDo(
                    document("patchProfile/failureWhenSameNameAsAlreadyExist",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                            headerWithName(HttpHeaders.COOKIE)
                                .attributes(field("constraints", "oDuckio.sid={SESSION_VALUE}"))
                                .description("Header Cookie, 세션 쿠키")
                        ),
                        requestFields(
                            attributes(key("title")
                                .value("Fields for member creation")),
                            fieldWithPath("name")
                                .type(JsonFieldType.STRING)
                                .attributes(
                                    field("constraints",
                                        "한,영소문자, 숫자 포함 2-10자. ^[0-9A-Za-z가-힣]{2,10}$"))
                                .description("이름 변경시 필요한 이름"),
                            fieldWithPath("description")
                                .type(JsonFieldType.STRING)
                                .attributes(field("constraints", "문자열 0-100자"))
                                .description("자기 소개 변경시 필요한 내용")
                        ),
                        responseFields(
                            attributes(key("title")
                                .value("Fields for member creation")),
                            fieldWithPath("message")
                                .type(JsonFieldType.STRING)
                                .description("예외 메시지"),
                            fieldWithPath("fieldErrors")
                                .type(JsonFieldType.NULL)
                                .description("api 요청 필드 오류"),
                            fieldWithPath("violationErrors")
                                .type(JsonFieldType.NULL)
                                .description("api 요청 규칙 위반 오류")
                        )
                    )
                );
        }
    }

    @DisplayName("회원의 북마크 애니 목록 조회")
    @Nested
    class GetBookmarks {
        @DisplayName("회원의 북마크 애니 목록 조회 성공시 200 OK 응답(cursor 없이 최초 요청)")
        @Test
        void getBookmarksSuccess() throws Exception {
            // given
            // 회원의 북마크 애니 목록 조회에 필요한 데이터
            int size = 2;
            String sort = "created_at";
            String order = "desc";

            // when
            ResultActions actions = mockMvc.perform(
                get("/members/{memberId}/bookmarks", 1)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .param("size", String.valueOf(size))
                    .param("sort", sort)
                    .param("direction", order)
            );

            // then
            actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items[0].animeId").exists())
                .andExpect(jsonPath("$.items[0].title").exists())
                .andExpect(jsonPath("$.items[0].thumbnail").exists())
                .andExpect(jsonPath("$.size").exists())
                .andExpect(jsonPath("$.lastId").exists())
                .andExpect(jsonPath("$.hasNext").exists())
                .andDo(
                    document("getBookmarks/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                            parameterWithName("memberId")
                                .description("회원 id")),
                        queryParameters(
                            parameterWithName("size")
                                .attributes(field("constraints", "1-100, 기본 10"))
                                .optional()
                                .description("한 페이지에 보여줄 아이템의 개수"),
                            parameterWithName("sort")
                                .attributes(field("constraints", "latest"))
                                .optional()
                                .description("정렬 기준"),
                            parameterWithName("direction")
                                .attributes(field("constraints", "asc, desc"))
                                .optional()
                                .description("정렬 방향"),
                            parameterWithName("cursor")
                                .optional()
                                .description("마지막 아이템 id")
                        ),
                        responseFields(
                            fieldWithPath("items")
                                .type(JsonFieldType.ARRAY)
                                .description("애니 리스트"),
                            fieldWithPath("items[].animeId")
                                .type(JsonFieldType.NUMBER)
                                .description("애니 id"),
                            fieldWithPath("items[].title")
                                .type(JsonFieldType.STRING)
                                .description("애니 제목"),
                            fieldWithPath("items[].thumbnail")
                                .type(JsonFieldType.STRING)
                                .description("애니 썸네일"),
                            fieldWithPath("items[].myScore")
                                .type(JsonFieldType.NUMBER)
                                .description("해당 회원이 애니에 매긴 별점. 없을 경우 -1"),
                            fieldWithPath("items[].createdAt")
                                .type(JsonFieldType.STRING)
                                .description("애니 북마크 생성 날짜"),
                            fieldWithPath("size")
                                .type(JsonFieldType.NUMBER)
                                .description("한 페이지에 보여줄 아이템의 개수"),
                            fieldWithPath("hasNext")
                                .type(JsonFieldType.BOOLEAN)
                                .description("마지막 페이지 여부"),
                            fieldWithPath("lastId")
                                .type(JsonFieldType.STRING)
                                .description("마지막 아이템 id, 다음 페이지 요청시 cursor로 사용. 다음 페이지가 없다면 \"\"")
                        )
                    )
                );
        }

        @DisplayName("회원의 북마크 애니 목록 조회 성공시 200 OK 응답(커서 요청)")
        @Test
        void getBookmarksSuccessWithCursor() throws Exception {
            // given
            // 회원의 북마크 애니 목록 조회에 필요한 데이터
            int size = 2;
            String sort = "created_at";
            String order = "desc";
            String cursor = "2023-10-11T21:05:31.859";

            // when
            ResultActions actions = mockMvc.perform(
                get("/members/{memberId}/bookmarks", 1)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .param("size", String.valueOf(size))
                    .param("sort", sort)
                    .param("direction", order)
                    .param("cursor", cursor)
            );

            // then
            actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items[0].animeId").exists())
                .andExpect(jsonPath("$.items[0].title").exists())
                .andExpect(jsonPath("$.items[0].thumbnail").exists())
                .andExpect(jsonPath("$.size").exists())
                .andExpect(jsonPath("$.lastId").exists())
                .andExpect(jsonPath("$.hasNext").exists())
                .andDo(
                    document("getBookmarks/successWithCursor",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                            parameterWithName("memberId")
                                .description("회원 id")),
                        queryParameters(
                            parameterWithName("size")
                                .attributes(field("constraints", "1-100, 기본 10"))
                                .optional()
                                .description("한 페이지에 보여줄 아이템의 개수"),
                            parameterWithName("sort")
                                .attributes(field("constraints", "latest"))
                                .optional()
                                .description("정렬 기준"),
                            parameterWithName("direction")
                                .attributes(field("constraints", "asc, desc"))
                                .optional()
                                .description("정렬 방향"),
                            parameterWithName("cursor")
                                .optional()
                                .description("마지막 아이템 id")
                        ),
                        responseFields(
                            fieldWithPath("items")
                                .type(JsonFieldType.ARRAY)
                                .description("애니 리스트"),
                            fieldWithPath("items[].animeId")
                                .type(JsonFieldType.NUMBER)
                                .description("애니 id"),
                            fieldWithPath("items[].title")
                                .type(JsonFieldType.STRING)
                                .description("애니 제목"),
                            fieldWithPath("items[].thumbnail")
                                .type(JsonFieldType.STRING)
                                .description("애니 썸네일"),
                            fieldWithPath("items[].myScore")
                                .type(JsonFieldType.NUMBER)
                                .description("해당 회원이 애니에 매긴 별점. 없을 경우 -1"),
                            fieldWithPath("items[].createdAt")
                                .type(JsonFieldType.STRING)
                                .description("애니 북마크 생성 날짜"),
                            fieldWithPath("size")
                                .type(JsonFieldType.NUMBER)
                                .description("한 페이지에 보여줄 아이템의 개수"),
                            fieldWithPath("hasNext")
                                .type(JsonFieldType.BOOLEAN)
                                .description("마지막 페이지 여부"),
                            fieldWithPath("lastId")
                                .type(JsonFieldType.STRING)
                                .description("마지막 아이템 id, 다음 페이지 요청시 cursor로 사용. 다음 페이지가 없다면 \"\"")
                        )
                    )
                );
        }
    }

    // TODO: 회원 리뷰 목록 조회
}
