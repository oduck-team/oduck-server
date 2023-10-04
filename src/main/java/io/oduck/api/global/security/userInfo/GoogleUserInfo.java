package io.oduck.api.global.security.userInfo;

import java.util.Map;

public class GoogleUserInfo extends SocialUserInfo {
    public GoogleUserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return (String) attributes.get("sub");
    }

    // 구글 닉네임
    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

//    // 구글 닉네임
//    @Override
//    public String getNickname() {
//        return (String) attributes.get("name");
//    }

//    // 구글 프로필 이미지.
//    @Override
//    public String getImageUrl() {
//        return (String) attributes.get("picture");
//    }
}
