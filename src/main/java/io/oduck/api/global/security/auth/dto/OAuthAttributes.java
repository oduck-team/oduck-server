package io.oduck.api.global.security.auth.dto;

import io.oduck.api.global.security.auth.entity.AuthSocial;
import io.oduck.api.global.security.auth.entity.SocialType;
import io.oduck.api.global.security.userInfo.GoogleUserInfo;
import io.oduck.api.global.security.userInfo.KakaoUserInfo;
import io.oduck.api.global.security.userInfo.NaverUserInfo;
import io.oduck.api.global.security.userInfo.SocialUserInfo;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

/**
 * 각 소셜에서 받아오는 데이터가 다르므로
 * 소셜별로 데이터를 받는 데이터를 분기 처리하는 DTO 클래스
 */
@Getter
public class OAuthAttributes {

    private String nameAttributeKey; // OAuth2 로그인 진행 시 키가 되는 필드 값, PK와 같은 의미
    private SocialUserInfo socialUserInfo; // 소셜 타입별 로그인 유저 정보(닉네임, 이메일, 프로필 사진 등등)

    @Builder
    public OAuthAttributes(String nameAttributeKey, SocialUserInfo socialUserInfo) {
        this.nameAttributeKey = nameAttributeKey;
        this.socialUserInfo = socialUserInfo;
    }

    /**
     * SocialType에 맞는 메소드 호출하여 OAuthAttributes 객체 반환
     * 파라미터 : userNameAttributeName -> OAuth2 로그인 시 키(PK)가 되는 값 / attributes : OAuth 서비스의 유저 정보들
     * 소셜별 of 메소드(ofGoogle, ofKaKao, ofNaver)들은 각각 소셜 로그인 API에서 제공하는
     * 회원의 식별값(id), attributes, nameAttributeKey를 저장 후 build
     */
    public static OAuthAttributes of(SocialType socialType,
        String userNameAttributeName, Map<String, Object> attributes) {

        if (socialType == SocialType.NAVER) {
            return ofNaver(userNameAttributeName, attributes);
        }
        if (socialType == SocialType.KAKAO) {
            return ofKakao(userNameAttributeName, attributes);
        }
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
            .nameAttributeKey(userNameAttributeName)
            .socialUserInfo(new KakaoUserInfo(attributes))
            .build();
    }

    public static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
            .nameAttributeKey(userNameAttributeName)
            .socialUserInfo(new GoogleUserInfo(attributes))
            .build();
    }

    public static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
            .nameAttributeKey(userNameAttributeName)
            .socialUserInfo(new NaverUserInfo(attributes))
            .build();
    }


    /**
     * of메소드로 OAuthAttributes 객체가 생성되어, 유저 정보들이 담긴 OAuth2UserInfo가 소셜 타입별로 주입된 상태
     * OAuth2UserInfo에서 socialId(식별값), nickname, imageUrl을 가져와서 build
     * email에는 UUID로 중복 없는 랜덤 값 생성
     * role은 GUEST로 설정
     */
    public AuthSocial toEntity(SocialType socialType, SocialUserInfo socialUserInfo) {
        return AuthSocial.builder()
            .socialType(socialType)
            .socialId(socialUserInfo.getId())
            .email(socialUserInfo.getEmail())
            .build();
    }
}