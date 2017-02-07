/**
 * Takamasa Nakayama OS_Proj2_Round Robin Multi Queue CPU scheduler Project.
 * 
 * This class is used for main function of the App. It basically use logic of RoundRobin CPU scheduling,
 * and displays the result at the GUI&Console. Detail of the codes are writted next to each
 * lines.
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.Timer;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.SwingConstants;

public class GUI2 extends JFrame implements ActionListener{
	////////////////////////////for GUI
	private JPanel contentPane;	//place you put your GUI on
	private JScrollPane scpTable,scpOutput;	//used for scrolling output and table on GUI
	private JTable table;	//for showing table
	JTextArea txtTimeLine;	//for output in GUI
	JPanel que1,que2,que3;	//queue1,2,3 on GUI
	private Random rndProcNum,rndArrTime,rndBurstTime,rndColor;	//Random used for # of processes, Arrival Time, Burst Time and Color
	int rN=0;	//random number, used for storing Rand rndProcNum to int.
	String pcolumns[]={"ProcessID", "ArrivalTime", "BurstTime","Color"};	//columns for the processes.
	String pcolumnsToShow[]={"ProcessID", "ArrivalTime", "BurstTime"};	// columns to display on GUI
	String processValues[][];	//storing processes in 2 dimention array.
	/////////////////////////////for Gantt Chart/Queue.
	Timer time;//start counting time as soon as the startbtn is clicked.
	int sec;//counting up everytime time ticks. working as int type of time. 
	Queue<Process> q0,q1,q2, prepq,q0Temp,q1Temp,q2Temp;//preparation-queue is necessary before get into CPU queue. 3 queues for CPUs. Temps are for GUI, showing graphics.
	Process p;//Processes coming from Process class. 
	private JLabel lblSec;//label to show seconds on GUI
	Color color,colorArr[];//color for the each processes, color Array was used for storing those as a set.
	int colorcounter=1,n,x0,x1,x2;
	int arrTime=0;//used for arrival time. 
	int remainTime=0;//used for remain time.
	int q0Counter=0;
	int q1Counter=0;
	int q2Counter=0;
	int waitExchange=0;
	//for result. calculation
	JLabel lblAverageWaitingTime,lblAverageTurnaroundTime,lblAverageRespomseTime;
	int waitTotal=0;
	double avewaitTime=0;
	int respTotal=0;
	double aveRespTime=0;
	int turnAroundTotal=0;
	double aveturnAround=0;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI2 frame = new GUI2();
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
	public GUI2() {	//constructor
		n=0;x0=0;x1=0;x2=0;
		q0=new LinkedList<Process>();	//Prepare Queue0 with process coming in.
		q1=new LinkedList<Process>();	//Prepare Queue1 with process coming in.
		q2=new LinkedList<Process>();	//Prepare Queue2 with process coming in.
		q0Temp=new LinkedList<Process>();	//Prepare Queue0 to show in GUI with process coming in.
		q1Temp=new LinkedList<Process>();	//Prepare Queue1 to show in GUI with process coming in.
		q2Temp=new LinkedList<Process>();	//Prepare Queue2 to show in GUI with process coming in.
		sec=0;
		color=Color.CYAN;
		setTitle("CPSC 503 Operation Systems : Project 2 Takamasa Nakayama (0983814)");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 625, 388);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		rndProcNum=new Random();//decide number of process. more than 20, less than 25.
		rN=rndProcNum.nextInt(6)+20;//more than 20, less than 25.
		
		processValues=new String[rN][pcolumns.length];//2 dimention array,using number of processes and columns
		colorArr=new Color[rN];	//color array is in separate.
		prepq=new LinkedList<Process>();//prepq stores all processes created in the processValues into Queue. works as preparation Queue before CPU scheduling.
		
		for(int i=0;i<rN;i++){
			for(int j=0;j<pcolumns.length;j++){
				if(j==0){	//if j==0, it's first column. Process ID
					processValues[i][j]=Integer.toString(i);
				}else if(j==1){	//if j==1, it's second column. Arrival Time
					if(i==0){	//first process has to be arrived at time 0
						processValues[i][j]=Integer.toString(0);
					}else{		//after second process, arriving time can be randomized between 1-10 seconds.
						rndArrTime=new Random();
						int rA=rndArrTime.nextInt(10)+1;
						arrTime=arrTime+rA;
						processValues[i][j]=Integer.toString(arrTime);
					}
				}else if(j==2){	//if j==2, it's 3rd column. Bursting time. Also can be ranged between 1-25 seconds.
					rndBurstTime=new Random();
					int rB=rndBurstTime.nextInt(25)+1;
					processValues[i][j]=Integer.toString(rB);
				}else if(j==3){	//if j==3, it's 4th column. color. 
					rndColor=new Random();
					float r=rndColor.nextFloat();
					float g=rndColor.nextFloat();
					float b=rndColor.nextFloat();
					color=new Color(r,g,b);//randomized color will be applied.
					colorArr[i]=color;
				}
			}
			p=new Process(Integer.parseInt(processValues[i][0]),Integer.parseInt(processValues[i][1]),Integer.parseInt(processValues[i][2]),colorArr[i]);//creates process using Process constructor
			p.setProcessID(Integer.parseInt(processValues[i][0]));
			p.setArrTime(Integer.parseInt(processValues[i][1]));
			p.setBurstTime(Integer.parseInt(processValues[i][2]));
			p.setColor(colorArr[i]);
			prepq.add(p);//add into the preparation queue.
		}
		
		//tale on GUI
		table = new JTable(processValues,pcolumnsToShow);//table to show all randomly generated processes.
		scpTable=new JScrollPane(table);//adding scroll function to the table
		scpTable.setBounds(10, 11, 250, 249);
		contentPane.add(scpTable,BorderLayout.CENTER);
		
		//label to show Average Waiting Time
		lblAverageWaitingTime = new JLabel("Average Waiting Time :");
		lblAverageWaitingTime.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblAverageWaitingTime.setBounds(282, 153, 317, 23);
		contentPane.add(lblAverageWaitingTime);
		
		//label to show Average Turn around time
		lblAverageTurnaroundTime = new JLabel("Average Turnaround Time :");
		lblAverageTurnaroundTime.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblAverageTurnaroundTime.setBounds(282, 176, 317, 23);
		contentPane.add(lblAverageTurnaroundTime);
		
		//label to show average response time
		lblAverageRespomseTime = new JLabel("Average Response Time :");
		lblAverageRespomseTime.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblAverageRespomseTime.setBounds(282, 199, 317, 23);
		contentPane.add(lblAverageRespomseTime);
		
		
		time=new Timer(1000,this);//every 1 second, do some work.
		
		
		//button start
		JButton btnStart = new JButton("START");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	//start button is clicked.
				time.start();		//Timer starts.
				lblAverageRespomseTime.setText("Average Response Time : Calculating...");
				lblAverageTurnaroundTime.setText("Average Turnaround Time : Calculating...");
				lblAverageWaitingTime.setText("Average Waiting Time : Calculating...");
			}
		});
		btnStart.setBounds(381, 237, 89, 23);
		contentPane.add(btnStart);
		
		//button exit
		JButton btnExit = new JButton("EXIT");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {// if button was clicked, go back to menu
				dispose();
				GUI g1=new GUI();
				g1.setVisible(true);
			}
		});
		btnExit.setBounds(282, 237, 89, 23);
		contentPane.add(btnExit);
		
		
		//just label for Q0
		JLabel lblQ = new JLabel("Q0 :");
		lblQ.setBounds(282, 19, 46, 14);
		contentPane.add(lblQ);
		
		//just label for Q1
		JLabel lblQ_1 = new JLabel("Q1 :");
		lblQ_1.setBounds(282, 69, 46, 14);
		contentPane.add(lblQ_1);
		
		//just label for Q2
		JLabel lblQ_2 = new JLabel("Q2 :");
		lblQ_2.setBounds(282, 116, 46, 14);
		contentPane.add(lblQ_2);
		
		//queue1 on GUI
		que1 = new JPanel();
		que1.setBounds(313, 11, 286, 36);
		que1.setBackground(Color.WHITE);
		contentPane.add(que1);
		
		//queue2 on GUI
		que2 = new JPanel();
		que2.setBounds(313, 58, 286, 37);
		que2.setBackground(Color.WHITE);
		contentPane.add(que2);
		
		//queue3 on GUI
		que3 = new JPanel();
		que3.setBounds(313, 106, 286, 36);
		que3.setBackground(Color.WHITE);
		contentPane.add(que3);
		
		//Output/Console on GUI
		txtTimeLine = new JTextArea("Time\t\tDescription\n---------\t------------------------------\n");
		txtTimeLine.setFont(new Font("Monospaced", Font.PLAIN, 10));
		System.out.println("Time\t\tDescription\n---------\t------------------------------");
		
		//Scroll function for Output/Console on GUI
		scpOutput=new JScrollPane(txtTimeLine);//adding scroll function to the table
		scpOutput.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scpOutput.setBounds(10, 271, 589, 67);
		contentPane.add(scpOutput,BorderLayout.CENTER);
		
		//Label to show Seconds
		lblSec = new JLabel("0 sec");
		lblSec.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblSec.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSec.setBounds(545, 235, 54, 23);
		contentPane.add(lblSec);	
	}//end of constructor
	
	
	
	public void paint(Graphics gr){		//every time any events happen with queue, it repaints the GUI.
		super.paint(gr);
		this.setBackground(Color.WHITE);
		
		//q0 at GUI. Clear it everytime before painting.
		if(!q0Temp.isEmpty()){
			while(!q0Temp.isEmpty()){
				q0Temp.remove();
			}
		}
		
		//q1 at GUI. Clear it everytime before painting.
		if(!q1Temp.isEmpty()){
			while(!q1Temp.isEmpty()){
				q1Temp.remove();
			}
		}
		
		//q2 at GUI. Clear it everytime before painting.
		if(!q2Temp.isEmpty()){
			while(!q2Temp.isEmpty()){
				q2Temp.remove();
			}
		}
		
		//then copy q0,q1,q2 to queue for GUI
		q0Temp.addAll(q0);
		q1Temp.addAll(q1);
		q2Temp.addAll(q2);
		
		//keep paining the blocks, which represents the processes in the queue
		while(!q0Temp.isEmpty()){
				gr.setColor(q0Temp.peek().getColor());
				gr.fillRect((que1.getX()+280)+x0, que1.getY()+30,15,38);
				gr.setColor(Color.BLACK);
				gr.drawString("P"+Integer.toString(q0Temp.peek().getProcessID()), (que1.getX()+275)+x0, que1.getY()+30);
				x0-=18;
				q0Temp.remove();
		}
		while(!q1Temp.isEmpty()){
				gr.setColor(q1Temp.peek().getColor());
				gr.fillRect((que2.getX()+280)+x1, que2.getY()+30,15,38);
				gr.setColor(Color.BLACK);
				gr.drawString("P"+Integer.toString(q1Temp.peek().getProcessID()), (que2.getX()+275)+x1, que2.getY()+30);
				x1-=18;
				q1Temp.remove();
		}
		while(!q2Temp.isEmpty()){
				gr.setColor(q2Temp.peek().getColor());
				gr.fillRect((que3.getX()+280)+x2, que3.getY()+30,15,38);
				gr.setColor(Color.BLACK);
				gr.drawString("P"+Integer.toString(q2Temp.peek().getProcessID()), (que3.getX()+275)+x2, que3.getY()+30);
				x2-=18;
				q2Temp.remove();
	}
		//reset the first painting-location 
			x0=0;
			x1=0;
			x2=0;
	}

	@Override
	public void actionPerformed(ActionEvent e) {	//triggered by Timer. Every second it's triggered
		// TODO Auto-generated method stub
		lblSec.setText(sec+" sec");
		
		//if all Queues are empty, the timer stops. Then calculate the averages
		if(((prepq.isEmpty())&&(q0.isEmpty())&&(q1.isEmpty())&&(q2.isEmpty()))){	
			DecimalFormat df=new DecimalFormat("#.##");
			avewaitTime=(double)waitTotal/rN;
			aveRespTime=(double)respTotal/rN;
			aveturnAround=(double)turnAroundTotal/rN;
			lblAverageWaitingTime.setText("Average Waiting Time : "+df.format(avewaitTime)+" sec");
			lblAverageRespomseTime.setText("Average Response Time : "+df.format(aveRespTime)+" sec");
			lblAverageTurnaroundTime.setText("Average TurnAround Time : "+df.format(aveturnAround)+" sec");
			System.out.println("All processes are done. Finishing App.");
			txtTimeLine.append("All processes are done. Finishing App.\n");
			time.stop();
		}
		
		//if arriveTime has come, process is created from prepQueue and get into Q0.
		if((!prepq.isEmpty())&&(sec==prepq.peek().getArrTime())){	
			System.out.print(Integer.toString(prepq.peek().getArrTime())+" sec		P"+Integer.toString(prepq.peek().getProcessID())+" is created.  P"+Integer.toString(prepq.peek().getProcessID())+" enters Q0. ");
			txtTimeLine.append(Integer.toString(prepq.peek().getArrTime())+" sec		P"+Integer.toString(prepq.peek().getProcessID())+" is created.  P"+Integer.toString(prepq.peek().getProcessID())+" enters Q0. ");
			q0.add(prepq.remove());
			System.out.println(" P"+Integer.toString(q0.peek().getProcessID())+" is selected.");
			txtTimeLine.append(" P"+Integer.toString(q0.peek().getProcessID())+" is selected.\n");
		}

		//q2 transaction.
		//Q2 gets CPU resource when Q0 & Q1 are not not working.
		if((!q2.isEmpty())&&(q1.isEmpty())&&(q0.isEmpty())){
			if((q2Counter==12)&&(q2.peek().getRamainTime()>12)){
				q2.peek().setRamainTime(q2.peek().getRamainTime()-12);
				q2.peek().setTotalWaitTime(q2.peek().getTotalWaitTime()+(sec-12-q2.peek().getFinishedTimeQ1()));
				q2.peek().setFinishedTimeQ1(sec);
				System.out.println(Integer.toString(sec)+" sec		P"+Integer.toString(q2.peek().getProcessID())+" is aged(Q2). but continues. It's the last CPU. Remaining BurstTime is "+q2.peek().getRamainTime());
				txtTimeLine.append(Integer.toString(sec)+" sec		P"+Integer.toString(q2.peek().getProcessID())+" is aged(Q2). but continues. It's the last CPU. Remaining BurstTime is "+q2.peek().getRamainTime()+"\n");
				q2Counter=0;//reset the counter.
			}else if((q2Counter==q2.peek().getRamainTime())&&(q2.peek().getRamainTime()<=12)){
				q2.peek().setTotalWaitTime(q2.peek().getTotalWaitTime()+(sec-q2.peek().getRamainTime()-q2.peek().getFinishedTimeQ1()));
				System.out.println(sec+" sec		P"+q2.peek().getProcessID()+" is completed.(at Q2)");
				txtTimeLine.append(sec+" sec		P"+q2.peek().getProcessID()+" is completed.(at Q2)\n");
				waitTotal=waitTotal+q2.peek().getTotalWaitTime();
				q2.peek().setCompTime(sec);
				turnAroundTotal=turnAroundTotal+(q2.peek().getCompTime()-q2.peek().getArrTime());
				q2.remove();
				q2Counter=0;//reset the counter.
			}
			q2Counter++;//tick the counter when timer ticks. 
		}
		
		//q1 transaction.
		//Q1 gets CPU resource when Q0 is not not working.
		if((!q1.isEmpty())&&(q0.isEmpty())){
			if((q1Counter==8)&&(q1.peek().getRamainTime()>8)){
				q1.peek().setRamainTime(q1.peek().getRamainTime()-8);//rest of burst time is here.
				q1.peek().setTotalWaitTime(q1.peek().getTotalWaitTime()+(sec-8-q1.peek().getFinishedTimeQ0()));
				q1.peek().setFinishedTimeQ1(sec);
				System.out.println(Integer.toString(sec)+" sec		P"+Integer.toString(q1.peek().getProcessID())+" is aged(Q1).  P"+Integer.toString(q1.peek().getProcessID())+" enters Q2. Remaining BurstTime is "+q1.peek().getRamainTime());
				txtTimeLine.append(Integer.toString(sec)+" sec		P"+Integer.toString(q1.peek().getProcessID())+" is aged(Q1).  P"+Integer.toString(q1.peek().getProcessID())+" enters Q2. Remaining BurstTime is "+q1.peek().getRamainTime()+"\n");
				q2.add(q1.remove());
				q1Counter=0;//reset the counter.
			}else if((q1Counter==q1.peek().getRamainTime())&&(q1.peek().getRamainTime()<=8)){
				q1.peek().setTotalWaitTime(q1.peek().getTotalWaitTime()+(sec-q1.peek().getRamainTime()-q1.peek().getFinishedTimeQ0()));
				q1.peek().setFinishedTimeQ1(sec);
				System.out.println(sec+" sec		P"+q1.peek().getProcessID()+" is completed.(at Q1)");
				txtTimeLine.append(sec+" sec		P"+q1.peek().getProcessID()+" is completed.(at Q1)\n");
				waitTotal=waitTotal+q1.peek().getTotalWaitTime();
				q1.peek().setCompTime(sec);
				turnAroundTotal=turnAroundTotal+(q1.peek().getCompTime()-q1.peek().getArrTime());
				q1.remove();
				q1Counter=0;//reset the counter.
			}
			q1Counter++;
		}
		
		
		//q0 transaction
		//if process in q0 finishes within 4 sec 
		if((!q0.isEmpty())&&(q0.peek().getBurstTime()<=4)&&(sec==(q0.peek().getBurstTime()+q0.peek().getArrTime()+q0.peek().getWaitTime()))){
			System.out.println(sec+" sec		P"+q0.peek().getProcessID()+" is completed.(at Q0)");
			txtTimeLine.append(sec+" sec		P"+q0.peek().getProcessID()+" is completed.(at Q0)\n");
			q0.peek().setFinishedTimeQ0(sec);
			waitTotal=waitTotal+q0.peek().getTotalWaitTime();
			q0.peek().setCompTime(sec);
			turnAroundTotal=turnAroundTotal+(q0.peek().getCompTime()-q0.peek().getArrTime());
			q0.remove();
			if((!q0.isEmpty())&&(q0.peek().getArrTime()<sec)){	//check if next one has waiting time.
				q0.peek().setWaitTime(sec-(q0.peek().getArrTime()));
				q0.peek().setTotalWaitTime(q0.peek().getWaitTime());
				q0.peek().setFirstRespTime(sec);
				respTotal=respTotal+(q0.peek().getFirstRespTime()-q0.peek().getArrTime());
				System.out.println("P"+q0.peek().getProcessID()+" : Waiting Time: "+q0.peek().getWaitTime()+" seconds");
				txtTimeLine.append("P"+q0.peek().getProcessID()+" : Waiting Time: "+q0.peek().getWaitTime()+" seconds \n");
			}
		}else if((!q0.isEmpty())&&(q0.peek().getBurstTime()>4)&&(sec==(q0.peek().getArrTime()+q0.peek().getWaitTime()+4))){//if process in q0 doesnt finish in 4 sec 
			q0.peek().setRamainTime(q0.peek().getBurstTime()-4);
			System.out.println(Integer.toString(sec)+" sec		P"+Integer.toString(q0.peek().getProcessID())+" is aged(Q0).  P"+Integer.toString(q0.peek().getProcessID())+" enters Q1. Remaining BurstTime is "+q0.peek().getRamainTime());
			txtTimeLine.append(Integer.toString(sec)+" sec		P"+Integer.toString(q0.peek().getProcessID())+" is aged(Q0).  P"+Integer.toString(q0.peek().getProcessID())+" enters Q1. Remaining BurstTime is "+q0.peek().getRamainTime()+"\n");
			q0.peek().setFinishedTimeQ0(sec);
			q1.add(q0.remove());
			if((!q0.isEmpty())&&(q0.peek().getArrTime()<sec)){	//check if next one has waiting time.
				q0.peek().setWaitTime(sec-(q0.peek().getArrTime()));
				q0.peek().setTotalWaitTime(q0.peek().getWaitTime());
				q0.peek().setFirstRespTime(sec);
				respTotal=respTotal+(q0.peek().getFirstRespTime()-q0.peek().getArrTime());
				System.out.println("P"+q0.peek().getProcessID()+" : Waiting Time: "+q0.peek().getWaitTime()+" seconds");
				txtTimeLine.append("P"+q0.peek().getProcessID()+" : Waiting Time: "+q0.peek().getWaitTime()+" seconds \n");
			}
		}
		repaint();	//triggers the paint().
		scpOutput.getVerticalScrollBar().setValue(0);
		scpOutput.getViewport().scrollRectToVisible(new Rectangle(0,Integer.MAX_VALUE-1,1,1));
			sec++;//integer increments with timer.
			
	}
	
	
}//End of Class
