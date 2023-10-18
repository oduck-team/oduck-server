package io.oduck.api.global.initializer;

import io.oduck.api.domain.anime.repository.AnimeRepository;
import io.oduck.api.global.stub.AnimeStub;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

@Component
@ActiveProfiles("test")
public class AnimeConstruct {

    @Autowired
    AnimeRepository animeRepository;

    @PostConstruct
    public void run() {
        AnimeStub animeStub = new AnimeStub();
        animeRepository.saveAll(animeStub.getAnimes());
    }
}
