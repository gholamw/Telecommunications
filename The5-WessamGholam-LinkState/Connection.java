package link;

import java.io.IOException;

import tcdIO.Terminal;

public class Connection {
	public static void main(String[] args) throws IOException, InterruptedException{
		Terminal terminal= new Terminal("sp1");	
		Terminal terminal2= new Terminal("r1");	
		Terminal terminal3= new Terminal("r2");		
		Terminal terminal4= new Terminal("r3");
		Terminal terminal5= new Terminal("r4");		
		Terminal terminal6= new Terminal("r5");		
		Terminal terminal7= new Terminal("r6");		

		
//		private String routerName;
//		private int sequence;
//		private String dst1;
//		private String dst2;
//
//		private int port1;
//		private int port2;
//		private int distance1;
//		private int distance2;
		



		Smartphone sp1 = new Smartphone(terminal);
		Router r1 = new Router(terminal2,"a",0,false,sp1);
		Router r2 = new Router(terminal3,"b",-1,false,sp1);
		Router r3 = new Router(terminal4,"c",-1,false,sp1);
		Router r4 = new Router(terminal5,"d",-1,false,sp1);
		Router r5 = new Router(terminal6,"e",-1,false,sp1);
		Router r6 = new Router(terminal7,"f",-1,true,sp1);

		
		ForwardTable table1 = new ForwardTable("a","0",r2,r3,"58000","59000","4","2", r1,
				null,null, "-1","-1");
		ForwardTable table2 = new ForwardTable("c","0",r5,r4,"58100","59200","1","5", r2,
				r1,null, "57000", "-1");
		ForwardTable table3 = new ForwardTable("b","0",r5,r4,"58100","59200","1","10", r3,
				r1,null,"57000","-1");
		ForwardTable table4 = new ForwardTable("d","0",r6, r4,"60000","-1","4","1000", r4,
				r3,r2,"59000","58000");
		ForwardTable table5 = new ForwardTable("e","0",r6, r5,"60000","-1","4","1000", r5,
				r2,r3,"58000","59000");
		ForwardTable table6 = new ForwardTable("f","0",r6, r6,"60000","60000","4","1000", r6,
				r1,r2,"57000","58000");

		System.out.println(table1.printForwardTable());

		sp1.addSmartphone(52000, 57000);
		r1.setRouterManuel(57000,table1);
		r2.setRouterManuel(58000,table2);
		r3.setRouterManuel(59000,table3);
		r4.setRouterManuel(59200,table4);
		r5.setRouterManuel(58100,table5);
		r6.setRouterManuel(60000,table6);


		sp1.start();
		sp1.start();



		System.out.println("Program is done");
		//boolean isconnected = r1.connect(58000);
		//terminal3.print(""+isconnected);
		//isconnected = r2.connect(59000);
		//terminal4.print(""+isconnected);



		
		
		//r1.connect();
		//System.out.println("Connected");
		//sp1.connect(r2.scr_port);


		//sp2.setPhone(57000);
		//sp1.connect(55000);
	}

}
