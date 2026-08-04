package com.seekstorm.client.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Quota settings used when creating a SeekStorm API key.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiKeyQuotaRequest(
        /** Maximum number of indices allowed for the key. */ Integer indicesMax,
        /** Combined index size limit for the key. */ Integer indicesSizeMax,
        /** Maximum number of documents allowed for the key. */ Integer documentsMax,
        /** Maximum number of operations allowed for the key. */ Integer operationsMax,
        /** Optional per-second query rate limit. */ Integer rateLimit,
        /** Whether to create the fixed demo API key. */ Boolean demo) {
}