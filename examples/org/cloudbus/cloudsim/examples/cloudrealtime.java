package org.cloudbus.cloudsim.examples;

/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation
 *               of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009, The University of Melbourne, Australia
 */

import java.text.DecimalFormat;
import java.util.*;
import java.io.*;
import org.apache.commons.math3.distribution.ExponentialDistribution;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.User;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

/**
 * A simple example showing how to create a datacenter with one host and run one
 * cloudlet on it.
 */
public class cloudrealtime {
	
	
	
	/** The cloudlet list. */
	private static List<Cloudlet> cloudletList;

	/** The vmlist. */
	private static List<Vm> vmlist;
	
	private static List<User> userlist;
	
	private static List<Host> hostlist1;

	
	static int[] timestamp = readFile("/home/adrianhu/data/time");
	/**
	 * Creates main() to run this example.
	 *
	 * @param args the args
	 */
	
	public static void main(String[] args) {
		int[] timestamp = new int[41];
		int[] duration = new int[41];
		int[] resask = new int[41];
		int[] expiretime = new int[41];
		double[] aveRP = {0.5789900362189728,0.26751249376907243,0.45683801002938673,0.8169033070358542,0.6887823735245635, 
				0.5521070505433168,0.48104318942875435,0.8213682407628191,0.7673800917385135,0.4159151793161525, 
				0.611688723531414,0.2430319199121186,0.9305876732833652,0.3886281198542247,0.8529945049030038, 
				0.7967835025185838,0.6950044694191394,0.4769380711543552,0.6800913394271755,0.8759266195688536, 
				0.3767771347036506,0.3292976155940915,0.5067750019962286,0.7168649578463546,0.2577718508345221, 
				0.174919771501936,0.5063888546733628,0.6439396462149286,0.8413342693276544,0.08159418091775705, 
				0.5886053025776297,0.6674571021967124,0.7041954828767275,0.7091972185541121,0.6566634661241777, 
				0.6471562071336071,0.9738892259727726,0.5154452429481332,0.9308610862417835,0.3227846998717452,
				0.3549612655964895};
		timestamp[0] = 0;
		ExponentialDistribution exp = new ExponentialDistribution(3.0);
		ExponentialDistribution exp1 = new ExponentialDistribution(75.0);
		ExponentialDistribution exp2 = new ExponentialDistribution(75.0);
		resask[0] = (int)exp1.sample();
		duration[0] = (int)exp2.sample();
		for(int i = 1; i < 41; i++){
			timestamp[i] = (int)exp.sample() + 1+timestamp[i-1];
			resask[i] = (int)exp1.sample();
			duration[i] = (int)exp2.sample()+10;
		}
		
		//int[] timestamp = {0, 4, 6, 13, 15, 24, 25, 30, 32, 35, 36, 38, 39, 41, 51, 52, 53, 54, 58, 60};
		/*
		int[] duration = readFile("/home/adrianhu/data/duration");
		int[] resask = readFile("/home/adrianhu/data/resources");
		
		int[] expiretime = new int[timestamp.length];
		double[] aveRP = DreadFile("/home/adrianhu/data/ARP");*/
		int resources = 1000;
		//[0, 4, 6, 13, 15, 24, 25, 30, 32, 35, 36, 38, 39, 41, 51, 52, 53, 54, 58, 60]
		
		
		Random r = new Random();
		ArrayList<Integer> acpu = new ArrayList<Integer> ();
		ArrayList<Integer> aram = new ArrayList<Integer> ();
		ArrayList<Long> asto = new ArrayList<Long> ();
		ArrayList<Double> reimburse = new ArrayList<Double> ();
		ArrayList<Double> payment = new ArrayList<Double> ();
		ArrayList<Double> relinp = new ArrayList<Double> ();
		
		HashMap<Integer, Double> uti = new HashMap<Integer,Double>();
		TreeMap<Integer, Double> utireal = new TreeMap<Integer,Double>();
		

		
	
		Log.printLine("Starting CloudSimExample1...");
		///I removed "try"
			// First step: Initialize the CloudSim package. It should be called
			// before creating any entities.
			int num_user = 1; // number of cloud users
			Calendar calendar = Calendar.getInstance();
			boolean trace_flag = false; // mean trace events

			// Initialize the CloudSim library
			CloudSim.init(num_user, calendar, trace_flag);

			// Second step: Create Datacenters
			// Datacenters are the resource providers in CloudSim. We need at
			// list one of them to run a CloudSim simulation
			Datacenter datacenter0 = createDatacenter("Datacenter_0");
			
			//adrian
			userlist = new ArrayList<User>();
			int userid = 0;
			User user = new User(userid);

			userlist.add(user);
			
			hostlist1 = new ArrayList<Host>();

			// Third step: Create Broker
			DatacenterBroker broker = createBroker();
			int brokerId = broker.getId();
			//here I set the initial value of totalprofit as 100, which will be fixed after discussion;
			double totalProfit = 100;
			Host host = datacenter0.getHostList().get(0);
			hostlist1.add(host);
			host = hostlist1.get(0);
			host.setTotalProfit(totalProfit);
			
			broker.setHostList(hostlist1);
			broker.submitUserList(userlist);
			broker.BindUserToHost(user.getuserId(), host.getId());
			
			// Fourth step: Create one virtual machine
			vmlist = new ArrayList<Vm>();

			// Fifth step: Create one Cloudlet
			cloudletList = new ArrayList<Cloudlet>();
			
			int pesNumber = 1000; // number of cpus
			// Cloudlet properties
			int cloudletid = 0;
			long length = 400000;
			//the unit of this filesize is Byte; so I need to divide it with 1000000000;
			long fileSize = 300000000000L;
			long outputSize = 300;
			//double price = 300;
			//I will set pledge later.
			//double pledgeAmount = price * 1.05;
			UtilizationModel utilizationModel = new UtilizationModelFull();
			
			Cloudlet cloudlet = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
			cloudlet.setbrokerId(brokerId);
			cloudlet.setuser(user);
			//cloudlet.setExecStartTime(0.1);
			//cloudlet.setprice(price);
			
			user.setCloudletId(cloudlet.getCloudletId());
			
			// add the cloudlet to the list
			cloudletList.add(cloudlet);

			// submit cloudlet list to the broker
			broker.submitCloudletList(cloudletList);
			
			//adrian
			double numtime = duration[0];
			int res = resask[0];
			user.setnumtime(numtime);
			user.submitCloudlet(cloudlet);
			user.submitRequest(user.getCloudlet(), false, res, res, (long)(res), numtime);
			
			//set user's aop as not null
			host.getmtimes().put(userid, 0);
			if(aveRP[0] == 0){}
			else if(aveRP[0] > 0){
				for(int i = 0 ; i < 100; i++){
					double a = r.nextGaussian() * 0.3 + aveRP[0];
					if (a < 0 || a > 1){}
					else{
						relinp.add(a);
					}
				}
				ArrayList<Double> se = new ArrayList<Double> ();
				ArrayList<Double> de = new ArrayList<Double> ();
				host.getmaop().put(userid, se);
				host.getmsop().put(userid, de);
				for(int i = 1; i < 51; i++){
					host.addmaop(userid, relinp.get(i));
					host.addmsop(userid, relinp.get(i));
					host.addmtimes(userid);
				}
			}
			relinp.set(0,0.6128554864485367);
			
			//here I set the basic price of the task; 0.01 dollar per hour per CPU, 0.01 dollar per hour per GB of RAM, 1*0.001 dollar per hour per GB of Storage;
			//maybe I need to write a function to calculate the price of the task;
			double basicprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()*0.001);
			double pledge = basicprice * 1.05;
			
			System.out.println("user" + user.getuserId() + " asks for " + user.getnumCPU() + " CPUs and " + user.getnumRAM() + " GB of RAM and " + user.getnumStorage() + " GB of storage to host" + host.getId());
			//int num = 0;
			double payprice = 0;
			if(cloudlet.pledge == true){
				//here I didn't consider giving back the extra money if the user will not relinquish any resource;
				payprice = pledge;
			}
			else{
				int id = 1;
				//because we already know that it is the first time of the whole process, so we don't need to decide if the uti has nothing;
				if (host.getmtimes().get(userid) == 0){
					host.addmtimes(userid);
					ArrayList<Double> aaa = new ArrayList<Double> ();
					ArrayList<Double> bbb = new ArrayList<Double> ();
					//here I will set some default values for aop and sop and ...;
					
					
					if(host.getmaop().get(userid) == null){
					broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3, 0.3, id);
					//right now we don't consider the pricing model;
					//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
					host.getmaop().put(userid, bbb);
					}
					else {
						broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3, id);
						//right now we don't consider the pricing model;
						//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
					}
					host.getmsop().put(userid, aaa);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
				}
				else if(host.getmtimes().get(userid) == 1){
					host.addmtimes(userid);
					// will add codes for sop and aop and ...;
					broker.CalNumOfRes1(user.getuserId(), host.getId(), id);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
					//right now we don't consider the pricing model;
					//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
				}
				else if(host.getmtimes().get(userid) == 2){
					host.addmtimes(userid);
					// will add codes for sop and aop and ...;
					broker.CalNumOfRes2(user.getuserId(), host.getId(), id);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
					//right now we don't consider the pricing model;
					//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
				}
				else if(host.getmtimes().get(userid) > 2){
					int a = host.getmtimes().get(userid);
					broker.CalNumOfRes(user.getuserId(), host.getId(), id);
					host.addmtimes(userid);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
					//right now we don't consider the pricing model;
				}
			}
			
			int utimeh = host.getmtimes().get(userid) ;
			System.out.println("this is the " + utimeh + "th time for user" + user.getuserId() + " to use services of host" + host.getId());
			payprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()*0.001);
			acpu.add(user.getnumCPU());
			aram.add(user.getnumRAM());
			asto.add(user.getnumStorage());
			
						// VM description
						//pesNumber = user.getnumCPU();
						int vmid = 0;
						int mips = 1000;
						//long size = 500000; // image size (MB)
						//int ram = 2048; // vm memory (MB)
						long bw = 1000;
						String vmm = "Xen"; // VMM name
						// create VM
						Vm vm = new Vm(vmid, brokerId, mips, user.getnumCPU(), user.getnumRAM(), bw, user.getnumStorage(), vmm, new CloudletSchedulerTimeShared());
						vm.setHost(host);
						vmlist.add(vm);
						//int vmid2 = 1;
						//Vm vm2 = new Vm(vmid2, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
						// add the VM to the vmList
						duration[0] = (int)(duration[0]*(1-relinp.get(0)));
						//vmlist.add(vm2);
						expiretime[0] = timestamp[0] + duration[0];
						CalUtinew(timestamp[0], acpu, uti, 0.0, utireal);
						
						// submit vm list to the broker
						broker.submitVmList(vmlist);
						broker.bindCloudletToVm(cloudletid, vmid);
						
						
						
						// Calculate how much the user needs to pay.
						payprice = payprice * user.gettime();
						// Obtain the relinquish probability this time.
						double bb = relinp.get(0);
						System.out.println("the relinquish probability of the user for this time is " + bb);
						//although the unit of RAM and Storage is MB is the default configuration, but here I still use GB because of Amazon EC2's pricing model;
						System.out.println("host" + host.getId() + " will give " + user.getnumCPU() + " CPUs and " +user.getnumRAM()+ " GB of RAM and " + user.getnumStorage() + " GB of storage to user" + userid + " to finish his cloudlet " + cloudlet.getCloudletId());
						System.out.println("the user will pay " + payprice + " dollars to the broker.");
						// Calculate how much should the broker reimburse to the user
						double reimbursement = 0;
						reimbursement = payprice * bb;
						// Update the records of reimbursement and payment.
						reimburse.add(reimbursement);
						payment.add(payprice);
						
						
						
						
						
						System.out.println(host.getmtimes().get(userid));
			// Sixth step: Starts the simulation
			CloudSim.startSimulation();
			
			
			///////////////////////////////
			//////////////////////////////
			
			
			
			
			
			CloudSim.init(num_user, calendar, trace_flag);

			@SuppressWarnings("unused")
			Datacenter datacenter1 = createDatacenter("Datacenter_1");

			DatacenterBroker broker1 = createBroker();
			int broker1Id = broker1.getId();
			
			//update totalProfit;
			totalProfit += payprice;
			userid = 1;
			User user1 = new User(userid);
			userlist.add(user1);
			broker1.submitUserList(userlist);
			broker1.BindUserToHost(user1.getuserId(), host.getId());
			
			host.setTotalProfit(totalProfit);
			
			broker1.setHostList(hostlist1);
			
			cloudletid = 1;
			Cloudlet cloudlet1 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
			cloudlet1.setbrokerId(broker1Id);
			//update the price of cloudlet;
			//price = 200;
			//cloudlet1.setprice(price);
			cloudlet1.setuser(user1);
			user1.setCloudletId(cloudlet1.getCloudletId());
			
			// add the cloudlet to the list
			cloudletList.add(cloudlet1);

			// submit cloudlet list to the broker
			broker1.submitCloudletList(cloudletList);
			
			//adrian
			numtime = duration[1];
			res = resask[1];
			user1.submitCloudlet(cloudlet1);
			user1.submitRequest(user1.getCloudlet(), false, res, res, (long)(res), numtime);
			
			relinp.clear();
			host.getmtimes().put(userid, 0);
			if(aveRP[1] == 0){}
			else if(aveRP[1] > 0){
				for(int i = 0 ; i < 100; i++){
					double a = r.nextGaussian() * 0.3 + aveRP[1];
					if (a < 0 || a > 1){}
					else{
						relinp.add(a);
					}
				}
				ArrayList<Double> se1 = new ArrayList<Double> ();
				ArrayList<Double> de1 = new ArrayList<Double> ();
				host.getmaop().put(userid, se1);
				host.getmsop().put(userid, de1);
				for(int i = 1; i < 51; i++){
					host.addmaop(userid, relinp.get(i));
					host.addmsop(userid, relinp.get(i));
					host.addmtimes(userid);
				}
			}
			relinp.set(0,0.4585833354187909);
			basicprice = user1.getnumCPU() * 0.01 + user1.getnumRAM() * 0.01 + (double)(user1.getnumStorage()*0.001);
			pledge = basicprice * 1.05;
			System.out.println("user1" + user1.getuserId() + " asks for " + user1.getnumCPU() + " CPUs and " + user1.getnumRAM() + " GB of RAM and " + user1.getnumStorage() + " GB of storage to host" + host.getId());
			
			double utiliz = CalUti(timestamp[1], acpu, uti, expiretime, utireal);
			int id = 0;
			
			if(cloudlet1.pledge == true){
				//here I didn't consider giving back the extra money if the user will not relinquish any resource;
				payprice = pledge;
			}
			else{
				
				id = DecideModel(utiliz, aveRP[1], userid, res);
				id = 1;
				//host.minusmtimes(userid);
				//here remember the BROKER is changing to BROKER1!!!!!!!!! and cloudlet is cloudlet1!!!!!!!!!
				if (host.getmtimes().get(userid) == 0){
					host.addmtimes(userid);
					ArrayList<Double> aaa = new ArrayList<Double> ();
					ArrayList<Double> bbb = new ArrayList<Double> ();
					//here I will set some default values for aop and sop and ...;
					if(host.getmaop().get(userid) == null){
					broker1.CalNumOfRes(user1.getuserId(), host.getId(), 0.3, 0.3, id);
					//right now we don't consider the pricing model;
					//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
					host.getmaop().put(userid, bbb);
					}
					else {
						broker1.CalNumOfRes(user1.getuserId(), host.getId(), 0.3, id);
						//right now we don't consider the pricing model;
						//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
					}
					host.getmsop().put(userid, aaa);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
				}
				else if(host.getmtimes().get(userid) == 1){
					host.addmtimes(userid);
					// will add codes for sop and aop and ...;
					broker1.CalNumOfRes1(user1.getuserId(), host.getId(), id);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
					//payprice = broker1.CalPrice(cloudlet1.getCloudletId(), host.getId());
				}
				else if(host.getmtimes().get(userid) == 2){
					host.addmtimes(userid);
					// will add codes for sop and aop and ...;
					broker1.CalNumOfRes2(user1.getuserId(), host.getId(), id);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
					//payprice = broker1.CalPrice(cloudlet1.getCloudletId(), host.getId());
				}
				else if(host.getmtimes().get(userid) > 2){
					broker1.CalNumOfRes(user1.getuserId(), host.getId(), id);
					//int a = host.getmtimes().get(userid);
					host.addmtimes(userid);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
					
				}
			}

			utimeh = host.getmtimes().get(userid) ;
			System.out.println("this is the " + utimeh + "th time for user" + user1.getuserId() + " to use services of host" + host.getId());
			payprice = user1.getnumCPU() * 0.01 + user1.getnumRAM() * 0.01 + (double)(user1.getnumStorage()*0.001);
			vmid = 1;
			acpu.add(user1.getnumCPU());
			aram.add(user1.getnumRAM());
			asto.add(user1.getnumStorage());
			Vm vm1 = new Vm(vmid, broker1.getId(), mips, user1.getnumCPU(), user1.getnumRAM(), bw, user1.getnumStorage(), vmm, new CloudletSchedulerTimeShared());
			
			vm1.setHost(host);
			
			//int vmid2 = 1;
			//Vm vm2 = new Vm(vmid2, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
			// add the VM to the vmList
			vmlist.add(vm1);
			//vmlist.add(vm2);
			duration[1] = (int)(duration[1]*(1-relinp.get(0)));
			expiretime[1] = timestamp[1] + duration[1];
			CalUtinew(timestamp[1], acpu, uti, utiliz, utireal);
			// submit vm list to the broker
			broker1.submitVmList(vmlist);
			
			broker1.bindCloudletToVm(cloudletid, vmid);
			
			payprice = payprice * user1.gettime();

			
			bb = relinp.get(0);
			System.out.println("the relinquish probability of the user for this time is " + bb);
			//System.out.println(bb);

			System.out.println("host " + host.getId() + " will give " + user1.getnumCPU() + " CPUs and " +user1.getnumRAM()+ " GB of RAM and " + user1.getnumStorage() + " GB of storage to user" + userid + " to finish his cloudlet " + cloudlet1.getCloudletId());
			System.out.println("the user will pay " + payprice + " dollars to the broker.");
			
			System.out.println(host.getmtimes().get(userid));
			reimbursement = payprice * bb;
			// Update the records of reimbursement and payment.
			reimburse.add(reimbursement);
			payment.add(payprice);
			CloudSim.startSimulation();
			
			///////////////////////////////////
			///////////////////////////////////
			//////////////////////////////////
			//////////////////////////////////
			//the third time: 
			/////////////////////////////////////
			////////////////////////////////////
			
			
			
			CloudSim.init(num_user, calendar, trace_flag);

			@SuppressWarnings("unused")
			Datacenter datacenter2 = createDatacenter("Datacenter_1");

			DatacenterBroker broker2 = createBroker();
			int broker2Id = broker2.getId();
			
			//update totalProfit;
			totalProfit += payprice;
			userid = 2;
			User user2 = new User(userid);
			userlist.add(user2);
			
			broker2.submitUserList(userlist);
			broker2.BindUserToHost(user2.getuserId(), host.getId());
			
			host.setTotalProfit(totalProfit);
			
			broker2.setHostList(hostlist1);
			
			cloudletid = 2;
			Cloudlet cloudlet2 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
			cloudlet2.setbrokerId(broker2Id);
			//update the price of cloudlet;
			//price = 200;
			//cloudlet2.setprice(price);
			cloudlet2.setuser(user2);
			user2.setCloudletId(cloudlet2.getCloudletId());
			
			// add the cloudlet to the list
			cloudletList.add(cloudlet2);

			// submit cloudlet list to the broker
			broker2.submitCloudletList(cloudletList);
			
			//adrian
			numtime = duration[2];
			res = resask[2];
			user2.submitCloudlet(cloudlet2);
			user2.submitRequest(user2.getCloudlet(), false, res, res, (long)(res), numtime);
			relinp.clear();
			host.getmtimes().put(userid, 0);
			for(int i = 0 ; i < 100; i++){
				double a = r.nextGaussian() * 0.3 + aveRP[2];
				if (a < 0 || a > 1){}
				else{
					relinp.add(a);
				}
			}
			if(aveRP[2] == 0){
				
			}
			else if(aveRP[2] > 0){
				
				ArrayList<Double> se2 = new ArrayList<Double> ();
				ArrayList<Double> de2 = new ArrayList<Double> ();
				host.getmaop().put(userid, se2);
				host.getmsop().put(userid, de2);
				for(int i = 1; i < 51; i++){
					host.addmaop(userid, relinp.get(i));
					host.addmsop(userid, relinp.get(i));
					host.addmtimes(userid);
				}
			}
			relinp.set(0,0.2887622931588689);
			basicprice = user2.getnumCPU() * 0.01 + user2.getnumRAM() * 0.01 + (double)(user2.getnumStorage()*0.001);
			pledge = basicprice * 1.05;
			System.out.println("user" + user2.getuserId() + " asks for " + user2.getnumCPU() + " CPUs and " + user2.getnumRAM() + " GB of RAM and " + user2.getnumStorage() + " GB of storage to host" + host.getId());
			utiliz = CalUti(timestamp[2], acpu, uti, expiretime, utireal);
			id = 0;
			
			if(cloudlet2.pledge == true){
				//here I didn't consider giving back the extra money if the user will not relinquish any resource;
				payprice = pledge;
			}
			else{
				
				id = DecideModel(utiliz, aveRP[2], userid, res);
				id = 1;
				//host.minusmtimes(userid);
				//here remember the BROKER is changing to BROKER2!!!!!!!!! and cloudlet is cloudlet2!!!!!!!!!
				if (host.getmtimes().get(userid) == 0){
					host.addmtimes(userid);
					ArrayList<Double> aaa = new ArrayList<Double> ();
					ArrayList<Double> bbb = new ArrayList<Double> ();
					//here I will set some default values for aop and sop and ...;
					if(host.getmaop().get(userid) == null){
					broker2.CalNumOfRes(user2.getuserId(), host.getId(), 0.3, 0.3, id);
					//right now we don't consider the pricing model;
					//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
					host.getmaop().put(userid, bbb);
					}
					else {
						broker2.CalNumOfRes(user2.getuserId(), host.getId(), 0.3, id);
						//right now we don't consider the pricing model;
						//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
					}
					host.getmsop().put(userid, aaa);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
				}
				else if(host.getmtimes().get(userid) == 1){
					host.addmtimes(userid);
					// will add codes for sop and aop and ...;
					broker2.CalNumOfRes1(user2.getuserId(), host.getId(), id);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
					//payprice = broker2.CalPrice(cloudlet2.getCloudletId(), host.getId());
				}
				else if(host.getmtimes().get(userid) == 2){
					host.addmtimes(userid);
					// will add codes for sop and aop and ...;
					broker2.CalNumOfRes2(user2.getuserId(), host.getId(), id);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
					//payprice = broker2.CalPrice(cloudlet2.getCloudletId(), host.getId());
				}
				else if(host.getmtimes().get(userid) > 2){
					broker2.CalNumOfRes(user2.getuserId(), host.getId(), id);
					int a = host.getmtimes().get(userid);
					host.addmtimes(userid);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
					
				}
			}

			
			acpu.add(user2.getnumCPU());
			aram.add(user2.getnumRAM());
			asto.add(user2.getnumStorage());
			utimeh = host.getmtimes().get(userid) ;
			System.out.println("this is the " + utimeh + "th time for user" + user2.getuserId() + " to use services of host" + host.getId());
			payprice = user2.getnumCPU() * 0.01 + user2.getnumRAM() * 0.01 + (double)(user2.getnumStorage()*0.001);
			vmid = 2;
			
			Vm vm2 = new Vm(vmid, broker2Id, mips, user2.getnumCPU(), user2.getnumRAM(), bw, user2.getnumStorage(), vmm, new CloudletSchedulerTimeShared());
			
			vm2.setHost(host);
			
			//int vmid2 = 1;
			//Vm vm2 = new Vm(vmid2, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
			// add the VM to the vmList
			vmlist.add(vm2);
			duration[2] = (int)(duration[2]*(1-relinp.get(0)));
			//vmlist.add(vm2);
			expiretime[2] = timestamp[2] + duration[2];
			CalUtinew(timestamp[2], acpu, uti, utiliz, utireal);
			// submit vm list to the broker
			broker2.submitVmList(vmlist);
			
			broker2.bindCloudletToVm(cloudletid, vmid);
			
			payprice = payprice * user2.gettime();
			
			reimbursement = payprice * bb;
			// Update the records of reimbursement and payment.
			reimburse.add(reimbursement);
			System.out.println(host.getmtimes().get(userid));
			payment.add(payprice);
			
			
			
			
			CloudSim.startSimulation();
			
			
