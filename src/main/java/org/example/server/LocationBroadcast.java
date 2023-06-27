package org.example.server;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class LocationBroadcast extends Thread {
    private final int tcpLocation;
    private final String serverName;
    private final AtomicBoolean running = new AtomicBoolean(false);

    public LocationBroadcast(final int tcpLocation, final String serverName){
        super("LocationBroadcast");
        this.tcpLocation = tcpLocation;
        this.serverName = serverName;
    }

    public void run() {
        running.set(true);
        try{
            DatagramSocket socket = new DatagramSocket();
            socket.setBroadcast(true);
            byte[] buffer = (serverName + "__" + tcpLocation + "__").getBytes();
            InetAddress broadcastAdress = InetAddress.getByName("255.255.255.255");
            //InetAddress multicastAddress = InetAddress.getByName("239.255.255.255");

            while (running.get()){
                DatagramPacket packet = null;
                packet = new DatagramPacket(buffer, buffer.length, broadcastAdress, 6666);
                socket.send(packet);
                try{
                    sleep(1000L);
                } catch (InterruptedException e){
                    System.out.println("Interrupted exception raised!");
                }
            }
            socket.close();
        } catch (UnknownHostException e){
            System.out.println("Unknown host exception raised!");
        } catch (SocketException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void stopBroadcasting() {
        running.set(false);
    }


}
