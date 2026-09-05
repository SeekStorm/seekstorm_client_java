# SeekStorm Java REST Client

<img src="https://raw.githubusercontent.com/SeekStorm/seekstorm_client_cs/main/assets/logo.png" width="450" alt="Logo"><br>
**Java REST client** for the **SeekStorm vector & lexical search server**.

seekstorm_client_java is open source licensed under the [Apache License 2.0](https://github.com/SeekStorm/seekstorm_client_java?tab=Apache-2.0-1-ov-file#readme)

## SeekStorm REST client (Java)
[![Maven Central](https://img.shields.io/maven-central/v/org.seekstorm.client/seekstorm-java)](https://central.sonatype.com/artifact/org.seekstorm.client/seekstorm-java)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/SeekStorm/seekstorm_client_java?tab=Apache-2.0-1-ov-file#readme)

## SeekStorm REST client (TypeScript)
[![GitHub Stars](https://img.shields.io/github/stars/SeekStorm/seekstorm_client_ts)](https://github.com/SeekStorm/seekstorm_client_ts)
[![npm](https://img.shields.io/npm/v/seekstorm_client_ts?label=npm)](https://www.npmjs.com/package/seekstorm_client_ts)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/SeekStorm/seekstorm_client_pure_ts?tab=Apache-2.0-1-ov-file#readme)

## SeekStorm REST client (Python)
[![GitHub Stars](https://img.shields.io/github/stars/SeekStorm/seekstorm_client_pure_py)](https://github.com/SeekStorm/seekstorm_client_pure_py)
[![PyPI](https://img.shields.io/pypi/v/seekstorm-client-pure-py?label=PyPI)](https://pypi.org/project/seekstorm-client-pure-py/)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/SeekStorm/seekstorm_client_pure_py?tab=Apache-2.0-1-ov-file#readme)

## SeekStorm REST client (C#)
[![GitHub Stars](https://img.shields.io/github/stars/SeekStorm/seekstorm_client_cs)](https://github.com/SeekStorm/seekstorm_client_cs)
[![NuGet version](https://badge.fury.io/nu/SeekStorm.Client.svg)](https://badge.fury.io/nu/SeekStorm.Client)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/SeekStorm/seekstorm_client_cs?tab=Apache-2.0-1-ov-file#readme)

## SeekStorm REST client (Rust)
[![GitHub Stars](https://img.shields.io/github/stars/SeekStorm/SeekStorm)](https://github.com/SeekStorm/SeekStorm)
[![Crates.io](https://img.shields.io/crates/v/seekstorm_client_rs.svg)](https://crates.io/crates/seekstorm_client_rs)
[![Downloads](https://img.shields.io/crates/d/seekstorm_client_rs.svg?style=flat-square)](https://crates.io/crates/seekstorm_client_rs)
[![Documentation](https://docs.rs/seekstorm_client_rs/badge.svg)](https://docs.rs/seekstorm_client_rs)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/SeekStorm/SeekStorm?tab=Apache-2.0-1-ov-file#readme)
[![Roadmap](https://img.shields.io/badge/Roadmap-2026-DA7F07.svg)](#roadmap)

## SeekStorm multi-tenancy search server
[![GitHub Stars](https://img.shields.io/github/stars/SeekStorm/SeekStorm)](https://github.com/SeekStorm/SeekStorm)
[![Crates.io](https://img.shields.io/crates/v/seekstorm_server.svg)](https://crates.io/crates/seekstorm_server)
[![Downloads](https://img.shields.io/crates/d/seekstorm_server.svg?style=flat-square)](https://crates.io/crates/seekstorm_server)
[![Docker](https://img.shields.io/docker/pulls/wolfgarbe/seekstorm_server)](https://hub.docker.com/r/wolfgarbe/seekstorm_server)
[![REST API Documentation](https://docs.rs/seekstorm/badge.svg)](https://seekstorm.github.io/documentation/)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/SeekStorm/SeekStorm?tab=Apache-2.0-1-ov-file#readme)
[![Roadmap](https://img.shields.io/badge/Roadmap-2026-DA7F07.svg)](#roadmap)

## SeekStorm in-process search library
[![GitHub Stars](https://img.shields.io/github/stars/SeekStorm/SeekStorm)](https://github.com/SeekStorm/SeekStorm)
[![Crates.io](https://img.shields.io/crates/v/seekstorm.svg)](https://crates.io/crates/seekstorm)
[![Downloads](https://img.shields.io/crates/d/seekstorm.svg?style=flat-square)](https://crates.io/crates/seekstorm)
[![Documentation](https://docs.rs/seekstorm/badge.svg)](https://docs.rs/seekstorm)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/SeekStorm/SeekStorm?tab=Apache-2.0-1-ov-file#readme)
[![Roadmap](https://img.shields.io/badge/Roadmap-2026-DA7F07.svg)](#roadmap)
<p>
  <a href="https://seekstorm.com">Website</a> | 
  <a href="https://seekstorm.github.io/search-benchmark-game/">Benchmark</a> | 
  <a href="https://deephn.org/">Demo</a> | 
  <a href="https://github.com/SeekStorm/seekstorm_client_py">Repository for SeekStorm Python client </a> | 
  <a href="https://github.com/SeekStorm/SeekStorm">Repository for SeekStorm library, server, Rust client </a> | 
  <a href="https://github.com/SeekStorm/SeekStorm#roadmap">Roadmap</a> | 
  <a href="https://seekstorm.com/blog/">Blog</a> | 
  <a href="https://x.com/seekstorm">X</a>
</p>

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

