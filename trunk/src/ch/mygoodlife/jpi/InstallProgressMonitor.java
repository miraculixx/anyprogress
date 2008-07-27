package ch.mygoodlife.jpi;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import no.geosoft.cc.io.FileListener;
import no.geosoft.cc.io.FileMonitor;

public class InstallProgressMonitor extends JPanel implements FileListener {
	private static final long serialVersionUID = 1L;
   
	JProgressBar progressBar;
	JLabel       label;
    JTextArea    logarea;
    JScrollPane  logscroll;
    boolean      logview;
	
	static Properties   values;
	static FileMonitor monitor = new FileMonitor(1000);
		
	public InstallProgressMonitor(Dimension size, String inittext) {
		this(size, false, inittext);
	}
	
	public InstallProgressMonitor(Dimension size, boolean log, String inittext)  {
	   setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
	   setPreferredSize(size);	

	   progressBar = new JProgressBar(0, 100);
	   progressBar.setValue(0);
	   progressBar.setStringPainted(true);
	   progressBar.setAlignmentY(Component.TOP_ALIGNMENT);
	   
	   label = new JLabel(inittext);
	   label.setHorizontalTextPosition(JLabel.LEFT);
	   label.setAlignmentY(Component.TOP_ALIGNMENT);
	   
	   add(progressBar);
	   add(label);
	   
	   if(log) {
		   logview = true;
		   logarea = new JTextArea(8, 20);
	       logarea.setMargin(new Insets(5,5,5,5));
	       logarea.setEditable(false);
	       //logarea.setPreferredSize(new Dimension(this.getWidth(),100));
	       logarea.setAlignmentY(Component.TOP_ALIGNMENT);	
	       logscroll = new JScrollPane(logarea);
		   add(logscroll);
	   }
	}
	
	public void fileChanged(File file) {
		// TODO Auto-generated method stub
	 String fileName = file.toString();
	 
	 if(values.containsKey(fileName))
	 {
      String instr = (String) values.get(fileName);
      
      if(!instr.startsWith("..exit"))
      {	  
       String pct   = instr.substring(0, instr.indexOf(";"));
       progressBar.setValue(Integer.parseInt(pct));
       String text  = instr.substring(instr.indexOf(";")+1);
       label.setText(text.concat(System.getProperty("line.separator")));
       
       if(logview) {
    	   if(logarea.getText().trim().length()==0)
    		  logarea.setText(text);
    	   else 
    	      logarea.setText(logarea.getText().concat(System.getProperty("line.separator").concat(text)));   
    	   logarea.setCaretPosition(logarea.getDocument().getLength());

       }
        
      } 
      else 
       System.exit(0);
	 }
     //progressBar.setValue(progressBar.getValue()+50); 	
     System.out.println ("File changed: " + file);
	}


	protected static void readValues(String propFile)
	{
     values = new Properties();  
     try {
      values.load(new FileInputStream(propFile));
     }
     catch(IOException e) {  
       e.printStackTrace();	 
     }
	}
	
	protected static void init(String title, String inittext, Dimension size, boolean log)
	{
	  String key = null;
	  for(Enumeration<Object> e=values.keys();e.hasMoreElements();) {
		key=(String)e.nextElement();
		key.replace('/', File.separatorChar); // ensure Win32 path names
		if(key != "..init" && key != null)
	     monitor.addFile(new File(key));  
	  }		  
			
	  JFrame frame = new JFrame(title);
	  JPanel panel = new InstallProgressMonitor(size, log, inittext);

	  monitor.addListener((FileListener) panel);
	  
	  panel.setOpaque(true);
	  panel.setPreferredSize(size);
	  frame.setContentPane(panel);
	  frame.setLocationRelativeTo(null); //set to center of screen
      frame.setResizable(false);
	  frame.pack();
	  frame.setVisible(true);
	} 
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	 	
	  // read the keys from the property file provided
	  //readValues("./config/test.jpi");
	  if(args.length > 0)
	  {
	   readValues(args[0]);
	   
	   Dimension size = null;
	   String    title = "InstallProgress";
	   String    inittext = "";
	   boolean   log  = false;
	   
	   if(values.containsKey("..size")) {
	    String[] sizeStr = values.getProperty("..size").split("x");
	    size = new Dimension();
	    size.height = Integer.parseInt(sizeStr[0]);
	    size.width  = Integer.parseInt(sizeStr[1]);
	   }
	   if(values.containsKey("..title"))
	    title = (String) values.get("..title");
	   if(values.containsKey("..log"))
		log = values.get("..log").equals("yes");
	   if(values.containsKey("..init"))
		inittext = (String) values.get("..init");
	   init(title, inittext, size, log); 
	  }	  
	  else
	  {
	   System.out.println("InstallProgressMonitor (c) 2008 Patrick Senti, patrick@mygoodlife.ch ");
	   System.out.println("Licensed under GNU GPL 3");
	   System.out.println("");
	   System.out.println("Syntax: java jpi.jar config.jpi");
	  }
	}

	
}
