package com.seekstorm.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.seekstorm.client.model.ApiKeyQuotaRequest;
import com.seekstorm.client.model.CreateIndexRequest;
import com.seekstorm.client.model.DeleteApiKeyRequest;
import com.seekstorm.client.model.DocumentRequest;
import com.seekstorm.client.model.GetDocumentRequest;
import com.seekstorm.client.model.GetIteratorRequest;
import com.seekstorm.client.model.IndexResponse;
import com.seekstorm.client.model.IteratorResult;
import com.seekstorm.client.model.SearchRequest;
import com.seekstorm.client.model.SearchResponse;
import com.seekstorm.client.model.UpdateDocumentRequest;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Synchronous SeekStorm REST client for the vector and lexical server.
 *
 * <p>The client uses {@link java.net.http.HttpClient} under the hood, sends the {@code apikey}
 * header automatically when configured, and maps JSON responses with Jackson.</p>
 */
public final class SeekStormClient {
    private static final String API_PREFIX = "/api/v1";

    private final SeekStormClientConfig config;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    private SeekStormClient(SeekStormClientConfig config, HttpClient httpClient, ObjectMapper objectMapper) {
        this.config = config;
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    /**
     * Creates a new builder with the default base URL {@code http://127.0.0.1}.
     *
     * @return a new client builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Probes the server health endpoint.
     *
     * @return the plain-text live message returned by the server
     */
    public String live() {
        return sendText(HttpMethod.GET, path("live"), null, null, null);
    }

    /**
     * Retrieves information for the API key currently set on this client.
     *
     * @return JSON describing the indices associated with the configured API key
     */
    public JsonNode getApiKey() {
        return sendJson(HttpMethod.GET, path("apikey"), null);
    }

    /**
     * Creates a new API key using the configured master API key.
     *
     * @param request quota limits for the new key
     * @return the Base64-encoded API key returned by the server
     */
    public String createApiKey(ApiKeyQuotaRequest request) {
        return sendText(HttpMethod.POST, path("apikey"), request, null, null);
    }

    /**
     * Deletes an API key using the configured master API key.
     *
     * @param request the API key to delete
     * @return the number of remaining API keys
     */
    public long deleteApiKey(DeleteApiKeyRequest request) {
        return sendLong(HttpMethod.DELETE, path("apikey"), request);
    }

    /**
     * Creates an index from a typed request model.
     *
     * @param request index configuration and schema definition
     * @return the created index ID
     */
    public long createIndex(CreateIndexRequest request) {
        return sendLong(HttpMethod.POST, path("index"), request);
    }

    /**
     * Creates an index from raw JSON.
     *
     * @param request raw index creation payload
     * @return the created index ID
     */
    public long createIndex(JsonNode request) {
        return sendLong(HttpMethod.POST, path("index"), request);
    }

    /**
     * Fetches metadata for an index.
     *
     * @param indexId the index ID
     * @return the index metadata returned by the server
     */
    public IndexResponse getIndex(String indexId) {
        return sendJson(HttpMethod.GET, path("index", indexId), IndexResponse.class, null);
    }

    /**
     * Deletes an index.
     *
     * @param indexId the index ID
     * @return the number of remaining indices
     */
    public long deleteIndex(String indexId) {
        return sendLong(HttpMethod.DELETE, path("index", indexId), null);
    }

    /**
     * Commits an index so documents become durable outside the realtime path.
     *
     * @param indexId the index ID
     * @return the number of committed documents
     */
    public long commitIndex(String indexId) {
        return sendLong(HttpMethod.PATCH, path("index", indexId), null);
    }

    /**
     * Applies a raw JSON patch to an index.
     *
     * @param indexId the index ID
     * @param patch the patch payload
     * @return the updated index metadata
     */
    public IndexResponse patchIndex(String indexId, JsonNode patch) {
        return sendJson(HttpMethod.PATCH, path("index", indexId), IndexResponse.class, patch);
    }

    /**
     * Creates an iterator request that returns the number of skipped documents.
     *
     * @param indexId the index ID
     * @param request iterator options
     * @return the number of skipped document IDs
     */
    public long createIterator(String indexId, GetIteratorRequest request) {
        IteratorResult result = sendJson(HttpMethod.POST, path("index", indexId, "iterator"), IteratorResult.class, request);
        return result != null && result.skip() != null ? result.skip() : 0L;
    }

    /**
     * Iterates documents in an index.
     *
     * @param indexId the index ID
     * @param request iterator options such as skip, take, and field selection
     * @return iterator results containing document IDs and optional documents
     */
    public IteratorResult getIterator(String indexId, GetIteratorRequest request) {
        return sendJson(HttpMethod.POST, path("index", indexId, "iterator"), IteratorResult.class, request);
    }

    /**
     * Indexes a single document from a typed request model.
     *
     * @param indexId the index ID
     * @param request the document payload
     * @return the number of indexed documents
     */
    public long indexDocument(String indexId, DocumentRequest request) {
        return sendLong(HttpMethod.POST, path("index", indexId, "doc"), request);
    }

    /**
     * Indexes a single document or bulk document array from raw JSON.
     *
     * @param indexId the index ID
     * @param request raw document payload
     * @return the number of indexed documents
     */
    public long indexDocument(String indexId, JsonNode request) {
        return sendLong(HttpMethod.POST, path("index", indexId, "doc"), request);
    }

    /**
     * Indexes multiple documents in one request.
     *
     * @param indexId the index ID
     * @param documents document payload array
     * @return the number of indexed documents
     */
    public long indexDocuments(String indexId, List<JsonNode> documents) {
        return sendLong(HttpMethod.POST, path("index", indexId, "doc"), documents);
    }

    /**
     * Updates a document from a typed request model.
     *
     * @param indexId the index ID
     * @param request the update payload
     * @return the number of updated documents
     */
    public long updateDocument(String indexId, UpdateDocumentRequest request) {
        return sendLong(HttpMethod.PATCH, path("index", indexId, "doc"), request);
    }

    /**
     * Updates a document from raw JSON.
     *
     * @param indexId the index ID
     * @param request raw update payload
     * @return the number of updated documents
     */
    public long updateDocument(String indexId, JsonNode request) {
        return sendLong(HttpMethod.PATCH, path("index", indexId, "doc"), request);
    }

    /**
     * Updates multiple documents in one request.
     *
     * @param indexId the index ID
     * @param request raw bulk update payload
     * @return the number of updated documents
     */
    public long updateDocuments(String indexId, JsonNode request) {
        return sendLong(HttpMethod.PATCH, path("index", indexId, "doc"), request);
    }

    /**
     * Updates multiple documents in one request.
     *
     * @param indexId the index ID
     * @param requests typed update requests where each item maps to [doc_id, document]
     * @return the number of updated documents
     */
    public long updateDocuments(String indexId, List<UpdateDocumentRequest> requests) {
        ArrayNode payload = objectMapper.createArrayNode();
        for (UpdateDocumentRequest request : requests) {
            ArrayNode tuple = payload.addArray();
            tuple.add(request.docId());
            tuple.add(request.document());
        }
        return sendLong(HttpMethod.PATCH, path("index", indexId, "doc"), payload);
    }

    /**
     * Deletes documents using a search request.
     *
     * @param indexId the index ID
     * @param request delete-by-query request object
     * @return the number of documents after deletion
     */
    public long deleteDocumentsByQuery(String indexId, SearchRequest request) {
        return sendLong(HttpMethod.DELETE, path("index", indexId, "doc"), request);
    }

    /**
     * Deletes multiple documents by document IDs.
     *
     * @param indexId the index ID
     * @param documentIds document IDs to delete
     * @return the number of documents after deletion
     */
    public long deleteDocumentsByDocIds(String indexId, List<Long> documentIds) {
        return sendLong(HttpMethod.DELETE, path("index", indexId, "doc"), documentIds);
    }

    /**
     * Deletes one document by document ID through the request-body endpoint.
     *
     * @param indexId the index ID
     * @param documentId document ID to delete
     * @return the number of documents after deletion
     */
    public long deleteDocumentByDocId(String indexId, long documentId) {
        return sendLong(HttpMethod.DELETE, path("index", indexId, "doc"), documentId);
    }

    /**
     * Clears all documents in the index while keeping the index itself.
     *
     * @param indexId the index ID
     * @return the number of documents after clear
     */
    public long clearIndex(String indexId) {
        try {
            HttpRequest request = baseRequest(path("index", indexId, "doc"))
                    .header("Content-Type", "application/octet-stream")
                    .method("DELETE", HttpRequest.BodyPublishers.ofByteArray("clear".getBytes(StandardCharsets.UTF_8)))
                    .build();
            return parseLongResponse(send(request));
        } catch (SeekStormApiException exception) {
            if (exception.getStatusCode() == 400
                    && exception.getResponseBody() != null
                    && exception.getResponseBody().contains("expected u64")) {
                return clearIndexByDocIdBatches(indexId);
            }
            throw exception;
        }
    }

    private long clearIndexByDocIdBatches(String indexId) {
        while (true) {
            IteratorResult iterator = getIterator(indexId, new GetIteratorRequest(null, 0, 1000, false, false, List.of()));
            if (iterator == null || iterator.results() == null || iterator.results().isEmpty()) {
                return 0L;
            }

            List<Long> docIds = iterator.results().stream()
                    .map(result -> Long.parseLong(result.docId()))
                    .toList();

            if (docIds.isEmpty()) {
                return 0L;
            }

            deleteDocumentsByDocIds(indexId, docIds);
        }
    }

    /**
     * Fetches a single document by ID.
     *
     * @param indexId the index ID
     * @param documentId the document ID
     * @param request field filtering, highlights, and distance-field options
     * @return the document JSON returned by the server
     */
    public JsonNode getDocument(String indexId, String documentId, GetDocumentRequest request) {
        return sendJson(HttpMethod.GET, path("index", indexId, "doc", documentId), JsonNode.class, request);
    }

    /**
     * Deletes a document by document ID.
     *
     * @param indexId the index ID
     * @param documentId the document ID
     * @return the number of deleted documents
     */
    public long deleteDocument(String indexId, String documentId) {
        return sendLong(HttpMethod.DELETE, path("index", indexId, "doc", documentId), null);
    }

    /**
     * Uploads a PDF or other octet-stream payload with document metadata in headers.
     *
     * @param indexId the index ID
     * @param fileName the file name to send in the {@code file} header
     * @param content the binary file contents
     * @param dateHeader optional file date header value
     * @return the number of indexed documents
     */
    public long uploadFile(String indexId, String fileName, byte[] content, Optional<String> dateHeader) {
        HttpRequest.Builder builder = baseRequest(path("index", indexId, "file"))
                .header("file", fileName)
                .header("Content-Type", "application/octet-stream")
                .POST(HttpRequest.BodyPublishers.ofByteArray(content));
        dateHeader.ifPresent(date -> builder.header("date", date));
        HttpResponse<String> response = send(builder.build());
        return parseLongResponse(response);
    }

    /**
     * Downloads a file from the server as raw bytes.
     *
     * @param indexId the index ID
     * @param documentId the document ID
     * @return the binary file contents
     */
    public byte[] getFile(String indexId, String documentId) {
        HttpResponse<byte[]> response = sendBytes(baseRequest(path("index", indexId, "file", documentId)).GET().build());
        if (response.statusCode() / 100 != 2) {
            throw new SeekStormApiException(response.statusCode(), new String(response.body(), StandardCharsets.UTF_8));
        }
        return response.body();
    }

    /**
     * Executes a search query with the typed request model.
     *
     * @param indexId the index ID
     * @param request the search request
     * @return the parsed search response
     */
    public SearchResponse search(String indexId, SearchRequest request) {
        return sendJson(HttpMethod.POST, path("index", indexId, "query"), SearchResponse.class, request);
    }

    /**
     * Executes a search query from raw JSON.
     *
     * @param indexId the index ID
     * @param request raw search payload
     * @return the parsed search response
     */
    public SearchResponse search(String indexId, JsonNode request) {
        return sendJson(HttpMethod.POST, path("index", indexId, "query"), SearchResponse.class, request);
    }

    /**
     * Executes a raw search query and returns the JSON response unchanged.
     *
     * @param indexId the index ID
     * @param request raw search payload
     * @return the response JSON node
     */
    public JsonNode queryRaw(String indexId, JsonNode request) {
        return sendJson(HttpMethod.POST, path("index", indexId, "query"), JsonNode.class, request);
    }

    private <T> T sendJson(HttpMethod method, String path, Class<T> responseType, Object requestBody) {
        HttpResponse<String> response = send(request(method, path, requestBody));
        if (responseType == String.class) {
            return responseType.cast(response.body());
        }
        if (response.statusCode() / 100 != 2) {
            throw new SeekStormApiException(response);
        }
        try {
            return objectMapper.readValue(response.body(), responseType);
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to parse SeekStorm JSON response", exception);
        }
    }

    private JsonNode sendJson(HttpMethod method, String path, Object requestBody) {
        HttpResponse<String> response = send(request(method, path, requestBody));
        if (response.statusCode() / 100 != 2) {
            throw new SeekStormApiException(response);
        }
        try {
            return objectMapper.readTree(response.body());
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Failed to parse SeekStorm JSON response", exception);
        }
    }

    private String sendText(HttpMethod method, String path, Object requestBody, String contentType, List<HttpHeader> headers) {
        HttpResponse<String> response = send(request(method, path, requestBody, contentType, headers));
        if (response.statusCode() / 100 != 2) {
            throw new SeekStormApiException(response);
        }
        return response.body();
    }

    private long sendLong(HttpMethod method, String path, Object requestBody) {
        return parseLongResponse(send(request(method, path, requestBody)));
    }

    private long parseLongResponse(HttpResponse<String> response) {
        if (response.statusCode() / 100 != 2) {
            throw new SeekStormApiException(response);
        }
        String body = response.body().trim();
        if (body.isEmpty()) {
            return 0L;
        }
        try {
            return Long.parseLong(body);
        } catch (NumberFormatException exception) {
            try {
                JsonNode node = objectMapper.readTree(body);
                if (node.isNumber()) {
                    return node.longValue();
                }
                for (String field : List.of("Ok", "ok", "value", "result")) {
                    JsonNode child = node.get(field);
                    if (child != null && child.isNumber()) {
                        return child.longValue();
                    }
                    if (child != null && child.isTextual()) {
                        return Long.parseLong(child.asText().trim());
                    }
                }
            } catch (Exception ignored) {
                // Fall through to a descriptive exception below.
            }
            throw new IllegalStateException("Expected a numeric SeekStorm response but received: " + body);
        }
    }

    private HttpResponse<String> send(HttpRequest request) {
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (response.statusCode() / 100 != 2) {
                return response;
            }
            return response;
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("SeekStorm request interrupted", exception);
        } catch (IOException exception) {
            throw new IllegalStateException("SeekStorm request failed", exception);
        }
    }

