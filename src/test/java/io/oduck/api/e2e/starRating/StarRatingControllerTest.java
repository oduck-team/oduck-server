package io.oduck.api.e2e.starRating;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import io.oduck.api.domain.member.entity.Role;
import io.oduck.api.domain.starRating.dto.StarRatingReqDto.CreateReq;
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
public class StarRatingControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    private final String BASE_URL = "/ratings";

    @Nested
    @DisplayName("POST /ratings/{animeId}")
    class PostScore {
        @Test
        @DisplayName("중복된 평점이 없을 때 평점을 등록하면 201 Created를 반환")
        @WithCustomMockMember(id = 2L, email = "john", password = "Qwer!234", role = Role.MEMBER)
        void createScore() throws Exception {
            // given
            Long animeId = 2L;
            int score = 5;

            CreateReq body = CreateReq.builder()
                .score(score)
                .build();

            String content = gson.toJson(body);

            // when
            ResultActions actions = mockMvc.perform(
                post(BASE_URL + "/{animeId}", animeId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.COOKIE, "oDuckio.sid={SESSION_VALUE}")
                    .content(content)
            );

            // then
            actions
                .andExpect(status().isCreated())
                .andDo(document("starRating/createScore/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                            parameterWithName("animeId")
                                .description("애니메이션 식별자")
                        ),
                        requestHeaders(
                            headerWithName(HttpHeaders.COOKIE)
                                .attributes(field("constraints", "oDuckio.sid={SESSION_VALUE}"))
                                .optional()
                                .description("Header Cookie, 세션 쿠키")
                        ),
                        requestFields(
                            attributes(key("title")
                                .value("Fields for starRating creation")),
                            fieldWithPath("score")
                                .type(JsonFieldType.NUMBER)
                                .attributes(field("constraints", "1~10 사이의 정수"))
                                .description("애니메 별점")
                        )
                    )
                );
        }

        @Test
        @DisplayName("중복된 평점이 있을 때 평점을 등록하면 209 Conflict 반환")
        @WithCustomMockMember(id = 2L, email = "john", password = "Qwer!234", role = Role.MEMBER)
        void createScoreIfAlreadyExist() throws Exception {
            // given
            Long animeId = 1L;
            int score = 5;

            CreateReq body = CreateReq.builder()
                .score(score)
                .build();

            String content = gson.toJson(body);

            // when
            ResultActions actions = mockMvc.perform(
                post(BASE_URL + "/{animeId}", animeId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.COOKIE, "oDuckio.sid={SESSION_VALUE}")
                    .content(content)
            );

            // then
            actions
                .andExpect(status().isConflict())
                .andDo(document("starRating/createScore/failure",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                            parameterWithName("animeId")
                                .description("애니메이션 식별자")
                        ),
                        requestHeaders(
                            headerWithName(HttpHeaders.COOKIE)
                                .attributes(field("constraints", "oDuckio.sid={SESSION_VALUE}"))
                                .optional()
                                .description("Header Cookie, 세션 쿠키")
                        ),
                        requestFields(
                            attributes(key("title")
                                .value("Fields for starRating creation")),
                            fieldWithPath("score")
                                .type(JsonFieldType.NUMBER)
                                .attributes(field("constraints", "1~10 사이의 정수"))
                                .description("애니메 별점")
                        )
                    )
                );
        }
    }

    @Nested
    @DisplayName("GET /ratings/{animeId}")
    class GetScore {
        @Test
        @DisplayName("평점이 없을 때 평점을 조회하면 200 OK를 반환")
        @WithCustomMockMember(id = 1L, email = "admin", password = "Qwer!234", role = Role.MEMBER)
        void checkRatedExist() throws Exception {
            // given
            Long animeId = 1L;

            // when
            ResultActions actions = mockMvc.perform(
                get(BASE_URL + "/{animeId}", animeId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.COOKIE, "oDuckio.sid={SESSION_VALUE}")
            );

            // then
            actions
                .andExpect(status().isOk())
                .andDo(
                    document("starRating/checkRated/exist",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                            parameterWithName("animeId")
                                .description("애니메 id")
                        ),
                        requestHeaders(
                            headerWithName(HttpHeaders.COOKIE)
                                .attributes(field("constraints", "oDuckio.sid={SESSION_VALUE}"))
                                .description("Header Cookie, 세션 쿠키")
                        ),
                        responseFields(
                            attributes(key("title")
                                .value("Fields for starRating get")),
                            fieldWithPath("createdAt")
                                .description("별점 생성일")
                        )
                    )
                );
        }

        @Test
        @DisplayName("평점이 없을 때 평점을 조회하면 404 Not Found를 반환")
        @WithCustomMockMember(id = 3L, email = "david", password = "Qwer!234", role = Role.MEMBER)
        void checkRatedNotExist() throws Exception {
            // given
            Long animeId = 2L;

            // when
            ResultActions actions = mockMvc.perform(
                get(BASE_URL + "/{animeId}", animeId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.COOKIE, "oDuckio.sid={SESSION_VALUE}")
            );

            // then
            actions
                .andExpect(status().isNotFound())
                .andDo(
                    document("starRating/checkRated/notExist",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                            parameterWithName("animeId")
                                .description("애니메 id")
                        ),
                        requestHeaders(
                            headerWithName(HttpHeaders.COOKIE)
                                .attributes(field("constraints", "oDuckio.sid={SESSION_VALUE}"))
                                .description("Header Cookie, 세션 쿠키")
                        ),
                        responseFields(
                            attributes(key("title")
                                .value("Fields for bookmark creation")),
                            fieldWithPath("message")
                                .description("에러 메시지"),
                            fieldWithPath("fieldErrors")
                                .description("필드 에러 목록"),
                            fieldWithPath("violationErrors")
                                .description("위반 에러 목록")
                        )
                    )
                );
        }

    }
}
