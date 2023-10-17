package io.oduck.api.global.utils;

import io.oduck.api.domain.anime.dto.AnimeReq.PatchAnimeReq;
import io.oduck.api.domain.anime.dto.AnimeReq.PostReq;
import io.oduck.api.domain.anime.entity.BroadcastType;
import io.oduck.api.domain.anime.entity.Quarter;
import io.oduck.api.domain.anime.entity.Rating;
import io.oduck.api.domain.anime.entity.Status;
import java.util.ArrayList;
import java.util.List;

public class AnimeTestUtils {
    public static PostReq createPostAnimeRequest(){
        return new PostReq(
            getTitle(), getSummary(), getBroadcastType(), getEpisodeCount(),
            getThumbnail(), getYear(), getQuarter(), getRating(), getStatus(),
            getOriginalAuthorIds(), getStudioIds(), getVoiceActorIds(), getGenreIds(), getSeriesId());
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

    public static List getVoiceActorIds() {
        List voiceActorIds = getStudioIds();
        voiceActorIds.add(2L);
        voiceActorIds.add(4L);
        return voiceActorIds;
    }

    public static List getStudioIds() {
        List studioIds = new ArrayList();
        studioIds.add(1L);
        return studioIds;
    }

    public static List getOriginalAuthorIds() {
        List originalAuthorIds = new ArrayList();
        originalAuthorIds.add(5L);
        return originalAuthorIds;
    }
}
