package io.oduck.api.domain.anime.service;

import io.oduck.api.domain.anime.dto.AnimeRes;
import io.oduck.api.domain.anime.dto.AnimeRes.Anime;
import io.oduck.api.domain.anime.dto.AnimeRes.Broadcast;
import io.oduck.api.domain.anime.entity.BroadcastType;
import io.oduck.api.domain.anime.entity.Quarter;
import io.oduck.api.domain.anime.entity.Rating;
import io.oduck.api.domain.anime.entity.Status;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AnimeServiceStub implements AnimeService{

    @Override
    public AnimeRes getAnimeById(Long animeId) {
        Anime anime = createAnime(animeId);

        return AnimeRes.builder()
            .anime(anime)
            .build();
    }

    private Anime createAnime(Long animeId) {
        return Anime.builder()
            .animeId(animeId)
            .title("귀멸의 칼날: 도공 마을편")
            .thumbnail("https://image파일경로/uuid.jpg")
            .broadcast(getBroadcast())
            .summary(
                "113년 만에 상현 혈귀가 죽자 분개한 무잔은 나머지 상현 혈귀들에게 또 다른 명령을 내린다! 한편, 규타로와의 전투 도중 검이 심하게 손상된 탄지로에게 하가네즈카는 대 격노하고 탄지로는 그 검을 만든 대장장이 하가네즈카 호타루에게 검이 어떻게 심하게 손상되었는지 설명하기 위해 도공 마을을 방문한다. 탄지로가 검이 수리되기를 기다리는 동안, 상현 혈귀 한텐구와 쿗코가 숨겨진 마을인 ‘도공 마을'을 습격한다. 공격할 때마다 분열해서 위력이 커지는 한텐구로 인해 탄지로와 겐야는 고전을 면치 못한다. 한편, 타인에 대한 관심이 희박한 하주 토키토 무이치로는 혈귀들에게 공격당하고 있는 코테츠를 목격하는데….")
            .episodeCount(11)
            .rating(Rating.ADULT)
            .status(Status.FINISHED)
            .genres(getGenres())
            .originalAuthors(getOriginalAuthors())
            .voiceActors(getVoiceActors())
            .studios(getStudios())
            .reviewCount(172)
            .bookmarkCount(72)
            .build();
    }

    private Broadcast getBroadcast() {
        return Broadcast.builder()
            .broadcastType(BroadcastType.TVA)
            .year(2023)
            .quarter(Quarter.Q2)
            .build();

    }

    private List<String> getStudios() {
        List<String> studios = new ArrayList<>();
        studios.add("ufotable");
        return studios;
    }

    private List<String> getVoiceActors() {
        List<String> voiceActors = new ArrayList<>();
        voiceActors.add("하나에 나츠키");
        voiceActors.add("키토 아카리");
        voiceActors.add("카와니시 켄고");
        voiceActors.add("하나자와 카나");
        voiceActors.add("오카모토 노부히코");
        voiceActors.add("나미카와 다이스케");
        voiceActors.add("후루카와 토시오");
        voiceActors.add("토리우미 코스케");
        voiceActors.add("타케우치 슌스케");
        voiceActors.add("우메하라 유이치로");
        voiceActors.add("사이토 소마");
        voiceActors.add("이시카와 카이토");
        voiceActors.add("야마데라 코이치");
        voiceActors.add("무라세 아유무");
        voiceActors.add("타케모토 에이지");
        voiceActors.add("야라 유사쿠");
        voiceActors.add("시모노 히로");
        voiceActors.add("마츠오카 요시츠구");
        voiceActors.add("우에다 레이나");
        voiceActors.add("에하라 유리");
        return voiceActors;
    }

    private List<String> getGenres(){
        List<String> genres = new ArrayList<>();
        genres.add("판타지");
        genres.add("액션");
        return genres;
    }

    private List<String> getOriginalAuthors() {
        List<String> originalAuthors = new ArrayList<>();
        originalAuthors.add("고토게 코요하루");
        return originalAuthors;
    }
}
