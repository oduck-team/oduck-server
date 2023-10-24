package io.oduck.api.e2e.admin;

import static io.oduck.api.global.config.RestDocsConfig.field;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.attributes;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import io.oduck.api.domain.anime.dto.AnimeReq.PatchAnimeReq;
import io.oduck.api.domain.anime.dto.AnimeReq.PatchGenreIdsReq;
import io.oduck.api.domain.anime.dto.AnimeReq.PatchOriginalAuthorIdsReq;
import io.oduck.api.domain.anime.dto.AnimeReq.PatchSeriesIdReq;
import io.oduck.api.domain.anime.dto.AnimeReq.PatchStudioIdsReq;
import io.oduck.api.domain.anime.dto.AnimeReq.PatchVoiceActorIdsReq;
import io.oduck.api.domain.anime.dto.AnimeReq.PostReq;
import io.oduck.api.domain.anime.dto.VoiceActorReq;
import io.oduck.api.global.utils.AnimeTestUtils;
import java.util.List;
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
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest
@ActiveProfiles("test")
public class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    private final static String ADMIN_URL = "/oduckdmin";

    @Nested
    @DisplayName("등록")
    class PostAnime{

        @Test
        @DisplayName("성공 시 Http Status 200 반환")
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
    }

    @Nested
    @DisplayName("수정")
    class PatchAnime{

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
                            .description("애니 상태")
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
            List<VoiceActorReq> voiceActors = AnimeTestUtils.getVoiceActorReqs();

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
        @DisplayName("애니의 시리즈 수정 성공 시 Http Status 204 반환")
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
    }
}
