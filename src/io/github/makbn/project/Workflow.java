package io.github.makbn.project;

import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.Vm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Workflow {
	private int id;
	/* Delta(W)*/
	private double deadline;
	private Container container;
	private HashSet<Task> tasks;
	private HashSet<Edge> edges;
	private HashMap<Task,ArrayList<Edge>> edgesByParent;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getDeadline() {
		return deadline;
	}

	public void setDeadline(double deadline) {
		this.deadline = deadline;
	}

	public Container getContainer() {
		return container;
	}

	public void setContainer(Container container) {
		this.container = container;
	}

	public HashSet<Task> getTasks() {
		return tasks;
	}

	public void setTasks(HashSet<Task> tasks) {
		this.tasks = tasks;
	}

	public HashSet<Edge> getEdges() {
		return edges;
	}

	public void setEdges(HashSet<Edge> edges) {
		this.edges = edges;
	}

	public HashMap<Task, ArrayList<Edge>> getEdgesByParent() {
		return edgesByParent;
	}

	public void setEdgesByParent(HashMap<Task, ArrayList<Edge>> edgesByParent) {
		this.edgesByParent = edgesByParent;
	}


	public ArrayList<Edge> getEdgesByTask(Task t){
		return edgesByParent.get(t);
	}

	public boolean addEdge(Task parent,Task child){
		Edge temp=new Edge(parent,child);
		if(edges.contains(temp)){
			System.out.println("requested edge exists between "+ parent.toString() +" and "+ child.toString());
			return false;
		}
		System.out.println("edge added between "+parent.toString()+" and "+ child.toString());
		return edges.add(temp);
	}

	//private double containerDelay;
	

}




