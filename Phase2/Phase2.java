import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.BitSet;
import java.util.Stack;

class ListComparator implements Comparator<Loop>{
	public int compare(Loop user0, Loop user1) {
          int t1=0,t2=0;
          int length1 = user0.getblock().length,length2 = user1.getblock().length;
		  while(t1<length1&&user0.getblock()[t1]!=1){
			  t1++;
		  }
		  while(t2<length2&&user1.getblock()[t1]!=1){
			  t2++;
		  }
		  if(t1>=t2){
			  return 1;
		  } else {
			  return -1;
		  }
		 }
}




public class Phase2 {
	public static boolean isNumeric(String str){
		   for(int i=str.length();--i>=0;){
		      int chr=str.charAt(i);
		      if(chr<48 || chr>57)
		         return false;
		   }
		   return true;
		}
	
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
		List<String []> Var = new ArrayList<String []>();
		List<String []> Arr = new ArrayList<String []>();
		try {
		in = new BufferedReader(new FileReader(path));
		line = in.readLine();
		while(!line.contains(": START")){
			line = line.trim();
			if(line.contains(".")){
				if(line.contains(".[]")){
					String a[] = line.substring(3).trim().split(",");
					int length = Integer.parseInt(a[1].trim());
					String arr[] = new String [length+1];
					arr[0] = a[0];
					Arr.add(arr);
				} else {
					String name = line.substring(1).trim();
					String var[] = new String [2];
					var[0] = name;
					Var.add(var);
				}
			}
			line = in.readLine();
		}
		for(int i=0;i<Var.size();i++){
			//System.out.println(Var.get(i)[0]);
		}
		for(int i=0;i<Arr.size();i++){
			//System.out.println(Arr.get(i)[0]);
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
/*		System.out.println("");
		for(int j=0;j<list.size();j++){
			if(list.get(j).contains("BB")){
			System.out.println(list.get(j));
			} else {
				System.out.println("    " + list.get(j));
			}
		}
		System.out.println("\n");
*/		
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
		List<int []> liste = new ArrayList<int []>();
		for(int j=0;j<listb.size();j++){
			edgeOut=listb.get(j).getOut();
			if(edgeOut[0]!=-1){
//				System.out.println("EDGE "+ Integer.toString(edgeNum)+": BB"+ Integer.toString(j)+" -> BB"+ Integer.toString(edgeOut[0]) );
				int e[] = {0,j,edgeOut[0]};
				liste.add(e);
				edgeNum++;
				if(edgeOut[1]!=-1){
//					System.out.println("EDGE "+ Integer.toString(edgeNum)+": BB"+ Integer.toString(j)+" -> BB"+ Integer.toString(edgeOut[1]) );
					int E[] = {0,j,edgeOut[1]};
					liste.add(E);
					edgeNum++;
				}
			}
		}
//		System.out.println("");
		
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
		List<Loop> listl = new ArrayList<Loop>();
		for(int j=0;j<listb.size();j++){
			edgeOut=listb.get(j).getOut();
			for(int e=0;e<2;e++){
			if (edgeOut[e]!=-1){
				temp = (BitSet) listb.get(j).getDominator().clone();
     			//listb.get(j).printDomi();
				if(temp.get(edgeOut[e])==true){
					Stack <Integer> s = new Stack<Integer>();
					Loop l = new Loop(B,j,edgeOut[e]);
					List<Integer> loop = new ArrayList<Integer>();
					l.add2Loop(edgeOut[e]);
					loop.add(edgeOut[e]);
					if(edgeOut[e]!=j){
					l.add2Loop(j);
					loop.add(j);
					s.add(j);
					}
					while(!s.isEmpty()){
						int m = s.pop();
						int[] p = listb.get(m).getIn();
						int numin = 0;
						while(p[numin]!=-1){
							if(l.getblock()[p[numin]]==0){//!loop.contains(p[numin])
								//loop.add(p[numin]);
								l.add2Loop(p[numin]);
								loop.add(p[numin]);
								s.add(p[numin]);
							}
							numin++;
						}
					}
					Collections.sort(loop);
//					System.out.print("LOOP " + Integer.toString(backEdgenum) + ": ");
//					for(int t=0;t<loop.size();t++){
//						System.out.print("BB" + Integer.toString(loop.get(t)) + " ");
//					}
//					System.out.println("");
					
					listl.add(l);
					//System.out.println("BACK EDGE "+ Integer.toString(backEdgenum)+": BB"+ Integer.toString(j)+" -> BB"+ Integer.toString(edgeOut[0]) );
					backEdgenum++;
				}
			}
			}
		}
		ListComparator comparator = new ListComparator();
		Collections.sort(listl,comparator);
		
		int j=0;
		int currentb = -1;
		while(j<list.size()){
			line = list.get(j).trim();
			j++;
			if(line.contains("BB")){
				int oldb = currentb;
				currentb = Integer.parseInt(line.substring(2));
				if(oldb!=-1){
					for(int t=0;t<liste.size();t++){
						if(liste.get(t)[1] == oldb && liste.get(t)[2] == currentb){
							liste.get(t)[0]++;
						}
					}
					//for(int t=0;t<listl.size();t++){
						//if(listl.get(t).backedge()[0]==oldb && listl.get(t).backedge()[1]==currentb){
							//listl.get(t).countOne();
						//}
					//}
				}
				for(int t=0;t<listl.size();t++){
					if(listl.get(t).check(currentb)){
						if(listl.get(t).backedge()[0] == currentb){
							listl.get(t).countOne();
						}
					}
				}
				listb.get(currentb).frequp();	
			} else if(line.contains(".<")){
				String dst = line.substring(2).trim();
				System.out.println("");
				System.out.println("enter a value for " + dst);
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			    String value = br.readLine();
			    while(!isNumeric(value)){
			    	System.out.println("not an integer!");
			    	value = br.readLine();
			    }
			    for(int t=0;t<Var.size();t++){
			    	if(Var.get(t)[0].equals(dst)){
			    		Var.get(t)[1] = value;
			    	}
			    }
			} else if(line.contains(".[]<")){
				String split[] = line.substring(4).trim().split(",");
				String dst = split[0];
			    String index0 = split[1].trim();
			    int index = -1;
			    if(isNumeric(index0)){
			    	index = Integer.parseInt(index0);
			    } else {
			    	for(int t=0;t<Var.size();t++){
				    	if(Var.get(t)[0].equals(index0)){
				    		index = Integer.parseInt(Var.get(t)[1]);
				    	}
				    }
			    }
				System.out.println("");
				System.out.println("enter a value for " + dst + "[" + Integer.toString(index) + "]");
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			    String value = br.readLine();
			    while(!isNumeric(value)){
			    	System.out.println("not an integer!");
			    	value = br.readLine();
			    }
			    
			    for(int t=0;t<Arr.size();t++){
			    	if(Arr.get(t)[0].equals(dst)){
			    		Arr.get(t)[index+1] = value;
			    	}
			    }  
			} else if(line.contains(".>")){
				String src = line.substring(2).trim();
				if(isNumeric(src)){
					System.out.println(src);
				} else {
					for(int t=0;t<Var.size();t++){
						if(Var.get(t)[0].equals(src)){
							System.out.println(Var.get(t)[1]);
						}
					}
				}
			} else if(line.contains(".[]>")){
				String split[] = line.substring(4).trim().split(",");
				String src = split[0];
				String index0 = split[1].trim();
				if(isNumeric(index0)){
					for(int t=0;t<Arr.size();t++){
				    	if(Arr.get(t)[0].equals(src)){
				    		System.out.println(Arr.get(t)[Integer.parseInt(index0)+1]);
				    	}
				    }  
				} else {
					int index = -1;
					for(int t=0;t<Var.size();t++){
						if(Var.get(t)[0].equals(index0)){
							index = Integer.parseInt(Var.get(t)[1]);
						}
					}
					for(int t=0;t<Arr.size();t++){
				    	if(Arr.get(t)[0].equals(src)){
				    		System.out.println(Arr.get(t)[index+1]);
				    	}
				    }  
				}
			} else if(line.contains("+")) {
				String split[] = line.substring(1).trim().split(",");
				String dst = split[0];
				String src10 = split[1].trim();
				String src20 = split[2].trim();
				int src,src1 = 0,src2 = 0;
				if(isNumeric(src10)){
					src1 = Integer.parseInt(src10);
				} else {
					for(int t=0;t<Var.size();t++){
						if(Var.get(t)[0].equals(src10)){
							src1 = Integer.parseInt(Var.get(t)[1]);
						}
					}
				}
				if(isNumeric(src20)){
					src2 = Integer.parseInt(src20);
				} else {
					for(int t=0;t<Var.size();t++){
						if(Var.get(t)[0].equals(src20)){
							src2 = Integer.parseInt(Var.get(t)[1]);
						}
					}
				}
				src = src1 + src2;
				for(int t=0;t<Var.size();t++){
					if(Var.get(t)[0].equals(dst)){
						Var.get(t)[1] = Integer.toString(src);
					}
				}
			} else if(line.contains("-")){
				String split[] = line.substring(1).trim().split(",");
				String dst = split[0];
				String src10 = split[1].trim();
				String src20 = split[2].trim();
				int src,src1 = 0,src2 = 0;
				if(isNumeric(src10)){
					src1 = Integer.parseInt(src10);
				} else {
					for(int t=0;t<Var.size();t++){
						if(Var.get(t)[0].equals(src10)){
							src1 = Integer.parseInt(Var.get(t)[1]);
						}
					}
				}
				if(isNumeric(src20)){
					src2 = Integer.parseInt(src20);
				} else {
					for(int t=0;t<Var.size();t++){
						if(Var.get(t)[0].equals(src20)){
							src2 = Integer.parseInt(Var.get(t)[1]);
						}
					}
				}
				src = src1 - src2;
				for(int t=0;t<Var.size();t++){
					if(Var.get(t)[0].equals(dst)){
						Var.get(t)[1] = Integer.toString(src);
					}
				}
			} else if(line.contains("*")){
				String split[] = line.substring(1).trim().split(",");
				String dst = split[0];
				String src10 = split[1].trim();
				String src20 = split[2].trim();
				int src,src1 = 0,src2 = 0;
				if(isNumeric(src10)){
					src1 = Integer.parseInt(src10);
				} else {
					for(int t=0;t<Var.size();t++){
						if(Var.get(t)[0].equals(src10)){
							src1 = Integer.parseInt(Var.get(t)[1]);
						}
					}
				}
				if(isNumeric(src20)){
					src2 = Integer.parseInt(src20);
				} else {
					for(int t=0;t<Var.size();t++){
						if(Var.get(t)[0].equals(src20)){
							src2 = Integer.parseInt(Var.get(t)[1]);
						}
					}
				}
				src = src1 * src2;
				for(int t=0;t<Var.size();t++){
					if(Var.get(t)[0].equals(dst)){
						Var.get(t)[1] = Integer.toString(src);
					}
				}
			} else if(line.contains("/")){
				String split[] = line.substring(1).trim().split(",");
				String dst = split[0];
				String src10 = split[1].trim();
				String src20 = split[2].trim();
				int src,src1 = 0,src2 = 0;
				if(isNumeric(src10)){
					src1 = Integer.parseInt(src10);
				} else {
					for(int t=0;t<Var.size();t++){
						if(Var.get(t)[0].equals(src10)){
							src1 = Integer.parseInt(Var.get(t)[1]);
						}
					}
				}
				if(isNumeric(src20)){
					src2 = Integer.parseInt(src20);
				} else {
					for(int t=0;t<Var.size();t++){
						if(Var.get(t)[0].equals(src20)){
							src2 = Integer.parseInt(Var.get(t)[1]);
						}
					}
				}
				src = src1 / src2;
				for(int t=0;t<Var.size();t++){
					if(Var.get(t)[0].equals(dst)){
						Var.get(t)[1] = Integer.toString(src);
					}
				}
			} else if(line.contains("%")){
				String split[] = line.substring(1).trim().split(",");
				String dst = split[0];
				String src10 = split[1].trim();
				String src20 = split[2].trim();
				int src,src1 = 0,src2 = 0;
				if(isNumeric(src10)){
					src1 = Integer.parseInt(src10);
				} else {
					for(int t=0;t<Var.size();t++){
						if(Var.get(t)[0].equals(src10)){
							src1 = Integer.parseInt(Var.get(t)[1]);
						}
					}
				}
				if(isNumeric(src20)){
					src2 = Integer.parseInt(src20);
				} else {
					for(int t=0;t<Var.size();t++){
						if(Var.get(t)[0].equals(src20)){
							src2 = Integer.parseInt(Var.get(t)[1]);
						}
					}
				}
				src = src1 % src2;
				for(int t=0;t<Var.size();t++){
					if(Var.get(t)[0].equals(dst)){
						Var.get(t)[1] = Integer.toString(src);
					}
				}
			} else if(line.contains("||")){
				String split[] = line.substring(1).trim().split(",");
				String dst = split[0];
				String src10 = split[1].trim();
				String src20 = split[2].trim();
				int src1 = 0,src2 = 0;
				String src = "0";
				if(isNumeric(src10)){
					src1 = Integer.parseInt(src10);
				} else {
					for(int t=0;t<Var.size();t++){
						if(Var.get(t)[0].equals(src10)){
							src1 = Integer.parseInt(Var.get(t)[1]);
						}
					}
				}
				if(isNumeric(src20)){
					src2 = Integer.parseInt(src20);
				} else {
					for(int t=0;t<Var.size();t++){
						if(Var.get(t)[0].equals(src20)){
							src2 = Integer.parseInt(Var.get(t)[1]);
						}
					}
				}
				if(src1 == 0 && src2 == 0){
					src = "0";
				} else {
					src = "1";
				}
				for(int t=0;t<Var.size();t++){
					if(Var.get(t)[0].equals(dst)){
						Var.get(t)[1] = src;
					}
				}
			} else if(line.contains("&&")){
				String split[] = line.substring(1).trim().split(",");
				String dst = split[0];
				String src10 = split[1].trim();
				String src20 = split[2].trim();
				int src1 = 0,src2 = 0;
				String src = "0";
				if(isNumeric(src10)){
					src1 = Integer.parseInt(src10);
				} else {
					for(int t=0;t<Var.size();t++){
						if(Var.get(t)[0].equals(src10)){
							src1 = Integer.parseInt(Var.get(t)[1]);
						}
					}
				}
				if(isNumeric(src20)){
					src2 = Integer.parseInt(src20);
				} else {
					for(int t=0;t<Var.size();t++){
						if(Var.get(t)[0].equals(src20)){
							src2 = Integer.parseInt(Var.get(t)[1]);
						}
					}
				}
				if(src1 == 1 && src2 == 1){
					src = "1";
				} else {
					src = "0";
				}
				for(int t=0;t<Var.size();t++){
					if(Var.get(t)[0].equals(dst)){
						Var.get(t)[1] = src;
					}
				}
			} else if(line.contains("=[]")){
				String split[] = line.substring(3).trim().split(",");
				String dst = split[0];
				String src = split[1].trim();
				String value = "";
				String index0 = split[2].trim();
				int index = -1;
				if(isNumeric(index0)){
					index = Integer.parseInt(index0);
				} else {
					for(int t=0;t<Var.size();t++){
						if(Var.get(t)[0].equals(index0)){
							index = Integer.parseInt(Var.get(t)[1]);
						}
					}
				}
				for(int t=0;t<Arr.size();t++){
					if(Arr.get(t)[0].equals(src)){
						value = Arr.get(t)[index+1];
					}
				}
				for(int t=0;t<Var.size();t++){
					if(Var.get(t)[0].equals(dst)){
						Var.get(t)[1] = value;
					}
				}
			} else if(line.contains("[]=")){
				String split[] = line.substring(3).trim().split(",");
				String dst = split[0];
				String index0 = split[1].trim();
				String src = split[2].trim();
				String value = "";
				int index = -1;
				if(isNumeric(index0)){
					index = Integer.parseInt(index0);
				} else {
					for(int t=0;t<Var.size();t++){
						if(Var.get(t)[0].equals(index0)){
							index = Integer.parseInt(Var.get(t)[1]);
						}
					}
				}
				if(isNumeric(src)){
					index = Integer.parseInt(src);
				} else {
					for(int t=0;t<Var.size();t++){
						if(Var.get(t)[0].equals(src)){
							value = Var.get(t)[1];
						}
					}
				}
				for(int t=0;t<Arr.size();t++){
					if(Arr.get(t)[0].equals(dst)){
						Arr.get(t)[index+1] = value;
					}
				}
			} /////////comparison statements
			else if(line.contains("<=")){
				String split[] = line.substring(2).trim().split(",");
				String dst = split[0];
				String src10 = split[1].trim();
				String src20 = split[2].trim();
				int dst0,src1 = 0,src2 = 0;
				//src1 is a integer
				if(isNumeric(src10)){
					src1 = Integer.parseInt(src10);
				}
				//src1 is a variable 
				else{
					for (int t = 0; t < Var.size();t++){
						if(Var.get(t)[0].equals(src10)){
							src1 = Integer.parseInt(Var.get(t)[1]);
						}
					}
				}
				//src2 is a integer
				if(isNumeric(src20)){
					src2 = Integer.parseInt(src20);
				}
				//src2 is a variable
				else{
					for (int t = 0; t < Var.size();t++){
						if(Var.get(t)[0].equals(src20)){
							src2 = Integer.parseInt(Var.get(t)[1]);
						}
					}
				}
				//dst's value if src1<= src2 is true(1) 
				if (src1<=src2){
					dst0=1;
				}
				//else dst's value is false(0)
				else{
					dst0=0;
				}
				for (int t = 0; t < Var.size();t++)
					if(Var.get(t)[0].equals(dst)){
						Var.get(t)[1]=Integer.toString(dst0);
					}
			}
			
			
			
			else if(line.contains(">=")){
				String split[] = line.substring(2).trim().split(",");
				String dst = split[0];
				String src10 = split[1].trim();
				String src20 = split[2].trim();
				int dst0,src1 = 0,src2 = 0;
				if(isNumeric(src10)){
					src1 = Integer.parseInt(src10);
				}
				//src1 is a variable 
				else{
					for (int t = 0; t < Var.size();t++)
						if(Var.get(t)[0].equals(src10)){
							src1 = Integer.parseInt(Var.get(t)[1]);
						}
				}
				//src2 is a integer
				if(isNumeric(src20)){
					src2 = Integer.parseInt(src20);
				}
				//src2 is a variable
				else{
					for (int t = 0; t < Var.size();t++)
						if(Var.get(t)[0].equals(src20)){
							src2 = Integer.parseInt(Var.get(t)[1]);
						}
				}
				//dst's value if src1>= src2 is true(1) 
				if (src1 >= src2){
					dst0=1;
				}
				//else dst's value is false(0)
				else{
					dst0=0;
				}
				for (int t = 0; t < Var.size();t++)
					if(Var.get(t)[0].equals(dst)){
						Var.get(t)[1]=Integer.toString(dst0);
					}
			}
			
			else if(line.contains("==")){
				String split[] = line.substring(2).trim().split(",");
				String dst = split[0];
				String src10 = split[1].trim();
				String src20 = split[2].trim();
				int dst0,src1 = 0,src2 = 0;
				
				//src1 is a integer
				if(isNumeric(src10)){
					src1 = Integer.parseInt(src10);
				}
				//src1 is a variable 
				else{
					for (int t = 0; t < Var.size();t++)
						if(Var.get(t)[0].equals(src10)){
							src1 = Integer.parseInt(Var.get(t)[1]);
						}
				}
				//src2 is a integer
				if(isNumeric(src20)){
					src2 = Integer.parseInt(src20);
				}
				//src2 is a variable
				else{
					for (int t = 0; t < Var.size();t++)
						if(Var.get(t)[0].equals(src20)){
							src2 = Integer.parseInt(Var.get(t)[1]);
						}
				}
				//dst's value if src1== src2 is true(1) 
				if (src1 == src2){
					dst0=1;
				}
				//else dst's value is false(0)
				else{
					dst0=0;
				}
				for (int t = 0; t < Var.size();t++)
					if(Var.get(t)[0].equals(dst)){
						Var.get(t)[1]=Integer.toString(dst0);
					}
			}
			
			else if(line.contains("!=")){
				String split[] = line.substring(2).trim().split(",");
				String dst = split[0];
				String src10 = split[1].trim();
				String src20 = split[2].trim();
				int dst0,src1 = 0,src2 = 0;
				
				//src1 is a integer
				if(isNumeric(src10)){
					src1 = Integer.parseInt(src10);
				}
				//src1 is a variable 
				else{
					for (int t = 0; t < Var.size();t++)
						if(Var.get(t)[0].equals(src10)){
							src1 = Integer.parseInt(Var.get(t)[1]);
						}
				}
				//src2 is a integer
				if(isNumeric(src20)){
					src2 = Integer.parseInt(src20);
				}
				//src2 is a variable
				else{
					for (int t = 0; t < Var.size();t++)
						if(Var.get(t)[0].equals(src20)){
							src2 = Integer.parseInt(Var.get(t)[1]);
						}
				}
				//dst's value if src1<= src2 is true(1) 
				if (src1 != src2){
					dst0=1;
				}
				//else dst's value is false(0)
				else{
					dst0=0;
				}
				for (int t = 0; t < Var.size();t++)
					if(Var.get(t)[0].equals(dst)){
						Var.get(t)[1]=Integer.toString(dst0);
					}
			}
///////////////////////////////////////////////////////////////put this part at the end of code
			else if(line.contains("<")){
				String split[] = line.substring(1).trim().split(",");
				String dst = split[0];
				String src10 = split[1].trim();
				String src20 = split[2].trim();
				int dst0,src1 = 0,src2 = 0;
				if(isNumeric(src10)){
					src1 = Integer.parseInt(src10);
				}
				else{
					for (int t = 0; t < Var.size();t++)
						if(Var.get(t)[0].equals(src10)){
							src1 = Integer.parseInt(Var.get(t)[1]);
						}
				}
				if(isNumeric(src20)){
					src2 = Integer.parseInt(src20);
				}
				else{
					for (int t = 0; t < Var.size();t++)
						if(Var.get(t)[0].equals(src20)){
							src2 = Integer.parseInt(Var.get(t)[1]);
						}
				}
				if (src1<src2){
					dst0=1;
				}
				else{
					dst0=0;
				}
				for (int t = 0; t < Var.size();t++)
					if(Var.get(t)[0].equals(dst)){
						Var.get(t)[1]=Integer.toString(dst0);
					}
			}
			
			else if(line.contains(">")){
				String split[] = line.substring(1).trim().split(",");
				String dst = split[0];
				String src10 = split[1].trim();
				String src20 = split[2].trim();
				int dst0,src1 = 0,src2 = 0;
				if(isNumeric(src10)){
					src1 = Integer.parseInt(src10);
				}
				else{
					for (int t = 0; t < Var.size();t++)
						if(Var.get(t)[0].equals(src10)){
							src1 = Integer.parseInt(Var.get(t)[1]);
						}
				}
				if(isNumeric(src20)){
					src2 = Integer.parseInt(src20);
				}
				else{
					for (int t = 0; t < Var.size();t++)
						if(Var.get(t)[0].equals(src20)){
							src2 = Integer.parseInt(Var.get(t)[1]);
						}
				}
				if (src1>src2){
					dst0=1;
				}
				else{
					dst0=0;
				}
				for (int t = 0; t < Var.size();t++)
					if(Var.get(t)[0].equals(dst)){
						Var.get(t)[1]=Integer.toString(dst0);
					}
			}
///////////////////////////////////////////////////////////////put this part at the end of code
			
//branch statement
			else if (line.contains("?:=")){
				String split[] = line.substring(3).trim().split(",");
				String label = split[0].trim();
				String pred = split[1].trim();
				int pred0=-1;//predicate's value
				StringBuilder mylabel = new StringBuilder(": ");
				mylabel.append(label);//combine : and LLX, make it looks like : LL0
				String find = mylabel.toString();
				for (int t = 0; t < Var.size();t++){
					if(Var.get(t)[0].equals(pred)){
						pred0 = Integer.parseInt(Var.get(t)[1]);
					}
				}
				//if is true j jump to label's index
				if (pred0 == 1){
					j=list.indexOf(find)-1;
				}
			}
			else if (line.contains(":=")){
				StringBuilder mylabel = new StringBuilder(": ");
				String label = line.substring(2).trim();
				mylabel.append(label);//combine : and LLX, make it looks like : LL0
				String find = mylabel.toString();
				j=list.indexOf(find)-1;
				
			}
/////////////logic-op statement not
			else if (line.contains("!")){
				String split[] = line.substring(1).trim().split(",");
				String dst = split[0];
				String src10 = split[1].trim();
				int dst0,src1 = 0;
				if(isNumeric(src10)){
					src1 = Integer.parseInt(src10);
				}
				else{
					for (int t = 0; t < Var.size();t++){
						if(Var.get(t)[0].equals(src10)){
							src1 = Integer.parseInt(Var.get(t)[1]);
						}
					}
				}
				if (src1==0){
					dst0=1;
				}
				else{
					dst0=0;
				}
				for (int t = 0; t < Var.size();t++)
					if(Var.get(t)[0].equals(dst)){
						Var.get(t)[1]=Integer.toString(dst0);
					}
			}
///////////////Copy statement 
			else if (line.contains("=")){
				String split[] = line.substring(1).trim().split(",");
				String dst = split[0];
				String src10 = split[1].trim();
				int dst0,src1 = 0;
				if(isNumeric(src10)){
					src1 = Integer.parseInt(src10);
				}
				else{
					for (int t = 0; t < Var.size();t++){
						if(Var.get(t)[0].equals(src10)){
							src1 = Integer.parseInt(Var.get(t)[1]);
						}
					}
				}
				dst0=src1;
				for (int t = 0; t < Var.size();t++)
					if(Var.get(t)[0].equals(dst)){
						Var.get(t)[1]=Integer.toString(dst0);
					}
			}
		}
		System.out.println("1111");
		for(int t=0;t<listb.size();t++){
			System.out.println(listb.get(t).getFreq());
		}
		System.out.println("5555");
		for(int t=0;t<liste.size();t++){
			System.out.println(liste.get(t)[0]);
		}
		for(int t=0;t<listl.size();t++){
			System.out.println("9999");
			listl.get(t).print();
		}
}
}
