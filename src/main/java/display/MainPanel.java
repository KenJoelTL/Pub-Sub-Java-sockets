package display;

import main.App;

import javax.swing.*;
import java.awt.*;


public class MainPanel extends JPanel{

	private static final long serialVersionUID = 1L;	
	
	private final float LEFT_SIDE_PERCENTAGE  = 0.80f;
	private final float RIGHT_SIDE_PERCENTAGE = 0.20f;

	private int leftPanelWidth,rightPanelWidth;

	private JPanel leftPanel;
	private JPanel rightPanel;
	private JButton startBtn,stopBtn,clearBtn;
	private App app;
	private JTextArea console;
	private JScrollPane sp; 
	
	
	public MainPanel(int width, int height, App b) {
		
		this.app = b;
	
		this.leftPanel 		= new JPanel();
		this.rightPanel 	= new JPanel();
		
		this.startBtn 	= new JButton("Start");
		this.clearBtn 	= new JButton("Clear");
		this.stopBtn	= new JButton("Stop");

		this.stopBtn.setEnabled(false);
		
		this.startBtn.addActionListener(e -> {

			this.app.startServer();
			this.startBtn.setEnabled(false);
			this.stopBtn.setEnabled(true);
        });
		
		
		this.clearBtn.addActionListener(e -> {
			
			this.app.clearLog();
        });
		
		
		this.stopBtn.addActionListener(e -> {
			
			this.startBtn.setEnabled(true);
			this.stopBtn.setEnabled(false);
			this.app.stopServer();
        });
		
		
		this.console = new JTextArea(10,height);
		this.console.setEditable(false);
		this.sp = new JScrollPane(console, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		
		this.setLayout(new BorderLayout());

		
		this.setComponentSize(this,width,height);
		
		this.leftPanelWidth  = (int)(width * LEFT_SIDE_PERCENTAGE);
		this.setComponentSize(this.leftPanel,leftPanelWidth,height);
		
		
		this.console = new JTextArea(20,35);
	
		
		this.console.setEditable(false);
		this.console.setForeground(Color.BLACK);

		this.console.setLineWrap(true);
		this.console.setWrapStyleWord(true);
			
		this.sp = new JScrollPane(console, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		
		
		this.rightPanelWidth = (int)(width * RIGHT_SIDE_PERCENTAGE);
		this.setComponentSize(this.rightPanel,rightPanelWidth,height);
		
		
		//-- add components to the right panel
		this.rightPanel.add(startBtn);
		this.rightPanel.add(clearBtn);
		this.rightPanel.add(stopBtn);
		
		this.leftPanel.add(sp);
		
		this.add(leftPanel,BorderLayout.WEST);
		this.add(rightPanel,BorderLayout.EAST);
		
	}
	
	private void setComponentSize(Component comp,int width,int height) {
		
		Dimension dimension = new Dimension(width,height);
		comp.setPreferredSize(dimension);
		comp.setMaximumSize(dimension);
		comp.setMinimumSize(dimension);	
	}
	

	public void updateView(String s) {
		this.console.setText(s);

	}

}
