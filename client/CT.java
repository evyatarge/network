package client;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTextArea;


public class CT extends Thread{

	final int SERVER_PORT = 55000;
	private JTextArea text; private Socket so;private PrintWriter writer;private BufferedReader reader;
	private JCheckBox system;private InetAddress myAddress;private JButton downloadB;private boolean flag;
	private String downloadPath;
        private String fileServerPath;  private String ClientName;


	public CT(String name, String address, JTextArea textArea, JCheckBox check) throws IOException{
		text=textArea;
		myAddress= InetAddress.getByName(address);
		so= new Socket (myAddress, SERVER_PORT);
		writer = new PrintWriter(so.getOutputStream(), true);
		reader = new BufferedReader(new InputStreamReader(so.getInputStream()));
		writer.println("<connect><" + name + ">");
                ClientName=name;
		flag=true;
		system=check;
	}
	
	public void run(){
		try {
			while (flag){
				String line= reader.readLine();
				if (system.isSelected())
					text.append("System: "+line + "\n");
				StringTokenizer str = new StringTokenizer(line, "<>");
				if (str.hasMoreTokens()){
					String type = str.nextToken();
					if (type.equals("connected")){
						text.append("got a connection \n");
					}else if (type.equals("users_lst")){
						text.append("--- online list --- \n");
						text.append(str.nextToken()+ "\n");
						text.append("--- end list  --- \n");
					}
					else if (type.equals("msg")){
						text.append(str.nextToken() + "\n");
				        } else if (type.equals("disconnected")){
						reader.close();
                                                writer.close();
                                                flag=false;
                                                Client.setProcessValue(0);
					} else if (type.equals("download_port")){
						openControlDownloadPort(str.nextToken());
					} else if (type.equals("half")){
						text.append("User " + str.nextToken() + " downloaded 50% out of file. Last byte is:" +
										str.nextToken() + ".\nConfirm proceeding\n");
						downloadB.setEnabled(true);
                                                Client.setProcessValue(50);
					} else if (type.equals("finished")){
						text.append("User " + str.nextToken() + " downloaded 100% out of file. Last byte is:" +str.nextToken() + "\n");
						downloadB.setEnabled(true);
                                                 Client.setProcessValue(100);
					} else if (type.equals("no_file")){
						text.append("File is not exist, check Server File \n");
						downloadB.setText("Download");
						downloadB.setEnabled(true);
					} else if (type.equals("download_full")){
						text.append("Download ports are full, please try later\n");
						downloadB.setText("Download");
						downloadB.setEnabled(true);
					}
				}
			} 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("IOException: " + e.getMessage());
		}
	}

	public void sendMsg(String msg, String to){
		writer.println("<set_msg><"+ to+"><" + ClientName + ": " + msg+">");
	}
	
	public void sendMsgAll(String msg){
		writer.println("<set_msg_all><" + ClientName + ": " + msg + ">");
	}
	
	public void getUsers(){
		writer.println("<get_users>");
	}
	
	public void disconnect(){
		writer.println("<disconnect>");
	}
	
	public void downloadFile(String filePath, String path, JButton button){
                fileServerPath=filePath;
		writer.println("<download><"+filePath+">");
		downloadPath = path;
		downloadB= button;
	}
	
	private void openControlDownloadPort(String port) throws NumberFormatException, IOException {
		DC doc= new DC(myAddress, text, port, downloadPath, fileServerPath);
		doc.start();
	}
	
	public void continueDownload(){
		writer.println("<proceed>");
	}
	
}
