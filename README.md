# SeekStorm Java REST Client

<img src="https://raw.githubusercontent.com/SeekStorm/seekstorm_client_cs/main/assets/logo.png" width="450" alt="Logo"><br>
**Java REST client** for the **SeekStorm vector & lexical search server**.

seekstorm_client_java is open source licensed under the [Apache License 2.0](https://github.com/SeekStorm/seekstorm_client_java?tab=Apache-2.0-1-ov-file#readme)

## SeekStorm REST client (Java)
[![Maven Central](https://img.shields.io/maven-central/v/org.seekstorm.client/seekstorm-java)](https://central.sonatype.com/artifact/org.seekstorm.client/seekstorm-java)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/SeekStorm/seekstorm_client_java?tab=Apache-2.0-1-ov-file#readme)

## Features

- `java.net.http.HttpClient` transport
- Jackson-based JSON mapping
- Custom `apikey` header support
- Helpers for index, document, file, iterator, and search endpoints

## Build

```bash
mvn test
```

## Quick Start

```java
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seekstorm.client.SeekStormClient;
import com.seekstorm.client.model.SearchRequest;
import com.seekstorm.client.model.SearchResponse;

ObjectMapper mapper = new ObjectMapper();

SeekStormClient client = SeekStormClient.builder()
    .baseUrl("http://127.0.0.1:8080")
    .apiKey("your-apikey")
    .build();

String live = client.live();
System.out.println(live);

JsonNode createIndexRequest = mapper.createObjectNode()
    .put("index_name", "demo")
    .set("schema", mapper.createArrayNode()
        .add(mapper.createObjectNode()
            .put("field", "title")
            .put("field_type", "Text")
            .put("store", true)
            .put("index_lexical", true)));

long indexId = client.createIndex(createIndexRequest);

JsonNode document = mapper.createObjectNode()
    .put("title", "hello seekstorm");
client.indexDocument(String.valueOf(indexId), document);

client.commitIndex(String.valueOf(indexId));

SearchRequest request = SearchRequest.builder("hello")
    .length(5)
    .build();

SearchResponse response = client.search(String.valueOf(indexId), request);
System.out.println("Total hits: " + response.countTotal());
```

## IntelliSense Guidance

- `SeekStormClient` is the synchronous entry point for REST calls.
- `SearchRequest` supports lexical, vector, and hybrid query shapes and uses enum-backed fields for result type, query type, rewriting, and search mode.
- `CreateIndexRequest` uses enum-backed fields for similarity, tokenizer, stemmer, compression, clustering, and inference.
- `GetIteratorRequest` is the iterator payload for paging through documents.
- `GetDocumentRequest` lets you request highlights, fields, and distance fields.
- `ApiKeyQuotaRequest` and `DeleteApiKeyRequest` are for API key lifecycle operations.
- `CreateIndexRequest` and `SchemaField` describe index layout and field capabilities.

## Usage Notes

- Use the demo API key only for local development.
- Call `commitIndex(...)` when you need durable results outside realtime search.
- Use the raw `JsonNode` overloads when you want to send server-specific payloads.

