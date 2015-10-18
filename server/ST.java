package server;


import javax.swing.JTextArea;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;


public class ST extends Thread{
	
	private JTextArea textArea;
	final int SERVER_PORT = 55000;
	final int MAX_PORT = 55010;
	private ServerSocket serverSOC;
	private boolean[] downloadPort = new boolean[MAX_PORT-SERVER_PORT];
	private Vector<SCT> clients;
	boolean flag;

	public ST(JTextArea textArea) {
		this.textArea=textArea;
		clients=new Vector<SCT>();
		for (int i=0; i<downloadPort.length; i++) downloadPort[i] = true;
	}
	
        @Override
	public void run(){
		try {
			serverSOC= new ServerSocket(SERVER_PORT);
			flag=true;
			while (flag){
				Socket client = serverSOC.accept();
				add(client);
			}
		} catch (IOException e) {
		
                       System.err.println("Cannot listen on this port.\n" + e.getMessage());  
		}
	}

	private synchronized void add(Socket so){
		SCT client = new SCT(so, this);
		clients.add(client);
		//client.start();
		((SCT)clients.lastElement()).start();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			System.err.println("Interrupted Exception: " + e.getMessage());
		}
		String message = "client " + ((SCT)clients.lastElement()).getClientName()+ " entered\n";
		textArea.append(message);
		for (SCT c : clients){
			c.sendMsg(message);
		}
	}
	
	public void finish(){
		flag=false;
		try {
			serverSOC.close();
		} catch (IOException e) {
			System.err.println("Error While closing server.\n" + e.getMessage());
		}
	}

	public synchronized String getAllClients() {
		String clientList="";
		for (SCT client : clients){
			clientList= clientList + client.getClientName() + ",";
		}
		return clientList;
	}

	public synchronized boolean sendMsg(String clientName, String msg) {
		if (clientName.equals("")){
			for (SCT client : clients){
				client.sendMsg(msg);
			}
			return true;
		}else{
			int index =clientSearch(clientName);
			if (index != -1){
				((SCT)clients.elementAt(index)).sendMsg(msg);
				return true;
			}
			else
				return false;
		}
	}

	private int clientSearch(String clientName) {
		for (int i =0; i<clients.size(); i++){
			if (((SCT)clients.elementAt(i)).getClientName().equals(clientName))
				return i;
		}
		return -1;
	}

	public synchronized void disconnect(SCT client) {
		textArea.append("client "+client.getClientName()+" leaved \r\n");
		sendMsg("", "client "+client.getClientName()+" leaved \r\n");
		clients.removeElement(client);
	}
	
	public synchronized int getDownloadPort(){
		for (int i=0; i<downloadPort.length; i++) 
			if (downloadPort[i]){
				downloadPort[i]=false;
				return SERVER_PORT+i+1; 
			}
		return -1;
	}

	public synchronized void freePort(int port) {
		downloadPort[port-SERVER_PORT-1] = true;
	}
}
