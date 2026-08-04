package com.seekstorm.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seekstorm.client.model.ApiKeyQuotaRequest;
import com.seekstorm.client.model.DeleteApiKeyRequest;
import com.seekstorm.client.model.GetDocumentRequest;
import com.seekstorm.client.model.GetIteratorRequest;
import com.seekstorm.client.model.IndexResponse;
import com.seekstorm.client.model.IteratorResult;
import com.seekstorm.client.model.SearchRequest;
import com.seekstorm.client.model.SearchResponse;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class SeekStormIntegrationTest {
    private static final String BASE_URL = System.getenv().getOrDefault("SEEKSTORM_BASE_URL", "http://127.0.0.1:80");
    private static final String API_KEY = System.getenv().getOrDefault(
            "SEEKSTORM_API_KEY",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=");
    private static final String MASTER_API_KEY = System.getenv().getOrDefault(
            "SEEKSTORM_MASTER_API_KEY",
            "/iWStCpyfpd/BVlHOFtwnMgrFrmof4jGq/OQDWXQzcM=");

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static SeekStormClient client;
    private static SeekStormClient masterClient;
    private static String liveMessage;

    private String createdIndexId;

    @BeforeAll
    static void setUpClass() {
        client = SeekStormClient.builder()
                .baseUrl(BASE_URL)
                .apiKey(API_KEY)
                .build();
        masterClient = SeekStormClient.builder()
            .baseUrl(BASE_URL)
            .apiKey(MASTER_API_KEY)
            .build();

        try {
            liveMessage = client.live();
        } catch (Exception exception) {
            Assumptions.assumeTrue(false, "SeekStorm server not reachable at " + BASE_URL + ": " + exception.getMessage());
            return;
        }

        Assumptions.assumeTrue(liveMessage != null && liveMessage.contains("SeekStorm"),
                "Unexpected live response: " + liveMessage);
    }

    @AfterEach
    void tearDown() {
        if (createdIndexId != null) {
            try {
                client.deleteIndex(createdIndexId);
            } catch (Exception ignored) {
                // Best-effort cleanup for integration runs.
            } finally {
                createdIndexId = null;
            }
        }
    }

    @Test
    void liveEndpointResponds() {
        Assertions.assertTrue(liveMessage.contains("SeekStorm"), "Live response should identify the server");
    }

    @Test
    void createAndDeleteApiKeyWithMasterKey() {
        Assumptions.assumeTrue(MASTER_API_KEY != null && !MASTER_API_KEY.isBlank(),
                "SEEKSTORM_MASTER_API_KEY not set; skipping master-key endpoint test");

        ApiKeyQuotaRequest quotaRequest = new ApiKeyQuotaRequest(
            10,
            100_000_000,
            100_000_000,
            1_000_000_000,
            null,
            false);

        String createdApiKey;
        try {
            createdApiKey = masterClient.createApiKey(quotaRequest);
        } catch (SeekStormApiException exception) {
            if (exception.getStatusCode() == 401 || exception.getStatusCode() == 403) {
                Assumptions.assumeTrue(false, "SeekStorm master API key rejected by server: " + exception.getResponseBody());
                return;
            }
            throw exception;
        }

        Assertions.assertNotNull(createdApiKey);
        Assertions.assertFalse(createdApiKey.isBlank());

        SeekStormClient createdKeyClient = SeekStormClient.builder()
            .baseUrl(BASE_URL)
            .apiKey(createdApiKey)
            .build();

        JsonNode info = createdKeyClient.getApiKey();
        Assertions.assertNotNull(info);
        JsonNode indices = info.isArray() ? info : info.get("Ok");
        if (indices == null) {
            indices = info.get("ok");
        }
        Assertions.assertNotNull(indices, "API key info should contain index metadata");
        Assertions.assertTrue(indices.isArray(), "API key info should contain an array of index metadata");

        long remainingApiKeys = masterClient.deleteApiKey(new DeleteApiKeyRequest(createdApiKey));
        Assertions.assertTrue(remainingApiKeys >= 0L);
    }

    @Test
    void indexDocumentQueryAndCleanup() throws Exception {
        String indexName = "java_e2e_" + getClass().getSimpleName().toLowerCase() + "_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString().replace('-', '_');
        JsonNode createIndexRequest = MAPPER.createObjectNode()
                .put("index_name", indexName)
                .set("schema", MAPPER.createArrayNode()
                        .add(MAPPER.createObjectNode()
                                .put("field", "title")
                                .put("field_type", "Text")
                                .put("store", true)
                                .put("index_lexical", true))
                        .add(MAPPER.createObjectNode()
                                .put("field", "body")
                                .put("field_type", "Text")
                                .put("store", true)
                                .put("index_lexical", true)
                                .put("longest", true))
                        .add(MAPPER.createObjectNode()
                                .put("field", "url")
                                .put("field_type", "Text")
                                .put("store", true)
                                .put("index_lexical", false))
                        .add(MAPPER.createObjectNode()
                                .put("field", "date")
                                .put("field_type", "Timestamp")
                                .put("store", true)
                                .put("index_lexical", false)));

        try {
            createdIndexId = String.valueOf(client.createIndex(createIndexRequest));
        } catch (SeekStormApiException exception) {
            if (exception.getStatusCode() == 401 || exception.getStatusCode() == 403) {
                Assumptions.assumeTrue(false, "SeekStorm API key rejected by server: " + exception.getResponseBody());
                return;
            }
            throw exception;
        }

        Assertions.assertNotNull(createdIndexId);
        Assertions.assertFalse(createdIndexId.isBlank());

        IndexResponse indexInfo = client.getIndex(createdIndexId);
        Assertions.assertNotNull(indexInfo);

        JsonNode firstDocument = MAPPER.createObjectNode()
                .put("title", "title-one")
                .put("body", "body one integration marker")
                .put("url", "https://example.org/one")
                .put("date", 1730901447L);
        long firstIndexCount = client.indexDocument(createdIndexId, firstDocument);
        Assertions.assertTrue(firstIndexCount >= 0L);

        JsonNode secondDocument = MAPPER.createObjectNode()
                .put("title", "title-two")
                .put("body", "body two integration marker")
                .put("url", "https://example.org/two")
                .put("date", 1730901448L);
        long secondIndexCount = client.indexDocument(createdIndexId, secondDocument);
        Assertions.assertTrue(secondIndexCount >= 0L);

        long committedCount = client.commitIndex(createdIndexId);
        Assertions.assertTrue(committedCount >= 0L);

        SearchResponse searchResponse = client.search(createdIndexId, new SearchRequest("integration marker"));
        Assertions.assertNotNull(searchResponse);
        Assertions.assertNotNull(searchResponse.results());
        Assertions.assertFalse(searchResponse.results().isEmpty(), "Search should return the indexed documents");

        IteratorResult iteratorResult = client.getIterator(
                createdIndexId,
                new GetIteratorRequest(null, 0, 10, false, true, List.of()));
        Assertions.assertNotNull(iteratorResult);
        Assertions.assertNotNull(iteratorResult.results());
        Assertions.assertFalse(iteratorResult.results().isEmpty(), "Iterator should return at least one document");

        String documentId = iteratorResult.results().get(0).docId();
        Assertions.assertNotNull(documentId);

        JsonNode fetchedDocument = client.getDocument(
                createdIndexId,
                documentId,
                new GetDocumentRequest(List.of(), List.of(), List.of(), List.of()));
        Assertions.assertNotNull(fetchedDocument);
        Assertions.assertTrue(fetchedDocument.isObject());
        Assertions.assertTrue(fetchedDocument.size() > 0);

        long deletedDocumentCount = client.deleteDocument(createdIndexId, documentId);
        Assertions.assertTrue(deletedDocumentCount >= 0L);
    }
}