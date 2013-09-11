package me.reckter.Generation;

import com.sun.javafx.geom.Vec3d;
import me.reckter.Generation.Utilities.Coordinates;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: reckter
 * Date: 9/4/13
 * Time: 10:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class Piano extends BasicGeneration {

    ArrayList<Coordinates> Pianocurve;

    public Piano() {
        Pianocurve = new ArrayList<Coordinates>();
    }

    @Override
    public void render() {

    }


	//TODO WTF!?!?!
    public void Piano(Coordinates start, Coordinates end, int iteration){
		if(start.equal(end)) {
			Pianocurve.add(start);
			return;
		}

        //Direction 1
        int step = (end.getX() - start.getX()) / 2;
        Piano(  start.add(0     ,0      ,0      ),  start.add(0      ,0      ,step  ),  iteration + 1);
	    Piano(  start.add(0     ,0      ,step   ),  start.add(0      ,step   ,step  ),  iteration + 1);
	    Piano(  start.add(0     ,step   ,step   ),  start.add(0      ,step   ,0     ),  iteration + 1);
	    Piano(  start.add(0     ,step   ,0      ),  start.add(step   ,step   ,0     ),  iteration + 1);
	    Piano(  start.add(step  ,step   ,0      ),  start.add(step   ,step   ,step  ),  iteration + 1);
	    Piano(  start.add(step  ,step   ,step   ),  start.add(step   ,0      ,step  ),  iteration + 1);
	    Piano(  start.add(step  ,0      ,step   ),  start.add(step   ,0      ,0     ),  iteration + 1);

    }
}