    private HttpResponse<byte[]> sendBytes(HttpRequest request) {
        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("SeekStorm request interrupted", exception);
        } catch (IOException exception) {
            throw new IllegalStateException("SeekStorm request failed", exception);
        }
    }

    private HttpRequest request(HttpMethod method, String path, Object requestBody) {
        return request(method, path, requestBody, null, List.of());
    }

    private HttpRequest request(HttpMethod method, String path, Object requestBody, String contentType, List<HttpHeader> headers) {
        HttpRequest.Builder builder = baseRequest(path);
        if (contentType != null) {
            builder.header("Content-Type", contentType);
        } else if (requestBody != null) {
            builder.header("Content-Type", "application/json");
        }
        if (headers != null) {
            for (HttpHeader header : headers) {
                builder.header(header.name(), header.value());
            }
        }
        HttpRequest.BodyPublisher bodyPublisher = bodyPublisher(requestBody);
        switch (method) {
            case GET -> {
                if (requestBody == null) {
                    builder.GET();
                } else {
                    builder.method("GET", bodyPublisher);
                }
            }
            case DELETE -> {
                if (requestBody == null) {
                    builder.DELETE();
                } else {
                    builder.method("DELETE", bodyPublisher);
                }
            }
            case POST -> builder.POST(bodyPublisher);
            case PATCH -> builder.method("PATCH", bodyPublisher);
        }
        return builder.build();
    }

    private HttpRequest.Builder baseRequest(String path) {
        HttpRequest.Builder builder = HttpRequest.newBuilder(config.baseUrl().resolve(API_PREFIX + path))
                .timeout(config.requestTimeout())
                .header("Accept", "application/json");
        if (config.apiKey() != null && !config.apiKey().isBlank()) {
            builder.header("apikey", config.apiKey());
        }
        return builder;
    }

    private HttpRequest.BodyPublisher bodyPublisher(Object requestBody) {
        if (requestBody == null) {
            return HttpRequest.BodyPublishers.noBody();
        }
        try {
            if (requestBody instanceof JsonNode jsonNode) {
                return HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(jsonNode), StandardCharsets.UTF_8);
            }
            return HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(requestBody), StandardCharsets.UTF_8);
        } catch (JsonProcessingException exception) {
            throw new IllegalArgumentException("Could not serialize SeekStorm request body", exception);
        }
    }

    private String path(String firstSegment, String... additionalSegments) {
        StringBuilder builder = new StringBuilder("/").append(firstSegment);
        for (String segment : additionalSegments) {
            builder.append('/').append(segment);
        }
        return builder.toString();
    }

    private enum HttpMethod {
        GET,
        POST,
        DELETE,
        PATCH
    }

    private record HttpHeader(String name, String value) {
    }

    public static final class Builder {
        private URI baseUrl = URI.create("http://127.0.0.1");
        private String apiKey;
        private Duration connectTimeout = Duration.ofSeconds(10);
        private Duration requestTimeout = Duration.ofSeconds(30);
        private HttpClient httpClient;
        private ObjectMapper objectMapper;

        private Builder() {
        }

        /**
         * Sets the base URL for the server.
         *
         * @param baseUrl base URL for all requests
         * @return this builder
         */
        public Builder baseUrl(String baseUrl) {
            this.baseUrl = URI.create(Objects.requireNonNull(baseUrl, "baseUrl"));
            return this;
        }

        /**
         * Sets the base URL for the server.
         *
         * @param baseUrl base URL for all requests
         * @return this builder
         */
        public Builder baseUrl(URI baseUrl) {
            this.baseUrl = Objects.requireNonNull(baseUrl, "baseUrl");
            return this;
        }

        /**
         * Sets the API key used for the {@code apikey} header.
         *
         * @param apiKey the API key
         * @return this builder
         */
        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        /**
         * Sets the HTTP client connect timeout.
         *
         * @param connectTimeout the connect timeout
         * @return this builder
         */
        public Builder connectTimeout(Duration connectTimeout) {
            this.connectTimeout = Objects.requireNonNull(connectTimeout, "connectTimeout");
            return this;
        }

        /**
         * Sets the per-request timeout.
         *
         * @param requestTimeout the request timeout
         * @return this builder
         */
        public Builder requestTimeout(Duration requestTimeout) {
            this.requestTimeout = Objects.requireNonNull(requestTimeout, "requestTimeout");
            return this;
        }

        /**
         * Supplies a custom {@link HttpClient}.
         *
         * @param httpClient the HTTP client to use
         * @return this builder
         */
        public Builder httpClient(HttpClient httpClient) {
            this.httpClient = Objects.requireNonNull(httpClient, "httpClient");
            return this;
        }

        /**
         * Supplies a custom {@link ObjectMapper}.
         *
         * @param objectMapper the mapper to use for JSON serialization
         * @return this builder
         */
        public Builder objectMapper(ObjectMapper objectMapper) {
            this.objectMapper = Objects.requireNonNull(objectMapper, "objectMapper");
            return this;
        }

        /**
         * Builds the configured client.
         *
         * @return a new {@link SeekStormClient}
         */
        public SeekStormClient build() {
            ObjectMapper mapper = objectMapper == null ? defaultMapper() : objectMapper;
            HttpClient client = httpClient == null ? HttpClient.newBuilder().connectTimeout(connectTimeout).build() : httpClient;
            SeekStormClientConfig config = new SeekStormClientConfig(baseUrl, apiKey, connectTimeout, requestTimeout);
            return new SeekStormClient(config, client, mapper);
        }

        private static ObjectMapper defaultMapper() {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
            return mapper;
        }
    }
}