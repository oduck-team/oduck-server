package io.oduck.api.e2e.shortReviewLike;

import static io.oduck.api.global.config.RestDocsConfig.field;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
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
import io.oduck.api.domain.member.entity.Role;
import io.oduck.api.domain.reviewLike.dto.ShortReviewLikeReqDto.ShortReviewLikeReq;
import io.oduck.api.global.mockMember.WithCustomMockMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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
public class ShortReviewLikeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    private final String BASE_URL = "/likes";

    @Nested
    @DisplayName("리뷰 좋아요")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class PostLike{

        @Test
        @DisplayName("짧은 리뷰 좋아요 성공")
        @Order(1)
        @WithCustomMockMember(id = 2L, email = "john", password = "Qwer!234", role = Role.MEMBER)
        void shortReviewLikeSaveSuccess() throws Exception{
            //given
            ShortReviewLikeReq likeRes = ShortReviewLikeReq
                                             .builder()
                                             .shortReviewId(1L)
                                             .build();
            String content = gson.toJson(likeRes);

            //when
            ResultActions actions = mockMvc.perform(
                post(BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.COOKIE, "oDuckio.sid={SESSION_VALUE}")
                    .content(content)
            );

            //then
            actions
                .andExpect(status().isCreated())
                .andDo(document("likes/shortReview/save/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                            headerWithName(HttpHeaders.COOKIE)
                                .attributes(field("constraints", "oDuckio.sid={SESSION_VALUE}"))
                                .description("Header Cookie, 세션 쿠키")
                        ),
                        requestFields(attributes(key("title").value("Fields for shortReviewLike creation")),
                            fieldWithPath("shortReviewId")
                                .type(JsonFieldType.NUMBER)
                                .attributes(field("constraints", "짧은 리뷰 아이디, NotNull, Min(1)"))
                                .description("짧은 리뷰 좋아요를 등록할 리뷰 고유 식별 번호"))
                    )
                );
        }

        @Test
        @DisplayName("짧은 리뷰 좋아요 삭제 성공")
        @Order(2)
        @WithCustomMockMember(id = 2L, email = "john", password = "Qwer!234", role = Role.MEMBER)
        void shorReviewLikeDeleteSuccess() throws Exception{
            //given
            ShortReviewLikeReq likeRes = ShortReviewLikeReq
                                             .builder()
                                             .shortReviewId(1L)
                                             .build();
            String content = gson.toJson(likeRes);

            //when
            ResultActions actions = mockMvc.perform(
                post(BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.COOKIE, "oDuckio.sid={SESSION_VALUE}")
                    .content(content)
            );

            //then
            actions
                .andExpect(status().isNoContent())
                .andDo(document("likes/shortReview/delete/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                            headerWithName(HttpHeaders.COOKIE)
                                .attributes(field("constraints", "oDuckio.sid={SESSION_VALUE}"))
                                .description("Header Cookie, 세션 쿠키")
                        ),
                        requestFields(attributes(key("title").value("Fields for shortReviewLike creation")),
                            fieldWithPath("shortReviewId")
                                .type(JsonFieldType.NUMBER)
                                .attributes(field("constraints", "짧은 리뷰 아이디, NotNull, Min(1)"))
                                .description("짧은 리뷰 좋아요를 등록할 리뷰 고유 식별 번호"))
                    )
                );
        }
    }

    @Nested
    @DisplayName("리뷰 좋아요 여부")
    class CheckLike{

        @Test
        @DisplayName("짧은 리뷰 좋아요 존재 여부")
        @WithCustomMockMember(id = 1L, email = "john", password = "Qwer!234", role = Role.MEMBER)
        void checkShortReviewLike() throws Exception{
            //given
            Long shortReviewId = 1L;

            //when
            ResultActions actions = mockMvc.perform(
                get(BASE_URL + "/{shortReviewId}", shortReviewId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.COOKIE, "oDuckio.sid={SESSION_VALUE}"));

            //then
            actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isLike").exists())
                .andDo(document("likes/shortReview/checkLike/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("shortReviewId")
                                        .description("짧은 리뷰 식별자")),
                        responseFields(
                            fieldWithPath("isLike")
                                .type(JsonFieldType.BOOLEAN)
                                .description("짧은 리뷰 좋아요 존재 시 true, 부재 시 false"))
                    )
                );
        }
    }
}