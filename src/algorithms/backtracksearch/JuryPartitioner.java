package algorithms.backtracksearch;

import java.util.*;

import DataEntity.*;

import DataEntity.JuryDataForScheduling;

public class JuryPartitioner {

	/**
	 * @param args
	 * 
	 * 
	 */
	Vector<Teacher> hustTeachers = null;
	int m;
	Vector[] domain;
	int[] x;
	int bestEval;
	int[] opt_x;
	int[] occ;// occ[v] is the number of items assigned to the bin v
	
	double t0;
	double timeLimit;
	
	public Vector<JuryDataForScheduling> partitionJury(Vector<JuryInfo> jury, Vector<Teacher> hustTeachers, Vector<Teacher> nonHustTeachers, int nbJuries, int timeLimit){
		System.out.println("JuryPartitioner::partitionJury");
		this.hustTeachers = hustTeachers;
		this.timeLimit = timeLimit*1000;
		Vector<JuryDataForScheduling> juryDataArr = new Vector<JuryDataForScheduling>();
		
		int n = jury.size();
		int sz_of_jury = n/nbJuries;
		int d = n%nbJuries;
		int sz_of_nonHust = nonHustTeachers.size()/nbJuries;
		int dNonHust = nonHustTeachers.size()%nbJuries;
		
		JuryInfo[] arrJ = new JuryInfo[n];
		for(int i = 0; i < n; i++){
			arrJ[i] = jury.get(i);
		}
		for(int i = 0; i < n-1; i++)
			for(int j = i+1; j < n; j++)
				if(arrJ[i].getSupervisorId() > arrJ[j].getSupervisorId()){
					JuryInfo jr = arrJ[i]; arrJ[i] = arrJ[j]; arrJ[j] = jr;
				}
		
		int idx = -1;
		int idxNonHust = -1;
		HashSet[] IDs = new HashSet[nbJuries];
		for(int i = 0; i < nbJuries; i++){
			IDs[i] = new HashSet<Integer>();
		}

		for(int i = 0; i < nbJuries; i++){
			Vector<JuryInfo> a = new Vector<JuryInfo>();
			for(int j = 0; j < sz_of_jury; j++){
				idx++;
				a.add(arrJ[idx]);
				IDs[i].add(arrJ[idx].getSupervisorId());
			}
			boolean addi = false;
			if(d > 0){
				idx++;
				addi = true;
				a.add(arrJ[idx]);
				IDs[i].add(arrJ[idx].getSupervisorId());
				d--;
			}
			Vector<Teacher> nonHustOfAJury = new Vector<Teacher>();
			for(int j = 0; j < sz_of_nonHust; j++){
				idxNonHust++;
				nonHustOfAJury.add(nonHustTeachers.get(idxNonHust));
			}
			if(addi && dNonHust > 0){
				idxNonHust++;
				nonHustOfAJury.add(nonHustTeachers.get(idxNonHust));
				dNonHust--;
			}
			System.out.println("JuryPartitioner::partition, nonHustOfAJury[" + i +"].sz = " + nonHustOfAJury.size());
			JuryDataForScheduling jdfs = new JuryDataForScheduling(a,new Vector<Teacher>(), nonHustOfAJury);
			juryDataArr.add(jdfs);
		}
	
		// assign hustTeachers to partitions
		m = hustTeachers.size();
		System.out.println("m = " + m);
		domain = new Vector[m];
		for(int i = 0; i < m; i++){
			domain[i] = new Vector<Integer>();
			int tid = hustTeachers.get(i).getID();
			for(int j = 0; j < nbJuries; j++){
				if(!IDs[j].contains(tid)){
					domain[i].add(j);
					System.out.println("domain[" + i + "].add " + j);
				}
			}
		}
		for(int i = 0; i < nbJuries; i++){
			Vector<JuryInfo> ji = juryDataArr.get(i).getJuryList();
			for(int j = 0; j < ji.size(); j++)
				System.out.println(ji.get(j).getSupervisorId());
			Iterator it = IDs[i].iterator();
			System.out.print("IDs = ");
			while(it.hasNext()){
				int tid = (Integer)it.next();
				System.out.print(tid + ",");
			}
			System.out.println();
			System.out.println("------------");
		}
		for(int i = 0; i < m; i++){
			int tid = hustTeachers.get(i).getID();
			System.out.print("domain[" + tid + "] = ");
			Vector<Integer> D = (Vector<Integer>)domain[i];
			for(int j = 0; j < D.size(); j++)
				System.out.print(D.get(j) + ",");
			System.out.println();
		}
		x = new int[m];
		opt_x = new int[m];
		occ = new int[nbJuries];
		for(int j = 0; j < nbJuries; j++){
			occ[j] = 0;
		}
		bestEval = 100000;
		t0 = System.currentTimeMillis();
		Try(0);
		for(int i = 0; i < m; i++){
			int jid = opt_x[i];// jury to which the (hust) teacher i is assigned
			Vector<Teacher> T = juryDataArr.get(jid).getHustTeachers();
			T.add(hustTeachers.get(i));
		}
		
		for(int i = 0; i < nbJuries; i++){
			System.out.println("Jury " + i );
			Vector<JuryInfo> ji = juryDataArr.get(i).getJuryList();
			Vector<Teacher> hT = juryDataArr.get(i).getHustTeachers();
			Vector<Teacher> nhT = juryDataArr.get(i).getNonHustTeachers();
			for(int j = 0; j < ji.size(); j++){
				System.out.println(ji.get(j).getStudentName() + "\t" + ji.get(j).getSupervisorName());
			}
			System.out.print("hust = ");
			for(int j = 0; j < hT.size(); j++){
				System.out.print(hT.get(j).getName() + ",");
			}
			System.out.println();
			System.out.print("nonhust = ");
			for(int j = 0; j < nhT.size(); j++){
				System.out.print(nhT.get(j).getName() + ",");
			}
			System.out.println();
			System.out.println("-------------------------------------------------");
		}
		return juryDataArr;
	}
	
	private int eval(){
		int min = 1000000;
		int max = -min;
		for(int j = 0; j < occ.length; j++){
			if(min > occ[j]) min = occ[j];
			if(max < occ[j]) max = occ[j];
		}
		return max-min;
	}
	private void updateBest(){
		int e = eval();
		if(bestEval > e){
			bestEval = e;
			for(int j = 0; j < m; j++)
				opt_x[j] = x[j];
			System.out.println("JuryPartitioner::updateBest, bestEval = " + bestEval);
		}
	}
	
	private void Try(int k){
		if(System.currentTimeMillis() - t0 > timeLimit) return;
		
		Vector<Integer> D = (Vector<Integer>)domain[k];
		for(int iv = 0; iv < D.size(); iv++){
			int v = (Integer)D.get(iv);
			x[k] = v;
			occ[v]++;
			if(k == m-1){
				updateBest();
			}else{
				Try(k+1);
			}
			occ[v]--;
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
