package io.oduck.api.e2e.shortReview;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;


import com.google.gson.Gson;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


@AutoConfigureRestDocs
@AutoConfigureMockMvc
@ExtendWith({ RestDocumentationExtension.class, SpringExtension.class })
@SpringBootTest
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
                    get("/short-review" + "/{animeId}", animeId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            );

            //then
            actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item.shortReview.animeId").exists())
                .andExpect(jsonPath("$.item.shortReview.title").exists())
                .andExpect(jsonPath("$.item.shortReview.content").exists())
                .andExpect(jsonPath("$.item.shortReview.hasSpoiler").exists())
                .andExpect(jsonPath("$.item.shortReview.score").exists())
                .andExpect(jsonPath("$.item.shortReview.shortReviewLikeCount").exists())
                .andExpect(jsonPath("$.item.shortReview.member.name").exists())
                .andExpect(jsonPath("$.item.shortReview.member.thumbnail").exists())
                .andDo(document("getShortReviews/success",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("animeId")
                            .description("애니 아이디")
                    ),
                    responseFields(
                        fieldWithPath("item")
                            .type(JsonFieldType.OBJECT)
                            .description("조회 데이터"),
                        fieldWithPath("item.shortReview")
                            .type(JsonFieldType.OBJECT)
                            .description("짧은 리뷰 데이터"),
                        fieldWithPath("item.shortReview.animeId")
                            .type(JsonFieldType.NUMBER)
                            .description("애니의 고유 식별자"),
                        fieldWithPath("item.shortReview.title")
                            .type(JsonFieldType.STRING)
                            .description("애니 제목"),
                        fieldWithPath("item.shortReview.content")
                            .type(JsonFieldType.STRING)
                            .description("짧은 리뷰 내용"),
                        fieldWithPath("item.shortReview.hasSpoiler")
                            .type(JsonFieldType.BOOLEAN)
                            .description("스포일러 유무"),
                        fieldWithPath("item.shortReview.score")
                            .type(JsonFieldType.NUMBER)
                            .description("평점"),
                        fieldWithPath("item.shortReview.shortReviewLikeCount")
                            .type(JsonFieldType.NUMBER)
                            .description("리뷰 좋아요 수")
                        ,fieldWithPath("item.shortReview.member")
                            .type(JsonFieldType.OBJECT)
                            .description("회원 관련 데이터"),
                        fieldWithPath("item.shortReview.member.name")
                            .type(JsonFieldType.STRING)
                            .description("회원 이름"),
                        fieldWithPath("item.shortReview.member.thumbnail")
                            .type(JsonFieldType.STRING)
                            .description("회원 이미지 사진")
                    )
                ));
            //TODO : 조회 실패 시
        }
    }
}