/////////////////////////////////////
////////////////////////////////////
			
			CloudSim.init(num_user, calendar, trace_flag);

			@SuppressWarnings("unused")
			Datacenter datacenter3 = createDatacenter("Datacenter_3");

			DatacenterBroker broker3 = createBroker();
			int broker3Id = broker3.getId();
			
			//update totalProfit;
			totalProfit += payprice;
			userid = 3;
			User user3 = new User(userid);
			userlist.add(user3);
			
			broker3.submitUserList(userlist);
			broker3.BindUserToHost(user3.getuserId(), host.getId());
			
			host.setTotalProfit(totalProfit);
			
			broker3.setHostList(hostlist1);
			
			cloudletid = 3;
			Cloudlet cloudlet3 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
			cloudlet3.setbrokerId(broker3Id);
			//update the price of cloudlet;
			//price = 200;
			//cloudlet2.setprice(price);
			cloudlet3.setuser(user3);
			user3.setCloudletId(cloudlet3.getCloudletId());
			
			// add the cloudlet to the list
			cloudletList.add(cloudlet3);

			// submit cloudlet list to the broker
			broker3.submitCloudletList(cloudletList);
			
			//adrian
			numtime = duration[3];
			res = resask[3];
			user3.submitCloudlet(cloudlet3);
			user3.submitRequest(user3.getCloudlet(), false, res, res, (long)(res), numtime);
			relinp.clear();
			host.getmtimes().put(userid, 0);
			for(int i = 0 ; i < 100; i++){
				double a = r.nextGaussian() * 0.3 + aveRP[3];
				if (a < 0 || a > 1){}
				else{
					relinp.add(a);
				}
			}
			if(aveRP[3] == 0){}
			else if(aveRP[3] > 0){
				
				ArrayList<Double> se3 = new ArrayList<Double> ();
				ArrayList<Double> de3 = new ArrayList<Double> ();
				host.getmaop().put(userid, se3);
				host.getmsop().put(userid, de3);
				for(int i = 1; i < 51; i++){
					host.addmaop(userid, relinp.get(i));
					host.addmsop(userid, relinp.get(i));
					host.addmtimes(userid);
				}
			}
			relinp.set(0, 0.2979604444598067);
			basicprice = user3.getnumCPU() * 0.01 + user3.getnumRAM() * 0.01 + (double)(user3.getnumStorage()*0.001);
			pledge = basicprice * 1.05;
			System.out.println("user" + user3.getuserId() + " asks for " + user3.getnumCPU() + " CPUs and " + user3.getnumRAM() + " GB of RAM and " + user3.getnumStorage() + " GB of storage to host" + host.getId());

			utiliz = CalUti(timestamp[3], acpu, uti, expiretime, utireal);
			id = 0;
			if(cloudlet3.pledge == true){
				//here I didn't consider giving back the extra money if the user will not relinquish any resource;
				payprice = pledge;
			}
			else{
				
				id = DecideModel(utiliz, aveRP[3], userid, res);
				id = 1;
				//host.minusmtimes(userid);
				//here remember the BROKER is changing to BROKER2!!!!!!!!! and cloudlet is cloudlet2!!!!!!!!!
				if (host.getmtimes().get(userid) == 0){
					host.addmtimes(userid);
					ArrayList<Double> aaa = new ArrayList<Double> ();
					ArrayList<Double> bbb = new ArrayList<Double> ();
					//here I will set some default values for aop and sop and ...;
					if(host.getmaop().get(userid) == null){
					broker3.CalNumOfRes(user3.getuserId(), host.getId(), 0.3, 0.3, id);
					//right now we don't consider the pricing model;
					//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
					host.getmaop().put(userid, bbb);
					}
					else {
						broker3.CalNumOfRes(user3.getuserId(), host.getId(), 0.3, id);
						//right now we don't consider the pricing model;
						//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
					}
					host.getmsop().put(userid, aaa);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
				}
				else if(host.getmtimes().get(userid) == 1){
					host.addmtimes(userid);
					// will add codes for sop and aop and ...;
					broker3.CalNumOfRes1(user3.getuserId(), host.getId(), id);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
					//payprice = broker3.CalPrice(cloudlet3.getCloudletId(), host.getId());
				}
				else if(host.getmtimes().get(userid) == 2){
					host.addmtimes(userid);
					// will add codes for sop and aop and ...;
					broker3.CalNumOfRes2(user3.getuserId(), host.getId(), id);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
					//payprice = broker3.CalPrice(cloudlet3.getCloudletId(), host.getId());
				}
				else if(host.getmtimes().get(userid) > 2){
					broker3.CalNumOfRes(user3.getuserId(), host.getId(), id);
					int a = host.getmtimes().get(userid);
					host.addmtimes(userid);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
					
				}
			}

			
			acpu.add(user3.getnumCPU());
			aram.add(user3.getnumRAM());
			asto.add(user3.getnumStorage());
			utimeh = host.getmtimes().get(userid) ;
			System.out.println("this is the " + utimeh + "th time for user" + user3.getuserId() + " to use services of host" + host.getId());
			payprice = user3.getnumCPU() * 0.01 + user3.getnumRAM() * 0.01 + (double)(user3.getnumStorage()*0.001);
			vmid = 3;
			
			Vm vm3 = new Vm(vmid, broker3Id, mips, user3.getnumCPU(), user3.getnumRAM(), bw, user3.getnumStorage(), vmm, new CloudletSchedulerTimeShared());
			
			vm3.setHost(host);
			
			//int vmid3 = 1;
			//Vm vm3 = new Vm(vmid3, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
			// add the VM to the vmList
			vmlist.add(vm3);
			//vmlist.add(vm2);
			duration[3] = (int)(duration[3]*(1-relinp.get(0)));
			expiretime[3] = timestamp[3] + duration[3];
			CalUtinew(timestamp[3], acpu, uti, utiliz, utireal);
			// submit vm list to the broker
			broker3.submitVmList(vmlist);
			
			broker3.bindCloudletToVm(cloudletid, vmid);
			
			payprice = payprice * user3.gettime();
			
			reimbursement = payprice * bb;
			// Update the records of reimbursement and payment.
			reimburse.add(reimbursement);
			System.out.println(host.getmtimes().get(userid));
			payment.add(payprice);
			
			
			
			
			CloudSim.startSimulation();
			////////////////////////////////////
			/////////////////////////////////////
			//////////////////////////////////////
			///////////////////////////////////////
			
			
			CloudSim.init(num_user, calendar, trace_flag);

			@SuppressWarnings("unused")
			Datacenter datacenter4 = createDatacenter("Datacenter_1");

			DatacenterBroker broker4 = createBroker();
			int broker4Id = broker4.getId();
			
			//update totalProfit;
			totalProfit += payprice;
			userid = 4;
			User user4 = new User(userid);
			userlist.add(user4);
			
			broker4.submitUserList(userlist);
			broker4.BindUserToHost(user4.getuserId(), host.getId());
			
			host.setTotalProfit(totalProfit);
			
			broker4.setHostList(hostlist1);
			
			cloudletid = 4;
			Cloudlet cloudlet4 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
			cloudlet4.setbrokerId(broker4Id);
			//update the price of cloudlet;
			//price = 200;
			//cloudlet2.setprice(price);
			cloudlet4.setuser(user4);
			user4.setCloudletId(cloudlet4.getCloudletId());
			
			// add the cloudlet to the list
			cloudletList.add(cloudlet4);

			// submit cloudlet list to the broker
			broker4.submitCloudletList(cloudletList);
			
			//adrian
			numtime = duration[4];
			res = resask[4];
			user4.submitCloudlet(cloudlet4);
			user4.submitRequest(user4.getCloudlet(), false, res, res, (long)(res), numtime);
			relinp.clear();
			host.getmtimes().put(userid, 0);
			for(int i = 0 ; i < 100; i++){
				double a = r.nextGaussian() * 0.3 + aveRP[4];
				if (a < 0 || a > 1){}
				else{
					relinp.add(a);
				}
			}
			if(aveRP[4] == 0){}
			else if(aveRP[4] > 0){
				
				ArrayList<Double> se4 = new ArrayList<Double> ();
				ArrayList<Double> de4 = new ArrayList<Double> ();
				host.getmaop().put(userid, se4);
				host.getmsop().put(userid, de4);
				for(int i = 1; i < 51; i++){
					host.addmaop(userid, relinp.get(i));
					host.addmsop(userid, relinp.get(i));
					host.addmtimes(userid);
				}
			}
			relinp.set(0,0.3232544084963322);
			basicprice = user4.getnumCPU() * 0.01 + user4.getnumRAM() * 0.01 + (double)(user4.getnumStorage()*0.001);
			pledge = basicprice * 1.05;
			System.out.println("user" + user4.getuserId() + " asks for " + user4.getnumCPU() + " CPUs and " + user4.getnumRAM() + " GB of RAM and " + user4.getnumStorage() + " GB of storage to host" + host.getId());
			utiliz = CalUti(timestamp[4], acpu, uti, expiretime, utireal);
			id = 0;
			if(cloudlet4.pledge == true){
				//here I didn't consider giving back the extra money if the user will not relinquish any resource;
				payprice = pledge;
			}
			else{
				id = DecideModel(utiliz, aveRP[4], userid, res);
				id = 1;
				//host.minusmtimes(userid);
				//here remember the BROKER is changing to BROKER2!!!!!!!!! and cloudlet is cloudlet2!!!!!!!!!
				if (host.getmtimes().get(userid) == 0){
					host.addmtimes(userid);
					ArrayList<Double> aaa = new ArrayList<Double> ();
					ArrayList<Double> bbb = new ArrayList<Double> ();
					//here I will set some default values for aop and sop and ...;
					if(host.getmaop().get(userid) == null){
					broker4.CalNumOfRes(user4.getuserId(), host.getId(), 0.3, 0.3, id);
					//right now we don't consider the pricing model;
					//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
					host.getmaop().put(userid, bbb);
					}
					else {
						broker4.CalNumOfRes(user4.getuserId(), host.getId(), 0.3, id);
						//right now we don't consider the pricing model;
						//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
					}
					host.getmsop().put(userid, aaa);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
				}
				else if(host.getmtimes().get(userid) == 1){
					host.addmtimes(userid);
					// will add codes for sop and aop and ...;
					broker4.CalNumOfRes1(user4.getuserId(), host.getId(), id);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
					//payprice = broker2.CalPrice(cloudlet2.getCloudletId(), host.getId());
				}
				else if(host.getmtimes().get(userid) == 2){
					host.addmtimes(userid);
					// will add codes for sop and aop and ...;
					broker4.CalNumOfRes2(user4.getuserId(), host.getId(), id);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
					//payprice = broker2.CalPrice(cloudlet2.getCloudletId(), host.getId());
				}
				else if(host.getmtimes().get(userid) > 2){
					broker4.CalNumOfRes(user4.getuserId(), host.getId(), id);
					int a = host.getmtimes().get(userid);
					host.addmtimes(userid);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
					
				}
			}

			
			acpu.add(user4.getnumCPU());
			aram.add(user4.getnumRAM());
			asto.add(user4.getnumStorage());
			utimeh = host.getmtimes().get(userid) ;
			System.out.println("this is the " + utimeh + "th time for user" + user4.getuserId() + " to use services of host" + host.getId());
			payprice = user4.getnumCPU() * 0.01 + user4.getnumRAM() * 0.01 + (double)(user4.getnumStorage()*0.001);
			vmid = 4;
			
			Vm vm4 = new Vm(vmid, broker4Id, mips, user4.getnumCPU(), user4.getnumRAM(), bw, user4.getnumStorage(), vmm, new CloudletSchedulerTimeShared());
			
			vm4.setHost(host);
			
			//int vmid2 = 1;
			//Vm vm2 = new Vm(vmid2, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
			// add the VM to the vmList
			vmlist.add(vm4);
			//vmlist.add(vm2);
			duration[4] = (int)(duration[4]*(1-relinp.get(0)));
			expiretime[4] = timestamp[4] + duration[4];
			CalUtinew(timestamp[4], acpu, uti, utiliz, utireal);
			// submit vm list to the broker
			broker4.submitVmList(vmlist);
			
			broker4.bindCloudletToVm(cloudletid, vmid);
			
			payprice = payprice * user4.gettime();
			
			reimbursement = payprice * bb;
			// Update the records of reimbursement and payment.
			reimburse.add(reimbursement);
			System.out.println(host.getmtimes().get(userid));
			payment.add(payprice);
			
			
			
			
			CloudSim.startSimulation();
			//////////////////////////////////////////
			////////////////////////////////////////////
			//////////////////////////////////////
			////////////////////////////////////////
			CloudSim.init(num_user, calendar, trace_flag);

			@SuppressWarnings("unused")
			Datacenter datacenter5 = createDatacenter("Datacenter_1");

			DatacenterBroker broker5 = createBroker();
			int broker5Id = broker5.getId();
			
			//update totalProfit;
			totalProfit += payprice;
			userid = 5;
			User user5 = new User(userid);
			userlist.add(user5);
			
			broker5.submitUserList(userlist);
			broker5.BindUserToHost(user5.getuserId(), host.getId());
			
			host.setTotalProfit(totalProfit);
			
			broker5.setHostList(hostlist1);
			
			cloudletid = 5;
			Cloudlet cloudlet5 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
			cloudlet5.setbrokerId(broker5Id);
			//update the price of cloudlet;
			//price = 200;
			//cloudlet2.setprice(price);
			cloudlet5.setuser(user5);
			user5.setCloudletId(cloudlet5.getCloudletId());
			
			// add the cloudlet to the list
			cloudletList.add(cloudlet5);

			// submit cloudlet list to the broker
			broker5.submitCloudletList(cloudletList);
			
			//adrian
			numtime = duration[5];
			res = resask[5];
			user5.submitCloudlet(cloudlet5);
			user5.submitRequest(user5.getCloudlet(), false, res, res, (long)(res), numtime);
			relinp.clear();
			host.getmtimes().put(userid, 0);
			for(int i = 0 ; i < 100; i++){
				double a = r.nextGaussian() * 0.3 + aveRP[5];
				if (a < 0 || a > 1){}
				else{
					relinp.add(a);
				}
			}
			if(aveRP[5] == 0){}
			else if(aveRP[5] > 0){
				
				ArrayList<Double> se5 = new ArrayList<Double> ();
				ArrayList<Double> de5 = new ArrayList<Double> ();
				host.getmaop().put(userid, se5);
				host.getmsop().put(userid, de5);
				for(int i = 1; i < 51; i++){
					host.addmaop(userid, relinp.get(i));
					host.addmsop(userid, relinp.get(i));
					host.addmtimes(userid);
				}
			}
			relinp.set(0,0.41384016286181785);
			basicprice = user5.getnumCPU() * 0.01 + user5.getnumRAM() * 0.01 + (double)(user5.getnumStorage()*0.001);
			pledge = basicprice * 1.05;
			System.out.println("user" + user5.getuserId() + " asks for " + user5.getnumCPU() + " CPUs and " + user5.getnumRAM() + " GB of RAM and " + user5.getnumStorage() + " GB of storage to host" + host.getId());
			utiliz = CalUti(timestamp[5], acpu, uti, expiretime, utireal);
			id = 0;
			if(cloudlet5.pledge == true){
				//here I didn't consider giving back the extra money if the user will not relinquish any resource;
				payprice = pledge;
			}
			else{
				id = DecideModel(utiliz, aveRP[5], userid, res);
				id = 1;
				//host.minusmtimes(userid);
				//here remember the BROKER is changing to BROKER2!!!!!!!!! and cloudlet is cloudlet2!!!!!!!!!
				if (host.getmtimes().get(userid) == 0){
					host.addmtimes(userid);
					ArrayList<Double> aaa = new ArrayList<Double> ();
					ArrayList<Double> bbb = new ArrayList<Double> ();
					//here I will set some default values for aop and sop and ...;
					if(host.getmaop().get(userid) == null){
					broker5.CalNumOfRes(user5.getuserId(), host.getId(), 0.3, 0.3, id);
					//right now we don't consider the pricing model;
					//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
					host.getmaop().put(userid, bbb);
					}
					else {
						broker5.CalNumOfRes(user5.getuserId(), host.getId(), 0.3, id);
						//right now we don't consider the pricing model;
						//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
					}
					host.getmsop().put(userid, aaa);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
				}
				else if(host.getmtimes().get(userid) == 1){
					host.addmtimes(userid);
					// will add codes for sop and aop and ...;
					broker5.CalNumOfRes1(user5.getuserId(), host.getId(), id);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
					//payprice = broker2.CalPrice(cloudlet2.getCloudletId(), host.getId());
				}
				else if(host.getmtimes().get(userid) == 2){
					host.addmtimes(userid);
					// will add codes for sop and aop and ...;
					broker5.CalNumOfRes2(user5.getuserId(), host.getId(), id);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
					//payprice = broker2.CalPrice(cloudlet2.getCloudletId(), host.getId());
				}
				else if(host.getmtimes().get(userid) > 2){
					broker5.CalNumOfRes(user5.getuserId(), host.getId(), id);
					int a = host.getmtimes().get(userid);
					host.addmtimes(userid);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
					
				}
			}

			
			acpu.add(user5.getnumCPU());
			aram.add(user5.getnumRAM());
			asto.add(user5.getnumStorage());
			utimeh = host.getmtimes().get(userid) ;
			System.out.println("this is the " + utimeh + "th time for user" + user5.getuserId() + " to use services of host" + host.getId());
			payprice = user5.getnumCPU() * 0.01 + user5.getnumRAM() * 0.01 + (double)(user5.getnumStorage()*0.001);
			vmid = 5;
			
			Vm vm5 = new Vm(vmid, broker5Id, mips, user5.getnumCPU(), user5.getnumRAM(), bw, user5.getnumStorage(), vmm, new CloudletSchedulerTimeShared());
			
			vm5.setHost(host);
			
			//int vmid2 = 1;
			//Vm vm2 = new Vm(vmid2, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
			// add the VM to the vmList
			vmlist.add(vm5);
			//vmlist.add(vm2);
			duration[5] = (int)(duration[5]*(1-relinp.get(0)));
			expiretime[5] = timestamp[5] + duration[5];
			CalUtinew(timestamp[5], acpu, uti, utiliz, utireal);

			// submit vm list to the broker
			broker5.submitVmList(vmlist);
			
			broker5.bindCloudletToVm(cloudletid, vmid);
			
			payprice = payprice * user5.gettime();
			
			reimbursement = payprice * bb;
			// Update the records of reimbursement and payment.
			reimburse.add(reimbursement);
			System.out.println(host.getmtimes().get(userid));
			payment.add(payprice);
			
			
			
			
			CloudSim.startSimulation();
			
			///////////////////////////////////
			//////////////////////////////////
			////////////////////////////////
			
			CloudSim.init(num_user, calendar, trace_flag);

			@SuppressWarnings("unused")
			Datacenter datacenter6 = createDatacenter("Datacenter_1");

			DatacenterBroker broker6 = createBroker();
			int broker6Id = broker6.getId();
			
			//update totalProfit;
			totalProfit += payprice;
			userid = 6;
			User user6 = new User(userid);
			userlist.add(user6);
			
			broker6.submitUserList(userlist);
			broker6.BindUserToHost(user6.getuserId(), host.getId());
			
			host.setTotalProfit(totalProfit);
			
			broker6.setHostList(hostlist1);
			
			cloudletid = 6;
			Cloudlet cloudlet6 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
			cloudlet6.setbrokerId(broker6Id);
			//update the price of cloudlet;
			//price = 600;
			//cloudlet6.setprice(price);
			cloudlet6.setuser(user6);
			user6.setCloudletId(cloudlet6.getCloudletId());
			
			// add the cloudlet to the list
			cloudletList.add(cloudlet6);

			// submit cloudlet list to the broker
			broker6.submitCloudletList(cloudletList);
			
			//adrian
			numtime = duration[6];
			res = resask[6];
			user6.submitCloudlet(cloudlet6);
			user6.submitRequest(user6.getCloudlet(), false, res, res, (long)(res), numtime);
			relinp.clear();
			host.getmtimes().put(userid, 0);
			for(int i = 0 ; i < 100; i++){
				double a = r.nextGaussian() * 0.3 + aveRP[6];
				if (a < 0 || a > 1){}
				else{
					relinp.add(a);
				}
			}
			if(aveRP[6] == 0){}
			else if(aveRP[6] > 0){
				
				ArrayList<Double> se6 = new ArrayList<Double> ();
				ArrayList<Double> de6 = new ArrayList<Double> ();
				host.getmaop().put(userid, se6);
				host.getmsop().put(userid, de6);
				for(int i = 1; i < 51; i++){
					host.addmaop(userid, relinp.get(i));
					host.addmsop(userid, relinp.get(i));
					host.addmtimes(userid);
				}
			}
			relinp.set(0,0.7754216615025256);
			basicprice = user6.getnumCPU() * 0.01 + user6.getnumRAM() * 0.01 + (double)(user6.getnumStorage()*0.001);
			pledge = basicprice * 1.05;
			System.out.println("user" + user6.getuserId() + " asks for " + user6.getnumCPU() + " CPUs and " + user6.getnumRAM() + " GB of RAM and " + user6.getnumStorage() + " GB of storage to host" + host.getId());
			utiliz = CalUti(timestamp[3], acpu, uti, expiretime, utireal);
			id = 0;
			if(cloudlet6.pledge == true){
				//here I didn't consider giving back the extra money if the user will not relinquish any resource;
				payprice = pledge;
			}
			else{
				id = DecideModel(utiliz, aveRP[6], userid, res);
				id = 1;
				//host.minusmtimes(userid);
				//here remember the BROKER is changing to BROKER2!!!!!!!!! and cloudlet is cloudlet2!!!!!!!!!
				if (host.getmtimes().get(userid) == 0){
					host.addmtimes(userid);
					ArrayList<Double> aaa = new ArrayList<Double> ();
					ArrayList<Double> bbb = new ArrayList<Double> ();
					//here I will set some default values for aop and sop and ...;
					if(host.getmaop().get(userid) == null){
					broker6.CalNumOfRes(user6.getuserId(), host.getId(), 0.3, 0.3, id);
					//right now we don't consider the pricing model;
					//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
					host.getmaop().put(userid, bbb);
					}
					else {
						broker6.CalNumOfRes(user6.getuserId(), host.getId(), 0.3, id);
						//right now we don't consider the pricing model;
						//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
					}
					host.getmsop().put(userid, aaa);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
				}
				else if(host.getmtimes().get(userid) == 1){
					host.addmtimes(userid);
					// will add codes for sop and aop and ...;
					broker6.CalNumOfRes1(user6.getuserId(), host.getId(), id);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
					//payprice = broker2.CalPrice(cloudlet2.getCloudletId(), host.getId());
				}
				else if(host.getmtimes().get(userid) == 2){
					host.addmtimes(userid);
					// will add codes for sop and aop and ...;
					broker6.CalNumOfRes2(user6.getuserId(), host.getId(), id);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
					//payprice = broker6.CalPrice(cloudlet6.getCloudletId(), host.getId());
				}
				else if(host.getmtimes().get(userid) > 2){
					broker6.CalNumOfRes(user6.getuserId(), host.getId(), id);
					int a = host.getmtimes().get(userid);
					host.addmtimes(userid);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
					
				}
			}

			
			acpu.add(user6.getnumCPU());
			aram.add(user6.getnumRAM());
			asto.add(user6.getnumStorage());
			utimeh = host.getmtimes().get(userid) ;
			System.out.println("this is the " + utimeh + "th time for user" + user6.getuserId() + " to use services of host" + host.getId());
			payprice = user6.getnumCPU() * 0.01 + user6.getnumRAM() * 0.01 + (double)(user6.getnumStorage()*0.001);
			vmid = 6;
			
			Vm vm6 = new Vm(vmid, broker6Id, mips, user6.getnumCPU(), user6.getnumRAM(), bw, user6.getnumStorage(), vmm, new CloudletSchedulerTimeShared());
			
			vm6.setHost(host);
			
			//int vmid2 = 1;
			//Vm vm2 = new Vm(vmid2, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
			// add the VM to the vmList
			vmlist.add(vm6);
			//vmlist.add(vm2);
			duration[6] = (int)(duration[6]*(1-relinp.get(0)));
			expiretime[6] = timestamp[6] + duration[6];
			CalUtinew(timestamp[6], acpu, uti, utiliz, utireal);
			// submit vm list to the broker
			broker6.submitVmList(vmlist);
			
			broker6.bindCloudletToVm(cloudletid, vmid);
			
			payprice = payprice * user6.gettime();
			
			reimbursement = payprice * bb;
			// Update the records of reimbursement and payment.
			reimburse.add(reimbursement);
			System.out.println(host.getmtimes().get(userid));
			payment.add(payprice);
			
			
			
			
			CloudSim.startSimulation();
			/////////////////////////////////
			//////////////////////////////////
			/////////////////////////////////
			CloudSim.init(num_user, calendar, trace_flag);

			@SuppressWarnings("unused")
			Datacenter datacenter7 = createDatacenter("Datacenter_1");

			DatacenterBroker broker7 = createBroker();
			int broker7Id = broker7.getId();
			
			//update totalProfit;
			totalProfit += payprice;
			userid = 7;
			User user7 = new User(userid);
			userlist.add(user7);
			
			broker7.submitUserList(userlist);
			broker7.BindUserToHost(user7.getuserId(), host.getId());
			
			host.setTotalProfit(totalProfit);
			
			broker7.setHostList(hostlist1);
			
			cloudletid = 7;
			Cloudlet cloudlet7 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
			cloudlet7.setbrokerId(broker7Id);
			//update the price of cloudlet;
			//price = 700;
			//cloudlet7.setprice(price);
			cloudlet7.setuser(user7);
			user7.setCloudletId(cloudlet7.getCloudletId());
			
			// add the cloudlet to the list
			cloudletList.add(cloudlet7);

			// submit cloudlet list to the broker
			broker7.submitCloudletList(cloudletList);
			
			//adrian
			numtime = duration[7];
			res = resask[7];
			user7.submitCloudlet(cloudlet7);
			user7.submitRequest(user7.getCloudlet(), false, res, res, (long)(res), numtime);
			relinp.clear();
			host.getmtimes().put(userid, 0);
			for(int i = 0 ; i < 100; i++){
				double a = r.nextGaussian() * 0.3 + aveRP[7];
				if (a < 0 || a > 1){}
				else{
					relinp.add(a);
				}
			}
			if(aveRP[7] == 0){}
			else if(aveRP[7] > 0){
				
				ArrayList<Double> se7 = new ArrayList<Double> ();
				ArrayList<Double> de7 = new ArrayList<Double> ();
				host.getmaop().put(userid, se7);
				host.getmsop().put(userid, de7);
				for(int i = 1; i < 51; i++){
					host.addmaop(userid, relinp.get(i));
					host.addmsop(userid, relinp.get(i));
					host.addmtimes(userid);
				}
			}
			relinp.set(0,0.29205263293591033);
			basicprice = user7.getnumCPU() * 0.01 + user7.getnumRAM() * 0.01 + (double)(user7.getnumStorage()*0.001);
			pledge = basicprice * 1.05;
			System.out.println("user" + user7.getuserId() + " asks for " + user7.getnumCPU() + " CPUs and " + user7.getnumRAM() + " GB of RAM and " + user7.getnumStorage() + " GB of storage to host" + host.getId());

			utiliz = CalUti(timestamp[7], acpu, uti, expiretime, utireal);
			id = 0;
			if(cloudlet7.pledge == true){
				//here I didn't consider giving back the extra money if the user will not relinquish any resource;
				payprice = pledge;
			}
			else{
				id = DecideModel(utiliz, aveRP[7], userid, res);
				id = 1;
				//host.minusmtimes(userid);
				//here remember the BROKER is changing to BROKER2!!!!!!!!! and cloudlet is cloudlet2!!!!!!!!!
				if (host.getmtimes().get(userid) == 0){
					host.addmtimes(userid);
					ArrayList<Double> aaa = new ArrayList<Double> ();
					ArrayList<Double> bbb = new ArrayList<Double> ();
					//here I will set some default values for aop and sop and ...;
					if(host.getmaop().get(userid) == null){
					broker7.CalNumOfRes(user7.getuserId(), host.getId(), 0.3, 0.3, id);
					//right now we don't consider the pricing model;
					//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
					host.getmaop().put(userid, bbb);
					}
					else {
						broker7.CalNumOfRes(user7.getuserId(), host.getId(), 0.3, id);
						//right now we don't consider the pricing model;
						//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
					}
					host.getmsop().put(userid, aaa);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
				}
				else if(host.getmtimes().get(userid) == 1){
					host.addmtimes(userid);
					// will add codes for sop and aop and ...;
					broker7.CalNumOfRes1(user7.getuserId(), host.getId(), id);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
					//payprice = broker2.CalPrice(cloudlet2.getCloudletId(), host.getId());
				}
				else if(host.getmtimes().get(userid) == 2){
					host.addmtimes(userid);
					// will add codes for sop and aop and ...;
					broker7.CalNumOfRes2(user7.getuserId(), host.getId(), id);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
					//payprice = broker7.CalPrice(cloudlet7.getCloudletId(), host.getId());
				}
				else if(host.getmtimes().get(userid) > 2){
					broker7.CalNumOfRes(user7.getuserId(), host.getId(), id);
					int a = host.getmtimes().get(userid);
					host.addmtimes(userid);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
					
				}
			}

			
			acpu.add(user7.getnumCPU());
			aram.add(user7.getnumRAM());
			asto.add(user7.getnumStorage());
			utimeh = host.getmtimes().get(userid) ;
			System.out.println("this is the " + utimeh + "th time for user" + user7.getuserId() + " to use services of host" + host.getId());
			payprice = user7.getnumCPU() * 0.01 + user7.getnumRAM() * 0.01 + (double)(user7.getnumStorage()*0.001);
			vmid = 7;
			
			Vm vm7 = new Vm(vmid, broker7Id, mips, user7.getnumCPU(), user7.getnumRAM(), bw, user7.getnumStorage(), vmm, new CloudletSchedulerTimeShared());
			
			vm7.setHost(host);
			
			//int vmid2 = 1;
			//Vm vm2 = new Vm(vmid2, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
			// add the VM to the vmList
			vmlist.add(vm7);
			//vmlist.add(vm2);
			duration[7] = (int)(duration[7]*(1-relinp.get(0)));
			expiretime[7] = timestamp[7] + duration[7];
			CalUtinew(timestamp[7], acpu, uti, utiliz, utireal);
			// submit vm list to the broker
			broker7.submitVmList(vmlist);
			
			broker7.bindCloudletToVm(cloudletid, vmid);
			
			payprice = payprice * user7.gettime();
			
			reimbursement = payprice * bb;
			// Update the records of reimbursement and payment.
			reimburse.add(reimbursement);
			System.out.println(host.getmtimes().get(userid));
			payment.add(payprice);
			
			
			
			
			CloudSim.startSimulation();
			//////////////////////////////////
			///////////////////////////////
			/////////////////////////////////
			CloudSim.init(num_user, calendar, trace_flag);

			@SuppressWarnings("unused")
			Datacenter datacenter8 = createDatacenter("Datacenter_1");

			DatacenterBroker broker8 = createBroker();
			int broker8Id = broker8.getId();
			
			//update totalProfit;
			totalProfit += payprice;
			userid = 8;
			User user8 = new User(userid);
			userlist.add(user8);
			
			broker8.submitUserList(userlist);
			broker8.BindUserToHost(user8.getuserId(), host.getId());
			
			host.setTotalProfit(totalProfit);
			
			broker8.setHostList(hostlist1);
			
			cloudletid = 8;
			Cloudlet cloudlet8 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
			cloudlet8.setbrokerId(broker8Id);
			//update the price of cloudlet;
			//price = 800;
			//cloudlet8.setprice(price);
			cloudlet8.setuser(user8);
			user8.setCloudletId(cloudlet8.getCloudletId());
			
			// add the cloudlet to the list
			cloudletList.add(cloudlet8);

			// submit cloudlet list to the broker
			broker8.submitCloudletList(cloudletList);
			
			//adrian
			numtime = duration[8];
			res = resask[8];
			user8.submitCloudlet(cloudlet8);
			user8.submitRequest(user8.getCloudlet(), false, res, res, (long)(res), numtime);
			relinp.clear();
			host.getmtimes().put(userid, 0);
			for(int i = 0 ; i < 100; i++){
				double a = r.nextGaussian() * 0.3 + aveRP[8];
				if (a < 0 || a > 1){}
				else{
					relinp.add(a);
				}
			}
			if(aveRP[8] == 0){}
			else if(aveRP[8] > 0){
				
				ArrayList<Double> se8 = new ArrayList<Double> ();
				ArrayList<Double> de8 = new ArrayList<Double> ();
				host.getmaop().put(userid, se8);
				host.getmsop().put(userid, de8);
				for(int i = 1; i < 51; i++){
					host.addmaop(userid, relinp.get(i));
					host.addmsop(userid, relinp.get(i));
					host.addmtimes(userid);
				}
			}
			relinp.set(0,0.7936001340804522);
			basicprice = user8.getnumCPU() * 0.01 + user8.getnumRAM() * 0.01 + (double)(user8.getnumStorage()*0.001);
			pledge = basicprice * 1.05;
			System.out.println("user" + user8.getuserId() + " asks for " + user8.getnumCPU() + " CPUs and " + user8.getnumRAM() + " GB of RAM and " + user8.getnumStorage() + " GB of storage to host" + host.getId());


			utiliz = CalUti(timestamp[8], acpu, uti, expiretime, utireal);
			id = 0;
			if(cloudlet8.pledge == true){
				//here I didn't consider giving back the extra money if the user will not relinquish any resource;
				payprice = pledge;
			}
			else{
				id = DecideModel(utiliz, aveRP[8], userid, res);
				id = 1;
				//host.minusmtimes(userid);
				//here remember the BROKER is changing to BROKER2!!!!!!!!! and cloudlet is cloudlet2!!!!!!!!!
				if (host.getmtimes().get(userid) == 0){
					host.addmtimes(userid);
					ArrayList<Double> aaa = new ArrayList<Double> ();
					ArrayList<Double> bbb = new ArrayList<Double> ();
					//here I will set some default values for aop and sop and ...;
					if(host.getmaop().get(userid) == null){
					broker8.CalNumOfRes(user8.getuserId(), host.getId(), 0.3, 0.3, id);
					//right now we don't consider the pricing model;
					//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
					host.getmaop().put(userid, bbb);
					}
					else {
						broker8.CalNumOfRes(user8.getuserId(), host.getId(), 0.3, id);
						//right now we don't consider the pricing model;
						//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
					}
					host.getmsop().put(userid, aaa);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
				}
				else if(host.getmtimes().get(userid) == 1){
					host.addmtimes(userid);
					// will add codes for sop and aop and ...;
					broker8.CalNumOfRes1(user8.getuserId(), host.getId(), id);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
					//payprice = broker2.CalPrice(cloudlet2.getCloudletId(), host.getId());
				}
				else if(host.getmtimes().get(userid) == 2){
					host.addmtimes(userid);
					// will add codes for sop and aop and ...;
					broker8.CalNumOfRes2(user8.getuserId(), host.getId(), id);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
					//payprice = broker8.CalPrice(cloudlet8.getCloudletId(), host.getId());
				}
				else if(host.getmtimes().get(userid) > 2){
					broker8.CalNumOfRes(user8.getuserId(), host.getId(), id);
					int a = host.getmtimes().get(userid);
					host.addmtimes(userid);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
					
				}
			}

			
			acpu.add(user8.getnumCPU());
			aram.add(user8.getnumRAM());
			asto.add(user8.getnumStorage());
			utimeh = host.getmtimes().get(userid) ;
			System.out.println("this is the " + utimeh + "th time for user" + user8.getuserId() + " to use services of host" + host.getId());
			payprice = user8.getnumCPU() * 0.01 + user8.getnumRAM() * 0.01 + (double)(user8.getnumStorage()*0.001);
			vmid = 8;
			
			Vm vm8 = new Vm(vmid, broker8Id, mips, user8.getnumCPU(), user8.getnumRAM(), bw, user8.getnumStorage(), vmm, new CloudletSchedulerTimeShared());
			
			vm8.setHost(host);
			
			//int vmid2 = 1;
			//Vm vm2 = new Vm(vmid2, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
			// add the VM to the vmList
			vmlist.add(vm8);
			//vmlist.add(vm2);
			duration[8] = (int)(duration[8]*(1-relinp.get(0)));
			expiretime[8] = timestamp[8] + duration[8];
			CalUtinew(timestamp[8], acpu, uti, utiliz, utireal);
			// submit vm list to the broker
			broker8.submitVmList(vmlist);
			
			broker8.bindCloudletToVm(cloudletid, vmid);
			
			payprice = payprice * user8.gettime();
			
			reimbursement = payprice * bb;
			// Update the records of reimbursement and payment.
			reimburse.add(reimbursement);
			System.out.println(host.getmtimes().get(userid));
			payment.add(payprice);
			
			
			
			
			CloudSim.startSimulation();
			////////////////////////////////////////
			///////////////////////////////////////
			/////////////////////////////////////
			CloudSim.init(num_user, calendar, trace_flag);

			@SuppressWarnings("unused")
			Datacenter datacenter9 = createDatacenter("Datacenter_1");

			DatacenterBroker broker9 = createBroker();
			int broker9Id = broker9.getId();
			
			//update totalProfit;
			totalProfit += payprice;
			userid = 9;
			User user9 = new User(userid);
			userlist.add(user9);
			
			broker9.submitUserList(userlist);
			broker9.BindUserToHost(user9.getuserId(), host.getId());
			
			host.setTotalProfit(totalProfit);
			
			broker9.setHostList(hostlist1);
			
			cloudletid = 9;
			Cloudlet cloudlet9 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
			cloudlet9.setbrokerId(broker9Id);
			//update the price of cloudlet;
			//price = 900;
			//cloudlet9.setprice(price);
			cloudlet9.setuser(user9);
			user9.setCloudletId(cloudlet9.getCloudletId());
			
			// add the cloudlet to the list
			cloudletList.add(cloudlet9);

			// submit cloudlet list to the broker
			broker9.submitCloudletList(cloudletList);
			
			//adrian
			numtime = duration[9];
			res = resask[9];
			user9.submitCloudlet(cloudlet9);
			user9.submitRequest(user9.getCloudlet(), false, res, res, (long)(res), numtime);
			relinp.clear();
			host.getmtimes().put(userid, 0);
			for(int i = 0 ; i < 100; i++){
				double a = r.nextGaussian() * 0.3 + aveRP[9];
				if (a < 0 || a > 1){}
				else{
					relinp.add(a);
				}
			}
			if(aveRP[9] == 0){}
			else if(aveRP[9] > 0){
				
				ArrayList<Double> se9 = new ArrayList<Double> ();
				ArrayList<Double> de9 = new ArrayList<Double> ();
				host.getmaop().put(userid, se9);
				host.getmsop().put(userid, de9);
				for(int i = 1; i < 51; i++){
					host.addmaop(userid, relinp.get(i));
					host.addmsop(userid, relinp.get(i));
					host.addmtimes(userid);
				}
			}
			relinp.set(0,0.46724986450346023);
			basicprice = user9.getnumCPU() * 0.01 + user9.getnumRAM() * 0.01 + (double)(user9.getnumStorage()*0.001);
			pledge = basicprice * 1.05;
			System.out.println("user" + user9.getuserId() + " asks for " + user9.getnumCPU() + " CPUs and " + user9.getnumRAM() + " GB of RAM and " + user9.getnumStorage() + " GB of storage to host" + host.getId());


			utiliz = CalUti(timestamp[9], acpu, uti, expiretime, utireal);
			id = 0;
			if(cloudlet9.pledge == true){
				//here I didn't consider giving back the extra money if the user will not relinquish any resource;
				payprice = pledge;
			}
			else{
				id = DecideModel(utiliz, aveRP[9], userid, res);
				id = 1;
				//host.minusmtimes(userid);
				//here remember the BROKER is changing to BROKER2!!!!!!!!! and cloudlet is cloudlet2!!!!!!!!!
				if (host.getmtimes().get(userid) == 0){
					host.addmtimes(userid);
					ArrayList<Double> aaa = new ArrayList<Double> ();
					ArrayList<Double> bbb = new ArrayList<Double> ();
					//here I will set some default values for aop and sop and ...;
					if(host.getmaop().get(userid) == null){
					broker9.CalNumOfRes(user9.getuserId(), host.getId(), 0.3, 0.3, id);
					//right now we don't consider the pricing model;
					//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
					host.getmaop().put(userid, bbb);
					}
					else {
						broker9.CalNumOfRes(user9.getuserId(), host.getId(), 0.3, id);
						//right now we don't consider the pricing model;
						//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
					}
					host.getmsop().put(userid, aaa);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
				}
				else if(host.getmtimes().get(userid) == 1){
					host.addmtimes(userid);
					// will add codes for sop and aop and ...;
					broker9.CalNumOfRes1(user9.getuserId(), host.getId(), id);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
					//payprice = broker2.CalPrice(cloudlet2.getCloudletId(), host.getId());
				}
				else if(host.getmtimes().get(userid) == 2){
					host.addmtimes(userid);
					// will add codes for sop and aop and ...;
					broker9.CalNumOfRes2(user9.getuserId(), host.getId(), id);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
					//payprice = broker9.CalPrice(cloudlet9.getCloudletId(), host.getId());
				}
				else if(host.getmtimes().get(userid) > 2){
					broker9.CalNumOfRes(user9.getuserId(), host.getId(), id);
					int a = host.getmtimes().get(userid);
					host.addmtimes(userid);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
					
				}
			}

			
			acpu.add(user9.getnumCPU());
			aram.add(user9.getnumRAM());
			asto.add(user9.getnumStorage());
			utimeh = host.getmtimes().get(userid) ;
			System.out.println("this is the " + utimeh + "th time for user" + user9.getuserId() + " to use services of host" + host.getId());
			payprice = user9.getnumCPU() * 0.01 + user9.getnumRAM() * 0.01 + (double)(user9.getnumStorage()*0.001);
			vmid = 9;
			
			Vm vm9 = new Vm(vmid, broker9Id, mips, user9.getnumCPU(), user9.getnumRAM(), bw, user9.getnumStorage(), vmm, new CloudletSchedulerTimeShared());
			
			vm9.setHost(host);
			
			//int vmid2 = 1;
			//Vm vm2 = new Vm(vmid2, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
			// add the VM to the vmList
			vmlist.add(vm9);
			//vmlist.add(vm2);
			duration[9] = (int)(duration[9]*(1-relinp.get(0)));
			expiretime[9] = timestamp[9] + duration[9];
			CalUtinew(timestamp[9], acpu, uti, utiliz, utireal);
			// submit vm list to the broker
			broker9.submitVmList(vmlist);
			
			broker9.bindCloudletToVm(cloudletid, vmid);
			
			payprice = payprice * user9.gettime();
			
			reimbursement = payprice * bb;
			// Update the records of reimbursement and payment.
			reimburse.add(reimbursement);
			System.out.println(host.getmtimes().get(userid));
			payment.add(payprice);
			
			
			
			
			CloudSim.startSimulation();
			/////////////////////////////////////////
			////////////////////////////////////////////
			
			CloudSim.init(num_user, calendar, trace_flag);

			@SuppressWarnings("unused")
			Datacenter datacenter10 = createDatacenter("Datacenter_1");

			DatacenterBroker broker10 = createBroker();
			int broker10Id = broker10.getId();
			
			//update totalProfit;
			totalProfit += payprice;
			userid = 10;
			User user10 = new User(userid);
			userlist.add(user10);
			
			broker10.submitUserList(userlist);
			broker10.BindUserToHost(user10.getuserId(), host.getId());
			
			host.setTotalProfit(totalProfit);
			
			broker10.setHostList(hostlist1);
			
			cloudletid = 10;
			Cloudlet cloudlet10 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
			cloudlet10.setbrokerId(broker10Id);
			//update the price of cloudlet;
			//price = 1000;
			//cloudlet10.setprice(price);
			cloudlet10.setuser(user10);
			user10.setCloudletId(cloudlet10.getCloudletId());
			
			// add the cloudlet to the list
			cloudletList.add(cloudlet10);

			// submit cloudlet list to the broker
			broker10.submitCloudletList(cloudletList);
			
			//adrian
			numtime = duration[10];
			res = resask[10];
			user10.submitCloudlet(cloudlet10);
			user10.submitRequest(user10.getCloudlet(), false, res, res, (long)(res), numtime);
			relinp.clear();
			host.getmtimes().put(userid, 0);
			for(int i = 0 ; i < 100; i++){
				double a = r.nextGaussian() * 0.3 + aveRP[10];
				if (a < 0 || a > 1){}
				else{
					relinp.add(a);
				}
			}
			if(aveRP[10] == 0){}
			else if(aveRP[10] > 0){
				
				ArrayList<Double> se10 = new ArrayList<Double> ();
				ArrayList<Double> de10 = new ArrayList<Double> ();
				host.getmaop().put(userid, se10);
				host.getmsop().put(userid, de10);
				for(int i = 1; i < 51; i++){
					host.addmaop(userid, relinp.get(i));
					host.addmsop(userid, relinp.get(i));
					host.addmtimes(userid);
				}
			}
			relinp.set(0,0.13830361655261783);
			basicprice = user10.getnumCPU() * 0.01 + user10.getnumRAM() * 0.01 + (double)(user10.getnumStorage()*0.001);
			pledge = basicprice * 1.05;
			System.out.println("user" + user10.getuserId() + " asks for " + user10.getnumCPU() + " CPUs and " + user10.getnumRAM() + " GB of RAM and " + user10.getnumStorage() + " GB of storage to host" + host.getId());

			utiliz = CalUti(timestamp[10], acpu, uti, expiretime, utireal);
			id = 0;
			if(cloudlet10.pledge == true){
				//here I didn't consider giving back the extra money if the user will not relinquish any resource;
				payprice = pledge;
			}
			else{
				id = DecideModel(utiliz, aveRP[10], userid, res);
				id = 1;
				//host.minusmtimes(userid);
				//here remember the BROKER is changing to BROKER2!!!!!!!!! and cloudlet is cloudlet2!!!!!!!!!
				if (host.getmtimes().get(userid) == 0){
					host.addmtimes(userid);
					ArrayList<Double> aaa = new ArrayList<Double> ();
					ArrayList<Double> bbb = new ArrayList<Double> ();
					//here I will set some default values for aop and sop and ...;
					if(host.getmaop().get(userid) == null){
					broker10.CalNumOfRes(user10.getuserId(), host.getId(), 0.3, 0.3, id);
					//right now we don't consider the pricing model;
					//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
					host.getmaop().put(userid, bbb);
					}
					else {
						broker10.CalNumOfRes(user10.getuserId(), host.getId(), 0.3, id);
						//right now we don't consider the pricing model;
						//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
					}
					host.getmsop().put(userid, aaa);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
				}
				else if(host.getmtimes().get(userid) == 1){
					host.addmtimes(userid);
					// will add codes for sop and aop and ...;
					broker10.CalNumOfRes1(user10.getuserId(), host.getId(), id);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
					//payprice = broker2.CalPrice(cloudlet2.getCloudletId(), host.getId());
				}
				else if(host.getmtimes().get(userid) == 2){
					host.addmtimes(userid);
					// will add codes for sop and aop and ...;
					broker10.CalNumOfRes2(user10.getuserId(), host.getId(), id);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
					//payprice = broker10.CalPrice(cloudlet10.getCloudletId(), host.getId());
				}
				else if(host.getmtimes().get(userid) > 2){
					broker10.CalNumOfRes(user10.getuserId(), host.getId(), id);
					int a = host.getmtimes().get(userid);
					host.addmtimes(userid);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
					
				}
			}

			
			acpu.add(user10.getnumCPU());
			aram.add(user10.getnumRAM());
			asto.add(user10.getnumStorage());
			utimeh = host.getmtimes().get(userid) ;
			System.out.println("this is the " + utimeh + "th time for user" + user10.getuserId() + " to use services of host" + host.getId());
			payprice = user10.getnumCPU() * 0.01 + user10.getnumRAM() * 0.01 + (double)(user10.getnumStorage()*0.001);
			vmid = 10;
			
			Vm vm10 = new Vm(vmid, broker10Id, mips, user10.getnumCPU(), user10.getnumRAM(), bw, user10.getnumStorage(), vmm, new CloudletSchedulerTimeShared());
			
			vm10.setHost(host);
			
			//int vmid2 = 1;
			//Vm vm2 = new Vm(vmid2, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
			// add the VM to the vmList
			vmlist.add(vm10);
			//vmlist.add(vm2);
			duration[10] = (int)(duration[10]*(1-relinp.get(0)));
			expiretime[10] = timestamp[10] + duration[10];
			CalUtinew(timestamp[10], acpu, uti, utiliz, utireal);
			// submit vm list to the broker
			broker10.submitVmList(vmlist);
			
			broker10.bindCloudletToVm(cloudletid, vmid);
			
			payprice = payprice * user10.gettime();
			
			reimbursement = payprice * bb;
			// Update the records of reimbursement and payment.
			reimburse.add(reimbursement);
			System.out.println(host.getmtimes().get(userid));
			payment.add(payprice);
			
			
			
			
			CloudSim.startSimulation();
			//////////////////////////////////////////
			////////////////////////////////////////////
			////////////////////////////////////////////
			CloudSim.init(num_user, calendar, trace_flag);

			@SuppressWarnings("unused")
			Datacenter datacenter11 = createDatacenter("Datacenter_1");

			DatacenterBroker broker11 = createBroker();
			int broker11Id = broker11.getId();
			
			//update totalProfit;
			totalProfit += payprice;
			userid = 11;
			User user11 = new User(userid);
			userlist.add(user11);
			
			broker11.submitUserList(userlist);
			broker11.BindUserToHost(user11.getuserId(), host.getId());
			
			host.setTotalProfit(totalProfit);
			
			broker11.setHostList(hostlist1);
			
			cloudletid = 11;
			Cloudlet cloudlet11 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
			cloudlet11.setbrokerId(broker11Id);
			//update the price of cloudlet;
			//price = 1100;
			//cloudlet11.setprice(price);
			cloudlet11.setuser(user11);
			user11.setCloudletId(cloudlet11.getCloudletId());
			
			// add the cloudlet to the list
			cloudletList.add(cloudlet11);

			// submit cloudlet list to the broker
			broker11.submitCloudletList(cloudletList);
			
			//adrian
			numtime = duration[11];
			res = resask[11];
			user11.submitCloudlet(cloudlet11);
			user11.submitRequest(user11.getCloudlet(), false, res, res, (long)(res), numtime);
			relinp.clear();
			host.getmtimes().put(userid, 0);
			for(int i = 0 ; i < 100; i++){
				double a = r.nextGaussian() * 0.3 + aveRP[11];
				if (a < 0 || a > 1){}
				else{
					relinp.add(a);
				}
			}
			if(aveRP[11] == 0){}
			else if(aveRP[11] > 0){
				
				ArrayList<Double> se11 = new ArrayList<Double> ();
				ArrayList<Double> de11 = new ArrayList<Double> ();
				host.getmaop().put(userid, se11);
				host.getmsop().put(userid, de11);
				for(int i = 1; i < 51; i++){
					host.addmaop(userid, relinp.get(i));
					host.addmsop(userid, relinp.get(i));
					host.addmtimes(userid);
				}
			}
			relinp.set(0,0.8914325412955506);
			basicprice = user11.getnumCPU() * 0.01 + user11.getnumRAM() * 0.01 + (double)(user11.getnumStorage()*0.001);
			pledge = basicprice * 1.05;
			System.out.println("user" + user11.getuserId() + " asks for " + user11.getnumCPU() + " CPUs and " + user11.getnumRAM() + " GB of RAM and " + user11.getnumStorage() + " GB of storage to host" + host.getId());

			utiliz = CalUti(timestamp[11], acpu, uti, expiretime, utireal);
			id = 0;
			if(cloudlet11.pledge == true){
				//here I didn't consider giving back the extra money if the user will not relinquish any resource;
				payprice = pledge;
			}
			else{
				id = DecideModel(utiliz, aveRP[11], userid, res);
				id = 1;
				//host.minusmtimes(userid);
				//here remember the BROKER is changing to BROKER2!!!!!!!!! and cloudlet is cloudlet2!!!!!!!!!
				if (host.getmtimes().get(userid) == 0){
					host.addmtimes(userid);
					ArrayList<Double> aaa = new ArrayList<Double> ();
					ArrayList<Double> bbb = new ArrayList<Double> ();
					//here I will set some default values for aop and sop and ...;
					if(host.getmaop().get(userid) == null){
					broker11.CalNumOfRes(user11.getuserId(), host.getId(), 0.3, 0.3, id);
					//right now we don't consider the pricing model;
					//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
					host.getmaop().put(userid, bbb);
					}
					else {
						broker11.CalNumOfRes(user11.getuserId(), host.getId(), 0.3, id);
						//right now we don't consider the pricing model;
						//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
					}
					host.getmsop().put(userid, aaa);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
				}
				else if(host.getmtimes().get(userid) == 1){
					host.addmtimes(userid);
					// will add codes for sop and aop and ...;
					broker11.CalNumOfRes1(user11.getuserId(), host.getId(), id);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
					//payprice = broker2.CalPrice(cloudlet2.getCloudletId(), host.getId());
				}
				else if(host.getmtimes().get(userid) == 2){
					host.addmtimes(userid);
					// will add codes for sop and aop and ...;
					broker11.CalNumOfRes2(user11.getuserId(), host.getId(), id);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
					//payprice = broker11.CalPrice(cloudlet11.getCloudletId(), host.getId());
				}
				else if(host.getmtimes().get(userid) > 2){
					broker11.CalNumOfRes(user11.getuserId(), host.getId(), id);
					int a = host.getmtimes().get(userid);
					host.addmtimes(userid);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
					
				}
			}

			
			acpu.add(user11.getnumCPU());
			aram.add(user11.getnumRAM());
			asto.add(user11.getnumStorage());
			utimeh = host.getmtimes().get(userid) ;
			System.out.println("this is the " + utimeh + "th time for user" + user11.getuserId() + " to use services of host" + host.getId());
			payprice = user11.getnumCPU() * 0.01 + user11.getnumRAM() * 0.01 + (double)(user11.getnumStorage()*0.001);
			vmid = 11;
			
			Vm vm11 = new Vm(vmid, broker11Id, mips, user11.getnumCPU(), user11.getnumRAM(), bw, user11.getnumStorage(), vmm, new CloudletSchedulerTimeShared());
			
			vm11.setHost(host);
			
			//int vmid2 = 1;
			//Vm vm2 = new Vm(vmid2, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
			// add the VM to the vmList
			vmlist.add(vm11);
			//vmlist.add(vm2);
			duration[11] = (int)(duration[11]*(1-relinp.get(0)));
			expiretime[11] = timestamp[11] + duration[11];
			CalUtinew(timestamp[11], acpu, uti, utiliz, utireal);
			// submit vm list to the broker
			broker11.submitVmList(vmlist);
			
			broker11.bindCloudletToVm(cloudletid, vmid);
			
			payprice = payprice * user11.gettime();
			
			reimbursement = payprice * bb;
			// Update the records of reimbursement and payment.
			reimburse.add(reimbursement);
			System.out.println(host.getmtimes().get(userid));
			payment.add(payprice);
			
			
			
			
			CloudSim.startSimulation();
			/////////////////////////////////////////////////
			//////////////////////////////////////////////////
			CloudSim.init(num_user, calendar, trace_flag);

			@SuppressWarnings("unused")
			Datacenter datacenter12 = createDatacenter("Datacenter_1");

			DatacenterBroker broker12 = createBroker();
			int broker12Id = broker12.getId();
			
			//update totalProfit;
			totalProfit += payprice;
			userid = 12;
			User user12 = new User(userid);
			userlist.add(user12);
			
			broker12.submitUserList(userlist);
			broker12.BindUserToHost(user12.getuserId(), host.getId());
			
			host.setTotalProfit(totalProfit);
			
			broker12.setHostList(hostlist1);
			
			cloudletid = 12;
			Cloudlet cloudlet12 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
			cloudlet12.setbrokerId(broker12Id);
			//update the price of cloudlet;
			//price = 1200;
			//cloudlet12.setprice(price);
			cloudlet12.setuser(user12);
			user12.setCloudletId(cloudlet12.getCloudletId());
			
			// add the cloudlet to the list
			cloudletList.add(cloudlet12);

			// submit cloudlet list to the broker
			broker12.submitCloudletList(cloudletList);
			
			//adrian
			numtime = duration[12];
			res = resask[12];
			user12.submitCloudlet(cloudlet12);
			user12.submitRequest(user12.getCloudlet(), false, res, res, (long)(res), numtime);
			relinp.clear();
			host.getmtimes().put(userid, 0);
			for(int i = 0 ; i < 100; i++){
				double a = r.nextGaussian() * 0.3 + aveRP[12];
				if (a < 0 || a > 1){}
				else{
					relinp.add(a);
				}
			}
			if(aveRP[12] == 0){}
			else if(aveRP[12] > 0){
				
				ArrayList<Double> se12 = new ArrayList<Double> ();
				ArrayList<Double> de12 = new ArrayList<Double> ();
				host.getmaop().put(userid, se12);
				host.getmsop().put(userid, de12);
				for(int i = 1; i < 51; i++){
					host.addmaop(userid, relinp.get(i));
					host.addmsop(userid, relinp.get(i));
					host.addmtimes(userid);
				}
			}
			relinp.set(0,0.5342878638709047);
			basicprice = user12.getnumCPU() * 0.01 + user12.getnumRAM() * 0.01 + (double)(user12.getnumStorage()*0.001);
			pledge = basicprice * 1.05;
			System.out.println("user" + user12.getuserId() + " asks for " + user12.getnumCPU() + " CPUs and " + user12.getnumRAM() + " GB of RAM and " + user12.getnumStorage() + " GB of storage to host" + host.getId());

			utiliz = CalUti(timestamp[12], acpu, uti, expiretime, utireal);
			id = 0;
			if(cloudlet12.pledge == true){
				//here I didn't consider giving back the extra money if the user will not relinquish any resource;
				payprice = pledge;
			}
			else{
				id = DecideModel(utiliz, aveRP[12], userid, res);
				id = 1;
				//host.minusmtimes(userid);
				//here remember the BROKER is changing to BROKER2!!!!!!!!! and cloudlet is cloudlet2!!!!!!!!!
				if (host.getmtimes().get(userid) == 0){
					host.addmtimes(userid);
					ArrayList<Double> aaa = new ArrayList<Double> ();
					ArrayList<Double> bbb = new ArrayList<Double> ();
					//here I will set some default values for aop and sop and ...;
					if(host.getmaop().get(userid) == null){
					broker12.CalNumOfRes(user12.getuserId(), host.getId(), 0.3, 0.3, id);
					//right now we don't consider the pricing model;
					//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
					host.getmaop().put(userid, bbb);
					}
					else {
						broker12.CalNumOfRes(user12.getuserId(), host.getId(), 0.3, id);
						//right now we don't consider the pricing model;
						//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
					}
					host.getmsop().put(userid, aaa);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
				}
				else if(host.getmtimes().get(userid) == 1){
					host.addmtimes(userid);
					// will add codes for sop and aop and ...;
					broker12.CalNumOfRes1(user12.getuserId(), host.getId(), id);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
					//payprice = broker2.CalPrice(cloudlet2.getCloudletId(), host.getId());
				}
				else if(host.getmtimes().get(userid) == 2){
					host.addmtimes(userid);
					// will add codes for sop and aop and ...;
					broker12.CalNumOfRes2(user12.getuserId(), host.getId(), id);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
					//payprice = broker12.CalPrice(cloudlet12.getCloudletId(), host.getId());
				}
				else if(host.getmtimes().get(userid) > 2){
					broker12.CalNumOfRes(user12.getuserId(), host.getId(), id);
					int a = host.getmtimes().get(userid);
					host.addmtimes(userid);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
					
				}
			}

			
			acpu.add(user12.getnumCPU());
			aram.add(user12.getnumRAM());
			asto.add(user12.getnumStorage());
			utimeh = host.getmtimes().get(userid) ;
			System.out.println("this is the " + utimeh + "th time for user" + user12.getuserId() + " to use services of host" + host.getId());
			payprice = user12.getnumCPU() * 0.01 + user12.getnumRAM() * 0.01 + (double)(user12.getnumStorage()*0.001);
			vmid = 12;
			
			Vm vm12 = new Vm(vmid, broker12Id, mips, user12.getnumCPU(), user12.getnumRAM(), bw, user12.getnumStorage(), vmm, new CloudletSchedulerTimeShared());
			
			vm12.setHost(host);
			
			//int vmid2 = 1;
			//Vm vm2 = new Vm(vmid2, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
			// add the VM to the vmList
			vmlist.add(vm12);
			//vmlist.add(vm2);
			duration[12] = (int)(duration[12]*(1-relinp.get(0)));
			expiretime[12] = timestamp[12] + duration[12];
			CalUtinew(timestamp[12], acpu, uti, utiliz, utireal);
			// submit vm list to the broker
			broker12.submitVmList(vmlist);
			
			broker12.bindCloudletToVm(cloudletid, vmid);
			
			payprice = payprice * user12.gettime();
			
			reimbursement = payprice * bb;
			// Update the records of reimbursement and payment.
			reimburse.add(reimbursement);
			System.out.println(host.getmtimes().get(userid));
			payment.add(payprice);
			
			
			
			
			CloudSim.startSimulation();
			///////////////////////////////////////
			/////////////////////////////////////////
			CloudSim.init(num_user, calendar, trace_flag);

			@SuppressWarnings("unused")
			Datacenter datacenter13 = createDatacenter("Datacenter_1");

			DatacenterBroker broker13 = createBroker();
			int broker13Id = broker13.getId();
			
			//update totalProfit;
			totalProfit += payprice;
			userid = 13;
			User user13 = new User(userid);
			userlist.add(user13);
			
			broker13.submitUserList(userlist);
			broker13.BindUserToHost(user13.getuserId(), host.getId());
			
			host.setTotalProfit(totalProfit);
			
			broker13.setHostList(hostlist1);
			
			cloudletid = 13;
			Cloudlet cloudlet13 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
			cloudlet13.setbrokerId(broker13Id);
			//update the price of cloudlet;
			//price = 1300;
			//cloudlet13.setprice(price);
			cloudlet13.setuser(user13);
			user13.setCloudletId(cloudlet13.getCloudletId());
			
			// add the cloudlet to the list
			cloudletList.add(cloudlet13);

			// submit cloudlet list to the broker
			broker13.submitCloudletList(cloudletList);
			
			//adrian
			numtime = duration[13];
			res = resask[13];
			user13.submitCloudlet(cloudlet13);
			user13.submitRequest(user13.getCloudlet(), false, res, res, (long)(res), numtime);
			relinp.clear();
			host.getmtimes().put(userid, 0);
			for(int i = 0 ; i < 100; i++){
				double a = r.nextGaussian() * 0.3 + aveRP[13];
				if (a < 0 || a > 1){}
				else{
					relinp.add(a);
				}
			}
			if(aveRP[13] == 0){}
			else if(aveRP[13] > 0){
				
				ArrayList<Double> se13 = new ArrayList<Double> ();
				ArrayList<Double> de13 = new ArrayList<Double> ();
				host.getmaop().put(userid, se13);
				host.getmsop().put(userid, de13);
				for(int i = 1; i < 51; i++){
					host.addmaop(userid, relinp.get(i));
					host.addmsop(userid, relinp.get(i));
					host.addmtimes(userid);
				}
			}
			relinp.set(0,0.5618863601910468);
			basicprice = user13.getnumCPU() * 0.01 + user13.getnumRAM() * 0.01 + (double)(user13.getnumStorage()*0.001);
			pledge = basicprice * 1.05;
			System.out.println("user" + user13.getuserId() + " asks for " + user13.getnumCPU() + " CPUs and " + user13.getnumRAM() + " GB of RAM and " + user13.getnumStorage() + " GB of storage to host" + host.getId());

			utiliz = CalUti(timestamp[13], acpu, uti, expiretime, utireal);
			id = 0;
			if(cloudlet13.pledge == true){
				//here I didn't consider giving back the extra money if the user will not relinquish any resource;
				payprice = pledge;
			}
			else{
				id = DecideModel(utiliz, aveRP[13], userid, res);
				id = 1;
				//host.minusmtimes(userid);
				//here remember the BROKER is changing to BROKER2!!!!!!!!! and cloudlet is cloudlet2!!!!!!!!!
				if (host.getmtimes().get(userid) == 0){
					host.addmtimes(userid);
					ArrayList<Double> aaa = new ArrayList<Double> ();
					ArrayList<Double> bbb = new ArrayList<Double> ();
					//here I will set some default values for aop and sop and ...;
					if(host.getmaop().get(userid) == null){
					broker13.CalNumOfRes(user13.getuserId(), host.getId(), 0.3, 0.3, id);
					//right now we don't consider the pricing model;
					//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
					host.getmaop().put(userid, bbb);
					}
					else {
						broker13.CalNumOfRes(user13.getuserId(), host.getId(), 0.3, id);
						//right now we don't consider the pricing model;
						//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
					}
					host.getmsop().put(userid, aaa);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
				}
				else if(host.getmtimes().get(userid) == 1){
					host.addmtimes(userid);
					// will add codes for sop and aop and ...;
					broker13.CalNumOfRes1(user13.getuserId(), host.getId(), id);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
					//payprice = broker2.CalPrice(cloudlet2.getCloudletId(), host.getId());
				}
				else if(host.getmtimes().get(userid) == 2){
					host.addmtimes(userid);
					// will add codes for sop and aop and ...;
					broker13.CalNumOfRes2(user13.getuserId(), host.getId(), id);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
					//payprice = broker13.CalPrice(cloudlet13.getCloudletId(), host.getId());
				}
				else if(host.getmtimes().get(userid) > 2){
					broker13.CalNumOfRes(user13.getuserId(), host.getId(), id);
					int a = host.getmtimes().get(userid);
					host.addmtimes(userid);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
					
				}
			}

			
			acpu.add(user13.getnumCPU());
			aram.add(user13.getnumRAM());
			asto.add(user13.getnumStorage());
			utimeh = host.getmtimes().get(userid) ;
			System.out.println("this is the " + utimeh + "th time for user" + user13.getuserId() + " to use services of host" + host.getId());
			payprice = user13.getnumCPU() * 0.01 + user13.getnumRAM() * 0.01 + (double)(user13.getnumStorage()*0.001);
			vmid = 13;
			
			Vm vm13 = new Vm(vmid, broker13Id, mips, user13.getnumCPU(), user13.getnumRAM(), bw, user13.getnumStorage(), vmm, new CloudletSchedulerTimeShared());
			
			vm13.setHost(host);
			
			//int vmid2 = 1;
			//Vm vm2 = new Vm(vmid2, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
			// add the VM to the vmList
			vmlist.add(vm13);
			//vmlist.add(vm2);
			duration[13] = (int)(duration[13]*(1-relinp.get(0)));
			expiretime[13] = timestamp[13] + duration[13];
			CalUtinew(timestamp[13], acpu, uti, utiliz, utireal);
			// submit vm list to the broker
			broker13.submitVmList(vmlist);
			
			broker13.bindCloudletToVm(cloudletid, vmid);
			
			payprice = payprice * user13.gettime();
			
			reimbursement = payprice * bb;
			// Update the records of reimbursement and payment.
			reimburse.add(reimbursement);
			System.out.println(host.getmtimes().get(userid));
			payment.add(payprice);
			
			
			
			
			CloudSim.startSimulation();
			////////////////////////////////////
			/////////////////////////////////////
			CloudSim.init(num_user, calendar, trace_flag);

			@SuppressWarnings("unused")
			Datacenter datacenter14 = createDatacenter("Datacenter_1");

			DatacenterBroker broker14 = createBroker();
			int broker14Id = broker14.getId();
			
			//update totalProfit;
			totalProfit += payprice;
			userid = 14;
			User user14 = new User(userid);
			userlist.add(user14);
			
			broker14.submitUserList(userlist);
			broker14.BindUserToHost(user14.getuserId(), host.getId());
			
			host.setTotalProfit(totalProfit);
			
			broker14.setHostList(hostlist1);
			
			cloudletid = 14;
			Cloudlet cloudlet14 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
			cloudlet14.setbrokerId(broker14Id);
			//update the price of cloudlet;
			//price = 1400;
			//cloudlet14.setprice(price);
			cloudlet14.setuser(user14);
			user14.setCloudletId(cloudlet14.getCloudletId());
			
			// add the cloudlet to the list
			cloudletList.add(cloudlet14);

			// submit cloudlet list to the broker
			broker14.submitCloudletList(cloudletList);
			
			//adrian
			numtime = duration[14];
			res = resask[14];
			user14.submitCloudlet(cloudlet14);
			user14.submitRequest(user14.getCloudlet(), false, res, res, (long)(res), numtime);
			relinp.clear();
			host.getmtimes().put(userid, 0);
			for(int i = 0 ; i < 100; i++){
				double a = r.nextGaussian() * 0.3 + aveRP[14];
				if (a < 0 || a > 1){}
				else{
					relinp.add(a);
				}
			}
			if(aveRP[14] == 0){}
			else if(aveRP[14] > 0){
				
				ArrayList<Double> se14 = new ArrayList<Double> ();
				ArrayList<Double> de14 = new ArrayList<Double> ();
				host.getmaop().put(userid, se14);
				host.getmsop().put(userid, de14);
				for(int i = 1; i < 51; i++){
					host.addmaop(userid, relinp.get(i));
					host.addmsop(userid, relinp.get(i));
					host.addmtimes(userid);
				}
			}
			relinp.set(0,0.6316339619573237);
			basicprice = user14.getnumCPU() * 0.01 + user14.getnumRAM() * 0.01 + (double)(user14.getnumStorage()*0.001);
			pledge = basicprice * 1.05;
			System.out.println("user" + user14.getuserId() + " asks for " + user14.getnumCPU() + " CPUs and " + user14.getnumRAM() + " GB of RAM and " + user14.getnumStorage() + " GB of storage to host" + host.getId());

			
			utiliz = CalUti(timestamp[14], acpu, uti, expiretime, utireal);
			id = 0;
			if(cloudlet14.pledge == true){
				//here I didn't consider giving back the extra money if the user will not relinquish any resource;
				payprice = pledge;
			}
			else{
				id = DecideModel(utiliz, aveRP[14], userid, res);
				id = 1;
				//host.minusmtimes(userid);
				//here remember the BROKER is changing to BROKER2!!!!!!!!! and cloudlet is cloudlet2!!!!!!!!!
				if (host.getmtimes().get(userid) == 0){
					host.addmtimes(userid);
					ArrayList<Double> aaa = new ArrayList<Double> ();
					ArrayList<Double> bbb = new ArrayList<Double> ();
					//here I will set some default values for aop and sop and ...;
					if(host.getmaop().get(userid) == null){
					broker14.CalNumOfRes(user14.getuserId(), host.getId(), 0.3, 0.3, id);
					//right now we don't consider the pricing model;
					//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
					host.getmaop().put(userid, bbb);
					}
					else {
						broker14.CalNumOfRes(user14.getuserId(), host.getId(), 0.3, id);
						//right now we don't consider the pricing model;
						//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
					}
					host.getmsop().put(userid, aaa);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
				}
				else if(host.getmtimes().get(userid) == 1){
					host.addmtimes(userid);
					// will add codes for sop and aop and ...;
					broker14.CalNumOfRes1(user14.getuserId(), host.getId(), id);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
					//payprice = broker2.CalPrice(cloudlet2.getCloudletId(), host.getId());
				}
				else if(host.getmtimes().get(userid) == 2){
					host.addmtimes(userid);
					// will add codes for sop and aop and ...;
					broker14.CalNumOfRes2(user14.getuserId(), host.getId(), id);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
					//payprice = broker14.CalPrice(cloudlet14.getCloudletId(), host.getId());
				}
				else if(host.getmtimes().get(userid) > 2){
					broker14.CalNumOfRes(user14.getuserId(), host.getId(), id);
					int a = host.getmtimes().get(userid);
					host.addmtimes(userid);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
					
				}
			}

			
			acpu.add(user14.getnumCPU());
			aram.add(user14.getnumRAM());
			asto.add(user14.getnumStorage());
			utimeh = host.getmtimes().get(userid) ;
			System.out.println("this is the " + utimeh + "th time for user" + user14.getuserId() + " to use services of host" + host.getId());
			payprice = user14.getnumCPU() * 0.01 + user14.getnumRAM() * 0.01 + (double)(user14.getnumStorage()*0.001);
			vmid = 14;
			
			Vm vm14 = new Vm(vmid, broker14Id, mips, user14.getnumCPU(), user14.getnumRAM(), bw, user14.getnumStorage(), vmm, new CloudletSchedulerTimeShared());
			
			vm14.setHost(host);
			
			//int vmid2 = 1;
			//Vm vm2 = new Vm(vmid2, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
			// add the VM to the vmList
			vmlist.add(vm14);
			//vmlist.add(vm2);
			duration[14] = (int)(duration[14]*(1-relinp.get(0)));
			expiretime[14] = timestamp[14] + duration[14];
			CalUtinew(timestamp[14], acpu, uti, utiliz, utireal);
			// submit vm list to the broker
			broker14.submitVmList(vmlist);
			
			broker14.bindCloudletToVm(cloudletid, vmid);
			
			payprice = payprice * user14.gettime();
			
			reimbursement = payprice * bb;
			// Update the records of reimbursement and payment.
			reimburse.add(reimbursement);
			System.out.println(host.getmtimes().get(userid));
			payment.add(payprice);
			
			
			
			
			CloudSim.startSimulation();
			//////////////////////////////////////
			///////////////////////////////////////
			CloudSim.init(num_user, calendar, trace_flag);

			@SuppressWarnings("unused")
			Datacenter datacenter15 = createDatacenter("Datacenter_1");

			DatacenterBroker broker15 = createBroker();
			int broker15Id = broker15.getId();
			
			//update totalProfit;
			totalProfit += payprice;
			userid = 15;
			User user15 = new User(userid);
			userlist.add(user15);
			
			broker15.submitUserList(userlist);
			broker15.BindUserToHost(user15.getuserId(), host.getId());
			
			host.setTotalProfit(totalProfit);
			
			broker15.setHostList(hostlist1);
			
			cloudletid = 15;
			Cloudlet cloudlet15 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
			cloudlet15.setbrokerId(broker15Id);
			//update the price of cloudlet;
			//price = 1500;
			//cloudlet15.setprice(price);
			cloudlet15.setuser(user15);
			user15.setCloudletId(cloudlet15.getCloudletId());
			
			// add the cloudlet to the list
			cloudletList.add(cloudlet15);

			// submit cloudlet list to the broker
			broker15.submitCloudletList(cloudletList);
			
			//adrian
			numtime = duration[15];
			res = resask[15];
			user15.submitCloudlet(cloudlet15);
			user15.submitRequest(user15.getCloudlet(), false, res, res, (long)(res), numtime);
			relinp.clear();
			host.getmtimes().put(userid, 0);
			for(int i = 0 ; i < 100; i++){
				double a = r.nextGaussian() * 0.3 + aveRP[15];
				if (a < 0 || a > 1){}
				else{
					relinp.add(a);
				}
			}
			if(aveRP[15] == 0){}
			else if(aveRP[15] > 0){
				
				ArrayList<Double> se15 = new ArrayList<Double> ();
				ArrayList<Double> de15 = new ArrayList<Double> ();
				host.getmaop().put(userid, se15);
				host.getmsop().put(userid, de15);
				for(int i = 1; i < 51; i++){
					host.addmaop(userid, relinp.get(i));
					host.addmsop(userid, relinp.get(i));
					host.addmtimes(userid);
				}
			}
			relinp.set(0,0.42142471778390245);
			basicprice = user15.getnumCPU() * 0.01 + user15.getnumRAM() * 0.01 + (double)(user15.getnumStorage()*0.001);
			pledge = basicprice * 1.05;
			System.out.println("user" + user15.getuserId() + " asks for " + user15.getnumCPU() + " CPUs and " + user15.getnumRAM() + " GB of RAM and " + user15.getnumStorage() + " GB of storage to host" + host.getId());

			utiliz = CalUti(timestamp[15], acpu, uti, expiretime, utireal);
			id = 0;
			if(cloudlet15.pledge == true){
				//here I didn't consider giving back the extra money if the user will not relinquish any resource;
				payprice = pledge;
			}
			else{
				id = DecideModel(utiliz, aveRP[15], userid, res);
				id = 1;
				//host.minusmtimes(userid);
				//here remember the BROKER is changing to BROKER2!!!!!!!!! and cloudlet is cloudlet2!!!!!!!!!
				if (host.getmtimes().get(userid) == 0){
					host.addmtimes(userid);
					ArrayList<Double> aaa = new ArrayList<Double> ();
					ArrayList<Double> bbb = new ArrayList<Double> ();
					//here I will set some default values for aop and sop and ...;
					if(host.getmaop().get(userid) == null){
					broker15.CalNumOfRes(user15.getuserId(), host.getId(), 0.3, 0.3, id);
					//right now we don't consider the pricing model;
					//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
					host.getmaop().put(userid, bbb);
					}
					else {
						broker15.CalNumOfRes(user15.getuserId(), host.getId(), 0.3, id);
						//right now we don't consider the pricing model;
						//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
					}
					host.getmsop().put(userid, aaa);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
				}
				else if(host.getmtimes().get(userid) == 1){
					host.addmtimes(userid);
					// will add codes for sop and aop and ...;
					broker15.CalNumOfRes1(user15.getuserId(), host.getId(), id);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
					//payprice = broker2.CalPrice(cloudlet2.getCloudletId(), host.getId());
				}
				else if(host.getmtimes().get(userid) == 2){
					host.addmtimes(userid);
					// will add codes for sop and aop and ...;
					broker15.CalNumOfRes2(user15.getuserId(), host.getId(), id);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
					//payprice = broker15.CalPrice(cloudlet15.getCloudletId(), host.getId());
				}
				else if(host.getmtimes().get(userid) > 2){
					broker15.CalNumOfRes(user15.getuserId(), host.getId(), id);
					int a = host.getmtimes().get(userid);
					host.addmtimes(userid);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
					
				}
			}

			
			acpu.add(user15.getnumCPU());
			aram.add(user15.getnumRAM());
			asto.add(user15.getnumStorage());
			utimeh = host.getmtimes().get(userid) ;
			System.out.println("this is the " + utimeh + "th time for user" + user15.getuserId() + " to use services of host" + host.getId());
			payprice = user15.getnumCPU() * 0.01 + user15.getnumRAM() * 0.01 + (double)(user15.getnumStorage()*0.001);
			vmid = 15;
			
			Vm vm15 = new Vm(vmid, broker15Id, mips, user15.getnumCPU(), user15.getnumRAM(), bw, user15.getnumStorage(), vmm, new CloudletSchedulerTimeShared());
			
			vm15.setHost(host);
			
			//int vmid2 = 1;
			//Vm vm2 = new Vm(vmid2, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
			// add the VM to the vmList
			vmlist.add(vm15);
			//vmlist.add(vm2);
			duration[15] = (int)(duration[15]*(1-relinp.get(0)));
			expiretime[15] = timestamp[15] + duration[15];
			CalUtinew(timestamp[15], acpu, uti, utiliz, utireal);
			// submit vm list to the broker
			broker15.submitVmList(vmlist);
			
			broker15.bindCloudletToVm(cloudletid, vmid);
			
			payprice = payprice * user15.gettime();
			
			reimbursement = payprice * bb;
			// Update the records of reimbursement and payment.
			reimburse.add(reimbursement);
			System.out.println(host.getmtimes().get(userid));
			payment.add(payprice);
			
			
			
			
			CloudSim.startSimulation();
			////////////////////////////////
			////////////////////////////
			CloudSim.init(num_user, calendar, trace_flag);

			@SuppressWarnings("unused")
			Datacenter datacenter16 = createDatacenter("Datacenter_1");

			DatacenterBroker broker16 = createBroker();
			int broker16Id = broker16.getId();
			
			//update totalProfit;
			totalProfit += payprice;
			userid = 16;
			User user16 = new User(userid);
			userlist.add(user16);
			
			broker16.submitUserList(userlist);
			broker16.BindUserToHost(user16.getuserId(), host.getId());
			
			host.setTotalProfit(totalProfit);
			
			broker16.setHostList(hostlist1);
			
			cloudletid = 16;
			Cloudlet cloudlet16 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
			cloudlet16.setbrokerId(broker16Id);
			//update the price of cloudlet;
			//price = 1600;
			//cloudlet16.setprice(price);
			cloudlet16.setuser(user16);
			user16.setCloudletId(cloudlet16.getCloudletId());
			
			// add the cloudlet to the list
			cloudletList.add(cloudlet16);

			// submit cloudlet list to the broker
			broker16.submitCloudletList(cloudletList);
			
			//adrian
			numtime = duration[16];
			res = resask[16];
			user16.submitCloudlet(cloudlet16);
			user16.submitRequest(user16.getCloudlet(), false, res, res, (long)(res), numtime);
			relinp.clear();
			host.getmtimes().put(userid, 0);
			for(int i = 0 ; i < 100; i++){
				double a = r.nextGaussian() * 0.3 + aveRP[16];
				if (a < 0 || a > 1){}
				else{
					relinp.add(a);
				}
			}
			if(aveRP[16] == 0){}
			else if(aveRP[16] > 0){
				ArrayList<Double> se16 = new ArrayList<Double> ();
				ArrayList<Double> de16 = new ArrayList<Double> ();
				host.getmaop().put(userid, se16);
				host.getmsop().put(userid, de16);
				for(int i = 1; i < 51; i++){
					host.addmaop(userid, relinp.get(i));
					host.addmsop(userid, relinp.get(i));
					host.addmtimes(userid);
				}
			}
			relinp.set(0,0.27795662970055923);
			basicprice = user16.getnumCPU() * 0.01 + user16.getnumRAM() * 0.01 + (double)(user16.getnumStorage()*0.001);
			pledge = basicprice * 1.05;
			System.out.println("user" + user16.getuserId() + " asks for " + user16.getnumCPU() + " CPUs and " + user16.getnumRAM() + " GB of RAM and " + user16.getnumStorage() + " GB of storage to host" + host.getId());

			utiliz = CalUti(timestamp[16], acpu, uti, expiretime, utireal);
			id = 0;
			if(cloudlet16.pledge == true){
				//here I didn't consider giving back the extra money if the user will not relinquish any resource;
				payprice = pledge;
			}
			else{
				id = DecideModel(utiliz, aveRP[16], userid, res);
				id = 1;
				//host.minusmtimes(userid);
				//here remember the BROKER is changing to BROKER2!!!!!!!!! and cloudlet is cloudlet2!!!!!!!!!
				if (host.getmtimes().get(userid) == 0){
					host.addmtimes(userid);
					ArrayList<Double> aaa = new ArrayList<Double> ();
					ArrayList<Double> bbb = new ArrayList<Double> ();
					//here I will set some default values for aop and sop and ...;
					if(host.getmaop().get(userid) == null){
					broker16.CalNumOfRes(user16.getuserId(), host.getId(), 0.3, 0.3, id);
					//right now we don't consider the pricing model;
					//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
					host.getmaop().put(userid, bbb);
					}
					else {
						broker16.CalNumOfRes(user16.getuserId(), host.getId(), 0.3, id);
						//right now we don't consider the pricing model;
						//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
					}
					host.getmsop().put(userid, aaa);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
				}
				else if(host.getmtimes().get(userid) == 1){
					host.addmtimes(userid);
					// will add codes for sop and aop and ...;
					broker16.CalNumOfRes1(user16.getuserId(), host.getId(), id);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
					//payprice = broker2.CalPrice(cloudlet2.getCloudletId(), host.getId());
				}
				else if(host.getmtimes().get(userid) == 2){
					host.addmtimes(userid);
					// will add codes for sop and aop and ...;
					broker16.CalNumOfRes2(user16.getuserId(), host.getId(), id);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
					//payprice = broker16.CalPrice(cloudlet16.getCloudletId(), host.getId());
				}
				else if(host.getmtimes().get(userid) > 2){
					broker16.CalNumOfRes(user16.getuserId(), host.getId(), id);
					int a = host.getmtimes().get(userid);
					host.addmtimes(userid);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
					
				}
			}

			
			acpu.add(user16.getnumCPU());
			aram.add(user16.getnumRAM());
			asto.add(user16.getnumStorage());
			utimeh = host.getmtimes().get(userid) ;
			System.out.println("this is the " + utimeh + "th time for user" + user16.getuserId() + " to use services of host" + host.getId());
			payprice = user16.getnumCPU() * 0.01 + user16.getnumRAM() * 0.01 + (double)(user16.getnumStorage()*0.001);
			vmid = 16;
			
			Vm vm16 = new Vm(vmid, broker16Id, mips, user16.getnumCPU(), user16.getnumRAM(), bw, user16.getnumStorage(), vmm, new CloudletSchedulerTimeShared());
			
			vm16.setHost(host);
			
			//int vmid2 = 1;
			//Vm vm2 = new Vm(vmid2, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
			// add the VM to the vmList
			vmlist.add(vm16);
			//vmlist.add(vm2);
			duration[16] = (int)(duration[16]*(1-relinp.get(0)));
			expiretime[16] = timestamp[16] + duration[16];
			CalUtinew(timestamp[16], acpu, uti, utiliz, utireal);
			// submit vm list to the broker
			broker16.submitVmList(vmlist);
			
			broker16.bindCloudletToVm(cloudletid, vmid);
			
			payprice = payprice * user16.gettime();
			
			reimbursement = payprice * bb;
			// Update the records of reimbursement and payment.
			reimburse.add(reimbursement);
			System.out.println(host.getmtimes().get(userid));
			payment.add(payprice);
			
			
			
			
			CloudSim.startSimulation();
			
			///////////////////////////////////
			///////////////////////////////////
			CloudSim.init(num_user, calendar, trace_flag);

			@SuppressWarnings("unused")
			Datacenter datacenter17 = createDatacenter("Datacenter_1");

			DatacenterBroker broker17 = createBroker();
			int broker17Id = broker17.getId();
			
			//update totalProfit;
			totalProfit += payprice;
			userid = 17;
			User user17 = new User(userid);
			userlist.add(user17);
			
			broker17.submitUserList(userlist);
			broker17.BindUserToHost(user17.getuserId(), host.getId());
			
			host.setTotalProfit(totalProfit);
			
			broker17.setHostList(hostlist1);
			
			cloudletid = 17;
			Cloudlet cloudlet17 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
			cloudlet17.setbrokerId(broker17Id);
			//update the price of cloudlet;
			//price = 1700;
			//cloudlet17.setprice(price);
			cloudlet17.setuser(user17);
			user17.setCloudletId(cloudlet17.getCloudletId());
			
			// add the cloudlet to the list
			cloudletList.add(cloudlet17);

			// submit cloudlet list to the broker
			broker17.submitCloudletList(cloudletList);
			
			//adrian
			numtime = duration[17];
			res = resask[17];
			user17.submitCloudlet(cloudlet17);
			user17.submitRequest(user17.getCloudlet(), false, res, res, (long)(res), numtime);
			relinp.clear();
			host.getmtimes().put(userid, 0);
			for(int i = 0 ; i < 150; i++){
				double a = r.nextGaussian() * 0.3 + aveRP[17];
				if (a < 0 || a > 1){}
				else{
					relinp.add(a);
				}
			}
			if(aveRP[17] == 0){}
			else if(aveRP[17] > 0){
				
				ArrayList<Double> se17 = new ArrayList<Double> ();
				ArrayList<Double> de17 = new ArrayList<Double> ();
				host.getmaop().put(userid, se17);
				host.getmsop().put(userid, de17);
				for(int i = 1; i < 51; i++){
					host.addmaop(userid, relinp.get(i));
					host.addmsop(userid, relinp.get(i));
					host.addmtimes(userid);
				}
			}
			relinp.set(0,0.6855749421501627);
			basicprice = user17.getnumCPU() * 0.01 + user17.getnumRAM() * 0.01 + (double)(user17.getnumStorage()*0.001);
			pledge = basicprice * 1.05;
			System.out.println("user" + user17.getuserId() + " asks for " + user17.getnumCPU() + " CPUs and " + user17.getnumRAM() + " GB of RAM and " + user17.getnumStorage() + " GB of storage to host" + host.getId());

			utiliz = CalUti(timestamp[17], acpu, uti, expiretime, utireal);
			id = 0;
			if(cloudlet17.pledge == true){
				//here I didn't consider giving back the extra money if the user will not relinquish any resource;
				payprice = pledge;
			}
			else{
				id = DecideModel(utiliz, aveRP[17], userid, res);
				id = 1;
				//host.minusmtimes(userid);
				//here remember the BROKER is changing to BROKER2!!!!!!!!! and cloudlet is cloudlet2!!!!!!!!!
				if (host.getmtimes().get(userid) == 0){
					host.addmtimes(userid);
					ArrayList<Double> aaa = new ArrayList<Double> ();
					ArrayList<Double> bbb = new ArrayList<Double> ();
					//here I will set some default values for aop and sop and ...;
					if(host.getmaop().get(userid) == null){
					broker17.CalNumOfRes(user17.getuserId(), host.getId(), 0.3, 0.3, id);
					//right now we don't consider the pricing model;
					//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
					host.getmaop().put(userid, bbb);
					}
					else {
						broker17.CalNumOfRes(user17.getuserId(), host.getId(), 0.3, id);
						//right now we don't consider the pricing model;
						//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
					}
					host.getmsop().put(userid, aaa);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
				}
				else if(host.getmtimes().get(userid) == 1){
					host.addmtimes(userid);
					// will add codes for sop and aop and ...;
					broker17.CalNumOfRes1(user17.getuserId(), host.getId(), id);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
					//payprice = broker2.CalPrice(cloudlet2.getCloudletId(), host.getId());
				}
				else if(host.getmtimes().get(userid) == 2){
					host.addmtimes(userid);
					// will add codes for sop and aop and ...;
					broker17.CalNumOfRes2(user17.getuserId(), host.getId(), id);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
					//payprice = broker17.CalPrice(cloudlet17.getCloudletId(), host.getId());
				}
				else if(host.getmtimes().get(userid) > 2){
					broker17.CalNumOfRes(user17.getuserId(), host.getId(), id);
					int a = host.getmtimes().get(userid);
					host.addmtimes(userid);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
					
				}
			}

			
			acpu.add(user17.getnumCPU());
			aram.add(user17.getnumRAM());
			asto.add(user17.getnumStorage());
			utimeh = host.getmtimes().get(userid) ;
			System.out.println("this is the " + utimeh + "th time for user" + user17.getuserId() + " to use services of host" + host.getId());
			payprice = user17.getnumCPU() * 0.01 + user17.getnumRAM() * 0.01 + (double)(user17.getnumStorage()*0.001);
			vmid = 17;
			
			Vm vm17 = new Vm(vmid, broker17Id, mips, user17.getnumCPU(), user17.getnumRAM(), bw, user17.getnumStorage(), vmm, new CloudletSchedulerTimeShared());
			
			vm17.setHost(host);
			
			//int vmid2 = 1;
			//Vm vm2 = new Vm(vmid2, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
			// add the VM to the vmList
			vmlist.add(vm17);
			//vmlist.add(vm2);
			duration[17] = (int)(duration[17]*(1-relinp.get(0)));
			expiretime[17] = timestamp[17] + duration[17];
			CalUtinew(timestamp[17], acpu, uti, utiliz, utireal);
			// submit vm list to the broker
			broker17.submitVmList(vmlist);
			
			broker17.bindCloudletToVm(cloudletid, vmid);
			
			payprice = payprice * user17.gettime();
			
			reimbursement = payprice * bb;
			// Update the records of reimbursement and payment.
			reimburse.add(reimbursement);
			System.out.println(host.getmtimes().get(userid));
			payment.add(payprice);
			
			
			
			
			CloudSim.startSimulation();
			////////////////////
			////////////////
			CloudSim.init(num_user, calendar, trace_flag);

			@SuppressWarnings("unused")
			Datacenter datacenter18 = createDatacenter("Datacenter_1");

			DatacenterBroker broker18 = createBroker();
			int broker18Id = broker18.getId();
			
			//update totalProfit;
			totalProfit += payprice;
			userid = 18;
			User user18 = new User(userid);
			userlist.add(user18);
			
			broker18.submitUserList(userlist);
			broker18.BindUserToHost(user18.getuserId(), host.getId());
			
			host.setTotalProfit(totalProfit);
			
			broker18.setHostList(hostlist1);
			
			cloudletid = 18;
			Cloudlet cloudlet18 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
			cloudlet18.setbrokerId(broker18Id);
			//update the price of cloudlet;
			//price = 1800;
			//cloudlet18.setprice(price);
			cloudlet18.setuser(user18);
			user18.setCloudletId(cloudlet18.getCloudletId());
			
			// add the cloudlet to the list
			cloudletList.add(cloudlet18);

			// submit cloudlet list to the broker
			broker18.submitCloudletList(cloudletList);
			
			//adrian
			numtime = duration[18];
			res = resask[18];
			user18.submitCloudlet(cloudlet18);
			user18.submitRequest(user18.getCloudlet(), false, res, res, (long)(res), numtime);
			relinp.clear();
			host.getmtimes().put(userid, 0);
			for(int i = 0 ; i < 100; i++){
				double a = r.nextGaussian() * 0.3 + aveRP[18];
				if (a < 0 || a > 1){}
				else{
					relinp.add(a);
				}
			}
			if(aveRP[18] == 0){}
			else if(aveRP[18] > 0){
				
				ArrayList<Double> se18 = new ArrayList<Double> ();
				ArrayList<Double> de18 = new ArrayList<Double> ();
				host.getmaop().put(userid, se18);
				host.getmsop().put(userid, de18);
				for(int i = 1; i < 51; i++){
					host.addmaop(userid, relinp.get(i));
					host.addmsop(userid, relinp.get(i));
					host.addmtimes(userid);
				}
			}
			relinp.set(0,0.43705027280002506);
			basicprice = user18.getnumCPU() * 0.01 + user18.getnumRAM() * 0.01 + (double)(user18.getnumStorage()*0.001);
			pledge = basicprice * 1.05;
			System.out.println("user" + user18.getuserId() + " asks for " + user18.getnumCPU() + " CPUs and " + user18.getnumRAM() + " GB of RAM and " + user18.getnumStorage() + " GB of storage to host" + host.getId());

			utiliz = CalUti(timestamp[18], acpu, uti, expiretime, utireal);
			id = 0;
			if(cloudlet18.pledge == true){
				//here I didn't consider giving back the extra money if the user will not relinquish any resource;
				payprice = pledge;
			}
			else{
				id = DecideModel(utiliz, aveRP[18], userid, res);
				id = 1;
				//host.minusmtimes(userid);
				//here remember the BROKER is changing to BROKER2!!!!!!!!! and cloudlet is cloudlet2!!!!!!!!!
				if (host.getmtimes().get(userid) == 0){
					host.addmtimes(userid);
					ArrayList<Double> aaa = new ArrayList<Double> ();
					ArrayList<Double> bbb = new ArrayList<Double> ();
					//here I will set some default values for aop and sop and ...;
					if(host.getmaop().get(userid) == null){
					broker18.CalNumOfRes(user18.getuserId(), host.getId(), 0.3, 0.3, id);
					//right now we don't consider the pricing model;
					//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
					host.getmaop().put(userid, bbb);
					}
					else {
						broker18.CalNumOfRes(user18.getuserId(), host.getId(), 0.3, id);
						//right now we don't consider the pricing model;
						//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
					}
					host.getmsop().put(userid, aaa);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
				}
				else if(host.getmtimes().get(userid) == 1){
					host.addmtimes(userid);
					// will add codes for sop and aop and ...;
					broker18.CalNumOfRes1(user18.getuserId(), host.getId(), id);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
					//payprice = broker2.CalPrice(cloudlet2.getCloudletId(), host.getId());
				}
				else if(host.getmtimes().get(userid) == 2){
					host.addmtimes(userid);
					// will add codes for sop and aop and ...;
					broker18.CalNumOfRes2(user18.getuserId(), host.getId(), id);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
					//payprice = broker18.CalPrice(cloudlet18.getCloudletId(), host.getId());
				}
				else if(host.getmtimes().get(userid) > 2){
					broker18.CalNumOfRes(user18.getuserId(), host.getId(), id);
					int a = host.getmtimes().get(userid);
					host.addmtimes(userid);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
					
				}
			}

			
			acpu.add(user18.getnumCPU());
			aram.add(user18.getnumRAM());
			asto.add(user18.getnumStorage());
			utimeh = host.getmtimes().get(userid) ;
			System.out.println("this is the " + utimeh + "th time for user" + user18.getuserId() + " to use services of host" + host.getId());
			payprice = user18.getnumCPU() * 0.01 + user18.getnumRAM() * 0.01 + (double)(user18.getnumStorage()*0.001);
			vmid = 18;
			
			Vm vm18 = new Vm(vmid, broker18Id, mips, user18.getnumCPU(), user18.getnumRAM(), bw, user18.getnumStorage(), vmm, new CloudletSchedulerTimeShared());
			
			vm18.setHost(host);
			
			//int vmid2 = 1;
			//Vm vm2 = new Vm(vmid2, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
			// add the VM to the vmList
			vmlist.add(vm18);
			//vmlist.add(vm2);
			duration[18] = (int)(duration[18]*(1-relinp.get(0)));
			expiretime[18] = timestamp[18] + duration[18];
			CalUtinew(timestamp[18], acpu, uti, utiliz, utireal);
			// submit vm list to the broker
			broker18.submitVmList(vmlist);
			
			broker18.bindCloudletToVm(cloudletid, vmid);
			
			payprice = payprice * user18.gettime();
			
			reimbursement = payprice * bb;
			// Update the records of reimbursement and payment.
			reimburse.add(reimbursement);
			System.out.println(host.getmtimes().get(userid));
			payment.add(payprice);
			
			
			
			
			CloudSim.startSimulation();
			/////////////////////
			///////////////////
			CloudSim.init(num_user, calendar, trace_flag);

			@SuppressWarnings("unused")
			Datacenter datacenter19 = createDatacenter("Datacenter_1");

			DatacenterBroker broker19 = createBroker();
			int broker19Id = broker19.getId();
			
			//update totalProfit;
			totalProfit += payprice;
			userid = 19;
			User user19 = new User(userid);
			userlist.add(user19);
			
			broker19.submitUserList(userlist);
			broker19.BindUserToHost(user19.getuserId(), host.getId());
			
			host.setTotalProfit(totalProfit);
			
			broker19.setHostList(hostlist1);
			
			cloudletid = 19;
			Cloudlet cloudlet19 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
			cloudlet19.setbrokerId(broker19Id);
			//update the price of cloudlet;
			//price = 1900;
			//cloudlet19.setprice(price);
			cloudlet19.setuser(user19);
			user19.setCloudletId(cloudlet19.getCloudletId());
			
			// add the cloudlet to the list
			cloudletList.add(cloudlet19);

			// submit cloudlet list to the broker
			broker19.submitCloudletList(cloudletList);
			
			//adrian
			numtime = duration[19];
			res = resask[19];
			user19.submitCloudlet(cloudlet19);
			user19.submitRequest(user19.getCloudlet(), false, res, res, (long)(res), numtime);
			relinp.clear();
			host.getmtimes().put(userid, 0);
			for(int i = 0 ; i < 100; i++){
				double a = r.nextGaussian() * 0.3 + aveRP[19];
				if (a < 0 || a > 1){}
				else{
					relinp.add(a);
				}
			}
			if(aveRP[19] == 0){}
			else if(aveRP[19] > 0){
				
				ArrayList<Double> se19 = new ArrayList<Double> ();
				ArrayList<Double> de19 = new ArrayList<Double> ();
				host.getmaop().put(userid, se19);
				host.getmsop().put(userid, de19);
				for(int i = 1; i < 51; i++){
					host.addmaop(userid, relinp.get(i));
					host.addmsop(userid, relinp.get(i));
					host.addmtimes(userid);
				}
			}
			relinp.set(0,0.759289226467618);
			basicprice = user19.getnumCPU() * 0.01 + user19.getnumRAM() * 0.01 + (double)(user19.getnumStorage()*0.001);
			pledge = basicprice * 1.05;
			System.out.println("user" + user19.getuserId() + " asks for " + user19.getnumCPU() + " CPUs and " + user19.getnumRAM() + " GB of RAM and " + user19.getnumStorage() + " GB of storage to host" + host.getId());

			utiliz = CalUti(timestamp[19], acpu, uti, expiretime, utireal);
			id = 0;
			if(cloudlet19.pledge == true){
				//here I didn't consider giving back the extra money if the user will not relinquish any resource;
				payprice = pledge;
			}
			else{
				id = DecideModel(utiliz, aveRP[19], userid, res);
				id = 1;
				//host.minusmtimes(userid);
				//here remember the BROKER is changing to BROKER2!!!!!!!!! and cloudlet is cloudlet2!!!!!!!!!
				if (host.getmtimes().get(userid) == 0){
					host.addmtimes(userid);
					ArrayList<Double> aaa = new ArrayList<Double> ();
					ArrayList<Double> bbb = new ArrayList<Double> ();
					//here I will set some default values for aop and sop and ...;
					if(host.getmaop().get(userid) == null){
					broker19.CalNumOfRes(user19.getuserId(), host.getId(), 0.3, 0.3, id);
					//right now we don't consider the pricing model;
					//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
					host.getmaop().put(userid, bbb);
					}
					else {
						broker19.CalNumOfRes(user19.getuserId(), host.getId(), 0.3, id);
						//right now we don't consider the pricing model;
						//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
					}
					host.getmsop().put(userid, aaa);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
				}
				else if(host.getmtimes().get(userid) == 1){
					host.addmtimes(userid);
					// will add codes for sop and aop and ...;
					broker19.CalNumOfRes1(user19.getuserId(), host.getId(), id);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
					//payprice = broker2.CalPrice(cloudlet2.getCloudletId(), host.getId());
				}
				else if(host.getmtimes().get(userid) == 2){
					host.addmtimes(userid);
					// will add codes for sop and aop and ...;
					broker19.CalNumOfRes2(user19.getuserId(), host.getId(), id);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
					//payprice = broker19.CalPrice(cloudlet19.getCloudletId(), host.getId());
				}
				else if(host.getmtimes().get(userid) > 2){
					broker19.CalNumOfRes(user19.getuserId(), host.getId(), id);
					int a = host.getmtimes().get(userid);
					host.addmtimes(userid);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
					
				}
			}

			
			acpu.add(user19.getnumCPU());
			aram.add(user19.getnumRAM());
			asto.add(user19.getnumStorage());
			utimeh = host.getmtimes().get(userid) ;
			System.out.println("this is the " + utimeh + "th time for user" + user19.getuserId() + " to use services of host" + host.getId());
			payprice = user19.getnumCPU() * 0.01 + user19.getnumRAM() * 0.01 + (double)(user19.getnumStorage()*0.001);
			vmid = 19;
			
			Vm vm19 = new Vm(vmid, broker19Id, mips, user19.getnumCPU(), user19.getnumRAM(), bw, user19.getnumStorage(), vmm, new CloudletSchedulerTimeShared());
			
			vm19.setHost(host);
			
			//int vmid2 = 1;
			//Vm vm2 = new Vm(vmid2, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
			// add the VM to the vmList
			vmlist.add(vm19);
			//vmlist.add(vm2);
			duration[19] = (int)(duration[19]*(1-relinp.get(0)));
			expiretime[19] = timestamp[19] + duration[19];
			CalUtinew(timestamp[19], acpu, uti, utiliz, utireal);
			// submit vm list to the broker
			broker19.submitVmList(vmlist);
			
			broker19.bindCloudletToVm(cloudletid, vmid);
			
			payprice = payprice * user19.gettime();
			
			reimbursement = payprice * bb;
			// Update the records of reimbursement and payment.
			reimburse.add(reimbursement);
			System.out.println(host.getmtimes().get(userid));
			payment.add(payprice);

			CloudSim.startSimulation();
			///////////////////////
			////////////////

			///////////////////////////////////////////
			////////////////////////////////////////////
