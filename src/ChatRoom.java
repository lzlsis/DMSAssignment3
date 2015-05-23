
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Andy Li
 */


public class ChatRoom implements ChatInterface{

    private ArrayList<String> chatHistory;
    
    public ChatRoom(int port) throws RemoteException{
        chatHistory = new ArrayList<String>();
        LocateRegistry.createRegistry(port);
        try
      {  // create stub (note prior to Java 5.0 must use rmic utility)
         ChatInterface stub = (ChatInterface)
            UnicastRemoteObject.exportObject(this, 0);
         // get the registry which is running on the default port 1099
         Registry registry = LocateRegistry.getRegistry();
         registry.rebind("chat", stub);//binds if not already
         // display the names currently bound in the registry
         System.out.println("Names bound in RMI registry");
         try
         {  String[] bindings = Naming.list("localhost"); // no URL
            for (String name : bindings)
               System.out.println(name); 
         }
         catch (MalformedURLException e)
         {  System.err.println("Unable to see names: " + e);
         }
      }
      catch (RemoteException e)
      {  System.err.println("Unable to bind to registry: " + e);
      }
      // note that separate thread created to keep remoteObject alive
      System.out.println("Main method of RMIGreetingImpl done");
    }
    @Override
    public ArrayList<String> getChatHistory() throws RemoteException {
       return chatHistory;
    }

    @Override
    public void createChat(String message) throws RemoteException {
        chatHistory.add(message);
    }

    @Override
    public void clear() throws RemoteException {
       chatHistory.clear();
    }


    
}
