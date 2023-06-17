package org.example;

import java.net.*;
import java.io.*;
import org.example.KnockKnockProtocol;

/*
 * Kada se pokreće KnockKnockClient pri run-anju treba postaviti argumente 127.0.0.1 4444
 * */
public class KnockKnockClient2 {
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
            String fromUser;

            while ((fromServer = in.readLine()) != null) {
                System.out.println("Server: " + fromServer);
                if (fromServer.equals("Bye."))
                    break;

                fromUser = stdIn.readLine();
                if (fromUser != null) {
                    System.out.println("Client: " + fromUser);
                    out.println(fromUser);
                }
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to "
                    + hostName);
            System.exit(1);
        }
    }
}