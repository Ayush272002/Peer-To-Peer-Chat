package com.PeerToPeer;

import javax.json.Json;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.Socket;

public class Peer
{
    public static void main(String[] args) throws Exception
    {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter the username and port for this peer :");
        String[] setupValues = bf.readLine().split(" ");
        ServerThread serverThread = new ServerThread(setupValues[1]);
        serverThread.start();
        new Peer().updateListenToPeers(bf, setupValues[0], serverThread);
    }

    public void updateListenToPeers(BufferedReader bf, String username, ServerThread serverThread) throws Exception
    {
        System.out.println("Enter (space seperated) hostname:port#");
        System.out.println("peers to receive messages from (s to skip)");
        String input = bf.readLine();
        String[] inputValues = input.split(" ");
        if(!input.equals("s"))
        {
            for (int i = 0; i < inputValues.length; i++)
            {
                String[] address = inputValues[i].split(":");
                Socket socket = null;

                try
                {
                    socket = new Socket(address[0], Integer.parseInt(address[1]));
                    new PeerThread(socket).start();
                }
                catch (Exception e)
                {
                    if(socket != null) socket.close();
                    else System.out.println("Invalid input skipping to next ");
                }
            }
        }

        communicate(bf, username,serverThread);
    }

    public void communicate(BufferedReader bf, String username, ServerThread serverThread)
    {
        try
        {
            System.out.println("You can now communicate (e to exit, c to change)");
            boolean flag = true;

            while(flag)
            {
                String message = bf.readLine();
                if(message.equals("e"))
                {
                    flag = false;
                    //break;
                }
                else if(message.equals("c")) updateListenToPeers(bf, username, serverThread);
                else
                {
                    StringWriter sw = new StringWriter();

                    Json.createWriter(sw).writeObject(Json.createObjectBuilder().add("username", username).add("message",message).build());
                    serverThread.sendMessages(sw.toString());
                }
            }
            System.exit(0);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
