package com.kuzmich.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hashids.Hashids;

@Getter
public class CryptoTool {

    private final Hashids hashids;
    private final int minHashLength;

    public CryptoTool(String secret, int minHashLength) {
        this.minHashLength = minHashLength;
        this.hashids = new Hashids(secret, this.minHashLength);
    }

    public String toHash(Long value) {
        return hashids.encode(value);
    }

    public Long fromHash(String value) {
        long[] result = hashids.decode(value);
        if(result != null && result.length > 0) {
            return result[0];
        }
        return null;
    }
}
