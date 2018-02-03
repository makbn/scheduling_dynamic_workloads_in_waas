package io.github.makbn.project;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisioner;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

public class PhaseOne {

	public final static int MAX_USER_COUNT = 10;
	public final static int MIN_USER_COUNT = 5;
	public final static int MIN_VM_COUNT=3;
	public final static int MAX_VM_COUNT=5;
	public final static int HOST_COUNT = 10;
	public final static int MIN_TASK_COUNT=30;
	public final static int MAX_TASK_COUNT=50;
	
	public static void main(String[] args) throws Exception {

		
		Log.print("Start Simulation\n");
		int numberOfUser = getUserCount();
		Log.print("Number of users : "+numberOfUser+"\n");
		
		boolean traceFlag = false;
		Calendar cal = Calendar.getInstance();
		ArrayList<HostConfig> hConfigs = new ArrayList<>();
		ArrayList<Host> hosts = new ArrayList<>();
		LinkedList<Storage> storages = new LinkedList<>();
		ArrayList<DatacenterBroker> brokers=new ArrayList<>();
		Datacenter dc;

		CloudSim.init(numberOfUser, cal, traceFlag);

		for (int i = 0; i < HOST_COUNT; i++) {
			hConfigs.add(HostConfig.getSimpleHostConfig(i));
		}

		for (int i = 0; i < HOST_COUNT; i++) {
			hosts.add(createHost(hConfigs.get(i)));
		}

		dc = createDatacenter("DC_0", HOST_COUNT, hosts, storages);
		
		for(int i=0;i<numberOfUser;i++) {
			brokers.add(createBroker("Broker_No_"+i));
		}
		
		
		for(int i=0;i<numberOfUser;i++) {
			Log.print("===================== START USER: "+i+" =====================\n");
			int numberOfVm=getVmCount();
			Log.print("Number of Vms for User("+i+") is : "+numberOfVm);
			int numberOfTask=getTaskCount();
			Log.print("Number of Tasks for User("+i+") is : "+numberOfVm);
			ArrayList<Vm> vms=new ArrayList<>();
			for(int j=0;j<numberOfVm;j++) {
				vms.add(createVm(j, brokers.get(i).getId(), 512, 100, 1, 10000, 1000, "Xen"));
			}
			ArrayList<Cloudlet> cloudlets=new ArrayList<>();
			for(int j=0;j<numberOfTask;j++) {
				cloudlets.add(createCloudlet(j, brokers.get(i).getId(), 1000, 300, 300, 1));
			}
			
			brokers.get(i).submitVmList(vms);
			brokers.get(i).submitCloudletList(cloudlets);
			Log.print("===================== END =====================\n");
		}
		
		CloudSim.startSimulation();
		
		
		ArrayList<List<Cloudlet>> results=new ArrayList<>();
		
		for(int i=0;i<numberOfUser;i++) {
			
			List<Cloudlet> result=brokers.get(i).getCloudletReceivedList();
			results.add(result);
		}
		
		CloudSim.stopSimulation();
		
		for(int i=0;i<numberOfUser;i++) {
			Log.print("Broker ID: "+brokers.get(i).getId());
			printCloudletList(results.get(i));
		}
		
		

	}
	
	
	
	private static Cloudlet createCloudlet(int id,int userId,long lenght,long fileSize,long outputSize,int pesNumber) {
		UtilizationModel utCpu=new UtilizationModelFull();
		UtilizationModel utRam=new UtilizationModelFull();
		UtilizationModel utBw=new UtilizationModelFull();
		Cloudlet cloudlet=new Cloudlet(id, lenght, pesNumber, fileSize,outputSize, utCpu, utRam, utBw);
		cloudlet.setUserId(userId);
		return cloudlet;
	}
	
