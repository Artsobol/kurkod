package io.github.artsobol.kurkod.infrastructure.utils;

import io.github.artsobol.kurkod.exception.http.InvalidIfMatchException;
import io.github.artsobol.kurkod.exception.http.MissingIfMatchException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class EtagUtils {

    private static final Pattern ETAG_PATTERN = Pattern.compile("\"(\\d+)\"");

    private EtagUtils() {}

    public static String toEtag(long version) {
        return "\"" + version + "\"";
    }

    public static long parseIfMatch(String ifMatchHeader) {
        checkIfMatch(ifMatchHeader);

        try {
            return parseValue(ifMatchHeader.trim());
        } catch (NumberFormatException e) {
            throw new InvalidIfMatchException("common.if.match.invalid", ifMatchHeader);
        }
    }

    private static void checkIfMatch(String ifMatchHeader) {
        if (ifMatchHeader == null || ifMatchHeader.isBlank()) {
            throw new MissingIfMatchException("common.if.match.missing", ifMatchHeader);
        }
    }

    private static long parseValue(String headerValue) throws NumberFormatException {
        Matcher matcher = ETAG_PATTERN.matcher(headerValue);
        if (!matcher.matches()) {
            throw new NumberFormatException("Invalid ETag format");
        }
        return Long.parseLong(matcher.group(1));
    }
}
