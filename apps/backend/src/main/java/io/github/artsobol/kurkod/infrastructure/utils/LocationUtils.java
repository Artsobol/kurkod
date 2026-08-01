package io.github.artsobol.kurkod.infrastructure.utils;

import java.net.URI;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public final class LocationUtils {

    private LocationUtils() {}

    public static URI buildLocation(long id) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }

    public static URI buildLocation() {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .build()
                .toUri();
    }

    public static URI buildLocation(String pathTemplate, Object... uriVars) {
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path(pathTemplate)
                .buildAndExpand(uriVars)
                .toUri();
    }
}
