package com.seekstorm.client.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.util.EnumSet;

final class NgramSetMaskDeserializer extends JsonDeserializer<EnumSet<NgramSet>> {
    @Override
    public EnumSet<NgramSet> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (p.currentToken() == JsonToken.VALUE_NULL) {
            return null;
        }
        int mask = p.getIntValue();
        EnumSet<NgramSet> result = EnumSet.noneOf(NgramSet.class);
        for (NgramSet ngramSet : NgramSet.values()) {
            if ((mask & ngramSet.mask()) != 0) {
                result.add(ngramSet);
            }
        }
        return result;
    }
}