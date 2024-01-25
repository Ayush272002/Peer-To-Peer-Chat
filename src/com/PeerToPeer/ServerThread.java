package com.PeerToPeer;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;

public class ServerThread extends Thread
{
    private final ServerSocket serverSocket;
    private final Set<ServerThreadThread> serverThreadThreads  = new HashSet<>();

    public ServerThread(String portNumber) throws IOException
    {
        serverSocket = new ServerSocket(Integer.parseInt(portNumber));
    }

    @Override
    public void run() {
        try
        {
            while (!Thread.interrupted())
            {
                ServerThreadThread serverThreadThread = new ServerThreadThread(serverSocket.accept(), this);
                serverThreadThreads.add(serverThreadThread);
                serverThreadThread.start();
            }
        }
        catch (IOException e)
        {
            if (!serverSocket.isClosed())
                System.out.println("ServerSocket accept error: " + e.getMessage());

        }
        finally
        {
            // Close the server socket when the loop exits
            try
            {
                serverSocket.close();
            }
            catch (IOException e)
            {
                System.out.println("Error closing server socket: " + e.getMessage());
            }
        }
    }


    void sendMessages(String message)
    {
        try
        {
            serverThreadThreads.forEach(t->t.getPw().println(message));
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
