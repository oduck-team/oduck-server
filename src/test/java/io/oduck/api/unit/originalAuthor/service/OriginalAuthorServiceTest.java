package io.oduck.api.unit.originalAuthor.service;

import io.oduck.api.domain.originalAuthor.dto.OriginalAuthorReq;
import io.oduck.api.domain.originalAuthor.entity.OriginalAuthor;
import io.oduck.api.domain.originalAuthor.repository.OriginalAuthorRepository;
import io.oduck.api.domain.originalAuthor.service.OriginalAuthorServiceImpl;
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
public class OriginalAuthorServiceTest {

    @InjectMocks
    private OriginalAuthorServiceImpl originalAuthorService;

    @Mock
    private OriginalAuthorRepository originalAuthorRepository;

    @Nested
    @DisplayName("등록")
    class PostOriginalAuthor {
        @Test
        @DisplayName("원작 작가 등록 성공")
        void postOriginalAuthor() {
            //given
            OriginalAuthorReq.PostReq postReq = new OriginalAuthorReq.PostReq("하나에 나츠키");

            //when
            originalAuthorService.save(postReq);

            //then
            assertThatNoException();

            //verify
            verify(originalAuthorRepository, times(1)).save(any(OriginalAuthor.class));
        }
    }

    @Nested
    @DisplayName("조회")
    class GetOriginalAuthors {
        @Test
        @DisplayName("원작 작가들 조회")
        void getOriginalAuthors(){
            //when
            originalAuthorService.getOriginalAuthors();

            //then
            assertThatNoException();

            //verify
            verify(originalAuthorRepository, times(1)).findAllByDeletedAtIsNull();
        }
    }
}
