package com.seekstorm.client.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * A single field definition inside a SeekStorm index schema.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record SchemaField(
        /** Field name. */ String fieldName,
        /** Field type. */ String fieldType,
        /** Whether the field is stored. */ Boolean stored,
        /** Whether the field is indexed. */ Boolean indexed,
        /** Whether the field is a fast field. */ Boolean fast,
        /** Whether the field stores vector data. */ Boolean vector) {
}