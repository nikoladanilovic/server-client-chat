package enea.dgs.knockknock;

/*
 * Copyright (c) 1995, 2013, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * Client implementation using the UDP broadcast to find a server network location.
 */
public class KnockKnockClient {

    private static final byte[] BUFFER = new byte[256];

    public static void main(String[] args) throws IOException {

        InetAddress address;
        String receivedData;
        // in case multiple clients try to bind the same address and port exception will occur for the second client
        // (Address already in use), therefore "Reuse" flag is used in order to counter the problem
        // (https://docs.oracle.com/javase/8/docs/api/java/net/DatagramSocket.html#setReuseAddress-boolean-)
//        try (DatagramSocket socket = new DatagramSocket(6666)) {
        try (DatagramSocket socket = new DatagramSocket(null)) {
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
        int portNumber = Integer.parseInt(receivedDataParts[1].trim());

        try (Socket kkSocket = new Socket(address, portNumber);
                PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(kkSocket.getInputStream()));) {

            String fromServer;

            InputCollector inputCollector = new InputCollector(out::println);
            inputCollector.start();

            while ((fromServer = in.readLine()) != null) {
                System.out.println(fromServer);
                if (fromServer.equals("Bye"))
                    break;
            }

            inputCollector.stopCollecting();

        } catch (IOException e) {
            System.err.println("Unexpected exception occurred: " + e);
            System.exit(1);
        }

        System.exit(0);
    }

    public static class InputCollector extends Thread {
        private final Consumer<String> inputConsumer;
        private final AtomicBoolean running = new AtomicBoolean(false);

        public InputCollector(final Consumer<String> inputConsumer) {
            super("InputCollector");
            this.inputConsumer = inputConsumer;
        }

        public void run() {
            running.set(true);
            try (BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {
                String input = null;
                while (running.get() && (input = stdIn.readLine()) != null) {
                    inputConsumer.accept(input);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void stopCollecting() {
            running.set(false);
        }

    }

}
