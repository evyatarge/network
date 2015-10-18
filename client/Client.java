package client;



import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import javax.swing.JButton;
import java.awt.Rectangle;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JCheckBox;
import javax.swing.JProgressBar;

public class Client {

	private JFrame frame = null;  private JPanel ContentPane = null; private JButton loginB = null; private JTextField nameTextField = null;
	private JLabel Label = null; private JLabel Label1 = null; private JTextField addressTextF = null; private JButton showOnlineB = null;
	private JButton clearB = null; private JTextArea TextArea = null; private JTextField toTextF = null; private JTextField msgTxtF = null;
	private JButton sendB = null; private CT clientT; private JCheckBox CheckBox = null; private JScrollPane scrollPane = null;       
	private JButton downloadB = null; private JLabel filePathLabel = null; private JLabel downloadPathLabel = null; private JTextField downloadPathTextField = null; private JTextField filePathTextField = null;        
        static JProgressBar ProccesBar = null; private JLabel toLabel = null; private JLabel msgTxtLabel = null;
	
        
        
	private JFrame getJFrame() {
		if (frame == null) {
			frame = new JFrame();
                        frame.setTitle("Client");
			frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
			frame.setSize(new Dimension(730, 510));
			frame.setContentPane(getJContentPane());
		}
		return frame;
	}



