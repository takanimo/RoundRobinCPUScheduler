/**
 * Takamasa Nakayama OS_Proj2_Round Robin Multi Queue CPU scheduler Project.
 * 
 * 1. This class is used for Process's getter&setter. 
 * 2. Each process contains initially 4 parameters, processID, arrTime, burstTime and its color.
 * 3. Later it requires total 7 more information,waitTime,ramainTime, compTime, firstRespTime,
 *    totalWaitTime, finishedTimeQ0, finishedTimeQ1. Those are used for handling multi-queues.
 */
import java.awt.Color;

public class Process {
	private int processID;
	private int arrTime;
	private int burstTime;
	private int waitTime,ramainTime,compTime,firstRespTime,totalWaitTime,finishedTimeQ0,finishedTimeQ1;//additional info. 
	private Color color;
	
	public Process(){
		processID=0;
		arrTime=0;
		burstTime=0;
		color=null;
		waitTime=0;
		burstTime=0;
		ramainTime=0;
		compTime=0;
		totalWaitTime=0;
		finishedTimeQ0=0;
		finishedTimeQ1=0;
		firstRespTime=0;
	}
	public Process(int processID,int arrTime,int burstTime,Color color){
	this.processID=processID;
	this.arrTime=arrTime;
	this.burstTime=burstTime;
	this.color=color;
	this.waitTime=0;
	this.totalWaitTime=0;
	this.finishedTimeQ0=0;
	this.finishedTimeQ1=0;
	this.firstRespTime=0;
	this.compTime=0;
	}
	public int getProcessID() {
		return processID;
	}
	public void setProcessID(int processID) {
		this.processID = processID;
	}
	public int getArrTime() {
		return arrTime;
	}
	public void setArrTime(int arrTime) {
		this.arrTime = arrTime;
	}
	
	public int getWaitTime() {
		return waitTime;
	}
	public void setWaitTime(int waitTime) {
		this.waitTime = waitTime;
	}
	public int getRamainTime() {
		return ramainTime;
	}
	public void setRamainTime(int ramainTime) {
		this.ramainTime = ramainTime;
	}
	public int getCompTime() {
		return compTime;
	}
	public void setCompTime(int compTime) {
		this.compTime = compTime;
	}
	public int getFirstRespTime() {
		return firstRespTime;
	}
	public void setFirstRespTime(int firstRespTime) {
		this.firstRespTime = firstRespTime;
	}
	public int getBurstTime() {
		return burstTime;
	}
	public void setBurstTime(int burstTime) {
		this.burstTime = burstTime;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	public int getTotalWaitTime() {
		return totalWaitTime;
	}
	public void setTotalWaitTime(int totalWaitTime) {
		this.totalWaitTime = totalWaitTime;
	}
	public int getFinishedTimeQ0() {
		return finishedTimeQ0;
	}
	public void setFinishedTimeQ0(int finishedTimeQ0) {
		this.finishedTimeQ0 = finishedTimeQ0;
	}
	public int getFinishedTimeQ1() {
		return finishedTimeQ1;
	}
	public void setFinishedTimeQ1(int finishedTimeQ1) {
		this.finishedTimeQ1 = finishedTimeQ1;
	}
}
