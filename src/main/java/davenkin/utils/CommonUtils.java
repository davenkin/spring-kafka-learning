package davenkin.utils;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class CommonUtils {

    public static String nullIfBlank(String string) {
        if (isBlank(string)) {
            return null;
        }

        return string;
    }
}
