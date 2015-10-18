package server;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;


public class DLS extends Thread {

	private SCT sec;private String filePath;
	private ServerSocket serverSOC;private Socket clientSOC;private int portSOC; 
	boolean flag;
        boolean transferContinue=true;
	
	public DLS(String path, int port, SCT sender) {
		filePath = path;
		portSOC = port;
		sec = sender;
	}
	
        @Override
	public void run(){           
                
                 try { 
                
                serverSOC= new ServerSocket(portSOC);
    
                    while (transferContinue) {
                        try {
                            System.out.println("Waiting while client downloads ");

                            clientSOC = serverSOC.accept(); // wait for client to connect
                            System.out.println("Accepted connection: " + clientSOC);

                            File myFileWithoutPath = new File (filePath);
                            File myFile = new File (myFileWithoutPath.getAbsoluteFile().toString());
                            if (!myFile.exists()){
                                sec.fileNotExdists();
                                return;
                            }	
                            byte [] mybytearray = new byte [(int)myFile.length()];

                            FileInputStream fis = new FileInputStream(myFile);
                            BufferedInputStream bis = new BufferedInputStream(fis);
                            PrintWriter write;
                            
                            write=new PrintWriter(clientSOC.getOutputStream(), true);
                            write.println("<connected>");
                            
                               try {
                                       Thread.sleep(100);
                               } catch (InterruptedException e) {
                                       e.printStackTrace();
                               }
                             
                            write.println("<start>");

                            bis.read(mybytearray, 0, mybytearray.length);
                            byte [] part1 = Arrays.copyOfRange(mybytearray, 0 , (int)(myFile.length() /2)+1);
                            byte [] part2 = Arrays.copyOfRange(mybytearray, (int)(myFile.length() /2) , (int)(myFile.length())-1);

                            OutputStream os = clientSOC.getOutputStream();
                            System.out.println("Sending file");

                            os.write(part1, 0, part1.length);   
                            os.flush();

                            sec.halfDownload(part1[part1.length-1]);
                            flag= false;

                            while(!flag){
                                try {
                                       Thread.sleep(100);
                               } catch (InterruptedException e) {
                                       e.printStackTrace();
                               }
                            }

                            bis.read(part2, 0, part2.length);
                            os.write(part2, 0, part2.length);   
                            os.flush();

                            sec.finishDownload(part2[part2.length-1]);

                            fis.close();
                            bis.close();
                            os.close();
                            write.close();
                            serverSOC.close();


                        } catch (IOException e) {
                               System.err.println("server cant listen this port: " +e.getMessage());
                                                              
                        }
                      transferContinue = false;  
                     
                    }

                    clientSOC.close();                
                    System.out.println("Cant download now, server is closed");

                } catch (IOException e) {
                   System.out.println("IOException " + e.getMessage());                   
                }	
	}

	public void proceed() {
		flag = true;
	}

}
