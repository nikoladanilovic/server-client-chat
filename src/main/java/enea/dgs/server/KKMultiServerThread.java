package enea.dgs.server;

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

import enea.dgs.chat.ClientMessageObservable;
import enea.dgs.chat.ClientMessageObserver;
import enea.dgs.knockknock.KnockKnockProtocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class KKMultiServerThread extends Thread {
    private final Socket socket;
    private final String identifier;

    public KKMultiServerThread(Socket socket, long identifier) {
        super("KKMultiServerThread");
        this.socket = socket;
        this.identifier = "client_" + identifier;
    }

    public void run() {

        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true); BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));) {

            ClientMessageObserver messageObserver = ClientMessageObserver.of((clientID, message) -> {
                if (!identifier.equals(clientID))
                    out.println(clientID + "!" + message);
            });
            messageObserver.attach();

            // send to client last received messages from the other clients
            Executors.newSingleThreadScheduledExecutor().schedule(
                    () -> LastKnownPlayerLocations.getInstance().getKnownLocations(identifier).forEach(out::println),
                    1000L, TimeUnit.MILLISECONDS);

            String inputLine;
            String outputLine;

            KnockKnockProtocol kkp = new KnockKnockProtocol();
            outputLine = kkp.processInput(null);
            out.println(String.format("Hello %s!", identifier));

            while ((inputLine = in.readLine()) != null) {
                outputLine = kkp.processInput(inputLine);
                if (outputLine.equals("Bye")) {
                    out.println(outputLine);
                    break;
                }

                ClientMessageObservable.getInstance().broadcastMessageFrom(identifier, inputLine);
            }

            messageObserver.detach();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            // TODO: close socket and detach observer
        }
    }
}