/////////////////////
	///////////////////
	CloudSim.init(num_user, calendar, trace_flag);

	@SuppressWarnings("unused")
	Datacenter datacenter20 = createDatacenter("Datacenter_1");

	DatacenterBroker broker20 = createBroker();
	int broker20Id = broker20.getId();
	
	//update totalProfit;
	totalProfit += payprice;
	userid = 20;
	User user20 = new User(userid);
	userlist.add(user20);
	
	broker20.submitUserList(userlist);
	broker20.BindUserToHost(user20.getuserId(), host.getId());
	
	host.setTotalProfit(totalProfit);
	
	broker20.setHostList(hostlist1);
	
	cloudletid = 20;
	Cloudlet cloudlet20 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
	cloudlet20.setbrokerId(broker20Id);
	//update the price of cloudlet;
	//price = 2000;
	//cloudlet20.setprice(price);
	cloudlet20.setuser(user20);
	user20.setCloudletId(cloudlet20.getCloudletId());
	
	// add the cloudlet to the list
	cloudletList.add(cloudlet20);

	// submit cloudlet list to the broker
	broker20.submitCloudletList(cloudletList);
	
	//adrian
	numtime = duration[20];
	res = resask[20];
	user20.submitCloudlet(cloudlet20);
	user20.submitRequest(user20.getCloudlet(), false, res, res, (long)(res), numtime);
	relinp.clear();
	host.getmtimes().put(userid, 0);
	for(int i = 0 ; i < 100; i++){
		double a = r.nextGaussian() * 0.3 + aveRP[20];
		if (a < 0 || a > 1){}
		else{
			relinp.add(a);
		}
	}
	if(aveRP[20] == 0){}
	else if(aveRP[20] > 0){
		
		ArrayList<Double> se20 = new ArrayList<Double> ();
		ArrayList<Double> de20 = new ArrayList<Double> ();
		host.getmaop().put(userid, se20);
		host.getmsop().put(userid, de20);
		for(int i = 1; i < 51; i++){
			host.addmaop(userid, relinp.get(i));
			host.addmsop(userid, relinp.get(i));
			host.addmtimes(userid);
		}
	}
	relinp.set(0,0.759289226467618);
	basicprice = user20.getnumCPU() * 0.01 + user20.getnumRAM() * 0.01 + (double)(user20.getnumStorage()*0.001);
	pledge = basicprice * 1.05;
	System.out.println("user" + user20.getuserId() + " asks for " + user20.getnumCPU() + " CPUs and " + user20.getnumRAM() + " GB of RAM and " + user20.getnumStorage() + " GB of storage to host" + host.getId());

	utiliz = CalUti(timestamp[20], acpu, uti, expiretime, utireal);
	id = 0;
	if(cloudlet20.pledge == true){
		//here I didn't consider giving back the extra money if the user will not relinquish any resource;
		payprice = pledge;
	}
	else{
		id = DecideModel(utiliz, aveRP[20], userid, res);
		id = 1;
		//host.minusmtimes(userid);
		//here remember the BROKER is changing to BROKER2!!!!!!!!! and cloudlet is cloudlet2!!!!!!!!!
		if (host.getmtimes().get(userid) == 0){
			host.addmtimes(userid);
			ArrayList<Double> aaa = new ArrayList<Double> ();
			ArrayList<Double> bbb = new ArrayList<Double> ();
			//here I will set some default values for aop and sop and ...;
			if(host.getmaop().get(userid) == null){
			broker20.CalNumOfRes(user20.getuserId(), host.getId(), 0.3, 0.3, id);
			//right now we don't consider the pricing model;
			//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
			host.getmaop().put(userid, bbb);
			}
			else {
				broker20.CalNumOfRes(user20.getuserId(), host.getId(), 0.3, id);
				//right now we don't consider the pricing model;
				//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
			}
			host.getmsop().put(userid, aaa);
			host.addmsop(userid, relinp.get(0));
			host.addmaop(userid, relinp.get(0));
		}
		else if(host.getmtimes().get(userid) == 1){
			host.addmtimes(userid);
			// will add codes for sop and aop and ...;
			broker20.CalNumOfRes1(user20.getuserId(), host.getId(), id);
			host.addmsop(userid, relinp.get(0));
			host.addmaop(userid, relinp.get(0));
			//payprice = broker2.CalPrice(cloudlet2.getCloudletId(), host.getId());
		}
		else if(host.getmtimes().get(userid) == 2){
			host.addmtimes(userid);
			// will add codes for sop and aop and ...;
			broker20.CalNumOfRes2(user20.getuserId(), host.getId(), id);
			host.addmsop(userid, relinp.get(0));
			host.addmaop(userid, relinp.get(0));
			//payprice = broker20.CalPrice(cloudlet20.getCloudletId(), host.getId());
		}
		else if(host.getmtimes().get(userid) > 2){
			broker20.CalNumOfRes(user20.getuserId(), host.getId(), id);
			int a = host.getmtimes().get(userid);
			host.addmtimes(userid);
			host.addmsop(userid, relinp.get(0));
			host.addmaop(userid, relinp.get(0));
			
		}
	}

	
	acpu.add(user20.getnumCPU());
	aram.add(user20.getnumRAM());
	asto.add(user20.getnumStorage());
	utimeh = host.getmtimes().get(userid) ;
	System.out.println("this is the " + utimeh + "th time for user" + user20.getuserId() + " to use services of host" + host.getId());
	payprice = user20.getnumCPU() * 0.01 + user20.getnumRAM() * 0.01 + (double)(user20.getnumStorage()*0.001);
	vmid = 20;
	
	Vm vm20 = new Vm(vmid, broker20Id, mips, user20.getnumCPU(), user20.getnumRAM(), bw, user20.getnumStorage(), vmm, new CloudletSchedulerTimeShared());
	
	vm20.setHost(host);
	
	//int vmid2 = 1;
	//Vm vm2 = new Vm(vmid2, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
	// add the VM to the vmList
	vmlist.add(vm20);
	//vmlist.add(vm2);
	duration[20] = (int)(duration[20]*(1-relinp.get(0)));
	expiretime[20] = timestamp[20] + duration[20];
	CalUtinew(timestamp[20], acpu, uti, utiliz, utireal);
	// submit vm list to the broker
	broker20.submitVmList(vmlist);
	
	broker20.bindCloudletToVm(cloudletid, vmid);
	
	payprice = payprice * user20.gettime();
	
	reimbursement = payprice * bb;
	// Update the records of reimbursement and payment.
	reimburse.add(reimbursement);
	System.out.println(host.getmtimes().get(userid));
	payment.add(payprice);

	CloudSim.startSimulation();
