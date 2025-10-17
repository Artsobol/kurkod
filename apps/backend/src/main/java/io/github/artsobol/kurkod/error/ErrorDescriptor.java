package io.github.artsobol.kurkod.error;

import org.springframework.http.HttpStatus;

public interface ErrorDescriptor {
    String getCode();
    String getMessageKey();
    HttpStatus getStatus();
}
