
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
	public class CloudSimExample01 {

		/** The cloudlet list. */
		private static List<Cloudlet> cloudletList;

		/** The vmlist. */
		private static List<Vm> vmlist;
		
		private static List<User> userlist;

		/**
		 * Creates main() to run this example.
		 *
		 * @param args the args
		 */
		public static void main(String[] args) {
			Random r = new Random();
			ArrayList<Double> relinp = new ArrayList<Double> ();
			
			for(int i = 0 ; i < 100; i++){
				double a = r.nextGaussian() * 0.5 + 0.5;
				if (a < 0 || a > 1){}
				else{
					relinp.add(a);
				}
			}
			
			System.out.println(relinp.get(0));

			Log.printLine("Starting CloudSimExample1...");
			
			///我把try去掉了
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

				// Third step: Create Broker
				DatacenterBroker broker = createBroker();
				int brokerId = broker.getId();

				double totalProfit = 100;
				List<Host> hostlist = datacenter0.getHostList();
				Host host = hostlist.get(0);
				host.setTotalProfit(totalProfit);
				
				broker.setHostList(hostlist);
				broker.submitUserList(userlist);
				broker.BindUserToHost(user.getuserId(), host.getId());
				
				// Fourth step: Create one virtual machine
				vmlist = new ArrayList<Vm>();

				// Fifth step: Create one Cloudlet
				cloudletList = new ArrayList<Cloudlet>();
				
				int pesNumber = 1; // number of cpus
				// Cloudlet properties
				int cloudletid = 0;
				long length = 400000;
				long fileSize = 300;
				long outputSize = 300;
				double price = 300;
				double pledgeAmount = price * 1.05;
				UtilizationModel utilizationModel = new UtilizationModelFull();
				
				Cloudlet cloudlet = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
				cloudlet.setbrokerId(brokerId);
				
				cloudlet.setprice(price);
				
				user.setCloudletId(cloudlet.getCloudletId());
				
				// add the cloudlet to the list
				cloudletList.add(cloudlet);

				// submit cloudlet list to the broker
				broker.submitCloudletList(cloudletList);
				
				//adrian
				
				user.submitCloudlet(cloudlet);
				user.submitRequest(user.getCloudlet(), false);
				
				
				if(cloudlet.pledge == true){
					//here I didn't consider giving back the extra money if the user will not relinquish any resource;
					cloudlet.setprice(pledgeAmount);
				}
				
				double num = 0;

				if (host.getmtimes().get(userid) == null){
					host.getmtimes().put(userid, 1);
					ArrayList<Double> aaa = new ArrayList<Double> ();
					
					//here I will set some default values for aop and sop and ...;
					if(host.getmaop().get(userid) == null){
						num = broker.CalNumOfRes(cloudlet.getCloudletId(), host.getId(), 0.5, 0.3);
					}
					else {
						num = broker.CalNumOfRes(cloudlet.getCloudletId(), host.getId(), 0.5);
					}
					host.getmsop().put(userid, aaa);
					host.getmaop().put(userid, aaa);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
				}
				else if(host.getmtimes().get(userid) == 1){
					host.addmtimes(userid);
					// will add codes for sop and aop and ...;
					num = broker.CalNumOfRes1(cloudlet.getCloudletId(), host.getId());
					host.addmsop(userid, relinp.get(1));
					host.addmaop(userid, relinp.get(1));
				}
				else if(host.getmtimes().get(userid) == 2){
					host.addmtimes(userid);
					// will add codes for sop and aop and ...;
					num = broker.CalNumOfRes2(cloudlet.getCloudletId(), host.getId());
					host.addmsop(userid, relinp.get(2));
					host.addmaop(userid, relinp.get(2));
				}
				else if(host.getmtimes().get(userid) > 2){
					int a = host.getmtimes().get(userid);
					host.addmtimes(userid);
					host.addmsop(userid, relinp.get(a));
					host.addmaop(userid, relinp.get(a));
					num = broker.CalNumOfRes(cloudlet.getCloudletId(), host.getId());
				}
				
				
				// VM description
							int vmid = 0;
							int mips = 1000;
							long size = 10000; // image size (MB)
							int ram = 512; // vm memory (MB)
							long bw = 1000;
							if(num > 0.64){
								bw = 1560;
							}
							else if(num < 0.1){
								bw = 400;
							}
							else {
								bw =  (long) (num * 1560 / 0.64) ;
							}
							
							String vmm = "Xen"; // VMM name
							
							// create VM
							Vm vm = new Vm(vmid, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
							
							vm.setHost(host);
							
							//int vmid2 = 1;
							//Vm vm2 = new Vm(vmid2, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
							// add the VM to the vmList
							vmlist.add(vm);
							//vmlist.add(vm2);

							// submit vm list to the broker
							broker.submitVmList(vmlist);
							
							broker.bindCloudletToVm(cloudletid, vmid);
				
							System.out.println("host " + host.getId() + " will allocate " + (double)(bw)/100 +  " Mbps of bandwidth to user " + userid + " to finish his cloudlet " + cloudlet.getCloudletId());
							System.out.println("the user will pay" );
							

				// Sixth step: Starts the simulation
				CloudSim.startSimulation();

				
				///////////////////////////////
				//////////////////////////////
				
				
				CloudSim.init(num_user, calendar, trace_flag);

				Datacenter datacenter1 = createDatacenter("Datacenter_1");

				DatacenterBroker broker1 = createBroker();
				int broker1Id = broker1.getId();

				totalProfit += 1;
				broker1.setHostList(hostlist);
				broker1.submitUserList(userlist);
				broker1.BindUserToHost(user.getuserId(), host.getId());
				
				List<Host> hostlist1 = datacenter1.getHostList();
				hostlist1.add(0,host);
				host.setTotalProfit(totalProfit);
				
				cloudletid = 1;
				
				Cloudlet cloudlet1 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
				cloudlet1.setbrokerId(broker1Id);
				
				cloudlet1.setprice(price);
				
				user.setCloudletId(cloudlet1.getCloudletId());
				
				// add the cloudlet to the list
				cloudletList.add(cloudlet1);

				// submit cloudlet list to the broker
				broker1.submitCloudletList(cloudletList);
				
				//adrian
				
				user.submitCloudlet(cloudlet1);
				user.submitRequest(user.getCloudlet(), false);
				
				
				if(cloudlet1.pledge == true){
					//here I didn't consider giving back the extra money if the user will not relinquish any resource;
					cloudlet.setprice(pledgeAmount);
				}

				host.minusmtimes(userid);
				
				if (host.getmtimes().get(userid) == null){
					host.getmtimes().put(userid, 1);
					ArrayList<Double> aaa = new ArrayList<Double> ();
					
					//here I will set some default values for aop and sop and ...;
					if(host.getmaop().get(userid) == null){
						num = broker.CalNumOfRes(cloudlet.getCloudletId(), host.getId(), 0.5, 0.3);
					}
					else {
						num = broker.CalNumOfRes(cloudlet.getCloudletId(), host.getId(), 0.5);
					}
					host.getmsop().put(userid, aaa);
					host.getmaop().put(userid, aaa);
					host.addmsop(userid, relinp.get(0));
					host.addmaop(userid, relinp.get(0));
				}
				else if(host.getmtimes().get(userid) == 1){
					host.addmtimes(userid);
					// will add codes for sop and aop and ...;
					num = broker.CalNumOfRes1(cloudlet.getCloudletId(), host.getId());
					host.addmsop(userid, relinp.get(1));
					host.addmaop(userid, relinp.get(1));
				}
				else if(host.getmtimes().get(userid) == 2){
					host.addmtimes(userid);
					// will add codes for sop and aop and ...;
					num = broker.CalNumOfRes2(cloudlet.getCloudletId(), host.getId());
					host.addmsop(userid, relinp.get(2));
					host.addmaop(userid, relinp.get(2));
				}
				else if(host.getmtimes().get(userid) > 2){
					int a = host.getmtimes().get(userid);
					host.addmtimes(userid);
					host.addmsop(userid, relinp.get(a));
					host.addmaop(userid, relinp.get(a));
					num = broker.CalNumOfRes(cloudlet.getCloudletId(), host.getId());
				}
				
				vmid = 1;
				
				Vm vm1 = new Vm(vmid, broker1Id, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
				
				vm1.setHost(host);
				
				//int vmid2 = 1;
				//Vm vm2 = new Vm(vmid2, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
				// add the VM to the vmList
				vmlist.add(vm1);
				//vmlist.add(vm2);

				// submit vm list to the broker
				broker1.submitVmList(vmlist);
				
				broker1.bindCloudletToVm(cloudletid, vmid);
				
				System.out.println(host.getmtimes().get(userid));
				
				CloudSim.startSimulation();
				
				
				CloudSim.stopSimulation();

				//Final step: Print results when simulation is over
				List<Cloudlet> newList = broker1.getCloudletReceivedList();
				printCloudletList(newList);

				Log.printLine("CloudSimExample1 finished!");
				
				
				///////////////////////////////////////////////////////////////////////////////////////////////////////
				///////////////////////////////////////////////////////////////////////////////////////////////////////
				
				/*second time
				cloudletid = 1;
				
				Cloudlet cloudlet1 = new Cloudlet(cloudletid, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
				cloudlet1.setbrokerId(brokerId);
				
				cloudlet1.setprice(price);
				
				user.setCloudletId(cloudlet1.getCloudletId());
				
				// add the cloudlet to the list
				cloudletList.add(cloudlet1);

				// submit cloudlet list to the broker
				broker.submitCloudletList(cloudletList);
				
				//adrian
				
				user.submitCloudlet(cloudlet1);
				user.submitRequest(user.getCloudlet(), false);
				
				
				if(cloudlet1.pledge == true){
					//here I didn't consider giving back the extra money if the user will not relinquish any resource;
					cloudlet.setprice(pledgeAmount);
				}
				

				if (host.getmtimes().get(userid) == null){
					host.getmtimes().put(userid, 1);
					//here I will set some default values for aop and sop and ...;
					if(host.getmaop().get(userid) == null){
						num = broker.CalNumOfRes(cloudlet1.getCloudletId(), host.getId(), 0.5, 0.3);
					}
					else {
						num = broker.CalNumOfRes(cloudlet1.getCloudletId(), host.getId(), 0.5);
					}
				}
				else if(host.getmtimes().get(userid) == 1){
					host.addmtimes(userid);
					// will add codes for sop and aop and ...;
					num = broker.CalNumOfRes1(cloudlet1.getCloudletId(), host.getId());
				}
				else if(host.getmtimes().get(userid) == 2){
					host.addmtimes(userid);
					// will add codes for sop and aop and ...;
					num = broker.CalNumOfRes2(cloudlet1.getCloudletId(), host.getId());
				}
				else if(host.getmtimes().get(userid) > 2){
					host.addmtimes(userid);
					num = broker.CalNumOfRes(cloudlet1.getCloudletId(), host.getId());
				}
				
				vmid = 1;
				
				Vm vm1 = new Vm(vmid, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
				
				vm1.setHost(host);
				
				//int vmid2 = 1;
				//Vm vm2 = new Vm(vmid2, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
				// add the VM to the vmList
				vmlist.add(vm1);
				//vmlist.add(vm2);

				// submit vm list to the broker
				broker.submitVmList(vmlist);
				
				broker.bindCloudletToVm(cloudletid, vmid);
				
				CloudSim.startSimulation();

				CloudSim.stopSimulation();
				*/
				
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
			peList.add(new Pe(0, new PeProvisionerSimple(mips))); // need to store Pe id and MIPS Rating

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
