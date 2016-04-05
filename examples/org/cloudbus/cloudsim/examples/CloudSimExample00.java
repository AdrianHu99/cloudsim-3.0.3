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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.io.*;

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
public class CloudSimExample00 {

	
	
	
	/** The cloudlet list. */
	private static List<Cloudlet> cloudletList;

	/** The vmlist. */
	private static List<Vm> vmlist;
	
	private static List<User> userlist;
	
	private static List<Host> hostlist1;

	/**
	 * Creates main() to run this example.
	 *
	 * @param args the args
	 */
	
	public static void main(String[] args) {
		
		/*int[] timestamp = readFile("/home/adrianhu/data/time");
		int[] duration = readFile("/home/adrianhu/data/duration");
		int [] resask = readFile("/home/adrianhu/data/resources");*/
		
		
		
		
		/////////////creates some datasets for collecting data
		Random r = new Random();
		ArrayList<Integer> acpu = new ArrayList<Integer> ();
		ArrayList<Integer> aram = new ArrayList<Integer> ();
		ArrayList<Long> asto = new ArrayList<Long> ();
		ArrayList<Double> reimburse = new ArrayList<Double> ();
		ArrayList<Double> payment = new ArrayList<Double> ();
		ArrayList<Double> relinp = new ArrayList<Double> ();
		
		
		
		
		
		
		/*double[] dddd = {
				0.17420717555954304,0.6796464520685601,0.3046389840089347,0.3135013768048024,0.08632956232885294, 
				0.4821213386048301,0.5556507163891892,0.4980677634454449,0.8536793028322704,0.3536081438034621, 
				0.2070221065534352,0.513119582254405,0.28324665587805475,0.22146323940792528,0.3569166307707895, 
				0.4701439231516571,0.32530418825180774,0.38601519479269275,0.3324710353250233,0.044869341125193085, 
				0.15602306294772297,0.7068317660319127,0.6661958170212816,0.4389452572473278,0.23188117622048315, 
				0.26808642564521523,0.5557509516220587,0.12625202637369923,0.45393633461613414,0.5935559979907541, 
				0.3555894252945264,0.3984221034836199,0.5124717796724407,4.0618478603732244E-4,0.37197904757889066, 
				0.10047966514701054,0.5810484875196904,0.3558738661517059,0.3045223745840083,0.3091858044126561, 
				0.372987274063576,0.06029324161040561,0.22721715092443534,0.9342564324550591,0.06196120027581345, 
				0.04323044461063147,0.19394986997639568,0.489347607336296,0.20270815478234122,0.27789920462457673, 
				0.003787652532095731,0.2229919327450844,0.6825407301188657,0.3094071596268823,0.6310355726009035, 
				0.24659865410095289,0.5756288088867134,0.1909517534241138,0.3299804117441606,0.1730567806839697, 
				0.19896893658346365,0.4684847650535585,0.40536045909158736,0.36754580982411683,0.4378112970646255, 
				0.5227509030008429,0.4463107544252173,0.5166399978744487,0.5084262518106217,0.7238309435744215, 
				0.27058055351312194,0.05592388440811916,0.4235584584911962,0.15137011343336418,0.6345796517683723, 
				0.49566825698373584,0.26500583254055177,0.2597647339033765,0.8414211358408517,0.04355225688899861, 
				0.5067601843888623,0.8642829121907374,0.17779169817980306,0.28929782935352943,0.2536570109452562, 
				0.11746922463931309,0.7023277051477748,0.5493390392342602,0.23495672941732282,0.4376651472642129, 
				0.17971112767404274,0.5046204411437387,0.11201121005504677,0.20120328844472396,0.21762381631545194
				};
		
		
		
		for (int i = 0; i<dddd.length;i++){
			relinp.add(dddd[i]);
		}*/

		//standard deviation is 0.3, and the average is 0.5;
		//70% of the value will be between 0 to 1; 95% of the value will be between -0.5 and 1.5;
		
		for(int i = 0 ; i < 400; i++){
			double a = r.nextGaussian() * 0.3 + 0.9;
			if (a < 0 || a > 1){}
			else{
				relinp.add(a);
			}
		}
		
		
	
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
			
			int pesNumber = 300; // number of cpus
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
			double numtime = 10;
			int numram = 300;
			user.setnumtime(numtime);
			user.submitCloudlet(cloudlet);
			user.submitRequest(user.getCloudlet(), false, 300, 300, (long)(300), numtime);
			
			//set user's aop as not null
			host.getmtimes().put(userid, 0);
			
			ArrayList<Double> se = new ArrayList<Double> ();
			ArrayList<Double> de = new ArrayList<Double> ();
			host.getmaop().put(userid, se);
			host.getmsop().put(userid, de);
			for(int i = 0; i < 35; i++){
				host.addmaop(userid, relinp.get(i));
				host.addmsop(userid, relinp.get(i));
				host.addmtimes(userid);
			}
			
			//here I set the basic price of the task; 0.01 dollar per hour per CPU, 0.01 dollar per hour per GB of RAM, 1*0.001 dollar per hour per GB of Storage;
			//maybe I need to write a function to calculate the price of the task;
			/*double basicprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()*0.001);
			double pledge = basicprice * 1.05;*/
			
			System.out.println("user" + user.getuserId() + " asks for " + user.getnumCPU() + " CPUs and " + user.getnumRAM() + " GB of RAM and " + user.getnumStorage() + " GB of storage to host" + host.getId());
			//int num = 0;
			double payprice = 0;
			double priceA = 0;
			int id = 4;
			if(cloudlet.pledge == true){
				//here I didn't consider giving back the extra money if the user will not relinquish any resource;
				///////////the user is an old user////////////////////
				int a = host.getmtimes().get(userid);
				priceA = broker.REpledge(user.getuserId(), host.getId(), id);
				host.addmtimes(userid);
				host.addmsop(userid, relinp.get(a));
				host.addmaop(userid, relinp.get(a));
				
				
				
				int utimeh = host.getmtimes().get(userid) ;
				System.out.println("this is the " + utimeh + "th time for user" + user.getuserId() + " to use services of host" + host.getId());
				payprice = (user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()*0.001) - priceA) * 1.1 + priceA;
				acpu.add(user.getnumCPU());
				aram.add(user.getnumRAM());
				asto.add(user.getnumStorage());
			}
			else{
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
					host.addmsop(userid, relinp.get(1));
					host.addmaop(userid, relinp.get(1));
					//right now we don't consider the pricing model;
					//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
				}
				else if(host.getmtimes().get(userid) == 2){
					host.addmtimes(userid);
					// will add codes for sop and aop and ...;
					broker.CalNumOfRes2(user.getuserId(), host.getId(), id);
					host.addmsop(userid, relinp.get(2));
					host.addmaop(userid, relinp.get(2));
					//right now we don't consider the pricing model;
					//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
				}
				else if(host.getmtimes().get(userid) > 2){
					int a = host.getmtimes().get(userid);
					broker.CalNumOfRes(user.getuserId(), host.getId(), id);
					host.addmtimes(userid);
					host.addmsop(userid, relinp.get(a));
					host.addmaop(userid, relinp.get(a));
					//right now we don't consider the pricing model;
				}
				
