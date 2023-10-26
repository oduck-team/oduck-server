package io.oduck.api.domain.genre.service;

import io.oduck.api.domain.genre.dto.GenreRes;

import java.util.List;

import static io.oduck.api.domain.genre.dto.GenreReq.PostReq;

public interface GenreService {

    void save(PostReq req);

    List<GenreRes> getGenres();
}
