package com.seekstorm.client.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Request payload used when deleting an API key.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record DeleteApiKeyRequest(
	/** Base64-encoded API key to delete. */ String apikeyBase64) {
}