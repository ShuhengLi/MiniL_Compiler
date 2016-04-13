

import java.util.BitSet;

 class Block {
	
	private BitSet dominator;
	private int in[];
	private int out[];
	private int label;
	private int num;
	private int freq;
	
	public Block(int numbers,int start){
		this.num = numbers;
		this.dominator = new  BitSet(num);
		freq = 0;
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
	
	void frequp(){
		this.freq++;
	}
	
	public int getFreq(){
		return freq;
	}

}