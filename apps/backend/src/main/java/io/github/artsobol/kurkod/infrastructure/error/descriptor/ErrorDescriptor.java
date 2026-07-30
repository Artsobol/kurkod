package io.github.artsobol.kurkod.infrastructure.error.descriptor;

import org.springframework.http.HttpStatus;

public interface ErrorDescriptor {
    String getCode();
    String getMessageKey();
    HttpStatus getStatus();
}
