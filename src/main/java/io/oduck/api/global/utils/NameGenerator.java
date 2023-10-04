package io.oduck.api.global.utils;

import java.util.Random;

public class NameGenerator {
    private static final String[] adjectives = {
        "벽력일섬쓰는",
        "영역전개하는",
        "에네르기파쓰는",
        "이세계로간",
        "트럭에치인",
        "포켓몬잡는",
        "건담을타는",
        "마법소녀",
        "열매능력자",
        "해적왕",
        "칠무해",
        "해군",
        "나뭇잎마을닌자",
        "아카츠키",
        "닌자연합군",
        "호정13대",
        "사신대행",
        "아란칼",
        "영령",
        "마스터",
        "서번트",
        "최강슬라임",
        "헌터"
    };

    private static final String[] nouns = {
        "오리",
        "오덕",
        "덕후"
    };

    private static final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String generateNickname() {
        String selectedAdjective = adjectives[new Random().nextInt(adjectives.length)];
        String selectedNoun = nouns[new Random().nextInt(nouns.length)];

        return selectedAdjective + "_" + selectedNoun + "_" + generateRandomTag();
    }

    private static String generateRandomTag() {
        StringBuilder tag = new StringBuilder();

        for (int i = 0; i < 4; i++) {
            int randomIndex = new Random().nextInt(characters.length());
            tag.append(characters.charAt(randomIndex));
        }

        return tag.toString();
    }
}
