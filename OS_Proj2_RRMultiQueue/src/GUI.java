/**
 * Takamasa Nakayama OS_Proj2_Round Robin Multi Queue CPU scheduler Project.
 * 
 * This class is used just for starting screen. It explains what this project is about 
 * and contains two buttons, START and EXIT 
 */
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GUI extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI frame = new GUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GUI() {
		setTitle("CPSC 503 Operation Systems : Project 2 Takamasa Nakayama (0983814)");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 625, 388);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 255, 102));
		contentPane.setForeground(Color.BLUE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnStart = new JButton("START");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {//Executed when the btnStart is pressed.
				dispose();
				//setVisible(false);
				GUI2 g2=new GUI2();
				g2.setVisible(true);
				
				
				
			}
		});
		btnStart.setBackground(Color.ORANGE);
		btnStart.setForeground(Color.BLUE);
		btnStart.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnStart.setBounds(242, 244, 133, 41);
		contentPane.add(btnStart);
		
		JLabel lblWelcomeToTakas = new JLabel("Welcome to Taka's Multi-Level Feedback Queue Scheduler");
		lblWelcomeToTakas.setForeground(new Color(51, 0, 102));
		lblWelcomeToTakas.setBackground(Color.YELLOW);
		lblWelcomeToTakas.setHorizontalAlignment(SwingConstants.CENTER);
		lblWelcomeToTakas.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 15));
		lblWelcomeToTakas.setBounds(50, 43, 506, 41);
		contentPane.add(lblWelcomeToTakas);
		
		JLabel lblThisAppDoes = new JLabel("This App does:");
		lblThisAppDoes.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblThisAppDoes.setBounds(129, 141, 119, 28);
		contentPane.add(lblThisAppDoes);
		
		JLabel lblcreatesRound = new JLabel("- creates 3 Round Robin Queues.");
		lblcreatesRound.setBounds(288, 117, 185, 14);
		contentPane.add(lblcreatesRound);
		
		JLabel lblNewLabel = new JLabel("- process goes into the queues and get executed");
		lblNewLabel.setBounds(288, 141, 289, 14);
		contentPane.add(lblNewLabel);
		
		JLabel lblComputePersormance = new JLabel("- compute performance measures");
		lblComputePersormance.setBounds(288, 166, 251, 14);
		contentPane.add(lblComputePersormance);
		
		JButton btnExit = new JButton("EXIT");
		btnExit.setBackground(Color.PINK);
		btnExit.setForeground(Color.BLUE);
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnExit.setBounds(267, 296, 89, 23);
		contentPane.add(btnExit);
	}
}
