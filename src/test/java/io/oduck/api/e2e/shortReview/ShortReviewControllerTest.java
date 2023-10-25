package io.oduck.api.e2e.shortReview;

import static io.oduck.api.global.config.RestDocsConfig.field;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;


import com.google.gson.Gson;
import io.oduck.api.domain.review.dto.ShortReviewReqDto.PatchShortReviewReq;
import io.oduck.api.domain.review.dto.ShortReviewReqDto.PostShortReviewReq;
import io.oduck.api.global.utils.ShortReviewTestUtils;
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
@ExtendWith({ RestDocumentationExtension.class, SpringExtension.class })
@SpringBootTest
@ActiveProfiles("test")
public class ShortReviewControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @Nested
    @DisplayName("짧은 리뷰 조회")
    class GetShortReviews{

        @DisplayName("조회 성공 시 Http Status 200 반환")
        @Test
        void getShortReviews() throws Exception{
            //given
            Long animeId = 1L;

            //when
            ResultActions actions = mockMvc.perform(
                get("/short-reviews" + "/{animeId}", animeId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            );

            //then
            actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].reviewId").exists())
                .andExpect(jsonPath("$.items[0].animeId").exists())
                .andExpect(jsonPath("$.items[0].content").exists())
                .andExpect(jsonPath("$.items[0].hasSpoiler").exists())
                .andExpect(jsonPath("$.items[0].score").exists())
                .andExpect(jsonPath("$.items[0].shortReviewLikeCount").exists())
                .andExpect(jsonPath("$.items[0].member.name").exists())
                .andExpect(jsonPath("$.items[0].member.thumbnail").exists())
                .andDo(document("getShortReviews/success",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("animeId")
                            .description("애니 아이디")
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
                        fieldWithPath("items[].content")
                            .type(JsonFieldType.STRING)
                            .description("짧은 리뷰 내용"),
                        fieldWithPath("items[].hasSpoiler")
                            .type(JsonFieldType.BOOLEAN)
                            .description("스포일러 유무"),
                        fieldWithPath("items[].score")
                            .type(JsonFieldType.NUMBER)
                            .description("평점"),
                        fieldWithPath("items[].shortReviewLikeCount")
                            .type(JsonFieldType.NUMBER)
                            .description("리뷰 좋아요 수")
                        ,fieldWithPath("items[].member")
                             .type(JsonFieldType.OBJECT)
                             .description("회원 관련 데이터"),
                        fieldWithPath("items[].member.name")
                            .type(JsonFieldType.STRING)
                            .description("회원 이름"),
                        fieldWithPath("items[].member.thumbnail")
                            .type(JsonFieldType.STRING)
                            .description("회원 이미지 사진"),
                        fieldWithPath("items[].id")
                            .type(JsonFieldType.NUMBER)
                            .description("애니 아이디"),
                        fieldWithPath("size")
                            .type(JsonFieldType.NUMBER)
                            .description("한 페이지에 보여줄 아이템의 개수"),
                        fieldWithPath("lastId")
                            .type(JsonFieldType.NUMBER)
                            .description("마지막 아이템의 id"),
                        fieldWithPath("lastPage")
                            .type(JsonFieldType.BOOLEAN)
                            .description("마지막 페이지일 경우, true 반환.")

                    )
                ));
            //TODO : 조회 실패 시
        }
    }

    @Nested
    @DisplayName("짧은 리뷰 작성")
    class PostShortReviews{

        @DisplayName("리뷰 작성 성공시 Http Status 200 ok 반환")
        @Test
        void postShortReview() throws Exception{
            //given
            PostShortReviewReq req = ShortReviewTestUtils.createPostShoreReviewReq();
            String content = gson.toJson(req);

            //when
            ResultActions actions = mockMvc.perform(
                post("/short-reviews")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(content));

            //then
            actions
                .andExpect(status().isOk())
                .andDo(document("postShortReview/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(attributes(key("title").value("Fields for shortReview creation")),

                            fieldWithPath("animeId")
                                .type(JsonFieldType.NUMBER)
                                .attributes(field("constraints", "숫자만 가능합니다."))
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
    @DisplayName("짧은 리뷰 수정")
    class PatchShortReview{

        @DisplayName("짧은 리뷰 수정 성공시 Http Status 200 반환")
        @Test
        void patchShortReview() throws Exception{
            //given
            Long reviewId = 1L;
            PatchShortReviewReq req = ShortReviewTestUtils.createPatchShortReview();
            String content = gson.toJson(req);

            //when
            ResultActions actions = mockMvc.perform(
                patch("/short-reviews/{reviewId}", reviewId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(content)
            );

            //then
            actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.drawing").exists())
                .andExpect(jsonPath("$.story").exists())
                .andExpect(jsonPath("$.music").exists())
                .andExpect(jsonPath("$.character").exists())
                .andExpect(jsonPath("$.voiceActor").exists())
                .andDo(document("patchShortReview/success",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("reviewId")
                            .description("리뷰 식별자")),
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
                            .description("성우 입덕포인트")),
                    requestFields(attributes(key("title").value("Fields for shortReview creation")),
                        fieldWithPath("animeId")
                            .type(JsonFieldType.NUMBER)
                            .attributes(field("constraints", "숫자만 가능합니다."))
                            .description("애니 식별자"),
                        fieldWithPath("name")
                            .type(JsonFieldType.STRING)
                            .attributes(field("constraints", "회원의 이름입니다."))
                            .description("회원의 이름"),
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
    }
}