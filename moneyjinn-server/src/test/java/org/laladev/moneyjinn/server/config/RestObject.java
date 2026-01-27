package org.laladev.moneyjinn.server.config;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class RestObject {
    private String attribute1;
    private Long attribute2;
    private OffsetDateTime attribute3;
}
