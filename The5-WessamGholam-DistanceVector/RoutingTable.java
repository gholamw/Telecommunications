
//package link;

import java.io.Serializable;
import java.util.Arrays;

public class RoutingTable implements Serializable {
	public String[][] array;

	RoutingTable() {
		array = new String[5][4];
		// array[0][0] = Double.POSITIVE_INFINITY;
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[0].length; j++) {
				array[i][j] = "∞";
			}
		}

	}
}
