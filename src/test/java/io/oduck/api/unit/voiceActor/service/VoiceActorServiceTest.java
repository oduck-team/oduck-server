package io.oduck.api.unit.voiceActor.service;

import io.oduck.api.domain.voiceActor.dto.VoiceActorReq;
import io.oduck.api.domain.voiceActor.entity.VoiceActor;
import io.oduck.api.domain.voiceActor.repository.VoiceActorRepository;
import io.oduck.api.domain.voiceActor.service.VoiceActorServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class VoiceActorServiceTest {

    @InjectMocks
    private VoiceActorServiceImpl voiceActorService;

    @Mock
    private VoiceActorRepository voiceActorRepository;

    @Nested
    @DisplayName("등록")
    class SaveVoiceActor {
        @Test
        @DisplayName("성우 등록 성공")
        void saveVoiceActor() {
            //given
            VoiceActorReq.PostReq postReq = new VoiceActorReq.PostReq("성우1");

            //when
            voiceActorService.save(postReq);

            //then
            assertThatNoException();

            //verify
            verify(voiceActorRepository, times(1)).save(any(VoiceActor.class));
        }
    }

    @Nested
    @DisplayName("조회")
    class GetVoiceActors {
        @Test
        @DisplayName("성우 조회 성공")
        void getVoiceActors() {
            //when
            voiceActorService.getVoiceActors();

            //then
            assertThatNoException();

            //verify
            verify(voiceActorRepository, times(1)).findAll();
        }
    }
}
