package com.example.redis.overrite;


import lombok.extern.slf4j.Slf4j;

import java.io.IOException;



@Slf4j
public class Client extends Connection {
    public static final String set = "*3\r\n" +
            "$3\r\n" +
            "SET\r\n" +
            "$5\r\n" +
            "mykey\r\n" +
            "$7\r\n" +
            "myvalue\r\n";
    public static final String get = "*2\r\n" +
            "$3\r\n" +
            "GET\r\n" +
            "$5\r\n" +
            "mykey\r\n";


    public Client(String host, int port) {
        super(host, port);
    }

    public void set(final String key, final String value) {
        log.info("client set key value");

        try {
            connect();
            writer.write(get);
            getStatusCodeReply();
            log.info("client set sucess");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
