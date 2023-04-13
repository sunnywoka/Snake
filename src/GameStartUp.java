//Yuekai Wu 13113181

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;

public class GameStartUp extends JFrame implements ActionListener{
	JMenuItem help;
	GamePanel snake = new GamePanel();
	
	public GameStartUp(){
		super("Snake");//Set up game window frame
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(800,800);
		
		JMenuBar menubar = new JMenuBar();//Set up menu bar
		this.setJMenuBar(menubar);
		
		JMenu helpMenu = new JMenu("Player Guide");//Set up one menu button
		menubar.add(helpMenu);
		
		help = new JMenuItem("Player Guide");
		help.addActionListener(this);
		helpMenu.add(help);
		
		add(snake);//Add main game panel into frame
		this.addKeyListener(snake);
		this.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent event) {
		JComponent sourse = (JComponent)event.getSource();
		//Provide the function for player reading game rules
		if(sourse == help) {
        	JDialog information = new JDialog();
        	information.setTitle("Playing Guide");
        	information.setBounds(
        			new Rectangle(//a new dialog for displaying the rules
        					(int)this.getBounds().getX()+50,
        					(int)this.getBounds().getY()+50,
        					400,500));
        	JTextArea rules = new JTextArea();
        	information.add(rules, BorderLayout.NORTH);
        	Scanner scanner;
            try{//copying rule from README.txt
                scanner = new Scanner(new BufferedReader(new FileReader("README.txt")));
                String item;
                try{
                    while(scanner.hasNextLine()){
                        item = scanner.nextLine();
                        rules.append(String.format("%s%n", item));
                    }
                } finally {
                        scanner.close();
                    }
                } catch (IOException e){
                    e.printStackTrace();
                }
        	information.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        	information.setVisible(true);
        }
	}
	
	public static void main(String[] args) { new GameStartUp(); }//call main function
}
