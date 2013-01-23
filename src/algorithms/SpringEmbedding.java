package algorithms;

import graph.Graph;
import graph.Tuple;
import graph.Vertex;

import java.util.ArrayList;

import district.Groundplan;

public class SpringEmbedding {

	public final double DAMPING = 0.005;
	public final double TIMESTEP = 1;

	public void springEmbed(Graph g){
		
		ArrayList<Vertex> vertices = g.getVertices();
		Tuple total_kinetic_energy = new Tuple();
		
		for (Vertex v : vertices){
			Tuple netto_sum = new Tuple();

			/*for each other node
			 * net-force := net-force + Coulomb_repulsion( this_node, other_node )
			 * next node
			 */
			for (Vertex v_other : vertices){
				if (v_other == v)
					continue;
				getWallPosition(g, v, v_other);
				netto_sum.sum(Tuple.coulombRepulsion(v,v_other, g.distanceBetween(v, v_other)));
			}
			/*
			 *   for each spring connected to this node
			 *   net-force := net-force + Hooke_attraction( this_node, spring )
			 *   next spring
			 */
			getWallPosition(g,v.getToVertex(),v);
			netto_sum.sum((Tuple.hookeAttraction(v,v.getToVertex())));
			//v.velocity := (v.velocity + timestep * net-force) * damping
			v.getVelocity().dx = (v.getVelocity().dx + TIMESTEP * netto_sum.dx) * DAMPING;
			v.getVelocity().dy = (v.getVelocity().dy + TIMESTEP * netto_sum.dy) * DAMPING;
			//v.position := v.position + timestep * this_node.velocity
			if(v.getPlaceable()!=null)
			{
				v.getPlaceable().setX(v.getPosition().dx + (TIMESTEP * v.getVelocity().dx));
				v.getPlaceable().setY(v.getPosition().dy + (TIMESTEP * v.getVelocity().dy));
				bounce(v);
				removeStack(v, vertices);
			}
			//total_kinetic_energy := total_kinetic_energy + this_node.mass * (this_node.velocity)^2
			total_kinetic_energy.dx =  total_kinetic_energy.dx + (v.getMass() * Math.sqrt(v.getVelocity().dx));
			total_kinetic_energy.dy =  total_kinetic_energy.dy+ (v.getMass() * Math.sqrt(v.getVelocity().dy));
			
			vertices= g.setNearestNeighbours();
		
		}
	}

	private void removeStack(Vertex v,ArrayList<Vertex> vertices) {
		for(Vertex v2:vertices)
		{
			if(v==v2) continue;
			if(v.getPosition().equals(v2.getPosition()));
			{
				v.getPosition().dx++;
			}
		}
		
	}

	private void bounce(Vertex v) {
		if(v.getPlaceable().getX()<0)
			v.getPlaceable().setX(10);
		if(v.getPlaceable().getX()+v.getPlaceable().getHeight()>Groundplan.HEIGHT)
			v.getPlaceable().setX(100);
		if(v.getPlaceable().getY()<0)
			v.getPlaceable().setY(10);
		if(v.getPlaceable().getY()+v.getPlaceable().getWidth()>Groundplan.WIDTH)
			v.getPlaceable().setY(140);
	}

	private void getWallPosition(Graph g, Vertex v, Vertex v_other) {
		try {
			if(v.isfixed)
				v.setPosition(g.wallPosition(v_other));
			if(v_other.isfixed)
				v_other.setPosition(g.wallPosition(v));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}