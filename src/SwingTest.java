import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.logging.Logger;

import javax.swing.*;
import javax.swing.text.DefaultCaret;

public class SwingTest {
	private static final Logger logger = Logger.getLogger(SwingTest.class.getName());
	public static JTextArea myArea = new JTextArea();
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();

		JButton b = new JButton("click");
		b.setBounds(130, 100, 100, 40); 
		
//	    myArea.setAutoscrolls(true);
		DefaultCaret caret = (DefaultCaret) myArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

	    JScrollPane scrollPane = new JScrollPane(myArea);
	    scrollPane.setBounds(130, 100, 400, 200);
	    scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	    scrollPane.setAutoscrolls(true);
	    
	    
		frame.add(scrollPane);
		frame.setSize(800, 600);
		frame.setLayout(null);
		frame.setVisible(true);
		frame.setTitle("Programowanie w jêzyku Java");
		
		frame.addWindowListener(new WindowListener() {
			@Override
			public void windowClosing(WindowEvent e) {
				logger.info("exiting");
				System.exit(0);
			}
			
			@Override
			public void windowOpened(WindowEvent e) {}
			
			@Override
			public void windowIconified(WindowEvent e) {}
			
			@Override
			public void windowDeiconified(WindowEvent e) {}
			
			@Override
			public void windowDeactivated(WindowEvent e) {}
			
			@Override
			public void windowClosed(WindowEvent e) {}
			
			@Override
			public void windowActivated(WindowEvent e) {}
		});
	}
}
