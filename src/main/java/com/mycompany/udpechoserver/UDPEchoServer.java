package com.mycompany.udpechoserver;

/**
 *
 * @author massiveboi
 */
import java.io.*;
import java.net.*;
import java.util.*;

public class UDPEchoServer {

    private static final int PORT = 8443;
    private static DatagramSocket dgramSocket;
    private static DatagramPacket inPacket, outPacket;
    private static byte[] buffer;
    String details = "";

    public static void main(String[] args) {

        System.out.println("Opening port...\n");

        try {
            dgramSocket = new DatagramSocket(PORT); //Step 1. Create a DatagramSocket object

        } catch (SocketException e) {
            System.out.println("Unable to attach to port!");
            System.exit(1);
        }
        run();
    }


    private static void run() {
        try {
            String messageIn, messageOut;
            int numMessages = 0;
            ArrayList < String > event = new ArrayList < String > ();
            event.add("15 October 2021, 5 pm, is a good day.");//sample event

            do {
                buffer = new byte[256]; //Step 2. Create a buffer for incoming datagrams

                inPacket = new DatagramPacket(buffer, buffer.length); //Step 3. Create a DatagramPacket object for the incoming datagrams

                dgramSocket.receive(inPacket); //Step 4. Accept an incoming datagram

                InetAddress clientAddress = inPacket.getAddress(); //Step 5. Accept the sender’s address and port from the packet
                int clientPort = inPacket.getPort(); //Step 5. Accept the sender’s address and port from the packet

                messageIn = new String(inPacket.getData(),
                    0,
                    inPacket.getLength()); //Step 6. Retrieve the data from the buffer

                System.out.println("Message received.");

                StringTokenizer st = new StringTokenizer(messageIn, " ");//instantiate stringtokenizer object

                String date = st.nextToken() + " " + st.nextToken() + " " + st.nextToken();//set date parameter

                Boolean add = true;//new event
                String output = "";

                for (int i = 0; i < event.size(); i++) {//loop through events
                    
                    if (event.get(i).contains(date)) {//if entered date is already present in list
                        
                        while (st.hasMoreTokens()) {
                            
                            event.set(i, event.get(i) + " " + st.nextToken()); //add description
                        }
                        
                        output = event.get(i);//return all events for the day
                        add = false;
                        break;
                        
                    }
                }
                if (add) {
                    event.add(messageIn);//add event to a new day
                    output = messageIn;
                }
                
                numMessages++;

                messageOut = ("Message " + numMessages + ": " + output);

                outPacket = new DatagramPacket(messageOut.getBytes(),
                    messageOut.length(),
                    clientAddress,
                    clientPort); //Step 7. Create the response datagram

                dgramSocket.send(outPacket); //Step 8. Send the response datagram

            }
            while (true);
        } catch (IOException e) {
            
            e.printStackTrace();
            
        } finally { //If exception thrown, close connection.
            
            System.out.println("\n* Closing connection... *");
            dgramSocket.close(); //Step 9.  Close the DatagramSocket
        }
    }
}