/////////////////////
///////////////////
CloudSim.init(num_user, calendar, trace_flag);

@SuppressWarnings("unused")
Datacenter datacenter21 = createDatacenter("Datacenter_1");

DatacenterBroker broker21 = createBroker();
int broker21Id = broker21.getId();

//update totalProfit;
totalProfit += payprice;
userid = 21;
User user21 = new User(userid);
userlist.add(user21);

broker21.submitUserList(userlist);
broker21.BindUserToHost(user21.getuserId(), host.getId());

host.setTotalProfit(totalProfit);

broker21.setHostList(hostlist1);

cloudletid = 21;
Cloudlet cloudlet21 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
cloudlet21.setbrokerId(broker21Id);
//update the price of cloudlet;
//price = 2100;
//cloudlet21.setprice(price);
cloudlet21.setuser(user21);
user21.setCloudletId(cloudlet21.getCloudletId());

// add the cloudlet to the list
cloudletList.add(cloudlet21);

// submit cloudlet list to the broker
broker21.submitCloudletList(cloudletList);

//adrian
numtime = duration[21];
res = resask[21];
user21.submitCloudlet(cloudlet21);
user21.submitRequest(user21.getCloudlet(), false, res, res, (long)(res), numtime);
relinp.clear();
host.getmtimes().put(userid, 0);
for(int i = 0 ; i < 100; i++){
double a = r.nextGaussian() * 0.3 + aveRP[21];
if (a < 0 || a > 1){}
else{
	relinp.add(a);
}
}
if(aveRP[21] == 0){}
else if(aveRP[21] > 0){

ArrayList<Double> se21 = new ArrayList<Double> ();
ArrayList<Double> de21 = new ArrayList<Double> ();
host.getmaop().put(userid, se21);
host.getmsop().put(userid, de21);
for(int i = 1; i < 51; i++){
	host.addmaop(userid, relinp.get(i));
	host.addmsop(userid, relinp.get(i));
	host.addmtimes(userid);
}
}
relinp.set(0,0.759289226467618);
basicprice = user21.getnumCPU() * 0.01 + user21.getnumRAM() * 0.01 + (double)(user21.getnumStorage()*0.001);
pledge = basicprice * 1.05;
System.out.println("user" + user21.getuserId() + " asks for " + user21.getnumCPU() + " CPUs and " + user21.getnumRAM() + " GB of RAM and " + user21.getnumStorage() + " GB of storage to host" + host.getId());

