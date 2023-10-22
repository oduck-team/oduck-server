package io.oduck.api.domain.originalAuthor.service;

import io.oduck.api.domain.originalAuthor.repository.OriginalAuthorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OriginalAuthorServiceImpl implements OriginalAuthorService{

    private final OriginalAuthorRepository originalAuthorRepository;
}
