package com.seekstorm.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.seekstorm.client.model.GetIteratorRequest;
import com.seekstorm.client.model.SearchRequest;
import com.seekstorm.client.model.UpdateDocumentRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Direct live smoke harness for parity methods when Maven is unavailable.
 */
public final class ParityLiveSmoke {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private ParityLiveSmoke() {
    }

    public static void main(String[] args) throws Exception {
        String baseUrl = System.getenv().getOrDefault("SEEKSTORM_BASE_URL", "http://127.0.0.1:80");
        String apiKey = System.getenv().getOrDefault("SEEKSTORM_API_KEY", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=");

        SeekStormClient client = SeekStormClient.builder()
                .baseUrl(baseUrl)
                .apiKey(apiKey)
                .build();

        String live = client.live();
        if (live == null || !live.contains("SeekStorm")) {
            throw new IllegalStateException("Server live endpoint did not return SeekStorm banner");
        }

        String indexName = "java_parity_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString().replace('-', '_');
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
                                .put("index_lexical", true)));

        String indexId = String.valueOf(client.createIndex(createIndexRequest));

        try {
            List<JsonNode> docs = List.of(
                    MAPPER.createObjectNode().put("title", "one").put("body", "parity smoke alpha"),
                    MAPPER.createObjectNode().put("title", "two").put("body", "parity smoke beta"),
                    MAPPER.createObjectNode().put("title", "three").put("body", "parity smoke gamma"));
            long indexed = client.indexDocuments(indexId, docs);
            if (indexed < 0L) {
                throw new IllegalStateException("indexDocuments returned negative count");
            }

            client.commitIndex(indexId);

            var iterator = client.getIterator(indexId, new GetIteratorRequest(null, 0, 50, false, false, List.of()));
            if (iterator == null || iterator.results() == null || iterator.results().isEmpty()) {
                throw new IllegalStateException("Iterator returned no results for bulk indexed docs");
            }

            List<Long> docIds = new ArrayList<>();
            for (var result : iterator.results()) {
                docIds.add(Long.parseLong(result.docId()));
            }

            List<UpdateDocumentRequest> updates = List.of(
                    new UpdateDocumentRequest(docIds.get(0), MAPPER.createObjectNode().put("title", "one-updated").put("body", "parity smoke updated alpha")),
                    new UpdateDocumentRequest(docIds.get(1), MAPPER.createObjectNode().put("title", "two-updated").put("body", "parity smoke updated beta")));
            long updated = client.updateDocuments(indexId, updates);
            if (updated < 0L) {
                throw new IllegalStateException("updateDocuments returned negative count");
            }

            client.commitIndex(indexId);

            long afterDeleteByDocIds = client.deleteDocumentsByDocIds(indexId, List.of(docIds.get(0)));
            if (afterDeleteByDocIds < 0L) {
                throw new IllegalStateException("deleteDocumentsByDocIds returned negative count");
            }

            long afterDeleteByQuery = client.deleteDocumentsByQuery(indexId, new SearchRequest("parity smoke"));
            if (afterDeleteByQuery < 0L) {
                throw new IllegalStateException("deleteDocumentsByQuery returned negative count");
            }

            ArrayNode reindexDocs = MAPPER.createArrayNode()
                    .add(MAPPER.createObjectNode().put("title", "x").put("body", "clear marker one"))
                    .add(MAPPER.createObjectNode().put("title", "y").put("body", "clear marker two"));
            long reindexed = client.indexDocuments(indexId, List.of(reindexDocs.get(0), reindexDocs.get(1)));
            if (reindexed < 0L) {
                throw new IllegalStateException("Reindex before clear returned negative count");
            }

            client.commitIndex(indexId);
            long afterClear = client.clearIndex(indexId);
            if (afterClear < 0L) {
                throw new IllegalStateException("clearIndex returned negative count");
            }

            System.out.println("PARITY_LIVE_SMOKE_PASSED");
        } finally {
            try {
                client.deleteIndex(indexId);
            } catch (Exception ignored) {
                // Best-effort cleanup.
            }
        }
    }
}