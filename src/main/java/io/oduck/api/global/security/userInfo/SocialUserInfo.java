package io.oduck.api.global.security.userInfo;

import java.util.Map;

public abstract class SocialUserInfo {
    protected Map<String, Object> attributes;

    public SocialUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public abstract String getId(); //소셜 식별 값 : 구글 - "sub", 카카오 - "id", 네이버 - "id"

    public abstract String getEmail();
}
