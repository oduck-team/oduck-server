package io.oduck.api.global.security.auth.repository;

import io.oduck.api.global.security.auth.entity.AuthSocial;
import io.oduck.api.global.security.auth.entity.SocialType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthSocialRepository extends JpaRepository<AuthSocial,Long> {

    Optional<AuthSocial> findBySocialIdAndSocialType(String socialId, SocialType socialType);
    Optional<AuthSocial> findByEmail(String email);
}
