
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import javax.swing.JTextArea;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Andy Li
 */
public class Peer {

    public static final String REGISTRY_URL = "192.168.1.2";

    public static void main(String[] args) throws IOException {

        try {
            Registry registry = LocateRegistry.getRegistry(REGISTRY_URL);
            ChatInterface remoteProxy
                    = (ChatInterface) registry.lookup("chat");
            ArrayList<String> chatHistory = remoteProxy.getChatHistory();
            final StringBuilder stringBuilder = new StringBuilder();
//            for (int i = 0; i < chatHistory.size() && i < 15;
//                    i++) {
//                stringBuilder.append(chatHistory.get(i));
//                stringBuilder.append("\n");
//            }
            for (String m : chatHistory) {
                stringBuilder.append(m);
                stringBuilder.append("\n");
            }
            System.out.println(stringBuilder.toString());
        } catch (RemoteException e) {
            System.err.println("Unable to use registry: " + e);
        } catch (NotBoundException e) {
            System.err.println("Name greeting not currently bound: " + e);
        }
    }

}
