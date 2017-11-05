package link;

import java.io.Serializable;
import java.util.Arrays;

public class RoutingTable  implements Serializable {
	public double[][] array;
	
	RoutingTable(){		
		array = new double[7][7];
		//array[0][0] = Double.POSITIVE_INFINITY;



	}
	
// Uncomment when nessesaary 	
//	public void run() {
//		array = new double[7][7];
//
//		for(int i=0; i<array.length; i++){
//			for(int j=0; j<array.length; i++){
//				array[i][j] = Double.POSITIVE_INFINITY;
//			}
//		}
//	}
}

