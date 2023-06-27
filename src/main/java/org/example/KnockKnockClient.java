package org.example;

import java.net.*;
import java.io.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import org.example.KnockKnockProtocol;

/*
* Kada se pokreće KnockKnockClient pri run-anju treba postaviti argumente 127.0.0.1 4444
* */
public class KnockKnockClient {
    private static final byte[] BUFFER = new byte[256];
    public static void main(String[] args) throws IOException {

        //if (args.length != 2) {
        //    System.err
        //            .println("Usage: java EchoClient <host name> <port number>");
        //    System.exit(1);
        //}

        //String hostName = args[0];
        //int portNumber = Integer.parseInt(args[1]);

        InetAddress address;
        String receivedData;

        try(DatagramSocket socket = new DatagramSocket(null)){
            socket.setReuseAddress(true);
            socket.bind(new InetSocketAddress(6666));
            DatagramPacket packet = new DatagramPacket(BUFFER, BUFFER.length);
            socket.receive(packet);

            address = packet.getAddress();
            int port = packet.getPort();
            packet = new DatagramPacket(BUFFER, BUFFER.length, address, port);
            receivedData = new String(packet.getData(), 0, packet.getLength());
        }

        System.out.println(receivedData);
        String[] receivedDataParts = receivedData.split("__");
        String serverName = receivedDataParts[0];
        int portNumber = Integer.parseInt(receivedDataParts[1]);

        try (Socket kkSocket = new Socket(serverName, portNumber);
             PrintWriter out = new PrintWriter(
                     kkSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(kkSocket.getInputStream()));) {
            BufferedReader stdIn = new BufferedReader(
                    new InputStreamReader(System.in));
            String fromServer;

            InputCollector inputCollector = new InputCollector(out::println);
            inputCollector.start();

            while ((fromServer = in.readLine()) != null) {
                System.out.println("Server: " + fromServer);
                if (fromServer.equals("Bye."))
                    break;
            }

            inputCollector.stopCollecting();

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + serverName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to "
                    + serverName);
            System.exit(1);
        }
    }

    public static class InputCollector extends Thread {
        private final Consumer<String> inputConsumer;
        private final AtomicBoolean running = new AtomicBoolean(false);

        public InputCollector(final Consumer<String> inputConsumer){
            super("InputCollector");
            this.inputConsumer = inputConsumer;
        }

        public void run() {
            running.set(true);
            try(BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))){
                String input = null;
                while (running.get() && (input = stdIn.readLine()) != null){
                    inputConsumer.accept(input);
                }
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        public void stopCollecting(){
            running.set(false);
        }
    }

}

