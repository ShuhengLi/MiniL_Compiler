

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.BitSet;
import java.util.Stack;

 class Block {
	
	private BitSet dominator;
	private int in[];
	private int out[];
	private int label;
	private int num;
	
	public Block(int numbers,int start){
		this.num = numbers;
		dominator = new  BitSet(num);
		in = new int [num-1];
		out = new int[2];
		label = start;
		if (start ==0){
			dominator.set(0);
			out[0]=-1;
			out[1]=-1;
			for (int i=1; i< num ; i++)
			{
				dominator.clear(i);
				}
			for (int i=0; i< num-1 ; i++)
			{
				this.in[i]=-1;
				}
		}
		else{
			out[0]=-1;
			out[1]=-1;
			for (int i=0; i< num ; i++)
			{
				dominator.set(i);
				}
			for (int i=0; i< num-1 ; i++)
			{
				this.in[i]=-1;
				}
		}
	}
	
	BitSet getDominator(){
		return dominator;
	}
	
	void setDominator(BitSet set){
		this.dominator= (BitSet) set.clone();
	}
	
	public void addOut(int edge){
		if ( out[0]==-1){
			out[0]=edge;
		}
		else{
			if(edge<out[0]){
				out[1]=out[0];
				out[0]=edge;
			} else {
				out[1] = edge;
			}
		}
	}
	
	public void addIn(int edge){
			int i=0;
			while(in[i]!=-1){
				i++;
			}
			this.in[i]=edge;
		}
	public void printOut(){
		if (out[0]==-1){
			System.out.println("=========== Block " + Integer.toString(label) +" is no OUT ============\n");
		}
		else {
			if(out[1]==-1){
				System.out.println("=========== Block " + Integer.toString(label) + " have only one OUT is " + Integer.toString(out[0])+ " ================");
			}
			if(out[1]!=-1){
				System.out.println("=========== Block " + Integer.toString(label) + " have two OUT,  " + Integer.toString(out[0]) + " and" + Integer.toString(out[1])+ " ================");
			}
		}
		
	}
	public void printIn(){
		int i=0;
		if (in[0]==-1){
			System.out.println("=========== Block " + Integer.toString(label) +" is no in ============");
		}
		else{
			System.out.print("=========== Block" + Integer.toString(label) + "'s IN are "  );
			while(in[i]!=-1){
				System.out.print("  " + Integer.toString(in[i]) + " and ");
				i++;
			}
			System.out.print("===========\n");
		}
	}
	
	void printDomi(){
		boolean flag;
		System.out.print("=========== Block " + Integer.toString(label) +"'s Dominator is ");
		for(int i=0;i < num;i++){
			flag=this.dominator.get(i);
			if (flag==true){
				System.out.print("1 ");
			}
			else{
				System.out.print("0 ");
			}
		}
		System.out.print(" ===========\n");
	}
	
	public int[] getIn(){
		return in;
	}
	
	public int[] getOut(){
		return out;
	}

}

