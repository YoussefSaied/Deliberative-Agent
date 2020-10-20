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

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;


/**
 * An optimal planner for one vehicle.
 */
@SuppressWarnings("unused")
public class DeliberativeTemplate implements DeliberativeBehavior {

	Topology topology;
	TaskDistribution td;
	String agent_name;

	Agent agent;
	int capacity;

	String algorithm;

	@Override
	public void setup(Topology topology, TaskDistribution td, Agent agent) {
		this.topology = topology;
		this.td = td;
		this.agent = agent;

		// Get the vehicle's capacity.
		int capacity = agent.vehicles().get(0).capacity();

		// Get the algorithm.
		algorithm = agent.readProperty("algorithm", String.class, "BFS").toUpperCase();

		// ...
	}
	
	@Override
	public Plan plan(Vehicle vehicle, TaskSet tasks) {

		Plan plan;

		// Get the timestamp when the plan computation begins.
		long startTime = System.currentTimeMillis();

		// Compute the plan with the selected algorithm.
		switch (algorithm) {
		case "ASTAR":
			// Compute a plan using A* algorithm.
			plan = new AStarAlgorithm(vehicle, tasks).build();
			break;
		case "BFS":
			// ...
			plan = new BFS(vehicle, tasks).build(false);
			break;
			default:
				throw new AssertionError("Please provide a valid algorithm.");
		}

		// Get the timestamp when the computed plan is returned.
		long endTime = System.currentTimeMillis();

		System.out.println("Algorithm: " + algorithm);


		// The time it took to compute the plan.
		double timeTaken= (endTime - startTime) / 1000.0;

		// The cols of the plan for the current vehicle.
		long cost = (long) (vehicle.costPerKm()*plan.totalDistance());

		System.out.println("Plan computed in " + (endTime - startTime) / 1000.0 + "s");

//		String fileName =  System.getProperty("user.dir")+ "/txtFiles/"
//				+ (Integer) tasks.size() + agent.readProperty("algorithm", String.class, "BFS");
//		writeDataToCSV(fileName, cost, timeTaken);

		return plan;
	}


	public void writeDataToCSV(String csvFile, long dataItem1, double dataItem2) {
		// Writes data to a .csv file.

		FileWriter writer;

		try {
			writer = new FileWriter(csvFile, false);

			CSVWriter.writeLine(writer, Arrays.asList(
					Long.toString(dataItem1),
					Double.toString(dataItem2)
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
