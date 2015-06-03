
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Andy Li
 */
public class ChatRoom implements ChatInterface {

    private ArrayList<String> chatHistory;
    private int currentPort = 4444;
    private HashMap<String, Integer> vectorTimestamp;
    private String leader, tempLeader;
    private int power, tempPower;
    private boolean inProgress;

    public ChatRoom(int port) throws RemoteException {
        chatHistory = new ArrayList<>();
        vectorTimestamp = new HashMap<>();
        inProgress = false;
        LocateRegistry.createRegistry(port);
        try {  // create stub (note prior to Java 5.0 must use rmic utility)
            ChatInterface stub = (ChatInterface) UnicastRemoteObject.exportObject(this, 0);
            // get the registry which is running on the default port 1099
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind("chat", stub);//binds if not already
            // display the names currently bound in the registry
            System.out.println("Names bound in RMI registry");
            try {
                String[] bindings = Naming.list("localhost"); // no URL
                for (String name : bindings) {
                    System.out.println(name);
                }
            } catch (MalformedURLException e) {
                System.err.println("Unable to see names: " + e);
            }
        } catch (RemoteException e) {
            System.err.println("Unable to bind to registry: " + e);
        }
        // note that separate thread created to keep remoteObject alive
        System.out.println("Chat Room is Created");
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

    @Override
    public void updateTimestamp(HashMap<String, Integer> other) throws RemoteException {
        if (vectorTimestamp.keySet() != null || !other.keySet().isEmpty()) {
            for (String os : other.keySet()) {
                if (vectorTimestamp.keySet().contains(os)) {
                    // for the record which is already in this VectorTimestamp
                    if (vectorTimestamp.get(os).compareTo(other.get(os)) > 0) {
                        // update the time of the process if time is greater than the current record
                        vectorTimestamp.put(os, other.get(os));
                    }
                } else {
                    // add the record which is no in this VectorTimestamp
                    vectorTimestamp.put(os, other.get(os));
                }
            }
        } else {
            for (String os : other.keySet()) {
                vectorTimestamp.put(os, other.get(os));
            }
        }
    }

    @Override
    public HashMap<String, Integer> getVectorTimestamp() throws RemoteException {
        return vectorTimestamp;
    }

    @Override
    public void election(String name, int power) throws RemoteException {
        tempPower = power;
        tempLeader = name;
    }

    @Override
    public void startElection(boolean start) throws RemoteException {
        inProgress = start;
        if (!start) {
            if (tempPower > power) {
                leader = tempLeader;
                power = tempPower;

            }
        }
    }

    @Override
    public boolean checkProgress() throws RemoteException {
        return inProgress;
    }

    @Override
    public int getTempPower() throws RemoteException {
        return tempPower;
    }

    @Override
    public String getLeader() throws RemoteException {
        return leader;
    }

    @Override
    public int getPower() throws RemoteException {
        return power;
    }

}
