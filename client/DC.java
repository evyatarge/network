package client;



import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.util.StringTokenizer;

import javax.swing.JTextArea;


public class DC extends Thread{
	
	private JTextArea text; private Socket soc; private BufferedReader reader;
	private InetAddress myAddress; private boolean flag; private String downloadPath; private String fileServerPath;
	
	public DC (InetAddress address, JTextArea textArea, String port, String path, String fileServer) throws IOException{
		text=textArea;
		myAddress= address;
		downloadPath= path;
                fileServerPath=fileServer;
		soc= new Socket (myAddress, Integer.parseInt(port));
		reader = new BufferedReader(new InputStreamReader(soc.getInputStream()));
		flag=true;
	}
	
       	public void run(){
		try {
			while (flag){
				String line= reader.readLine();  
                                if(line!=null){
                                    StringTokenizer str = new StringTokenizer(line,"<>");
                                    if (str.hasMoreTokens()){
                                            String type = str.nextToken();
                                            if (type.equals("connected")){
                                                    text.append("Got a download connection \r\n");
                                            } else if (type.equals("start")){
                                                    startDownload();
                                                    text.append("End downloading!!! \r\n");
                                            }                                    
                                    }
                                }
			}
	        soc.close();
	        System.out.println("Downloading client socket close");
		} catch (IOException e) {
			
			System.out.println("IOException: " + e.getMessage());
                        text.append("Error: " + e.getMessage()+"\r\n");
		}
	}

	private void startDownload() throws IOException {
		byte [] mybytearray = new byte [6022386];
                InputStream is = soc.getInputStream();
                FileOutputStream fos;
                BufferedOutputStream bos;
            try {
                    if("".equals(downloadPath))
                        {downloadPath=(int)(Math.random()*6)+fileServerPath;}
                    fos = new FileOutputStream (downloadPath); 
                    bos = new BufferedOutputStream(fos); 

                    int bytesRead = is.read(mybytearray, 0, mybytearray.length);
                    int current = bytesRead;

                    do{
                      bytesRead =  is.read(mybytearray, current, (mybytearray.length-current)); 
                      if (bytesRead >=0) current +=bytesRead;
                    }while (bytesRead > -1);

                    bos.write(mybytearray, 0, current);
                    bos.flush();  
                    fos.close();
                    bos.close();
                    
            } catch (IOException e) {
			System.out.println("DownloadClient-IOException: " + e.getMessage());
                        flag= false;
                        text.append("Error: " + e.getMessage()+"\r\n");                       
                    
	    }
            reader.close();
            flag= false;
	}
}