package utils;

import java.util.Iterator;

public class StringUtils {

    public static String join(Iterable<?> things, String delimiter) {
        StringBuilder buffer = new StringBuilder();
        Iterator<?> iter = things.iterator();
        while (iter.hasNext()) {
            buffer.append(iter.next());
            if (iter.hasNext())
                buffer.append(delimiter);

        }
        return buffer.toString();
    }
}
