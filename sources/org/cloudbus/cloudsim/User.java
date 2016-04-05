package org.cloudbus.cloudsim;

import java.text.DecimalFormat;
import java.util.*;

import org.cloudbus.cloudsim.core.CloudSim;


@SuppressWarnings("unused")
public class User {
	
	private int userid;
	
	public int hostId;
	
	protected int cloudletId;
	
	private double payment;
	
	private StringBuffer history;
	
	private DecimalFormat num;
	
	private Cloudlet cloudlet;
	
	private int numCPU;
	
	private int numRAM;
	
	private long numStorage;
	
	private double time;
	
	public void submitCloudlet(Cloudlet cloudlet){
		this.cloudlet = cloudlet;
	}
	
	public Cloudlet getCloudlet(){
		return cloudlet;
	}
	
	public void sethostId(int hostId){
		this.hostId = hostId;
	}
	
	public int gethostId(){
		return hostId;
	}
	//private list<> SOPrecords;
	
	//private ArrayList VMtimes;
	
	private String newline;
	
	
	//private final boolean record;
	
	public User(int userid/*, boolean record*/){
		this.userid = userid;
//		this.record = record;
		cloudletId = -1;
		hostId = -1;
		payment = 0.0;
	}
	
	public int getuserId(){
		return userid;
	}
	
	
	public void submitRequest(Cloudlet cloudlet, boolean i, int x, int y, long z, double time){
		//cloudlet.setFinishTime(time);
		cloudlet.setpledge(i);
		this.numCPU = x;
		this.numRAM = y;
		this.numStorage = z;
		this.time = time;
	}
	
	public int getnumCPU(){
		return numCPU;
	}
	
	public void setnumCPU(int numCPU){
		this.numCPU = numCPU;
	}
	
	public int getnumRAM(){
		return numRAM;
	}	
	
	public void setnumRAM(int numRAM){
		this.numRAM = numRAM;
	}
	
	public long getnumStorage(){
		return numStorage;
	}	
	
	public void setnumStorage(long numStorage){
		this.numStorage = numStorage;
	}
	
	public void setnumtime(double time){
		this.time = time;
	}
	
	public double gettime(){
		return time;
	}
	
	/*public double calculateSOP(){
		
	}*/
	
	public int getCloudletId(){
		return cloudletId;
	}
	
	public void setCloudletId(int CloudletId){
		this.cloudletId = CloudletId;
	}
	
	public void setPayment (double a ){
		payment = a;
	}
	
	
	public double getPayment(){
		return payment;
	}
	/*protected void write(final String str) {
		if (!record) {
			return;
		}

		if (num == null || history == null) { // Creates the history or
												// transactions of this Cloudlet
			newline = System.getProperty("line.separator");
			num = new DecimalFormat("#0.00#"); // with 3 decimal spaces
			history = new StringBuffer(1000);
			history.append("Time below denotes the simulation time.");
			history.append(System.getProperty("line.separator"));
			history.append("Time (sec)       Description User #" + userid);
			history.append(System.getProperty("line.separator"));
			history.append("------------------------------------------");
			history.append(System.getProperty("line.separator"));
			history.append(num.format(CloudSim.clock()));
			history.append("   Creates User ID #" + userid);
			history.append(System.getProperty("line.separator"));
		}

		history.append(num.format(CloudSim.clock()));
		history.append("   " + str + newline);
	}*/
}
