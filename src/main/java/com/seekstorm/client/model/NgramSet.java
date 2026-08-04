package com.seekstorm.client.model;

/**
 * N-gram indexing flags used as a bitmask.
 */
public enum NgramSet {
    NgramFF(1),
    NgramFFF(2);

    private final int mask;

    NgramSet(int mask) {
        this.mask = mask;
    }

    public int mask() {
        return mask;
    }

    public static int maskOf(NgramSet... values) {
        int result = 0;
        if (values != null) {
            for (NgramSet value : values) {
                if (value != null) {
                    result |= value.mask;
                }
            }
        }
        return result;
    }
}