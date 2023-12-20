package com.kuzmich.configuration;

import com.kuzmich.utils.CryptoTool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NodeConfiguration {

    @Value("${hashids.secret}")
    private String hashIdsSecret;
    @Value ("${hashids.min-hash-length}")
    private String minHashLength;

    @Bean
    public CryptoTool cryptoTool() {
        return new CryptoTool(hashIdsSecret, Integer.parseInt(minHashLength));
    }


}
