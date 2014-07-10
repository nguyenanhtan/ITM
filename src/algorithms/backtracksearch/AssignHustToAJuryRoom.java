package algorithms.backtracksearch;

import java.util.*;
import DataEntity.*;

public class AssignHustToAJuryRoom {

	/**
	 * @param args
	 */
	private Vector<JuryInfo> jury;
	private int n;// size of jury
	private int m;
	HashSet<Integer> members;
	int[] x;// size = m = 3*n, teacher assigned as examiner2 of jury i is x[2*i], 
			//teacher assigned as president of jury i is x[2*i+1]
			//teacher assigned as secretary of jury i is x[2*i+2]
	int[] opt_x;
	Vector[] domain; // domain[i] is the domain of x[i]
	int bestEval;
	
	HashMap<Integer, Integer> occExaminer2;// occExaminer2[t] is the number of occurrences of teacher t appearing as examiner 2
	HashMap<Integer, Integer> occPresident;// occPresident[t] is the number of occurrences of teacher t appearing as presidents
	HashMap<Integer, Integer> occSecretary;// occSecretary[t] is the number of occurrences of teacher t appearing as secretary
	
	
	private double t0;
	private double timeLimit;
	private boolean foundSolution;
	
	public boolean foundSolution(){ return this.foundSolution;}
	private int eval(){
		int minE = 10000;
		int maxE = -minE;
		int minP = 10000;
		int maxP = -minP;
		int minS = 10000;
		int maxS = -minS;
		Iterator it = members.iterator();
		while(it.hasNext()){
			int tid = (Integer)it.next();
			if(occExaminer2.get(tid) < minE) minE = occExaminer2.get(tid);
			if(occExaminer2.get(tid) > maxE) maxE = occExaminer2.get(tid);
			if(occPresident.get(tid) < minP) minP = occPresident.get(tid);
			if(occPresident.get(tid) > maxP) maxP = occPresident.get(tid);
			if(occSecretary.get(tid) < minS) minS = occSecretary.get(tid);
			if(occSecretary.get(tid) > maxS) maxS = occSecretary.get(tid);
		}
		
		return (maxE - minE + maxP - minP + maxS - minS);
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
			if(k%3==1 && x[k-1] == v) continue;
			if(k%3==2 && (x[k-1] == v || x[k-2] == v)) continue;
			
			x[k] = v;
			if(k%3==0){
				occExaminer2.put(v,occExaminer2.get(v)+1);
			}else if (k%3 == 1){
				occPresident.put(v, occPresident.get(v)+1);
			}else{
				occSecretary.put(v, occSecretary.get(v)+1);
			}
			if(k == m-1){
				updateBest();
			}else{
				TRY(k+1);
			}
			if(k%3==0){
				occExaminer2.put(v,occExaminer2.get(v)-1);
			}else if (k%3 == 1){
				occPresident.put(v, occPresident.get(v)-1);
			}else{
				occSecretary.put(v, occSecretary.get(v)-1);
			}
		}
	}
	public void assign(Vector<JuryInfo> jury, HashSet<Integer> members, double timeLimit){
		this.jury = jury;
		this.members = members;
		this.timeLimit = timeLimit*1000; //convert into milliseconds
		n = jury.size();
		m = 3*n;
		x = new int[m];
		opt_x = new int[m];
		domain = new Vector[m];
		for(int i = 0; i < m; i++){
			domain[i] = new Vector<Integer>();
			JuryInfo J = jury.get(i/3);
			if(i%3==0){
				if(J.getExaminerId2() > 0)
					domain[i].add(J.getExaminerId2());
				else{
					Iterator it = members.iterator();
					while(it.hasNext()){
						int a = (Integer)it.next();
						if(J.getSupervisorId() != a) domain[i].add(a);
					}
				}
			}else if(i%3==1){
				if(J.getPresidentId() > 0)
					domain[i].add(J.getPresidentId());
				else{
					Iterator it = members.iterator();
					while(it.hasNext()){
						int a = (Integer)it.next();
						if(J.getSupervisorId() != a) domain[i].add(a);
					}
				}
				
			}else{
				if(J.getSecretaryId() > 0)
					domain[i].add(J.getSecretaryId());
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
		occExaminer2 = new HashMap<Integer, Integer>();
		occPresident = new HashMap<Integer, Integer>();
		occSecretary = new HashMap<Integer, Integer>();
		Iterator it = members.iterator();
		while(it.hasNext()){
			int tid = (Integer)it.next();
			occExaminer2.put(tid, 0);
			occPresident.put(tid, 0);
			occSecretary.put(tid, 0);
		}
		t0 = System.currentTimeMillis();
		
		foundSolution = false;
		TRY(0);
		
		System.out.println("AssignHustToAJuryRoom::assign --> RESULT(" + foundSolution + ")");
		for(int i = 0; i < m; i++){
			System.out.print(opt_x[i] + ",");
		}
		System.out.println();
		if(!foundSolution) return;
		
		for(int i = 0; i < m; i++){
			JuryInfo J = jury.get(i/3);
			int tid = opt_x[i];
			if(i%3==0){
				J.setExaminerId2(tid);
			}else if(i%3==1){
				J.setPresidentId(tid);
			}else{
				J.setSecretaryId(tid);
			}
		}
		
		/*
		for(int i = 0; i < jury.size(); i++){
			JuryInfo J = jury.get(i);
			J.setSlotId(i+1);
			
		}
		*/
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
