package part1.simtrafficbase;

/**
 *
 * P2d -- modelling a point in a 2D space
 * 
 */
public record P2d(double x, double y) {

    public String toString(){
        return "P2d("+x+","+y+")";
    }

    public static double len(P2d p0, P2d p1) {
    	double dx = p0.x - p1.x;
    	double dy = p0.y - p1.y;   	
        return (double)Math.sqrt(dx*dx+dy*dy);

    }

}