utiliz = CalUti(timestamp[21], acpu, uti, expiretime, utireal);
id = 0;
if(cloudlet21.pledge == true){
//here I didn't consider giving back the extra money if the user will not relinquish any resource;
payprice = pledge;
}
else{
id = DecideModel(utiliz, aveRP[21], userid, res);
id = 1;
//host.minusmtimes(userid);
//here remember the BROKER is changing to BROKER2!!!!!!!!! and cloudlet is cloudlet2!!!!!!!!!
if (host.getmtimes().get(userid) == 0){
	host.addmtimes(userid);
	ArrayList<Double> aaa = new ArrayList<Double> ();
	ArrayList<Double> bbb = new ArrayList<Double> ();
	//here I will set some default values for aop and sop and ...;
	if(host.getmaop().get(userid) == null){
	broker21.CalNumOfRes(user21.getuserId(), host.getId(), 0.3, 0.3, id);
	//right now we don't consider the pricing model;
	//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
	host.getmaop().put(userid, bbb);
	}
	else {
		broker21.CalNumOfRes(user21.getuserId(), host.getId(), 0.3, id);
		//right now we don't consider the pricing model;
		//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
	}
	host.getmsop().put(userid, aaa);
	host.addmsop(userid, relinp.get(0));
	host.addmaop(userid, relinp.get(0));
}
else if(host.getmtimes().get(userid) == 1){
	host.addmtimes(userid);
	// will add codes for sop and aop and ...;
	broker21.CalNumOfRes1(user21.getuserId(), host.getId(), id);
	host.addmsop(userid, relinp.get(0));
	host.addmaop(userid, relinp.get(0));
	//payprice = broker2.CalPrice(cloudlet2.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) == 2){
	host.addmtimes(userid);
	// will add codes for sop and aop and ...;
	broker21.CalNumOfRes2(user21.getuserId(), host.getId(), id);
	host.addmsop(userid, relinp.get(0));
	host.addmaop(userid, relinp.get(0));
	//payprice = broker21.CalPrice(cloudlet21.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) > 2){
	broker21.CalNumOfRes(user21.getuserId(), host.getId(), id);
	int a = host.getmtimes().get(userid);
	host.addmtimes(userid);
	host.addmsop(userid, relinp.get(0));
	host.addmaop(userid, relinp.get(0));
	
}
}


acpu.add(user21.getnumCPU());
aram.add(user21.getnumRAM());
asto.add(user21.getnumStorage());
utimeh = host.getmtimes().get(userid) ;
System.out.println("this is the " + utimeh + "th time for user" + user21.getuserId() + " to use services of host" + host.getId());
payprice = user21.getnumCPU() * 0.01 + user21.getnumRAM() * 0.01 + (double)(user21.getnumStorage()*0.001);
vmid = 21;

Vm vm21 = new Vm(vmid, broker21Id, mips, user21.getnumCPU(), user21.getnumRAM(), bw, user21.getnumStorage(), vmm, new CloudletSchedulerTimeShared());

vm21.setHost(host);

//int vmid2 = 1;
//Vm vm2 = new Vm(vmid2, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
// add the VM to the vmList
vmlist.add(vm21);
//vmlist.add(vm2);
duration[21] = (int)(duration[21]*(1-relinp.get(0)));
expiretime[21] = timestamp[21] + duration[21];
CalUtinew(timestamp[21], acpu, uti, utiliz, utireal);
// submit vm list to the broker
broker21.submitVmList(vmlist);

broker21.bindCloudletToVm(cloudletid, vmid);

payprice = payprice * user21.gettime();

reimbursement = payprice * bb;
// Update the records of reimbursement and payment.
reimburse.add(reimbursement);
System.out.println(host.getmtimes().get(userid));
payment.add(payprice);

CloudSim.startSimulation();
/////////////////////
///////////////////
CloudSim.init(num_user, calendar, trace_flag);

@SuppressWarnings("unused")
Datacenter datacenter22 = createDatacenter("Datacenter_1");

DatacenterBroker broker22 = createBroker();
int broker22Id = broker22.getId();

//update totalProfit;
totalProfit += payprice;
userid = 22;
User user22 = new User(userid);
userlist.add(user22);

broker22.submitUserList(userlist);
broker22.BindUserToHost(user22.getuserId(), host.getId());

host.setTotalProfit(totalProfit);

broker22.setHostList(hostlist1);

cloudletid = 22;
Cloudlet cloudlet22 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
cloudlet22.setbrokerId(broker22Id);
//update the price of cloudlet;
//price = 2200;
//cloudlet22.setprice(price);
cloudlet22.setuser(user22);
user22.setCloudletId(cloudlet22.getCloudletId());

// add the cloudlet to the list
cloudletList.add(cloudlet22);

// submit cloudlet list to the broker
broker22.submitCloudletList(cloudletList);

//adrian
numtime = duration[22];
res = resask[22];
user22.submitCloudlet(cloudlet22);
user22.submitRequest(user22.getCloudlet(), false, res, res, (long)(res), numtime);
relinp.clear();
host.getmtimes().put(userid, 0);
for(int i = 0 ; i < 100; i++){
double a = r.nextGaussian() * 0.3 + aveRP[22];
if (a < 0 || a > 1){}
else{
relinp.add(a);
}
}
if(aveRP[22] == 0){}
else if(aveRP[22] > 0){

ArrayList<Double> se22 = new ArrayList<Double> ();
ArrayList<Double> de22 = new ArrayList<Double> ();
host.getmaop().put(userid, se22);
host.getmsop().put(userid, de22);
for(int i = 1; i < 51; i++){
host.addmaop(userid, relinp.get(i));
host.addmsop(userid, relinp.get(i));
host.addmtimes(userid);
}
}
relinp.set(0,0.759289226467618);
basicprice = user22.getnumCPU() * 0.01 + user22.getnumRAM() * 0.01 + (double)(user22.getnumStorage()*0.001);
pledge = basicprice * 1.05;
System.out.println("user" + user22.getuserId() + " asks for " + user22.getnumCPU() + " CPUs and " + user22.getnumRAM() + " GB of RAM and " + user22.getnumStorage() + " GB of storage to host" + host.getId());

utiliz = CalUti(timestamp[22], acpu, uti, expiretime, utireal);
id = 0;
if(cloudlet22.pledge == true){
//here I didn't consider giving back the extra money if the user will not relinquish any resource;
payprice = pledge;
}
else{
id = DecideModel(utiliz, aveRP[22], userid, res);
id = 1;
//host.minusmtimes(userid);
//here remember the BROKER is changing to BROKER2!!!!!!!!! and cloudlet is cloudlet2!!!!!!!!!
if (host.getmtimes().get(userid) == 0){
host.addmtimes(userid);
ArrayList<Double> aaa = new ArrayList<Double> ();
ArrayList<Double> bbb = new ArrayList<Double> ();
//here I will set some default values for aop and sop and ...;
if(host.getmaop().get(userid) == null){
broker22.CalNumOfRes(user22.getuserId(), host.getId(), 0.3, 0.3, id);
//right now we don't consider the pricing model;
//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
host.getmaop().put(userid, bbb);
}
else {
	broker22.CalNumOfRes(user22.getuserId(), host.getId(), 0.3, id);
	//right now we don't consider the pricing model;
	//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
}
host.getmsop().put(userid, aaa);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));
}
else if(host.getmtimes().get(userid) == 1){
host.addmtimes(userid);
// will add codes for sop and aop and ...;
broker22.CalNumOfRes1(user22.getuserId(), host.getId(), id);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));
//payprice = broker2.CalPrice(cloudlet2.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) == 2){
host.addmtimes(userid);
// will add codes for sop and aop and ...;
broker22.CalNumOfRes2(user22.getuserId(), host.getId(), id);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));
//payprice = broker22.CalPrice(cloudlet22.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) > 2){
broker22.CalNumOfRes(user22.getuserId(), host.getId(), id);
int a = host.getmtimes().get(userid);
host.addmtimes(userid);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));

}
}


acpu.add(user22.getnumCPU());
aram.add(user22.getnumRAM());
asto.add(user22.getnumStorage());
utimeh = host.getmtimes().get(userid) ;
System.out.println("this is the " + utimeh + "th time for user" + user22.getuserId() + " to use services of host" + host.getId());
payprice = user22.getnumCPU() * 0.01 + user22.getnumRAM() * 0.01 + (double)(user22.getnumStorage()*0.001);
vmid = 22;

Vm vm22 = new Vm(vmid, broker22Id, mips, user22.getnumCPU(), user22.getnumRAM(), bw, user22.getnumStorage(), vmm, new CloudletSchedulerTimeShared());

vm22.setHost(host);

//int vmid2 = 1;
//Vm vm2 = new Vm(vmid2, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
// add the VM to the vmList
vmlist.add(vm22);
//vmlist.add(vm2);
duration[22] = (int)(duration[22]*(1-relinp.get(0)));
expiretime[22] = timestamp[22] + duration[22];
CalUtinew(timestamp[22], acpu, uti, utiliz, utireal);
// submit vm list to the broker
broker22.submitVmList(vmlist);

broker22.bindCloudletToVm(cloudletid, vmid);

payprice = payprice * user22.gettime();

reimbursement = payprice * bb;
// Update the records of reimbursement and payment.
reimburse.add(reimbursement);
System.out.println(host.getmtimes().get(userid));
payment.add(payprice);

CloudSim.startSimulation();
/////////////////////
///////////////////
CloudSim.init(num_user, calendar, trace_flag);

@SuppressWarnings("unused")
Datacenter datacenter23 = createDatacenter("Datacenter_1");

DatacenterBroker broker23 = createBroker();
int broker23Id = broker23.getId();

//update totalProfit;
totalProfit += payprice;
userid = 23;
User user23 = new User(userid);
userlist.add(user23);

broker23.submitUserList(userlist);
broker23.BindUserToHost(user23.getuserId(), host.getId());

host.setTotalProfit(totalProfit);

broker23.setHostList(hostlist1);

cloudletid = 23;
Cloudlet cloudlet23 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
cloudlet23.setbrokerId(broker23Id);
//update the price of cloudlet;
//price = 2300;
//cloudlet23.setprice(price);
cloudlet23.setuser(user23);
user23.setCloudletId(cloudlet23.getCloudletId());

// add the cloudlet to the list
cloudletList.add(cloudlet23);

// submit cloudlet list to the broker
broker23.submitCloudletList(cloudletList);

//adrian
numtime = duration[23];
res = resask[23];
user23.submitCloudlet(cloudlet23);
user23.submitRequest(user23.getCloudlet(), false, res, res, (long)(res), numtime);
relinp.clear();
host.getmtimes().put(userid, 0);
for(int i = 0 ; i < 100; i++){
double a = r.nextGaussian() * 0.3 + aveRP[23];
if (a < 0 || a > 1){}
else{
relinp.add(a);
}
}
if(aveRP[23] == 0){}
else if(aveRP[23] > 0){

ArrayList<Double> se23 = new ArrayList<Double> ();
ArrayList<Double> de23 = new ArrayList<Double> ();
host.getmaop().put(userid, se23);
host.getmsop().put(userid, de23);
for(int i = 1; i < 51; i++){
host.addmaop(userid, relinp.get(i));
host.addmsop(userid, relinp.get(i));
host.addmtimes(userid);
}
}
relinp.set(0,0.759289226467618);
basicprice = user23.getnumCPU() * 0.01 + user23.getnumRAM() * 0.01 + (double)(user23.getnumStorage()*0.001);
pledge = basicprice * 1.05;
System.out.println("user" + user23.getuserId() + " asks for " + user23.getnumCPU() + " CPUs and " + user23.getnumRAM() + " GB of RAM and " + user23.getnumStorage() + " GB of storage to host" + host.getId());

utiliz = CalUti(timestamp[23], acpu, uti, expiretime, utireal);
id = 0;
if(cloudlet23.pledge == true){
//here I didn't consider giving back the extra money if the user will not relinquish any resource;
payprice = pledge;
}
else{
id = DecideModel(utiliz, aveRP[23], userid, res);
id = 1;
//host.minusmtimes(userid);
//here remember the BROKER is changing to BROKER2!!!!!!!!! and cloudlet is cloudlet2!!!!!!!!!
if (host.getmtimes().get(userid) == 0){
host.addmtimes(userid);
ArrayList<Double> aaa = new ArrayList<Double> ();
ArrayList<Double> bbb = new ArrayList<Double> ();
//here I will set some default values for aop and sop and ...;
if(host.getmaop().get(userid) == null){
broker23.CalNumOfRes(user23.getuserId(), host.getId(), 0.3, 0.3, id);
//right now we don't consider the pricing model;
//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
host.getmaop().put(userid, bbb);
}
else {
	broker23.CalNumOfRes(user23.getuserId(), host.getId(), 0.3, id);
	//right now we don't consider the pricing model;
	//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
}
host.getmsop().put(userid, aaa);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));
}
else if(host.getmtimes().get(userid) == 1){
host.addmtimes(userid);
// will add codes for sop and aop and ...;
broker23.CalNumOfRes1(user23.getuserId(), host.getId(), id);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));
//payprice = broker2.CalPrice(cloudlet2.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) == 2){
host.addmtimes(userid);
// will add codes for sop and aop and ...;
broker23.CalNumOfRes2(user23.getuserId(), host.getId(), id);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));
//payprice = broker23.CalPrice(cloudlet23.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) > 2){
broker23.CalNumOfRes(user23.getuserId(), host.getId(), id);
int a = host.getmtimes().get(userid);
host.addmtimes(userid);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));

}
}


acpu.add(user23.getnumCPU());
aram.add(user23.getnumRAM());
asto.add(user23.getnumStorage());
utimeh = host.getmtimes().get(userid) ;
System.out.println("this is the " + utimeh + "th time for user" + user23.getuserId() + " to use services of host" + host.getId());
payprice = user23.getnumCPU() * 0.01 + user23.getnumRAM() * 0.01 + (double)(user23.getnumStorage()*0.001);
vmid = 23;

Vm vm23 = new Vm(vmid, broker23Id, mips, user23.getnumCPU(), user23.getnumRAM(), bw, user23.getnumStorage(), vmm, new CloudletSchedulerTimeShared());

vm23.setHost(host);

//int vmid2 = 1;
//Vm vm2 = new Vm(vmid2, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
// add the VM to the vmList
vmlist.add(vm23);
//vmlist.add(vm2);
duration[23] = (int)(duration[23]*(1-relinp.get(0)));
expiretime[23] = timestamp[23] + duration[23];
CalUtinew(timestamp[23], acpu, uti, utiliz, utireal);
// submit vm list to the broker
broker23.submitVmList(vmlist);

broker23.bindCloudletToVm(cloudletid, vmid);

payprice = payprice * user23.gettime();

reimbursement = payprice * bb;
// Update the records of reimbursement and payment.
reimburse.add(reimbursement);
System.out.println(host.getmtimes().get(userid));
payment.add(payprice);

CloudSim.startSimulation();
/////////////////////
///////////////////
CloudSim.init(num_user, calendar, trace_flag);

@SuppressWarnings("unused")
Datacenter datacenter24 = createDatacenter("Datacenter_1");

DatacenterBroker broker24 = createBroker();
int broker24Id = broker24.getId();

//update totalProfit;
totalProfit += payprice;
userid = 24;
User user24 = new User(userid);
userlist.add(user24);

broker24.submitUserList(userlist);
broker24.BindUserToHost(user24.getuserId(), host.getId());

host.setTotalProfit(totalProfit);

broker24.setHostList(hostlist1);

cloudletid = 24;
Cloudlet cloudlet24 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
cloudlet24.setbrokerId(broker24Id);
//update the price of cloudlet;
//price = 2400;
//cloudlet24.setprice(price);
cloudlet24.setuser(user24);
user24.setCloudletId(cloudlet24.getCloudletId());

// add the cloudlet to the list
cloudletList.add(cloudlet24);

// submit cloudlet list to the broker
broker24.submitCloudletList(cloudletList);

//adrian
numtime = duration[24];
res = resask[24];
user24.submitCloudlet(cloudlet24);
user24.submitRequest(user24.getCloudlet(), false, res, res, (long)(res), numtime);
relinp.clear();
host.getmtimes().put(userid, 0);
for(int i = 0 ; i < 100; i++){
double a = r.nextGaussian() * 0.3 + aveRP[24];
if (a < 0 || a > 1){}
else{
relinp.add(a);
}
}
if(aveRP[24] == 0){}
else if(aveRP[24] > 0){

ArrayList<Double> se24 = new ArrayList<Double> ();
ArrayList<Double> de24 = new ArrayList<Double> ();
host.getmaop().put(userid, se24);
host.getmsop().put(userid, de24);
for(int i = 1; i < 51; i++){
host.addmaop(userid, relinp.get(i));
host.addmsop(userid, relinp.get(i));
host.addmtimes(userid);
}
}
relinp.set(0,0.759289226467618);
basicprice = user24.getnumCPU() * 0.01 + user24.getnumRAM() * 0.01 + (double)(user24.getnumStorage()*0.001);
pledge = basicprice * 1.05;
System.out.println("user" + user24.getuserId() + " asks for " + user24.getnumCPU() + " CPUs and " + user24.getnumRAM() + " GB of RAM and " + user24.getnumStorage() + " GB of storage to host" + host.getId());

utiliz = CalUti(timestamp[24], acpu, uti, expiretime, utireal);
id = 0;
if(cloudlet24.pledge == true){
//here I didn't consider giving back the extra money if the user will not relinquish any resource;
payprice = pledge;
}
else{
id = DecideModel(utiliz, aveRP[24], userid, res);
id = 1;
//host.minusmtimes(userid);
//here remember the BROKER is changing to BROKER2!!!!!!!!! and cloudlet is cloudlet2!!!!!!!!!
if (host.getmtimes().get(userid) == 0){
host.addmtimes(userid);
ArrayList<Double> aaa = new ArrayList<Double> ();
ArrayList<Double> bbb = new ArrayList<Double> ();
//here I will set some default values for aop and sop and ...;
if(host.getmaop().get(userid) == null){
broker24.CalNumOfRes(user24.getuserId(), host.getId(), 0.3, 0.3, id);
//right now we don't consider the pricing model;
//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
host.getmaop().put(userid, bbb);
}
else {
	broker24.CalNumOfRes(user24.getuserId(), host.getId(), 0.3, id);
	//right now we don't consider the pricing model;
	//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
}
host.getmsop().put(userid, aaa);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));
}
else if(host.getmtimes().get(userid) == 1){
host.addmtimes(userid);
// will add codes for sop and aop and ...;
broker24.CalNumOfRes1(user24.getuserId(), host.getId(), id);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));
//payprice = broker2.CalPrice(cloudlet2.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) == 2){
host.addmtimes(userid);
// will add codes for sop and aop and ...;
broker24.CalNumOfRes2(user24.getuserId(), host.getId(), id);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));
//payprice = broker24.CalPrice(cloudlet24.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) > 2){
broker24.CalNumOfRes(user24.getuserId(), host.getId(), id);
int a = host.getmtimes().get(userid);
host.addmtimes(userid);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));

}
}


acpu.add(user24.getnumCPU());
aram.add(user24.getnumRAM());
asto.add(user24.getnumStorage());
utimeh = host.getmtimes().get(userid) ;
System.out.println("this is the " + utimeh + "th time for user" + user24.getuserId() + " to use services of host" + host.getId());
payprice = user24.getnumCPU() * 0.01 + user24.getnumRAM() * 0.01 + (double)(user24.getnumStorage()*0.001);
vmid = 24;

Vm vm24 = new Vm(vmid, broker24Id, mips, user24.getnumCPU(), user24.getnumRAM(), bw, user24.getnumStorage(), vmm, new CloudletSchedulerTimeShared());

vm24.setHost(host);

//int vmid2 = 1;
//Vm vm2 = new Vm(vmid2, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
// add the VM to the vmList
vmlist.add(vm24);
//vmlist.add(vm2);
duration[24] = (int)(duration[24]*(1-relinp.get(0)));
expiretime[24] = timestamp[24] + duration[24];
CalUtinew(timestamp[24], acpu, uti, utiliz, utireal);
// submit vm list to the broker
broker24.submitVmList(vmlist);

broker24.bindCloudletToVm(cloudletid, vmid);

payprice = payprice * user24.gettime();

reimbursement = payprice * bb;
// Update the records of reimbursement and payment.
reimburse.add(reimbursement);
System.out.println(host.getmtimes().get(userid));
payment.add(payprice);

CloudSim.startSimulation();
/////////////////////
///////////////////
CloudSim.init(num_user, calendar, trace_flag);

@SuppressWarnings("unused")
Datacenter datacenter25 = createDatacenter("Datacenter_1");

DatacenterBroker broker25 = createBroker();
int broker25Id = broker25.getId();

//update totalProfit;
totalProfit += payprice;
userid = 25;
User user25 = new User(userid);
userlist.add(user25);

broker25.submitUserList(userlist);
broker25.BindUserToHost(user25.getuserId(), host.getId());

host.setTotalProfit(totalProfit);

broker25.setHostList(hostlist1);

cloudletid = 25;
Cloudlet cloudlet25 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
cloudlet25.setbrokerId(broker25Id);
//update the price of cloudlet;
//price = 2500;
//cloudlet25.setprice(price);
cloudlet25.setuser(user25);
user25.setCloudletId(cloudlet25.getCloudletId());

// add the cloudlet to the list
cloudletList.add(cloudlet25);

// submit cloudlet list to the broker
broker25.submitCloudletList(cloudletList);

//adrian
numtime = duration[25];
res = resask[25];
user25.submitCloudlet(cloudlet25);
user25.submitRequest(user25.getCloudlet(), false, res, res, (long)(res), numtime);
relinp.clear();
host.getmtimes().put(userid, 0);
for(int i = 0 ; i < 150; i++){
double a = r.nextGaussian() * 0.3 + aveRP[25];
if (a < 0 || a > 1){}
else{
relinp.add(a);
}
}
if(aveRP[25] == 0){}
else if(aveRP[25] > 0){

ArrayList<Double> se25 = new ArrayList<Double> ();
ArrayList<Double> de25 = new ArrayList<Double> ();
host.getmaop().put(userid, se25);
host.getmsop().put(userid, de25);
for(int i = 1; i < 51; i++){
host.addmaop(userid, relinp.get(i));
host.addmsop(userid, relinp.get(i));
host.addmtimes(userid);
}
}
relinp.set(0,0.759289226467618);
basicprice = user25.getnumCPU() * 0.01 + user25.getnumRAM() * 0.01 + (double)(user25.getnumStorage()*0.001);
pledge = basicprice * 1.05;
System.out.println("user" + user25.getuserId() + " asks for " + user25.getnumCPU() + " CPUs and " + user25.getnumRAM() + " GB of RAM and " + user25.getnumStorage() + " GB of storage to host" + host.getId());

