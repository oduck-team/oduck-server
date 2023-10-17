package io.oduck.api.e2e.anime;

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
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
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
public class AnimeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Nested
    @DisplayName("애니 조회")
    class GetAnimes{

        @DisplayName("조회 성공 시 Http Status 200 반환")
        @Test
        void getAnimes() throws Exception {
            //given
            Long animeId = 1L;

            //when
            ResultActions actions = mockMvc.perform(
                RestDocumentationRequestBuilders.get("/animes"+"/{animeId}", animeId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            );

            //then
            actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.anime.id").exists())
                .andExpect(jsonPath("$.anime.title").exists())
                .andExpect(jsonPath("$.anime.thumbnail").exists())
                .andExpect(jsonPath("$.anime.broadcastType").exists())
                .andExpect(jsonPath("$.anime.year").exists())
                .andExpect(jsonPath("$.anime.quarter").exists())
                .andExpect(jsonPath("$.anime.summary").exists())
                .andExpect(jsonPath("$.anime.episodeCount").exists())
                .andExpect(jsonPath("$.anime.rating").exists())
                .andExpect(jsonPath("$.anime.status").exists())
                .andExpect(jsonPath("$.anime.genres").exists())
                .andExpect(jsonPath("$.anime.originalAuthors").exists())
                .andExpect(jsonPath("$.anime.voiceActors").exists())
                .andExpect(jsonPath("$.anime.studios").exists())
                .andExpect(jsonPath("$.anime.reviewCount").exists())
                .andExpect(jsonPath("$.anime.bookmarkCount").exists())
                .andDo(document("getAnimeById/success",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("animeId")
                            .description("애니의 고유 식별자")
                    ),
                    responseFields(
                        fieldWithPath("anime")
                            .type(JsonFieldType.OBJECT)
                            .description("애니 정보"),
                        fieldWithPath("anime.id")
                            .type(JsonFieldType.NUMBER)
                            .description("애니의 고유 식별자"),
                        fieldWithPath("anime.title")
                            .type(JsonFieldType.STRING)
                            .description("애니 제목"),
                        fieldWithPath("anime.thumbnail")
                            .type(JsonFieldType.STRING)
                            .description("애니 이미지, 경로로 저장됨"),
                        fieldWithPath("anime.broadcastType")
                            .type(JsonFieldType.STRING)
                            .description("작품의 출시 방식. TVA, OVA, ONA, MOV가 있음"),
                        fieldWithPath("anime.year")
                            .type(JsonFieldType.NUMBER)
                            .description("작품의 출시 년도"),
                        fieldWithPath("anime.quarter")
                            .type(JsonFieldType.STRING)
                            .description("작품의 출시 분기"),
                        fieldWithPath("anime.summary")
                            .type(JsonFieldType.STRING)
                            .description("애니를 소개할 때 줄거리"),
                        fieldWithPath("anime.episodeCount")
                            .type(JsonFieldType.NUMBER)
                            .description("에피소드의 숫자. 16화까지 나왔으면 16으로 표기됨."),
                        fieldWithPath("anime.rating")
                            .type(JsonFieldType.STRING)
                            .description("작품의 심의 등급. ADULT, FIFTEEN, TWELVE, ALL가 있다."),
                        fieldWithPath("anime.status")
                            .type(JsonFieldType.STRING)
                            .description("방영 상태. FINISHED, ONGOING, UPCOMING, UNKNOWN이 있다."),
                        fieldWithPath("anime.genres")
                            .type(JsonFieldType.ARRAY)
                            .description("애니의 장르."),
                        fieldWithPath("anime.originalAuthors")
                            .type(JsonFieldType.ARRAY)
                            .description("애니의 원작 작가."),
                        fieldWithPath("anime.voiceActors")
                            .type(JsonFieldType.ARRAY)
                            .description("애니의 출연 성우."),
                        fieldWithPath("anime.studios")
                            .type(JsonFieldType.ARRAY)
                            .description("애니의 제작사."),
                        fieldWithPath("anime.reviewCount")
                            .type(JsonFieldType.NUMBER)
                            .description("애니의 리뷰 개수."),
                        fieldWithPath("anime.bookmarkCount")
                            .type(JsonFieldType.NUMBER)
                            .description("애니의 덕후 수.")
                    )
                ));

            //TODO : 조회 실패 시
        }
    }
}
