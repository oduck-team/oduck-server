package io.oduck.api.global.utils;

import io.oduck.api.domain.anime.dto.AnimeReq.PatchAnimeReq;
import io.oduck.api.domain.anime.dto.AnimeReq.PostReq;
import io.oduck.api.domain.anime.dto.AnimeVoiceActorReq;
import io.oduck.api.domain.anime.entity.Anime;
import io.oduck.api.domain.anime.entity.BroadcastType;
import io.oduck.api.domain.anime.entity.Quarter;
import io.oduck.api.domain.anime.entity.Rating;
import io.oduck.api.domain.anime.entity.Status;
import io.oduck.api.domain.genre.entity.Genre;
import io.oduck.api.domain.originalAuthor.entity.OriginalAuthor;
import io.oduck.api.domain.series.entity.Series;
import io.oduck.api.domain.studio.entity.Studio;
import io.oduck.api.domain.voiceActor.entity.VoiceActor;
import java.util.ArrayList;
import java.util.List;

public class AnimeTestUtils {
    public static PostReq createPostAnimeRequest(){
        return new PostReq(
            getTitle(), getSummary(), getBroadcastType(), getEpisodeCount(),
            getThumbnail(), getYear(), getQuarter(), getRating(), getStatus(),
            getOriginalAuthorIds(), getStudioIds(), getVoiceActorReqs(), getGenreIds(), getSeriesId());
    }

    public static PatchAnimeReq createPatchAnimeRequest(){
        return new PatchAnimeReq(
            getTitle(), getSummary(), getBroadcastType(), getEpisodeCount(),
            getThumbnail(), getYear(), getQuarter(), getRating(), getStatus());
    }

    public static long getSeriesId() {
        return 1L;
    }

    public static Status getStatus() {
        return Status.FINISHED;
    }

    public static Rating getRating() {
        return Rating.ADULT;
    }

    public static Quarter getQuarter() {
        return Quarter.Q2;
    }

    public static int getYear() {
        return 2023;
    }

    public static String getThumbnail() {
        return "/uuid.jpg";
    }

    public static int getEpisodeCount() {
        return 11;
    }

    public static BroadcastType getBroadcastType() {
        return BroadcastType.TVA;
    }

    public static String getSummary() {
        return "113년 만에 상현 혈귀가 죽자 분개한 무잔은 나머지 상현 혈귀들에게 또 다른 명령을 내린다! 한편, 규타로와의 전투 도중 검이 심하게 손상된 탄지로에게 하가네즈카는 대 격노하고 탄지로는 그 검을 만든 대장장이 하가네즈카 호타루에게 검이 어떻게 심하게 손상되었는지 설명하기 위해 도공 마을을 방문한다.";
    }

    public static String getTitle() {
        return "귀멸의 칼날: 도공 마을편";
    }


    public static List getGenreIds() {
        List genreIds = new ArrayList();
        genreIds.add(4L);
        genreIds.add(5L);
        return genreIds;
    }

    public static List<AnimeVoiceActorReq> getVoiceActorReqs() {
        List<AnimeVoiceActorReq> voiceActors = new ArrayList<>();
        AnimeVoiceActorReq firstDto = new AnimeVoiceActorReq(5L, "카마도 탄지로");
        AnimeVoiceActorReq secondDto = new AnimeVoiceActorReq(6L, "카마도 네즈코");
        voiceActors.add(firstDto);
        voiceActors.add(secondDto);
        return voiceActors;
    }

    public static List getStudioIds() {
        List studioIds = new ArrayList();
        studioIds.add(1L);
        return studioIds;
    }

    public static List getOriginalAuthorIds() {
        List originalAuthorIds = new ArrayList();
        originalAuthorIds.add(1L);
        return originalAuthorIds;
    }

    public static Anime createAnime() {
        Anime anime = Anime.createAnime(getTitle(), getSummary(), getBroadcastType(), getEpisodeCount(),
            getThumbnail(), getYear(), getQuarter(), getRating(), getStatus(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), null);
        return anime;
    }

    public static List<OriginalAuthor> getOriginalAuthors() {
        List<OriginalAuthor> originalAuthors = new ArrayList<>();

        for(int i = 0; i<2; i++){
            OriginalAuthor originalAuthor = OriginalAuthor.builder()
                .name("작가"+i)
                .build();
            originalAuthors.add(originalAuthor);
        }
        return originalAuthors;
    }

    public static List<VoiceActor> getVoiceActors() {
        List<VoiceActor> voiceActors = new ArrayList<>();

        VoiceActor firstVoiceActor = VoiceActor.builder()
            .id(5L)
            .name("성우A")
            .build();
        VoiceActor secondVoiceActor = VoiceActor.builder()
            .id(6L)
            .name("성우B")
            .build();

        voiceActors.add(firstVoiceActor);
        voiceActors.add(firstVoiceActor);
        return voiceActors;
    }

    public static List<Long> getVoiceActorIds() {
        List<Long> voiceActorIds = new ArrayList<>();
        voiceActorIds.add(5L);
        voiceActorIds.add(6L);
        return voiceActorIds;
    }

    public static List<Studio> getStudios() {
        List<Studio> studios = new ArrayList<>();
        Studio studio = Studio.builder()
            .name("ufortable")
            .build();

        studios.add(studio);
        return studios;
    }

    public static List<Genre> getGenres() {
        List<Genre> genres = new ArrayList<>();

        for(int i = 0; i < 2; i++){
            Genre genre = Genre.builder()
                .name("장르"+i)
                .build();
            genres.add(genre);
        }
        return genres;
    }

    public static Series getSeries() {
        return Series.builder()
            .title("귀멸의 칼날")
            .build();
    }
}
