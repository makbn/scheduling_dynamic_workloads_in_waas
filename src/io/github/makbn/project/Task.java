package io.github.makbn.project;

import org.cloudbus.cloudsim.Vm;

import java.util.HashSet;

public class Task{
    private int id;

    private HashSet<Task> parents;
    private HashSet<Task> children;

    private double executeTimeOnVm =-1;
    private double inputTime=-1;
    private double outputTime=-1;
    private double outputDta;
    private double deadline;
    private int size;
    private  MyStorage storage;

    private double startTime;
    private double endTime;
    private double deltaTime;

    private Vm taskVm;

    public Task(int id, double deadline, int size, MyStorage storage) {
        this.id = id;
        this.deadline = deadline;
        this.size = size;
        this.storage = storage;
        this.parents=new HashSet<>();
        this.children=new HashSet<>();
    }

    public boolean addParent(Task task){
        if(parents.contains(task)) {
            System.err.println(task.toString()+" exists in parent list of "+toString());
            return false;
        }
        System.out.println(task.toString()+" added as parent of"+toString());
        return parents.add(task);
    }

    public boolean addChild(Task child){
        if(children.contains(child)) {
            System.err.println(child.toString()+" exists in child list of "+toString());
            return false;
        }
        System.out.println(child.toString()+" added as parent of"+toString());
        return children.add(child);
    }
    public double getOutputTime() {
        return outputTime;
    }

    public double getOutputDta() {
        return outputDta;
    }

    public double getExecutionTimeOnTask(Vm vm){
        double time= (size/vm.getMips());

        executeTimeOnVm=time;

        return time;
    }

    public double getReadInputTime(Vm vm){
        double time=0;
        for(Task parent:parents){
            time+=(parent.getOutputDta()/vm.getBw()) + (parent.getOutputDta()/storage.getMaxReadTransferRate());
        }
        inputTime=time;
        return time;
    }

    public double getStartTime() {
        return startTime;
    }

    public double getEndTime() {
        return endTime;
    }

    public double getDeltaTime() {
        return startTime+getTotalTime(taskVm);
    }

    public double getDeadline() {
        return deadline;
    }

    public double getWriteOutputTime(Vm vm){
        double time=0;
        for(Task parent:parents){
            time+=(parent.getOutputDta()/vm.getBw()) + (parent.getOutputDta()/storage.getMaxWriteTransferRate());
        }
        outputTime=time;
        return time;
    }

    public double getTotalTime(Vm vm){
        if(executeTimeOnVm==-1)
            getExecutionTimeOnTask(vm);
        if(inputTime==-1)
            getReadInputTime(vm);
        if(outputTime==-1)
            getWriteOutputTime(vm);
        return executeTimeOnVm+inputTime+outputTime;
    }
}

