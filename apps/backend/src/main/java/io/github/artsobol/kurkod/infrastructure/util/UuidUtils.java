package io.github.artsobol.kurkod.infrastructure.util;

import io.github.artsobol.kurkod.infrastructure.constants.CommonConstants;
import io.github.artsobol.kurkod.infrastructure.constants.PasswordConstants;

import java.util.UUID;

public final class UuidUtils {

    private UuidUtils() {}

    public static String generateUuidWithoutDash() {
        return UUID.randomUUID().toString().replaceAll(CommonConstants.DASH, org.apache.commons.lang3.StringUtils.EMPTY);
    }
}
