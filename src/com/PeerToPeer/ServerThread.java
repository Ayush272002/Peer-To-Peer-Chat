package com.PeerToPeer;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;

public class ServerThread extends Thread
{
    private ServerSocket serverSocket;
    private Set<ServerThreadThread> serverThreadThreads  = new HashSet<>();

    public ServerThread(String portNumber) throws IOException
    {
        serverSocket = new ServerSocket(Integer.parseInt(portNumber));
    }

    @Override
    public void run()
    {
        try
        {
            while(true)
            {
                ServerThreadThread serverThreadThread = new ServerThreadThread(serverSocket.accept(), this);
                serverThreadThreads.add(serverThreadThread);
                serverThreadThread.start();
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
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

    public Set<ServerThreadThread> getServerThreadThreads()
    {
        return serverThreadThreads;
    }
}
