package io.oduck.api.global.security.auth.repository;


import io.oduck.api.global.security.auth.entity.AuthLocal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthLocalRepository extends JpaRepository<AuthLocal,Long> {

    boolean existsByEmail(String email);
}
