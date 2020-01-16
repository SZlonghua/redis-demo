package com.example.redis.overrite;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class Ledis {

    private Client client;

    public Ledis(String host,int port) {
        this.client = new Client(host, port);
    }

    public String set(final String key, final String value) {
        log.info("Ledis set key value");
        client.set(key, value);
        //return client.getStatusCodeReply();
        return null;
    }
}
