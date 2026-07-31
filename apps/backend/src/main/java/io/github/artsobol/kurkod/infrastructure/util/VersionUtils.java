package io.github.artsobol.kurkod.infrastructure.util;

import io.github.artsobol.kurkod.exception.http.VersionConflictException;
import java.util.Objects;

public final class VersionUtils {

    private VersionUtils() {}

    public static void checkVersion(Long entityVersion, Long requestVersion) {
        if (!Objects.equals(entityVersion, requestVersion)) {
            throw new VersionConflictException("common.version.mismatch", entityVersion, requestVersion);
        }
    }
}
