package link;

import java.io.Serializable;

public class ForwardTable{
	
	private String routerName;
	public String sequence;
	private Router dst1;
	private Router dst2;
	private Router sourceRouter;

	private String port1;
	private String port2;
	private String distance1;
	private String distance2;
	private Router previousRouter1;
	private Router previousRouter2;
	private String prevPort1;
	private String prevPort2;

	
	
	ForwardTable(String name, String seq, Router dstName1, Router dstName2,
			String dstPort1, String dstPort2,
			String dstDistance1, String dstDistance2, Router scr, Router prevRouter1,
			Router prevRouter2, String portPrev1, String portPrev2){
		
		routerName = name;
		sequence = seq;
		dst1 = dstName1;
		dst2 = dstName2;
		port1 = dstPort1;
		port2 = dstPort2;
		distance1 = dstDistance1;
		distance2 = dstDistance2;
		sourceRouter = scr;
		previousRouter1 = prevRouter1;
		previousRouter2 = prevRouter2;
		if(previousRouter1  == null){
			System.out.println("null");
		}else{
		System.out.println(previousRouter1.routerName);
		}
		
		prevPort1 = portPrev1;
		prevPort2 = portPrev2;

	}
	
	public String printForwardTable(){
		return "" + "ID"+":" + routerName+System.lineSeparator() + "SEQN" + ":"+ sequence + 
	System.lineSeparator() + dst1.getRouterName() + ":" + distance1 + System.lineSeparator() +
	dst2.getRouterName() + ":" + distance2 + System.lineSeparator();
	}
	
	
	public String[] forwardTableArray(){
		String[] array = new String[10];
		array[0] = routerName;
	    array[1] = sequence;
		array[2] = dst1.getRouterName();
		array[3] = dst2.getRouterName();
		array[4] = port1;
		array[5] = port2;
		array[6] = distance1;
		array[7] = distance2;
		array[8] = prevPort1;
		array[9] = prevPort2;

		
		return array;
	}
	
	public Router[] dstRouters(){
		Router[] array = new Router[5];
		array[0] = dst1;
		array[1] = dst2;
		array[2] = sourceRouter;
		array[3] = previousRouter1;
		array[4] = previousRouter2;

		return array;

	}
	
	
	

}
