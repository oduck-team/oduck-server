package io.oduck.api.e2e.shortReview;

import static io.oduck.api.global.config.RestDocsConfig.field;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;


import com.google.gson.Gson;
import io.oduck.api.domain.member.entity.Role;
import io.oduck.api.domain.review.dto.ShortReviewReqDto.ShortReviewReq;
import io.oduck.api.global.mockMember.WithCustomMockMember;
import io.oduck.api.global.utils.ShortReviewTestUtils;
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
public class ShortReviewControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    private final String BASE_URL = "/short-reviews";

    @Nested
    @DisplayName("짧은 리뷰 작성")
    class PostShortReviews{

        @DisplayName("리뷰 작성 성공시 Http Status 200 ok 반환")
        @Test
        @WithCustomMockMember(id = 2L, email = "john", password = "Qwer!234", role = Role.MEMBER)
        void postShortReview() throws Exception{
            //given
            ShortReviewReq req = ShortReviewTestUtils.createPostShoreReviewReq();
            String content = gson.toJson(req);

            //when
            ResultActions actions = mockMvc.perform(
                post(BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.COOKIE, "oDuckio.sid={SESSION_VALUE}")
                    .content(content));

            //then
            actions
                .andExpect(status().isOk())
                .andDo(document("postShortReview/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                            headerWithName(HttpHeaders.COOKIE)
                                .attributes(field("constraints", "oDuckio.sid={SESSION_VALUE}"))
                                .optional()
                                .description("Header Cookie, 세션 쿠키")
                        ),
                        requestFields(attributes(key("title").value("Fields for shortReview creation")),
                        fieldWithPath("animeId")
                            .type(JsonFieldType.NUMBER)
                            .attributes(field("constraints","애니메 ID, NotNull, Min(1)"))
                            .description("리뷰를 등록할 애니 고유 식별 번호"),
                        fieldWithPath("name")
                            .type(JsonFieldType.STRING)
                            .attributes(field("constraints", "String만 가능합니다"))
                            .description("리뷰를 등록할 회원의 이름"),
                        fieldWithPath("hasSpoiler")
                            .type(JsonFieldType.BOOLEAN)
                            .attributes(field("constraints", "true 또는 false."))
                            .description("스포일러 유무"),
                        fieldWithPath("content")
                        .type(JsonFieldType.STRING)
                        .attributes(field("constraints", "최소 10에서 100자 까지 입력 가능합니다."))
                        .description("짧은 리뷰 내용"))
                    )
                );
        }
        //TODO: 리뷰 작성 실패 시
    }

    @Nested
    @DisplayName("짧은 리뷰 조회")
    class GetShortReviews{

        @DisplayName("조회 성공 시 Http Status 200 반환(커서 없이 최소 요청)")
        @Test
        void getShortReviews() throws Exception{
            //given
            Long animeId = 1L;
            int size = 2;
            String sort = "like_count";
            String order = "desc";

            //when
            ResultActions actions = mockMvc.perform(
                get(BASE_URL+ "/animes/{animeId}", animeId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .param("size", String.valueOf(size))
                    .param("sort", sort)
                    .param("direction", order)
            );

            //then
            actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items[0].reviewId").exists())
                .andExpect(jsonPath("$.items[0].animeId").exists())
                .andExpect(jsonPath("$.items[0].name").exists())
                .andExpect(jsonPath("$.items[0].thumbnail").exists())
                .andExpect(jsonPath("$.items[0].score").exists())
                .andExpect(jsonPath("$.items[0].content").exists())
                .andExpect(jsonPath("$.items[0].isSpoiler").exists())
                .andExpect(jsonPath("$.items[0].isLike").exists())
                .andExpect(jsonPath("$.items[0].likeCount").exists())
                .andExpect(jsonPath("$.items[0].createdAt").exists())
                .andExpect(jsonPath("$.size").exists())
                .andExpect(jsonPath("$.cursor").exists())
                .andExpect(jsonPath("$.hasNext").exists())
                .andDo(document("getShortReviews/success",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("animeId")
                            .description("애니 아이디")
                    ),
                    queryParameters(
                        parameterWithName("size")
                            .attributes(field("constraints","1-100, 기본 10"))
                            .optional()
                            .description("한 페이지에 보여줄 아이템의 개수"),
                        parameterWithName("sort")
                            .attributes(field("constraints","latest"))
                            .optional()
                            .description("정렬 기준"),
                        parameterWithName("direction")
                            .attributes(field("constraints","asc, desc"))
                            .optional()
                            .description("정렬 방향"),
                        parameterWithName("cursor")
                            .optional()
                            .description("마지막 아이템 id")
                    ),
                    responseFields(
                        fieldWithPath("items")
                            .type(JsonFieldType.ARRAY)
                            .description("조회 데이터"),
                        fieldWithPath("items[].reviewId")
                            .type(JsonFieldType.NUMBER)
                            .description("리뷰 고유 식별자"),
                        fieldWithPath("items[].animeId")
                            .type(JsonFieldType.NUMBER)
                            .description("애니 고유 식별자"),
                        fieldWithPath("items[].name")
                            .type(JsonFieldType.STRING)
                            .description("회원 이름"),
                        fieldWithPath("items[].thumbnail")
                            .type(JsonFieldType.STRING)
                            .description("회원 이미지 사진"),
                        fieldWithPath("items[].score")
                            .type(JsonFieldType.NUMBER)
                            .description("평점"),
                        fieldWithPath("items[].content")
                            .type(JsonFieldType.STRING)
                            .description("짧은 리뷰 내용"),
                        fieldWithPath("items[].isSpoiler")
                            .type(JsonFieldType.BOOLEAN)
                            .description("스포일러 유무"),
                        fieldWithPath("items[].isLike")
                            .type(JsonFieldType.BOOLEAN)
                            .description("짧은 리뷰 좋아요 유무"),
                        fieldWithPath("items[].likeCount")
                            .type(JsonFieldType.NUMBER)
                            .description("짧은 리뷰 좋아요 수"),
                        fieldWithPath("items[].createdAt")
                            .type(JsonFieldType.STRING)
                            .description("짧은 리뷰 작성일"),
                        fieldWithPath("size")
                            .type(JsonFieldType.NUMBER)
                            .description("한 페이지에 보여줄 아이템의 개수"),
                        fieldWithPath("cursor")
                            .type(JsonFieldType.STRING)
                            .description("마지막 아이템 id, 다음 페이지 요청시 cursor로 사용. 다음 페이지가 없다면 \"\""),
                        fieldWithPath("hasNext")
                            .type(JsonFieldType.BOOLEAN)
                            .description("마지막 페이지일 경우, false 반환.")

                    )
                ));
            //TODO : 조회 실패 시
        }

        @DisplayName("조회 성공 시 Http Status 200 반환(커서 요청)")
        @Test
        void getShortReviewsWithCursor() throws Exception{
            //given
            Long animeId = 1L;
            int size = 2;
            String sort = "like_count";
            String order = "desc";
            String cursor = "0, 2023-10-11T21:05:31.859";

            //when
            ResultActions actions = mockMvc.perform(
                get(BASE_URL+ "/animes/{animeId}", animeId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .param("size", String.valueOf(size))
                    .param("sort", sort)
                    .param("direction", order)
                    .param("cursor", cursor)
            );

            //then
            actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items[0].reviewId").exists())
                .andExpect(jsonPath("$.items[0].animeId").exists())
                .andExpect(jsonPath("$.items[0].name").exists())
                .andExpect(jsonPath("$.items[0].thumbnail").exists())
                .andExpect(jsonPath("$.items[0].score").exists())
                .andExpect(jsonPath("$.items[0].content").exists())
                .andExpect(jsonPath("$.items[0].isSpoiler").exists())
                .andExpect(jsonPath("$.items[0].isLike").exists())
                .andExpect(jsonPath("$.items[0].likeCount").exists())
                .andExpect(jsonPath("$.items[0].createdAt").exists())
                .andExpect(jsonPath("$.size").exists())
                .andExpect(jsonPath("$.cursor").exists())
                .andExpect(jsonPath("$.hasNext").exists())
                .andDo(document("getShortReviews/withCursor/success",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("animeId")
                            .description("애니 아이디")
                    ),
                    queryParameters(
                        parameterWithName("size")
                            .attributes(field("constraints","1-100, 기본 10"))
                            .optional()
                            .description("한 페이지에 보여줄 아이템의 개수"),
                        parameterWithName("sort")
                            .attributes(field("constraints","latest"))
                            .optional()
                            .description("정렬 기준"),
                        parameterWithName("direction")
                            .attributes(field("constraints","asc, desc"))
                            .optional()
                            .description("정렬 방향"),
                        parameterWithName("cursor")
                            .optional()
                            .description("마지막 아이템 id")
                    ),
                    responseFields(
                        fieldWithPath("items")
                            .type(JsonFieldType.ARRAY)
                            .description("조회 데이터"),
                        fieldWithPath("items[].reviewId")
                            .type(JsonFieldType.NUMBER)
                            .description("리뷰 고유 식별자"),
                        fieldWithPath("items[].animeId")
                            .type(JsonFieldType.NUMBER)
                            .description("애니 고유 식별자"),
                        fieldWithPath("items[].name")
                            .type(JsonFieldType.STRING)
                            .description("회원 이름"),
                        fieldWithPath("items[].thumbnail")
                            .type(JsonFieldType.STRING)
                            .description("회원 이미지 사진"),
                        fieldWithPath("items[].score")
                            .type(JsonFieldType.NUMBER)
                            .description("평점"),
                        fieldWithPath("items[].content")
                            .type(JsonFieldType.STRING)
                            .description("짧은 리뷰 내용"),
                        fieldWithPath("items[].isSpoiler")
                            .type(JsonFieldType.BOOLEAN)
                            .description("스포일러 유무"),
                        fieldWithPath("items[].isLike")
                            .type(JsonFieldType.BOOLEAN)
                            .description("짧은 리뷰 좋아요 유무"),
                        fieldWithPath("items[].likeCount")
                            .type(JsonFieldType.NUMBER)
                            .description("짧은 리뷰 좋아요 수"),
                        fieldWithPath("items[].createdAt")
                            .type(JsonFieldType.STRING)
                            .description("짧은 리뷰 작성일"),
                        fieldWithPath("size")
                            .type(JsonFieldType.NUMBER)
                            .description("한 페이지에 보여줄 아이템의 개수"),
                        fieldWithPath("cursor")
                            .type(JsonFieldType.STRING)
                            .description("마지막 아이템 id, 다음 페이지 요청시 cursor로 사용. 다음 페이지가 없다면 \"\""),
                        fieldWithPath("hasNext")
                            .type(JsonFieldType.BOOLEAN)
                            .description("마지막 페이지일 경우, false 반환.")

                    )
                ));
            //TODO : 조회 실패 시
        }

    }

    @Nested
    @DisplayName("입덕포인트 조회")
    class getAttractionPoint{
        @DisplayName("애니아이디와 회원이름으로 입덕포인트 조회")
        @Test
        @WithCustomMockMember(id = 1L, email = "john", password = "Qwer!234", role = Role.MEMBER)
        void getAttractionPointsShortReview() throws Exception{
            //given
            String animeId = "1";
            String name = "회원 이름";

            //when
            ResultActions actions = mockMvc.perform(
                get(BASE_URL + "/attraction-points")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.COOKIE, "oDuckio.sid={SESSION_VALUE}")
                    .param("animeId", animeId)
                    .param("name", name)
            );

            //then
            actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.drawing").exists())
                .andExpect(jsonPath("$.story").exists())
                .andExpect(jsonPath("$.music").exists())
                .andExpect(jsonPath("$.character").exists())
                .andExpect(jsonPath("$.voiceActor").exists())
                .andDo(document("shortReview/getAttractionPoints/success",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestHeaders(
                        headerWithName(HttpHeaders.COOKIE)
                            .attributes(field("constraints", "oDuckio.sid={SESSION_VALUE}"))
                            .description("Header Cookie, 세션 쿠키")
                    ),
                    queryParameters(
                        parameterWithName("animeId")
                            .attributes(field("constraints","애니메 ID, NotNull, Min(1)"))
                            .optional()
                            .description("애니 고유의 식별자"),
                        parameterWithName("name")
                            .attributes(field("constraints","회원 이름"))
                            .optional()
                            .description("회원 이름")
                    ),
                    responseFields(
                        fieldWithPath("drawing")
                            .type(JsonFieldType.BOOLEAN)
                            .description("그림 입덕포인트"),
                        fieldWithPath("story")
                            .type(JsonFieldType.BOOLEAN)
                            .description("그림 입덕포인트"),
                        fieldWithPath("music")
                            .type(JsonFieldType.BOOLEAN)
                            .description("음악 입덕포인트"),
                        fieldWithPath("character")
                            .type(JsonFieldType.BOOLEAN)
                            .description("캐릭터 입덕포인트"),
                        fieldWithPath("voiceActor")
                            .type(JsonFieldType.BOOLEAN)
                            .description("성우 입덕포인트"))
                ));
        }
    }

    @Nested
    @DisplayName("짧은 리뷰 수정")
    class PatchShortReview{
        

        @DisplayName("짧은 리뷰 수정 성공시 Http Status 204 반환")
        @Test
        @WithCustomMockMember(id = 1L, email = "john", password = "Qwer!234", role = Role.MEMBER)
        void patchShortReview() throws Exception{
            //given
            Long reviewId = 1L;
            ShortReviewReq req = ShortReviewTestUtils.createPatchShortReview();
            String content = gson.toJson(req);

            //when
            ResultActions actions = mockMvc.perform(
                patch(BASE_URL +"/{reviewId}", reviewId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.COOKIE, "oDuckio.sid={SESSION_VALUE}")
                    .content(content)
            );

            //then
            actions
                .andExpect(status().isNoContent())
                .andDo(document("patchShortReview/success",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestHeaders(
                        headerWithName(HttpHeaders.COOKIE)
                            .attributes(field("constraints", "oDuckio.sid={SESSION_VALUE}"))
                            .description("Header Cookie, 세션 쿠키")
                    ),
                    pathParameters(
                        parameterWithName("reviewId")
                            .description("리뷰 식별자")),
                    requestFields(attributes(key("title").value("Fields for shortReview creation")),
                        fieldWithPath("animeId")
                            .type(JsonFieldType.NUMBER)
                            .attributes(field("constraints","애니메 ID, NotNull, Min(1)"))
                            .description("리뷰를 등록할 애니 고유 식별 번호"),
                        fieldWithPath("name")
                            .type(JsonFieldType.STRING)
                            .attributes(field("constraints", "String만 가능합니다"))
                            .description("리뷰를 등록할 회원의 이름"),
                        fieldWithPath("hasSpoiler")
                            .type(JsonFieldType.BOOLEAN)
                            .attributes(field("constraints", "true 또는 false."))
                            .description("스포일러 유무"),
                        fieldWithPath("content")
                            .type(JsonFieldType.STRING)
                            .attributes(field("constraints", "최소 10에서 100자 까지 입력 가능합니다."))
                            .description("짧은 리뷰 내용"))
                ));
        }
//        @DisplayName("짧은 리뷰 수정 기존 내용과 동일할 시 400 BadRequest 응답")
//        @Test
//        void updateShortReviewFailsWhenSameContentAsBefore() throws Exception {
//            //given
//            Long reviewId = 1L;
//            PatchShortReviewReq req = ShortReviewTestUtils.createPatchShortReview();
//            String content = gson.toJson(req);
//
//            //when
//            ResultActions actions = mockMvc.perform(
//                patch(BASE_URL +"/{reviewId}", reviewId)
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .accept(MediaType.APPLICATION_JSON)
//                    .content(content)
//            );
//
//            //then
//            actions
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.drawing").exists())
//                .andExpect(jsonPath("$.story").exists())
//                .andExpect(jsonPath("$.music").exists())
//                .andExpect(jsonPath("$.character").exists())
//                .andExpect(jsonPath("$.voiceActor").exists())
//                .andDo(document("patchShortReview/failsWhenSameContentAsBefore",
//                    preprocessRequest(prettyPrint()),
//                    preprocessResponse(prettyPrint()),
//                    pathParameters(
//                        parameterWithName("reviewId")
//                            .description("리뷰 식별자")),
//                    responseFields(
//                        attributes(key("title").value("Fields for shortReview update")),
//                        fieldWithPath("message")
//                            .type(JsonFieldType.STRING)
//                            .description("예외 메세지"),
//                        fieldWithPath("fieldErrors")
//                            .type(JsonFieldType.NULL)
//                            .description("api 요청 필드 오류"),
//                        fieldWithPath("violationErrors")
//                            .type(JsonFieldType.NULL)
//                            .description("api 요청 규칙 위반 오류")
//                    )
//                ));
//        }
    }
}