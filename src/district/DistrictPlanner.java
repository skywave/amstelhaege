package district;
import graph.Tuple;

import java.util.Random;

import algorithms.SimulatedAnnealing;


import datastructures.Charges;
import districtobjects.Bungalow;
import districtobjects.Cottage;
import districtobjects.Mansion;
import districtobjects.Residence;
import districtobjects.WaterBody;

/**
 * De wijkplanner is de basis file voor de Heuristieken opdracht Amstelhaege.
 * Het algoritme voor het oplossen van deze opdracht kan/hoort in deze class
 * geschreven te worden te beginnen in de functie planWijk.
 * 
 * @author bweel
 * 
 */
public class DistrictPlanner {
	Random random;
	GroundplanFrame frame;
	SimulatedAnnealing algorithm;
	private static final int SCALE = 1;

	public DistrictPlanner() {
		random = new Random(1);
		frame = new GroundplanFrame();
		int houses=60;

		Groundplan plan = planWijk(houses,1000);
		printSolution(plan);
		
	}

	/**
	 * Startpunt voor het oplossen van de opdracht
	 */
	public Groundplan planWijk(int houses, int iter) {
		int infeasiblesolutions=0;
		Groundplan optimalSolution=null;
		double bestsolution=0;
		Charges charges = new Charges(1,2,3,0,0);
		Groundplan currentSolution=null;
		
		algorithm = new SimulatedAnnealing(randomPlan(houses));
		optimalSolution=algorithm.getGroundplan();
		for(int i=0;i<=5;i++)
		{
			//Calc initial solution:
			currentSolution=algorithm.getOptimalSolution(iter,charges,frame);
			printSolution(currentSolution);		
			
			currentSolution = runSimulatedAnnealingChangingCharge(houses,
					infeasiblesolutions, charges, currentSolution,iter);
			if(currentSolution.isValid() && currentSolution.getPlanValue()>optimalSolution.getPlanValue())
			{
				optimalSolution=currentSolution;
				bestsolution=optimalSolution.getPlanValue();
				System.out.println("Best value: "+bestsolution);
			}
		}
		return currentSolution;
	}

	private Groundplan runSimulatedAnnealingChangingCharge(int houses,
			int infeasiblesolutions, Charges charges,
			Groundplan currentSolution,int iter) {
		
		charges = new Charges(1,1,1,0,0);
		Groundplan solution;
		while(infeasiblesolutions<=2)
		{
			//run x iterations of simulated annealing algorithm
			algorithm = new SimulatedAnnealing(randomPlan(houses));
			printSolution(currentSolution);
			
			solution= algorithm.getOptimalSolution(iter,charges,frame);
			
			printSolution(solution);
			if(!solution.isValid())
				infeasiblesolutions++;
			else infeasiblesolutions=0;
			if(solution.getPlanValue()>currentSolution.getPlanValue() && solution.isValid())
			{
				System.out.println("Value: "+solution.getPlanValue()+" Feasible: "+solution.isValid());
				currentSolution=solution;
			}
			else infeasiblesolutions++;
			//increaseCharges(charges);
		}
		return currentSolution;
	}

	private void printSolution(Groundplan solution) {
		frame.setPlan(solution);
		//System.out.println("Value: "+solution.getPlanValue()+" Feasible:"+solution.isValid());
	}

	private void increaseCharges(Charges charges) {
		double randomnumber = random.nextDouble();
		if(randomnumber<=0.33)
			charges.cottagecharge+=0.5;
		else if(randomnumber<=0.66)
			charges.bungalowcharge+=0.5;
		else
			charges.mansioncharge+=0.5;		
	}

	private Groundplan randomPlan(int houses) {
		Groundplan plan = new Groundplan(houses);
		
		for (int i = 0; i < Groundplan.MINIMUM_COTTAGE_PERCENTAGE * houses; i++) {
			plan.addResidence(new Cottage(random.nextDouble()
					* Groundplan.WIDTH, random.nextDouble() * Groundplan.HEIGHT));
		}

		for (int i = 0; i < Groundplan.MINIMUM_BUNGALOW_PERCENTAGE *houses ; i++) {
			plan.addResidence(new Bungalow(random.nextDouble()
					* Groundplan.WIDTH, random.nextDouble() * Groundplan.HEIGHT));
		}

		for (int i = 0; i < Groundplan.MINIMUM_MANSION_PERCENTAGE * houses; i++) {
			plan.addResidence(new Mansion(random.nextDouble()
					* Groundplan.WIDTH, random.nextDouble() * Groundplan.HEIGHT));
		}
		
		createWater(plan);
	
		
		//System.out.println("Value of the plan: "+plan.getPlanValue());
		return plan;
	}

	private void createWater(Groundplan plan) {
		double size = (Groundplan.WIDTH * Groundplan.HEIGHT * Groundplan.MINIMUM_WATER_PERCENTAGE)/4;
		double height =Math.sqrt(size/4);
		double width=size/height;
			plan.addWaterBody(new WaterBody(0,0, width, height));
			plan.addWaterBody(new WaterBody(Groundplan.WIDTH-width,0, width, height));
			plan.addWaterBody(new WaterBody(0,Groundplan.HEIGHT-height, width, height));
			plan.addWaterBody(new WaterBody(Groundplan.WIDTH-width,Groundplan.HEIGHT-height, width, height));
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new DistrictPlanner();
	}
}