public class Phase1 {
	public static void main(String[] args) throws IOException {
		String path;
		if(args.length==0){
		System.out.println("");
	    System.out.println("Enter the path where the input file is: (e.g. /tmp/test1.mil or D:/test1.mil)");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	    path = br.readLine();
		} else {
			path = args[0];
		}
		List<String> list = new ArrayList<String>();
		BufferedReader in = null;
		String line = null;
		int B = 1;
		try {
		in = new BufferedReader(new FileReader(path));
		line = in.readLine();
		while(!line.contains(": START")){
			line = in.readLine();
		}
		list.add("BB0");
		while (line!= null) {
			list.add(line);
			if(line.contains(":=")){
				list.add("BB");
				B++;
			} 
			line = in.readLine();
		}
		} finally {
		if (in != null) {
		in.close();
		}
		}
		int i=0;
		while(i<list.size()){
			line = list.get(i);
			if(line.contains(":=")){
			if(line.contains("?:=")){
					String b = ": " + line.substring(line.indexOf("L"),line.indexOf(","));
					//System.out.print(b);
					int index = list.indexOf(b);
					String line2 = list.get(index-1);
					if(!line2.contains("BB")){
						list.add(list.indexOf(b),"BB");
						B++;
						if(index<i){
							i++;
						}
					}
			} else {
				String b = ": " + line.substring(line.indexOf("L"));
				int index = list.indexOf(b);
				String line2 = list.get(index-1);
				if(!line2.contains("BB")){
					list.add(list.indexOf(b),"BB");
					B++;
					if(index<i){
						i++;
					}
				}
			}
			}
			i++;
		}
	
		
		//////find leaders and blocks
		List<Integer> leaders = new ArrayList<Integer>();
		leaders.add(0);
		for(int j=1;j<list.size();j++){
			line = list.get(j);
			if(line.contains("BB")){
				list.set(j, "BB" + Integer.toString(leaders.size()));
				leaders.add(j);
			} else {
				list.set(j,line);
			}
		}
		
		List<Block> listb = new ArrayList<Block>();
		listb.add(new Block(B,0));
		for(int j=1;j<B;j++){
			listb.add(new Block(B,j));
		}
		
	    /////print code
		System.out.println("");
		for(int j=0;j<list.size();j++){
			if(list.get(j).contains("BB")){
			System.out.println(list.get(j));
			} else {
				System.out.println("    " + list.get(j));
			}
		}
		System.out.println("\n");
		
		/////////find edges
		for(int j=1;j<leaders.size();j++){
			int index = leaders.get(j);
			line = list.get(index-1);
			if(line.contains(":=")){
				if(line.contains("?:=")){
					String b = ": " + line.substring(line.indexOf("L"),line.indexOf(","));
					int index2 = list.indexOf(b)-1;
					int out = leaders.indexOf(index2);
					listb.get(j-1).addOut(out);
					listb.get(j-1).addOut(j);
					listb.get(out).addIn(j-1);
					listb.get(j).addIn(j-1);
				} else {
					String b = ": " + line.substring(line.indexOf("L"));
					int index2 = list.indexOf(b)-1;
					int out = leaders.indexOf(index2);
					listb.get(j-1).addOut(out);
					listb.get(out).addIn(j-1);
				} 
			}else {
					listb.get(j-1).addOut(j);
					listb.get(j).addIn(j-1);	
			}
		}
		line = list.get(list.size()-1);
		if(line.contains(":=")){
			String b;
			if(line.contains("?:=")){
				b = ": " + line.substring(line.indexOf("L"),line.indexOf(","));
			} else {
				b = ": " + line.substring(line.indexOf("L"));
			}
			int index2 = list.indexOf(b)-1;
			int out = leaders.indexOf(index2);
			listb.get(listb.size()-1).addOut(out);
			listb.get(out).addIn(listb.size()-1);
		} 
		
		//for(int j=0;j<listb.size();j++){
		//listb.get(j).printIn();
		//listb.get(j).printOut();
	//}
		
/////////////find in and Print EDGE
		int edgeOut[];
		int edgeNum=0;
		for(int j=0;j<listb.size();j++){
			edgeOut=listb.get(j).getOut();
			if(edgeOut[0]!=-1){
				System.out.println("EDGE "+ Integer.toString(edgeNum)+": BB"+ Integer.toString(j)+" -> BB"+ Integer.toString(edgeOut[0]) );
				edgeNum++;
			}
			if(edgeOut[1]!=-1){
				System.out.println("EDGE "+ Integer.toString(edgeNum)+": BB"+ Integer.toString(j)+" -> BB"+ Integer.toString(edgeOut[1]) );
				edgeNum++;
			}
		}
		System.out.println("");
		
///////////find out and compute dominator
		int edgeIn[];
		BitSet temp ;
		BitSet temp1;
		BitSet old = null ;
		boolean change=true;
		
		while (change){
			change=false;
			for(int j=1;j<listb.size();j++){
				int numIn=0;
				edgeIn=listb.get(j).getIn();
				old = (BitSet) listb.get(j).getDominator().clone();
				while(edgeIn[numIn]!=-1){//count how many edges in this node
					numIn++;
				}

				int k=0;
			    temp =(BitSet) listb.get(edgeIn[0]).getDominator().clone();
				while(k < numIn-1){
					k++;
					temp1 =(BitSet) listb.get(edgeIn[k]).getDominator().clone();
					temp.and(temp1);
				}
				temp.set(j);
				listb.get(j).setDominator(temp);
				//temp1 =(BitSet) listb.get(j).getDominator();
				//listb.get(j).printDomi();
				if(old.equals(temp)==false){
					change=true;
				}
		}
		}

////////// find back edge and loop
		int backEdgenum=0;
		for(int j=0;j<listb.size();j++){
			edgeOut=listb.get(j).getOut();
			for(int e=0;e<2;e++){
			if (edgeOut[e]!=-1){
				temp = (BitSet) listb.get(j).getDominator().clone();
     			//listb.get(j).printDomi();
				if(temp.get(edgeOut[e])==true){
					Stack <Integer> s = new Stack<Integer>();
					List<Integer> loop = new ArrayList<Integer>();
					loop.add(edgeOut[e]);
					if(edgeOut[e]!=j){
					loop.add(j);
					s.add(j);
					}
					while(!s.isEmpty()){
						int m = s.pop();
						int[] p = listb.get(m).getIn();
						int numin = 0;
						while(p[numin]!=-1){
							if(!loop.contains(p[numin])){
								loop.add(p[numin]);
								s.add(p[numin]);
							}
							numin++;
						}
					}
					Collections.sort(loop);
					System.out.print("LOOP " + Integer.toString(backEdgenum) + ": ");
					for(int t=0;t<loop.size();t++){
						System.out.print("BB" + Integer.toString(loop.get(t)) + " ");
					}
					System.out.println("");
					//System.out.println("BACK EDGE "+ Integer.toString(backEdgenum)+": BB"+ Integer.toString(j)+" -> BB"+ Integer.toString(edgeOut[0]) );
					backEdgenum++;
				}
			}
			}
	}
}
}