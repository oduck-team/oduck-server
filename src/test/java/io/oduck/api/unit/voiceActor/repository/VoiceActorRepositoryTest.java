package io.oduck.api.unit.voiceActor.repository;

import io.oduck.api.domain.voiceActor.entity.VoiceActor;
import io.oduck.api.domain.voiceActor.repository.VoiceActorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class VoiceActorRepositoryTest {

    @Autowired
    private VoiceActorRepository voiceActorRepository;

    @Nested
    @DisplayName("등록")
    class SaveVoiceActor {

        @Test
        @DisplayName("성우 등록 성공")
        void saveVoiceActor() {
            //given
            VoiceActor voiceActor = VoiceActor.builder()
                    .name("성우1")
                    .build();

            //when
            VoiceActor savedVoiceActor = voiceActorRepository.save(voiceActor);

            //then
            assertThat(voiceActor.getId()).isEqualTo(savedVoiceActor.getId());
            assertThat(voiceActor.getName()).isEqualTo(savedVoiceActor.getName());
        }
    }

    @Nested
    @DisplayName("조회")
    class FindSeries {
        @Test
        @DisplayName("성우 조회 성공")
        void findSeries() {
            //given
            VoiceActor voiceActor = VoiceActor.builder()
                    .name("성우A")
                    .build();

            VoiceActor savedVoiceActor = voiceActorRepository.save(voiceActor);

            //when
            List<VoiceActor> voiceActors = voiceActorRepository.findAll();

            //then
            assertThat(savedVoiceActor).isIn(voiceActors);
        }
    }
}
