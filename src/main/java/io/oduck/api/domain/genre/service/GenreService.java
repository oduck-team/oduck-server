package io.oduck.api.domain.genre.service;

import static io.oduck.api.domain.genre.dto.GenreReq.PostReq;

import io.oduck.api.domain.genre.dto.GenreRes;
import java.util.List;

public interface GenreService {

    void save(PostReq req);

    List<GenreRes> getGenres();

    void update(Long genreId, String name);

    void delete(Long genreId);
}
