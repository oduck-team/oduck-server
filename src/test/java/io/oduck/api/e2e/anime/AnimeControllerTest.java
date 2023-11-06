package io.oduck.api.e2e.anime;

import static io.oduck.api.global.config.RestDocsConfig.field;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
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
    @DisplayName("조회")
    class GetAnime{

        @Test
        @DisplayName("애니 검색 조회 성공 시 Http Status 200 반환")
        void getAnimes() throws Exception {
            //given

            //when
            ResultActions actions = mockMvc.perform(
                    RestDocumentationRequestBuilders.get("/animes")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
            );

            //then
            actions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.items").exists())
                    .andExpect(jsonPath("$.items[0].id").exists())
                    .andExpect(jsonPath("$.items[0].title").exists())
                    .andExpect(jsonPath("$.items[0].thumbnail").exists())
                    .andExpect(jsonPath("$.items[0].starScoreAvg").exists())
                    .andExpect(jsonPath("$.size").exists())
                    .andExpect(jsonPath("$.hasNext").exists())
                    .andExpect(jsonPath("$.cursor").exists())
                    .andDo(document("getAnimes/success",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            queryParameters(
                                    parameterWithName("query")
                                            .attributes(field("constraints", "1-50자만 허용합니다"))
                                            .optional()
                                            .description("애니의 제목 검색"),
                                    parameterWithName("size")
                                            .attributes(field("constraints", "1-100, 기본 10"))
                                            .optional()
                                            .description("한 페이지에 보여줄 아이템의 개수"),
                                    parameterWithName("sort")
                                            .attributes(field("constraints", "LATEST, REVIEW_COUNT, SCORE"))
                                            .optional()
                                            .description("정렬 기준이다. LATEST, REVIEW_COUNT, SCORE만 허용한다."),
                                    parameterWithName("direction")
                                            .attributes(field("constraints", "ASC, DESC"))
                                            .optional()
                                            .description("정렬 방향이다. ASC, DESC만 허용한다."),
                                    parameterWithName("genreIds")
                                            .attributes(field("constraints", "장르 아이디 리스트"))
                                            .optional()
                                            .description("장르 아이디 리스트"),
                                    parameterWithName("broadcastTypes")
                                            .attributes(field("constraints", "BroadcastType의 리스트"))
                                            .optional()
                                            .description("BroadcastType의 리스트 TVA, ONA, OVA, MOV"),
                                    parameterWithName("statuses")
                                            .attributes(field("constraints", "Status의 리스트 ONGOING, FINISHED, UPCOMING, UNKNOWN"))
                                            .optional()
                                            .description("status의 리스트. ONGOING, FINISHED, UPCOMING, UNKNOWN"),
                                    parameterWithName("episodeCounts")
                                            .attributes(field("constraints", "EpisodeCountEnum의 리스트. UNDER_TWELVE, UNDER_TWENTY_FOUR, UNDER_FORTY_EIGHT, UNDER_HUNDRED, OVER_HUNDRED를 허용"))
                                            .optional()
                                            .description("EpisodeCountEnum의 리스트. UNDER_TWELVE, UNDER_TWENTY_FOUR, UNDER_FORTY_EIGHT, UNDER_HUNDRED, OVER_HUNDRED를 허용"),
                                    parameterWithName("years")
                                            .attributes(field("constraints", "년도의 리스트. 현재 년도는 내부 로직으로 걸러집니다."))
                                            .optional()
                                            .description("년도의 리스트. 현재 년도는 내부 로직으로 걸러집니다."),
                                    parameterWithName("quarters")
                                            .attributes(field("constraints", "Quarter의 리스트. Quarters가 null이거나 empty하지 않으면 자동으로 <최신 년도+분기>로 예상하고 로직을 수행합니다. 예를 들어 2023년에 클라이언트가 Q1으로 요청하면, 서버에 Q1만 보내도 자동으로 2023년은 내부 로직으로 계산합니다."))
                                            .optional()
                                            .description("Quarter의 리스트. Quarters가 null이거나 empty하지 않으면 자동으로 <최신 년도+분기>로 예상하고 로직을 수행합니다. 예를 들어 2023년에 클라이언트가 Q1으로 요청하면, 서버에 Q1만 보내도 자동으로 2023년은 내부 로직으로 계산합니다."),
                                    parameterWithName("cursor")
                                            .optional()
                                            .description("마지막 아이템 제목")
                            ),
                            responseFields(
                                    fieldWithPath("items")
                                            .type(JsonFieldType.ARRAY)
                                            .description("애니 검색 결과 리스트"),
                                    fieldWithPath("items[].id")
                                            .type(JsonFieldType.NUMBER)
                                            .description("애니의 고유 식별자"),
                                    fieldWithPath("items[].title")
                                            .type(JsonFieldType.STRING)
                                            .description("애니의 제목"),
                                    fieldWithPath("items[].thumbnail")
                                            .type(JsonFieldType.STRING)
                                            .description("애니의 이미지 경로"),
                                    fieldWithPath("items[].starScoreAvg")
                                            .type(JsonFieldType.NUMBER)
                                            .description("애니의 이미지 경로"),
                                    fieldWithPath("size")
                                            .type(JsonFieldType.NUMBER)
                                            .description("1-100, 기본 10"),
                                    fieldWithPath("hasNext")
                                            .type(JsonFieldType.BOOLEAN)
                                            .description("마지막 페이지 여부"),
                                    fieldWithPath("cursor")
                                            .type(JsonFieldType.STRING)
                                            .description("마지막 아이템의 제목, 다음 페이지 요청시 cursor로 사용. 다음 페이지가 없다면 \"\"")
                            )
                    ));

        }

        @Test
        @DisplayName("애니 상세 조회 성공 시 Http Status 200 반환")
        void getAnimeById() throws Exception {
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
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.thumbnail").exists())
                .andExpect(jsonPath("$.broadcastType").exists())
                .andExpect(jsonPath("$.year").exists())
                .andExpect(jsonPath("$.quarter").exists())
                .andExpect(jsonPath("$.summary").exists())
                .andExpect(jsonPath("$.episodeCount").exists())
                .andExpect(jsonPath("$.rating").exists())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.genres").exists())
                .andExpect(jsonPath("$.originalAuthors").exists())
                .andExpect(jsonPath("$.voiceActors").exists())
                .andExpect(jsonPath("$.voiceActors[0].name").exists())
                .andExpect(jsonPath("$.voiceActors[0].part").exists())
                .andExpect(jsonPath("$.studios").exists())
                .andExpect(jsonPath("$.reviewCount").exists())
                .andExpect(jsonPath("$.bookmarkCount").exists())
                .andDo(document("getAnimeById/success",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("animeId")
                            .description("애니의 고유 식별자")
                    ),
                    responseFields(
                        fieldWithPath("id")
                            .type(JsonFieldType.NUMBER)
                            .description("애니의 고유 식별자"),
                        fieldWithPath("title")
                            .type(JsonFieldType.STRING)
                            .description("애니 제목"),
                        fieldWithPath("thumbnail")
                            .type(JsonFieldType.STRING)
                            .description("애니 이미지, 경로로 저장됨"),
                        fieldWithPath("broadcastType")
                            .type(JsonFieldType.STRING)
                            .description("작품의 출시 방식. TVA, OVA, ONA, MOV가 있음"),
                        fieldWithPath("year")
                            .type(JsonFieldType.NUMBER)
                            .description("작품의 출시 년도"),
                        fieldWithPath("quarter")
                            .type(JsonFieldType.STRING)
                            .description("작품의 출시 분기"),
                        fieldWithPath("summary")
                            .type(JsonFieldType.STRING)
                            .description("애니를 소개할 때 줄거리"),
                        fieldWithPath("episodeCount")
                            .type(JsonFieldType.NUMBER)
                            .description("에피소드의 숫자. 16화까지 나왔으면 16으로 표기됨."),
                        fieldWithPath("rating")
                            .type(JsonFieldType.STRING)
                            .description("작품의 심의 등급. ADULT, FIFTEEN, TWELVE, ALL가 있다."),
                        fieldWithPath("status")
                            .type(JsonFieldType.STRING)
                            .description("방영 상태. FINISHED, ONGOING, UPCOMING, UNKNOWN이 있다."),
                        fieldWithPath("genres")
                            .type(JsonFieldType.ARRAY)
                            .description("애니의 장르."),
                        fieldWithPath("originalAuthors")
                            .type(JsonFieldType.ARRAY)
                            .description("애니의 원작 작가."),
                        fieldWithPath("voiceActors")
                            .type(JsonFieldType.ARRAY)
                            .description("애니 성우 리스트"),
                        fieldWithPath("voiceActors[].name")
                            .type(JsonFieldType.STRING)
                            .description("성우의 이름"),
                        fieldWithPath("voiceActors[].part")
                            .type(JsonFieldType.STRING)
                            .description("성우의 역할"),
                        fieldWithPath("studios")
                            .type(JsonFieldType.ARRAY)
                            .description("애니의 제작사."),
                        fieldWithPath("reviewCount")
                            .type(JsonFieldType.NUMBER)
                            .description("애니의 리뷰 개수."),
                        fieldWithPath("bookmarkCount")
                            .type(JsonFieldType.NUMBER)
                            .description("애니의 덕후 수.")
                    )
                ));

            //TODO : 조회 실패 시
        }
    }
}
