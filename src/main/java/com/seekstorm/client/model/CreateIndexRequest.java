package com.seekstorm.client.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.List;
import java.util.EnumSet;

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
}