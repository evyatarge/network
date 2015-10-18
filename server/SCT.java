package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;


public class SCT extends Thread{
	
	private Socket clientSOC;private ST server;private String name;
	private PrintWriter writer;private BufferedReader readr;private DLS dos;private boolean flag;
	private int downloadPort;

	public SCT(Socket so, ST se) {
		clientSOC=so;
		server=se;
		try {
			writer=new PrintWriter(clientSOC.getOutputStream(), true);
			readr=new BufferedReader(new InputStreamReader(clientSOC.getInputStream()));
		} catch (IOException e) {
			System.out.println("IOException: " + e.getMessage());
		}
		flag=true;
	}
	
	public String getClientName(){
		return name;
	}
	
        @Override
	public void run(){
		try {
			while (flag){
				String line= readr.readLine();
				StringTokenizer str = new StringTokenizer(line, "<>");
				String type = str.nextToken();
				if (type.equals("connect")){
					name=str.nextToken();
					writer.println("<connected>");
				}else if (type.equals("get_users"))
					writer.println("<users_lst><"+server.getAllClients()+">");
				else if (type.equals("set_msg")){
					String clientName = str.nextToken();
					String msg= str.nextToken();
					if (!server.sendMsg(clientName,msg))
						writer.println("<msg><there is no client: " + clientName + ">\n");
				} else if (type.equals("disconnect")){
					disconnect();
				} else if (type.equals("set_msg_all")){
					String msg = str.nextToken();
					server.sendMsg("",msg);
				} else if (type.equals("download")){
					String path = str.nextToken();
					int port = server.getDownloadPort();
					if (port == -1){
						writer.println("<download_full>");
					}else {
						dos = new DLS(path, port, this);
						writer.println("<download_port><"+ port +">");
						dos.start();
						downloadPort = port;
					}
				} else if (type.equals("proceed")){;
					dos.proceed();
				} 
			} 
			clientSOC.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("IOException: " + e.getMessage());
		}
	}

	public void sendMsg(String message) {
		// TODO Auto-generated method stub
		writer.println("<msg><"+message+">");
	}
	
	public void disconnect() {
		flag=false;
		writer.println("<disconnected>");
	    try {
			clientSOC.close();
			readr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("IOException: " + e.getMessage());
		}
    	        writer.close();
		server.disconnect(this);
	}

	public void halfDownload(byte b) {
		writer.println("<half><"+name+"><"+b +">");
	}

	public void finishDownload(byte b) {
		writer.println("<finished><"+ name +">"+ b +"<yyy>");
		server.freePort(downloadPort);
	}

	public void fileNotExdists() {
		writer.println("<no_file>");
	}
}
