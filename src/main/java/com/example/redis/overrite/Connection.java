package com.example.redis.overrite;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.util.SafeEncoder;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

@Data
@AllArgsConstructor
@Slf4j
public class Connection {

    private String host;
    private int port;
    protected Writer writer;
    protected InputStream inputStream;

    public Connection(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private Socket socket;

    public void connect() {
        if (!isConnected()){

            try {
                socket = new Socket(host, port);
                //socket.setSoTimeout(200 * 1000);
                writer =  new OutputStreamWriter(socket.getOutputStream());
                inputStream = socket.getInputStream();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public boolean isConnected() {
        return socket != null && socket.isBound() && !socket.isClosed() && socket.isConnected()
                && !socket.isInputShutdown() && !socket.isOutputShutdown();
    }

    protected void flush() {
        log.info("connection flush");
        try {
            writer.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String getStatusCodeReply(){
        log.info("connection getStatusCodeReply");
        flush();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        log.info("connection reciever");
        br.lines().forEach(l->
                System.out.println(l.toString())
        );
        return br.toString();
    }


}