	/**
	 * 
	 * @param vmm vmm name
	 * @param size image size
	 * @param bw 
	 * @param ram vm memory
	 * @param mips
	 * @param pesNumber number of cpus
	 * @return vm
	 */
	private static Vm createVm(int id,int userId,int ram,int mips,int pesNumber,long size,long bw,String vmm) {
		Vm vm=new Vm(id, userId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
		return vm;
	}

	private static Datacenter createDatacenter(String name, int hostCount, ArrayList<Host> hosts,
			LinkedList<Storage> storages) throws Exception {

		Datacenter dc = new Datacenter(name, DatacenterConfig.getSimpleCharactrestics(hosts),
				new VmAllocationPolicySimple(hosts), storages, 0);

		return dc;
	}

	private static Host createHost(HostConfig config) {

		return new Host(config.getId(), config.getRamProvisioner(), config.getBwProvisioner(), config.getStorage(),
				config.getPList(), config.geVmSchedulerTimeShared());
	}
	
	private static DatacenterBroker createBroker(String name) {
		DatacenterBroker dcb=null;
		try {
			dcb=new DatacenterBroker(name);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return dcb;
	}

	/**
	 * @return number between {@link PhaseOne#MAX_USER_COUNT} and {@link PhaseOne#MIN_USER_COUNT}
	 */
	private static int getUserCount() {
		int count = MIN_USER_COUNT + new Random().nextInt(MAX_USER_COUNT - MIN_USER_COUNT+1);
		return count;
	}
	
	/**
	 * @return number between {@link PhaseOne#MAX_TASK_COUNT} and {@link PhaseOne#MIN_TASK_COUNT}
	 */
	private static int getTaskCount() {
		int count = MIN_TASK_COUNT + new Random().nextInt(MAX_TASK_COUNT - MIN_TASK_COUNT+1);
		return count;
	}
	
	/**
	 * @return number between {@link PhaseOne#MIN_VM_COUNT} and {@link PhaseOne#MAX_VM_COUNT}
	 */
	private static int getVmCount() {
		int count = MIN_VM_COUNT + new Random().nextInt(MAX_VM_COUNT - MIN_VM_COUNT+1);
		return count;
	}
	
	
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

class HostConfig {
	protected int id;
	protected int ram;
	protected int storage;
	protected int bw;
	protected int coreCount;
	protected int mips;
	protected ArrayList<Pe> pList;

	public HostConfig(int id, int ram, int storage, int bw, int coreCount, int mips) {
		super();
		this.id = id;
		this.ram = ram;
		this.storage = storage;
		this.bw = bw;
		this.mips=mips;
		this.coreCount = coreCount;

	}

	public RamProvisionerSimple getRamProvisioner() {
		return new RamProvisionerSimple(this.ram);
	}

	public BwProvisionerSimple getBwProvisioner() {
		return new BwProvisionerSimple(this.bw);
	}

	public VmSchedulerTimeShared geVmSchedulerTimeShared() {
		return new VmSchedulerTimeShared(getPList());
	}

	public int getStorage() {
		return storage;
	}

	public void setStorage(int storage) {
		this.storage = storage;
	}

	public ArrayList<Pe> getPList() {
		if (pList == null || pList.size() == 0) {
			pList = new ArrayList<>();

			for (int i = 0; i < coreCount; i++) {
				Pe pe = new Pe(i, new PeProvisionerSimple(this.mips));
				pList.add(pe);
			}
		}
		return pList;
	}

	public static HostConfig getSimpleHostConfig(int id) {
		return new HostConfig(id, 2048, 1000000, 10000, 4, 1000000);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}

class DatacenterConfig {
	protected String arch;
	protected String os;
	protected String vmm;
	protected double timeZone;
	protected double cost;
	protected double costPerMem;
	protected double costPerStorage;
	protected double costPerBw;

	public DatacenterConfig(String arch, String os, String vmm, double timeZone, double cost, double costPerMem,
			double costPerStorage, double costPerBw) {
		super();
		this.arch = arch;
		this.os = os;
		this.vmm = vmm;
		this.timeZone = timeZone;
		this.cost = cost;
		this.costPerMem = costPerMem;
		this.costPerStorage = costPerStorage;
		this.costPerBw = costPerBw;
	}

	public String getArch() {
		return arch;
	}

	public void setArch(String arch) {
		this.arch = arch;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getVmm() {
		return vmm;
	}

	public void setVmm(String vmm) {
		this.vmm = vmm;
	}

	public double getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(double timeZone) {
		this.timeZone = timeZone;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public double getCostPerMem() {
		return costPerMem;
	}

	public void setCostPerMem(double costPerMem) {
		this.costPerMem = costPerMem;
	}

	public double getCostPerStorage() {
		return costPerStorage;
	}

	public void setCostPerStorage(double costPerStorage) {
		this.costPerStorage = costPerStorage;
	}

	public double getCostPerBw() {
		return costPerBw;
	}

	public void setCostPerBw(double costPerBw) {
		this.costPerBw = costPerBw;
	}

	public static DatacenterConfig getSimpleConfig() {
		return new DatacenterConfig("x86", "Linux", "Xen", 10.0, 3.0, 0.5, 0.1, 0.1);
	}

	public static DatacenterCharacteristics getSimpleCharactrestics(ArrayList<Host> hosts) {
		DatacenterConfig simpleConfig = getSimpleConfig();
		return new DatacenterCharacteristics(simpleConfig.getArch(), simpleConfig.getOs(), simpleConfig.getVmm(), hosts,
				simpleConfig.getTimeZone(), simpleConfig.getCost(), simpleConfig.getCostPerMem(),
				simpleConfig.getCostPerStorage(), simpleConfig.getCostPerBw());

	}

}
