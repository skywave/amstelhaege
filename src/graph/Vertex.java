package graph;

import districtobjects.Placeable;

/**
 * Vertex is a graph-vertex defining the nearest neighbour. 
 * If a vertex is fixed this means that object can't move, for example a wall
 * TODO 
 * @author thomas
 */

public class Vertex {
    private Placeable p;
    private boolean isfixed;
    private double velocity;
    private Vertex toVertex;
    
    public class Position{
    	public double x,y;
    	public Position(double x, double y){
    		this.x = x;
    		this.y = y;
    	}
     }
    
    public Vertex(Placeable p){
    	this(p,false);
    }
    
    public Vertex(Placeable p, boolean isfixed){
    	this.p = p;
    	this.isfixed = isfixed;
    }
    
    public Vertex(boolean isfixed){
    	this(null,true);
    }
    
    /**
     * @return the edge defining the nearest neighbour.
     */       
    public Vertex getToVertex(){
    	return toVertex;
    }
 
    /**
     * Set the nearest neighbour relation (directed)
     */
    public void setToVertex(Vertex toVertex){
    	this.toVertex = toVertex;
    }
    
    /**
     * @return Veolicty of the vertex
     */

	public double getVelocity() {
		return velocity;
	}
	
    /**
     * set the veolicity of the vertex
     */

	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}
	
	/**
	 * 
	 * @return Postion object with the position of the object.
	 * null when there is no position for example a wall.
	 * TODO Or set position of wall? When calculating distance important to consider width and length
	 */
	
	public Position getPosition(){
		if (isfixed)
			return null;
		return new Position(p.getX(),p.getY());
	}
	
	public Placeable getPlaceable(){
		return this.p;
	}
}