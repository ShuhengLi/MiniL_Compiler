import java.util.ArrayList;
import java.util.List;


public class Loop {
		private int backedge[] = {-1,-1};
		private int block[];
		private int total;
		List<Integer> listc = new ArrayList<Integer>();	
		private boolean onProcessing;
		
		public Loop(int number,int head, int tail){
			this.total = number;
			this.backedge[0] = head;
			this.backedge[1] = tail;
			this.block = new int[total];
			for(int i=0;i<total;i++){
				block[i] = 0;
			}
			this.onProcessing = false;
		}
		void add2Loop(int location){// if this BBm in this loop, then block[m]=1 
			block[location]=1;
		}
		void countOne(){
			int count;
			count = listc.get(listc.size()-1);
			count++;
			listc.set(listc.size()-1, count);
			
		}
		
		int[] getblock(){
			return block;
		}
		
		boolean check(int location){
			boolean oldstate;
			oldstate= onProcessing;
			if (block[location]==1){
				onProcessing = true;
				if(oldstate!= onProcessing){
					listc.add(0);
				}
			}
			else{
				onProcessing = false;
			}
			return onProcessing;
		}
		
		int[] backedge(){
			return backedge;
		}
		
		void print(){
			for(int i=0;i<listc.size();i++){
				System.out.println(listc.get(i));
			}
		}

}
