package com.seekstorm.client.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.util.EnumSet;

final class NgramSetMaskSerializer extends JsonSerializer<EnumSet<NgramSet>> {
    @Override
    public void serialize(EnumSet<NgramSet> value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null || value.isEmpty()) {
            gen.writeNull();
            return;
        }
        int mask = 0;
        for (NgramSet ngramSet : value) {
            mask |= ngramSet.mask();
        }
        gen.writeNumber(mask);
    }
}