				int utimeh = host.getmtimes().get(userid) ;
				System.out.println("this is the " + utimeh + "th time for user" + user.getuserId() + " to use services of host" + host.getId());
				payprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()*0.001);
				acpu.add(user.getnumCPU());
				aram.add(user.getnumRAM());
				asto.add(user.getnumStorage());
				
				
			}
			
			int utimeh = host.getmtimes().get(userid) ;
			
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
						
						//int vmid2 = 1;
						//Vm vm2 = new Vm(vmid2, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
						// add the VM to the vmList
						vmlist.add(vm);
						//vmlist.add(vm2);

						// submit vm list to the broker
						broker.submitVmList(vmlist);
						
						broker.bindCloudletToVm(cloudletid, vmid);
						payprice = payprice * user.gettime();
						int aa = utimeh -1;
						double bb = relinp.get(aa);
						System.out.println("the relinquish probability of the user for this time is " + bb);
						//System.out.println(bb);
						//the amount of resources that the user will relinquish this time
						
						//although the unit of RAM and Storage is MB is the default configuration, but here I still use GB because of Amazon EC2's pricing model;
						System.out.println("host" + host.getId() + " will give " + user.getnumCPU() + " CPUs and " +user.getnumRAM()+ " GB of RAM and " + user.getnumStorage() + " GB of storage to user" + userid + " to finish his cloudlet " + cloudlet.getCloudletId());
						System.out.println("the user will pay " + payprice + " dollars to the broker.");
						double reimbursement = 0;
						if(cloudlet.pledge == true){
							 reimbursement = priceA * bb * user.gettime();
						}else{
							 reimbursement = payprice * bb;
						}
						
						double etime = numtime * (1-bb);
						System.out.println("the expire time is the start time adding" + etime);
						System.out.println("the broker will reimburse" + reimbursement + " to the customer");
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
			
			broker1.submitUserList(userlist);
			broker1.BindUserToHost(user.getuserId(), host.getId());
			
			host.setTotalProfit(totalProfit);
			
			broker1.setHostList(hostlist1);
			
			cloudletid = 1;
			Cloudlet cloudlet1 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
			cloudlet1.setbrokerId(broker1Id);
			cloudlet1.setExecStartTime(15);
			//update the price of cloudlet;
			//price = 200;
			//cloudlet1.setprice(price);
			cloudlet1.setuser(user);
			user.setCloudletId(cloudlet1.getCloudletId());
			
			// add the cloudlet to the list
			cloudletList.add(cloudlet1);

			// submit cloudlet list to the broker
			broker1.submitCloudletList(cloudletList);
			
			//adrian
			
			user.submitCloudlet(cloudlet1);
			user.submitRequest(user.getCloudlet(), false, pesNumber, numram, (long)(fileSize/1000000000), numtime);
			
			
			double basicprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()*0.001);
			double pledge = basicprice * 1.05;
			System.out.println("user" + user.getuserId() + " asks for " + user.getnumCPU() + " CPUs and " + user.getnumRAM() + " GB of RAM and " + user.getnumStorage() + " GB of storage to host" + host.getId());

			
			if(cloudlet1.pledge == true){
				//here I didn't consider giving back the extra money if the user will not relinquish any resource;
				payprice = pledge;
			}
			else{
				//host.minusmtimes(userid);
				//here remember the BROKER is changing to BROKER1!!!!!!!!! and cloudlet is cloudlet1!!!!!!!!!
				if (host.getmtimes().get(userid) == 0){
					host.addmtimes(userid);
					ArrayList<Double> aaa = new ArrayList<Double> ();
					ArrayList<Double> bbb = new ArrayList<Double> ();
					//here I will set some default values for aop and sop and ...;
					if(host.getmaop().get(userid) == null){
					broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3, 0.3,id);
					//right now we don't consider the pricing model;
					//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
					host.getmaop().put(userid, bbb);
					}
					else {
						broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3,id);
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
					broker1.CalNumOfRes1(user.getuserId(), host.getId(),id);
					host.addmsop(userid, relinp.get(1));
					host.addmaop(userid, relinp.get(1));
					//payprice = broker1.CalPrice(cloudlet1.getCloudletId(), host.getId());
				}
				else if(host.getmtimes().get(userid) == 2){
					host.addmtimes(userid);
					// will add codes for sop and aop and ...;
					broker1.CalNumOfRes2(user.getuserId(), host.getId(),id);
					host.addmsop(userid, relinp.get(2));
					host.addmaop(userid, relinp.get(2));
					//payprice = broker1.CalPrice(cloudlet1.getCloudletId(), host.getId());
				}
				else if(host.getmtimes().get(userid) > 2){
					broker1.CalNumOfRes(user.getuserId(), host.getId(),id);
					int a = host.getmtimes().get(userid);
					host.addmtimes(userid);
					host.addmsop(userid, relinp.get(a));
					host.addmaop(userid, relinp.get(a));
					
				}
			}

			utimeh = host.getmtimes().get(userid) ;
			System.out.println("this is the " + utimeh + "th time for user" + user.getuserId() + " to use services of host" + host.getId());
			payprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()*0.001);
			vmid = 1;
			acpu.add(user.getnumCPU());
			aram.add(user.getnumRAM());
			asto.add(user.getnumStorage());
			Vm vm1 = new Vm(vmid, broker1.getId(), mips, user.getnumCPU(), user.getnumRAM(), bw, user.getnumStorage(), vmm, new CloudletSchedulerTimeShared());
			
			vm1.setHost(host);
			
			//int vmid2 = 1;
			//Vm vm2 = new Vm(vmid2, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
			// add the VM to the vmList
			vmlist.add(vm1);
			//vmlist.add(vm2);

			// submit vm list to the broker
			broker1.submitVmList(vmlist);
			
			broker1.bindCloudletToVm(cloudletid, vmid);
			
			payprice = payprice * user.gettime();

			aa = utimeh -1;
			bb = relinp.get(aa);
			System.out.println("the relinquish probability of the user for this time is " + bb);
			//System.out.println(bb);
			//d = (int) (user.getnumCPU() * bb);
			//e = (int) (user.getnumRAM() * bb);
			//f = (long) (user.getnumStorage() * bb);
			System.out.println("host " + host.getId() + " will give " + user.getnumCPU() + " CPUs and " +user.getnumRAM()+ " GB of RAM and " + user.getnumStorage() + " GB of storage to user" + userid + " to finish his cloudlet " + cloudlet1.getCloudletId());
			System.out.println("the user will pay " + payprice + " dollars to the broker.");
			reimbursement = payprice * bb;
			//System.out.println("then user" + user.getuserId() + " will relinquish " + d + " CPUs and " + e + " GB of RAM and " + f + " GB of storage");
			System.out.println("the broker will reimburse" + reimbursement + " to the customer");
			reimburse.add(reimbursement);
			System.out.println(host.getmtimes().get(userid));
			payment.add(payprice);
			CloudSim.startSimulation();
			
			///////////////////////////////////
			///////////////////////////////////
			//////////////////////////////////
			//////////////////////////////////
			//the third time: 
			/////////////////////////////////////
			////////////////////////////////////
			
			
			/*
			CloudSim.init(num_user, calendar, trace_flag);

			@SuppressWarnings("unused")
			Datacenter datacenter2 = createDatacenter("Datacenter_1");

			DatacenterBroker broker2 = createBroker();
			int broker2Id = broker2.getId();
			
			//update totalProfit;
			totalProfit += payprice;
			
			broker2.submitUserList(userlist);
			broker2.BindUserToHost(user.getuserId(), host.getId());
			
			host.setTotalProfit(totalProfit);
			
			broker2.setHostList(hostlist1);
			
			cloudletid = 2;
			Cloudlet cloudlet2 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
			cloudlet2.setbrokerId(broker2Id);
			//update the price of cloudlet;
			//price = 200;
			//cloudlet2.setprice(price);
			cloudlet2.setuser(user);
			user.setCloudletId(cloudlet2.getCloudletId());
			
			// add the cloudlet to the list
			cloudletList.add(cloudlet2);

			// submit cloudlet list to the broker
			broker2.submitCloudletList(cloudletList);
			
			//adrian
			
			user.submitCloudlet(cloudlet2);
			user.submitRequest(user.getCloudlet(), false, pesNumber, numram, (long)(fileSize/1000000000), numtime);
			
			
			basicprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()*0.001);
			pledge = basicprice * 1.05;
			System.out.println("user" + user.getuserId() + " asks for " + user.getnumCPU() + " CPUs and " + user.getnumRAM() + " GB of RAM and " + user.getnumStorage() + " GB of storage to host" + host.getId());

			
			if(cloudlet2.pledge == true){
				//here I didn't consider giving back the extra money if the user will not relinquish any resource;
				payprice = pledge;
			}
			else{
				//host.minusmtimes(userid);
				//here remember the BROKER is changing to BROKER2!!!!!!!!! and cloudlet is cloudlet2!!!!!!!!!
				if (host.getmtimes().get(userid) == 0){
					host.addmtimes(userid);
					ArrayList<Double> aaa = new ArrayList<Double> ();
					ArrayList<Double> bbb = new ArrayList<Double> ();
					//here I will set some default values for aop and sop and ...;
					if(host.getmaop().get(userid) == null){
					broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3, 0.3);
					//right now we don't consider the pricing model;
					//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
					host.getmaop().put(userid, bbb);
					}
					else {
						broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3);
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
					broker2.CalNumOfRes1(user.getuserId(), host.getId());
					host.addmsop(userid, relinp.get(1));
					host.addmaop(userid, relinp.get(1));
					//payprice = broker2.CalPrice(cloudlet2.getCloudletId(), host.getId());
				}
				else if(host.getmtimes().get(userid) == 2){
					host.addmtimes(userid);
					// will add codes for sop and aop and ...;
					broker2.CalNumOfRes2(user.getuserId(), host.getId());
					host.addmsop(userid, relinp.get(2));
					host.addmaop(userid, relinp.get(2));
					//payprice = broker2.CalPrice(cloudlet2.getCloudletId(), host.getId());
				}
				else if(host.getmtimes().get(userid) > 2){
					broker2.CalNumOfRes(user.getuserId(), host.getId());
					int a = host.getmtimes().get(userid);
					host.addmtimes(userid);
					host.addmsop(userid, relinp.get(a));
					host.addmaop(userid, relinp.get(a));
					
				}
			}

			
			acpu.add(user.getnumCPU());
			aram.add(user.getnumRAM());
			asto.add(user.getnumStorage());
			utimeh = host.getmtimes().get(userid) ;
			System.out.println("this is the " + utimeh + "th time for user" + user.getuserId() + " to use services of host" + host.getId());
			payprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()*0.001);
			vmid = 2;
			
			Vm vm2 = new Vm(vmid, broker2Id, mips, user.getnumCPU(), user.getnumRAM(), bw, user.getnumStorage(), vmm, new CloudletSchedulerTimeShared());
			
			vm2.setHost(host);
			
			//int vmid2 = 1;
			//Vm vm2 = new Vm(vmid2, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
			// add the VM to the vmList
			vmlist.add(vm2);
			//vmlist.add(vm2);

			// submit vm list to the broker
			broker2.submitVmList(vmlist);
			
			broker2.bindCloudletToVm(cloudletid, vmid);
			
			payprice = payprice * user.gettime();
			
			aa = utimeh -1;
			bb = relinp.get(aa);
			System.out.println("the relinquish probability of the user for this time is " + bb);
			//System.out.println(bb);
			d = (int) (user.getnumCPU() * bb);
			e = (int) (user.getnumRAM() * bb);
			f = (long) (user.getnumStorage() * bb);
			System.out.println("host " + host.getId() + " will give " + user.getnumCPU() + " CPUs and " +user.getnumRAM()+ " GB of RAM and " + user.getnumStorage() + " GB of storage to user" + userid + " to finish his cloudlet " + cloudlet1.getCloudletId());
			System.out.println("the user will pay " + payprice + " dollars to the broker.");
			reimbursement = payprice * bb;
			System.out.println("then user" + user.getuserId() + " will relinquish " + d + " CPUs and " + e + " GB of RAM and " + f + " GB of storage");
			System.out.println("the broker will reimburse" + reimbursement + " to the customer");
			reimburse.add(reimbursement);
			System.out.println(host.getmtimes().get(userid));
			payment.add(payprice);
			
			
			
			
			CloudSim.startSimulation();
			
			
/////////////////////////////////////
////////////////////////////////////



CloudSim.init(num_user, calendar, trace_flag);

@SuppressWarnings("unused")
Datacenter datacenter3 = createDatacenter("Datacenter_1");

DatacenterBroker broker3 = createBroker();
int broker3Id = broker3.getId();

//update totalProfit;
totalProfit += payprice;

broker3.submitUserList(userlist);
broker3.BindUserToHost(user.getuserId(), host.getId());

host.setTotalProfit(totalProfit);

broker3.setHostList(hostlist1);

cloudletid = 3;
Cloudlet cloudlet3 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
cloudlet3.setbrokerId(broker3Id);
//update the price of cloudlet;
//price = 200;
//cloudlet3.setprice(price);
cloudlet3.setuser(user);
user.setCloudletId(cloudlet3.getCloudletId());

// add the cloudlet to the list
cloudletList.add(cloudlet3);

// submit cloudlet list to the broker
broker3.submitCloudletList(cloudletList);

//adrian

user.submitCloudlet(cloudlet3);
user.submitRequest(user.getCloudlet(), false, pesNumber, numram, (long)(fileSize/1000000000), numtime);


basicprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()*0.001);
pledge = basicprice * 1.05;
System.out.println("user" + user.getuserId() + " asks for " + user.getnumCPU() + " CPUs and " + user.getnumRAM() + " GB of RAM and " + user.getnumStorage() + " GB of storage to host" + host.getId());


if(cloudlet3.pledge == true){
//here I didn't consider giving back the extra money if the user will not relinquish any resource;
payprice = pledge;
}
else{
//host.minusmtimes(userid);
//here remember the BROKER is changing to BROKER3!!!!!!!!! and cloudlet is cloudlet3!!!!!!!!!
	if (host.getmtimes().get(userid) == 0){
		host.addmtimes(userid);
		ArrayList<Double> aaa = new ArrayList<Double> ();
		ArrayList<Double> bbb = new ArrayList<Double> ();
		//here I will set some default values for aop and sop and ...;
		if(host.getmaop().get(userid) == null){
		broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3, 0.3);
		//right now we don't consider the pricing model;
		//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
		host.getmaop().put(userid, bbb);
		}
		else {
			broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3);
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
broker3.CalNumOfRes1(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(1));
host.addmaop(userid, relinp.get(1));
//payprice = broker3.CalPrice(cloudlet3.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) == 2){
host.addmtimes(userid);
// will add codes for sop and aop and ...;
broker3.CalNumOfRes2(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(2));
host.addmaop(userid, relinp.get(2));
//payprice = broker3.CalPrice(cloudlet3.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) > 2){
broker3.CalNumOfRes(user.getuserId(), host.getId());
int a = host.getmtimes().get(userid);
host.addmtimes(userid);
host.addmsop(userid, relinp.get(a));
host.addmaop(userid, relinp.get(a));

}
}
acpu.add(user.getnumCPU());
aram.add(user.getnumRAM());
asto.add(user.getnumStorage());


utimeh = host.getmtimes().get(userid) ;
System.out.println("this is the " + utimeh + "th time for user" + user.getuserId() + " to use services of host" + host.getId());
payprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()*0.001);
vmid = 3;

Vm vm3 = new Vm(vmid, broker3Id, mips, user.getnumCPU(), user.getnumRAM(), bw, user.getnumStorage(), vmm, new CloudletSchedulerTimeShared());

vm3.setHost(host);

//int vmid3 = 1;
//Vm vm3 = new Vm(vmid3, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
// add the VM to the vmList
vmlist.add(vm3);
//vmlist.add(vm3);

// submit vm list to the broker
broker3.submitVmList(vmlist);

broker3.bindCloudletToVm(cloudletid, vmid);

payprice = payprice * user.gettime();

aa = utimeh -1;
bb = relinp.get(aa);
System.out.println("the relinquish probability of the user for this time is " + bb);
//System.out.println(bb);
d = (int) (user.getnumCPU() * bb);
e = (int) (user.getnumRAM() * bb);
f = (long) (user.getnumStorage() * bb);
System.out.println("host " + host.getId() + " will give " + user.getnumCPU() + " CPUs and " +user.getnumRAM()+ " GB of RAM and " + user.getnumStorage() + " GB of storage to user" + userid + " to finish his cloudlet " + cloudlet1.getCloudletId());
System.out.println("the user will pay " + payprice + " dollars to the broker.");
reimbursement = payprice * bb;
System.out.println("then user" + user.getuserId() + " will relinquish " + d + " CPUs and " + e + " GB of RAM and " + f + " GB of storage");
System.out.println("the broker will reimburse" + reimbursement + " to the customer");
reimburse.add(reimbursement);
System.out.println(host.getmtimes().get(userid));
payment.add(payprice);




CloudSim.startSimulation();
			
			
			
			
			
/////////////////////////////////////
////////////////////////////////////



CloudSim.init(num_user, calendar, trace_flag);

@SuppressWarnings("unused")
Datacenter datacenter4 = createDatacenter("Datacenter_1");

DatacenterBroker broker4 = createBroker();
int broker4Id = broker4.getId();

//update totalProfit;
totalProfit += payprice;

broker4.submitUserList(userlist);
broker4.BindUserToHost(user.getuserId(), host.getId());

host.setTotalProfit(totalProfit);

broker4.setHostList(hostlist1);

cloudletid = 4;
Cloudlet cloudlet4 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
cloudlet4.setbrokerId(broker4Id);
//update the price of cloudlet;
//price = 200;
//cloudlet4.setprice(price);
cloudlet4.setuser(user);
user.setCloudletId(cloudlet4.getCloudletId());

// add the cloudlet to the list
cloudletList.add(cloudlet4);

// submit cloudlet list to the broker
broker4.submitCloudletList(cloudletList);

//adrian

user.submitCloudlet(cloudlet4);
user.submitRequest(user.getCloudlet(), false, pesNumber, numram, (long)(fileSize/1000000000), numtime);


basicprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()/400);
pledge = basicprice * 1.05;
System.out.println("user" + user.getuserId() + " asks for " + user.getnumCPU() + " CPUs and " + user.getnumRAM() + " GB of RAM and " + user.getnumStorage() + " GB of storage to host" + host.getId());


if(cloudlet4.pledge == true){
//here I didn't consider giving back the extra money if the user will not relinquish any resource;
payprice = pledge;
}
else{
//host.minusmtimes(userid);
//here remember the BROKER is changing to BROKER4!!!!!!!!! and cloudlet is cloudlet4!!!!!!!!!
	if (host.getmtimes().get(userid) == 0){
		host.addmtimes(userid);
		ArrayList<Double> aaa = new ArrayList<Double> ();
		ArrayList<Double> bbb = new ArrayList<Double> ();
		//here I will set some default values for aop and sop and ...;
		if(host.getmaop().get(userid) == null){
		broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3, 0.3);
		//right now we don't consider the pricing model;
		//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
		host.getmaop().put(userid, bbb);
		}
		else {
			broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3);
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
broker4.CalNumOfRes1(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(1));
host.addmaop(userid, relinp.get(1));
//payprice = broker4.CalPrice(cloudlet4.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) == 2){
host.addmtimes(userid);
// will add codes for sop and aop and ...;
broker4.CalNumOfRes2(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(2));
host.addmaop(userid, relinp.get(2));
//payprice = broker4.CalPrice(cloudlet4.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) > 2){
broker4.CalNumOfRes(user.getuserId(), host.getId());
int a = host.getmtimes().get(userid);
host.addmtimes(userid);
host.addmsop(userid, relinp.get(a));
host.addmaop(userid, relinp.get(a));

}
}

acpu.add(user.getnumCPU());
aram.add(user.getnumRAM());
asto.add(user.getnumStorage());

utimeh = host.getmtimes().get(userid) ;
System.out.println("this is the " + utimeh + "th time for user" + user.getuserId() + " to use services of host" + host.getId());
payprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()*0.001);
vmid = 4;

Vm vm4 = new Vm(vmid, broker4Id, mips, user.getnumCPU(), user.getnumRAM(), bw, user.getnumStorage(), vmm, new CloudletSchedulerTimeShared());

vm4.setHost(host);

//int vmid4 = 1;
//Vm vm4 = new Vm(vmid4, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
// add the VM to the vmList
vmlist.add(vm4);
//vmlist.add(vm4);

// submit vm list to the broker
broker4.submitVmList(vmlist);

broker4.bindCloudletToVm(cloudletid, vmid);

payprice = payprice * user.gettime();

aa = utimeh -1;
bb = relinp.get(aa);
System.out.println("the relinquish probability of the user for this time is " + bb);
//System.out.println(bb);
d = (int) (user.getnumCPU() * bb);
e = (int) (user.getnumRAM() * bb);
f = (long) (user.getnumStorage() * bb);
System.out.println("host " + host.getId() + " will give " + user.getnumCPU() + " CPUs and " +user.getnumRAM()+ " GB of RAM and " + user.getnumStorage() + " GB of storage to user" + userid + " to finish his cloudlet " + cloudlet1.getCloudletId());
System.out.println("the user will pay " + payprice + " dollars to the broker.");
reimbursement = payprice * bb;
System.out.println("then user" + user.getuserId() + " will relinquish " + d + " CPUs and " + e + " GB of RAM and " + f + " GB of storage");
System.out.println("the broker will reimburse" + reimbursement + " to the customer");
reimburse.add(reimbursement);
System.out.println(host.getmtimes().get(userid));
payment.add(payprice);




CloudSim.startSimulation();
			
			
/////////////////////////////////////
////////////////////////////////////



CloudSim.init(num_user, calendar, trace_flag);

@SuppressWarnings("unused")
Datacenter datacenter5 = createDatacenter("Datacenter_1");

DatacenterBroker broker5 = createBroker();
int broker5Id = broker5.getId();

//update totalProfit;
totalProfit += payprice;

broker5.submitUserList(userlist);
broker5.BindUserToHost(user.getuserId(), host.getId());

host.setTotalProfit(totalProfit);

broker5.setHostList(hostlist1);

cloudletid = 5;
Cloudlet cloudlet5 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
cloudlet5.setbrokerId(broker5Id);
//update the price of cloudlet;
//price = 200;
//cloudlet5.setprice(price);
cloudlet5.setuser(user);
user.setCloudletId(cloudlet5.getCloudletId());

// add the cloudlet to the list
cloudletList.add(cloudlet5);

// submit cloudlet list to the broker
broker5.submitCloudletList(cloudletList);

//adrian

user.submitCloudlet(cloudlet5);
user.submitRequest(user.getCloudlet(), false, pesNumber, numram, (long)(fileSize/1000000000), numtime);


basicprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()/400);
pledge = basicprice * 1.05;
System.out.println("user" + user.getuserId() + " asks for " + user.getnumCPU() + " CPUs and " + user.getnumRAM() + " GB of RAM and " + user.getnumStorage() + " GB of storage to host" + host.getId());


if(cloudlet5.pledge == true){
//here I didn't consider giving back the extra money if the user will not relinquish any resource;
payprice = pledge;
}
else{
//host.minusmtimes(userid);
//here remember the BROKER is changing to BROKER5!!!!!!!!! and cloudlet is cloudlet5!!!!!!!!!
	if (host.getmtimes().get(userid) == 0){
		host.addmtimes(userid);
		ArrayList<Double> aaa = new ArrayList<Double> ();
		ArrayList<Double> bbb = new ArrayList<Double> ();
		//here I will set some default values for aop and sop and ...;
		if(host.getmaop().get(userid) == null){
		broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3, 0.3);
		//right now we don't consider the pricing model;
		//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
		host.getmaop().put(userid, bbb);
		}
		else {
			broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3);
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
broker5.CalNumOfRes1(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(1));
host.addmaop(userid, relinp.get(1));
//payprice = broker5.CalPrice(cloudlet5.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) == 2){
host.addmtimes(userid);
// will add codes for sop and aop and ...;
broker5.CalNumOfRes2(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(2));
host.addmaop(userid, relinp.get(2));
//payprice = broker5.CalPrice(cloudlet5.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) > 2){
broker5.CalNumOfRes(user.getuserId(), host.getId());	
int a = host.getmtimes().get(userid);
host.addmtimes(userid);
host.addmsop(userid, relinp.get(a));
host.addmaop(userid, relinp.get(a));

}
}
acpu.add(user.getnumCPU());
aram.add(user.getnumRAM());
asto.add(user.getnumStorage());


utimeh = host.getmtimes().get(userid) ;
System.out.println("this is the " + utimeh + "th time for user" + user.getuserId() + " to use services of host" + host.getId());
payprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()*0.001);
vmid = 5;

Vm vm5 = new Vm(vmid, broker5Id, mips, user.getnumCPU(), user.getnumRAM(), bw, user.getnumStorage(), vmm, new CloudletSchedulerTimeShared());

vm5.setHost(host);

//int vmid5 = 1;
//Vm vm5 = new Vm(vmid5, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
// add the VM to the vmList
vmlist.add(vm5);
//vmlist.add(vm5);

// submit vm list to the broker
broker5.submitVmList(vmlist);

broker5.bindCloudletToVm(cloudletid, vmid);

payprice = payprice * user.gettime();

aa = utimeh -1;
bb = relinp.get(aa);
System.out.println("the relinquish probability of the user for this time is " + bb);
//System.out.println(bb);
d = (int) (user.getnumCPU() * bb);
e = (int) (user.getnumRAM() * bb);
f = (long) (user.getnumStorage() * bb);

System.out.println("host " + host.getId() + " will give " + user.getnumCPU() + " CPUs and " +user.getnumRAM()+ " GB of RAM and " + user.getnumStorage() + " GB of storage to user" + userid + " to finish his cloudlet " + cloudlet1.getCloudletId());
System.out.println("the user will pay " + payprice + " dollars to the broker.");
reimbursement = payprice * bb;
System.out.println("then user" + user.getuserId() + " will relinquish " + d + " CPUs and " + e + " GB of RAM and " + f + " GB of storage");
System.out.println("the broker will reimburse" + reimbursement + " to the customer");
reimburse.add(reimbursement);
System.out.println(host.getmtimes().get(userid));
payment.add(payprice);




CloudSim.startSimulation();
			
/////////////////////////////////////
////////////////////////////////////



CloudSim.init(num_user, calendar, trace_flag);

@SuppressWarnings("unused")
Datacenter datacenter6 = createDatacenter("Datacenter_1");

DatacenterBroker broker6 = createBroker();
int broker6Id = broker6.getId();

//update totalProfit;
totalProfit += payprice;

broker6.submitUserList(userlist);
broker6.BindUserToHost(user.getuserId(), host.getId());

host.setTotalProfit(totalProfit);

broker6.setHostList(hostlist1);

cloudletid = 6;
Cloudlet cloudlet6 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
cloudlet6.setbrokerId(broker6Id);
//update the price of cloudlet;
//price = 200;
//cloudlet6.setprice(price);
cloudlet6.setuser(user);
user.setCloudletId(cloudlet6.getCloudletId());

// add the cloudlet to the list
cloudletList.add(cloudlet6);

// submit cloudlet list to the broker
broker6.submitCloudletList(cloudletList);

//adrian

user.submitCloudlet(cloudlet6);
user.submitRequest(user.getCloudlet(), false, pesNumber, numram, (long)(fileSize/1000000000), numtime);


basicprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()/400);
pledge = basicprice * 1.05;
System.out.println("user" + user.getuserId() + " asks for " + user.getnumCPU() + " CPUs and " + user.getnumRAM() + " GB of RAM and " + user.getnumStorage() + " GB of storage to host" + host.getId());


if(cloudlet6.pledge == true){
//here I didn't consider giving back the extra money if the user will not relinquish any resource;
payprice = pledge;
}
else{
//host.minusmtimes(userid);
//here remember the BROKER is changing to BROKER6!!!!!!!!! and cloudlet is cloudlet6!!!!!!!!!
	if (host.getmtimes().get(userid) == 0){
		host.addmtimes(userid);
		ArrayList<Double> aaa = new ArrayList<Double> ();
		ArrayList<Double> bbb = new ArrayList<Double> ();
		//here I will set some default values for aop and sop and ...;
		if(host.getmaop().get(userid) == null){
		broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3, 0.3);
		//right now we don't consider the pricing model;
		//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
		host.getmaop().put(userid, bbb);
		}
		else {
			broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3);
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
broker6.CalNumOfRes1(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(1));
host.addmaop(userid, relinp.get(1));
//payprice = broker6.CalPrice(cloudlet6.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) == 2){
host.addmtimes(userid);
// will add codes for sop and aop and ...;
broker6.CalNumOfRes2(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(2));
host.addmaop(userid, relinp.get(2));
//payprice = broker6.CalPrice(cloudlet6.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) > 2){
broker6.CalNumOfRes(user.getuserId(), host.getId());	
int a = host.getmtimes().get(userid);
host.addmtimes(userid);
host.addmsop(userid, relinp.get(a));
host.addmaop(userid, relinp.get(a));

}
}
acpu.add(user.getnumCPU());
aram.add(user.getnumRAM());
asto.add(user.getnumStorage());


utimeh = host.getmtimes().get(userid) ;
System.out.println("this is the " + utimeh + "th time for user" + user.getuserId() + " to use services of host" + host.getId());
payprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()*0.001);
vmid = 6;

Vm vm6 = new Vm(vmid, broker6Id, mips, user.getnumCPU(), user.getnumRAM(), bw, user.getnumStorage(), vmm, new CloudletSchedulerTimeShared());

vm6.setHost(host);

//int vmid6 = 1;
//Vm vm6 = new Vm(vmid6, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
// add the VM to the vmList
vmlist.add(vm6);
//vmlist.add(vm6);

// submit vm list to the broker
broker6.submitVmList(vmlist);

broker6.bindCloudletToVm(cloudletid, vmid);

payprice = payprice * user.gettime();

aa = utimeh -1;
bb = relinp.get(aa);
System.out.println("the relinquish probability of the user for this time is " + bb);
//System.out.println(bb);
d = (int) (user.getnumCPU() * bb);
e = (int) (user.getnumRAM() * bb);
f = (long) (user.getnumStorage() * bb);
System.out.println("host " + host.getId() + " will give " + user.getnumCPU() + " CPUs and " +user.getnumRAM()+ " GB of RAM and " + user.getnumStorage() + " GB of storage to user" + userid + " to finish his cloudlet " + cloudlet1.getCloudletId());
System.out.println("the user will pay " + payprice + " dollars to the broker.");
reimbursement = payprice * bb;
System.out.println("then user" + user.getuserId() + " will relinquish " + d + " CPUs and " + e + " GB of RAM and " + f + " GB of storage");
System.out.println("the broker will reimburse" + reimbursement + " to the customer");
reimburse.add(reimbursement);
System.out.println(host.getmtimes().get(userid));
payment.add(payprice);




CloudSim.startSimulation();
/////////////////////////////////////
////////////////////////////////////



CloudSim.init(num_user, calendar, trace_flag);

@SuppressWarnings("unused")
Datacenter datacenter7 = createDatacenter("Datacenter_1");

DatacenterBroker broker7 = createBroker();
int broker7Id = broker7.getId();

//update totalProfit;
totalProfit += payprice;

broker7.submitUserList(userlist);
broker7.BindUserToHost(user.getuserId(), host.getId());

host.setTotalProfit(totalProfit);

broker7.setHostList(hostlist1);

cloudletid = 7;
Cloudlet cloudlet7 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
cloudlet7.setbrokerId(broker7Id);
//update the price of cloudlet;
//price = 200;
//cloudlet7.setprice(price);
cloudlet7.setuser(user);
user.setCloudletId(cloudlet7.getCloudletId());

// add the cloudlet to the list
cloudletList.add(cloudlet7);

// submit cloudlet list to the broker
broker7.submitCloudletList(cloudletList);

//adrian

user.submitCloudlet(cloudlet7);
user.submitRequest(user.getCloudlet(), false, pesNumber, numram, (long)(fileSize/1000000000), numtime);


basicprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()/400);
pledge = basicprice * 1.05;
System.out.println("user" + user.getuserId() + " asks for " + user.getnumCPU() + " CPUs and " + user.getnumRAM() + " GB of RAM and " + user.getnumStorage() + " GB of storage to host" + host.getId());


if(cloudlet7.pledge == true){
//here I didn't consider giving back the extra money if the user will not relinquish any resource;
payprice = pledge;
}
else{
//host.minusmtimes(userid);
//here remember the BROKER is changing to BROKER7!!!!!!!!! and cloudlet is cloudlet7!!!!!!!!!
	if (host.getmtimes().get(userid) == 0){
		host.addmtimes(userid);
		ArrayList<Double> aaa = new ArrayList<Double> ();
		ArrayList<Double> bbb = new ArrayList<Double> ();
		//here I will set some default values for aop and sop and ...;
		if(host.getmaop().get(userid) == null){
		broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3, 0.3);
		//right now we don't consider the pricing model;
		//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
		host.getmaop().put(userid, bbb);
		}
		else {
			broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3);
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
broker7.CalNumOfRes1(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(1));
host.addmaop(userid, relinp.get(1));
//payprice = broker7.CalPrice(cloudlet7.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) == 2){
host.addmtimes(userid);
// will add codes for sop and aop and ...;
broker7.CalNumOfRes2(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(2));
host.addmaop(userid, relinp.get(2));
//payprice = broker7.CalPrice(cloudlet7.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) > 2){
broker7.CalNumOfRes(user.getuserId(), host.getId());	
int a = host.getmtimes().get(userid);
host.addmtimes(userid);
host.addmsop(userid, relinp.get(a));
host.addmaop(userid, relinp.get(a));

}
}


acpu.add(user.getnumCPU());
aram.add(user.getnumRAM());
asto.add(user.getnumStorage());
utimeh = host.getmtimes().get(userid) ;
System.out.println("this is the " + utimeh + "th time for user" + user.getuserId() + " to use services of host" + host.getId());
payprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()*0.001);
vmid = 7;

Vm vm7 = new Vm(vmid, broker7Id, mips, user.getnumCPU(), user.getnumRAM(), bw, user.getnumStorage(), vmm, new CloudletSchedulerTimeShared());

vm7.setHost(host);

//int vmid7 = 1;
//Vm vm7 = new Vm(vmid7, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
// add the VM to the vmList
vmlist.add(vm7);
//vmlist.add(vm7);

// submit vm list to the broker
broker7.submitVmList(vmlist);

broker7.bindCloudletToVm(cloudletid, vmid);

payprice = payprice * user.gettime();

aa = utimeh -1;
bb = relinp.get(aa);
System.out.println("the relinquish probability of the user for this time is " + bb);
//System.out.println(bb);
d = (int) (user.getnumCPU() * bb);
e = (int) (user.getnumRAM() * bb);
f = (long) (user.getnumStorage() * bb);
System.out.println("host " + host.getId() + " will give " + user.getnumCPU() + " CPUs and " +user.getnumRAM()+ " GB of RAM and " + user.getnumStorage() + " GB of storage to user" + userid + " to finish his cloudlet " + cloudlet1.getCloudletId());
System.out.println("the user will pay " + payprice + " dollars to the broker.");
reimbursement = payprice * bb;
System.out.println("then user" + user.getuserId() + " will relinquish " + d + " CPUs and " + e + " GB of RAM and " + f + " GB of storage");
System.out.println("the broker will reimburse" + reimbursement + " to the customer");
reimburse.add(reimbursement);
System.out.println(host.getmtimes().get(userid));
payment.add(payprice);




CloudSim.startSimulation();
/////////////////////////////////////
////////////////////////////////////



CloudSim.init(num_user, calendar, trace_flag);

@SuppressWarnings("unused")
Datacenter datacenter8 = createDatacenter("Datacenter_1");

DatacenterBroker broker8 = createBroker();
int broker8Id = broker8.getId();

//update totalProfit;
totalProfit += payprice;

broker8.submitUserList(userlist);
broker8.BindUserToHost(user.getuserId(), host.getId());

host.setTotalProfit(totalProfit);

broker8.setHostList(hostlist1);

cloudletid = 8;
Cloudlet cloudlet8 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
cloudlet8.setbrokerId(broker8Id);
//update the price of cloudlet;
//price = 200;
//cloudlet8.setprice(price);
cloudlet8.setuser(user);
user.setCloudletId(cloudlet8.getCloudletId());

// add the cloudlet to the list
cloudletList.add(cloudlet8);

// submit cloudlet list to the broker
broker8.submitCloudletList(cloudletList);

//adrian

user.submitCloudlet(cloudlet8);
user.submitRequest(user.getCloudlet(), false, pesNumber, numram, (long)(fileSize/1000000000), numtime);


basicprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()/400);
pledge = basicprice * 1.05;
System.out.println("user" + user.getuserId() + " asks for " + user.getnumCPU() + " CPUs and " + user.getnumRAM() + " GB of RAM and " + user.getnumStorage() + " GB of storage to host" + host.getId());


if(cloudlet8.pledge == true){
//here I didn't consider giving back the extra money if the user will not relinquish any resource;
payprice = pledge;
}
else{
//host.minusmtimes(userid);
//here remember the BROKER is changing to BROKER8!!!!!!!!! and cloudlet is cloudlet8!!!!!!!!!
	if (host.getmtimes().get(userid) == 0){
		host.addmtimes(userid);
		ArrayList<Double> aaa = new ArrayList<Double> ();
		ArrayList<Double> bbb = new ArrayList<Double> ();
		//here I will set some default values for aop and sop and ...;
		if(host.getmaop().get(userid) == null){
		broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3, 0.3);
		//right now we don't consider the pricing model;
		//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
		host.getmaop().put(userid, bbb);
		}
		else {
			broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3);
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
broker8.CalNumOfRes1(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(1));
host.addmaop(userid, relinp.get(1));
//payprice = broker8.CalPrice(cloudlet8.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) == 2){
host.addmtimes(userid);
// will add codes for sop and aop and ...;
broker8.CalNumOfRes2(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(2));
host.addmaop(userid, relinp.get(2));
//payprice = broker8.CalPrice(cloudlet8.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) > 2){
broker8.CalNumOfRes(user.getuserId(), host.getId());	
int a = host.getmtimes().get(userid);
host.addmtimes(userid);
host.addmsop(userid, relinp.get(a));
host.addmaop(userid, relinp.get(a));

}
}

acpu.add(user.getnumCPU());
aram.add(user.getnumRAM());
asto.add(user.getnumStorage());

utimeh = host.getmtimes().get(userid) ;
System.out.println("this is the " + utimeh + "th time for user" + user.getuserId() + " to use services of host" + host.getId());
payprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()*0.001);
vmid = 8;

Vm vm8 = new Vm(vmid, broker8Id, mips, user.getnumCPU(), user.getnumRAM(), bw, user.getnumStorage(), vmm, new CloudletSchedulerTimeShared());

vm8.setHost(host);

//int vmid8 = 1;
//Vm vm8 = new Vm(vmid8, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
// add the VM to the vmList
vmlist.add(vm8);
//vmlist.add(vm8);

// submit vm list to the broker
broker8.submitVmList(vmlist);

broker8.bindCloudletToVm(cloudletid, vmid);

payprice = payprice * user.gettime();

aa = utimeh -1;
bb = relinp.get(aa);
System.out.println("the relinquish probability of the user for this time is " + bb);
//System.out.println(bb);
d = (int) (user.getnumCPU() * bb);
e = (int) (user.getnumRAM() * bb);
f = (long) (user.getnumStorage() * bb);
System.out.println("host " + host.getId() + " will give " + user.getnumCPU() + " CPUs and " +user.getnumRAM()+ " GB of RAM and " + user.getnumStorage() + " GB of storage to user" + userid + " to finish his cloudlet " + cloudlet1.getCloudletId());
System.out.println("the user will pay " + payprice + " dollars to the broker.");
reimbursement = payprice * bb;
System.out.println("then user" + user.getuserId() + " will relinquish " + d + " CPUs and " + e + " GB of RAM and " + f + " GB of storage");
System.out.println("the broker will reimburse" + reimbursement + " to the customer");
reimburse.add(reimbursement);
System.out.println(host.getmtimes().get(userid));
payment.add(payprice);




CloudSim.startSimulation();
/////////////////////////////////////
////////////////////////////////////



CloudSim.init(num_user, calendar, trace_flag);

@SuppressWarnings("unused")
Datacenter datacenter9 = createDatacenter("Datacenter_1");

DatacenterBroker broker9 = createBroker();
int broker9Id = broker9.getId();

//update totalProfit;
totalProfit += payprice;

broker9.submitUserList(userlist);
broker9.BindUserToHost(user.getuserId(), host.getId());

host.setTotalProfit(totalProfit);

broker9.setHostList(hostlist1);

cloudletid = 9;
Cloudlet cloudlet9 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
cloudlet9.setbrokerId(broker9Id);
//update the price of cloudlet;
//price = 200;
//cloudlet9.setprice(price);
cloudlet9.setuser(user);
user.setCloudletId(cloudlet9.getCloudletId());

// add the cloudlet to the list
cloudletList.add(cloudlet9);

// submit cloudlet list to the broker
broker9.submitCloudletList(cloudletList);

//adrian

user.submitCloudlet(cloudlet9);
user.submitRequest(user.getCloudlet(), false, pesNumber, numram, (long)(fileSize/1000000000), numtime);


basicprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()/400);
pledge = basicprice * 1.05;
System.out.println("user" + user.getuserId() + " asks for " + user.getnumCPU() + " CPUs and " + user.getnumRAM() + " GB of RAM and " + user.getnumStorage() + " GB of storage to host" + host.getId());


if(cloudlet9.pledge == true){
//here I didn't consider giving back the extra money if the user will not relinquish any resource;
payprice = pledge;
}
else{
//host.minusmtimes(userid);
//here remember the BROKER is changing to BROKER9!!!!!!!!! and cloudlet is cloudlet9!!!!!!!!!
	if (host.getmtimes().get(userid) == 0){
		host.addmtimes(userid);
		ArrayList<Double> aaa = new ArrayList<Double> ();
		ArrayList<Double> bbb = new ArrayList<Double> ();
		//here I will set some default values for aop and sop and ...;
		if(host.getmaop().get(userid) == null){
		broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3, 0.3);
		//right now we don't consider the pricing model;
		//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
		host.getmaop().put(userid, bbb);
		}
		else {
			broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3);
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
broker9.CalNumOfRes1(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(1));
host.addmaop(userid, relinp.get(1));
//payprice = broker9.CalPrice(cloudlet9.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) == 2){
host.addmtimes(userid);
// will add codes for sop and aop and ...;
broker9.CalNumOfRes2(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(2));
host.addmaop(userid, relinp.get(2));
//payprice = broker9.CalPrice(cloudlet9.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) > 2){
broker9.CalNumOfRes(user.getuserId(), host.getId());
int a = host.getmtimes().get(userid);
host.addmtimes(userid);
host.addmsop(userid, relinp.get(a));
host.addmaop(userid, relinp.get(a));

}
}

acpu.add(user.getnumCPU());
aram.add(user.getnumRAM());
asto.add(user.getnumStorage());

utimeh = host.getmtimes().get(userid) ;
System.out.println("this is the " + utimeh + "th time for user" + user.getuserId() + " to use services of host" + host.getId());
payprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()*0.001);
vmid = 9;

Vm vm9 = new Vm(vmid, broker9Id, mips, user.getnumCPU(), user.getnumRAM(), bw, user.getnumStorage(), vmm, new CloudletSchedulerTimeShared());

vm9.setHost(host);

//int vmid9 = 1;
//Vm vm9 = new Vm(vmid9, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
// add the VM to the vmList
vmlist.add(vm9);
//vmlist.add(vm9);

// submit vm list to the broker
broker9.submitVmList(vmlist);

broker9.bindCloudletToVm(cloudletid, vmid);

payprice = payprice * user.gettime();

aa = utimeh -1;
bb = relinp.get(aa);
System.out.println("the relinquish probability of the user for this time is " + bb);
//System.out.println(bb);
d = (int) (user.getnumCPU() * bb);
e = (int) (user.getnumRAM() * bb);
f = (long) (user.getnumStorage() * bb);
System.out.println("host " + host.getId() + " will give " + user.getnumCPU() + " CPUs and " +user.getnumRAM()+ " GB of RAM and " + user.getnumStorage() + " GB of storage to user" + userid + " to finish his cloudlet " + cloudlet1.getCloudletId());
System.out.println("the user will pay " + payprice + " dollars to the broker.");
reimbursement = payprice * bb;
System.out.println("then user" + user.getuserId() + " will relinquish " + d + " CPUs and " + e + " GB of RAM and " + f + " GB of storage");
System.out.println("the broker will reimburse" + reimbursement + " to the customer");
reimburse.add(reimbursement);
System.out.println(host.getmtimes().get(userid));
payment.add(payprice);




CloudSim.startSimulation();
/////////////////////////////////////
////////////////////////////////////



CloudSim.init(num_user, calendar, trace_flag);

@SuppressWarnings("unused")
Datacenter datacenter10 = createDatacenter("Datacenter_1");

DatacenterBroker broker10 = createBroker();
int broker10Id = broker10.getId();

//update totalProfit;
totalProfit += payprice;

broker10.submitUserList(userlist);
broker10.BindUserToHost(user.getuserId(), host.getId());

host.setTotalProfit(totalProfit);

broker10.setHostList(hostlist1);

cloudletid = 10;
Cloudlet cloudlet10 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
cloudlet10.setbrokerId(broker10Id);
//update the price of cloudlet;
//price = 200;
//cloudlet10.setprice(price);
cloudlet10.setuser(user);
user.setCloudletId(cloudlet10.getCloudletId());

// add the cloudlet to the list
cloudletList.add(cloudlet10);

// submit cloudlet list to the broker
broker10.submitCloudletList(cloudletList);

//adrian

user.submitCloudlet(cloudlet10);
user.submitRequest(user.getCloudlet(), false, pesNumber, numram, (long)(fileSize/1000000000), numtime);


basicprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()/400);
pledge = basicprice * 1.05;
System.out.println("user" + user.getuserId() + " asks for " + user.getnumCPU() + " CPUs and " + user.getnumRAM() + " GB of RAM and " + user.getnumStorage() + " GB of storage to host" + host.getId());


if(cloudlet10.pledge == true){
//here I didn't consider giving back the extra money if the user will not relinquish any resource;
payprice = pledge;
}
else{
//host.minusmtimes(userid);
//here remember the BROKER is changing to BROKER10!!!!!!!!! and cloudlet is cloudlet10!!!!!!!!!
if (host.getmtimes().get(userid) == 0){
					host.addmtimes(userid);
					ArrayList<Double> aaa = new ArrayList<Double> ();
					ArrayList<Double> bbb = new ArrayList<Double> ();
					//here I will set some default values for aop and sop and ...;
					if(host.getmaop().get(userid) == null){
					broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3, 0.3);
					//right now we don't consider the pricing model;
					//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
					host.getmaop().put(userid, bbb);
					}
					else {
						broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3);
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
broker10.CalNumOfRes1(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(1));
host.addmaop(userid, relinp.get(1));
//payprice = broker10.CalPrice(cloudlet10.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) == 2){
host.addmtimes(userid);
// will add codes for sop and aop and ...;
broker10.CalNumOfRes2(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(2));
host.addmaop(userid, relinp.get(2));
//payprice = broker10.CalPrice(cloudlet10.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) > 2){
broker10.CalNumOfRes(user.getuserId(), host.getId());	
int a = host.getmtimes().get(userid);
host.addmtimes(userid);
host.addmsop(userid, relinp.get(a));
host.addmaop(userid, relinp.get(a));

}
}


acpu.add(user.getnumCPU());
aram.add(user.getnumRAM());
asto.add(user.getnumStorage());
utimeh = host.getmtimes().get(userid) ;
System.out.println("this is the " + utimeh + "th time for user" + user.getuserId() + " to use services of host" + host.getId());
payprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()*0.001);
vmid = 10;

Vm vm10 = new Vm(vmid, broker10Id, mips, user.getnumCPU(), user.getnumRAM(), bw, user.getnumStorage(), vmm, new CloudletSchedulerTimeShared());

vm10.setHost(host);

//int vmid10 = 1;
//Vm vm10 = new Vm(vmid10, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
// add the VM to the vmList
vmlist.add(vm10);
//vmlist.add(vm10);

// submit vm list to the broker
broker10.submitVmList(vmlist);

broker10.bindCloudletToVm(cloudletid, vmid);

payprice = payprice * user.gettime();

aa = utimeh -1;
bb = relinp.get(aa);
System.out.println("the relinquish probability of the user for this time is " + bb);
//System.out.println(bb);
d = (int) (user.getnumCPU() * bb);
e = (int) (user.getnumRAM() * bb);
f = (long) (user.getnumStorage() * bb);
System.out.println("host " + host.getId() + " will give " + user.getnumCPU() + " CPUs and " +user.getnumRAM()+ " GB of RAM and " + user.getnumStorage() + " GB of storage to user" + userid + " to finish his cloudlet " + cloudlet1.getCloudletId());
System.out.println("the user will pay " + payprice + " dollars to the broker.");
reimbursement = payprice * bb;
System.out.println("then user" + user.getuserId() + " will relinquish " + d + " CPUs and " + e + " GB of RAM and " + f + " GB of storage");
System.out.println("the broker will reimburse" + reimbursement + " to the customer");
reimburse.add(reimbursement);
System.out.println(host.getmtimes().get(userid));
payment.add(payprice);




CloudSim.startSimulation();


///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////

CloudSim.init(num_user, calendar, trace_flag);

@SuppressWarnings("unused")
Datacenter datacenter11 = createDatacenter("Datacenter_1");

DatacenterBroker broker11 = createBroker();
int broker11Id = broker11.getId();

//update totalProfit;
totalProfit += payprice;

broker11.submitUserList(userlist);
broker11.BindUserToHost(user.getuserId(), host.getId());

host.setTotalProfit(totalProfit);

broker11.setHostList(hostlist1);

cloudletid = 11;
Cloudlet cloudlet11 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
cloudlet11.setbrokerId(broker11Id);
//update the price of cloudlet;
//price = 200;
//cloudlet11.setprice(price);
cloudlet11.setuser(user);
user.setCloudletId(cloudlet11.getCloudletId());

// add the cloudlet to the list
cloudletList.add(cloudlet11);

// submit cloudlet list to the broker
broker11.submitCloudletList(cloudletList);

//adrian

user.submitCloudlet(cloudlet11);
user.submitRequest(user.getCloudlet(), false, pesNumber, numram, (long)(fileSize/1000000000), numtime);


basicprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()/400);
pledge = basicprice * 1.05;
System.out.println("user" + user.getuserId() + " asks for " + user.getnumCPU() + " CPUs and " + user.getnumRAM() + " GB of RAM and " + user.getnumStorage() + " GB of storage to host" + host.getId());


if(cloudlet11.pledge == true){
//here I didn't consider giving back the extra money if the user will not relinquish any resource;
payprice = pledge;
}
else{
//host.minusmtimes(userid);
//here remember the BROKER is changing to BROKER11!!!!!!!!! and cloudlet is cloudlet11!!!!!!!!!
if (host.getmtimes().get(userid) == 0){
					host.addmtimes(userid);
					ArrayList<Double> aaa = new ArrayList<Double> ();
					ArrayList<Double> bbb = new ArrayList<Double> ();
					//here I will set some default values for aop and sop and ...;
					if(host.getmaop().get(userid) == null){
					broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3, 0.3);
					//right now we don't consider the pricing model;
					//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
					host.getmaop().put(userid, bbb);
					}
					else {
						broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3);
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
broker11.CalNumOfRes1(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(1));
host.addmaop(userid, relinp.get(1));
//payprice = broker11.CalPrice(cloudlet11.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) == 2){
host.addmtimes(userid);
// will add codes for sop and aop and ...;
broker11.CalNumOfRes2(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(2));
host.addmaop(userid, relinp.get(2));
//payprice = broker11.CalPrice(cloudlet11.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) > 2){
broker11.CalNumOfRes(user.getuserId(), host.getId());	
int a = host.getmtimes().get(userid);
host.addmtimes(userid);
host.addmsop(userid, relinp.get(a));
host.addmaop(userid, relinp.get(a));

}
}


acpu.add(user.getnumCPU());
aram.add(user.getnumRAM());
asto.add(user.getnumStorage());
utimeh = host.getmtimes().get(userid) ;
System.out.println("this is the " + utimeh + "th time for user" + user.getuserId() + " to use services of host" + host.getId());
payprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()*0.001);
vmid = 11;

Vm vm11 = new Vm(vmid, broker11Id, mips, user.getnumCPU(), user.getnumRAM(), bw, user.getnumStorage(), vmm, new CloudletSchedulerTimeShared());

vm11.setHost(host);

//int vmid11 = 1;
//Vm vm11 = new Vm(vmid11, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
// add the VM to the vmList
vmlist.add(vm11);
//vmlist.add(vm11);

// submit vm list to the broker
broker11.submitVmList(vmlist);

broker11.bindCloudletToVm(cloudletid, vmid);

payprice = payprice * user.gettime();

aa = utimeh -1;
bb = relinp.get(aa);
System.out.println("the relinquish probability of the user for this time is " + bb);
//System.out.println(bb);
d = (int) (user.getnumCPU() * bb);
e = (int) (user.getnumRAM() * bb);
f = (long) (user.getnumStorage() * bb);
System.out.println("host " + host.getId() + " will give " + user.getnumCPU() + " CPUs and " +user.getnumRAM()+ " GB of RAM and " + user.getnumStorage() + " GB of storage to user" + userid + " to finish his cloudlet " + cloudlet1.getCloudletId());
System.out.println("the user will pay " + payprice + " dollars to the broker.");
reimbursement = payprice * bb;
System.out.println("then user" + user.getuserId() + " will relinquish " + d + " CPUs and " + e + " GB of RAM and " + f + " GB of storage");
System.out.println("the broker will reimburse" + reimbursement + " to the customer");
reimburse.add(reimbursement);
System.out.println(host.getmtimes().get(userid));
payment.add(payprice);





CloudSim.startSimulation();
///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////
CloudSim.init(num_user, calendar, trace_flag);

@SuppressWarnings("unused")
Datacenter datacenter12 = createDatacenter("Datacenter_1");

DatacenterBroker broker12 = createBroker();
int broker12Id = broker12.getId();

//update totalProfit;
totalProfit += payprice;

broker12.submitUserList(userlist);
broker12.BindUserToHost(user.getuserId(), host.getId());

host.setTotalProfit(totalProfit);

broker12.setHostList(hostlist1);

cloudletid = 12;
Cloudlet cloudlet12 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
cloudlet12.setbrokerId(broker12Id);
//update the price of cloudlet;
//price = 200;
//cloudlet12.setprice(price);
cloudlet12.setuser(user);
user.setCloudletId(cloudlet12.getCloudletId());

//add the cloudlet to the list
cloudletList.add(cloudlet12);

//submit cloudlet list to the broker
broker12.submitCloudletList(cloudletList);

//adrian

user.submitCloudlet(cloudlet12);
user.submitRequest(user.getCloudlet(), false, pesNumber, numram, (long)(fileSize/1000000000), numtime);


basicprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()/400);
pledge = basicprice * 1.05;
System.out.println("user" + user.getuserId() + " asks for " + user.getnumCPU() + " CPUs and " + user.getnumRAM() + " GB of RAM and " + user.getnumStorage() + " GB of storage to host" + host.getId());


if(cloudlet12.pledge == true){
//here I didn't consider giving back the extra money if the user will not relinquish any resource;
payprice = pledge;
}
else{
//host.minusmtimes(userid);
//here remember the BROKER is changing to BROKER12!!!!!!!!! and cloudlet is cloudlet12!!!!!!!!!
if (host.getmtimes().get(userid) == 0){
					host.addmtimes(userid);
					ArrayList<Double> aaa = new ArrayList<Double> ();
					ArrayList<Double> bbb = new ArrayList<Double> ();
					//here I will set some default values for aop and sop and ...;
					if(host.getmaop().get(userid) == null){
					broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3, 0.3);
					//right now we don't consider the pricing model;
					//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
					host.getmaop().put(userid, bbb);
					}
					else {
						broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3);
						//right now we don't consider the pricing model;
						//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
					}
					host.getmsop().put(userid, aaa);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
				}
else if(host.getmtimes().get(userid) == 1){
host.addmtimes(userid);
//will add codes for sop and aop and ...;
broker12.CalNumOfRes1(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(1));
host.addmaop(userid, relinp.get(1));
//payprice = broker12.CalPrice(cloudlet12.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) == 2){
host.addmtimes(userid);
//will add codes for sop and aop and ...;
broker12.CalNumOfRes2(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(2));
host.addmaop(userid, relinp.get(2));
//payprice = broker12.CalPrice(cloudlet12.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) > 2){
broker12.CalNumOfRes(user.getuserId(), host.getId());	
int a = host.getmtimes().get(userid);
host.addmtimes(userid);
host.addmsop(userid, relinp.get(a));
host.addmaop(userid, relinp.get(a));

}
}


acpu.add(user.getnumCPU());
aram.add(user.getnumRAM());
asto.add(user.getnumStorage());
utimeh = host.getmtimes().get(userid) ;
System.out.println("this is the " + utimeh + "th time for user" + user.getuserId() + " to use services of host" + host.getId());
payprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()*0.001);
vmid = 12;

Vm vm12 = new Vm(vmid, broker12Id, mips, user.getnumCPU(), user.getnumRAM(), bw, user.getnumStorage(), vmm, new CloudletSchedulerTimeShared());

vm12.setHost(host);

//int vmid12 = 1;
//Vm vm12 = new Vm(vmid12, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
//add the VM to the vmList
vmlist.add(vm12);
//vmlist.add(vm12);

//submit vm list to the broker
broker12.submitVmList(vmlist);

broker12.bindCloudletToVm(cloudletid, vmid);

payprice = payprice * user.gettime();

aa = utimeh -1;
bb = relinp.get(aa);
System.out.println("the relinquish probability of the user for this time is " + bb);
//System.out.println(bb);
d = (int) (user.getnumCPU() * bb);
e = (int) (user.getnumRAM() * bb);
f = (long) (user.getnumStorage() * bb);
System.out.println("host " + host.getId() + " will give " + user.getnumCPU() + " CPUs and " +user.getnumRAM()+ " GB of RAM and " + user.getnumStorage() + " GB of storage to user" + userid + " to finish his cloudlet " + cloudlet1.getCloudletId());
System.out.println("the user will pay " + payprice + " dollars to the broker.");
reimbursement = payprice * bb;
System.out.println("then user" + user.getuserId() + " will relinquish " + d + " CPUs and " + e + " GB of RAM and " + f + " GB of storage");
System.out.println("the broker will reimburse" + reimbursement + " to the customer");
reimburse.add(reimbursement);
System.out.println(host.getmtimes().get(userid));
payment.add(payprice);



CloudSim.startSimulation();
///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////
CloudSim.init(num_user, calendar, trace_flag);

@SuppressWarnings("unused")
Datacenter datacenter13 = createDatacenter("Datacenter_1");

DatacenterBroker broker13 = createBroker();
int broker13Id = broker13.getId();

//update totalProfit;
totalProfit += payprice;

broker13.submitUserList(userlist);
broker13.BindUserToHost(user.getuserId(), host.getId());

host.setTotalProfit(totalProfit);

broker13.setHostList(hostlist1);

cloudletid = 13;
Cloudlet cloudlet13 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
cloudlet13.setbrokerId(broker13Id);
//update the price of cloudlet;
//price = 200;
//cloudlet13.setprice(price);
cloudlet13.setuser(user);
user.setCloudletId(cloudlet13.getCloudletId());

//add the cloudlet to the list
cloudletList.add(cloudlet13);

//submit cloudlet list to the broker
broker13.submitCloudletList(cloudletList);

//adrian

user.submitCloudlet(cloudlet13);
user.submitRequest(user.getCloudlet(), false, pesNumber, numram, (long)(fileSize/1000000000), numtime);


basicprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()/400);
pledge = basicprice * 1.05;
System.out.println("user" + user.getuserId() + " asks for " + user.getnumCPU() + " CPUs and " + user.getnumRAM() + " GB of RAM and " + user.getnumStorage() + " GB of storage to host" + host.getId());


if(cloudlet13.pledge == true){
//here I didn't consider giving back the extra money if the user will not relinquish any resource;
payprice = pledge;
}
else{
//host.minusmtimes(userid);
//here remember the BROKER is changing to BROKER13!!!!!!!!! and cloudlet is cloudlet13!!!!!!!!!
if (host.getmtimes().get(userid) == 0){
					host.addmtimes(userid);
					ArrayList<Double> aaa = new ArrayList<Double> ();
					ArrayList<Double> bbb = new ArrayList<Double> ();
					//here I will set some default values for aop and sop and ...;
					if(host.getmaop().get(userid) == null){
					broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3, 0.3);
					//right now we don't consider the pricing model;
					//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
					host.getmaop().put(userid, bbb);
					}
					else {
						broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3);
						//right now we don't consider the pricing model;
						//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
					}
					host.getmsop().put(userid, aaa);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
				}
else if(host.getmtimes().get(userid) == 1){
host.addmtimes(userid);
//will add codes for sop and aop and ...;
broker13.CalNumOfRes1(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(1));
host.addmaop(userid, relinp.get(1));
//payprice = broker13.CalPrice(cloudlet13.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) == 2){
host.addmtimes(userid);
//will add codes for sop and aop and ...;
broker13.CalNumOfRes2(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(2));
host.addmaop(userid, relinp.get(2));
//payprice = broker13.CalPrice(cloudlet13.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) > 2){
broker13.CalNumOfRes(user.getuserId(), host.getId());	
int a = host.getmtimes().get(userid);
host.addmtimes(userid);
host.addmsop(userid, relinp.get(a));
host.addmaop(userid, relinp.get(a));

}
}


acpu.add(user.getnumCPU());
aram.add(user.getnumRAM());
asto.add(user.getnumStorage());
utimeh = host.getmtimes().get(userid) ;
System.out.println("this is the " + utimeh + "th time for user" + user.getuserId() + " to use services of host" + host.getId());
payprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()*0.001);
vmid = 13;

Vm vm13 = new Vm(vmid, broker13Id, mips, user.getnumCPU(), user.getnumRAM(), bw, user.getnumStorage(), vmm, new CloudletSchedulerTimeShared());

vm13.setHost(host);

//int vmid13 = 1;
//Vm vm13 = new Vm(vmid13, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
//add the VM to the vmList
vmlist.add(vm13);
//vmlist.add(vm13);

//submit vm list to the broker
broker13.submitVmList(vmlist);

broker13.bindCloudletToVm(cloudletid, vmid);

payprice = payprice * user.gettime();

aa = utimeh -1;
bb = relinp.get(aa);
System.out.println("the relinquish probability of the user for this time is " + bb);
//System.out.println(bb);
d = (int) (user.getnumCPU() * bb);
e = (int) (user.getnumRAM() * bb);
f = (long) (user.getnumStorage() * bb);
System.out.println("host " + host.getId() + " will give " + user.getnumCPU() + " CPUs and " +user.getnumRAM()+ " GB of RAM and " + user.getnumStorage() + " GB of storage to user" + userid + " to finish his cloudlet " + cloudlet1.getCloudletId());
System.out.println("the user will pay " + payprice + " dollars to the broker.");
reimbursement = payprice * bb;
System.out.println("then user" + user.getuserId() + " will relinquish " + d + " CPUs and " + e + " GB of RAM and " + f + " GB of storage");
System.out.println("the broker will reimburse" + reimbursement + " to the customer");
reimburse.add(reimbursement);
System.out.println(host.getmtimes().get(userid));
payment.add(payprice);




CloudSim.startSimulation();
///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////
CloudSim.init(num_user, calendar, trace_flag);

@SuppressWarnings("unused")
Datacenter datacenter14 = createDatacenter("Datacenter_1");

DatacenterBroker broker14 = createBroker();
int broker14Id = broker14.getId();

//update totalProfit;
totalProfit += payprice;

broker14.submitUserList(userlist);
broker14.BindUserToHost(user.getuserId(), host.getId());

host.setTotalProfit(totalProfit);

broker14.setHostList(hostlist1);

cloudletid = 14;
Cloudlet cloudlet14 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
cloudlet14.setbrokerId(broker14Id);
//update the price of cloudlet;
//price = 200;
//cloudlet14.setprice(price);
cloudlet14.setuser(user);
user.setCloudletId(cloudlet14.getCloudletId());

//add the cloudlet to the list
cloudletList.add(cloudlet14);

//submit cloudlet list to the broker
broker14.submitCloudletList(cloudletList);

//adrian

user.submitCloudlet(cloudlet14);
user.submitRequest(user.getCloudlet(), false, pesNumber, numram, (long)(fileSize/1000000000), numtime);


basicprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()/400);
pledge = basicprice * 1.05;
System.out.println("user" + user.getuserId() + " asks for " + user.getnumCPU() + " CPUs and " + user.getnumRAM() + " GB of RAM and " + user.getnumStorage() + " GB of storage to host" + host.getId());


if(cloudlet14.pledge == true){
//here I didn't consider giving back the extra money if the user will not relinquish any resource;
payprice = pledge;
}
else{
//host.minusmtimes(userid);
//here remember the BROKER is changing to BROKER14!!!!!!!!! and cloudlet is cloudlet14!!!!!!!!!
if (host.getmtimes().get(userid) == 0){
					host.addmtimes(userid);
					ArrayList<Double> aaa = new ArrayList<Double> ();
					ArrayList<Double> bbb = new ArrayList<Double> ();
					//here I will set some default values for aop and sop and ...;
					if(host.getmaop().get(userid) == null){
					broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3, 0.3);
					//right now we don't consider the pricing model;
					//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
					host.getmaop().put(userid, bbb);
					}
					else {
						broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3);
						//right now we don't consider the pricing model;
						//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
					}
					host.getmsop().put(userid, aaa);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
				}
else if(host.getmtimes().get(userid) == 1){
host.addmtimes(userid);
//will add codes for sop and aop and ...;
broker14.CalNumOfRes1(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(1));
host.addmaop(userid, relinp.get(1));
//payprice = broker14.CalPrice(cloudlet14.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) == 2){
host.addmtimes(userid);
//will add codes for sop and aop and ...;
broker14.CalNumOfRes2(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(2));
host.addmaop(userid, relinp.get(2));
//payprice = broker14.CalPrice(cloudlet14.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) > 2){
broker14.CalNumOfRes(user.getuserId(), host.getId());	
int a = host.getmtimes().get(userid);
host.addmtimes(userid);
host.addmsop(userid, relinp.get(a));
host.addmaop(userid, relinp.get(a));

}
}


acpu.add(user.getnumCPU());
aram.add(user.getnumRAM());
asto.add(user.getnumStorage());
utimeh = host.getmtimes().get(userid) ;
System.out.println("this is the " + utimeh + "th time for user" + user.getuserId() + " to use services of host" + host.getId());
payprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()*0.001);
vmid = 14;

Vm vm14 = new Vm(vmid, broker14Id, mips, user.getnumCPU(), user.getnumRAM(), bw, user.getnumStorage(), vmm, new CloudletSchedulerTimeShared());

vm14.setHost(host);

//int vmid14 = 1;
//Vm vm14 = new Vm(vmid14, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
//add the VM to the vmList
vmlist.add(vm14);
//vmlist.add(vm14);

//submit vm list to the broker
broker14.submitVmList(vmlist);

broker14.bindCloudletToVm(cloudletid, vmid);

payprice = payprice * user.gettime();

aa = utimeh -1;
bb = relinp.get(aa);
System.out.println("the relinquish probability of the user for this time is " + bb);
//System.out.println(bb);
d = (int) (user.getnumCPU() * bb);
e = (int) (user.getnumRAM() * bb);
f = (long) (user.getnumStorage() * bb);
System.out.println("host " + host.getId() + " will give " + user.getnumCPU() + " CPUs and " +user.getnumRAM()+ " GB of RAM and " + user.getnumStorage() + " GB of storage to user" + userid + " to finish his cloudlet " + cloudlet1.getCloudletId());
System.out.println("the user will pay " + payprice + " dollars to the broker.");
reimbursement = payprice * bb;
System.out.println("then user" + user.getuserId() + " will relinquish " + d + " CPUs and " + e + " GB of RAM and " + f + " GB of storage");
System.out.println("the broker will reimburse" + reimbursement + " to the customer");
reimburse.add(reimbursement);
System.out.println(host.getmtimes().get(userid));
payment.add(payprice);




CloudSim.startSimulation();
///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////
CloudSim.init(num_user, calendar, trace_flag);

@SuppressWarnings("unused")
Datacenter datacenter15 = createDatacenter("Datacenter_1");

DatacenterBroker broker15 = createBroker();
int broker15Id = broker15.getId();

//update totalProfit;
totalProfit += payprice;

broker15.submitUserList(userlist);
broker15.BindUserToHost(user.getuserId(), host.getId());

host.setTotalProfit(totalProfit);

broker15.setHostList(hostlist1);

cloudletid = 15;
Cloudlet cloudlet15 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
cloudlet15.setbrokerId(broker15Id);
//update the price of cloudlet;
//price = 200;
//cloudlet15.setprice(price);
cloudlet15.setuser(user);
user.setCloudletId(cloudlet15.getCloudletId());

//add the cloudlet to the list
cloudletList.add(cloudlet15);

//submit cloudlet list to the broker
broker15.submitCloudletList(cloudletList);

//adrian

user.submitCloudlet(cloudlet15);
user.submitRequest(user.getCloudlet(), false, pesNumber, numram, (long)(fileSize/1000000000), numtime);


basicprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()/400);
pledge = basicprice * 1.05;
System.out.println("user" + user.getuserId() + " asks for " + user.getnumCPU() + " CPUs and " + user.getnumRAM() + " GB of RAM and " + user.getnumStorage() + " GB of storage to host" + host.getId());


if(cloudlet15.pledge == true){
//here I didn't consider giving back the extra money if the user will not relinquish any resource;
payprice = pledge;
}
else{
//host.minusmtimes(userid);
//here remember the BROKER is changing to BROKER15!!!!!!!!! and cloudlet is cloudlet15!!!!!!!!!
if (host.getmtimes().get(userid) == 0){
					host.addmtimes(userid);
					ArrayList<Double> aaa = new ArrayList<Double> ();
					ArrayList<Double> bbb = new ArrayList<Double> ();
					//here I will set some default values for aop and sop and ...;
					if(host.getmaop().get(userid) == null){
					broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3, 0.3);
					//right now we don't consider the pricing model;
					//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
					host.getmaop().put(userid, bbb);
					}
					else {
						broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3);
						//right now we don't consider the pricing model;
						//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
					}
					host.getmsop().put(userid, aaa);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
				}
else if(host.getmtimes().get(userid) == 1){
host.addmtimes(userid);
//will add codes for sop and aop and ...;
broker15.CalNumOfRes1(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(1));
host.addmaop(userid, relinp.get(1));
//payprice = broker15.CalPrice(cloudlet15.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) == 2){
host.addmtimes(userid);
//will add codes for sop and aop and ...;
broker15.CalNumOfRes2(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(2));
host.addmaop(userid, relinp.get(2));
//payprice = broker15.CalPrice(cloudlet15.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) > 2){
broker15.CalNumOfRes(user.getuserId(), host.getId());	
int a = host.getmtimes().get(userid);
host.addmtimes(userid);
host.addmsop(userid, relinp.get(a));
host.addmaop(userid, relinp.get(a));

}
}


acpu.add(user.getnumCPU());
aram.add(user.getnumRAM());
asto.add(user.getnumStorage());
utimeh = host.getmtimes().get(userid) ;
System.out.println("this is the " + utimeh + "th time for user" + user.getuserId() + " to use services of host" + host.getId());
payprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()*0.001);
vmid = 15;

Vm vm15 = new Vm(vmid, broker15Id, mips, user.getnumCPU(), user.getnumRAM(), bw, user.getnumStorage(), vmm, new CloudletSchedulerTimeShared());

vm15.setHost(host);

//int vmid15 = 1;
//Vm vm15 = new Vm(vmid15, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
//add the VM to the vmList
vmlist.add(vm15);
//vmlist.add(vm15);

//submit vm list to the broker
broker15.submitVmList(vmlist);

broker15.bindCloudletToVm(cloudletid, vmid);

payprice = payprice * user.gettime();

aa = utimeh -1;
bb = relinp.get(aa);
System.out.println("the relinquish probability of the user for this time is " + bb);
//System.out.println(bb);
d = (int) (user.getnumCPU() * bb);
e = (int) (user.getnumRAM() * bb);
f = (long) (user.getnumStorage() * bb);
System.out.println("host " + host.getId() + " will give " + user.getnumCPU() + " CPUs and " +user.getnumRAM()+ " GB of RAM and " + user.getnumStorage() + " GB of storage to user" + userid + " to finish his cloudlet " + cloudlet1.getCloudletId());
System.out.println("the user will pay " + payprice + " dollars to the broker.");
reimbursement = payprice * bb;
System.out.println("then user" + user.getuserId() + " will relinquish " + d + " CPUs and " + e + " GB of RAM and " + f + " GB of storage");
System.out.println("the broker will reimburse" + reimbursement + " to the customer");
reimburse.add(reimbursement);
System.out.println(host.getmtimes().get(userid));
payment.add(payprice);



CloudSim.startSimulation();
///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////

CloudSim.init(num_user, calendar, trace_flag);

@SuppressWarnings("unused")
Datacenter datacenter16 = createDatacenter("Datacenter_1");

DatacenterBroker broker16 = createBroker();
int broker16Id = broker16.getId();

//update totalProfit;
totalProfit += payprice;

broker16.submitUserList(userlist);
broker16.BindUserToHost(user.getuserId(), host.getId());

host.setTotalProfit(totalProfit);

broker16.setHostList(hostlist1);

cloudletid = 16;
Cloudlet cloudlet16 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
cloudlet16.setbrokerId(broker16Id);
//update the price of cloudlet;
//price = 200;
//cloudlet16.setprice(price);
cloudlet16.setuser(user);
user.setCloudletId(cloudlet16.getCloudletId());

//add the cloudlet to the list
cloudletList.add(cloudlet16);

//submit cloudlet list to the broker
broker16.submitCloudletList(cloudletList);

//adrian

user.submitCloudlet(cloudlet16);
user.submitRequest(user.getCloudlet(), false, pesNumber, numram, (long)(fileSize/1000000000), numtime);


basicprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()/400);
pledge = basicprice * 1.05;
System.out.println("user" + user.getuserId() + " asks for " + user.getnumCPU() + " CPUs and " + user.getnumRAM() + " GB of RAM and " + user.getnumStorage() + " GB of storage to host" + host.getId());


if(cloudlet16.pledge == true){
//here I didn't consider giving back the extra money if the user will not relinquish any resource;
payprice = pledge;
}
else{
//host.minusmtimes(userid);
//here remember the BROKER is changing to BROKER16!!!!!!!!! and cloudlet is cloudlet16!!!!!!!!!
if (host.getmtimes().get(userid) == 0){
					host.addmtimes(userid);
					ArrayList<Double> aaa = new ArrayList<Double> ();
					ArrayList<Double> bbb = new ArrayList<Double> ();
					//here I will set some default values for aop and sop and ...;
					if(host.getmaop().get(userid) == null){
					broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3, 0.3);
					//right now we don't consider the pricing model;
					//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
					host.getmaop().put(userid, bbb);
					}
					else {
						broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3);
						//right now we don't consider the pricing model;
						//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
					}
					host.getmsop().put(userid, aaa);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
				}
else if(host.getmtimes().get(userid) == 1){
host.addmtimes(userid);
//will add codes for sop and aop and ...;
broker16.CalNumOfRes1(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(1));
host.addmaop(userid, relinp.get(1));
//payprice = broker16.CalPrice(cloudlet16.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) == 2){
host.addmtimes(userid);
//will add codes for sop and aop and ...;
broker16.CalNumOfRes2(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(2));
host.addmaop(userid, relinp.get(2));
//payprice = broker16.CalPrice(cloudlet16.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) > 2){
broker16.CalNumOfRes(user.getuserId(), host.getId());	
int a = host.getmtimes().get(userid);
host.addmtimes(userid);
host.addmsop(userid, relinp.get(a));
host.addmaop(userid, relinp.get(a));

}
}


acpu.add(user.getnumCPU());
aram.add(user.getnumRAM());
asto.add(user.getnumStorage());
utimeh = host.getmtimes().get(userid) ;
System.out.println("this is the " + utimeh + "th time for user" + user.getuserId() + " to use services of host" + host.getId());
payprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()*0.001);
vmid = 16;

Vm vm16 = new Vm(vmid, broker16Id, mips, user.getnumCPU(), user.getnumRAM(), bw, user.getnumStorage(), vmm, new CloudletSchedulerTimeShared());

vm16.setHost(host);

//int vmid16 = 1;
//Vm vm16 = new Vm(vmid16, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
//add the VM to the vmList
vmlist.add(vm16);
//vmlist.add(vm16);

//submit vm list to the broker
broker16.submitVmList(vmlist);

broker16.bindCloudletToVm(cloudletid, vmid);

payprice = payprice * user.gettime();

aa = utimeh -1;
bb = relinp.get(aa);
System.out.println("the relinquish probability of the user for this time is " + bb);
//System.out.println(bb);
d = (int) (user.getnumCPU() * bb);
e = (int) (user.getnumRAM() * bb);
f = (long) (user.getnumStorage() * bb);
System.out.println("host " + host.getId() + " will give " + user.getnumCPU() + " CPUs and " +user.getnumRAM()+ " GB of RAM and " + user.getnumStorage() + " GB of storage to user" + userid + " to finish his cloudlet " + cloudlet1.getCloudletId());
System.out.println("the user will pay " + payprice + " dollars to the broker.");
reimbursement = payprice * bb;
System.out.println("then user" + user.getuserId() + " will relinquish " + d + " CPUs and " + e + " GB of RAM and " + f + " GB of storage");
System.out.println("the broker will reimburse" + reimbursement + " to the customer");
reimburse.add(reimbursement);
System.out.println(host.getmtimes().get(userid));
payment.add(payprice);



CloudSim.startSimulation();
///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////
CloudSim.init(num_user, calendar, trace_flag);

@SuppressWarnings("unused")
Datacenter datacenter17 = createDatacenter("Datacenter_1");

DatacenterBroker broker17 = createBroker();
int broker17Id = broker17.getId();

//update totalProfit;
totalProfit += payprice;

broker17.submitUserList(userlist);
broker17.BindUserToHost(user.getuserId(), host.getId());

host.setTotalProfit(totalProfit);

broker17.setHostList(hostlist1);

cloudletid = 17;
Cloudlet cloudlet17 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
cloudlet17.setbrokerId(broker17Id);
//update the price of cloudlet;
//price = 200;
//cloudlet17.setprice(price);
cloudlet17.setuser(user);
user.setCloudletId(cloudlet17.getCloudletId());

//add the cloudlet to the list
cloudletList.add(cloudlet17);

//submit cloudlet list to the broker
broker17.submitCloudletList(cloudletList);

//adrian

user.submitCloudlet(cloudlet17);
user.submitRequest(user.getCloudlet(), false, pesNumber, numram, (long)(fileSize/1000000000), numtime);


basicprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()/400);
pledge = basicprice * 1.05;
System.out.println("user" + user.getuserId() + " asks for " + user.getnumCPU() + " CPUs and " + user.getnumRAM() + " GB of RAM and " + user.getnumStorage() + " GB of storage to host" + host.getId());


if(cloudlet17.pledge == true){
//here I didn't consider giving back the extra money if the user will not relinquish any resource;
payprice = pledge;
}
else{
//host.minusmtimes(userid);
//here remember the BROKER is changing to BROKER17!!!!!!!!! and cloudlet is cloudlet17!!!!!!!!!
if (host.getmtimes().get(userid) == 0){
					host.addmtimes(userid);
					ArrayList<Double> aaa = new ArrayList<Double> ();
					ArrayList<Double> bbb = new ArrayList<Double> ();
					//here I will set some default values for aop and sop and ...;
					if(host.getmaop().get(userid) == null){
					broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3, 0.3);
					//right now we don't consider the pricing model;
					//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
					host.getmaop().put(userid, bbb);
					}
					else {
						broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3);
						//right now we don't consider the pricing model;
						//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
					}
					host.getmsop().put(userid, aaa);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
				}
else if(host.getmtimes().get(userid) == 1){
host.addmtimes(userid);
//will add codes for sop and aop and ...;
broker17.CalNumOfRes1(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(1));
host.addmaop(userid, relinp.get(1));
//payprice = broker17.CalPrice(cloudlet17.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) == 2){
host.addmtimes(userid);
//will add codes for sop and aop and ...;
broker17.CalNumOfRes2(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(2));
host.addmaop(userid, relinp.get(2));
//payprice = broker17.CalPrice(cloudlet17.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) > 2){
broker17.CalNumOfRes(user.getuserId(), host.getId());	
int a = host.getmtimes().get(userid);
host.addmtimes(userid);
host.addmsop(userid, relinp.get(a));
host.addmaop(userid, relinp.get(a));

}
}


acpu.add(user.getnumCPU());
aram.add(user.getnumRAM());
asto.add(user.getnumStorage());
utimeh = host.getmtimes().get(userid) ;
System.out.println("this is the " + utimeh + "th time for user" + user.getuserId() + " to use services of host" + host.getId());
payprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()*0.001);
vmid = 17;

Vm vm17 = new Vm(vmid, broker17Id, mips, user.getnumCPU(), user.getnumRAM(), bw, user.getnumStorage(), vmm, new CloudletSchedulerTimeShared());

vm17.setHost(host);

//int vmid17 = 1;
//Vm vm17 = new Vm(vmid17, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
//add the VM to the vmList
vmlist.add(vm17);
//vmlist.add(vm17);

//submit vm list to the broker
broker17.submitVmList(vmlist);

broker17.bindCloudletToVm(cloudletid, vmid);

payprice = payprice * user.gettime();

aa = utimeh -1;
bb = relinp.get(aa);
System.out.println("the relinquish probability of the user for this time is " + bb);
//System.out.println(bb);
d = (int) (user.getnumCPU() * bb);
e = (int) (user.getnumRAM() * bb);
f = (long) (user.getnumStorage() * bb);
System.out.println("host " + host.getId() + " will give " + user.getnumCPU() + " CPUs and " +user.getnumRAM()+ " GB of RAM and " + user.getnumStorage() + " GB of storage to user" + userid + " to finish his cloudlet " + cloudlet1.getCloudletId());
System.out.println("the user will pay " + payprice + " dollars to the broker.");
reimbursement = payprice * bb;
System.out.println("then user" + user.getuserId() + " will relinquish " + d + " CPUs and " + e + " GB of RAM and " + f + " GB of storage");
System.out.println("the broker will reimburse" + reimbursement + " to the customer");
reimburse.add(reimbursement);
System.out.println(host.getmtimes().get(userid));
payment.add(payprice);



CloudSim.startSimulation();
///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////
CloudSim.init(num_user, calendar, trace_flag);

@SuppressWarnings("unused")
Datacenter datacenter18 = createDatacenter("Datacenter_1");

DatacenterBroker broker18 = createBroker();
int broker18Id = broker18.getId();

//update totalProfit;
totalProfit += payprice;

broker18.submitUserList(userlist);
broker18.BindUserToHost(user.getuserId(), host.getId());

host.setTotalProfit(totalProfit);

broker18.setHostList(hostlist1);

cloudletid = 18;
Cloudlet cloudlet18 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
cloudlet18.setbrokerId(broker18Id);
//update the price of cloudlet;
//price = 200;
//cloudlet18.setprice(price);
cloudlet18.setuser(user);
user.setCloudletId(cloudlet18.getCloudletId());

//add the cloudlet to the list
cloudletList.add(cloudlet18);

//submit cloudlet list to the broker
broker18.submitCloudletList(cloudletList);

//adrian

user.submitCloudlet(cloudlet18);
user.submitRequest(user.getCloudlet(), false, pesNumber, numram, (long)(fileSize/1000000000), numtime);


basicprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()/400);
pledge = basicprice * 1.05;
System.out.println("user" + user.getuserId() + " asks for " + user.getnumCPU() + " CPUs and " + user.getnumRAM() + " GB of RAM and " + user.getnumStorage() + " GB of storage to host" + host.getId());


if(cloudlet18.pledge == true){
//here I didn't consider giving back the extra money if the user will not relinquish any resource;
payprice = pledge;
}
else{
//host.minusmtimes(userid);
//here remember the BROKER is changing to BROKER18!!!!!!!!! and cloudlet is cloudlet18!!!!!!!!!
if (host.getmtimes().get(userid) == 0){
					host.addmtimes(userid);
					ArrayList<Double> aaa = new ArrayList<Double> ();
					ArrayList<Double> bbb = new ArrayList<Double> ();
					//here I will set some default values for aop and sop and ...;
					if(host.getmaop().get(userid) == null){
					broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3, 0.3);
					//right now we don't consider the pricing model;
					//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
					host.getmaop().put(userid, bbb);
					}
					else {
						broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3);
						//right now we don't consider the pricing model;
						//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
					}
					host.getmsop().put(userid, aaa);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
				}
else if(host.getmtimes().get(userid) == 1){
host.addmtimes(userid);
//will add codes for sop and aop and ...;
broker18.CalNumOfRes1(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(1));
host.addmaop(userid, relinp.get(1));
//payprice = broker18.CalPrice(cloudlet18.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) == 2){
host.addmtimes(userid);
//will add codes for sop and aop and ...;
broker18.CalNumOfRes2(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(2));
host.addmaop(userid, relinp.get(2));
//payprice = broker18.CalPrice(cloudlet18.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) > 2){
broker18.CalNumOfRes(user.getuserId(), host.getId());	
int a = host.getmtimes().get(userid);
host.addmtimes(userid);
host.addmsop(userid, relinp.get(a));
host.addmaop(userid, relinp.get(a));

}
}


acpu.add(user.getnumCPU());
aram.add(user.getnumRAM());
asto.add(user.getnumStorage());
utimeh = host.getmtimes().get(userid) ;
System.out.println("this is the " + utimeh + "th time for user" + user.getuserId() + " to use services of host" + host.getId());
payprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()*0.001);
vmid = 18;

Vm vm18 = new Vm(vmid, broker18Id, mips, user.getnumCPU(), user.getnumRAM(), bw, user.getnumStorage(), vmm, new CloudletSchedulerTimeShared());

vm18.setHost(host);

//int vmid18 = 1;
//Vm vm18 = new Vm(vmid18, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
//add the VM to the vmList
vmlist.add(vm18);
//vmlist.add(vm18);

//submit vm list to the broker
broker18.submitVmList(vmlist);

broker18.bindCloudletToVm(cloudletid, vmid);

payprice = payprice * user.gettime();

aa = utimeh -1;
bb = relinp.get(aa);
System.out.println("the relinquish probability of the user for this time is " + bb);
//System.out.println(bb);
d = (int) (user.getnumCPU() * bb);
e = (int) (user.getnumRAM() * bb);
f = (long) (user.getnumStorage() * bb);
System.out.println("host " + host.getId() + " will give " + user.getnumCPU() + " CPUs and " +user.getnumRAM()+ " GB of RAM and " + user.getnumStorage() + " GB of storage to user" + userid + " to finish his cloudlet " + cloudlet1.getCloudletId());
System.out.println("the user will pay " + payprice + " dollars to the broker.");
reimbursement = payprice * bb;
System.out.println("then user" + user.getuserId() + " will relinquish " + d + " CPUs and " + e + " GB of RAM and " + f + " GB of storage");
System.out.println("the broker will reimburse" + reimbursement + " to the customer");
reimburse.add(reimbursement);
System.out.println(host.getmtimes().get(userid));
payment.add(payprice);



CloudSim.startSimulation();
///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////
CloudSim.init(num_user, calendar, trace_flag);

@SuppressWarnings("unused")
Datacenter datacenter19 = createDatacenter("Datacenter_1");

DatacenterBroker broker19 = createBroker();
int broker19Id = broker19.getId();

//update totalProfit;
totalProfit += payprice;

broker19.submitUserList(userlist);
broker19.BindUserToHost(user.getuserId(), host.getId());

host.setTotalProfit(totalProfit);

broker19.setHostList(hostlist1);

cloudletid = 19;
Cloudlet cloudlet19 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
cloudlet19.setbrokerId(broker19Id);
//update the price of cloudlet;
//price = 200;
//cloudlet19.setprice(price);
cloudlet19.setuser(user);
user.setCloudletId(cloudlet19.getCloudletId());

//add the cloudlet to the list
cloudletList.add(cloudlet19);

//submit cloudlet list to the broker
broker19.submitCloudletList(cloudletList);

//adrian

user.submitCloudlet(cloudlet19);
user.submitRequest(user.getCloudlet(), false, pesNumber, numram, (long)(fileSize/1000000000), numtime);


basicprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()/400);
pledge = basicprice * 1.05;
System.out.println("user" + user.getuserId() + " asks for " + user.getnumCPU() + " CPUs and " + user.getnumRAM() + " GB of RAM and " + user.getnumStorage() + " GB of storage to host" + host.getId());


if(cloudlet19.pledge == true){
//here I didn't consider giving back the extra money if the user will not relinquish any resource;
payprice = pledge;
}
else{
//host.minusmtimes(userid);
//here remember the BROKER is changing to BROKER19!!!!!!!!! and cloudlet is cloudlet19!!!!!!!!!
if (host.getmtimes().get(userid) == 0){
					host.addmtimes(userid);
					ArrayList<Double> aaa = new ArrayList<Double> ();
					ArrayList<Double> bbb = new ArrayList<Double> ();
					//here I will set some default values for aop and sop and ...;
					if(host.getmaop().get(userid) == null){
					broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3, 0.3);
					//right now we don't consider the pricing model;
					//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
					host.getmaop().put(userid, bbb);
					}
					else {
						broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3);
						//right now we don't consider the pricing model;
						//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
					}
					host.getmsop().put(userid, aaa);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
				}
else if(host.getmtimes().get(userid) == 1){
host.addmtimes(userid);
//will add codes for sop and aop and ...;
broker19.CalNumOfRes1(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(1));
host.addmaop(userid, relinp.get(1));
//payprice = broker19.CalPrice(cloudlet19.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) == 2){
host.addmtimes(userid);
//will add codes for sop and aop and ...;
broker19.CalNumOfRes2(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(2));
host.addmaop(userid, relinp.get(2));
//payprice = broker19.CalPrice(cloudlet19.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) > 2){
broker19.CalNumOfRes(user.getuserId(), host.getId());	
int a = host.getmtimes().get(userid);
host.addmtimes(userid);
host.addmsop(userid, relinp.get(a));
host.addmaop(userid, relinp.get(a));

}
}


acpu.add(user.getnumCPU());
aram.add(user.getnumRAM());
asto.add(user.getnumStorage());
utimeh = host.getmtimes().get(userid) ;
System.out.println("this is the " + utimeh + "th time for user" + user.getuserId() + " to use services of host" + host.getId());
payprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()*0.001);
vmid = 19;

Vm vm19 = new Vm(vmid, broker19Id, mips, user.getnumCPU(), user.getnumRAM(), bw, user.getnumStorage(), vmm, new CloudletSchedulerTimeShared());

vm19.setHost(host);

//int vmid19 = 1;
//Vm vm19 = new Vm(vmid19, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
//add the VM to the vmList
vmlist.add(vm19);
//vmlist.add(vm19);

//submit vm list to the broker
broker19.submitVmList(vmlist);

broker19.bindCloudletToVm(cloudletid, vmid);

payprice = payprice * user.gettime();

aa = utimeh -1;
bb = relinp.get(aa);
System.out.println("the relinquish probability of the user for this time is " + bb);
//System.out.println(bb);
d = (int) (user.getnumCPU() * bb);
e = (int) (user.getnumRAM() * bb);
f = (long) (user.getnumStorage() * bb);
System.out.println("host " + host.getId() + " will give " + user.getnumCPU() + " CPUs and " +user.getnumRAM()+ " GB of RAM and " + user.getnumStorage() + " GB of storage to user" + userid + " to finish his cloudlet " + cloudlet1.getCloudletId());
System.out.println("the user will pay " + payprice + " dollars to the broker.");
reimbursement = payprice * bb;
System.out.println("then user" + user.getuserId() + " will relinquish " + d + " CPUs and " + e + " GB of RAM and " + f + " GB of storage");
System.out.println("the broker will reimburse" + reimbursement + " to the customer");
reimburse.add(reimbursement);
System.out.println(host.getmtimes().get(userid));
payment.add(payprice);


CloudSim.startSimulation();
///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////


CloudSim.init(num_user, calendar, trace_flag);

@SuppressWarnings("unused")
Datacenter datacenter20 = createDatacenter("Datacenter_1");

DatacenterBroker broker20 = createBroker();
int broker20Id = broker20.getId();

//update totalProfit;
totalProfit += payprice;

broker20.submitUserList(userlist);
broker20.BindUserToHost(user.getuserId(), host.getId());

host.setTotalProfit(totalProfit);

broker20.setHostList(hostlist1);

cloudletid = 20;
Cloudlet cloudlet20 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
cloudlet20.setbrokerId(broker20Id);
//update the price of cloudlet;
//price = 200;
//cloudlet20.setprice(price);
cloudlet20.setuser(user);
user.setCloudletId(cloudlet20.getCloudletId());

//add the cloudlet to the list
cloudletList.add(cloudlet20);

//submit cloudlet list to the broker
broker20.submitCloudletList(cloudletList);

//adrian

user.submitCloudlet(cloudlet20);
user.submitRequest(user.getCloudlet(), false, pesNumber, numram, (long)(fileSize/1000000000), numtime);


basicprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()/400);
pledge = basicprice * 1.05;
System.out.println("user" + user.getuserId() + " asks for " + user.getnumCPU() + " CPUs and " + user.getnumRAM() + " GB of RAM and " + user.getnumStorage() + " GB of storage to host" + host.getId());


if(cloudlet20.pledge == true){
//here I didn't consider giving back the extra money if the user will not relinquish any resource;
payprice = pledge;
}
else{
//host.minusmtimes(userid);
//here remember the BROKER is changing to BROKER20!!!!!!!!! and cloudlet is cloudlet20!!!!!!!!!
if (host.getmtimes().get(userid) == 0){
					host.addmtimes(userid);
					ArrayList<Double> aaa = new ArrayList<Double> ();
					ArrayList<Double> bbb = new ArrayList<Double> ();
					//here I will set some default values for aop and sop and ...;
					if(host.getmaop().get(userid) == null){
					broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3, 0.3);
					//right now we don't consider the pricing model;
					//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
					host.getmaop().put(userid, bbb);
					}
					else {
						broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3);
						//right now we don't consider the pricing model;
						//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
					}
					host.getmsop().put(userid, aaa);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
				}
else if(host.getmtimes().get(userid) == 1){
host.addmtimes(userid);
//will add codes for sop and aop and ...;
broker20.CalNumOfRes1(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(1));
host.addmaop(userid, relinp.get(1));
//payprice = broker20.CalPrice(cloudlet20.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) == 2){
host.addmtimes(userid);
//will add codes for sop and aop and ...;
broker20.CalNumOfRes2(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(2));
host.addmaop(userid, relinp.get(2));
//payprice = broker20.CalPrice(cloudlet20.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) > 2){
broker20.CalNumOfRes(user.getuserId(), host.getId());	
int a = host.getmtimes().get(userid);
host.addmtimes(userid);
host.addmsop(userid, relinp.get(a));
host.addmaop(userid, relinp.get(a));

}
}



utimeh = host.getmtimes().get(userid) ;
System.out.println("this is the " + utimeh + "th time for user" + user.getuserId() + " to use services of host" + host.getId());
payprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()*0.001);
vmid = 20;

Vm vm20 = new Vm(vmid, broker20Id, mips, user.getnumCPU(), user.getnumRAM(), bw, user.getnumStorage(), vmm, new CloudletSchedulerTimeShared());

vm20.setHost(host);

//int vmid20 = 1;
//Vm vm20 = new Vm(vmid20, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
//add the VM to the vmList
vmlist.add(vm20);
//vmlist.add(vm20);

//submit vm list to the broker
broker20.submitVmList(vmlist);

broker20.bindCloudletToVm(cloudletid, vmid);

payprice = payprice * user.gettime();

aa = utimeh -1;
bb = relinp.get(aa);
System.out.println("the relinquish probability of the user for this time is " + bb);
//System.out.println(bb);
d = (int) (user.getnumCPU() * bb);
e = (int) (user.getnumRAM() * bb);
f = (long) (user.getnumStorage() * bb);
System.out.println("host " + host.getId() + " will give " + user.getnumCPU() + " CPUs and " +user.getnumRAM()+ " GB of RAM and " + user.getnumStorage() + " GB of storage to user" + userid + " to finish his cloudlet " + cloudlet1.getCloudletId());
System.out.println("the user will pay " + payprice + " dollars to the broker.");
reimbursement = payprice * bb;
System.out.println("then user" + user.getuserId() + " will relinquish " + d + " CPUs and " + e + " GB of RAM and " + f + " GB of storage");
System.out.println("the broker will reimburse" + reimbursement + " to the customer");
reimburse.add(reimbursement);
System.out.println(host.getmtimes().get(userid));
CloudSim.startSimulation();
///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////

CloudSim.init(num_user, calendar, trace_flag);

@SuppressWarnings("unused")
Datacenter datacenter21 = createDatacenter("Datacenter_1");

DatacenterBroker broker21 = createBroker();
int broker21Id = broker21.getId();

//update totalProfit;
totalProfit += payprice;

broker21.submitUserList(userlist);
broker21.BindUserToHost(user.getuserId(), host.getId());

host.setTotalProfit(totalProfit);

broker21.setHostList(hostlist1);

cloudletid = 21;
Cloudlet cloudlet21 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
cloudlet21.setbrokerId(broker21Id);
//update the price of cloudlet;
//price = 200;
//cloudlet21.setprice(price);
cloudlet21.setuser(user);
user.setCloudletId(cloudlet21.getCloudletId());

//add the cloudlet to the list
cloudletList.add(cloudlet21);

//submit cloudlet list to the broker
broker21.submitCloudletList(cloudletList);

//adrian

user.submitCloudlet(cloudlet21);
user.submitRequest(user.getCloudlet(), false, pesNumber, numram, (long)(fileSize/1000000000), numtime);


basicprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()/400);
pledge = basicprice * 1.05;
System.out.println("user" + user.getuserId() + " asks for " + user.getnumCPU() + " CPUs and " + user.getnumRAM() + " GB of RAM and " + user.getnumStorage() + " GB of storage to host" + host.getId());


if(cloudlet21.pledge == true){
//here I didn't consider giving back the extra money if the user will not relinquish any resource;
payprice = pledge;
}
else{
//host.minusmtimes(userid);
//here remember the BROKER is changing to BROKER21!!!!!!!!! and cloudlet is cloudlet21!!!!!!!!!
if (host.getmtimes().get(userid) == 0){
					host.addmtimes(userid);
					ArrayList<Double> aaa = new ArrayList<Double> ();
					ArrayList<Double> bbb = new ArrayList<Double> ();
					//here I will set some default values for aop and sop and ...;
					if(host.getmaop().get(userid) == null){
					broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3, 0.3);
					//right now we don't consider the pricing model;
					//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
					host.getmaop().put(userid, bbb);
					}
					else {
						broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3);
						//right now we don't consider the pricing model;
						//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
					}
					host.getmsop().put(userid, aaa);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
				}
else if(host.getmtimes().get(userid) == 1){
host.addmtimes(userid);
//will add codes for sop and aop and ...;
broker21.CalNumOfRes1(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(1));
host.addmaop(userid, relinp.get(1));
//payprice = broker21.CalPrice(cloudlet21.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) == 2){
host.addmtimes(userid);
//will add codes for sop and aop and ...;
broker21.CalNumOfRes2(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(2));
host.addmaop(userid, relinp.get(2));
//payprice = broker21.CalPrice(cloudlet21.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) > 2){
broker21.CalNumOfRes(user.getuserId(), host.getId());	
int a = host.getmtimes().get(userid);
host.addmtimes(userid);
host.addmsop(userid, relinp.get(a));
host.addmaop(userid, relinp.get(a));

}
}



utimeh = host.getmtimes().get(userid) ;
System.out.println("this is the " + utimeh + "th time for user" + user.getuserId() + " to use services of host" + host.getId());
payprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()*0.001);
vmid = 21;

Vm vm21 = new Vm(vmid, broker21Id, mips, user.getnumCPU(), user.getnumRAM(), bw, user.getnumStorage(), vmm, new CloudletSchedulerTimeShared());

vm21.setHost(host);

//int vmid21 = 1;
//Vm vm21 = new Vm(vmid21, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
//add the VM to the vmList
vmlist.add(vm21);
//vmlist.add(vm21);

//submit vm list to the broker
broker21.submitVmList(vmlist);

broker21.bindCloudletToVm(cloudletid, vmid);

payprice = payprice * user.gettime();

aa = utimeh -1;
bb = relinp.get(aa);
System.out.println("the relinquish probability of the user for this time is " + bb);
//System.out.println(bb);
d = (int) (user.getnumCPU() * bb);
e = (int) (user.getnumRAM() * bb);
f = (long) (user.getnumStorage() * bb);
System.out.println("host " + host.getId() + " will give " + user.getnumCPU() + " CPUs and " +user.getnumRAM()+ " GB of RAM and " + user.getnumStorage() + " GB of storage to user" + userid + " to finish his cloudlet " + cloudlet1.getCloudletId());
System.out.println("the user will pay " + payprice + " dollars to the broker.");
reimbursement = payprice * bb;
System.out.println("then user" + user.getuserId() + " will relinquish " + d + " CPUs and " + e + " GB of RAM and " + f + " GB of storage");
System.out.println("the broker will reimburse" + reimbursement + " to the customer");
reimburse.add(reimbursement);
System.out.println(host.getmtimes().get(userid));
CloudSim.startSimulation();
///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////
CloudSim.init(num_user, calendar, trace_flag);

@SuppressWarnings("unused")
Datacenter datacenter22 = createDatacenter("Datacenter_1");

DatacenterBroker broker22 = createBroker();
int broker22Id = broker22.getId();

//update totalProfit;
totalProfit += payprice;

broker22.submitUserList(userlist);
broker22.BindUserToHost(user.getuserId(), host.getId());

host.setTotalProfit(totalProfit);

broker22.setHostList(hostlist1);

cloudletid = 22;
Cloudlet cloudlet22 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
cloudlet22.setbrokerId(broker22Id);
//update the price of cloudlet;
//price = 200;
//cloudlet22.setprice(price);
cloudlet22.setuser(user);
user.setCloudletId(cloudlet22.getCloudletId());

//add the cloudlet to the list
cloudletList.add(cloudlet22);

//submit cloudlet list to the broker
broker22.submitCloudletList(cloudletList);

//adrian

user.submitCloudlet(cloudlet22);
user.submitRequest(user.getCloudlet(), false, pesNumber, numram, (long)(fileSize/1000000000), numtime);


basicprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()/400);
pledge = basicprice * 1.05;
System.out.println("user" + user.getuserId() + " asks for " + user.getnumCPU() + " CPUs and " + user.getnumRAM() + " GB of RAM and " + user.getnumStorage() + " GB of storage to host" + host.getId());


if(cloudlet22.pledge == true){
//here I didn't consider giving back the extra money if the user will not relinquish any resource;
payprice = pledge;
}
else{
//host.minusmtimes(userid);
//here remember the BROKER is changing to BROKER22!!!!!!!!! and cloudlet is cloudlet22!!!!!!!!!
if (host.getmtimes().get(userid) == 0){
					host.addmtimes(userid);
					ArrayList<Double> aaa = new ArrayList<Double> ();
					ArrayList<Double> bbb = new ArrayList<Double> ();
					//here I will set some default values for aop and sop and ...;
					if(host.getmaop().get(userid) == null){
					broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3, 0.3);
					//right now we don't consider the pricing model;
					//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
					host.getmaop().put(userid, bbb);
					}
					else {
						broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3);
						//right now we don't consider the pricing model;
						//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
					}
					host.getmsop().put(userid, aaa);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
				}
else if(host.getmtimes().get(userid) == 1){
host.addmtimes(userid);
//will add codes for sop and aop and ...;
broker22.CalNumOfRes1(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(1));
host.addmaop(userid, relinp.get(1));
//payprice = broker22.CalPrice(cloudlet22.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) == 2){
host.addmtimes(userid);
//will add codes for sop and aop and ...;
broker22.CalNumOfRes2(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(2));
host.addmaop(userid, relinp.get(2));
//payprice = broker22.CalPrice(cloudlet22.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) > 2){
broker22.CalNumOfRes(user.getuserId(), host.getId());	
int a = host.getmtimes().get(userid);
host.addmtimes(userid);
host.addmsop(userid, relinp.get(a));
host.addmaop(userid, relinp.get(a));

}
}



utimeh = host.getmtimes().get(userid) ;
System.out.println("this is the " + utimeh + "th time for user" + user.getuserId() + " to use services of host" + host.getId());
payprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()*0.001);
vmid = 22;

Vm vm22 = new Vm(vmid, broker22Id, mips, user.getnumCPU(), user.getnumRAM(), bw, user.getnumStorage(), vmm, new CloudletSchedulerTimeShared());

vm22.setHost(host);

//int vmid22 = 1;
//Vm vm22 = new Vm(vmid22, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
//add the VM to the vmList
vmlist.add(vm22);
//vmlist.add(vm22);

//submit vm list to the broker
broker22.submitVmList(vmlist);

broker22.bindCloudletToVm(cloudletid, vmid);

payprice = payprice * user.gettime();

aa = utimeh -1;
bb = relinp.get(aa);
System.out.println("the relinquish probability of the user for this time is " + bb);
//System.out.println(bb);
d = (int) (user.getnumCPU() * bb);
e = (int) (user.getnumRAM() * bb);
f = (long) (user.getnumStorage() * bb);
System.out.println("host " + host.getId() + " will give " + user.getnumCPU() + " CPUs and " +user.getnumRAM()+ " GB of RAM and " + user.getnumStorage() + " GB of storage to user" + userid + " to finish his cloudlet " + cloudlet1.getCloudletId());
System.out.println("the user will pay " + payprice + " dollars to the broker.");
reimbursement = payprice * bb;
System.out.println("then user" + user.getuserId() + " will relinquish " + d + " CPUs and " + e + " GB of RAM and " + f + " GB of storage");
System.out.println("the broker will reimburse" + reimbursement + " to the customer");
reimburse.add(reimbursement);
System.out.println(host.getmtimes().get(userid));
CloudSim.startSimulation();
///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////
CloudSim.init(num_user, calendar, trace_flag);

@SuppressWarnings("unused")
Datacenter datacenter23 = createDatacenter("Datacenter_1");

DatacenterBroker broker23 = createBroker();
int broker23Id = broker23.getId();

//update totalProfit;
totalProfit += payprice;

broker23.submitUserList(userlist);
broker23.BindUserToHost(user.getuserId(), host.getId());

host.setTotalProfit(totalProfit);

broker23.setHostList(hostlist1);

cloudletid = 23;
Cloudlet cloudlet23 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
cloudlet23.setbrokerId(broker23Id);
//update the price of cloudlet;
//price = 200;
//cloudlet23.setprice(price);
cloudlet23.setuser(user);
user.setCloudletId(cloudlet23.getCloudletId());

//add the cloudlet to the list
cloudletList.add(cloudlet23);

//submit cloudlet list to the broker
broker23.submitCloudletList(cloudletList);

//adrian

user.submitCloudlet(cloudlet23);
user.submitRequest(user.getCloudlet(), false, pesNumber, numram, (long)(fileSize/1000000000), numtime);


basicprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()/400);
pledge = basicprice * 1.05;
System.out.println("user" + user.getuserId() + " asks for " + user.getnumCPU() + " CPUs and " + user.getnumRAM() + " GB of RAM and " + user.getnumStorage() + " GB of storage to host" + host.getId());


if(cloudlet23.pledge == true){
//here I didn't consider giving back the extra money if the user will not relinquish any resource;
payprice = pledge;
}
else{
//host.minusmtimes(userid);
//here remember the BROKER is changing to BROKER23!!!!!!!!! and cloudlet is cloudlet23!!!!!!!!!
if (host.getmtimes().get(userid) == 0){
					host.addmtimes(userid);
					ArrayList<Double> aaa = new ArrayList<Double> ();
					ArrayList<Double> bbb = new ArrayList<Double> ();
					//here I will set some default values for aop and sop and ...;
					if(host.getmaop().get(userid) == null){
					broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3, 0.3);
					//right now we don't consider the pricing model;
					//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
					host.getmaop().put(userid, bbb);
					}
					else {
						broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3);
						//right now we don't consider the pricing model;
						//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
					}
					host.getmsop().put(userid, aaa);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
				}
else if(host.getmtimes().get(userid) == 1){
host.addmtimes(userid);
//will add codes for sop and aop and ...;
broker23.CalNumOfRes1(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(1));
host.addmaop(userid, relinp.get(1));
//payprice = broker23.CalPrice(cloudlet23.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) == 2){
host.addmtimes(userid);
//will add codes for sop and aop and ...;
broker23.CalNumOfRes2(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(2));
host.addmaop(userid, relinp.get(2));
//payprice = broker23.CalPrice(cloudlet23.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) > 2){
broker23.CalNumOfRes(user.getuserId(), host.getId());	
int a = host.getmtimes().get(userid);
host.addmtimes(userid);
host.addmsop(userid, relinp.get(a));
host.addmaop(userid, relinp.get(a));

}
}



utimeh = host.getmtimes().get(userid) ;
System.out.println("this is the " + utimeh + "th time for user" + user.getuserId() + " to use services of host" + host.getId());
payprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()*0.001);
vmid = 23;

Vm vm23 = new Vm(vmid, broker23Id, mips, user.getnumCPU(), user.getnumRAM(), bw, user.getnumStorage(), vmm, new CloudletSchedulerTimeShared());

vm23.setHost(host);

//int vmid23 = 1;
//Vm vm23 = new Vm(vmid23, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
//add the VM to the vmList
vmlist.add(vm23);
//vmlist.add(vm23);

//submit vm list to the broker
broker23.submitVmList(vmlist);

broker23.bindCloudletToVm(cloudletid, vmid);

payprice = payprice * user.gettime();

aa = utimeh -1;
bb = relinp.get(aa);
System.out.println("the relinquish probability of the user for this time is " + bb);
//System.out.println(bb);
d = (int) (user.getnumCPU() * bb);
e = (int) (user.getnumRAM() * bb);
f = (long) (user.getnumStorage() * bb);
System.out.println("host " + host.getId() + " will give " + user.getnumCPU() + " CPUs and " +user.getnumRAM()+ " GB of RAM and " + user.getnumStorage() + " GB of storage to user" + userid + " to finish his cloudlet " + cloudlet1.getCloudletId());
System.out.println("the user will pay " + payprice + " dollars to the broker.");
reimbursement = payprice * bb;
System.out.println("then user" + user.getuserId() + " will relinquish " + d + " CPUs and " + e + " GB of RAM and " + f + " GB of storage");
System.out.println("the broker will reimburse" + reimbursement + " to the customer");
reimburse.add(reimbursement);
System.out.println(host.getmtimes().get(userid));
CloudSim.startSimulation();
///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////
CloudSim.init(num_user, calendar, trace_flag);

@SuppressWarnings("unused")
Datacenter datacenter24 = createDatacenter("Datacenter_1");

DatacenterBroker broker24 = createBroker();
int broker24Id = broker24.getId();

//update totalProfit;
totalProfit += payprice;

broker24.submitUserList(userlist);
broker24.BindUserToHost(user.getuserId(), host.getId());

host.setTotalProfit(totalProfit);

broker24.setHostList(hostlist1);

cloudletid = 24;
Cloudlet cloudlet24 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
cloudlet24.setbrokerId(broker24Id);
//update the price of cloudlet;
//price = 200;
//cloudlet24.setprice(price);
cloudlet24.setuser(user);
user.setCloudletId(cloudlet24.getCloudletId());

//add the cloudlet to the list
cloudletList.add(cloudlet24);

//submit cloudlet list to the broker
broker24.submitCloudletList(cloudletList);

//adrian

user.submitCloudlet(cloudlet24);
user.submitRequest(user.getCloudlet(), false, pesNumber, numram, (long)(fileSize/1000000000), numtime);


basicprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()/400);
pledge = basicprice * 1.05;
System.out.println("user" + user.getuserId() + " asks for " + user.getnumCPU() + " CPUs and " + user.getnumRAM() + " GB of RAM and " + user.getnumStorage() + " GB of storage to host" + host.getId());


if(cloudlet24.pledge == true){
//here I didn't consider giving back the extra money if the user will not relinquish any resource;
payprice = pledge;
}
else{
//host.minusmtimes(userid);
//here remember the BROKER is changing to BROKER24!!!!!!!!! and cloudlet is cloudlet24!!!!!!!!!
if (host.getmtimes().get(userid) == 0){
					host.addmtimes(userid);
					ArrayList<Double> aaa = new ArrayList<Double> ();
					ArrayList<Double> bbb = new ArrayList<Double> ();
					//here I will set some default values for aop and sop and ...;
					if(host.getmaop().get(userid) == null){
					broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3, 0.3);
					//right now we don't consider the pricing model;
					//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
					host.getmaop().put(userid, bbb);
					}
					else {
						broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3);
						//right now we don't consider the pricing model;
						//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
					}
					host.getmsop().put(userid, aaa);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
				}
else if(host.getmtimes().get(userid) == 1){
host.addmtimes(userid);
//will add codes for sop and aop and ...;
broker24.CalNumOfRes1(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(1));
host.addmaop(userid, relinp.get(1));
//payprice = broker24.CalPrice(cloudlet24.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) == 2){
host.addmtimes(userid);
//will add codes for sop and aop and ...;
broker24.CalNumOfRes2(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(2));
host.addmaop(userid, relinp.get(2));
//payprice = broker24.CalPrice(cloudlet24.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) > 2){
broker24.CalNumOfRes(user.getuserId(), host.getId());	
int a = host.getmtimes().get(userid);
host.addmtimes(userid);
host.addmsop(userid, relinp.get(a));
host.addmaop(userid, relinp.get(a));

}
}



utimeh = host.getmtimes().get(userid) ;
System.out.println("this is the " + utimeh + "th time for user" + user.getuserId() + " to use services of host" + host.getId());
payprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()*0.001);
vmid = 24;

Vm vm24 = new Vm(vmid, broker24Id, mips, user.getnumCPU(), user.getnumRAM(), bw, user.getnumStorage(), vmm, new CloudletSchedulerTimeShared());

vm24.setHost(host);

//int vmid24 = 1;
//Vm vm24 = new Vm(vmid24, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
//add the VM to the vmList
vmlist.add(vm24);
//vmlist.add(vm24);

//submit vm list to the broker
broker24.submitVmList(vmlist);

broker24.bindCloudletToVm(cloudletid, vmid);

payprice = payprice * user.gettime();

aa = utimeh -1;
bb = relinp.get(aa);
System.out.println("the relinquish probability of the user for this time is " + bb);
//System.out.println(bb);
d = (int) (user.getnumCPU() * bb);
e = (int) (user.getnumRAM() * bb);
f = (long) (user.getnumStorage() * bb);
System.out.println("host " + host.getId() + " will give " + user.getnumCPU() + " CPUs and " +user.getnumRAM()+ " GB of RAM and " + user.getnumStorage() + " GB of storage to user" + userid + " to finish his cloudlet " + cloudlet1.getCloudletId());
System.out.println("the user will pay " + payprice + " dollars to the broker.");
reimbursement = payprice * bb;
System.out.println("then user" + user.getuserId() + " will relinquish " + d + " CPUs and " + e + " GB of RAM and " + f + " GB of storage");
System.out.println("the broker will reimburse" + reimbursement + " to the customer");
reimburse.add(reimbursement);
System.out.println(host.getmtimes().get(userid));
CloudSim.startSimulation();
///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////
CloudSim.init(num_user, calendar, trace_flag);

@SuppressWarnings("unused")
Datacenter datacenter25 = createDatacenter("Datacenter_1");

DatacenterBroker broker25 = createBroker();
int broker25Id = broker25.getId();

//update totalProfit;
totalProfit += payprice;

broker25.submitUserList(userlist);
broker25.BindUserToHost(user.getuserId(), host.getId());

host.setTotalProfit(totalProfit);

broker25.setHostList(hostlist1);

cloudletid = 25;
Cloudlet cloudlet25 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
cloudlet25.setbrokerId(broker25Id);
//update the price of cloudlet;
//price = 200;
//cloudlet25.setprice(price);
cloudlet25.setuser(user);
user.setCloudletId(cloudlet25.getCloudletId());

//add the cloudlet to the list
cloudletList.add(cloudlet25);

//submit cloudlet list to the broker
broker25.submitCloudletList(cloudletList);

//adrian

user.submitCloudlet(cloudlet25);
user.submitRequest(user.getCloudlet(), false, pesNumber, numram, (long)(fileSize/1000000000), numtime);


basicprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()/400);
pledge = basicprice * 1.05;
System.out.println("user" + user.getuserId() + " asks for " + user.getnumCPU() + " CPUs and " + user.getnumRAM() + " GB of RAM and " + user.getnumStorage() + " GB of storage to host" + host.getId());


if(cloudlet25.pledge == true){
//here I didn't consider giving back the extra money if the user will not relinquish any resource;
payprice = pledge;
}
else{
//host.minusmtimes(userid);
//here remember the BROKER is changing to BROKER25!!!!!!!!! and cloudlet is cloudlet25!!!!!!!!!
if (host.getmtimes().get(userid) == 0){
					host.addmtimes(userid);
					ArrayList<Double> aaa = new ArrayList<Double> ();
					ArrayList<Double> bbb = new ArrayList<Double> ();
					//here I will set some default values for aop and sop and ...;
					if(host.getmaop().get(userid) == null){
					broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3, 0.3);
					//right now we don't consider the pricing model;
					//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
					host.getmaop().put(userid, bbb);
					}
					else {
						broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3);
						//right now we don't consider the pricing model;
						//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
					}
					host.getmsop().put(userid, aaa);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
				}
else if(host.getmtimes().get(userid) == 1){
host.addmtimes(userid);
//will add codes for sop and aop and ...;
broker25.CalNumOfRes1(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(1));
host.addmaop(userid, relinp.get(1));
//payprice = broker25.CalPrice(cloudlet25.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) == 2){
host.addmtimes(userid);
//will add codes for sop and aop and ...;
broker25.CalNumOfRes2(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(2));
host.addmaop(userid, relinp.get(2));
//payprice = broker25.CalPrice(cloudlet25.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) > 2){
broker25.CalNumOfRes(user.getuserId(), host.getId());	
int a = host.getmtimes().get(userid);
host.addmtimes(userid);
host.addmsop(userid, relinp.get(a));
host.addmaop(userid, relinp.get(a));

}
}



utimeh = host.getmtimes().get(userid) ;
System.out.println("this is the " + utimeh + "th time for user" + user.getuserId() + " to use services of host" + host.getId());
payprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()*0.001);
vmid = 25;

Vm vm25 = new Vm(vmid, broker25Id, mips, user.getnumCPU(), user.getnumRAM(), bw, user.getnumStorage(), vmm, new CloudletSchedulerTimeShared());

vm25.setHost(host);

//int vmid25 = 1;
//Vm vm25 = new Vm(vmid25, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
//add the VM to the vmList
vmlist.add(vm25);
//vmlist.add(vm25);

//submit vm list to the broker
broker25.submitVmList(vmlist);

broker25.bindCloudletToVm(cloudletid, vmid);

payprice = payprice * user.gettime();

aa = utimeh -1;
bb = relinp.get(aa);
System.out.println("the relinquish probability of the user for this time is " + bb);
//System.out.println(bb);
d = (int) (user.getnumCPU() * bb);
e = (int) (user.getnumRAM() * bb);
f = (long) (user.getnumStorage() * bb);
System.out.println("host " + host.getId() + " will give " + user.getnumCPU() + " CPUs and " +user.getnumRAM()+ " GB of RAM and " + user.getnumStorage() + " GB of storage to user" + userid + " to finish his cloudlet " + cloudlet1.getCloudletId());
System.out.println("the user will pay " + payprice + " dollars to the broker.");
reimbursement = payprice * bb;
System.out.println("then user" + user.getuserId() + " will relinquish " + d + " CPUs and " + e + " GB of RAM and " + f + " GB of storage");
System.out.println("the broker will reimburse" + reimbursement + " to the customer");
reimburse.add(reimbursement);
System.out.println(host.getmtimes().get(userid));
CloudSim.startSimulation();
///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////

CloudSim.init(num_user, calendar, trace_flag);

@SuppressWarnings("unused")
Datacenter datacenter26 = createDatacenter("Datacenter_1");

DatacenterBroker broker26 = createBroker();
int broker26Id = broker26.getId();

//update totalProfit;
totalProfit += payprice;

broker26.submitUserList(userlist);
broker26.BindUserToHost(user.getuserId(), host.getId());

host.setTotalProfit(totalProfit);

broker26.setHostList(hostlist1);

cloudletid = 26;
Cloudlet cloudlet26 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
cloudlet26.setbrokerId(broker26Id);
//update the price of cloudlet;
//price = 200;
//cloudlet26.setprice(price);
cloudlet26.setuser(user);
user.setCloudletId(cloudlet26.getCloudletId());

//add the cloudlet to the list
cloudletList.add(cloudlet26);

//submit cloudlet list to the broker
broker26.submitCloudletList(cloudletList);

//adrian

user.submitCloudlet(cloudlet26);
user.submitRequest(user.getCloudlet(), false, pesNumber, numram, (long)(fileSize/1000000000), numtime);


basicprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()/400);
pledge = basicprice * 1.05;
System.out.println("user" + user.getuserId() + " asks for " + user.getnumCPU() + " CPUs and " + user.getnumRAM() + " GB of RAM and " + user.getnumStorage() + " GB of storage to host" + host.getId());


if(cloudlet26.pledge == true){
//here I didn't consider giving back the extra money if the user will not relinquish any resource;
payprice = pledge;
}
else{
//host.minusmtimes(userid);
//here remember the BROKER is changing to BROKER26!!!!!!!!! and cloudlet is cloudlet26!!!!!!!!!
if (host.getmtimes().get(userid) == 0){
					host.addmtimes(userid);
					ArrayList<Double> aaa = new ArrayList<Double> ();
					ArrayList<Double> bbb = new ArrayList<Double> ();
					//here I will set some default values for aop and sop and ...;
					if(host.getmaop().get(userid) == null){
					broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3, 0.3);
					//right now we don't consider the pricing model;
					//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
					host.getmaop().put(userid, bbb);
					}
					else {
						broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3);
						//right now we don't consider the pricing model;
						//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
					}
					host.getmsop().put(userid, aaa);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
				}
else if(host.getmtimes().get(userid) == 1){
host.addmtimes(userid);
//will add codes for sop and aop and ...;
broker26.CalNumOfRes1(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(1));
host.addmaop(userid, relinp.get(1));
//payprice = broker26.CalPrice(cloudlet26.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) == 2){
host.addmtimes(userid);
//will add codes for sop and aop and ...;
broker26.CalNumOfRes2(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(2));
host.addmaop(userid, relinp.get(2));
//payprice = broker26.CalPrice(cloudlet26.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) > 2){
broker26.CalNumOfRes(user.getuserId(), host.getId());	
int a = host.getmtimes().get(userid);
host.addmtimes(userid);
host.addmsop(userid, relinp.get(a));
host.addmaop(userid, relinp.get(a));

}
}



utimeh = host.getmtimes().get(userid) ;
System.out.println("this is the " + utimeh + "th time for user" + user.getuserId() + " to use services of host" + host.getId());
payprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()*0.001);
vmid = 26;

Vm vm26 = new Vm(vmid, broker26Id, mips, user.getnumCPU(), user.getnumRAM(), bw, user.getnumStorage(), vmm, new CloudletSchedulerTimeShared());

vm26.setHost(host);

//int vmid26 = 1;
//Vm vm26 = new Vm(vmid26, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
//add the VM to the vmList
vmlist.add(vm26);
//vmlist.add(vm26);

//submit vm list to the broker
broker26.submitVmList(vmlist);

broker26.bindCloudletToVm(cloudletid, vmid);

payprice = payprice * user.gettime();

aa = utimeh -1;
bb = relinp.get(aa);
System.out.println("the relinquish probability of the user for this time is " + bb);
//System.out.println(bb);
d = (int) (user.getnumCPU() * bb);
e = (int) (user.getnumRAM() * bb);
f = (long) (user.getnumStorage() * bb);
System.out.println("host " + host.getId() + " will give " + user.getnumCPU() + " CPUs and " +user.getnumRAM()+ " GB of RAM and " + user.getnumStorage() + " GB of storage to user" + userid + " to finish his cloudlet " + cloudlet1.getCloudletId());
System.out.println("the user will pay " + payprice + " dollars to the broker.");
reimbursement = payprice * bb;
System.out.println("then user" + user.getuserId() + " will relinquish " + d + " CPUs and " + e + " GB of RAM and " + f + " GB of storage");
System.out.println("the broker will reimburse" + reimbursement + " to the customer");
reimburse.add(reimbursement);
System.out.println(host.getmtimes().get(userid));
CloudSim.startSimulation();
///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////
CloudSim.init(num_user, calendar, trace_flag);

@SuppressWarnings("unused")
Datacenter datacenter27 = createDatacenter("Datacenter_1");

DatacenterBroker broker27 = createBroker();
int broker27Id = broker27.getId();

//update totalProfit;
totalProfit += payprice;

broker27.submitUserList(userlist);
broker27.BindUserToHost(user.getuserId(), host.getId());

host.setTotalProfit(totalProfit);

broker27.setHostList(hostlist1);

cloudletid = 27;
Cloudlet cloudlet27 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
cloudlet27.setbrokerId(broker27Id);
//update the price of cloudlet;
//price = 200;
//cloudlet27.setprice(price);
cloudlet27.setuser(user);
user.setCloudletId(cloudlet27.getCloudletId());

//add the cloudlet to the list
cloudletList.add(cloudlet27);

//submit cloudlet list to the broker
broker27.submitCloudletList(cloudletList);

//adrian

user.submitCloudlet(cloudlet27);
user.submitRequest(user.getCloudlet(), false, pesNumber, numram, (long)(fileSize/1000000000), numtime);


basicprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()/400);
pledge = basicprice * 1.05;
System.out.println("user" + user.getuserId() + " asks for " + user.getnumCPU() + " CPUs and " + user.getnumRAM() + " GB of RAM and " + user.getnumStorage() + " GB of storage to host" + host.getId());


if(cloudlet27.pledge == true){
//here I didn't consider giving back the extra money if the user will not relinquish any resource;
payprice = pledge;
}
else{
//host.minusmtimes(userid);
//here remember the BROKER is changing to BROKER27!!!!!!!!! and cloudlet is cloudlet27!!!!!!!!!
if (host.getmtimes().get(userid) == 0){
					host.addmtimes(userid);
					ArrayList<Double> aaa = new ArrayList<Double> ();
					ArrayList<Double> bbb = new ArrayList<Double> ();
					//here I will set some default values for aop and sop and ...;
					if(host.getmaop().get(userid) == null){
					broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3, 0.3);
					//right now we don't consider the pricing model;
					//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
					host.getmaop().put(userid, bbb);
					}
					else {
						broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3);
						//right now we don't consider the pricing model;
						//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
					}
					host.getmsop().put(userid, aaa);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
				}
else if(host.getmtimes().get(userid) == 1){
host.addmtimes(userid);
//will add codes for sop and aop and ...;
broker27.CalNumOfRes1(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(1));
host.addmaop(userid, relinp.get(1));
//payprice = broker27.CalPrice(cloudlet27.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) == 2){
host.addmtimes(userid);
//will add codes for sop and aop and ...;
broker27.CalNumOfRes2(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(2));
host.addmaop(userid, relinp.get(2));
//payprice = broker27.CalPrice(cloudlet27.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) > 2){
broker27.CalNumOfRes(user.getuserId(), host.getId());	
int a = host.getmtimes().get(userid);
host.addmtimes(userid);
host.addmsop(userid, relinp.get(a));
host.addmaop(userid, relinp.get(a));

}
}



utimeh = host.getmtimes().get(userid) ;
System.out.println("this is the " + utimeh + "th time for user" + user.getuserId() + " to use services of host" + host.getId());
payprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()*0.001);
vmid = 27;

Vm vm27 = new Vm(vmid, broker27Id, mips, user.getnumCPU(), user.getnumRAM(), bw, user.getnumStorage(), vmm, new CloudletSchedulerTimeShared());

vm27.setHost(host);

//int vmid27 = 1;
//Vm vm27 = new Vm(vmid27, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
//add the VM to the vmList
vmlist.add(vm27);
//vmlist.add(vm27);

//submit vm list to the broker
broker27.submitVmList(vmlist);

broker27.bindCloudletToVm(cloudletid, vmid);

payprice = payprice * user.gettime();

aa = utimeh -1;
bb = relinp.get(aa);
System.out.println("the relinquish probability of the user for this time is " + bb);
//System.out.println(bb);
d = (int) (user.getnumCPU() * bb);
e = (int) (user.getnumRAM() * bb);
f = (long) (user.getnumStorage() * bb);
System.out.println("host " + host.getId() + " will give " + user.getnumCPU() + " CPUs and " +user.getnumRAM()+ " GB of RAM and " + user.getnumStorage() + " GB of storage to user" + userid + " to finish his cloudlet " + cloudlet1.getCloudletId());
System.out.println("the user will pay " + payprice + " dollars to the broker.");
reimbursement = payprice * bb;
System.out.println("then user" + user.getuserId() + " will relinquish " + d + " CPUs and " + e + " GB of RAM and " + f + " GB of storage");
System.out.println("the broker will reimburse" + reimbursement + " to the customer");
reimburse.add(reimbursement);
System.out.println(host.getmtimes().get(userid));
CloudSim.startSimulation();
///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////
CloudSim.init(num_user, calendar, trace_flag);

@SuppressWarnings("unused")
Datacenter datacenter28 = createDatacenter("Datacenter_1");

DatacenterBroker broker28 = createBroker();
int broker28Id = broker28.getId();

//update totalProfit;
totalProfit += payprice;

broker28.submitUserList(userlist);
broker28.BindUserToHost(user.getuserId(), host.getId());

host.setTotalProfit(totalProfit);

broker28.setHostList(hostlist1);

cloudletid = 28;
Cloudlet cloudlet28 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
cloudlet28.setbrokerId(broker28Id);
//update the price of cloudlet;
//price = 200;
//cloudlet28.setprice(price);
cloudlet28.setuser(user);
user.setCloudletId(cloudlet28.getCloudletId());

//add the cloudlet to the list
cloudletList.add(cloudlet28);

//submit cloudlet list to the broker
broker28.submitCloudletList(cloudletList);

//adrian

user.submitCloudlet(cloudlet28);
user.submitRequest(user.getCloudlet(), false, pesNumber, numram, (long)(fileSize/1000000000), numtime);


basicprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()/400);
pledge = basicprice * 1.05;
System.out.println("user" + user.getuserId() + " asks for " + user.getnumCPU() + " CPUs and " + user.getnumRAM() + " GB of RAM and " + user.getnumStorage() + " GB of storage to host" + host.getId());


if(cloudlet28.pledge == true){
//here I didn't consider giving back the extra money if the user will not relinquish any resource;
payprice = pledge;
}
else{
//host.minusmtimes(userid);
//here remember the BROKER is changing to BROKER28!!!!!!!!! and cloudlet is cloudlet28!!!!!!!!!
if (host.getmtimes().get(userid) == 0){
					host.addmtimes(userid);
					ArrayList<Double> aaa = new ArrayList<Double> ();
					ArrayList<Double> bbb = new ArrayList<Double> ();
					//here I will set some default values for aop and sop and ...;
					if(host.getmaop().get(userid) == null){
					broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3, 0.3);
					//right now we don't consider the pricing model;
					//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
					host.getmaop().put(userid, bbb);
					}
					else {
						broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3);
						//right now we don't consider the pricing model;
						//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
					}
					host.getmsop().put(userid, aaa);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
				}
else if(host.getmtimes().get(userid) == 1){
host.addmtimes(userid);
//will add codes for sop and aop and ...;
broker28.CalNumOfRes1(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(1));
host.addmaop(userid, relinp.get(1));
//payprice = broker28.CalPrice(cloudlet28.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) == 2){
host.addmtimes(userid);
//will add codes for sop and aop and ...;
broker28.CalNumOfRes2(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(2));
host.addmaop(userid, relinp.get(2));
//payprice = broker28.CalPrice(cloudlet28.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) > 2){
broker28.CalNumOfRes(user.getuserId(), host.getId());	
int a = host.getmtimes().get(userid);
host.addmtimes(userid);
host.addmsop(userid, relinp.get(a));
host.addmaop(userid, relinp.get(a));

}
}



utimeh = host.getmtimes().get(userid) ;
System.out.println("this is the " + utimeh + "th time for user" + user.getuserId() + " to use services of host" + host.getId());
payprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()*0.001);
vmid = 28;

Vm vm28 = new Vm(vmid, broker28Id, mips, user.getnumCPU(), user.getnumRAM(), bw, user.getnumStorage(), vmm, new CloudletSchedulerTimeShared());

vm28.setHost(host);

//int vmid28 = 1;
//Vm vm28 = new Vm(vmid28, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
//add the VM to the vmList
vmlist.add(vm28);
//vmlist.add(vm28);

//submit vm list to the broker
broker28.submitVmList(vmlist);

broker28.bindCloudletToVm(cloudletid, vmid);

payprice = payprice * user.gettime();

aa = utimeh -1;
bb = relinp.get(aa);
System.out.println("the relinquish probability of the user for this time is " + bb);
//System.out.println(bb);
d = (int) (user.getnumCPU() * bb);
e = (int) (user.getnumRAM() * bb);
f = (long) (user.getnumStorage() * bb);
System.out.println("host " + host.getId() + " will give " + user.getnumCPU() + " CPUs and " +user.getnumRAM()+ " GB of RAM and " + user.getnumStorage() + " GB of storage to user" + userid + " to finish his cloudlet " + cloudlet1.getCloudletId());
System.out.println("the user will pay " + payprice + " dollars to the broker.");
reimbursement = payprice * bb;
System.out.println("then user" + user.getuserId() + " will relinquish " + d + " CPUs and " + e + " GB of RAM and " + f + " GB of storage");
System.out.println("the broker will reimburse" + reimbursement + " to the customer");
reimburse.add(reimbursement);
System.out.println(host.getmtimes().get(userid));
CloudSim.startSimulation();
///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////
CloudSim.init(num_user, calendar, trace_flag);

@SuppressWarnings("unused")
Datacenter datacenter29 = createDatacenter("Datacenter_1");

DatacenterBroker broker29 = createBroker();
int broker29Id = broker29.getId();

//update totalProfit;
totalProfit += payprice;

broker29.submitUserList(userlist);
broker29.BindUserToHost(user.getuserId(), host.getId());

host.setTotalProfit(totalProfit);

broker29.setHostList(hostlist1);

cloudletid = 29;
Cloudlet cloudlet29 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
cloudlet29.setbrokerId(broker29Id);
//update the price of cloudlet;
//price = 200;
//cloudlet29.setprice(price);
cloudlet29.setuser(user);
user.setCloudletId(cloudlet29.getCloudletId());

//add the cloudlet to the list
cloudletList.add(cloudlet29);

//submit cloudlet list to the broker
broker29.submitCloudletList(cloudletList);

//adrian

user.submitCloudlet(cloudlet29);
user.submitRequest(user.getCloudlet(), false, pesNumber, numram, (long)(fileSize/1000000000), numtime);


basicprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()/400);
pledge = basicprice * 1.05;
System.out.println("user" + user.getuserId() + " asks for " + user.getnumCPU() + " CPUs and " + user.getnumRAM() + " GB of RAM and " + user.getnumStorage() + " GB of storage to host" + host.getId());


if(cloudlet29.pledge == true){
//here I didn't consider giving back the extra money if the user will not relinquish any resource;
payprice = pledge;
}
else{
//host.minusmtimes(userid);
//here remember the BROKER is changing to BROKER29!!!!!!!!! and cloudlet is cloudlet29!!!!!!!!!
if (host.getmtimes().get(userid) == 0){
					host.addmtimes(userid);
					ArrayList<Double> aaa = new ArrayList<Double> ();
					ArrayList<Double> bbb = new ArrayList<Double> ();
					//here I will set some default values for aop and sop and ...;
					if(host.getmaop().get(userid) == null){
					broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3, 0.3);
					//right now we don't consider the pricing model;
					//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
					host.getmaop().put(userid, bbb);
					}
					else {
						broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3);
						//right now we don't consider the pricing model;
						//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
					}
					host.getmsop().put(userid, aaa);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
				}
else if(host.getmtimes().get(userid) == 1){
host.addmtimes(userid);
//will add codes for sop and aop and ...;
broker29.CalNumOfRes1(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(1));
host.addmaop(userid, relinp.get(1));
//payprice = broker29.CalPrice(cloudlet29.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) == 2){
host.addmtimes(userid);
//will add codes for sop and aop and ...;
broker29.CalNumOfRes2(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(2));
host.addmaop(userid, relinp.get(2));
//payprice = broker29.CalPrice(cloudlet29.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) > 2){
broker29.CalNumOfRes(user.getuserId(), host.getId());	
int a = host.getmtimes().get(userid);
host.addmtimes(userid);
host.addmsop(userid, relinp.get(a));
host.addmaop(userid, relinp.get(a));

}
}



utimeh = host.getmtimes().get(userid) ;
System.out.println("this is the " + utimeh + "th time for user" + user.getuserId() + " to use services of host" + host.getId());
payprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()*0.001);
vmid = 29;

Vm vm29 = new Vm(vmid, broker29Id, mips, user.getnumCPU(), user.getnumRAM(), bw, user.getnumStorage(), vmm, new CloudletSchedulerTimeShared());

vm29.setHost(host);

//int vmid29 = 1;
//Vm vm29 = new Vm(vmid29, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
//add the VM to the vmList
vmlist.add(vm29);
//vmlist.add(vm29);

//submit vm list to the broker
broker29.submitVmList(vmlist);

broker29.bindCloudletToVm(cloudletid, vmid);

payprice = payprice * user.gettime();

aa = utimeh -1;
bb = relinp.get(aa);
System.out.println("the relinquish probability of the user for this time is " + bb);
//System.out.println(bb);
d = (int) (user.getnumCPU() * bb);
e = (int) (user.getnumRAM() * bb);
f = (long) (user.getnumStorage() * bb);
System.out.println("host " + host.getId() + " will give " + user.getnumCPU() + " CPUs and " +user.getnumRAM()+ " GB of RAM and " + user.getnumStorage() + " GB of storage to user" + userid + " to finish his cloudlet " + cloudlet1.getCloudletId());
System.out.println("the user will pay " + payprice + " dollars to the broker.");
reimbursement = payprice * bb;
System.out.println("then user" + user.getuserId() + " will relinquish " + d + " CPUs and " + e + " GB of RAM and " + f + " GB of storage");
System.out.println("the broker will reimburse" + reimbursement + " to the customer");
reimburse.add(reimbursement);
System.out.println(host.getmtimes().get(userid));
CloudSim.startSimulation();
///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////

CloudSim.init(num_user, calendar, trace_flag);

@SuppressWarnings("unused")
Datacenter datacenter30 = createDatacenter("Datacenter_1");

DatacenterBroker broker30 = createBroker();
int broker30Id = broker30.getId();

//update totalProfit;
totalProfit += payprice;

broker30.submitUserList(userlist);
broker30.BindUserToHost(user.getuserId(), host.getId());

host.setTotalProfit(totalProfit);

broker30.setHostList(hostlist1);

cloudletid = 30;
Cloudlet cloudlet30 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
cloudlet30.setbrokerId(broker30Id);
//update the price of cloudlet;
//price = 200;
//cloudlet30.setprice(price);
cloudlet30.setuser(user);
user.setCloudletId(cloudlet30.getCloudletId());

//add the cloudlet to the list
cloudletList.add(cloudlet30);

//submit cloudlet list to the broker
broker30.submitCloudletList(cloudletList);

//adrian

user.submitCloudlet(cloudlet30);
user.submitRequest(user.getCloudlet(), false, pesNumber, numram, (long)(fileSize/1000000000), numtime);


basicprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()/400);
pledge = basicprice * 1.05;
System.out.println("user" + user.getuserId() + " asks for " + user.getnumCPU() + " CPUs and " + user.getnumRAM() + " GB of RAM and " + user.getnumStorage() + " GB of storage to host" + host.getId());


if(cloudlet30.pledge == true){
//here I didn't consider giving back the extra money if the user will not relinquish any resource;
payprice = pledge;
}
else{
//host.minusmtimes(userid);
//here remember the BROKER is changing to BROKER30!!!!!!!!! and cloudlet is cloudlet30!!!!!!!!!
if (host.getmtimes().get(userid) == 0){
					host.addmtimes(userid);
					ArrayList<Double> aaa = new ArrayList<Double> ();
					ArrayList<Double> bbb = new ArrayList<Double> ();
					//here I will set some default values for aop and sop and ...;
					if(host.getmaop().get(userid) == null){
					broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3, 0.3);
					//right now we don't consider the pricing model;
					//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
					host.getmaop().put(userid, bbb);
					}
					else {
						broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3);
						//right now we don't consider the pricing model;
						//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
					}
					host.getmsop().put(userid, aaa);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
				}
else if(host.getmtimes().get(userid) == 1){
host.addmtimes(userid);
//will add codes for sop and aop and ...;
broker30.CalNumOfRes1(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(1));
host.addmaop(userid, relinp.get(1));
//payprice = broker30.CalPrice(cloudlet30.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) == 2){
host.addmtimes(userid);
//will add codes for sop and aop and ...;
broker30.CalNumOfRes2(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(2));
host.addmaop(userid, relinp.get(2));
//payprice = broker30.CalPrice(cloudlet30.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) > 2){
broker30.CalNumOfRes(user.getuserId(), host.getId());	
int a = host.getmtimes().get(userid);
host.addmtimes(userid);
host.addmsop(userid, relinp.get(a));
host.addmaop(userid, relinp.get(a));

}
}



utimeh = host.getmtimes().get(userid) ;
System.out.println("this is the " + utimeh + "th time for user" + user.getuserId() + " to use services of host" + host.getId());
payprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()*0.001);
vmid = 30;

Vm vm30 = new Vm(vmid, broker30Id, mips, user.getnumCPU(), user.getnumRAM(), bw, user.getnumStorage(), vmm, new CloudletSchedulerTimeShared());

vm30.setHost(host);

//int vmid30 = 1;
//Vm vm30 = new Vm(vmid30, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
//add the VM to the vmList
vmlist.add(vm30);
//vmlist.add(vm30);

//submit vm list to the broker
broker30.submitVmList(vmlist);

broker30.bindCloudletToVm(cloudletid, vmid);

payprice = payprice * user.gettime();

aa = utimeh -1;
bb = relinp.get(aa);
System.out.println("the relinquish probability of the user for this time is " + bb);
//System.out.println(bb);
d = (int) (user.getnumCPU() * bb);
e = (int) (user.getnumRAM() * bb);
f = (long) (user.getnumStorage() * bb);
System.out.println("host " + host.getId() + " will give " + user.getnumCPU() + " CPUs and " +user.getnumRAM()+ " GB of RAM and " + user.getnumStorage() + " GB of storage to user" + userid + " to finish his cloudlet " + cloudlet1.getCloudletId());
System.out.println("the user will pay " + payprice + " dollars to the broker.");
reimbursement = payprice * bb;
System.out.println("then user" + user.getuserId() + " will relinquish " + d + " CPUs and " + e + " GB of RAM and " + f + " GB of storage");
System.out.println("the broker will reimburse" + reimbursement + " to the customer");
reimburse.add(reimbursement);
System.out.println(host.getmtimes().get(userid));
CloudSim.startSimulation();
///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////
CloudSim.init(num_user, calendar, trace_flag);

@SuppressWarnings("unused")
Datacenter datacenter31 = createDatacenter("Datacenter_1");

DatacenterBroker broker31 = createBroker();
int broker31Id = broker31.getId();

//update totalProfit;
totalProfit += payprice;

broker31.submitUserList(userlist);
broker31.BindUserToHost(user.getuserId(), host.getId());

host.setTotalProfit(totalProfit);

broker31.setHostList(hostlist1);

cloudletid = 31;
Cloudlet cloudlet31 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
cloudlet31.setbrokerId(broker31Id);
//update the price of cloudlet;
//price = 200;
//cloudlet31.setprice(price);
cloudlet31.setuser(user);
user.setCloudletId(cloudlet31.getCloudletId());

//add the cloudlet to the list
cloudletList.add(cloudlet31);

//submit cloudlet list to the broker
broker31.submitCloudletList(cloudletList);

//adrian

user.submitCloudlet(cloudlet31);
user.submitRequest(user.getCloudlet(), false, pesNumber, numram, (long)(fileSize/1000000000), numtime);


basicprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()/400);
pledge = basicprice * 1.05;
System.out.println("user" + user.getuserId() + " asks for " + user.getnumCPU() + " CPUs and " + user.getnumRAM() + " GB of RAM and " + user.getnumStorage() + " GB of storage to host" + host.getId());


if(cloudlet31.pledge == true){
//here I didn't consider giving back the extra money if the user will not relinquish any resource;
payprice = pledge;
}
else{
//host.minusmtimes(userid);
//here remember the BROKER is changing to BROKER31!!!!!!!!! and cloudlet is cloudlet31!!!!!!!!!
if (host.getmtimes().get(userid) == 0){
					host.addmtimes(userid);
					ArrayList<Double> aaa = new ArrayList<Double> ();
					ArrayList<Double> bbb = new ArrayList<Double> ();
					//here I will set some default values for aop and sop and ...;
					if(host.getmaop().get(userid) == null){
					broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3, 0.3);
					//right now we don't consider the pricing model;
					//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
					host.getmaop().put(userid, bbb);
					}
					else {
						broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3);
						//right now we don't consider the pricing model;
						//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
					}
					host.getmsop().put(userid, aaa);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
				}
else if(host.getmtimes().get(userid) == 1){
host.addmtimes(userid);
//will add codes for sop and aop and ...;
broker31.CalNumOfRes1(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(1));
host.addmaop(userid, relinp.get(1));
//payprice = broker31.CalPrice(cloudlet31.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) == 2){
host.addmtimes(userid);
//will add codes for sop and aop and ...;
broker31.CalNumOfRes2(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(2));
host.addmaop(userid, relinp.get(2));
//payprice = broker31.CalPrice(cloudlet31.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) > 2){
broker31.CalNumOfRes(user.getuserId(), host.getId());	
int a = host.getmtimes().get(userid);
host.addmtimes(userid);
host.addmsop(userid, relinp.get(a));
host.addmaop(userid, relinp.get(a));

}
}



utimeh = host.getmtimes().get(userid) ;
System.out.println("this is the " + utimeh + "th time for user" + user.getuserId() + " to use services of host" + host.getId());
payprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()*0.001);
vmid = 31;

Vm vm31 = new Vm(vmid, broker31Id, mips, user.getnumCPU(), user.getnumRAM(), bw, user.getnumStorage(), vmm, new CloudletSchedulerTimeShared());

vm31.setHost(host);

//int vmid31 = 1;
//Vm vm31 = new Vm(vmid31, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
//add the VM to the vmList
vmlist.add(vm31);
//vmlist.add(vm31);

//submit vm list to the broker
broker31.submitVmList(vmlist);

broker31.bindCloudletToVm(cloudletid, vmid);

payprice = payprice * user.gettime();

aa = utimeh -1;
bb = relinp.get(aa);
System.out.println("the relinquish probability of the user for this time is " + bb);
//System.out.println(bb);
d = (int) (user.getnumCPU() * bb);
e = (int) (user.getnumRAM() * bb);
f = (long) (user.getnumStorage() * bb);
System.out.println("host " + host.getId() + " will give " + user.getnumCPU() + " CPUs and " +user.getnumRAM()+ " GB of RAM and " + user.getnumStorage() + " GB of storage to user" + userid + " to finish his cloudlet " + cloudlet1.getCloudletId());
System.out.println("the user will pay " + payprice + " dollars to the broker.");
reimbursement = payprice * bb;
System.out.println("then user" + user.getuserId() + " will relinquish " + d + " CPUs and " + e + " GB of RAM and " + f + " GB of storage");
System.out.println("the broker will reimburse" + reimbursement + " to the customer");
reimburse.add(reimbursement);
System.out.println(host.getmtimes().get(userid));
CloudSim.startSimulation();
///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////
CloudSim.init(num_user, calendar, trace_flag);

@SuppressWarnings("unused")
Datacenter datacenter32 = createDatacenter("Datacenter_1");

DatacenterBroker broker32 = createBroker();
int broker32Id = broker32.getId();

//update totalProfit;
totalProfit += payprice;

broker32.submitUserList(userlist);
broker32.BindUserToHost(user.getuserId(), host.getId());

host.setTotalProfit(totalProfit);

broker32.setHostList(hostlist1);

cloudletid = 32;
Cloudlet cloudlet32 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
cloudlet32.setbrokerId(broker32Id);
//update the price of cloudlet;
//price = 200;
//cloudlet32.setprice(price);
cloudlet32.setuser(user);
user.setCloudletId(cloudlet32.getCloudletId());

//add the cloudlet to the list
cloudletList.add(cloudlet32);

//submit cloudlet list to the broker
broker32.submitCloudletList(cloudletList);

//adrian

user.submitCloudlet(cloudlet32);
user.submitRequest(user.getCloudlet(), false, pesNumber, numram, (long)(fileSize/1000000000), numtime);


basicprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()/400);
pledge = basicprice * 1.05;
System.out.println("user" + user.getuserId() + " asks for " + user.getnumCPU() + " CPUs and " + user.getnumRAM() + " GB of RAM and " + user.getnumStorage() + " GB of storage to host" + host.getId());


if(cloudlet32.pledge == true){
//here I didn't consider giving back the extra money if the user will not relinquish any resource;
payprice = pledge;
}
else{
//host.minusmtimes(userid);
//here remember the BROKER is changing to BROKER32!!!!!!!!! and cloudlet is cloudlet32!!!!!!!!!
if (host.getmtimes().get(userid) == 0){
					host.addmtimes(userid);
					ArrayList<Double> aaa = new ArrayList<Double> ();
					ArrayList<Double> bbb = new ArrayList<Double> ();
					//here I will set some default values for aop and sop and ...;
					if(host.getmaop().get(userid) == null){
					broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3, 0.3);
					//right now we don't consider the pricing model;
					//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
					host.getmaop().put(userid, bbb);
					}
					else {
						broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3);
						//right now we don't consider the pricing model;
						//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
					}
					host.getmsop().put(userid, aaa);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
				}
else if(host.getmtimes().get(userid) == 1){
host.addmtimes(userid);
//will add codes for sop and aop and ...;
broker32.CalNumOfRes1(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(1));
host.addmaop(userid, relinp.get(1));
//payprice = broker32.CalPrice(cloudlet32.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) == 2){
host.addmtimes(userid);
//will add codes for sop and aop and ...;
broker32.CalNumOfRes2(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(2));
host.addmaop(userid, relinp.get(2));
//payprice = broker32.CalPrice(cloudlet32.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) > 2){
broker32.CalNumOfRes(user.getuserId(), host.getId());	
int a = host.getmtimes().get(userid);
host.addmtimes(userid);
host.addmsop(userid, relinp.get(a));
host.addmaop(userid, relinp.get(a));

}
}



utimeh = host.getmtimes().get(userid) ;
System.out.println("this is the " + utimeh + "th time for user" + user.getuserId() + " to use services of host" + host.getId());
payprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()*0.001);
vmid = 32;

Vm vm32 = new Vm(vmid, broker32Id, mips, user.getnumCPU(), user.getnumRAM(), bw, user.getnumStorage(), vmm, new CloudletSchedulerTimeShared());

vm32.setHost(host);

//int vmid32 = 1;
//Vm vm32 = new Vm(vmid32, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
//add the VM to the vmList
vmlist.add(vm32);
//vmlist.add(vm32);

//submit vm list to the broker
broker32.submitVmList(vmlist);

broker32.bindCloudletToVm(cloudletid, vmid);

payprice = payprice * user.gettime();

aa = utimeh -1;
bb = relinp.get(aa);
System.out.println("the relinquish probability of the user for this time is " + bb);
//System.out.println(bb);
d = (int) (user.getnumCPU() * bb);
e = (int) (user.getnumRAM() * bb);
f = (long) (user.getnumStorage() * bb);
System.out.println("host " + host.getId() + " will give " + user.getnumCPU() + " CPUs and " +user.getnumRAM()+ " GB of RAM and " + user.getnumStorage() + " GB of storage to user" + userid + " to finish his cloudlet " + cloudlet1.getCloudletId());
System.out.println("the user will pay " + payprice + " dollars to the broker.");
reimbursement = payprice * bb;
System.out.println("then user" + user.getuserId() + " will relinquish " + d + " CPUs and " + e + " GB of RAM and " + f + " GB of storage");
System.out.println("the broker will reimburse" + reimbursement + " to the customer");
reimburse.add(reimbursement);
System.out.println(host.getmtimes().get(userid));
CloudSim.startSimulation();
///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////
CloudSim.init(num_user, calendar, trace_flag);

@SuppressWarnings("unused")
Datacenter datacenter33 = createDatacenter("Datacenter_1");

DatacenterBroker broker33 = createBroker();
int broker33Id = broker33.getId();

//update totalProfit;
totalProfit += payprice;

broker33.submitUserList(userlist);
broker33.BindUserToHost(user.getuserId(), host.getId());

host.setTotalProfit(totalProfit);

broker33.setHostList(hostlist1);

cloudletid = 33;
Cloudlet cloudlet33 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
cloudlet33.setbrokerId(broker33Id);
//update the price of cloudlet;
//price = 200;
//cloudlet33.setprice(price);
cloudlet33.setuser(user);
user.setCloudletId(cloudlet33.getCloudletId());

//add the cloudlet to the list
cloudletList.add(cloudlet33);

//submit cloudlet list to the broker
broker33.submitCloudletList(cloudletList);

//adrian

user.submitCloudlet(cloudlet33);
user.submitRequest(user.getCloudlet(), false, pesNumber, numram, (long)(fileSize/1000000000), numtime);


basicprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()/400);
pledge = basicprice * 1.05;
System.out.println("user" + user.getuserId() + " asks for " + user.getnumCPU() + " CPUs and " + user.getnumRAM() + " GB of RAM and " + user.getnumStorage() + " GB of storage to host" + host.getId());


if(cloudlet33.pledge == true){
//here I didn't consider giving back the extra money if the user will not relinquish any resource;
payprice = pledge;
}
else{
//host.minusmtimes(userid);
//here remember the BROKER is changing to BROKER33!!!!!!!!! and cloudlet is cloudlet33!!!!!!!!!
if (host.getmtimes().get(userid) == 0){
					host.addmtimes(userid);
					ArrayList<Double> aaa = new ArrayList<Double> ();
					ArrayList<Double> bbb = new ArrayList<Double> ();
					//here I will set some default values for aop and sop and ...;
					if(host.getmaop().get(userid) == null){
					broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3, 0.3);
					//right now we don't consider the pricing model;
					//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId(), 0.3);
					host.getmaop().put(userid, bbb);
					}
					else {
						broker.CalNumOfRes(user.getuserId(), host.getId(), 0.3);
						//right now we don't consider the pricing model;
						//payprice = broker.CalPrice(cloudlet.getCloudletId(), host.getId());
					}
					host.getmsop().put(userid, aaa);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
				}
else if(host.getmtimes().get(userid) == 1){
host.addmtimes(userid);
//will add codes for sop and aop and ...;
broker33.CalNumOfRes1(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(1));
host.addmaop(userid, relinp.get(1));
//payprice = broker33.CalPrice(cloudlet33.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) == 2){
host.addmtimes(userid);
//will add codes for sop and aop and ...;
broker33.CalNumOfRes2(user.getuserId(), host.getId());
host.addmsop(userid, relinp.get(2));
host.addmaop(userid, relinp.get(2));
//payprice = broker33.CalPrice(cloudlet33.getCloudletId(), host.getId());
}
else if(host.getmtimes().get(userid) > 2){
broker33.CalNumOfRes(user.getuserId(), host.getId());	
int a = host.getmtimes().get(userid);
host.addmtimes(userid);
host.addmsop(userid, relinp.get(a));
host.addmaop(userid, relinp.get(a));

}
}



utimeh = host.getmtimes().get(userid) ;
System.out.println("this is the " + utimeh + "th time for user" + user.getuserId() + " to use services of host" + host.getId());
payprice = user.getnumCPU() * 0.01 + user.getnumRAM() * 0.01 + (double)(user.getnumStorage()*0.001);
vmid = 33;

Vm vm33 = new Vm(vmid, broker33Id, mips, user.getnumCPU(), user.getnumRAM(), bw, user.getnumStorage(), vmm, new CloudletSchedulerTimeShared());

vm33.setHost(host);

//int vmid33 = 1;
//Vm vm33 = new Vm(vmid33, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
//add the VM to the vmList
vmlist.add(vm33);
//vmlist.add(vm33);

//submit vm list to the broker
broker33.submitVmList(vmlist);

broker33.bindCloudletToVm(cloudletid, vmid);

payprice = payprice * user.gettime();

aa = utimeh -1;
bb = relinp.get(aa);
System.out.println("the relinquish probability of the user for this time is " + bb);
//System.out.println(bb);
d = (int) (user.getnumCPU() * bb);
e = (int) (user.getnumRAM() * bb);
f = (long) (user.getnumStorage() * bb);
System.out.println("host " + host.getId() + " will give " + user.getnumCPU() + " CPUs and " +user.getnumRAM()+ " GB of RAM and " + user.getnumStorage() + " GB of storage to user" + userid + " to finish his cloudlet " + cloudlet1.getCloudletId());
System.out.println("the user will pay " + payprice + " dollars to the broker.");
reimbursement = payprice * bb;
System.out.println("then user" + user.getuserId() + " will relinquish " + d + " CPUs and " + e + " GB of RAM and " + f + " GB of storage");
System.out.println("the broker will reimburse" + reimbursement + " to the customer");
reimburse.add(reimbursement);
System.out.println(host.getmtimes().get(userid));
CloudSim.startSimulation();

*/








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
        String s = buffer.toString();
        String[] a = s.split(",");
        int aa = a.length;
        int[] b = new int[aa];
        for(int i=0;i<a.length;i++){
        	b[i] = Integer.parseInt(a[i]);
        	System.out.println(b[i]);
        }
        return b;
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