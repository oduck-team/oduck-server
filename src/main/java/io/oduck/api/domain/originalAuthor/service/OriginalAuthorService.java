package io.oduck.api.domain.originalAuthor.service;

import io.oduck.api.domain.originalAuthor.dto.OriginalAuthorRes;

import java.util.List;

import static io.oduck.api.domain.originalAuthor.dto.OriginalAuthorReq.PostReq;

public interface OriginalAuthorService {

    void save(PostReq req);

    List<OriginalAuthorRes> getOriginalAuthors();
}
