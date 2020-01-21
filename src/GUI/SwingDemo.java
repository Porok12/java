package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;

public class SwingDemo {
	public static JTextArea myArea = new JTextArea();
	public static JPanel panel = new JPanel();
	private static final Logger logger = Logger.getLogger(SwingDemo.class.getName());
	private JFrame firstFrame;
	private JFrame secondFrame;
	private JSpinner groupSpinner;
	private JSpinner capacitySpinner;
	private JSpinner timeoutSpinner;
	private JSpinner clientRateSpinner;
	private JSpinner slotsSpinner;
	private volatile boolean secondFrameOpended = false;
	
	public SwingDemo() {
		buildFirstFrame();
		buildSecondFrame();
	}
	
	public boolean isSecondFrameOpened() {
		return secondFrameOpended;
	}
	
	public Integer getGroupAmount() {
		return (Integer) groupSpinner.getValue();
	}
	
	public Integer getCapacity() {
		return (Integer) capacitySpinner.getValue();
	}
	
	public Integer geTimeout() {
		return (Integer) timeoutSpinner.getValue();
	}
	
	public Integer getGenerateRate() {
		return (Integer) clientRateSpinner.getValue();
	}
	
	public Integer slots() {
		return (Integer) slotsSpinner.getValue();
	}

	private void buildFirstFrame() {
		firstFrame = new JFrame();
		
		JButton runBtn = new JButton("start");
		runBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				firstFrame.setVisible(false);
				secondFrame.setVisible(true);
				secondFrameOpended = true;
			}     
		});
		JLabel groupLabel = new JLabel("Pokoje: ");
		SpinnerNumberModel groupModel = new SpinnerNumberModel(3, 1, 9, 1);
		groupSpinner = new JSpinner(groupModel);
		
		JLabel capacityLabel = new JLabel("Pojemność: ");
		SpinnerNumberModel capacityModel = new SpinnerNumberModel(3, 1, 9, 1);
		capacitySpinner = new JSpinner(capacityModel);
		
		JLabel timeoutLabel = new JLabel("Timeout [s]: ");
		SpinnerNumberModel timeoutModel = new SpinnerNumberModel(5, 4, 15, 1);
		timeoutSpinner = new JSpinner(timeoutModel);

		JLabel clientRateLabel = new JLabel("Współczynnik generowania klientów [s]: ");
		SpinnerNumberModel clientRateModel = new SpinnerNumberModel(3, 1, 9, 1);
		clientRateSpinner = new JSpinner(clientRateModel);
		
		JLabel slotsLabel = new JLabel("Slots: ");
		SpinnerNumberModel slotsModel = new SpinnerNumberModel(4, 1, 9, 1);
		slotsSpinner = new JSpinner(slotsModel);
		
		firstFrame.add(groupLabel);
		firstFrame.add(groupSpinner);
		firstFrame.add(capacityLabel);
		firstFrame.add(capacitySpinner);
		firstFrame.add(timeoutLabel);
		firstFrame.add(timeoutSpinner);
		firstFrame.add(clientRateLabel);
		firstFrame.add(clientRateSpinner);
		firstFrame.add(slotsLabel);
		firstFrame.add(slotsSpinner);
		firstFrame.add(runBtn);
		firstFrame.add(runBtn);
		firstFrame.setSize(800, 600);
		GridLayout layout = new GridLayout(0, 2);
		layout.setHgap(10);
		layout.setVgap(10);
		firstFrame.setLayout(layout);
		firstFrame.setLocationRelativeTo(null);
		firstFrame.setVisible(true);
		firstFrame.setTitle("Programowanie w języku Java");
		firstFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		firstFrame.addWindowListener(new WindowAdapter() {
	         public void windowClosing(WindowEvent windowEvent) {
	            System.exit(0);
	         }
	      }); 
	}
	
	private void buildSecondFrame() {
		secondFrame = new JFrame();
		
		DefaultCaret caret = (DefaultCaret) myArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

	    JScrollPane scrollPane = new JScrollPane(myArea);
	    scrollPane.setBounds(5, 305, 785, 260);
	    scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	    scrollPane.setAutoscrolls(true);
	    
	    GridLayout layout = new GridLayout(0, 3);
	    layout.setHgap(10);
	    layout.setVgap(10);
	    panel.setBackground(Color.LIGHT_GRAY);
	    panel.setSize(800, 300);
	    panel.setLayout(layout);
	    
	    secondFrame.add(panel);
	    secondFrame.add(scrollPane);
	    secondFrame.setSize(800, 600);
	    GridLayout mainLayout = new GridLayout(0, 1);
	    mainLayout.setHgap(10);
	    mainLayout.setVgap(10);
	    secondFrame.setLayout(mainLayout);
	    secondFrame.setLocationRelativeTo(null);
	    secondFrame.setVisible(false);
	    secondFrame.setTitle("Programowanie w języku Java");
	    secondFrame.setResizable(true);
	    secondFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	    secondFrame.addWindowListener(new WindowAdapter() {
	         public void windowClosing(WindowEvent windowEvent) {
	            System.exit(0);
	         }
	      });   
	}
}
