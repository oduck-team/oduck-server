package io.oduck.api.e2e.admin;

import com.google.gson.Gson;
import io.oduck.api.domain.anime.dto.AnimeReq.*;
import io.oduck.api.domain.anime.dto.AnimeVoiceActorReq;
import io.oduck.api.domain.genre.dto.GenreReq;
import io.oduck.api.domain.originalAuthor.dto.OriginalAuthorReq;
import io.oduck.api.domain.originalAuthor.dto.OriginalAuthorReq.PatchReq;
import io.oduck.api.domain.series.dto.SeriesReq;
import io.oduck.api.domain.studio.dto.StudioReq;
import io.oduck.api.domain.voiceActor.dto.VoiceActorReq;
import io.oduck.api.global.utils.AnimeTestUtils;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static io.oduck.api.global.config.RestDocsConfig.field;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.attributes;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@AutoConfigureMockMvc
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    private final static String ADMIN_URL = "/oduckdmin";

    @Nested
    @DisplayName("애니")
    class AnimeTest{

        @Test
        @DisplayName("등록 성공 시 Http Status 200 반환")
        void postAnime() throws Exception {
            //given
            PostReq request = AnimeTestUtils.createPostAnimeRequest();
            String content = gson.toJson(request);

            //when
            ResultActions actions = mockMvc.perform(
                post(ADMIN_URL+"/animes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(content)
            );

            //then
            actions
                .andExpect(status().isOk())
                .andDo(document("postAnime/success",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(attributes(key("title").value("Fields for anime creation")),
                        fieldWithPath("title")
                            .type(JsonFieldType.STRING)
                            .attributes(field("constraints", "공백을 허용하지 않습니다. 1~50자를 허용합니다."))
                            .description("애니의 제목"),
                        fieldWithPath("summary")
                            .type(JsonFieldType.STRING)
                            .attributes(field("constraints", "공백을 허용하지 않습니다. 1~255자를 허용합니다."))
                            .description("애니 요약 설명"),
                        fieldWithPath("broadcastType")
                            .type(JsonFieldType.STRING)
                            .attributes(field("constraints", "TVA,ONA,OVA,MOV만 허용합니다."))
                            .description("애니 방송사"),
                        fieldWithPath("episodeCount")
                            .type(JsonFieldType.NUMBER)
                            .attributes(field("constraints", "0 이상의 숫자만 허용합니다."))
                            .description("애피소드 화수"),
                        fieldWithPath("thumbnail")
                            .type(JsonFieldType.STRING)
                            .attributes(field("constraints", "공백을 허용하지 않습니다."))
                            .description("애피소드 화수"),
                        fieldWithPath("year")
                            .type(JsonFieldType.NUMBER)
                            .attributes(field("constraints", "1900 이상의 숫자만 허용합니다."))
                            .description("애니 발행 년도"),
                        fieldWithPath("quarter")
                            .type(JsonFieldType.STRING)
                            .attributes(field("constraints", "Q1,Q2,Q3,Q4만 허용합니다."))
                            .description("애니 발행 분기"),
                        fieldWithPath("rating")
                            .type(JsonFieldType.STRING)
                            .attributes(field("constraints", "ADULT,FIFTEEN,TWELVE,ALL만 허용합니다."))
                            .description("애니 등급"),
                        fieldWithPath("status")
                            .type(JsonFieldType.STRING)
                            .attributes(field("constraints", "ONGOING,FINISHED,UPCOMING,UNKNOWN만 허용합니다."))
                            .description("애니 상태"),
                        fieldWithPath("isReleased")
                            .type(JsonFieldType.BOOLEAN)
                            .attributes(field("constraints", "true, false만 허용합니다. null일 시 false로 초기화합니다."))
                            .description("클라이언트가 애니를 열람할 수 있는지 여부"),
                        fieldWithPath("originalAuthorIds")
                            .type(JsonFieldType.ARRAY)
                            .description("원작 작가들 아이디 리스트")
                            .attributes(field("constraints", "List만 허용합니다."))
                            .optional(),
                        fieldWithPath("studioIds")
                            .type(JsonFieldType.ARRAY)
                            .description("애니 스튜디오 아이디 리스트")
                            .attributes(field("constraints", "List만 허용합니다."))
                            .optional(),
                        fieldWithPath("voiceActors")
                            .type(JsonFieldType.ARRAY)
                            .description("애니 성우 리스트")
                            .attributes(field("constraints", "List만 허용합니다."))
                            .optional(),
                        fieldWithPath("voiceActors[].id")
                            .type(JsonFieldType.NUMBER)
                            .description("성우의 아이디")
                            .attributes(field("constraints", "숫자만 허용합니다.")),
                        fieldWithPath("voiceActors[].part")
                            .type(JsonFieldType.STRING)
                            .description("성우의 역할")
                            .attributes(field("constraints", "100자 이하만 허용합니다.")),
                        fieldWithPath("genreIds")
                            .type(JsonFieldType.ARRAY)
                            .description("애니 장르 아이디 리스트")
                            .attributes(field("constraints", "List만 허용합니다."))
                            .optional(),
                        fieldWithPath("seriesId")
                            .type(JsonFieldType.NUMBER)
                            .description("애니 시리즈 아이디")
                            .attributes(field("constraints", "숫자만 허용합니다."))
                            .optional()
                    )
                ));
        }
        //TODO : 애니 등록 실패 시

        @Test
        @DisplayName("애니 수정 성공 시 Http Status 204 반환")
        void patchAnime() throws Exception {
            //given
            Long animeId = 1L;
            PatchAnimeReq req = AnimeTestUtils.createPatchAnimeRequest();
            String content = gson.toJson(req);

            //when
            ResultActions actions = mockMvc.perform(
                    patch(ADMIN_URL+"/animes/{animeId}", animeId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(content)
            );

            //then
            actions
                    .andExpect(status().isNoContent())
                    .andDo(document("patchAnime/success",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            pathParameters(
                                    parameterWithName("animeId").description("애니의 식별자")
                            ),
                            requestFields(attributes(key("title").value("Fields for anime creation")),
                                    fieldWithPath("title")
                                            .type(JsonFieldType.STRING)
                                            .attributes(field("constraints", "공백을 허용하지 않습니다. 1~50자를 허용합니다."))
                                            .description("애니의 제목"),
                                    fieldWithPath("summary")
                                            .type(JsonFieldType.STRING)
                                            .attributes(field("constraints", "공백을 허용하지 않습니다. 1~255자를 허용합니다."))
                                            .description("애니 요약 설명"),
                                    fieldWithPath("broadcastType")
                                            .type(JsonFieldType.STRING)
                                            .attributes(field("constraints", "TVA,ONA,OVA,MOV만 허용합니다."))
                                            .description("애니 방송사"),
                                    fieldWithPath("episodeCount")
                                            .type(JsonFieldType.NUMBER)
                                            .attributes(field("constraints", "0 이상의 숫자만 허용합니다."))
                                            .description("애피소드 화수"),
                                    fieldWithPath("thumbnail")
                                            .type(JsonFieldType.STRING)
                                            .attributes(field("constraints", "공백을 허용하지 않습니다."))
                                            .description("애피소드 화수"),
                                    fieldWithPath("year")
                                            .type(JsonFieldType.NUMBER)
                                            .attributes(field("constraints", "1900 이상의 숫자만 허용합니다."))
                                            .description("애니 발행 년도"),
                                    fieldWithPath("quarter")
                                            .type(JsonFieldType.STRING)
                                            .attributes(field("constraints", "Q1,Q2,Q3,Q4만 허용합니다."))
                                            .description("애니 발행 분기"),
                                    fieldWithPath("rating")
                                            .type(JsonFieldType.STRING)
                                            .attributes(field("constraints", "ADULT,FIFTEEN,TWELVE,ALL만 허용합니다."))
                                            .description("애니 등급"),
                                    fieldWithPath("status")
                                            .type(JsonFieldType.STRING)
                                            .attributes(field("constraints", "ONGOING,FINISHED,UPCOMING,UNKNOWN만 허용합니다."))
                                            .description("애니 상태"),
                                    fieldWithPath("isReleased")
                                        .type(JsonFieldType.BOOLEAN)
                                        .attributes(field("constraints", "true, false만 허용합니다. null일 시 false로 초기화합니다."))
                                        .description("클라이언트가 애니를 열람할 수 있는지 여부")
                            )
                    ));

        }
        //TODO: 수정 실패 시

        @Test
        @DisplayName("애니 원작 작가 수정 성공 시 Http Status 204 반환")
        void patchAnimeOriginalAuthors() throws Exception {
            //given
            Long animeId = 1L;
            List<Long> originalAuthorIds = AnimeTestUtils.getOriginalAuthorIds();

            PatchOriginalAuthorIdsReq req = new PatchOriginalAuthorIdsReq(originalAuthorIds);
            String content = gson.toJson(req);

            //when
            ResultActions actions = mockMvc.perform(
                    patch(ADMIN_URL+"/animes/{animeId}/original-authors", animeId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(content)
            );

            //then
            actions
                    .andDo(document("patchAnimeOriginalAuthors/success",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            pathParameters(
                                    parameterWithName("animeId").description("애니의 식별자")
                            ),
                            requestFields(attributes(key("title").value("Fields for anime creation")),
                                    fieldWithPath("originalAuthorIds")
                                            .type(JsonFieldType.ARRAY)
                                            .description("원작 작가들 아이디 리스트")
                                            .attributes(field("constraints", "List만 허용합니다."))
                                            .optional()
                            )
                    ));

        }
        //TODO: 수정 실패 시

        @Test
        @DisplayName("애니 원작 작가 수정 성공 시 Http Status 204 반환")
        void patchAnimeStudios() throws Exception {
            //given
            Long animeId = 1L;
            List<Long> studioIds = AnimeTestUtils.getStudioIds();

            PatchStudioIdsReq req = new PatchStudioIdsReq(studioIds);
            String content = gson.toJson(req);

            //when
            ResultActions actions = mockMvc.perform(
                    patch(ADMIN_URL+"/animes/{animeId}/studios", animeId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(content)
            );

            //then
            actions
                    .andDo(document("patchAnimeStudios/success",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            pathParameters(
                                    parameterWithName("animeId").description("애니의 식별자")
                            ),
                            requestFields(attributes(key("title").value("Fields for anime creation")),
                                    fieldWithPath("studioIds")
                                            .type(JsonFieldType.ARRAY)
                                            .description("스튜디오 아이디 리스트")
                                            .attributes(field("constraints", "List만 허용합니다."))
                                            .optional()
                            )
                    ));

        }
        //TODO: 수정 실패 시

        @Test
        @DisplayName("애니 성우 수정 성공 시 Http Status 204 반환")
        void patchAnimeVoiceActors() throws Exception {
            //given
            Long animeId = 1L;
            List<AnimeVoiceActorReq> voiceActors = AnimeTestUtils.getVoiceActorReqs();

            PatchVoiceActorIdsReq req = new PatchVoiceActorIdsReq(voiceActors);
            String content = gson.toJson(req);

            //when
            ResultActions actions = mockMvc.perform(
                    put(ADMIN_URL+"/animes/{animeId}/voice-actors", animeId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(content)
            );

            //then
            actions
                    .andDo(document("patchAnimeVoiceActors/success",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            pathParameters(
                                    parameterWithName("animeId").description("애니의 식별자")
                            ),
                            requestFields(attributes(key("title").value("Fields for anime creation")),
                                    fieldWithPath("voiceActors")
                                            .type(JsonFieldType.ARRAY)
                                            .description("애니 성우 리스트")
                                            .attributes(field("constraints", "List만 허용합니다."))
                                            .optional(),
                                    fieldWithPath("voiceActors[].id")
                                            .type(JsonFieldType.NUMBER)
                                            .description("성우의 아이디")
                                            .attributes(field("constraints", "숫자만 허용합니다.")),
                                    fieldWithPath("voiceActors[].part")
                                            .type(JsonFieldType.STRING)
                                            .description("성우의 역할")
                                            .attributes(field("constraints", "100자 이하만 허용합니다."))
                            )
                    ));
        }
        //TODO: 수정 실패 시

        @Test
        @DisplayName("애니 장르 수정 성공 시 Http Status 204 반환")
        void patchAnimeGenres() throws Exception {
            //given
            Long animeId = 1L;
            List<Long> genreIds = AnimeTestUtils.getGenreIds();

            PatchGenreIdsReq req = new PatchGenreIdsReq(genreIds);
            String content = gson.toJson(req);

            //when
            ResultActions actions = mockMvc.perform(
                    put(ADMIN_URL+"/animes/{animeId}/genres", animeId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(content)
            );

            //then
            actions
                    .andDo(document("patchAnimeGenres/success",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            pathParameters(
                                    parameterWithName("animeId").description("애니의 식별자")
                            ),
                            requestFields(attributes(key("title").value("Fields for anime creation")),
                                    fieldWithPath("genreIds")
                                            .type(JsonFieldType.ARRAY)
                                            .description("애니 장르 아이디 리스트")
                                            .attributes(field("constraints", "List만 허용합니다."))
                                            .optional()
                            )
                    ));
        }
        //TODO: 수정 실패 시

        @Test
        @DisplayName("시리즈 수정 성공 시 Http Status 204 반환")
        void patchAnimeSeries() throws Exception {
            //given
            Long animeId = 1L;
            PatchSeriesIdReq req = new PatchSeriesIdReq(AnimeTestUtils.getSeriesId());
            String content = gson.toJson(req);

            //when
            ResultActions actions = mockMvc.perform(
                    patch(ADMIN_URL+"/animes/{animeId}/series", animeId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(content)
            );

            //then
            actions
                    .andExpect(status().isNoContent())
                    .andDo(document("patchAnimeSeries/success",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            pathParameters(
                                    parameterWithName("animeId").description("애니의 식별자")
                            ),
                            requestFields(attributes(key("title").value("Fields for anime creation")),
                                    fieldWithPath("seriesId")
                                            .type(JsonFieldType.NUMBER)
                                            .description("애니 시리즈 아이디")
                                            .attributes(field("constraints", "숫자만 허용합니다."))
                                            .optional()
                            )
                    ));
        }
        //TODO: 수정 실패 시

        @Test
        @DisplayName("애니 삭제 성공 시 Http Status 204 반환")
        void deleteAnime() throws Exception {
            //given
            Long animeId = 1L;

            //when
            ResultActions actions = mockMvc.perform(
                delete(ADMIN_URL+"/animes/{animeId}", animeId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            );

            //then
            actions
                .andExpect(status().isNoContent())
                .andDo(document("deleteAnime/success",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("animeId").description("애니의 식별자")
                    )
                ));
        }
    }

    @Nested
    @DisplayName("원작 작가")
    class OriginalAuthorTest{
        @Test
        @DisplayName("조회 성공 시 Http status 200 반환")
        void getOriginalAuthors() throws Exception {
            ResultActions actions = mockMvc.perform(
                    RestDocumentationRequestBuilders.get(ADMIN_URL+"/original-authors")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
            );

            actions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id").exists())
                    .andExpect(jsonPath("$[0].name").exists())
                    .andDo(document("getOriginalAuthors/success",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            responseFields(
                        fieldWithPath("[].id")
                                            .type(JsonFieldType.NUMBER)
                                            .description("원작 작가의 고유 식별자"),
                                    fieldWithPath("[].name")
                                            .type(JsonFieldType.STRING)
                                            .description("원작 작가의 이름")
                            )
                    ));
        }

        @Test
        @DisplayName("등록 성공 시 Http Status 204 반환")
        void postOriginalAuthor() throws Exception {
            String name = "원작작가A";
            OriginalAuthorReq.PostReq postReq = new OriginalAuthorReq.PostReq(name);
            String content = gson.toJson(postReq);

            ResultActions actions = mockMvc.perform(
                    post(ADMIN_URL + "/original-authors")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(content)
            );

            actions
                    .andExpect(status().isNoContent())
                    .andDo(document("postOriginalAuthor/success",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            requestFields(attributes(key("title").value("Fields for original author creation")),
                                    fieldWithPath("name")
                                            .type(JsonFieldType.STRING)
                                            .attributes(field("constraints", "공백을 허용하지 않습니다. 1~50자를 허용합니다."))
                                            .description("원작 작가의 이름")
                            )
                    ));
        }

        @Test
        @DisplayName("등록 실패 - 중복된 이름 : Http Status 409 반환")
        void whenDuplicatedNameThenThrowException() throws Exception {
            //given
            String name = "고토게 코요하루";
            OriginalAuthorReq.PostReq postReq = new OriginalAuthorReq.PostReq(name);
            String content = gson.toJson(postReq);

            //when
            ResultActions actions = mockMvc.perform(
                post(ADMIN_URL + "/original-authors")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(content)
            );

            //then
            actions
                .andExpect(status().isConflict())
                .andDo(document("postOriginalAuthor/fail",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(attributes(key("title").value("Fields for original author creation")),
                        fieldWithPath("name")
                            .type(JsonFieldType.STRING)
                            .attributes(field("constraints", "공백을 허용하지 않습니다. 1~50자를 허용합니다."))
                            .description("원작 작가의 이름")
                    )
                ));
        }

        @Test
        @DisplayName("수정 성공 시 Http Status 204 반환")
        void patchOriginalAuthor() throws Exception {
            //given
            Long originalAuthorId = 1L;
            OriginalAuthorReq.PatchReq req = new PatchReq("원작 작가 수정");
            String content = gson.toJson(req);

            //when
            ResultActions actions = mockMvc.perform(
                patch(ADMIN_URL+"/original-authors/{originalAuthorId}", originalAuthorId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(content)
            );

            //then
            actions
                .andExpect(status().isNoContent())
                .andDo(document("patchOriginalAuthor/success",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("originalAuthorId").description("원작 작가의 식별자")
                    ),
                    requestFields(attributes(key("title").value("Fields for original author creation")),
                        fieldWithPath("name")
                            .type(JsonFieldType.STRING)
                            .attributes(field("constraints", "공백을 허용하지 않습니다. 1~50자를 허용합니다."))
                            .description("원작 작가의 이름")
                    )
                ));
        }

        @Test
        @DisplayName("삭제 성공 시 Http Status 204 반환")
        void deleteOriginalAuthor() throws Exception {
            //given
            Long originalAuthorId = 1L;

            //when
            ResultActions actions = mockMvc.perform(
                delete(ADMIN_URL+"/original-authors/{originalAuthorId}", originalAuthorId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            );

            //then
            actions
                .andExpect(status().isNoContent())
                .andDo(document("deleteOriginalAuthor/success",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("originalAuthorId").description("원작 작가의 식별자")
                    )
                ));
        }
    }

    @Nested
    @DisplayName("성우")
    class VoiceActorTest{
        @Test
        @DisplayName("조회 성공 시 Http status 200 반환")
        void getOriginalAuthors() throws Exception {
            ResultActions actions = mockMvc.perform(
                    RestDocumentationRequestBuilders.get(ADMIN_URL+"/voice-actors")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
            );

            actions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id").exists())
                    .andExpect(jsonPath("$[0].name").exists())
                    .andDo(document("getVoiceActors/success",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            responseFields(
                                    fieldWithPath("[].id")
                                            .type(JsonFieldType.NUMBER)
                                            .description("성우의 고유 식별자"),
                                    fieldWithPath("[].name")
                                            .type(JsonFieldType.STRING)
                                            .description("성우의 이름")
                            )
                    ));
        }

        @Test
        @DisplayName("등록 성공 시 Http Status 204 반환")
        void postVoiceActor() throws Exception {
            String name = "에구치 타쿠야";
            VoiceActorReq.PostReq postReq = new VoiceActorReq.PostReq(name);
            String content = gson.toJson(postReq);

            ResultActions actions = mockMvc.perform(
                    post(ADMIN_URL + "/voice-actors")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(content)
            );

            actions
                    .andExpect(status().isNoContent())
                    .andDo(document("postVoiceActor/success",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            requestFields(attributes(key("title").value("Fields for voice actor creation")),
                                    fieldWithPath("name")
                                            .type(JsonFieldType.STRING)
                                            .attributes(field("constraints", "공백을 허용하지 않습니다. 1~50자를 허용합니다."))
                                            .description("성우의 이름")
                            )
                    ));
        }

        @Test
        @DisplayName("등록 실패 - 중복된 이름 : Http Status 409 반환")
        void whenDuplicatedNameThenThrowException() throws Exception {
            //given
            String name = "하나에 나츠키";
            VoiceActorReq.PostReq postReq = new VoiceActorReq.PostReq(name);
            String content = gson.toJson(postReq);

            //when
            ResultActions actions = mockMvc.perform(
                post(ADMIN_URL + "/voice-actors")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(content)
            );

            //then
            actions
                .andExpect(status().isConflict())
                .andDo(document("postVoiceActor/fail",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(attributes(key("title").value("Fields for original author creation")),
                        fieldWithPath("name")
                            .type(JsonFieldType.STRING)
                            .attributes(field("constraints", "공백을 허용하지 않습니다. 1~50자를 허용합니다."))
                            .description("성우의 이름")
                    )
                ));
        }

        @Test
        @DisplayName("수정 성공 시 Http Status 204 반환")
        void patchVoiceActor() throws Exception {
            //given
            Long voiceActorId = 1L;
            VoiceActorReq.PatchReq req = new VoiceActorReq.PatchReq("성우 수정");
            String content = gson.toJson(req);

            //when
            ResultActions actions = mockMvc.perform(
                patch(ADMIN_URL+"/voice-actors/{voiceActorId}", voiceActorId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(content)
            );

            //then
            actions
                .andExpect(status().isNoContent())
                .andDo(document("patchVoiceActor/success",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("voiceActorId").description("성우의 식별자")
                    ),
                    requestFields(attributes(key("title").value("Fields for voice actor creation")),
                        fieldWithPath("name")
                            .type(JsonFieldType.STRING)
                            .attributes(field("constraints", "공백을 허용하지 않습니다. 1~50자를 허용합니다."))
                            .description("성우의 이름")
                    )
                ));
        }

        @Test
        @DisplayName("삭제 성공 시 Http Status 204 반환")
        void deleteVoiceActor() throws Exception {
            //given
            Long voiceActorId = 1L;

            //when
            ResultActions actions = mockMvc.perform(
                delete(ADMIN_URL+"/voice-actors/{voiceActorId}", voiceActorId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            );

            //then
            actions
                .andExpect(status().isNoContent())
                .andDo(document("deleteVoiceActor/success",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("voiceActorId").description("성우의 식별자")
                    )
                ));
        }
    }

    @Nested
    @DisplayName("스튜디오")
    class StudioTest{
        @Test
        @DisplayName("조회 성공 시 Http status 200 반환")
        void getStudios() throws Exception {
            ResultActions actions = mockMvc.perform(
                    RestDocumentationRequestBuilders.get(ADMIN_URL+"/studios")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
            );

            actions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id").exists())
                    .andExpect(jsonPath("$[0].name").exists())
                    .andDo(document("getStudios/success",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            responseFields(
                                    fieldWithPath("[].id")
                                            .type(JsonFieldType.NUMBER)
                                            .description("스튜디오의 고유 식별자"),
                                    fieldWithPath("[].name")
                                            .type(JsonFieldType.STRING)
                                            .description("스튜디오의 이름")
                            )
                    ));
        }

        @Test
        @DisplayName("등록 성공 시 Http Status 204 반환")
        void postStudio() throws Exception {
            String name = "스튜디오A";
            StudioReq.PostReq postReq = new StudioReq.PostReq(name);
            String content = gson.toJson(postReq);

            ResultActions actions = mockMvc.perform(
                    post(ADMIN_URL + "/studios")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(content)
            );

            actions
                    .andExpect(status().isNoContent())
                    .andDo(document("postStudio/success",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            requestFields(attributes(key("title").value("Fields for original author creation")),
                                    fieldWithPath("name")
                                            .type(JsonFieldType.STRING)
                                            .attributes(field("constraints", "공백을 허용하지 않습니다. 1~50자를 허용합니다."))
                                            .description("스튜디오의 이름")
                            )
                    ));
        }

        @Test
        @DisplayName("등록 실패 - 중복된 이름 : Http Status 409 반환")
        void whenDuplicatedNameThenThrowException() throws Exception {
            //given
            String name = "ufortable";
            StudioReq.PostReq postReq = new StudioReq.PostReq(name);
            String content = gson.toJson(postReq);

            //when
            ResultActions actions = mockMvc.perform(
                post(ADMIN_URL + "/studios")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(content)
            );

            //then
            actions
                .andExpect(status().isConflict())
                .andDo(document("postStudio/fail",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(attributes(key("title").value("Fields for studio creation")),
                        fieldWithPath("name")
                            .type(JsonFieldType.STRING)
                            .attributes(field("constraints", "공백을 허용하지 않습니다. 1~50자를 허용합니다."))
                            .description("스튜디오의 이름")
                    )
                ));
        }

        @Test
        @DisplayName("수정 성공 시 Http Status 204 반환")
        void patchStudio() throws Exception {
            //given
            Long studioId = 1L;
            StudioReq.PatchReq req = new StudioReq.PatchReq("스튜디오 수정");
            String content = gson.toJson(req);

            //when
            ResultActions actions = mockMvc.perform(
                patch(ADMIN_URL+"/studios/{studioId}", studioId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(content)
            );

            //then
            actions
                .andExpect(status().isNoContent())
                .andDo(document("patchStudio/success",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("studioId").description("스튜디오의 식별자")
                    ),
                    requestFields(attributes(key("title").value("Fields for studio creation")),
                        fieldWithPath("name")
                            .type(JsonFieldType.STRING)
                            .attributes(field("constraints", "공백을 허용하지 않습니다. 1~50자를 허용합니다."))
                            .description("스튜디오의 이름")
                    )
                ));
        }

        @Test
        @DisplayName("삭제 성공 시 Http Status 204 반환")
        void deleteStudio() throws Exception {
            //given
            Long studioId = 1L;

            //when
            ResultActions actions = mockMvc.perform(
                delete(ADMIN_URL+"/studios/{studioId}", studioId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            );

            //then
            actions
                .andExpect(status().isNoContent())
                .andDo(document("deleteStudio/success",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("studioId").description("스튜디오의 식별자")
                    )
                ));
        }
    }


    @Nested
    @DisplayName("장르")
    class GenreTest{
        @Test
        @DisplayName("조회 성공 시 Http status 200 반환")
        void getGenres() throws Exception {
            ResultActions actions = mockMvc.perform(
                    RestDocumentationRequestBuilders.get("/genres")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
            );

            actions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id").exists())
                    .andExpect(jsonPath("$[0].name").exists())
                    .andDo(document("getGenres/success",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            responseFields(
                                    fieldWithPath("[].id")
                                            .type(JsonFieldType.NUMBER)
                                            .description("장르의 고유 식별자"),
                                    fieldWithPath("[].name")
                                            .type(JsonFieldType.STRING)
                                            .description("장르의 이름")
                            )
                    ));
        }

        @Test
        @DisplayName("등록 성공 시 Http Status 204 반환")
        void postGenre() throws Exception {
            String name = "이세계";
            GenreReq.PostReq postReq = new GenreReq.PostReq(name);
            String content = gson.toJson(postReq);

            ResultActions actions = mockMvc.perform(
                    post(ADMIN_URL + "/genres")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(content)
            );

            actions
                    .andExpect(status().isNoContent())
                    .andDo(document("postGenre/success",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            requestFields(attributes(key("title").value("Fields for original author creation")),
                                    fieldWithPath("name")
                                            .type(JsonFieldType.STRING)
                                            .attributes(field("constraints", "공백을 허용하지 않습니다. 1~50자를 허용합니다."))
                                            .description("장르의 이름")
                            )
                    ));
        }

        @Test
        @DisplayName("등록 실패 - 중복된 이름 : Http Status 409 반환")
        void whenDuplicatedNameThenThrowException() throws Exception {
            //given
            String name = "판타지";
            GenreReq.PostReq postReq = new GenreReq.PostReq(name);
            String content = gson.toJson(postReq);

            //when
            ResultActions actions = mockMvc.perform(
                post(ADMIN_URL + "/genres")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(content)
            );

            //then
            actions
                .andExpect(status().isConflict())
                .andDo(document("postGenre/fail",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(attributes(key("title").value("Fields for genre creation")),
                        fieldWithPath("name")
                            .type(JsonFieldType.STRING)
                            .attributes(field("constraints", "공백을 허용하지 않습니다. 1~15자를 허용합니다."))
                            .description("장르의 이름")
                    )
                ));
        }

        @Test
        @DisplayName("수정 성공 시 Http Status 204 반환")
        void patchGenre() throws Exception {
            //given
            Long genreId = 1L;
            GenreReq.PatchReq req = new GenreReq.PatchReq("장르 수정");
            String content = gson.toJson(req);

            //when
            ResultActions actions = mockMvc.perform(
                patch(ADMIN_URL+"/genres/{genreId}", genreId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(content)
            );

            //then
            actions
                .andExpect(status().isNoContent())
                .andDo(document("patchGenre/success",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("genreId").description("장르의 식별자")
                    ),
                    requestFields(attributes(key("title").value("Fields for studio creation")),
                        fieldWithPath("name")
                            .type(JsonFieldType.STRING)
                            .attributes(field("constraints", "공백을 허용하지 않습니다. 1~15자를 허용합니다."))
                            .description("장르의 이름")
                    )
                ));
        }

        @Test
        @DisplayName("삭제 성공 시 Http Status 204 반환")
        void deleteGenre() throws Exception {
            //given
            Long genreId = 1L;

            //when
            ResultActions actions = mockMvc.perform(
                delete(ADMIN_URL+"/genres/{genreId}", genreId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            );

            //then
            actions
                .andExpect(status().isNoContent())
                .andDo(document("deleteGenre/success",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("genreId").description("장르의 식별자")
                    )
                ));
        }
    }


    @Nested
    @DisplayName("시리즈")
    class SeriesTest{
        @Test
        @DisplayName("조회 성공 시 Http status 200 반환")
        void getSeries() throws Exception {
            ResultActions actions = mockMvc.perform(
                    RestDocumentationRequestBuilders.get(ADMIN_URL+"/series")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
            );

            actions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id").exists())
                    .andExpect(jsonPath("$[0].title").exists())
                    .andDo(document("getSeries/success",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            responseFields(
                                    fieldWithPath("[].id")
                                            .type(JsonFieldType.NUMBER)
                                            .description("시리즈의 고유 식별자"),
                                    fieldWithPath("[].title")
                                            .type(JsonFieldType.STRING)
                                            .description("시리즈의 제목")
                            )
                    ));
        }

        @Test
        @DisplayName("등록 성공 시 Http Status 204 반환")
        void postSeries() throws Exception {
            String title = "스파이 패밀리";
            SeriesReq.PostReq postReq = new SeriesReq.PostReq(title);
            String content = gson.toJson(postReq);

            ResultActions actions = mockMvc.perform(
                    post(ADMIN_URL + "/series")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(content)
            );

            actions
                    .andExpect(status().isNoContent())
                    .andDo(document("postSeries/success",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            requestFields(attributes(key("title").value("Fields for original author creation")),
                                    fieldWithPath("title")
                                            .type(JsonFieldType.STRING)
                                            .attributes(field("constraints", "공백을 허용하지 않습니다. 1~50자를 허용합니다."))
                                            .description("시리즈의 제목")
                            )
                    ));
        }

        @Test
        @DisplayName("등록 실패 - 중복된 이름 : Http Status 409 반환")
        void whenDuplicatedNameThenThrowException() throws Exception {
            //given
            String name = "원피스";
            SeriesReq.PostReq postReq = new SeriesReq.PostReq(name);
            String content = gson.toJson(postReq);

            //when
            ResultActions actions = mockMvc.perform(
                post(ADMIN_URL + "/series")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(content)
            );

            //then
            actions
                .andExpect(status().isConflict())
                .andDo(document("postSeries/fail",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(attributes(key("title").value("Fields for series creation")),
                        fieldWithPath("title")
                            .type(JsonFieldType.STRING)
                            .attributes(field("constraints", "공백을 허용하지 않습니다. 1~50자를 허용합니다."))
                            .description("시리즈의 제목")
                    )
                ));
        }

        @Test
        @DisplayName("수정 성공 시 Http Status 204 반환")
        void patchSeries() throws Exception {
            //given
            Long seriesId = 1L;
            SeriesReq.PatchReq req = new SeriesReq.PatchReq("시리즈 수정");
            String content = gson.toJson(req);

            //when
            ResultActions actions = mockMvc.perform(
                patch(ADMIN_URL+"/series/{seriesId}", seriesId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(content)
            );

            //then
            actions
                .andExpect(status().isNoContent())
                .andDo(document("patchSeries/success",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("seriesId").description("시리즈의 식별자")
                    ),
                    requestFields(attributes(key("title").value("Fields for studio creation")),
                        fieldWithPath("title")
                            .type(JsonFieldType.STRING)
                            .attributes(field("constraints", "공백을 허용하지 않습니다. 1~50자를 허용합니다."))
                            .description("시리즈의 이름")
                    )
                ));
        }

        @Test
        @DisplayName("삭제 성공 시 Http Status 204 반환")
        void deleteSeries() throws Exception {
            //given
            Long seriesId = 1L;

            //when
            ResultActions actions = mockMvc.perform(
                delete(ADMIN_URL+"/series/{seriesId}", seriesId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            );

            //then
            actions
                .andExpect(status().isNoContent())
                .andDo(document("deleteSeries/success",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("seriesId").description("시리즈의 식별자")
                    )
                ));
        }
    }

}
