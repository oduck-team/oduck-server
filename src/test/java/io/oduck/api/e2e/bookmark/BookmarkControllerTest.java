package io.oduck.api.e2e.bookmark;

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
import io.oduck.api.domain.bookmark.dto.BookmarkReqDto.CreateReq;
import io.oduck.api.domain.member.entity.Role;
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
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@AutoConfigureRestDocs
@AutoConfigureMockMvc
@ExtendWith({ RestDocumentationExtension.class, SpringExtension.class })
@SpringBootTest
@ActiveProfiles("test")
public class BookmarkControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    private final String BASE_URL = "/bookmarks";

    @DisplayName("북마크 토글")
    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class toggleBookmark {
        @DisplayName("북마크 토글 저장 성공")
        @Test
        @Order(1)
        @WithCustomMockMember(id = 2L, email = "john", password = "Qwer!234", role = Role.MEMBER)
        void toggleBookmarkSaveSuccess() throws Exception {
            // given
            Long animeId = 1L;
            CreateReq createReq = CreateReq.builder()
                .animeId(animeId)
                .build();

            String content = gson.toJson(createReq);

            // when
            ResultActions actions = mockMvc.perform(
                post(BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(content)
            );

            // then
            actions
                .andExpect(status().isCreated())
                .andDo(
                    document("bookmark/toggleBookmark/save/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                            attributes(key("title")
                                .value("Fields for bookmark creation")),
                            fieldWithPath("animeId")
                                .attributes(key("constraints")
                                    .value("애니메 ID, NotNull, Min(1)"))
                                .description("애니메 ID")
                        )
                    )
                );
        }

        @DisplayName("북마크 토글 삭제 성공")
        @Test
        @Order(2)
        @WithCustomMockMember(id = 2L, email = "john", password = "Qwer!234", role = Role.MEMBER)
        void toggleBookmarkDeleteSuccess() throws Exception {
            // given
            Long animeId = 1L;
            CreateReq createReq = CreateReq.builder()
                .animeId(animeId)
                .build();

            String content = gson.toJson(createReq);

            // when
            ResultActions actions = mockMvc.perform(
                post(BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(content)
            );

            // then
            actions
                .andExpect(status().isNoContent())
                .andDo(
                    document("bookmark/toggleBookmark/delete/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                            attributes(key("title")
                                .value("Fields for bookmark creation")),
                            fieldWithPath("animeId")
                                .attributes(key("constraints")
                                    .value("애니메 ID, NotNull, Min(1)"))
                                .description("애니메 ID")
                        )
                    )
                );
        }
    }

    @DisplayName("북마크 여부 조회")
    @Nested
    class checkBookmarked {
        @DisplayName("북마크 여부 조회 북마크 존재시")
        @Test
        @WithCustomMockMember(id = 1L, email = "admin", password = "Qwer!234", role = Role.MEMBER)
        void checkBookmarkedExist() throws Exception {
            // given
            Long animeId = 1L;

            // when
            ResultActions actions = mockMvc.perform(
                get(BASE_URL + "/{animeId}", animeId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            );

            // then
            actions
                .andExpect(status().isOk())
                .andDo(
                    document("bookmark/checkBookmarked/exist",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                            parameterWithName("animeId")
                                .description("애니메 id")
                        ),
                        responseFields(
                            attributes(key("title")
                                .value("Fields for bookmark creation")),
                            fieldWithPath("createdAt")
                                .description("북마크 생성일")
                        )
                    )
                );
        }

        @DisplayName("북마크 여부 조회 북마크 부재시")
        @Test
        @WithCustomMockMember(id = 3L, email = "david", password = "Qwer!234", role = Role.MEMBER)
        void checkBookmarkedNotExist() throws Exception {
            // given
            Long animeId = 1L;

            // when
            ResultActions actions = mockMvc.perform(
                get(BASE_URL + "/{animeId}", animeId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            );

            // then
            actions
                .andExpect(status().isNotFound())
                .andDo(
                    document("bookmark/checkBookmarked/notExist",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                            parameterWithName("animeId")
                                .description("애니메 id")
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