utiliz = CalUti(timestamp[25], acpu, uti, expiretime, utireal);
id = 0;
if(cloudlet25.pledge == true){
//here I didn't consider giving back the extra money if the user will not relinquish any resource;
payprice = pledge;
}
else{
id = DecideModel(utiliz, aveRP[25], userid, res);
id = 1;
//host.minusmtimes(userid);
//here remember the BROKER is changing to BROKER2!!!!!!!!! and cloudlet is cloudlet2!!!!!!!!!
if (host.getmtimes().get(userid) == 0){
host.addmtimes(userid);
ArrayList<Double> aaa = new ArrayList<Double> ();
ArrayList<Double> bbb = new ArrayList<Double> ();
//here I will set some default values for aop and sop and ...;
if(host.getmaop().get(userid) == null){
broker25.CalNumOfRes(user25.getuserId(), host.getId(), 0.3, 0.3, id);
//right now we don't consider the pricing model;
//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
host.getmaop().put(userid, bbb);
}
else {
	broker25.CalNumOfRes(user25.getuserId(), host.getId(), 0.3, id);
	//right now we don't consider the pricing model;
	//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
}
host.getmsop().put(userid, aaa);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));
}
else if(host.getmtimes().get(userid) == 1){
host.addmtimes(userid);
// will add codes for sop and aop and ...;
broker25.CalNumOfRes1(user25.getuserId(), host.getId(), id);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));
//payprice = broker2.CalPrice(cloudlet2.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) == 2){
host.addmtimes(userid);
// will add codes for sop and aop and ...;
broker25.CalNumOfRes2(user25.getuserId(), host.getId(), id);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));
//payprice = broker25.CalPrice(cloudlet25.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) > 2){
broker25.CalNumOfRes(user25.getuserId(), host.getId(), id);
int a = host.getmtimes().get(userid);
host.addmtimes(userid);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));

}
}


acpu.add(user25.getnumCPU());
aram.add(user25.getnumRAM());
asto.add(user25.getnumStorage());
utimeh = host.getmtimes().get(userid) ;
System.out.println("this is the " + utimeh + "th time for user" + user25.getuserId() + " to use services of host" + host.getId());
payprice = user25.getnumCPU() * 0.01 + user25.getnumRAM() * 0.01 + (double)(user25.getnumStorage()*0.001);
vmid = 25;

Vm vm25 = new Vm(vmid, broker25Id, mips, user25.getnumCPU(), user25.getnumRAM(), bw, user25.getnumStorage(), vmm, new CloudletSchedulerTimeShared());

vm25.setHost(host);

//int vmid2 = 1;
//Vm vm2 = new Vm(vmid2, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
// add the VM to the vmList
vmlist.add(vm25);
//vmlist.add(vm2);
duration[25] = (int)(duration[25]*(1-relinp.get(0)));
expiretime[25] = timestamp[25] + duration[25];
CalUtinew(timestamp[25], acpu, uti, utiliz, utireal);
// submit vm list to the broker
broker25.submitVmList(vmlist);

broker25.bindCloudletToVm(cloudletid, vmid);

payprice = payprice * user25.gettime();

reimbursement = payprice * bb;
// Update the records of reimbursement and payment.
reimburse.add(reimbursement);
System.out.println(host.getmtimes().get(userid));
payment.add(payprice);

CloudSim.startSimulation();
/////////////////////
///////////////////
CloudSim.init(num_user, calendar, trace_flag);

@SuppressWarnings("unused")
Datacenter datacenter26 = createDatacenter("Datacenter_1");

DatacenterBroker broker26 = createBroker();
int broker26Id = broker26.getId();

//update totalProfit;
totalProfit += payprice;
userid = 26;
User user26 = new User(userid);
userlist.add(user26);

broker26.submitUserList(userlist);
broker26.BindUserToHost(user26.getuserId(), host.getId());

host.setTotalProfit(totalProfit);

broker26.setHostList(hostlist1);

cloudletid = 26;
Cloudlet cloudlet26 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
cloudlet26.setbrokerId(broker26Id);
//update the price of cloudlet;
//price = 2600;
//cloudlet26.setprice(price);
cloudlet26.setuser(user26);
user26.setCloudletId(cloudlet26.getCloudletId());

// add the cloudlet to the list
cloudletList.add(cloudlet26);

// submit cloudlet list to the broker
broker26.submitCloudletList(cloudletList);

//adrian
numtime = duration[26];
res = resask[26];
user26.submitCloudlet(cloudlet26);
user26.submitRequest(user26.getCloudlet(), false, res, res, (long)(res), numtime);
relinp.clear();
host.getmtimes().put(userid, 0);
for(int i = 0 ; i < 100; i++){
double a = r.nextGaussian() * 0.3 + aveRP[26];
if (a < 0 || a > 1){}
else{
relinp.add(a);
}
}
if(aveRP[26] == 0){}
else if(aveRP[26] > 0){

ArrayList<Double> se26 = new ArrayList<Double> ();
ArrayList<Double> de26 = new ArrayList<Double> ();
host.getmaop().put(userid, se26);
host.getmsop().put(userid, de26);
for(int i = 1; i < 51; i++){
host.addmaop(userid, relinp.get(i));
host.addmsop(userid, relinp.get(i));
host.addmtimes(userid);
}
}
relinp.set(0,0.759289226467618);
basicprice = user26.getnumCPU() * 0.01 + user26.getnumRAM() * 0.01 + (double)(user26.getnumStorage()*0.001);
pledge = basicprice * 1.05;
System.out.println("user" + user26.getuserId() + " asks for " + user26.getnumCPU() + " CPUs and " + user26.getnumRAM() + " GB of RAM and " + user26.getnumStorage() + " GB of storage to host" + host.getId());

utiliz = CalUti(timestamp[26], acpu, uti, expiretime, utireal);
id = 0;
if(cloudlet26.pledge == true){
//here I didn't consider giving back the extra money if the user will not relinquish any resource;
payprice = pledge;
}
else{
id = DecideModel(utiliz, aveRP[26], userid, res);
id = 1;
//host.minusmtimes(userid);
//here remember the BROKER is changing to BROKER2!!!!!!!!! and cloudlet is cloudlet2!!!!!!!!!
if (host.getmtimes().get(userid) == 0){
host.addmtimes(userid);
ArrayList<Double> aaa = new ArrayList<Double> ();
ArrayList<Double> bbb = new ArrayList<Double> ();
//here I will set some default values for aop and sop and ...;
if(host.getmaop().get(userid) == null){
broker26.CalNumOfRes(user26.getuserId(), host.getId(), 0.3, 0.3, id);
//right now we don't consider the pricing model;
//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
host.getmaop().put(userid, bbb);
}
else {
	broker26.CalNumOfRes(user26.getuserId(), host.getId(), 0.3, id);
	//right now we don't consider the pricing model;
	//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
}
host.getmsop().put(userid, aaa);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));
}
else if(host.getmtimes().get(userid) == 1){
host.addmtimes(userid);
// will add codes for sop and aop and ...;
broker26.CalNumOfRes1(user26.getuserId(), host.getId(), id);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));
//payprice = broker2.CalPrice(cloudlet2.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) == 2){
host.addmtimes(userid);
// will add codes for sop and aop and ...;
broker26.CalNumOfRes2(user26.getuserId(), host.getId(), id);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));
//payprice = broker26.CalPrice(cloudlet26.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) > 2){
broker26.CalNumOfRes(user26.getuserId(), host.getId(), id);
int a = host.getmtimes().get(userid);
host.addmtimes(userid);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));

}
}


acpu.add(user26.getnumCPU());
aram.add(user26.getnumRAM());
asto.add(user26.getnumStorage());
utimeh = host.getmtimes().get(userid) ;
System.out.println("this is the " + utimeh + "th time for user" + user26.getuserId() + " to use services of host" + host.getId());
payprice = user26.getnumCPU() * 0.01 + user26.getnumRAM() * 0.01 + (double)(user26.getnumStorage()*0.001);
vmid = 26;

Vm vm26 = new Vm(vmid, broker26Id, mips, user26.getnumCPU(), user26.getnumRAM(), bw, user26.getnumStorage(), vmm, new CloudletSchedulerTimeShared());

vm26.setHost(host);

//int vmid2 = 1;
//Vm vm2 = new Vm(vmid2, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
// add the VM to the vmList
vmlist.add(vm26);
//vmlist.add(vm2);
duration[26] = (int)(duration[26]*(1-relinp.get(0)));
expiretime[26] = timestamp[26] + duration[26];
CalUtinew(timestamp[26], acpu, uti, utiliz, utireal);
// submit vm list to the broker
broker26.submitVmList(vmlist);

broker26.bindCloudletToVm(cloudletid, vmid);

payprice = payprice * user26.gettime();

reimbursement = payprice * bb;
// Update the records of reimbursement and payment.
reimburse.add(reimbursement);
System.out.println(host.getmtimes().get(userid));
payment.add(payprice);

CloudSim.startSimulation();
/////////////////////
///////////////////
CloudSim.init(num_user, calendar, trace_flag);

@SuppressWarnings("unused")
Datacenter datacenter27 = createDatacenter("Datacenter_1");

DatacenterBroker broker27 = createBroker();
int broker27Id = broker27.getId();

//update totalProfit;
totalProfit += payprice;
userid = 27;
User user27 = new User(userid);
userlist.add(user27);

broker27.submitUserList(userlist);
broker27.BindUserToHost(user27.getuserId(), host.getId());

host.setTotalProfit(totalProfit);

broker27.setHostList(hostlist1);

cloudletid = 27;
Cloudlet cloudlet27 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
cloudlet27.setbrokerId(broker27Id);
//update the price of cloudlet;
//price = 2700;
//cloudlet27.setprice(price);
cloudlet27.setuser(user27);
user27.setCloudletId(cloudlet27.getCloudletId());

// add the cloudlet to the list
cloudletList.add(cloudlet27);

// submit cloudlet list to the broker
broker27.submitCloudletList(cloudletList);

//adrian
numtime = duration[27];
res = resask[27];
user27.submitCloudlet(cloudlet27);
user27.submitRequest(user27.getCloudlet(), false, res, res, (long)(res), numtime);
relinp.clear();
host.getmtimes().put(userid, 0);
for(int i = 0 ; i < 100; i++){
double a = r.nextGaussian() * 0.3 + aveRP[27];
if (a < 0 || a > 1){}
else{
relinp.add(a);
}
}
if(aveRP[27] == 0){}
else if(aveRP[27] > 0){

ArrayList<Double> se27 = new ArrayList<Double> ();
ArrayList<Double> de27 = new ArrayList<Double> ();
host.getmaop().put(userid, se27);
host.getmsop().put(userid, de27);
for(int i = 1; i < 51; i++){
host.addmaop(userid, relinp.get(i));
host.addmsop(userid, relinp.get(i));
host.addmtimes(userid);
}
}
relinp.set(0,0.759289226467618);
basicprice = user27.getnumCPU() * 0.01 + user27.getnumRAM() * 0.01 + (double)(user27.getnumStorage()*0.001);
pledge = basicprice * 1.05;
System.out.println("user" + user27.getuserId() + " asks for " + user27.getnumCPU() + " CPUs and " + user27.getnumRAM() + " GB of RAM and " + user27.getnumStorage() + " GB of storage to host" + host.getId());

utiliz = CalUti(timestamp[27], acpu, uti, expiretime, utireal);
id = 0;
if(cloudlet27.pledge == true){
//here I didn't consider giving back the extra money if the user will not relinquish any resource;
payprice = pledge;
}
else{
id = DecideModel(utiliz, aveRP[27], userid, res);
id = 1;
//host.minusmtimes(userid);
//here remember the BROKER is changing to BROKER2!!!!!!!!! and cloudlet is cloudlet2!!!!!!!!!
if (host.getmtimes().get(userid) == 0){
host.addmtimes(userid);
ArrayList<Double> aaa = new ArrayList<Double> ();
ArrayList<Double> bbb = new ArrayList<Double> ();
//here I will set some default values for aop and sop and ...;
if(host.getmaop().get(userid) == null){
broker27.CalNumOfRes(user27.getuserId(), host.getId(), 0.3, 0.3, id);
//right now we don't consider the pricing model;
//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
host.getmaop().put(userid, bbb);
}
else {
	broker27.CalNumOfRes(user27.getuserId(), host.getId(), 0.3, id);
	//right now we don't consider the pricing model;
	//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
}
host.getmsop().put(userid, aaa);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));
}
else if(host.getmtimes().get(userid) == 1){
host.addmtimes(userid);
// will add codes for sop and aop and ...;
broker27.CalNumOfRes1(user27.getuserId(), host.getId(), id);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));
//payprice = broker2.CalPrice(cloudlet2.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) == 2){
host.addmtimes(userid);
// will add codes for sop and aop and ...;
broker27.CalNumOfRes2(user27.getuserId(), host.getId(), id);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));
//payprice = broker27.CalPrice(cloudlet27.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) > 2){
broker27.CalNumOfRes(user27.getuserId(), host.getId(), id);
int a = host.getmtimes().get(userid);
host.addmtimes(userid);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));

}
}


acpu.add(user27.getnumCPU());
aram.add(user27.getnumRAM());
asto.add(user27.getnumStorage());
utimeh = host.getmtimes().get(userid) ;
System.out.println("this is the " + utimeh + "th time for user" + user27.getuserId() + " to use services of host" + host.getId());
payprice = user27.getnumCPU() * 0.01 + user27.getnumRAM() * 0.01 + (double)(user27.getnumStorage()*0.001);
vmid = 27;

Vm vm27 = new Vm(vmid, broker27Id, mips, user27.getnumCPU(), user27.getnumRAM(), bw, user27.getnumStorage(), vmm, new CloudletSchedulerTimeShared());

vm27.setHost(host);

//int vmid2 = 1;
//Vm vm2 = new Vm(vmid2, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
// add the VM to the vmList
vmlist.add(vm27);
//vmlist.add(vm2);
duration[27] = (int)(duration[27]*(1-relinp.get(0)));
expiretime[27] = timestamp[27] + duration[27];
CalUtinew(timestamp[27], acpu, uti, utiliz, utireal);
// submit vm list to the broker
broker27.submitVmList(vmlist);

broker27.bindCloudletToVm(cloudletid, vmid);

payprice = payprice * user27.gettime();

reimbursement = payprice * bb;
// Update the records of reimbursement and payment.
reimburse.add(reimbursement);
System.out.println(host.getmtimes().get(userid));
payment.add(payprice);

CloudSim.startSimulation();
/////////////////////
///////////////////
CloudSim.init(num_user, calendar, trace_flag);

@SuppressWarnings("unused")
Datacenter datacenter28 = createDatacenter("Datacenter_1");

DatacenterBroker broker28 = createBroker();
int broker28Id = broker28.getId();

//update totalProfit;
totalProfit += payprice;
userid = 28;
User user28 = new User(userid);
userlist.add(user28);

broker28.submitUserList(userlist);
broker28.BindUserToHost(user28.getuserId(), host.getId());

host.setTotalProfit(totalProfit);

broker28.setHostList(hostlist1);

cloudletid = 28;
Cloudlet cloudlet28 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
cloudlet28.setbrokerId(broker28Id);
//update the price of cloudlet;
//price = 2800;
//cloudlet28.setprice(price);
cloudlet28.setuser(user28);
user28.setCloudletId(cloudlet28.getCloudletId());

// add the cloudlet to the list
cloudletList.add(cloudlet28);

// submit cloudlet list to the broker
broker28.submitCloudletList(cloudletList);

//adrian
numtime = duration[28];
res = resask[28];
user28.submitCloudlet(cloudlet28);
user28.submitRequest(user28.getCloudlet(), false, res, res, (long)(res), numtime);
relinp.clear();
host.getmtimes().put(userid, 0);
for(int i = 0 ; i < 100; i++){
double a = r.nextGaussian() * 0.3 + aveRP[28];
if (a < 0 || a > 1){}
else{
relinp.add(a);
}
}
if(aveRP[28] == 0){}
else if(aveRP[28] > 0){

ArrayList<Double> se28 = new ArrayList<Double> ();
ArrayList<Double> de28 = new ArrayList<Double> ();
host.getmaop().put(userid, se28);
host.getmsop().put(userid, de28);
for(int i = 1; i < 51; i++){
host.addmaop(userid, relinp.get(i));
host.addmsop(userid, relinp.get(i));
host.addmtimes(userid);
}
}
relinp.set(0,0.759289226467618);
basicprice = user28.getnumCPU() * 0.01 + user28.getnumRAM() * 0.01 + (double)(user28.getnumStorage()*0.001);
pledge = basicprice * 1.05;
System.out.println("user" + user28.getuserId() + " asks for " + user28.getnumCPU() + " CPUs and " + user28.getnumRAM() + " GB of RAM and " + user28.getnumStorage() + " GB of storage to host" + host.getId());

utiliz = CalUti(timestamp[28], acpu, uti, expiretime, utireal);
id = 0;
if(cloudlet28.pledge == true){
//here I didn't consider giving back the extra money if the user will not relinquish any resource;
payprice = pledge;
}
else{
id = DecideModel(utiliz, aveRP[28], userid, res);
id = 1;
//host.minusmtimes(userid);
//here remember the BROKER is changing to BROKER2!!!!!!!!! and cloudlet is cloudlet2!!!!!!!!!
if (host.getmtimes().get(userid) == 0){
host.addmtimes(userid);
ArrayList<Double> aaa = new ArrayList<Double> ();
ArrayList<Double> bbb = new ArrayList<Double> ();
//here I will set some default values for aop and sop and ...;
if(host.getmaop().get(userid) == null){
broker28.CalNumOfRes(user28.getuserId(), host.getId(), 0.3, 0.3, id);
//right now we don't consider the pricing model;
//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
host.getmaop().put(userid, bbb);
}
else {
	broker28.CalNumOfRes(user28.getuserId(), host.getId(), 0.3, id);
	//right now we don't consider the pricing model;
	//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
}
host.getmsop().put(userid, aaa);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));
}
else if(host.getmtimes().get(userid) == 1){
host.addmtimes(userid);
// will add codes for sop and aop and ...;
broker28.CalNumOfRes1(user28.getuserId(), host.getId(), id);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));
//payprice = broker2.CalPrice(cloudlet2.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) == 2){
host.addmtimes(userid);
// will add codes for sop and aop and ...;
broker28.CalNumOfRes2(user28.getuserId(), host.getId(), id);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));
//payprice = broker28.CalPrice(cloudlet28.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) > 2){
broker28.CalNumOfRes(user28.getuserId(), host.getId(), id);
int a = host.getmtimes().get(userid);
host.addmtimes(userid);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));

}
}


acpu.add(user28.getnumCPU());
aram.add(user28.getnumRAM());
asto.add(user28.getnumStorage());
utimeh = host.getmtimes().get(userid) ;
System.out.println("this is the " + utimeh + "th time for user" + user28.getuserId() + " to use services of host" + host.getId());
payprice = user28.getnumCPU() * 0.01 + user28.getnumRAM() * 0.01 + (double)(user28.getnumStorage()*0.001);
vmid = 28;

Vm vm28 = new Vm(vmid, broker28Id, mips, user28.getnumCPU(), user28.getnumRAM(), bw, user28.getnumStorage(), vmm, new CloudletSchedulerTimeShared());

vm28.setHost(host);

//int vmid2 = 1;
//Vm vm2 = new Vm(vmid2, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
// add the VM to the vmList
vmlist.add(vm28);
//vmlist.add(vm2);
duration[28] = (int)(duration[28]*(1-relinp.get(0)));
expiretime[28] = timestamp[28] + duration[28];
CalUtinew(timestamp[28], acpu, uti, utiliz, utireal);
// submit vm list to the broker
broker28.submitVmList(vmlist);

broker28.bindCloudletToVm(cloudletid, vmid);

payprice = payprice * user28.gettime();

reimbursement = payprice * bb;
// Update the records of reimbursement and payment.
reimburse.add(reimbursement);
System.out.println(host.getmtimes().get(userid));
payment.add(payprice);

CloudSim.startSimulation();
/////////////////////
///////////////////
CloudSim.init(num_user, calendar, trace_flag);

@SuppressWarnings("unused")
Datacenter datacenter29 = createDatacenter("Datacenter_1");

DatacenterBroker broker29 = createBroker();
int broker29Id = broker29.getId();

//update totalProfit;
totalProfit += payprice;
userid = 29;
User user29 = new User(userid);
userlist.add(user29);

broker29.submitUserList(userlist);
broker29.BindUserToHost(user29.getuserId(), host.getId());

host.setTotalProfit(totalProfit);

broker29.setHostList(hostlist1);

cloudletid = 29;
Cloudlet cloudlet29 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
cloudlet29.setbrokerId(broker29Id);
//update the price of cloudlet;
//price = 2900;
//cloudlet29.setprice(price);
cloudlet29.setuser(user29);
user29.setCloudletId(cloudlet29.getCloudletId());

// add the cloudlet to the list
cloudletList.add(cloudlet29);

// submit cloudlet list to the broker
broker29.submitCloudletList(cloudletList);

//adrian
numtime = duration[29];
res = resask[29];
user29.submitCloudlet(cloudlet29);
user29.submitRequest(user29.getCloudlet(), false, res, res, (long)(res), numtime);
relinp.clear();
host.getmtimes().put(userid, 0);
for(int i = 0 ; i < 100; i++){
double a = r.nextGaussian() * 0.3 + aveRP[29];
if (a < 0 || a > 1){}
else{
relinp.add(a);
}
}
if(aveRP[29] == 0){}
else if(aveRP[29] > 0){

ArrayList<Double> se29 = new ArrayList<Double> ();
ArrayList<Double> de29 = new ArrayList<Double> ();
host.getmaop().put(userid, se29);
host.getmsop().put(userid, de29);
for(int i = 1; i < 51; i++){
host.addmaop(userid, relinp.get(i));
host.addmsop(userid, relinp.get(i));
host.addmtimes(userid);
}
}
relinp.set(0,0.759289226467618);
basicprice = user29.getnumCPU() * 0.01 + user29.getnumRAM() * 0.01 + (double)(user29.getnumStorage()*0.001);
pledge = basicprice * 1.05;
System.out.println("user" + user29.getuserId() + " asks for " + user29.getnumCPU() + " CPUs and " + user29.getnumRAM() + " GB of RAM and " + user29.getnumStorage() + " GB of storage to host" + host.getId());

utiliz = CalUti(timestamp[29], acpu, uti, expiretime, utireal);
id = 0;
if(cloudlet29.pledge == true){
//here I didn't consider giving back the extra money if the user will not relinquish any resource;
payprice = pledge;
}
else{
id = DecideModel(utiliz, aveRP[29], userid, res);
id = 1;
//host.minusmtimes(userid);
//here remember the BROKER is changing to BROKER2!!!!!!!!! and cloudlet is cloudlet2!!!!!!!!!
if (host.getmtimes().get(userid) == 0){
host.addmtimes(userid);
ArrayList<Double> aaa = new ArrayList<Double> ();
ArrayList<Double> bbb = new ArrayList<Double> ();
//here I will set some default values for aop and sop and ...;
if(host.getmaop().get(userid) == null){
broker29.CalNumOfRes(user29.getuserId(), host.getId(), 0.3, 0.3, id);
//right now we don't consider the pricing model;
//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
host.getmaop().put(userid, bbb);
}
else {
	broker29.CalNumOfRes(user29.getuserId(), host.getId(), 0.3, id);
	//right now we don't consider the pricing model;
	//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
}
host.getmsop().put(userid, aaa);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));
}
else if(host.getmtimes().get(userid) == 1){
host.addmtimes(userid);
// will add codes for sop and aop and ...;
broker29.CalNumOfRes1(user29.getuserId(), host.getId(), id);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));
//payprice = broker2.CalPrice(cloudlet2.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) == 2){
host.addmtimes(userid);
// will add codes for sop and aop and ...;
broker29.CalNumOfRes2(user29.getuserId(), host.getId(), id);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));
//payprice = broker29.CalPrice(cloudlet29.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) > 2){
broker29.CalNumOfRes(user29.getuserId(), host.getId(), id);
int a = host.getmtimes().get(userid);
host.addmtimes(userid);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));

}
}


acpu.add(user29.getnumCPU());
aram.add(user29.getnumRAM());
asto.add(user29.getnumStorage());
utimeh = host.getmtimes().get(userid) ;
System.out.println("this is the " + utimeh + "th time for user" + user29.getuserId() + " to use services of host" + host.getId());
payprice = user29.getnumCPU() * 0.01 + user29.getnumRAM() * 0.01 + (double)(user29.getnumStorage()*0.001);
vmid = 29;

Vm vm29 = new Vm(vmid, broker29Id, mips, user29.getnumCPU(), user29.getnumRAM(), bw, user29.getnumStorage(), vmm, new CloudletSchedulerTimeShared());

vm29.setHost(host);

//int vmid2 = 1;
//Vm vm2 = new Vm(vmid2, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
// add the VM to the vmList
vmlist.add(vm29);
//vmlist.add(vm2);
duration[29] = (int)(duration[29]*(1-relinp.get(0)));
expiretime[29] = timestamp[29] + duration[29];
CalUtinew(timestamp[29], acpu, uti, utiliz, utireal);
// submit vm list to the broker
broker29.submitVmList(vmlist);

broker29.bindCloudletToVm(cloudletid, vmid);

payprice = payprice * user29.gettime();

reimbursement = payprice * bb;
// Update the records of reimbursement and payment.
reimburse.add(reimbursement);
System.out.println(host.getmtimes().get(userid));
payment.add(payprice);

CloudSim.startSimulation();
/////////////////////
///////////////////
CloudSim.init(num_user, calendar, trace_flag);

@SuppressWarnings("unused")
Datacenter datacenter30 = createDatacenter("Datacenter_1");

DatacenterBroker broker30 = createBroker();
int broker30Id = broker30.getId();

//update totalProfit;
totalProfit += payprice;
userid = 30;
User user30 = new User(userid);
userlist.add(user30);

broker30.submitUserList(userlist);
broker30.BindUserToHost(user30.getuserId(), host.getId());

host.setTotalProfit(totalProfit);

broker30.setHostList(hostlist1);

cloudletid = 30;
Cloudlet cloudlet30 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
cloudlet30.setbrokerId(broker30Id);
//update the price of cloudlet;
//price = 3000;
//cloudlet30.setprice(price);
cloudlet30.setuser(user30);
user30.setCloudletId(cloudlet30.getCloudletId());

// add the cloudlet to the list
cloudletList.add(cloudlet30);

// submit cloudlet list to the broker
broker30.submitCloudletList(cloudletList);

//adrian
numtime = duration[30];
res = resask[30];
user30.submitCloudlet(cloudlet30);
user30.submitRequest(user30.getCloudlet(), false, res, res, (long)(res), numtime);
relinp.clear();
host.getmtimes().put(userid, 0);
for(int i = 0 ; i < 100; i++){
double a = r.nextGaussian() * 0.3 + aveRP[30];
if (a < 0 || a > 1){}
else{
relinp.add(a);
}
}
if(aveRP[30] == 0){}
else if(aveRP[30] > 0){

ArrayList<Double> se30 = new ArrayList<Double> ();
ArrayList<Double> de30 = new ArrayList<Double> ();
host.getmaop().put(userid, se30);
host.getmsop().put(userid, de30);
for(int i = 1; i < 51; i++){
host.addmaop(userid, relinp.get(i));
host.addmsop(userid, relinp.get(i));
host.addmtimes(userid);
}
}
relinp.set(0,0.759289226467618);
basicprice = user30.getnumCPU() * 0.01 + user30.getnumRAM() * 0.01 + (double)(user30.getnumStorage()*0.001);
pledge = basicprice * 1.05;
System.out.println("user" + user30.getuserId() + " asks for " + user30.getnumCPU() + " CPUs and " + user30.getnumRAM() + " GB of RAM and " + user30.getnumStorage() + " GB of storage to host" + host.getId());

utiliz = CalUti(timestamp[30], acpu, uti, expiretime, utireal);
id = 0;
if(cloudlet30.pledge == true){
//here I didn't consider giving back the extra money if the user will not relinquish any resource;
payprice = pledge;
}
else{
id = DecideModel(utiliz, aveRP[30], userid, res);
id = 1;
//host.minusmtimes(userid);
//here remember the BROKER is changing to BROKER2!!!!!!!!! and cloudlet is cloudlet2!!!!!!!!!
if (host.getmtimes().get(userid) == 0){
host.addmtimes(userid);
ArrayList<Double> aaa = new ArrayList<Double> ();
ArrayList<Double> bbb = new ArrayList<Double> ();
//here I will set some default values for aop and sop and ...;
if(host.getmaop().get(userid) == null){
broker30.CalNumOfRes(user30.getuserId(), host.getId(), 0.3, 0.3, id);
//right now we don't consider the pricing model;
//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
host.getmaop().put(userid, bbb);
}
else {
	broker30.CalNumOfRes(user30.getuserId(), host.getId(), 0.3, id);
	//right now we don't consider the pricing model;
	//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
}
host.getmsop().put(userid, aaa);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));
}
else if(host.getmtimes().get(userid) == 1){
host.addmtimes(userid);
// will add codes for sop and aop and ...;
broker30.CalNumOfRes1(user30.getuserId(), host.getId(), id);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));
//payprice = broker2.CalPrice(cloudlet2.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) == 2){
host.addmtimes(userid);
// will add codes for sop and aop and ...;
broker30.CalNumOfRes2(user30.getuserId(), host.getId(), id);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));
//payprice = broker30.CalPrice(cloudlet30.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) > 2){
broker30.CalNumOfRes(user30.getuserId(), host.getId(), id);
int a = host.getmtimes().get(userid);
host.addmtimes(userid);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));

}
}


acpu.add(user30.getnumCPU());
aram.add(user30.getnumRAM());
asto.add(user30.getnumStorage());
utimeh = host.getmtimes().get(userid) ;
System.out.println("this is the " + utimeh + "th time for user" + user30.getuserId() + " to use services of host" + host.getId());
payprice = user30.getnumCPU() * 0.01 + user30.getnumRAM() * 0.01 + (double)(user30.getnumStorage()*0.001);
vmid = 30;

Vm vm30 = new Vm(vmid, broker30Id, mips, user30.getnumCPU(), user30.getnumRAM(), bw, user30.getnumStorage(), vmm, new CloudletSchedulerTimeShared());

vm30.setHost(host);

//int vmid2 = 1;
//Vm vm2 = new Vm(vmid2, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
// add the VM to the vmList
vmlist.add(vm30);
//vmlist.add(vm2);
duration[30] = (int)(duration[30]*(1-relinp.get(0)));
expiretime[30] = timestamp[30] + duration[30];
CalUtinew(timestamp[30], acpu, uti, utiliz, utireal);
// submit vm list to the broker
broker30.submitVmList(vmlist);

broker30.bindCloudletToVm(cloudletid, vmid);

payprice = payprice * user30.gettime();

reimbursement = payprice * bb;
// Update the records of reimbursement and payment.
reimburse.add(reimbursement);
System.out.println(host.getmtimes().get(userid));
payment.add(payprice);

CloudSim.startSimulation();
/////////////////////
///////////////////
CloudSim.init(num_user, calendar, trace_flag);

@SuppressWarnings("unused")
Datacenter datacenter31 = createDatacenter("Datacenter_1");

DatacenterBroker broker31 = createBroker();
int broker31Id = broker31.getId();

//update totalProfit;
totalProfit += payprice;
userid = 31;
User user31 = new User(userid);
userlist.add(user31);

broker31.submitUserList(userlist);
broker31.BindUserToHost(user31.getuserId(), host.getId());

