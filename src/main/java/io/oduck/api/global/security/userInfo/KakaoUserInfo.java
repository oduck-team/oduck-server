package io.oduck.api.global.security.userInfo;

import java.util.Map;

public class KakaoUserInfo extends SocialUserInfo {
    public KakaoUserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return String.valueOf(attributes.get("id"));
    }

    // 카카오 닉네임
    @Override
    public String getEmail() {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");

        if (account == null) {
            return null;
        }

        return (String) account.get("email");
    }

//    // 카카오 닉네임
//    @Override
//    public String getNickname() {
//        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
//        Map<String, Object> profile = (Map<String, Object>) account.get("profile");
//
//        if (account == null || profile == null) {
//            return null;
//        }
//
//        return (String) profile.get("nickname");
//    }

//    // 카카오 프로필 이미지.
//    @Override
//    public String getImageUrl() {
//        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
//        Map<String, Object> profile = (Map<String, Object>) account.get("profile");
//
//        if (account == null || profile == null) {
//            return null;
//        }
//
//        return (String) profile.get("thumbnail_image_url");
//    }
}
