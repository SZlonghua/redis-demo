package com.example.redis;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class CustomerJedisTest {

    public static final String command = "*3\r\n" +
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
    public static void main(String[] args) {
        Socket socket = null;
        try {
            // 与服务端建立连接
            socket = new Socket("127.0.0.1", 6379);
            socket.setSoTimeout(3 * 1000);

            System.out.println("建立了链接\n");

            // 往服务写数据
            Writer writer = new OutputStreamWriter(socket.getOutputStream());

            System.out.println("准备写入\n");

            writer.write(get);

            System.out.println("写完啦 你收下\n");

            writer.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            br.lines().forEach(l->
                    System.out.println(l.toString())
            );

            Thread.sleep(100*1000);
        } catch (SocketTimeoutException e) {
            System.out.println("没收到回复 我下啦\n\n\n\n\n");
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        }  catch (UncheckedIOException e) {
            System.out.println("没收到回复 我下啦ssssss\n\n\n\n\n");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