host.setTotalProfit(totalProfit);

broker31.setHostList(hostlist1);

cloudletid = 31;
Cloudlet cloudlet31 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
cloudlet31.setbrokerId(broker31Id);
//update the price of cloudlet;
//price = 3100;
//cloudlet31.setprice(price);
cloudlet31.setuser(user31);
user31.setCloudletId(cloudlet31.getCloudletId());

// add the cloudlet to the list
cloudletList.add(cloudlet31);

// submit cloudlet list to the broker
broker31.submitCloudletList(cloudletList);

//adrian
numtime = duration[31];
res = resask[31];
user31.submitCloudlet(cloudlet31);
user31.submitRequest(user31.getCloudlet(), false, res, res, (long)(res), numtime);
relinp.clear();
host.getmtimes().put(userid, 0);
for(int i = 0 ; i < 100; i++){
double a = r.nextGaussian() * 0.3 + aveRP[31];
if (a < 0 || a > 1){}
else{
relinp.add(a);
}
}
if(aveRP[31] == 0){}
else if(aveRP[31] > 0){

ArrayList<Double> se31 = new ArrayList<Double> ();
ArrayList<Double> de31 = new ArrayList<Double> ();
host.getmaop().put(userid, se31);
host.getmsop().put(userid, de31);
for(int i = 1; i < 51; i++){
host.addmaop(userid, relinp.get(i));
host.addmsop(userid, relinp.get(i));
host.addmtimes(userid);
}
}
relinp.set(0,0.759289226467618);
basicprice = user31.getnumCPU() * 0.01 + user31.getnumRAM() * 0.01 + (double)(user31.getnumStorage()*0.001);
pledge = basicprice * 1.05;
System.out.println("user" + user31.getuserId() + " asks for " + user31.getnumCPU() + " CPUs and " + user31.getnumRAM() + " GB of RAM and " + user31.getnumStorage() + " GB of storage to host" + host.getId());

utiliz = CalUti(timestamp[31], acpu, uti, expiretime, utireal);
id = 0;
if(cloudlet31.pledge == true){
//here I didn't consider giving back the extra money if the user will not relinquish any resource;
payprice = pledge;
}
else{
id = DecideModel(utiliz, aveRP[31], userid, res);
id = 1;
//host.minusmtimes(userid);
//here remember the BROKER is changing to BROKER2!!!!!!!!! and cloudlet is cloudlet2!!!!!!!!!
if (host.getmtimes().get(userid) == 0){
host.addmtimes(userid);
ArrayList<Double> aaa = new ArrayList<Double> ();
ArrayList<Double> bbb = new ArrayList<Double> ();
//here I will set some default values for aop and sop and ...;
if(host.getmaop().get(userid) == null){
broker31.CalNumOfRes(user31.getuserId(), host.getId(), 0.3, 0.3, id);
//right now we don't consider the pricing model;
//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
host.getmaop().put(userid, bbb);
}
else {
	broker31.CalNumOfRes(user31.getuserId(), host.getId(), 0.3, id);
	//right now we don't consider the pricing model;
	//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
}
host.getmsop().put(userid, aaa);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));
}
else if(host.getmtimes().get(userid) == 1){
host.addmtimes(userid);
// will add codes for sop and aop and ...;
broker31.CalNumOfRes1(user31.getuserId(), host.getId(), id);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));
//payprice = broker2.CalPrice(cloudlet2.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) == 2){
host.addmtimes(userid);
// will add codes for sop and aop and ...;
broker31.CalNumOfRes2(user31.getuserId(), host.getId(), id);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));
//payprice = broker31.CalPrice(cloudlet31.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) > 2){
broker31.CalNumOfRes(user31.getuserId(), host.getId(), id);
int a = host.getmtimes().get(userid);
host.addmtimes(userid);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));

}
}


acpu.add(user31.getnumCPU());
aram.add(user31.getnumRAM());
asto.add(user31.getnumStorage());
utimeh = host.getmtimes().get(userid) ;
System.out.println("this is the " + utimeh + "th time for user" + user31.getuserId() + " to use services of host" + host.getId());
payprice = user31.getnumCPU() * 0.01 + user31.getnumRAM() * 0.01 + (double)(user31.getnumStorage()*0.001);
vmid = 31;

Vm vm31 = new Vm(vmid, broker31Id, mips, user31.getnumCPU(), user31.getnumRAM(), bw, user31.getnumStorage(), vmm, new CloudletSchedulerTimeShared());

vm31.setHost(host);

//int vmid2 = 1;
//Vm vm2 = new Vm(vmid2, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
// add the VM to the vmList
vmlist.add(vm31);
//vmlist.add(vm2);
duration[31] = (int)(duration[31]*(1-relinp.get(0)));
expiretime[31] = timestamp[31] + duration[31];
CalUtinew(timestamp[31], acpu, uti, utiliz, utireal);
// submit vm list to the broker
broker31.submitVmList(vmlist);

broker31.bindCloudletToVm(cloudletid, vmid);

payprice = payprice * user31.gettime();

reimbursement = payprice * bb;
// Update the records of reimbursement and payment.
reimburse.add(reimbursement);
System.out.println(host.getmtimes().get(userid));
payment.add(payprice);

CloudSim.startSimulation();
/////////////////////
///////////////////
CloudSim.init(num_user, calendar, trace_flag);

@SuppressWarnings("unused")
Datacenter datacenter32 = createDatacenter("Datacenter_1");

DatacenterBroker broker32 = createBroker();
int broker32Id = broker32.getId();

//update totalProfit;
totalProfit += payprice;
userid = 32;
User user32 = new User(userid);
userlist.add(user32);

broker32.submitUserList(userlist);
broker32.BindUserToHost(user32.getuserId(), host.getId());

host.setTotalProfit(totalProfit);

broker32.setHostList(hostlist1);

cloudletid = 32;
Cloudlet cloudlet32 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
cloudlet32.setbrokerId(broker32Id);
//update the price of cloudlet;
//price = 3200;
//cloudlet32.setprice(price);
cloudlet32.setuser(user32);
user32.setCloudletId(cloudlet32.getCloudletId());

// add the cloudlet to the list
cloudletList.add(cloudlet32);

// submit cloudlet list to the broker
broker32.submitCloudletList(cloudletList);

//adrian
numtime = duration[32];
res = resask[32];
user32.submitCloudlet(cloudlet32);
user32.submitRequest(user32.getCloudlet(), false, res, res, (long)(res), numtime);
relinp.clear();
host.getmtimes().put(userid, 0);
for(int i = 0 ; i < 100; i++){
double a = r.nextGaussian() * 0.3 + aveRP[32];
if (a < 0 || a > 1){}
else{
relinp.add(a);
}
}
if(aveRP[32] == 0){}
else if(aveRP[32] > 0){

ArrayList<Double> se32 = new ArrayList<Double> ();
ArrayList<Double> de32 = new ArrayList<Double> ();
host.getmaop().put(userid, se32);
host.getmsop().put(userid, de32);
for(int i = 1; i < 51; i++){
host.addmaop(userid, relinp.get(i));
host.addmsop(userid, relinp.get(i));
host.addmtimes(userid);
}
}
relinp.set(0,0.759289226467618);
basicprice = user32.getnumCPU() * 0.01 + user32.getnumRAM() * 0.01 + (double)(user32.getnumStorage()*0.001);
pledge = basicprice * 1.05;
System.out.println("user" + user32.getuserId() + " asks for " + user32.getnumCPU() + " CPUs and " + user32.getnumRAM() + " GB of RAM and " + user32.getnumStorage() + " GB of storage to host" + host.getId());

utiliz = CalUti(timestamp[32], acpu, uti, expiretime, utireal);
id = 0;
if(cloudlet32.pledge == true){
//here I didn't consider giving back the extra money if the user will not relinquish any resource;
payprice = pledge;
}
else{
id = DecideModel(utiliz, aveRP[32], userid, res);
id = 1;
//host.minusmtimes(userid);
//here remember the BROKER is changing to BROKER2!!!!!!!!! and cloudlet is cloudlet2!!!!!!!!!
if (host.getmtimes().get(userid) == 0){
host.addmtimes(userid);
ArrayList<Double> aaa = new ArrayList<Double> ();
ArrayList<Double> bbb = new ArrayList<Double> ();
//here I will set some default values for aop and sop and ...;
if(host.getmaop().get(userid) == null){
broker32.CalNumOfRes(user32.getuserId(), host.getId(), 0.3, 0.3, id);
//right now we don't consider the pricing model;
//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
host.getmaop().put(userid, bbb);
}
else {
	broker32.CalNumOfRes(user32.getuserId(), host.getId(), 0.3, id);
	//right now we don't consider the pricing model;
	//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
}
host.getmsop().put(userid, aaa);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));
}
else if(host.getmtimes().get(userid) == 1){
host.addmtimes(userid);
// will add codes for sop and aop and ...;
broker32.CalNumOfRes1(user32.getuserId(), host.getId(), id);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));
//payprice = broker2.CalPrice(cloudlet2.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) == 2){
host.addmtimes(userid);
// will add codes for sop and aop and ...;
broker32.CalNumOfRes2(user32.getuserId(), host.getId(), id);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));
//payprice = broker32.CalPrice(cloudlet32.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) > 2){
broker32.CalNumOfRes(user32.getuserId(), host.getId(), id);
int a = host.getmtimes().get(userid);
host.addmtimes(userid);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));

}
}


acpu.add(user32.getnumCPU());
aram.add(user32.getnumRAM());
asto.add(user32.getnumStorage());
utimeh = host.getmtimes().get(userid) ;
System.out.println("this is the " + utimeh + "th time for user" + user32.getuserId() + " to use services of host" + host.getId());
payprice = user32.getnumCPU() * 0.01 + user32.getnumRAM() * 0.01 + (double)(user32.getnumStorage()*0.001);
vmid = 32;

Vm vm32 = new Vm(vmid, broker32Id, mips, user32.getnumCPU(), user32.getnumRAM(), bw, user32.getnumStorage(), vmm, new CloudletSchedulerTimeShared());

vm32.setHost(host);

//int vmid2 = 1;
//Vm vm2 = new Vm(vmid2, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
// add the VM to the vmList
vmlist.add(vm32);
//vmlist.add(vm2);
duration[32] = (int)(duration[32]*(1-relinp.get(0)));
expiretime[32] = timestamp[32] + duration[32];
CalUtinew(timestamp[32], acpu, uti, utiliz, utireal);
// submit vm list to the broker
broker32.submitVmList(vmlist);

broker32.bindCloudletToVm(cloudletid, vmid);

payprice = payprice * user32.gettime();

reimbursement = payprice * bb;
// Update the records of reimbursement and payment.
reimburse.add(reimbursement);
System.out.println(host.getmtimes().get(userid));
payment.add(payprice);

CloudSim.startSimulation();
/////////////////////
///////////////////
CloudSim.init(num_user, calendar, trace_flag);

@SuppressWarnings("unused")
Datacenter datacenter33 = createDatacenter("Datacenter_1");

DatacenterBroker broker33 = createBroker();
int broker33Id = broker33.getId();

//update totalProfit;
totalProfit += payprice;
userid = 33;
User user33 = new User(userid);
userlist.add(user33);

broker33.submitUserList(userlist);
broker33.BindUserToHost(user33.getuserId(), host.getId());

host.setTotalProfit(totalProfit);

broker33.setHostList(hostlist1);

cloudletid = 33;
Cloudlet cloudlet33 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
cloudlet33.setbrokerId(broker33Id);
//update the price of cloudlet;
//price = 3300;
//cloudlet33.setprice(price);
cloudlet33.setuser(user33);
user33.setCloudletId(cloudlet33.getCloudletId());

// add the cloudlet to the list
cloudletList.add(cloudlet33);

// submit cloudlet list to the broker
broker33.submitCloudletList(cloudletList);

//adrian
numtime = duration[33];
res = resask[33];
user33.submitCloudlet(cloudlet33);
user33.submitRequest(user33.getCloudlet(), false, res, res, (long)(res), numtime);
relinp.clear();
host.getmtimes().put(userid, 0);
for(int i = 0 ; i < 100; i++){
double a = r.nextGaussian() * 0.3 + aveRP[33];
if (a < 0 || a > 1){}
else{
relinp.add(a);
}
}
if(aveRP[33] == 0){}
else if(aveRP[33] > 0){

ArrayList<Double> se33 = new ArrayList<Double> ();
ArrayList<Double> de33 = new ArrayList<Double> ();
host.getmaop().put(userid, se33);
host.getmsop().put(userid, de33);
for(int i = 1; i < 51; i++){
host.addmaop(userid, relinp.get(i));
host.addmsop(userid, relinp.get(i));
host.addmtimes(userid);
}
}
relinp.set(0,0.759289226467618);
basicprice = user33.getnumCPU() * 0.01 + user33.getnumRAM() * 0.01 + (double)(user33.getnumStorage()*0.001);
pledge = basicprice * 1.05;
System.out.println("user" + user33.getuserId() + " asks for " + user33.getnumCPU() + " CPUs and " + user33.getnumRAM() + " GB of RAM and " + user33.getnumStorage() + " GB of storage to host" + host.getId());

utiliz = CalUti(timestamp[33], acpu, uti, expiretime, utireal);
id = 0;
if(cloudlet33.pledge == true){
//here I didn't consider giving back the extra money if the user will not relinquish any resource;
payprice = pledge;
}
else{
id = DecideModel(utiliz, aveRP[33], userid, res);
id = 1;
//host.minusmtimes(userid);
//here remember the BROKER is changing to BROKER2!!!!!!!!! and cloudlet is cloudlet2!!!!!!!!!
if (host.getmtimes().get(userid) == 0){
host.addmtimes(userid);
ArrayList<Double> aaa = new ArrayList<Double> ();
ArrayList<Double> bbb = new ArrayList<Double> ();
//here I will set some default values for aop and sop and ...;
if(host.getmaop().get(userid) == null){
broker33.CalNumOfRes(user33.getuserId(), host.getId(), 0.3, 0.3, id);
//right now we don't consider the pricing model;
//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
host.getmaop().put(userid, bbb);
}
else {
	broker33.CalNumOfRes(user33.getuserId(), host.getId(), 0.3, id);
	//right now we don't consider the pricing model;
	//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
}
host.getmsop().put(userid, aaa);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));
}
else if(host.getmtimes().get(userid) == 1){
host.addmtimes(userid);
// will add codes for sop and aop and ...;
broker33.CalNumOfRes1(user33.getuserId(), host.getId(), id);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));
//payprice = broker2.CalPrice(cloudlet2.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) == 2){
host.addmtimes(userid);
// will add codes for sop and aop and ...;
broker33.CalNumOfRes2(user33.getuserId(), host.getId(), id);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));
//payprice = broker33.CalPrice(cloudlet33.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) > 2){
broker33.CalNumOfRes(user33.getuserId(), host.getId(), id);
int a = host.getmtimes().get(userid);
host.addmtimes(userid);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));

}
}


acpu.add(user33.getnumCPU());
aram.add(user33.getnumRAM());
asto.add(user33.getnumStorage());
utimeh = host.getmtimes().get(userid) ;
System.out.println("this is the " + utimeh + "th time for user" + user33.getuserId() + " to use services of host" + host.getId());
payprice = user33.getnumCPU() * 0.01 + user33.getnumRAM() * 0.01 + (double)(user33.getnumStorage()*0.001);
vmid = 33;

Vm vm33 = new Vm(vmid, broker33Id, mips, user33.getnumCPU(), user33.getnumRAM(), bw, user33.getnumStorage(), vmm, new CloudletSchedulerTimeShared());

vm33.setHost(host);

//int vmid2 = 1;
//Vm vm2 = new Vm(vmid2, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
// add the VM to the vmList
vmlist.add(vm33);
//vmlist.add(vm2);
duration[33] = (int)(duration[33]*(1-relinp.get(0)));
expiretime[33] = timestamp[33] + duration[33];
CalUtinew(timestamp[33], acpu, uti, utiliz, utireal);
// submit vm list to the broker
broker33.submitVmList(vmlist);

broker33.bindCloudletToVm(cloudletid, vmid);

payprice = payprice * user33.gettime();

reimbursement = payprice * bb;
// Update the records of reimbursement and payment.
reimburse.add(reimbursement);
System.out.println(host.getmtimes().get(userid));
payment.add(payprice);

CloudSim.startSimulation();
/////////////////////
///////////////////
CloudSim.init(num_user, calendar, trace_flag);

@SuppressWarnings("unused")
Datacenter datacenter34 = createDatacenter("Datacenter_1");

DatacenterBroker broker34 = createBroker();
int broker34Id = broker34.getId();

//update totalProfit;
totalProfit += payprice;
userid = 34;
User user34 = new User(userid);
userlist.add(user34);

broker34.submitUserList(userlist);
broker34.BindUserToHost(user34.getuserId(), host.getId());

host.setTotalProfit(totalProfit);

broker34.setHostList(hostlist1);

cloudletid = 34;
Cloudlet cloudlet34 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
cloudlet34.setbrokerId(broker34Id);
//update the price of cloudlet;
//price = 3400;
//cloudlet34.setprice(price);
cloudlet34.setuser(user34);
user34.setCloudletId(cloudlet34.getCloudletId());

// add the cloudlet to the list
cloudletList.add(cloudlet34);

// submit cloudlet list to the broker
broker34.submitCloudletList(cloudletList);

//adrian
numtime = duration[34];
res = resask[34];
user34.submitCloudlet(cloudlet34);
user34.submitRequest(user34.getCloudlet(), false, res, res, (long)(res), numtime);
relinp.clear();
host.getmtimes().put(userid, 0);
for(int i = 0 ; i < 100; i++){
double a = r.nextGaussian() * 0.3 + aveRP[34];
if (a < 0 || a > 1){}
else{
relinp.add(a);
}
}
if(aveRP[34] == 0){}
else if(aveRP[34] > 0){

ArrayList<Double> se34 = new ArrayList<Double> ();
ArrayList<Double> de34 = new ArrayList<Double> ();
host.getmaop().put(userid, se34);
host.getmsop().put(userid, de34);
for(int i = 1; i < 51; i++){
host.addmaop(userid, relinp.get(i));
host.addmsop(userid, relinp.get(i));
host.addmtimes(userid);
}
}
relinp.set(0,0.759289226467618);
basicprice = user34.getnumCPU() * 0.01 + user34.getnumRAM() * 0.01 + (double)(user34.getnumStorage()*0.001);
pledge = basicprice * 1.05;
System.out.println("user" + user34.getuserId() + " asks for " + user34.getnumCPU() + " CPUs and " + user34.getnumRAM() + " GB of RAM and " + user34.getnumStorage() + " GB of storage to host" + host.getId());

utiliz = CalUti(timestamp[34], acpu, uti, expiretime, utireal);
id = 0;
if(cloudlet34.pledge == true){
//here I didn't consider giving back the extra money if the user will not relinquish any resource;
payprice = pledge;
}
else{
id = DecideModel(utiliz, aveRP[34], userid, res);
id = 1;
//host.minusmtimes(userid);
//here remember the BROKER is changing to BROKER2!!!!!!!!! and cloudlet is cloudlet2!!!!!!!!!
if (host.getmtimes().get(userid) == 0){
host.addmtimes(userid);
ArrayList<Double> aaa = new ArrayList<Double> ();
ArrayList<Double> bbb = new ArrayList<Double> ();
//here I will set some default values for aop and sop and ...;
if(host.getmaop().get(userid) == null){
broker34.CalNumOfRes(user34.getuserId(), host.getId(), 0.3, 0.3, id);
//right now we don't consider the pricing model;
//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
host.getmaop().put(userid, bbb);
}
else {
	broker34.CalNumOfRes(user34.getuserId(), host.getId(), 0.3, id);
	//right now we don't consider the pricing model;
	//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
}
host.getmsop().put(userid, aaa);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));
}
else if(host.getmtimes().get(userid) == 1){
host.addmtimes(userid);
// will add codes for sop and aop and ...;
broker34.CalNumOfRes1(user34.getuserId(), host.getId(), id);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));
//payprice = broker2.CalPrice(cloudlet2.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) == 2){
host.addmtimes(userid);
// will add codes for sop and aop and ...;
broker34.CalNumOfRes2(user34.getuserId(), host.getId(), id);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));
//payprice = broker34.CalPrice(cloudlet34.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) > 2){
broker34.CalNumOfRes(user34.getuserId(), host.getId(), id);
int a = host.getmtimes().get(userid);
host.addmtimes(userid);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));

}
}


acpu.add(user34.getnumCPU());
aram.add(user34.getnumRAM());
asto.add(user34.getnumStorage());
utimeh = host.getmtimes().get(userid) ;
System.out.println("this is the " + utimeh + "th time for user" + user34.getuserId() + " to use services of host" + host.getId());
payprice = user34.getnumCPU() * 0.01 + user34.getnumRAM() * 0.01 + (double)(user34.getnumStorage()*0.001);
vmid = 34;

Vm vm34 = new Vm(vmid, broker34Id, mips, user34.getnumCPU(), user34.getnumRAM(), bw, user34.getnumStorage(), vmm, new CloudletSchedulerTimeShared());

vm34.setHost(host);

//int vmid2 = 1;
//Vm vm2 = new Vm(vmid2, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
// add the VM to the vmList
vmlist.add(vm34);
//vmlist.add(vm2);
duration[34] = (int)(duration[34]*(1-relinp.get(0)));
expiretime[34] = timestamp[34] + duration[34];
CalUtinew(timestamp[34], acpu, uti, utiliz, utireal);
// submit vm list to the broker
broker34.submitVmList(vmlist);

broker34.bindCloudletToVm(cloudletid, vmid);

payprice = payprice * user34.gettime();

reimbursement = payprice * bb;
// Update the records of reimbursement and payment.
reimburse.add(reimbursement);
System.out.println(host.getmtimes().get(userid));
payment.add(payprice);

CloudSim.startSimulation();
/////////////////////
///////////////////
CloudSim.init(num_user, calendar, trace_flag);

@SuppressWarnings("unused")
Datacenter datacenter35 = createDatacenter("Datacenter_1");

DatacenterBroker broker35 = createBroker();
int broker35Id = broker35.getId();

//update totalProfit;
totalProfit += payprice;
userid = 35;
User user35 = new User(userid);
userlist.add(user35);

broker35.submitUserList(userlist);
broker35.BindUserToHost(user35.getuserId(), host.getId());

host.setTotalProfit(totalProfit);

broker35.setHostList(hostlist1);

cloudletid = 35;
Cloudlet cloudlet35 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
cloudlet35.setbrokerId(broker35Id);
//update the price of cloudlet;
//price = 3500;
//cloudlet35.setprice(price);
cloudlet35.setuser(user35);
user35.setCloudletId(cloudlet35.getCloudletId());

// add the cloudlet to the list
cloudletList.add(cloudlet35);

// submit cloudlet list to the broker
broker35.submitCloudletList(cloudletList);

//adrian
numtime = duration[35];
res = resask[35];
user35.submitCloudlet(cloudlet35);
user35.submitRequest(user35.getCloudlet(), false, res, res, (long)(res), numtime);
relinp.clear();
host.getmtimes().put(userid, 0);
for(int i = 0 ; i < 100; i++){
double a = r.nextGaussian() * 0.3 + aveRP[35];
if (a < 0 || a > 1){}
else{
relinp.add(a);
}
}
if(aveRP[35] == 0){}
else if(aveRP[35] > 0){

ArrayList<Double> se35 = new ArrayList<Double> ();
ArrayList<Double> de35 = new ArrayList<Double> ();
host.getmaop().put(userid, se35);
host.getmsop().put(userid, de35);
for(int i = 1; i < 51; i++){
host.addmaop(userid, relinp.get(i));
host.addmsop(userid, relinp.get(i));
host.addmtimes(userid);
}
}
relinp.set(0,0.759289226467618);
basicprice = user35.getnumCPU() * 0.01 + user35.getnumRAM() * 0.01 + (double)(user35.getnumStorage()*0.001);
pledge = basicprice * 1.05;
System.out.println("user" + user35.getuserId() + " asks for " + user35.getnumCPU() + " CPUs and " + user35.getnumRAM() + " GB of RAM and " + user35.getnumStorage() + " GB of storage to host" + host.getId());

utiliz = CalUti(timestamp[35], acpu, uti, expiretime, utireal);
id = 0;
if(cloudlet35.pledge == true){
//here I didn't consider giving back the extra money if the user will not relinquish any resource;
payprice = pledge;
}
else{
id = DecideModel(utiliz, aveRP[35], userid, res);
id = 1;
//host.minusmtimes(userid);
//here remember the BROKER is changing to BROKER2!!!!!!!!! and cloudlet is cloudlet2!!!!!!!!!
if (host.getmtimes().get(userid) == 0){
host.addmtimes(userid);
ArrayList<Double> aaa = new ArrayList<Double> ();
ArrayList<Double> bbb = new ArrayList<Double> ();
//here I will set some default values for aop and sop and ...;
if(host.getmaop().get(userid) == null){
broker35.CalNumOfRes(user35.getuserId(), host.getId(), 0.3, 0.3, id);
//right now we don't consider the pricing model;
//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
host.getmaop().put(userid, bbb);
}
else {
	broker35.CalNumOfRes(user35.getuserId(), host.getId(), 0.3, id);
	//right now we don't consider the pricing model;
	//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
}
host.getmsop().put(userid, aaa);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));
}
else if(host.getmtimes().get(userid) == 1){
host.addmtimes(userid);
// will add codes for sop and aop and ...;
broker35.CalNumOfRes1(user35.getuserId(), host.getId(), id);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));
//payprice = broker2.CalPrice(cloudlet2.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) == 2){
host.addmtimes(userid);
// will add codes for sop and aop and ...;
broker35.CalNumOfRes2(user35.getuserId(), host.getId(), id);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));
//payprice = broker35.CalPrice(cloudlet35.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) > 2){
broker35.CalNumOfRes(user35.getuserId(), host.getId(), id);
int a = host.getmtimes().get(userid);
host.addmtimes(userid);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));

}
}


acpu.add(user35.getnumCPU());
aram.add(user35.getnumRAM());
asto.add(user35.getnumStorage());
utimeh = host.getmtimes().get(userid) ;
System.out.println("this is the " + utimeh + "th time for user" + user35.getuserId() + " to use services of host" + host.getId());
payprice = user35.getnumCPU() * 0.01 + user35.getnumRAM() * 0.01 + (double)(user35.getnumStorage()*0.001);
vmid = 35;

Vm vm35 = new Vm(vmid, broker35Id, mips, user35.getnumCPU(), user35.getnumRAM(), bw, user35.getnumStorage(), vmm, new CloudletSchedulerTimeShared());

vm35.setHost(host);

//int vmid2 = 1;
//Vm vm2 = new Vm(vmid2, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
// add the VM to the vmList
vmlist.add(vm35);
//vmlist.add(vm2);
duration[35] = (int)(duration[35]*(1-relinp.get(0)));
expiretime[35] = timestamp[35] + duration[35];
CalUtinew(timestamp[35], acpu, uti, utiliz, utireal);
// submit vm list to the broker
broker35.submitVmList(vmlist);

broker35.bindCloudletToVm(cloudletid, vmid);

payprice = payprice * user35.gettime();

reimbursement = payprice * bb;
// Update the records of reimbursement and payment.
reimburse.add(reimbursement);
System.out.println(host.getmtimes().get(userid));
payment.add(payprice);

CloudSim.startSimulation();
/////////////////////
///////////////////
CloudSim.init(num_user, calendar, trace_flag);

@SuppressWarnings("unused")
Datacenter datacenter36 = createDatacenter("Datacenter_1");

DatacenterBroker broker36 = createBroker();
int broker36Id = broker36.getId();

//update totalProfit;
totalProfit += payprice;
userid = 36;
User user36 = new User(userid);
userlist.add(user36);

broker36.submitUserList(userlist);
broker36.BindUserToHost(user36.getuserId(), host.getId());

host.setTotalProfit(totalProfit);

broker36.setHostList(hostlist1);

cloudletid = 36;
Cloudlet cloudlet36 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
cloudlet36.setbrokerId(broker36Id);
//update the price of cloudlet;
//price = 3600;
//cloudlet36.setprice(price);
cloudlet36.setuser(user36);
user36.setCloudletId(cloudlet36.getCloudletId());

// add the cloudlet to the list
cloudletList.add(cloudlet36);

// submit cloudlet list to the broker
broker36.submitCloudletList(cloudletList);

//adrian
numtime = duration[36];
res = resask[36];
user36.submitCloudlet(cloudlet36);
user36.submitRequest(user36.getCloudlet(), false, res, res, (long)(res), numtime);
relinp.clear();
host.getmtimes().put(userid, 0);
for(int i = 0 ; i < 150; i++){
double a = r.nextGaussian() * 0.3 + aveRP[36];
if (a < 0 || a > 1){}
else{
relinp.add(a);
}
}
if(aveRP[36] == 0){}
else if(aveRP[36] > 0){

ArrayList<Double> se36 = new ArrayList<Double> ();
ArrayList<Double> de36 = new ArrayList<Double> ();
host.getmaop().put(userid, se36);
host.getmsop().put(userid, de36);
for(int i = 1; i < 51; i++){
host.addmaop(userid, relinp.get(i));
host.addmsop(userid, relinp.get(i));
host.addmtimes(userid);
}
}
relinp.set(0,0.759289226467618);
basicprice = user36.getnumCPU() * 0.01 + user36.getnumRAM() * 0.01 + (double)(user36.getnumStorage()*0.001);
pledge = basicprice * 1.05;
System.out.println("user" + user36.getuserId() + " asks for " + user36.getnumCPU() + " CPUs and " + user36.getnumRAM() + " GB of RAM and " + user36.getnumStorage() + " GB of storage to host" + host.getId());

utiliz = CalUti(timestamp[36], acpu, uti, expiretime, utireal);
id = 0;
if(cloudlet36.pledge == true){
//here I didn't consider giving back the extra money if the user will not relinquish any resource;
payprice = pledge;
}
else{
id = DecideModel(utiliz, aveRP[36], userid, res);
id = 1;
//host.minusmtimes(userid);
//here remember the BROKER is changing to BROKER2!!!!!!!!! and cloudlet is cloudlet2!!!!!!!!!
if (host.getmtimes().get(userid) == 0){
host.addmtimes(userid);
ArrayList<Double> aaa = new ArrayList<Double> ();
ArrayList<Double> bbb = new ArrayList<Double> ();
//here I will set some default values for aop and sop and ...;
if(host.getmaop().get(userid) == null){
broker36.CalNumOfRes(user36.getuserId(), host.getId(), 0.3, 0.3, id);
//right now we don't consider the pricing model;
//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
host.getmaop().put(userid, bbb);
}
else {
	broker36.CalNumOfRes(user36.getuserId(), host.getId(), 0.3, id);
	//right now we don't consider the pricing model;
	//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
}
host.getmsop().put(userid, aaa);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));
}
else if(host.getmtimes().get(userid) == 1){
host.addmtimes(userid);
// will add codes for sop and aop and ...;
broker36.CalNumOfRes1(user36.getuserId(), host.getId(), id);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));
//payprice = broker2.CalPrice(cloudlet2.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) == 2){
host.addmtimes(userid);
// will add codes for sop and aop and ...;
broker36.CalNumOfRes2(user36.getuserId(), host.getId(), id);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));
//payprice = broker36.CalPrice(cloudlet36.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) > 2){
broker36.CalNumOfRes(user36.getuserId(), host.getId(), id);
int a = host.getmtimes().get(userid);
host.addmtimes(userid);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));

}
}


acpu.add(user36.getnumCPU());
aram.add(user36.getnumRAM());
asto.add(user36.getnumStorage());
utimeh = host.getmtimes().get(userid) ;
System.out.println("this is the " + utimeh + "th time for user" + user36.getuserId() + " to use services of host" + host.getId());
payprice = user36.getnumCPU() * 0.01 + user36.getnumRAM() * 0.01 + (double)(user36.getnumStorage()*0.001);
vmid = 36;

Vm vm36 = new Vm(vmid, broker36Id, mips, user36.getnumCPU(), user36.getnumRAM(), bw, user36.getnumStorage(), vmm, new CloudletSchedulerTimeShared());

