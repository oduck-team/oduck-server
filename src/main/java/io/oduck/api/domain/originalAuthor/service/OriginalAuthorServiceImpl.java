package io.oduck.api.domain.originalAuthor.service;

import io.oduck.api.domain.originalAuthor.dto.OriginalAuthorRes;
import io.oduck.api.domain.originalAuthor.entity.OriginalAuthor;
import io.oduck.api.domain.originalAuthor.repository.OriginalAuthorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static io.oduck.api.domain.originalAuthor.dto.OriginalAuthorReq.PostReq;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OriginalAuthorServiceImpl implements OriginalAuthorService{

    private final OriginalAuthorRepository originalAuthorRepository;

    @Override
    public void save(PostReq req) {

        OriginalAuthor originalAuthor = OriginalAuthor.builder()
                .name(req.getName())
                .build();
        originalAuthorRepository.save(originalAuthor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OriginalAuthorRes> getOriginalAuthors() {

        return originalAuthorRepository.findAll().stream()
                .map(oa -> new OriginalAuthorRes(oa.getId(), oa.getName()))
                .collect(Collectors.toList());
    }
}
