package com.seekstorm.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

/**
 * Metadata returned for a SeekStorm index.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record IndexResponse(
        /** The index ID. */ String id,
        /** The index name. */ String name,
        /** The index schema. */ List<SchemaField> schema,
        /** Number of indexed documents. */ Long indexedDocCount,
        /** Number of committed documents. */ Long committedDocCount,
        /** Number of operations performed. */ Long operationsCount,
        /** Number of queries performed. */ Long queryCount,
        /** Server or index version string. */ String version,
        /** Whether facet min/max is enabled. */ Boolean facetsMinmax) {
}