	private JPanel getJContentPane() {
		if (ContentPane == null) {
			Label1 = new JLabel();
			Label1.setBounds(new Rectangle(253, 9, 57, 20));
			Label1.setText("Address");
			Label = new JLabel();
			Label.setBounds(new Rectangle(95, 9, 38, 17));
			Label.setText("Name");
                        
                        downloadPathLabel = new JLabel();
			downloadPathLabel.setBounds(new Rectangle(283, 380, 130, 17));
			downloadPathLabel.setText("Save as...");
                        filePathLabel = new JLabel();
			filePathLabel.setBounds(new Rectangle(8, 380, 120, 17));
			filePathLabel.setText("File from server");
                        
                        ProccesBar = new JProgressBar();
                        ProccesBar.setBackground(java.awt.Color.yellow);
                        ProccesBar.setFont(new java.awt.Font("Tempus Sans ITC", 0, 11));
                        ProccesBar.setForeground(new java.awt.Color(0, 0, 0));
                        ProccesBar.setStringPainted(true);
                        ProccesBar.setBounds(new Rectangle(8, 440, 565, 25));
                        
                        msgTxtLabel = new JLabel();
			msgTxtLabel.setBounds(new Rectangle(139, 325, 90, 17));
			msgTxtLabel.setText("Message");
                        toLabel = new JLabel();
			toLabel.setBounds(new Rectangle(8, 325, 100, 17));
			toLabel.setText("To (empty=to all)");
                                                
			ContentPane = new JPanel(); ContentPane.setLayout(null); ContentPane.add(getLoginButton(), null); ContentPane.add(getNameTextField(), null);
                        ContentPane.add(Label, null); ContentPane.add(Label1, null); ContentPane.add(getAddressTextField(), null);
			ContentPane.add(getShowOnlineButton(), null); ContentPane.add(getClearButton(), null); ContentPane.add(getJTextArea(), null); ContentPane.add(getToTextField(), null);
			ContentPane.add(getMessageTextField(), null);ContentPane.add(getSendButton(), null);ContentPane.add(getJCheckBox(), null);
			ContentPane.add(getScrollPane(), null);ContentPane.add(getDownloadButton(), null); ContentPane.add(getDownloadPathTextField(), null);
			ContentPane.add(getFilePathTextField(), null);  ContentPane.add(ProccesBar, null);     ContentPane.add(downloadPathLabel, null);
                        ContentPane.add(filePathLabel, null);  ContentPane.add(msgTxtLabel, null);ContentPane.add(toLabel, null);
                        
			showOnlineB.setEnabled(false);toTextF.setEditable(false);msgTxtF.setEditable(false);sendB.setEnabled(false);downloadB.setEnabled(false);
			downloadPathTextField.setEditable(false);filePathTextField.setEditable(false);  ProccesBar.setEnabled(false);
		}
		return ContentPane;
	}

	
	private JButton getLoginButton() {
		if (loginB == null) {
			loginB = new JButton();
			loginB.setBounds(new Rectangle(7, 5, 76, 24));
			loginB.setText("Login");
			loginB.addActionListener(new java.awt.event.ActionListener() {
                                @Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (loginB.getText().equals("Login")){
						try{
							clientT = new CT(nameTextField.getText(), addressTextF.getText(), TextArea, CheckBox);
							clientT.start();
							loginB.setText("Logout");
							nameTextField.setEditable(false);
							addressTextF.setEditable(false);
							showOnlineB.setEnabled(true);
							toTextF.setEditable(true);
							msgTxtF.setEditable(true);
							downloadB.setEnabled(true);
							downloadPathTextField.setEditable(true);
							filePathTextField.setEditable(true);
							sendB.setEnabled(true);                                                        
						}catch (ConnectException e1){
							TextArea.append("Can't Login, Server must be on \n");
						}catch (SocketException e1){
							TextArea.append("Can't Login, wrong adress \n");
						}catch (IOException e1){
							System.out.println("IOException: " + e1.getMessage());
						}
					} else{
						loginB.setText("Login");
					        clientT.disconnect();
						nameTextField.setEditable(true);
						addressTextF.setEditable(true);
						showOnlineB.setEnabled(false);
						toTextF.setEditable(false);
						msgTxtF.setEditable(false);
						downloadB.setEnabled(false);
						downloadPathTextField.setEditable(false);
						filePathTextField.setEditable(false);
						sendB.setEnabled(false);                                               
					}
				}
			});
		}
		return loginB;
	}

	
	private JTextField getNameTextField() {
		if (nameTextField == null) {
			nameTextField = new JTextField();
			nameTextField.setBounds(new Rectangle(141, 8, 103, 22));
			nameTextField.setText("username");
		}
		return nameTextField;
	}

	
	private JTextField getAddressTextField() {
		if (addressTextF == null) {
			addressTextF = new JTextField();
			addressTextF.setBounds(new Rectangle(319, 8, 121, 23));
			addressTextF.setText("localhost");
		}
		return addressTextF;
	}

	
	private JButton getShowOnlineButton() {
		if (showOnlineB == null) {
			showOnlineB = new JButton();
			showOnlineB.setBounds(new Rectangle(458, 9, 105, 23));
			showOnlineB.setText("show online");
			showOnlineB.addActionListener(new java.awt.event.ActionListener() {
                                @Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					clientT.getUsers();
				}
			});
		}
		return showOnlineB;
	}

	
	private JButton getClearButton() {
		if (clearB == null) {
			clearB = new JButton();
			clearB.setBounds(new Rectangle(577, 8, 76, 24));
			clearB.setText("Clear");
			clearB.addActionListener(new java.awt.event.ActionListener() {
                                @Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					TextArea.setText("");
				}
			});
		}
		return clearB;
	}

	
	private JTextArea getJTextArea() {
		if (TextArea == null) {
			TextArea = new JTextArea();
			TextArea.setBounds(new Rectangle(40, 1, 8, 17));
                        TextArea.setBackground(Color.white);
		}
		return TextArea;
	}


	private JTextField getToTextField() {
		if (toTextF == null) {
			toTextF = new JTextField();
			toTextF.setBounds(new Rectangle(8, 342, 116, 25));
                        toTextF.setBackground(Color.white);
		}
		return toTextF;
	}

	
	private JTextField getMessageTextField() {
		if (msgTxtF == null) {
			msgTxtF = new JTextField();
			msgTxtF.setBounds(new Rectangle(134, 340, 437, 28));
                        msgTxtF.setBackground(Color.white);
		}
		return msgTxtF;
	}

	
	private JButton getSendButton() {
		if (sendB == null) {
			sendB = new JButton();
			sendB.setBounds(new Rectangle(587, 330, 120, 50));
			sendB.setText("Send Message");
                        sendB.setBackground(Color.white);
			sendB.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (toTextF.getText().equals(""))
						clientT.sendMsgAll(msgTxtF.getText());
					else
						clientT.sendMsg(msgTxtF.getText(), toTextF.getText());
				}
			});
		}
		return sendB;
	}
	
	
	private JCheckBox getJCheckBox() {
		if (CheckBox == null) {
			CheckBox = new JCheckBox();
			CheckBox.setBounds(new Rectangle(670, 9, 21, 21));
		}
		return CheckBox;
	}

	
	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane(TextArea);
			scrollPane.setBounds(new Rectangle(7, 36, 681, 288));
			scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		}
		return scrollPane;
	}
        private JButton getDownloadButton() {
		if (downloadB == null) {
			downloadB = new JButton();
			downloadB.setBounds(new Rectangle(588, 400, 120, 50));
			downloadB.setText("Download");
                         downloadB.setBackground(Color.white);
			downloadB.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (downloadB.getText().equals("Download")){
						clientT.downloadFile(filePathTextField.getText(), downloadPathTextField.getText(), downloadB);
						downloadB.setEnabled(false);
						downloadB.setText("Proceed");
					} else {
						clientT.continueDownload();
						downloadB.setEnabled(false);
						downloadB.setText("Download");
					}
				}
			});
		}
		return downloadB;
	}

	
	private JTextField getDownloadPathTextField() {
		if (downloadPathTextField == null) {
                       
			downloadPathTextField = new JTextField();
			downloadPathTextField.setBounds(new Rectangle(283, 400, 290, 35));
			downloadPathTextField.setText("copyfile.txt");
                        downloadPathTextField.setBackground(Color.white);
		}
		return downloadPathTextField;
	}

	
	private JTextField getFilePathTextField() {
		if (filePathTextField == null) {
                        
			filePathTextField = new JTextField();
			filePathTextField.setBounds(new Rectangle(8, 400, 268, 35));
			filePathTextField.setText("file.txt");
                        filePathTextField.setBackground(Color.white);
		}
		return filePathTextField;
	}
        
        static public void setProcessValue(int val){ProccesBar.setValue(val);}

	public static void main (String[] args){
		Client c= new Client();
		c.getJFrame().setVisible(true);
	}

}
