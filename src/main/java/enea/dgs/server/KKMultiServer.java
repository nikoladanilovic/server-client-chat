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

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.atomic.AtomicLong;

public class KKMultiServer {

    private static final AtomicLong IDENTIFIER = new AtomicLong(1L);

    public static void main(String[] args) throws IOException {

        if (args.length != 2) {
            System.err.println("Usage: java KKMultiServer <port number> <name>");
            System.exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);
        String serverName = args[1];
        boolean listening = true;

        LocationBroadcast broadcast = new LocationBroadcast(portNumber, serverName);
        broadcast.start();

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            while (listening) {
                new KKMultiServerThread(serverSocket.accept(), getNextIdentifier()).start();
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + portNumber);
            broadcast.stopBroadcasting();
            System.exit(-1);
        }
    }

    /**
     * Increments the {@link #IDENTIFIER} used for the client distinction.
     * {@link #IDENTIFIER} value is reset to zero in case maximum {@code long} value is reached.
     *
     * @return {@code long} value
     */
    private static synchronized long getNextIdentifier() {
        long value = IDENTIFIER.getAndIncrement();
        IDENTIFIER.compareAndSet(Long.MAX_VALUE, 0);

        return value;
    }

}
