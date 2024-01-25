package com.PeerToPeer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThreadThread extends Thread
{
    private final ServerThread serverThread;
    private final Socket socket;
    private PrintWriter pw;

    public ServerThreadThread(Socket socket, ServerThread serverThread)
    {
        this.serverThread = serverThread;
        this.socket = socket;
    }

    @Override
    public void run()
    {
        try
        {
            BufferedReader bf = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.pw = new PrintWriter(socket.getOutputStream(), true);
            String message;

            while((message = bf.readLine()) != null)
            {
                serverThread.sendMessages(message);
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public PrintWriter getPw() {return pw;}
}
