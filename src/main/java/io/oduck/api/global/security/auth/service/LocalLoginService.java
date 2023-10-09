//package io.oduck.api.global.security.auth.service;
//
//import io.oduck.api.domain.member.entity.Member;
//import io.oduck.api.global.exception.UnauthorizedException;
//import io.oduck.api.global.security.auth.dto.CustomUserDetails;
//import io.oduck.api.global.security.auth.entity.AuthLocal;
//import io.oduck.api.global.security.auth.repository.AuthLocalRepository;
//import java.util.Optional;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class LocalLoginService implements UserDetailsService {
//    private final AuthLocalRepository authLocalRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        Optional<AuthLocal> optionalAuthLocal = authLocalRepository.findByEmail(email);
//
//        if (!optionalAuthLocal.isPresent()) {
//            throw new UnauthorizedException("Unauthorized");
//        }
//
//        AuthLocal authLocal = optionalAuthLocal.get();
//
//        Member member = authLocal.getMember();
//
//        return new CustomUserDetails(
//            member.getId(),
//            authLocal.getEmail(),
//            authLocal.getPassword(),
//            member.getRole()
//        );
//    }
//}
