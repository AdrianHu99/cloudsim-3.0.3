package org.cloudbus.cloudsim.examples;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

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

public class SingleUser {
	/** The cloudlet list. */
	private static List<Cloudlet> cloudletList;

	/** The vmlist. */
	private static List<Vm> vmlist;
	
	/** The userlist. */
	private static List<User> userlist;
	
	/** The hostlist. */
	private static List<Host> hostlist1;

	/**
	 * Creates main() to run this example.
	 *
	 * @param args the args
	 */
	
	
	
	
	
	//how the broker works with a single customer with pledge system.
	public static void main(String[] args) {
		

		// Creates some datasets for collecting data during the simulation
		Random r = new Random();
		ArrayList<Integer> acpu = new ArrayList<Integer> ();
		ArrayList<Integer> aram = new ArrayList<Integer> ();
		ArrayList<Long> asto = new ArrayList<Long> ();
		ArrayList<Double> reimburse = new ArrayList<Double> ();
		ArrayList<Double> payment = new ArrayList<Double> ();
		ArrayList<Double> relinp = new ArrayList<Double> ();
		
		
		
		
		
		// We could first generate the relinquish probabilities that would be used during the simulation
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

		// Or we could also automatically use Gaussian distribution to generate relinquish probabilities
		// the standard deviation is 0.3, and the average is 0.9;
		for(int i = 0 ; i < 400; i++){
			double a = r.nextGaussian() * 0.3 + 0.9;
			if (a < 0 || a > 1){}
			else{
				relinp.add(a);
			}
		}
		
		
	
		Log.printLine("Starting CloudSimExample1...");
			// I removed "try" from the original example so that it could be extended easier by adding more instances
			// Initialize the CloudSim package. It should be called before creating any entities.
			int num_user = 1; // number of cloud users
			Calendar calendar = Calendar.getInstance();
			boolean trace_flag = false; // mean trace events

			// Initialize the CloudSim library
			CloudSim.init(num_user, calendar, trace_flag);

			// Create Datacenters
			// Datacenters are the resource providers in CloudSim. We need at
			// least one of them to run a CloudSim simulation
			Datacenter datacenter0 = createDatacenter("Datacenter_0");
			
			// Create an user and put it into userlist. Users are cloud service customers
			// they will submit requests(cloudlets) to the broker
			userlist = new ArrayList<User>();
			int userid = 0;
			User user = new User(userid);

			userlist.add(user);
		
			
			

			// Create Broker
			DatacenterBroker broker = createBroker();
			int brokerId = broker.getId();
			
			// Initiate the hostlist1, obtain the host from the original hostlist in datacenter and add the host into hostlist1
			Host host = datacenter0.getHostList().get(0);
			hostlist1 = new ArrayList<Host>();
			hostlist1.add(host);
			host = hostlist1.get(0);
			
			// Build the connection between the host and the user
			broker.setHostList(hostlist1);
			broker.submitUserList(userlist);
			broker.BindUserToHost(user.getuserId(), host.getId());
			
			// Fourth step: Create one virtual machine
			vmlist = new ArrayList<Vm>();

			// Create one Cloudlet(request) with the details and add it into cloudletlist
			cloudletList = new ArrayList<Cloudlet>();
			int pesNumber = 300; // number of cpus
			// Cloudlet properties
			int cloudletid = 0;
			long length = 400000;
			// The unit of this filesize is Byte; so I need to divide it with 1000000000;
			long fileSize = 300000000000L;
			long outputSize = 300;
			UtilizationModel utilizationModel = new UtilizationModelFull();
			Cloudlet cloudlet = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
			// Bind the cloudlet to the broker
			cloudlet.setbrokerId(brokerId);
			

			// Bind the cloudlet to the user
			cloudlet.setuser(user);
			//cloudlet.setExecStartTime(0.1);
			user.setCloudletId(cloudlet.getCloudletId());
			
			// add the cloudlet to the list
			cloudletList.add(cloudlet);

			// submit cloudlet list to the broker
			broker.submitCloudletList(cloudletList);
			
			// User submits the request to the broker with the cloudlet
			// It could be improved that the details of the request could be put into cloudlet, not just the request(variables of User)
			double numtime = 10;
			int numram = 300;
			int numcpu = 300;
			long numstorage = 300L;
			user.setnumtime(numtime);
			user.submitCloudlet(cloudlet);
			user.submitRequest(user.getCloudlet(), false, numcpu, numram, numstorage, numtime);
			
			//set user's aop as not null
			host.getmtimes().put(userid, 0);
			// Since the user in this simulaiton is an existing user, so we need to add some records for this user
			// In this simulation, AOP is same as SOP as we assume that the user didn't user other services before.
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
