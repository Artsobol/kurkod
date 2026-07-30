package io.github.artsobol.kurkod.infrastructure.util;

import io.github.artsobol.kurkod.exception.http.VersionConflictException;
import io.github.artsobol.kurkod.infrastructure.error.descriptor.VersionError;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public final class VersionUtils {

    private VersionUtils() {}

    public static void checkVersion(Long entityVersion, Long requestVersion) {
        if (!Objects.equals(entityVersion, requestVersion)) {
            log.info(VersionError.VERSION_NOT_EQUALS.format(entityVersion, requestVersion));
            throw new VersionConflictException("common.version.mismatch", entityVersion, requestVersion);
        }
    }
}
