package com.seekstorm.client.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;

/**
 * Request payload for creating a SeekStorm index.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CreateIndexRequest(
        /** The index name. */ String indexName,
        /** The schema definition for the index. */ List<SchemaField> schema,
        /** The similarity model, such as BM25 variants. */ LexicalSimilarity similarity,
        /** The tokenizer to use. */ TokenizerType tokenizer,
        /** The stemmer to use. */ StemmerType stemmer,
        /** Stop-word preset. */ StopwordType stopWords,
        /** Frequent-word preset. */ FrequentwordType frequentWords,
        /** N-gram indexing bitmask. */ @JsonSerialize(using = NgramSetMaskSerializer.class) @JsonDeserialize(using = NgramSetMaskDeserializer.class) EnumSet<NgramSet> ngramIndexing,
        /** Document compression preset. */ DocumentCompression documentCompression,
        /** Synonym configuration. */ List<String> synonyms,
        /** Enables spelling correction. */ Boolean spellingCorrection,
        /** Enables query completion. */ Boolean queryCompletion,
        /** Clustering preset. */ Clustering clustering,
        /** Inference preset. */ Inference inference) {

        public CreateIndexRequest {
                indexName = indexName == null ? "" : indexName;
                schema = schema == null ? List.of() : List.copyOf(schema);
        }

        /**
         * Creates an index request with only required fields.
         *
         * @param indexName index name
         * @param schema schema definition
         */
        public CreateIndexRequest(String indexName, List<SchemaField> schema) {
                this(indexName, schema, null, null, null, null, null, null, null, null, null, null, null, null);
        }

        /**
         * Creates an index request with an empty schema.
         *
         * @param indexName index name
         */
        public CreateIndexRequest(String indexName) {
                this(indexName, List.of());
        }

        /**
         * Creates a fluent builder for index creation.
         *
         * @param indexName index name
         * @param schema schema definition
         * @return builder instance
         */
        public static Builder builder(String indexName, List<SchemaField> schema) {
                return new Builder(indexName, schema);
        }

        public static final class Builder {
                private final String indexName;
                private final List<SchemaField> schema;
                private LexicalSimilarity similarity;
                private TokenizerType tokenizer;
                private StemmerType stemmer;
                private StopwordType stopWords;
                private FrequentwordType frequentWords;
                private EnumSet<NgramSet> ngramIndexing;
                private DocumentCompression documentCompression;
                private List<String> synonyms;
                private Boolean spellingCorrection;
                private Boolean queryCompletion;
                private Clustering clustering;
                private Inference inference;

                private Builder(String indexName, List<SchemaField> schema) {
                        this.indexName = Objects.requireNonNull(indexName, "indexName");
                        this.schema = schema == null ? List.of() : List.copyOf(schema);
                }

                public Builder similarity(LexicalSimilarity similarity) {
                        this.similarity = similarity;
                        return this;
                }

                public Builder tokenizer(TokenizerType tokenizer) {
                        this.tokenizer = tokenizer;
                        return this;
                }

                public Builder stemmer(StemmerType stemmer) {
                        this.stemmer = stemmer;
                        return this;
                }

                public Builder stopWords(StopwordType stopWords) {
                        this.stopWords = stopWords;
                        return this;
                }

                public Builder frequentWords(FrequentwordType frequentWords) {
                        this.frequentWords = frequentWords;
                        return this;
                }

                public Builder ngramIndexing(EnumSet<NgramSet> ngramIndexing) {
                        this.ngramIndexing = ngramIndexing;
                        return this;
                }

                public Builder documentCompression(DocumentCompression documentCompression) {
                        this.documentCompression = documentCompression;
                        return this;
                }

                public Builder synonyms(List<String> synonyms) {
                        this.synonyms = synonyms;
                        return this;
                }

                public Builder spellingCorrection(Boolean spellingCorrection) {
                        this.spellingCorrection = spellingCorrection;
                        return this;
                }

                public Builder queryCompletion(Boolean queryCompletion) {
                        this.queryCompletion = queryCompletion;
                        return this;
                }

                public Builder clustering(Clustering clustering) {
                        this.clustering = clustering;
                        return this;
                }

                public Builder inference(Inference inference) {
                        this.inference = inference;
                        return this;
                }

                public CreateIndexRequest build() {
                        return new CreateIndexRequest(
                                        indexName,
                                        schema,
                                        similarity,
                                        tokenizer,
                                        stemmer,
                                        stopWords,
                                        frequentWords,
                                        ngramIndexing,
                                        documentCompression,
                                        synonyms,
                                        spellingCorrection,
                                        queryCompletion,
                                        clustering,
                                        inference);
                }
        }
}