vm36.setHost(host);

//int vmid2 = 1;
//Vm vm2 = new Vm(vmid2, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
// add the VM to the vmList
vmlist.add(vm36);
//vmlist.add(vm2);
duration[36] = (int)(duration[36]*(1-relinp.get(0)));
expiretime[36] = timestamp[36] + duration[36];
CalUtinew(timestamp[36], acpu, uti, utiliz, utireal);
// submit vm list to the broker
broker36.submitVmList(vmlist);

broker36.bindCloudletToVm(cloudletid, vmid);

payprice = payprice * user36.gettime();

reimbursement = payprice * bb;
// Update the records of reimbursement and payment.
reimburse.add(reimbursement);
System.out.println(host.getmtimes().get(userid));
payment.add(payprice);

CloudSim.startSimulation();
/////////////////////
///////////////////
CloudSim.init(num_user, calendar, trace_flag);

@SuppressWarnings("unused")
Datacenter datacenter37 = createDatacenter("Datacenter_1");

DatacenterBroker broker37 = createBroker();
int broker37Id = broker37.getId();

//update totalProfit;
totalProfit += payprice;
userid = 37;
User user37 = new User(userid);
userlist.add(user37);

broker37.submitUserList(userlist);
broker37.BindUserToHost(user37.getuserId(), host.getId());

host.setTotalProfit(totalProfit);

broker37.setHostList(hostlist1);

cloudletid = 37;
Cloudlet cloudlet37 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
cloudlet37.setbrokerId(broker37Id);
//update the price of cloudlet;
//price = 3700;
//cloudlet37.setprice(price);
cloudlet37.setuser(user37);
user37.setCloudletId(cloudlet37.getCloudletId());

// add the cloudlet to the list
cloudletList.add(cloudlet37);

// submit cloudlet list to the broker
broker37.submitCloudletList(cloudletList);

//adrian
numtime = duration[37];
res = resask[37];
user37.submitCloudlet(cloudlet37);
user37.submitRequest(user37.getCloudlet(), false, res, res, (long)(res), numtime);
relinp.clear();
host.getmtimes().put(userid, 0);
for(int i = 0 ; i < 100; i++){
double a = r.nextGaussian() * 0.3 + aveRP[37];
if (a < 0 || a > 1){}
else{
relinp.add(a);
}
}
if(aveRP[37] == 0){}
else if(aveRP[37] > 0){

ArrayList<Double> se37 = new ArrayList<Double> ();
ArrayList<Double> de37 = new ArrayList<Double> ();
host.getmaop().put(userid, se37);
host.getmsop().put(userid, de37);
for(int i = 1; i < 51; i++){
host.addmaop(userid, relinp.get(i));
host.addmsop(userid, relinp.get(i));
host.addmtimes(userid);
}
}
relinp.set(0,0.759289226467618);
basicprice = user37.getnumCPU() * 0.01 + user37.getnumRAM() * 0.01 + (double)(user37.getnumStorage()*0.001);
pledge = basicprice * 1.05;
System.out.println("user" + user37.getuserId() + " asks for " + user37.getnumCPU() + " CPUs and " + user37.getnumRAM() + " GB of RAM and " + user37.getnumStorage() + " GB of storage to host" + host.getId());

utiliz = CalUti(timestamp[37], acpu, uti, expiretime, utireal);
id = 0;
if(cloudlet37.pledge == true){
//here I didn't consider giving back the extra money if the user will not relinquish any resource;
payprice = pledge;
}
else{
id = DecideModel(utiliz, aveRP[37], userid, res);
id = 1;
//host.minusmtimes(userid);
//here remember the BROKER is changing to BROKER2!!!!!!!!! and cloudlet is cloudlet2!!!!!!!!!
if (host.getmtimes().get(userid) == 0){
host.addmtimes(userid);
ArrayList<Double> aaa = new ArrayList<Double> ();
ArrayList<Double> bbb = new ArrayList<Double> ();
//here I will set some default values for aop and sop and ...;
if(host.getmaop().get(userid) == null){
broker37.CalNumOfRes(user37.getuserId(), host.getId(), 0.3, 0.3, id);
//right now we don't consider the pricing model;
//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
host.getmaop().put(userid, bbb);
}
else {
	broker37.CalNumOfRes(user37.getuserId(), host.getId(), 0.3, id);
	//right now we don't consider the pricing model;
	//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
}
host.getmsop().put(userid, aaa);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));
}
else if(host.getmtimes().get(userid) == 1){
host.addmtimes(userid);
// will add codes for sop and aop and ...;
broker37.CalNumOfRes1(user37.getuserId(), host.getId(), id);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));
//payprice = broker2.CalPrice(cloudlet2.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) == 2){
host.addmtimes(userid);
// will add codes for sop and aop and ...;
broker37.CalNumOfRes2(user37.getuserId(), host.getId(), id);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));
//payprice = broker37.CalPrice(cloudlet37.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) > 2){
broker37.CalNumOfRes(user37.getuserId(), host.getId(), id);
int a = host.getmtimes().get(userid);
host.addmtimes(userid);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));

}
}


acpu.add(user37.getnumCPU());
aram.add(user37.getnumRAM());
asto.add(user37.getnumStorage());
utimeh = host.getmtimes().get(userid) ;
System.out.println("this is the " + utimeh + "th time for user" + user37.getuserId() + " to use services of host" + host.getId());
payprice = user37.getnumCPU() * 0.01 + user37.getnumRAM() * 0.01 + (double)(user37.getnumStorage()*0.001);
vmid = 37;

Vm vm37 = new Vm(vmid, broker37Id, mips, user37.getnumCPU(), user37.getnumRAM(), bw, user37.getnumStorage(), vmm, new CloudletSchedulerTimeShared());

vm37.setHost(host);

//int vmid2 = 1;
//Vm vm2 = new Vm(vmid2, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
// add the VM to the vmList
vmlist.add(vm37);
//vmlist.add(vm2);
duration[37] = (int)(duration[37]*(1-relinp.get(0)));
expiretime[37] = timestamp[37] + duration[37];
CalUtinew(timestamp[37], acpu, uti, utiliz, utireal);
// submit vm list to the broker
broker37.submitVmList(vmlist);

broker37.bindCloudletToVm(cloudletid, vmid);

payprice = payprice * user37.gettime();

reimbursement = payprice * bb;
// Update the records of reimbursement and payment.
reimburse.add(reimbursement);
System.out.println(host.getmtimes().get(userid));
payment.add(payprice);

CloudSim.startSimulation();
/////////////////////
///////////////////
CloudSim.init(num_user, calendar, trace_flag);

@SuppressWarnings("unused")
Datacenter datacenter38 = createDatacenter("Datacenter_1");

DatacenterBroker broker38 = createBroker();
int broker38Id = broker38.getId();

//update totalProfit;
totalProfit += payprice;
userid = 38;
User user38 = new User(userid);
userlist.add(user38);

broker38.submitUserList(userlist);
broker38.BindUserToHost(user38.getuserId(), host.getId());

host.setTotalProfit(totalProfit);

broker38.setHostList(hostlist1);

cloudletid = 38;
Cloudlet cloudlet38 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
cloudlet38.setbrokerId(broker38Id);
//update the price of cloudlet;
//price = 3800;
//cloudlet38.setprice(price);
cloudlet38.setuser(user38);
user38.setCloudletId(cloudlet38.getCloudletId());

// add the cloudlet to the list
cloudletList.add(cloudlet38);

// submit cloudlet list to the broker
broker38.submitCloudletList(cloudletList);

//adrian
numtime = duration[38];
res = resask[38];
user38.submitCloudlet(cloudlet38);
user38.submitRequest(user38.getCloudlet(), false, res, res, (long)(res), numtime);
relinp.clear();
host.getmtimes().put(userid, 0);
for(int i = 0 ; i < 100; i++){
double a = r.nextGaussian() * 0.3 + aveRP[38];
if (a < 0 || a > 1){}
else{
relinp.add(a);
}
}
if(aveRP[38] == 0){}
else if(aveRP[38] > 0){

ArrayList<Double> se38 = new ArrayList<Double> ();
ArrayList<Double> de38 = new ArrayList<Double> ();
host.getmaop().put(userid, se38);
host.getmsop().put(userid, de38);
for(int i = 1; i < 51; i++){
host.addmaop(userid, relinp.get(i));
host.addmsop(userid, relinp.get(i));
host.addmtimes(userid);
}
}
relinp.set(0,0.759289226467618);
basicprice = user38.getnumCPU() * 0.01 + user38.getnumRAM() * 0.01 + (double)(user38.getnumStorage()*0.001);
pledge = basicprice * 1.05;
System.out.println("user" + user38.getuserId() + " asks for " + user38.getnumCPU() + " CPUs and " + user38.getnumRAM() + " GB of RAM and " + user38.getnumStorage() + " GB of storage to host" + host.getId());

utiliz = CalUti(timestamp[38], acpu, uti, expiretime, utireal);
id = 0;
if(cloudlet38.pledge == true){
//here I didn't consider giving back the extra money if the user will not relinquish any resource;
payprice = pledge;
}
else{
id = DecideModel(utiliz, aveRP[38], userid, res);
id = 1;
//host.minusmtimes(userid);
//here remember the BROKER is changing to BROKER2!!!!!!!!! and cloudlet is cloudlet2!!!!!!!!!
if (host.getmtimes().get(userid) == 0){
host.addmtimes(userid);
ArrayList<Double> aaa = new ArrayList<Double> ();
ArrayList<Double> bbb = new ArrayList<Double> ();
//here I will set some default values for aop and sop and ...;
if(host.getmaop().get(userid) == null){
broker38.CalNumOfRes(user38.getuserId(), host.getId(), 0.3, 0.3, id);
//right now we don't consider the pricing model;
//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
host.getmaop().put(userid, bbb);
}
else {
	broker38.CalNumOfRes(user38.getuserId(), host.getId(), 0.3, id);
	//right now we don't consider the pricing model;
	//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
}
host.getmsop().put(userid, aaa);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));
}
else if(host.getmtimes().get(userid) == 1){
host.addmtimes(userid);
// will add codes for sop and aop and ...;
broker38.CalNumOfRes1(user38.getuserId(), host.getId(), id);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));
//payprice = broker2.CalPrice(cloudlet2.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) == 2){
host.addmtimes(userid);
// will add codes for sop and aop and ...;
broker38.CalNumOfRes2(user38.getuserId(), host.getId(), id);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));
//payprice = broker38.CalPrice(cloudlet38.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) > 2){
broker38.CalNumOfRes(user38.getuserId(), host.getId(), id);
int a = host.getmtimes().get(userid);
host.addmtimes(userid);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));

}
}


acpu.add(user38.getnumCPU());
aram.add(user38.getnumRAM());
asto.add(user38.getnumStorage());
utimeh = host.getmtimes().get(userid) ;
System.out.println("this is the " + utimeh + "th time for user" + user38.getuserId() + " to use services of host" + host.getId());
payprice = user38.getnumCPU() * 0.01 + user38.getnumRAM() * 0.01 + (double)(user38.getnumStorage()*0.001);
vmid = 38;

Vm vm38 = new Vm(vmid, broker38Id, mips, user38.getnumCPU(), user38.getnumRAM(), bw, user38.getnumStorage(), vmm, new CloudletSchedulerTimeShared());

vm38.setHost(host);

//int vmid2 = 1;
//Vm vm2 = new Vm(vmid2, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
// add the VM to the vmList
vmlist.add(vm38);
//vmlist.add(vm2);
duration[38] = (int)(duration[38]*(1-relinp.get(0)));
expiretime[38] = timestamp[38] + duration[38];
CalUtinew(timestamp[38], acpu, uti, utiliz, utireal);
// submit vm list to the broker
broker38.submitVmList(vmlist);

broker38.bindCloudletToVm(cloudletid, vmid);

payprice = payprice * user38.gettime();

reimbursement = payprice * bb;
// Update the records of reimbursement and payment.
reimburse.add(reimbursement);
System.out.println(host.getmtimes().get(userid));
payment.add(payprice);

CloudSim.startSimulation();
/////////////////////
///////////////////
CloudSim.init(num_user, calendar, trace_flag);

@SuppressWarnings("unused")
Datacenter datacenter39 = createDatacenter("Datacenter_1");

DatacenterBroker broker39 = createBroker();
int broker39Id = broker39.getId();

//update totalProfit;
totalProfit += payprice;
userid = 39;
User user39 = new User(userid);
userlist.add(user39);

broker39.submitUserList(userlist);
broker39.BindUserToHost(user39.getuserId(), host.getId());

host.setTotalProfit(totalProfit);

broker39.setHostList(hostlist1);

cloudletid = 39;
Cloudlet cloudlet39 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
cloudlet39.setbrokerId(broker39Id);
//update the price of cloudlet;
//price = 3900;
//cloudlet39.setprice(price);
cloudlet39.setuser(user39);
user39.setCloudletId(cloudlet39.getCloudletId());

// add the cloudlet to the list
cloudletList.add(cloudlet39);

// submit cloudlet list to the broker
broker39.submitCloudletList(cloudletList);

//adrian
numtime = duration[39];
res = resask[39];
user39.submitCloudlet(cloudlet39);
user39.submitRequest(user39.getCloudlet(), false, res, res, (long)(res), numtime);
relinp.clear();
host.getmtimes().put(userid, 0);
for(int i = 0 ; i < 100; i++){
double a = r.nextGaussian() * 0.3 + aveRP[39];
if (a < 0 || a > 1){}
else{
relinp.add(a);
}
}
if(aveRP[39] == 0){}
else if(aveRP[39] > 0){

ArrayList<Double> se39 = new ArrayList<Double> ();
ArrayList<Double> de39 = new ArrayList<Double> ();
host.getmaop().put(userid, se39);
host.getmsop().put(userid, de39);
for(int i = 1; i < 51; i++){
host.addmaop(userid, relinp.get(i));
host.addmsop(userid, relinp.get(i));
host.addmtimes(userid);
}
}
relinp.set(0,0.759289226467618);
basicprice = user39.getnumCPU() * 0.01 + user39.getnumRAM() * 0.01 + (double)(user39.getnumStorage()*0.001);
pledge = basicprice * 1.05;
System.out.println("user" + user39.getuserId() + " asks for " + user39.getnumCPU() + " CPUs and " + user39.getnumRAM() + " GB of RAM and " + user39.getnumStorage() + " GB of storage to host" + host.getId());

utiliz = CalUti(timestamp[39], acpu, uti, expiretime, utireal);
id = 0;
if(cloudlet39.pledge == true){
//here I didn't consider giving back the extra money if the user will not relinquish any resource;
payprice = pledge;
}
else{
id = DecideModel(utiliz, aveRP[39], userid, res);
id = 1;
//host.minusmtimes(userid);
//here remember the BROKER is changing to BROKER2!!!!!!!!! and cloudlet is cloudlet2!!!!!!!!!
if (host.getmtimes().get(userid) == 0){
host.addmtimes(userid);
ArrayList<Double> aaa = new ArrayList<Double> ();
ArrayList<Double> bbb = new ArrayList<Double> ();
//here I will set some default values for aop and sop and ...;
if(host.getmaop().get(userid) == null){
broker39.CalNumOfRes(user39.getuserId(), host.getId(), 0.3, 0.3, id);
//right now we don't consider the pricing model;
//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
host.getmaop().put(userid, bbb);
}
else {
	broker39.CalNumOfRes(user39.getuserId(), host.getId(), 0.3, id);
	//right now we don't consider the pricing model;
	//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
}
host.getmsop().put(userid, aaa);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));
}
else if(host.getmtimes().get(userid) == 1){
host.addmtimes(userid);
// will add codes for sop and aop and ...;
broker39.CalNumOfRes1(user39.getuserId(), host.getId(), id);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));
//payprice = broker2.CalPrice(cloudlet2.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) == 2){
host.addmtimes(userid);
// will add codes for sop and aop and ...;
broker39.CalNumOfRes2(user39.getuserId(), host.getId(), id);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));
//payprice = broker39.CalPrice(cloudlet39.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) > 2){
broker39.CalNumOfRes(user39.getuserId(), host.getId(), id);
int a = host.getmtimes().get(userid);
host.addmtimes(userid);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));

}
}


acpu.add(user39.getnumCPU());
aram.add(user39.getnumRAM());
asto.add(user39.getnumStorage());
utimeh = host.getmtimes().get(userid) ;
System.out.println("this is the " + utimeh + "th time for user" + user39.getuserId() + " to use services of host" + host.getId());
payprice = user39.getnumCPU() * 0.01 + user39.getnumRAM() * 0.01 + (double)(user39.getnumStorage()*0.001);
vmid = 39;

Vm vm39 = new Vm(vmid, broker39Id, mips, user39.getnumCPU(), user39.getnumRAM(), bw, user39.getnumStorage(), vmm, new CloudletSchedulerTimeShared());

vm39.setHost(host);

//int vmid2 = 1;
//Vm vm2 = new Vm(vmid2, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
// add the VM to the vmList
vmlist.add(vm39);
//vmlist.add(vm2);
duration[39] = (int)(duration[39]*(1-relinp.get(0)));
expiretime[39] = timestamp[39] + duration[39];
CalUtinew(timestamp[39], acpu, uti, utiliz, utireal);
// submit vm list to the broker
broker39.submitVmList(vmlist);

broker39.bindCloudletToVm(cloudletid, vmid);

payprice = payprice * user39.gettime();

reimbursement = payprice * bb;
// Update the records of reimbursement and payment.
reimburse.add(reimbursement);
System.out.println(host.getmtimes().get(userid));
payment.add(payprice);

CloudSim.startSimulation();
/////////////////////
///////////////////
CloudSim.init(num_user, calendar, trace_flag);

@SuppressWarnings("unused")
Datacenter datacenter40 = createDatacenter("Datacenter_1");

DatacenterBroker broker40 = createBroker();
int broker40Id = broker40.getId();

//update totalProfit;
totalProfit += payprice;
userid = 40;
User user40 = new User(userid);
userlist.add(user40);

broker40.submitUserList(userlist);
broker40.BindUserToHost(user40.getuserId(), host.getId());

host.setTotalProfit(totalProfit);

broker40.setHostList(hostlist1);

cloudletid = 40;
Cloudlet cloudlet40 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
cloudlet40.setbrokerId(broker40Id);
//update the price of cloudlet;
//price = 4000;
//cloudlet40.setprice(price);
cloudlet40.setuser(user40);
user40.setCloudletId(cloudlet40.getCloudletId());

// add the cloudlet to the list
cloudletList.add(cloudlet40);

// submit cloudlet list to the broker
broker40.submitCloudletList(cloudletList);

//adrian
numtime = duration[40];
res = resask[40];
user40.submitCloudlet(cloudlet40);
user40.submitRequest(user40.getCloudlet(), false, res, res, (long)(res), numtime);
relinp.clear();
host.getmtimes().put(userid, 0);
for(int i = 0 ; i < 100; i++){
double a = r.nextGaussian() * 0.3 + aveRP[40];
if (a < 0 || a > 1){}
else{
relinp.add(a);
}
}
if(aveRP[40] == 0){}
else if(aveRP[40] > 0){

ArrayList<Double> se40 = new ArrayList<Double> ();
ArrayList<Double> de40 = new ArrayList<Double> ();
host.getmaop().put(userid, se40);
host.getmsop().put(userid, de40);
for(int i = 1; i < 51; i++){
host.addmaop(userid, relinp.get(i));
host.addmsop(userid, relinp.get(i));
host.addmtimes(userid);
}
}
relinp.set(0,0.759289226467618);
basicprice = user40.getnumCPU() * 0.01 + user40.getnumRAM() * 0.01 + (double)(user40.getnumStorage()*0.001);
pledge = basicprice * 1.05;
System.out.println("user" + user40.getuserId() + " asks for " + user40.getnumCPU() + " CPUs and " + user40.getnumRAM() + " GB of RAM and " + user40.getnumStorage() + " GB of storage to host" + host.getId());

utiliz = CalUti(timestamp[40], acpu, uti, expiretime, utireal);
id = 0;
if(cloudlet40.pledge == true){
//here I didn't consider giving back the extra money if the user will not relinquish any resource;
payprice = pledge;
}
else{
id = DecideModel(utiliz, aveRP[40], userid, res);
id = 1;
//host.minusmtimes(userid);
//here remember the BROKER is changing to BROKER2!!!!!!!!! and cloudlet is cloudlet2!!!!!!!!!
if (host.getmtimes().get(userid) == 0){
host.addmtimes(userid);
ArrayList<Double> aaa = new ArrayList<Double> ();
ArrayList<Double> bbb = new ArrayList<Double> ();
//here I will set some default values for aop and sop and ...;
if(host.getmaop().get(userid) == null){
broker40.CalNumOfRes(user40.getuserId(), host.getId(), 0.3, 0.3, id);
//right now we don't consider the pricing model;
//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
host.getmaop().put(userid, bbb);
}
else {
	broker40.CalNumOfRes(user40.getuserId(), host.getId(), 0.3, id);
	//right now we don't consider the pricing model;
	//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
}
host.getmsop().put(userid, aaa);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));
}
else if(host.getmtimes().get(userid) == 1){
host.addmtimes(userid);
// will add codes for sop and aop and ...;
broker40.CalNumOfRes1(user40.getuserId(), host.getId(), id);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));
//payprice = broker2.CalPrice(cloudlet2.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) == 2){
host.addmtimes(userid);
// will add codes for sop and aop and ...;
broker40.CalNumOfRes2(user40.getuserId(), host.getId(), id);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));
//payprice = broker40.CalPrice(cloudlet40.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) > 2){
broker40.CalNumOfRes(user40.getuserId(), host.getId(), id);
int a = host.getmtimes().get(userid);
host.addmtimes(userid);
host.addmsop(userid, relinp.get(0));
host.addmaop(userid, relinp.get(0));

}
}


acpu.add(user40.getnumCPU());
aram.add(user40.getnumRAM());
asto.add(user40.getnumStorage());
utimeh = host.getmtimes().get(userid) ;
System.out.println("this is the " + utimeh + "th time for user" + user40.getuserId() + " to use services of host" + host.getId());
payprice = user40.getnumCPU() * 0.01 + user40.getnumRAM() * 0.01 + (double)(user40.getnumStorage()*0.001);
vmid = 40;

Vm vm40 = new Vm(vmid, broker40Id, mips, user40.getnumCPU(), user40.getnumRAM(), bw, user40.getnumStorage(), vmm, new CloudletSchedulerTimeShared());

vm40.setHost(host);

//int vmid2 = 1;
//Vm vm2 = new Vm(vmid2, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
// add the VM to the vmList
vmlist.add(vm40);
//vmlist.add(vm2);
duration[40] = (int)(duration[40]*(1-relinp.get(0)));
expiretime[40] = timestamp[40] + duration[40];
CalUtinew(timestamp[40], acpu, uti, utiliz, utireal);
// submit vm list to the broker
broker40.submitVmList(vmlist);

broker40.bindCloudletToVm(cloudletid, vmid);

payprice = payprice * user40.gettime();

reimbursement = payprice * bb;
// Update the records of reimbursement and payment.
reimburse.add(reimbursement);
System.out.println(host.getmtimes().get(userid));
payment.add(payprice);

CloudSim.startSimulation();
////////////////////
////////////////////
			int ac=0, ar = 0;
			long ast = 0;
			for (Integer record : acpu) {
				ac += record;
			}
			for (Integer record : aram) {
				ar += record;
			}
			for (Long record : asto) {
				ast += record;
			}
			int avecpu = ac/acpu.size();
			int averam = ar/aram.size();
			long avesto = ast/asto.size();
			System.out.println("averageCPU " + avecpu + "averageRAM " + averam + "averagesto " + avesto);
			//double sd = broker.calculatevariance(payment);
			//System.out.println("the variance of payment is " + sd);
			for (int i = 0; i<utireal.size();i++){
				System.out.println(utireal.get(i));
			}
			double paymentAll = 0.0;
			double scoreAll = 0.0;
			for (Double record : reimburse) {
				scoreAll += record;
			}
			for (Double aaaa : payment) {
				paymentAll += aaaa;
			}
			double average = scoreAll/reimburse.size();
			double payave = paymentAll/payment.size();
			System.out.println("reimburse average is " + average);
			System.out.println("the provider earned " + (payave-average));
			System.out.println(avecpu);
			System.out.println(payave);
			System.out.println(String.format("%.2f", payave-average));
			CloudSim.stopSimulation();
			//Final step: Print results when simulation is over
			List<Cloudlet> newList = broker.getCloudletReceivedList();
			printCloudletList(cloudletList);
			
			Log.printLine("CloudSimExample1 finished!");
		}
	/**
	 * Read the file and input the parameters into the program
	 *
	 * @param the filepath of the parameter(the text file)
	 *
	 * @return the array of each kind of parameter
	 */
	public static final int[] readFile(String filePath) {
        File f = new File(filePath);
        if(!f.exists()) {
            System.err.println("File " + f.getName() + " not found");
            return null;
        }

        StringBuilder buffer = new StringBuilder();
        byte[] bytes = new byte[1024];
        int readBytesCount = 0;
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(f));
            while( (readBytesCount = in.read(bytes)) != -1) {
                buffer.append(new String(bytes, 0, readBytesCount));
            }
        } catch(Exception exc) {
            exc.printStackTrace();
            return null;
        }
        String s = buffer.toString().trim();
        String[] a = s.split(",");
        int aa = a.length;
        int[] b = new int[aa];
        for(int i=0;i<a.length;i++){
        		b[i] = Integer.parseInt(a[i]);
        }
        return b;
    }
	
	public static final double[] DreadFile(String filePath) {
        File f = new File(filePath);
        if(!f.exists()) {
            System.err.println("File " + f.getName() + " not found");
            return null;
        }

        StringBuilder buffer = new StringBuilder();
        byte[] bytes = new byte[1024];
        int readBytesCount = 0;
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(f));
            while( (readBytesCount = in.read(bytes)) != -1) {
                buffer.append(new String(bytes, 0, readBytesCount));
            }
        } catch(Exception exc) {
            exc.printStackTrace();
            return null;
        }
        String s = buffer.toString().trim();
        String[] a = s.split(",");
        int aa = a.length;
        double[] b = new double[aa];
        for(int i=0;i<a.length;i++){
        		b[i] = Double.parseDouble(a[i]);
        }
        return b;
    }
	
	
	
	public static final double CalUti(int timestamp, ArrayList<Integer> res, HashMap<Integer, Double> uti, int[] expiretime, TreeMap<Integer, Double> utireal) {
		if(timestamp == 0){
			return 0;
		}
		else{
			int lastentry = uti.size()-1;
			double utinow = uti.get(lastentry);
			int i = 0;
			while(expiretime[i]!=0){
				if(expiretime[i] <= timestamp && expiretime[i] != -1){
					utinow = utinow - res.get(i)*0.001;
					utireal.put(expiretime[i], utinow);
					expiretime[i] = -1;
				}
				else {
				}
				i++;
			}
			return utinow;
		}
	}
	
	public static final void CalUtinew(int timestamp, ArrayList<Integer> res, HashMap<Integer, Double> uti, double utinow, TreeMap<Integer, Double> utireal) {
		//remember the lastentry of res and expiretime are equal, but different to the lastentry of uti since it is now updated yet.
		if(timestamp == 0){
			// If there is no record meaning it is new, then just add record to it.
			uti.put(0, res.get(0)*0.001);
			utireal.put(0, res.get(0)*0.001);
		}
		else{
			// If there are records, first find the last record, and update it and then append the new record to the map with timestamp.
			int lastentry = res.size()-1;
			int resnow = res.get(lastentry);
			utinow = utinow + resnow*0.001;
			uti.put(lastentry, utinow);
			utireal.put(timestamp, utinow);
		}
		
	}
	
	
	public static final int DecideModel(double uti, double aveRP, int userid, int res){
		if(uti<=0.25){
			return 2;
		}
		else if(uti>=0.75){
			return 1;
		}
		else{
			if(aveRP==0){
				return 3;
			}
			else if(0 < aveRP&&aveRP <= 0.5){
				return 4;
			}
			else if(aveRP > 0.5&& aveRP <=1){
				if(res>22){
					return 3;
				}
				else{
					return 4;
				}
			}
			return 0;
		}
	}
	/**
	 * Creates the datacenter.
	 *
	 * @param name the name
	 *
	 * @return the datacenter
	 */
	private static Datacenter createDatacenter(String name) {

		// Here are the steps needed to create a PowerDatacenter:
		// 1. We need to create a list to store
		// our machine
		List<Host> hostList = new ArrayList<Host>();

		// 2. A Machine contains one or more PEs or CPUs/Cores.
		// In this example, it will have only one core.
		List<Pe> peList = new ArrayList<Pe>();

		int mips = 1000;

		// 3. Create PEs and add these into a list.
		for (int i = 0; i < 1000; i++){
			//create more pes so that the vm can have the value more than 1 of the pesNumber;
			peList.add(new Pe(i, new PeProvisionerSimple(mips)));
		}
		 // need to store Pe id and MIPS Rating
		
		// 4. Create Host with its id and list of PEs and add them to the list
		// of machines
		int hostId = 0;
		int ram = 2048; // host memory (MB)
		long storage = 1000000; // host storage
		int bw = 10000;

		
		Host host = new Host(hostId,
				new RamProvisionerSimple(ram),
				new BwProvisionerSimple(bw),
				storage,
				peList,
				new VmSchedulerTimeShared(peList));
		host.setTotalProfit(0);
		hostList.add(host); // This is our machine

		// 5. Create a DatacenterCharacteristics object that stores the
		// properties of a data center: architecture, OS, list of
		// Machines, allocation policy: time- or space-shared, time zone
		// and its price (G$/Pe time unit).
		String arch = "x86"; // system architecture
		String os = "Linux"; // operating system
		String vmm = "Xen";
		double time_zone = 10.0; // time zone this resource located
		double cost = 3.0; // the cost of using processing in this resource
		double costPerMem = 0.05; // the cost of using memory in this resource
		double costPerStorage = 0.001; // the cost of using storage in this
										// resource
		double costPerBw = 0.0; // the cost of using bw in this resource
		LinkedList<Storage> storageList = new LinkedList<Storage>(); // we are not adding SAN
													// devices by now

		DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
				arch, os, vmm, hostList, time_zone, cost, costPerMem,
				costPerStorage, costPerBw);

		// 6. Finally, we need to create a PowerDatacenter object.
		Datacenter datacenter = null;
		try {
			datacenter = new Datacenter(name, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return datacenter;
	}

	// We strongly encourage users to develop their own broker policies, to
	// submit vms and cloudlets according
	// to the specific rules of the simulated scenario
	/**
	 * Creates the broker.
	 *
	 * @return the datacenter broker
	 */
	private static DatacenterBroker createBroker() {
		DatacenterBroker broker = null;
		try {
			broker = new DatacenterBroker("Broker");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return broker;
	}

	/**
	 * Prints the Cloudlet objects.
	 *
	 * @param list list of Cloudlets
	 */
	private static void printCloudletList(List<Cloudlet> list) {
		int size = list.size();
		Cloudlet cloudlet;

		String indent = "    ";
		Log.printLine();
		Log.printLine("========== OUTPUT ==========");
		Log.printLine("Cloudlet ID" + indent + "STATUS" + indent
				+ "Data center ID" + indent + "VM ID" + indent + "Time" + indent
				+ "Start Time" + indent + "Finish Time");

		DecimalFormat dft = new DecimalFormat("###.##");
		for (int i = 0; i < size; i++) {
			cloudlet = list.get(i);
			Log.print(indent + cloudlet.getCloudletId() + indent + indent);

			if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS) {
				Log.print("SUCCESS");

				Log.printLine(indent + indent + cloudlet.getResourceId()
						+ indent + indent + indent + cloudlet.getVmId()
						+ indent + indent
						+ dft.format(cloudlet.getActualCPUTime()) + indent
						+ indent + dft.format(cloudlet.getExecStartTime())
						+ indent + indent
						+ dft.format(cloudlet.getFinishTime()));
			}
		}
	}

}