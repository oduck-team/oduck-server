package io.oduck.api.domain.attractionPoint.repository;

import io.oduck.api.domain.attractionPoint.entity.AttractionElement;
import io.oduck.api.domain.member.dto.MemberDslDto.ProfileWithoutActivity;

import java.util.Optional;

public interface AttractionPointRepositoryCustom {
    Long countElementByAnimeId(AttractionElement attractionElement, Long animeId);
    Long countByAnimeId(Long animeId);
}
