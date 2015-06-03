/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Andy Li
 */

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public interface ChatInterface extends Remote
{ 
    public ArrayList<String> getChatHistory() throws RemoteException; 
    public void createChat(String message) throws RemoteException; 
    public void clear() throws RemoteException; 
    public void updateTimestamp(HashMap<String, Integer> timestamp) throws RemoteException; 
    public void election(String name, int power) throws RemoteException; 
    public void startElection(boolean start) throws RemoteException; 
    public boolean checkProgress() throws RemoteException; 
    public int getTempPower() throws RemoteException; 
    public String getLeader() throws RemoteException; 
    public int getPower() throws RemoteException; 
    public HashMap<String, Integer> getVectorTimestamp() throws RemoteException; 
}
