package template;

/* import table */
import logist.simulation.Vehicle;
import logist.agent.Agent;
import logist.behavior.DeliberativeBehavior;
import logist.plan.Plan;
import logist.task.Task;
import logist.task.TaskDistribution;
import logist.task.TaskSet;
import logist.topology.Topology;
import logist.topology.Topology.City;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

/**
 * An optimal planner for one vehicle.
 */
@SuppressWarnings("unused")
public class DeliberativeTemplate implements DeliberativeBehavior {

	enum Algorithm { BFS, ASTAR }
	
	/* Environment */
	Topology topology;
	TaskDistribution td;
	
	/* the properties of the agent */
	Agent agent;
	int capacity;

	/* the planning class */
	Algorithm algorithm;
	
	@Override
	public void setup(Topology topology, TaskDistribution td, Agent agent) {
		this.topology = topology;
		this.td = td;
		this.agent = agent;
		
		// initialize the planner
		int capacity = agent.vehicles().get(0).capacity();
		String algorithmName = agent.readProperty("algorithm", String.class, "BFS");
		
		// Throws IllegalArgumentException if algorithm is unknown
		algorithm = Algorithm.valueOf(algorithmName.toUpperCase());
		
		// ...
	}
	
	@Override
	public Plan plan(Vehicle vehicle, TaskSet tasks) {
		Plan plan;
		long startTime = System.currentTimeMillis();
		// Compute the plan with the selected algorithm.
		switch (algorithm) {
		case ASTAR:
			// ...
			plan = naivePlan(vehicle, tasks);
			break;
		case BFS:
			// ...
			plan = new BFS(vehicle, tasks).build(false);
			break;
			default:
				throw new AssertionError("Should not happen.");
		}
		long endTime = System.currentTimeMillis();

		System.out.println("Algorithm: " + algorithm);

		double distance = vehicle.getDistance() + plan.totalDistance();
		System.out.println("Distance to be travelled: " + distance);

		double timeTaken= (endTime - startTime) / 1000.0;
		long cost= (long) (vehicle.costPerKm()*plan.totalDistance());
		System.out.println("Plan computed in " + (endTime - startTime) / 1000.0 + "s");
		String fileName =  System.getProperty("user.dir")+ "/txtFiles/"
				+ (Integer) tasks.size() + agent.readProperty("algorithm", String.class, "BFS");
		writeDataToCSV(fileName, cost, timeTaken);

		return plan;
	}

	public void writeDataToCSV(String csvFile, long dataItem2, double dataItem3) {
		FileWriter writer;

		try {
			writer = new FileWriter(csvFile, false);

			CSVWriter.writeLine(writer, Arrays.asList(
					Long.toString(dataItem2),
					Double.toString(dataItem3)
					)
			);

			writer.flush();
			writer.close();

		} catch (IOException e2) {
			e2.printStackTrace();
		}
	}

	private Plan naivePlan(Vehicle vehicle, TaskSet tasks) {
		City current = vehicle.getCurrentCity();
		Plan plan = new Plan(current);

		for (Task task : tasks) {
			// move: current city => pickup location
			for (City city : current.pathTo(task.pickupCity))
				plan.appendMove(city);

			plan.appendPickup(task);

			// move: pickup location => delivery location
			for (City city : task.path())
				plan.appendMove(city);

			plan.appendDelivery(task);

			// set current city
			current = task.deliveryCity;
		}
		return plan;
	}

	@Override
	public void planCancelled(TaskSet carriedTasks) {
		
		if (!carriedTasks.isEmpty()) {
			// This cannot happen for this simple agent, but typically
			// you will need to consider the carriedTasks when the next
			// plan is computed.
		}
	}
}
