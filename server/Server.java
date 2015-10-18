package server;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JTextArea;
public class Server {

	private ST s;private JFrame fram = null;private JPanel contentPane = null;
	private JButton startB = null;     
	private JTextArea TextArea = null;    

	
	private JFrame getJFrame() {
		if (fram == null) {
			fram = new JFrame();
                        fram.setTitle("Server");
			fram.setSize(new Dimension(423, 253));
			fram.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
			fram.setContentPane(getJContentPane());
			fram.addWindowListener(new java.awt.event.WindowAdapter() {
                                @Override
				public void windowClosing(java.awt.event.WindowEvent e) {
					if (startB.getText().equals("stop")) s.finish();
				}
			});
			s = new ST(TextArea);
		}
		return fram;
	}

	
	private JPanel getJContentPane() {
		if (contentPane == null) {
			contentPane = new JPanel();
			contentPane.setLayout(null);
			contentPane.add(getStartButton(), null);
			contentPane.add(getJTextArea(), null);
		}
		return contentPane;
	}

	
	private JButton getStartButton() {
		if (startB == null) {
			startB = new JButton();
			startB.setBounds(new Rectangle(7, 3, 76, 29));
			startB.setText("start");
			startB.addActionListener(new java.awt.event.ActionListener() {
                                @Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (startB.getText().equals("start")){
						startB.setText("stop");
						s= new ST(getJTextArea()); 
						s.start();
					}else{
						startB.setText("start");
						s.finish();
					}
				}
			});
		}
		return startB;
	}

	
	private JTextArea getJTextArea() {
		if (TextArea == null) {
			TextArea = new JTextArea();
			TextArea.setBounds(new Rectangle(11, 40, 386, 165));
		}
		return TextArea;
	}
	
	public static void main (String[] args){
		Server s= new Server();
		s.getJFrame().setVisible(true);
	}
	
}