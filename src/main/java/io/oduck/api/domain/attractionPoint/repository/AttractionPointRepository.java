package io.oduck.api.domain.attractionPoint.repository;

import io.oduck.api.domain.attractionPoint.entity.AttractionPoint;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AttractionPointRepository extends JpaRepository<AttractionPoint, Long>{
    @Query("select ap from AttractionPoint ap "
               + "where ap.anime.id = :animeId "
               + "and ap.member.id = :memberId")
    List<AttractionPoint> findAllByAnimeIdAndMemberId(@Param("memberId") Long memberId, @Param("animeId") Long animeId);

}
