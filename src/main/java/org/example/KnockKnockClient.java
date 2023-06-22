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
    public static void main(String[] args) throws IOException {

        if (args.length != 2) {
            System.err
                    .println("Usage: java EchoClient <host name> <port number>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        try (Socket kkSocket = new Socket(hostName, portNumber);
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
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to "
                    + hostName);
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

