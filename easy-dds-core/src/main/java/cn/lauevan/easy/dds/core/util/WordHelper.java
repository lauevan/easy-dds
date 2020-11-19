package cn.lauevan.easy.dds.core.util;

import java.util.Objects;

public final class WordHelper {

    private static final String EMPTY_WORD = "";

    private WordHelper() {
    }

    public static String lowercaseFirstChar(String word) {

        if (Objects.nonNull(word) && word.length() > 1) {
            final Tuple<String, String> t = split(word);
            return t.value1().toLowerCase() + t.value2();
        }

        return EMPTY_WORD;
    }

    public static String uppercaseFirstChar(String word) {

        if (Objects.nonNull(word) && word.length() > 1) {
            final Tuple<String, String> t = split(word);
            return t.value1().toUpperCase() + t.value2();
        }

        return EMPTY_WORD;
    }

    private static Tuple<String ,String> split(String word) {
        return Tuple.newTuple(word.substring(0, 1), word.substring(1));
    }
}
