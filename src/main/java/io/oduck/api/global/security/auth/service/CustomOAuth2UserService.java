package io.oduck.api.global.security.auth.service;

import static io.oduck.api.global.utils.NameGenerator.generateNickname;

import io.oduck.api.global.security.auth.dto.SessionUser;
import io.oduck.api.global.security.auth.entity.AuthSocial;
import io.oduck.api.domain.member.entity.LoginType;
import io.oduck.api.domain.member.entity.Member;
import io.oduck.api.domain.member.entity.MemberProfile;
import io.oduck.api.global.security.auth.entity.SocialType;
import io.oduck.api.global.security.auth.repository.AuthSocialRepository;
import io.oduck.api.domain.member.repository.MemberRepository;
import io.oduck.api.global.security.auth.dto.OAuthAttributes;
import jakarta.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;
    private final AuthSocialRepository authSocialRepository;
    private final HttpSession httpSession;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        SocialType socialType = getSocialType(registrationId);
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(
                socialType,
                userNameAttributeName,
                oAuth2User
                        .getAttributes());

        AuthSocial authSocial = attributes.toEntity(socialType, attributes.getSocialUserInfo());

        Member member = getOrSaveMember(authSocial);
        httpSession.setAttribute("user", new SessionUser(member.getId(), LoginType.SOCIAL));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(member.getMemberProfile().getRole().toString())),
                oAuth2User.getAttributes(),
                attributes.getNameAttributeKey());
    }

    private Member getOrSaveMember(AuthSocial authSocial) {
        Optional<AuthSocial> optionalAuthSocial = authSocialRepository
                .findBySocialIdAndSocialType(
                        authSocial.getSocialId(),
                        authSocial.getSocialType());

        if (optionalAuthSocial.isPresent()) {
            AuthSocial foundAuthSocial = optionalAuthSocial.get();
            return memberRepository.findById(foundAuthSocial.getMember().getId()).get();
        }

        MemberProfile memberProfile = MemberProfile.builder()
                .name(generateNickname())
                .build();

        Member member = Member.builder()
                .loginType(LoginType.SOCIAL)
                .authSocial(authSocial)
                .memberProfile(memberProfile)
                .build();

        return memberRepository.save(member);
    }

    private SocialType getSocialType(String registrationId) {
        if (registrationId.equals("naver")) {
            return SocialType.NAVER;
        }

        if (registrationId.equals("kakao")) {
            return SocialType.KAKAO;
        }

        return SocialType.GOOGLE;
    }
}