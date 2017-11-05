
import java.io.IOException;

import tcdIO.Terminal;

public class Connection {
	public static void main(String[] args) throws IOException, InterruptedException {
		Terminal terminal = new Terminal("sp1");
		Terminal terminal2 = new Terminal("r1");
		Terminal terminal3 = new Terminal("r2");
		Terminal terminal4 = new Terminal("r3");
		Terminal terminal5 = new Terminal("r4");
		Terminal terminal6 = new Terminal("sp2");

		Smartphone sp1 = new Smartphone(terminal, "sp1");
		Smartphone sp2 = new Smartphone(terminal6, "sp2");

		Router r1 = new Router(terminal2, "a", 0, false);
		Router r2 = new Router(terminal3, "b", -1, false);
		Router r3 = new Router(terminal4, "c", -1, false);
		Router r4 = new Router(terminal5, "d", -1, true);
		Router[] routers = new Router[4];
		routers[0] = r1;
		routers[1] = r2;
		routers[2] = r3;
		routers[3] = r4;

		ForwardTable table1 = new ForwardTable("a", "0", r2, r3, "58000", "59000", "4", "2", r1, null, null, "-1",
				"-1");
		ForwardTable table2 = new ForwardTable("c", "0", r4, r2, "59200", "58000", "1", "5", r2, r1, null, "57000",
				"-1");
		ForwardTable table3 = new ForwardTable("b", "0", r4, r3, "59200", "59000", "10", "10", r3, r1, null, "57000",
				"-1");
		ForwardTable table4 = new ForwardTable("d", "0", r4, r3, "61000", "61000", "4", "1000", r4, r2, r3, "58000",
				"59000");
		sp1.addSmartphone(52000, 57000);
		r1.setRouterManuel(57000, table1);
		r2.setRouterManuel(58000, table2);
		r3.setRouterManuel(59000, table3);
		r4.setRouterManuel(59200, table4);

		sp2.addSmartphone(61000, 61000);

		sp1.start();
	}
}