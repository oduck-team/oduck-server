package io.oduck.api.unit.studio.service;

import io.oduck.api.domain.studio.dto.StudioReq;
import io.oduck.api.domain.studio.entity.Studio;
import io.oduck.api.domain.studio.repository.StudioRepository;
import io.oduck.api.domain.studio.service.StudioServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudioServiceTest {

    @InjectMocks
    private StudioServiceImpl studioService;

    @Mock
    private StudioRepository studioRepository;

    @Nested
    @DisplayName("등록")
    class SaveStudio {

        @Test
        @DisplayName("스튜디오 등록 성공")
        void saveStudio() {
            //given
            StudioReq.PostReq postReq = new StudioReq.PostReq("ufortable");

            //when
            studioService.save(postReq);

            //then
            assertThatNoException();

            //verify
            verify(studioRepository,times(1)).save(any(Studio.class));
        }
    }

    @Nested
    @DisplayName("조회")
    class GetStudios {
        @Test
        @DisplayName("스튜디오 조회 성공")
        void getStudios() {
            //when
            studioService.getStudios();

            //then
            assertThatNoException();

            //verify
            verify(studioRepository, times(1)).findAll();
        }
    }
}
