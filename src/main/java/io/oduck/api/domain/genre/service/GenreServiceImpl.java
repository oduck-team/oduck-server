package io.oduck.api.domain.genre.service;

import io.oduck.api.domain.genre.dto.GenreRes;
import io.oduck.api.domain.genre.entity.Genre;
import io.oduck.api.domain.genre.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static io.oduck.api.domain.genre.dto.GenreReq.PostReq;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class GenreServiceImpl implements GenreService{

    private final GenreRepository genreRepository;

    @Override
    public void save(PostReq req) {
        Genre genre = Genre.builder()
                .name(req.getName())
                .build();
        genreRepository.save(genre);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GenreRes> getGenres() {
        return genreRepository.findAll().stream()
                .map(gr -> new GenreRes(gr.getId(), gr.getName()))
                .collect(Collectors.toList());
    }
}
