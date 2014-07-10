package algorithms.backtracksearch;

import java.util.*;
import DataEntity.*;

public class AssignNonHustToAJuryRoom {

	/**
	 * @param args
	 */
	private Vector<JuryInfo> jury;
	private int n;// size of jury
	private int m;
	HashSet<Integer> members;
	int[] x;// size = m = 2*n, teacher assigned as examiner1 of jury i is x[2*i], teacher assigned as additional member of jury i is x[2*i+1] 
	int[] opt_x;
	Vector[] domain; // domain[i] is the domain of x[i]
	int bestEval;
	
	HashMap<Integer, Integer> occExaminer1;// occExaminer1[t] is the number of occurrences of teacher t appearing as examiner 1
	HashMap<Integer, Integer> occAddMember;// occAddMember[t] is the number of occurrences of teacher t appearing as additional members
	
	
	private double t0;
	private double timeLimit;
	private boolean foundSolution;
	
	public boolean foundSolution(){ return this.foundSolution;}
	
	private int eval(){
		int minE = 10000;
		int maxE = -minE;
		int minA = 10000;
		int maxA = -minA;
		Iterator it = members.iterator();
		while(it.hasNext()){
			int tid = (Integer)it.next();
			if(occExaminer1.get(tid) < minE) minE = occExaminer1.get(tid);
			if(occExaminer1.get(tid) > maxE) maxE = occExaminer1.get(tid);
			if(occAddMember.get(tid) < minA) minA = occAddMember.get(tid);
			if(occAddMember.get(tid) > maxA) maxA = occAddMember.get(tid);
		}
		
		return (maxE - minE + maxA - minA);
	}
	private void updateBest(){
		int e = eval();
		if(bestEval > e){
			foundSolution = true;
			System.out.println("AssignNonHustToAJuryRoom::updateBest, bestEval = " + bestEval + " e = " + e);
			bestEval = e;
			for(int j = 0; j < m; j++)
				opt_x[j] = x[j];
		}
	}
	private void TRY(int k){// try values for x[k]
		if(bestEval == 0) return;
		if(System.currentTimeMillis() - t0 > timeLimit) return;
		
		for(int j = 0; j < domain[k].size(); j++){
			int v = (Integer)domain[k].get(j);
			if(k%2==1 && x[k-1] == v) continue;
			
			x[k] = v;
			if(k%2==0){
				occExaminer1.put(v,occExaminer1.get(v)+1);
			}else{
				occAddMember.put(v, occAddMember.get(v)+1);
			}
			if(k == m-1){
				updateBest();
			}else{
				TRY(k+1);
			}
			if(k%2==0){
				occExaminer1.put(v,occExaminer1.get(v)-1);
			}else{
				occAddMember.put(v, occAddMember.get(v)-1);
			}
		}
	}
	public void assign(Vector<JuryInfo> jury, HashSet<Integer> members, double timeLimit){
		this.jury = jury;
		this.members = members;
		this.timeLimit = timeLimit*1000; //convert into milliseconds
		n = jury.size();
		m = 2*n;
		x = new int[m];
		opt_x = new int[m];
		domain = new Vector[m];
		for(int i = 0; i < m; i++){
			domain[i] = new Vector<Integer>();
			JuryInfo J = jury.get(i/2);
			if(i%2==0){
				if(J.getExaminerId1() > 0)
					domain[i].add(J.getExaminerId1());
				else{
					Iterator it = members.iterator();
					while(it.hasNext()){
						int a = (Integer)it.next();
						if(J.getSupervisorId() != a) domain[i].add(a);
					}
				}
			}else{
				if(J.getAdditionalMemberId() > 0)
					domain[i].add(J.getAdditionalMemberId());
				else{
					Iterator it = members.iterator();
					while(it.hasNext()){
						int a = (Integer)it.next();
						if(J.getSupervisorId() != a) domain[i].add(a);
					}
				}
				
			}
		}
		
		bestEval = 100000;
		occExaminer1 = new HashMap<Integer, Integer>();
		occAddMember = new HashMap<Integer, Integer>();
		Iterator it = members.iterator();
		while(it.hasNext()){
			int tid = (Integer)it.next();
			occExaminer1.put(tid, 0);
			occAddMember.put(tid, 0);
		}
		t0 = System.currentTimeMillis();
		
		foundSolution = false;
		TRY(0);
		
		System.out.println("AssignNonHustToAJuryRoom::assign --> RESULT");
		for(int i = 0; i < m; i++){
			System.out.print(opt_x[i] + ",");
		}
		System.out.println();
		if(!foundSolution) return;
		
		for(int i = 0; i < m; i++){
			JuryInfo J = jury.get(i/2);
			int tid = opt_x[i];
			if(i%2==0){
				J.setExaminerId1(tid);
			}else{
				J.setAdditionalMemberId(tid);
			}
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
