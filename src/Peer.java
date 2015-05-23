
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.Socket;
import javax.swing.JTextArea;
import static javax.swing.SwingWorker.StateValue.DONE;

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

    int inPort, outPort;
    String myHost, peerHost;

    public Peer(int outPort) throws IOException {
        this.outPort = outPort;
        myHost = Inet4Address.getLocalHost().getHostAddress();
    }

    public void setInPort(int inPort) {
        this.inPort = inPort;
    }

    public void setPeerHost(String peerHost) {
        this.peerHost = peerHost;
    }

    public Peer(int inPort, int outPort, String peerHost) throws IOException {
        this.inPort = inPort;
        this.outPort = outPort;
        this.peerHost = peerHost;
        myHost = Inet4Address.getLocalHost().getHostAddress();
    }
    
    private class Recievor implements Runnable {

        private Socket socket; // socket for client/server communication
        private JTextArea textArea;
        
        
        public Recievor(Socket socket, JTextArea textArea) {
            this.socket = socket;
            this.textArea = textArea;
        }

        public void run() {
            PrintWriter pw = null; // output stream to client
            BufferedReader br = null; // input stream from client
            try {  // create an autoflush output stream for the socket
                pw = new PrintWriter(socket.getOutputStream(), true);
                // create a buffered input stream for this socket
                br = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()));
                // echo client messages until DONE is received
                String clientRequest;
                do {  // start communication by waiting for client request
                    clientRequest = br.readLine(); // blocking
                    String from = socket.getInetAddress().toString();
                    textArea.append(from + ":\n" + clientRequest);
                    System.out.println("Received line: " + clientRequest);
                } while (clientRequest != null);
                System.out.println("Closing connection with "
                        + socket.getInetAddress());
            } catch (IOException e) {
                System.err.println("Server error: " + e);
            } finally {
                try {
                    if (pw != null) {
                        pw.close();
                    }
                    if (br != null) {
                        br.close();
                    }
                    if (socket != null) {
                        socket.close();
                    }
                } catch (IOException e) {
                    System.err.println("Failed to close streams: " + e);
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {

        Peer p = new Peer(4444);

//        Peer p = new Peer(4444, 4445, "192.168.1.3");
    }

}
