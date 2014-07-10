package algorithms.backtracksearch;

import java.util.HashSet;
import java.util.Vector;
import java.util.Iterator;

//import javax.swing.text.html.HTMLDocument.Iterator;

import DataEntity.*;
import utils.*;

public class PartitionJuriesToRoomsTeachersNotMove extends BackTrackSearchTeachersSlotsRoomsObjectiveMatch {

	protected boolean preProcessCheck = true;
	public boolean preProcessCheck(){ return this.preProcessCheck;}
	
	@Override
	protected boolean checkFeasible(int r, int c, int v, int k, int dept) {
		// TODO Auto-generated method stub
		boolean ok = super.checkFeasible(r, c, v, k, dept);
		if(!ok) return false;
		
		JuryInfo J = jury.get(r);
		
		for(int rid = 1; rid <= rooms.size(); rid++) if(rid != v){
			int[] p = J.getJuryMemberIDs();
			for(int j = 0; j < p.length; j++){
				
				if(p[j] > 0){
					//System.out.println("PartitionJuriesToRoomsTeacherNotMove::checkFeasible(" + r + "," + c + 
								//"," + v + ", occTeacherRoom[" + rid + "].get(" + p[j] + ") = " + occTeacherRoom[rid].get(p[j]));
					if(occTeacherRoom[rid].get(p[j]) > 0) return false;
				}
			}
			
		}
		
		// check if the number of hust, nonHust assigned to room v exceed the upperbound
		int var_idx = 7*r+c;
		int nbHust = 0;
		int nbNonHust = 0;
		for(int p = 0; p < 5; p++){
			if(x[r*7+p] > 0){
				int tid = x[r*7+p];
				if(p == 0 || p == 4){
					if(!nonHustOfRoom[v].contains(tid)) nbNonHust++;
				}else{
					if(!hustOfRoom[v].contains(tid)) nbHust++;
				}
			}
		}
		
		if(hustOfRoom[v].size() + nbHust > maxHustOfRoom.get(v)) ok = false;
		//if(!ok && k >= dept) System.out.println("TRY(" + k + "), var_idx = " + var_idx + ", r = " + r + ", c = " + c + ", v = " + v + 
				//", FALSE hustOfRoom[" + v + "].sz = " + hustOfRoom[v].size() + ", nbHust = " + nbHust);
		if(nonHustOfRoom[v].size() + nbNonHust > maxNonHustOfRoom.get(v)) ok = false;
		//if(!ok && k >= dept) System.out.println("TRY(" + k + "), var_idx = " + var_idx + ", r = " + r + ", c = " + c + ", v = " + v + 
				//", FALSE nonHustOfRoom[" + v + "].sz = " + nonHustOfRoom[v].size() + ", nbNonHust = " + nbNonHust);
		
		return ok;
	}

	/*
	@Override
	
	protected void TRY(int k) {
		// TODO Auto-generated method stub
		int var_idx = order[k];
		int r = var_idx/7;
		int c = var_idx%7;
		int dept = 100000;
		
		if(System.currentTimeMillis() - t0 > timeLimit) return;
		if(stop) return;
		
		if(k >= dept){
			//stop = true;
		}
		
		for(int i = 0; i < domain[var_idx].size(); i++){
			int v = (Integer)domain[var_idx].get(i);
			
			
			boolean ok = checkFeasible(r,c,v,k,dept);
			
			if(!ok) continue;
			
			x[var_idx] = v;// ASSIGN
			
			
			propagate(r,c,v,k,dept);
			
			
			if(k == nbVars-1){// instantiate nbVars necessary variables
				updateBest();
			}else{
				TRY(k+1);
			}

			// RECOVER status
			recoverWhenBackTrack(r,c,v,k,dept);
			
		}
	}
	*/
	
	/*
	@Override
	protected void propagate(int r, int c, int v, int k, int dept) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void recoverWhenBackTrack(int r, int c, int v, int k, int dept) {
		// TODO Auto-generated method stub

	}

	*/
	@Override
	protected int[] eval() {
		// TODO Auto-generated method stub
		//setObjectiveType("subject-match");
		//return super.eval();
		
		int[] e = new int[nbObj];
		int match = 0;
		for(int r = 1; r <= rooms.size(); r++){
			int d = 0;
			Vector<Integer> C = new Vector<Integer>();
			for(int i = 0; i < jury.size(); i++){
				if(x[7*i+6] == r)
					C.add(i);
			}
			for(int i = 0; i < C.size()-1; i++)
				for(int j = i+1; j < C.size(); j++)
					d += matchJury[i][j];
			
			if(match > d) match = d;
		}
		e[0] = match;
		return e;
	}
	
	private boolean checkCommon(JuryInfo J1, JuryInfo J2){
		if(J1.getSlotId() > 0 && J2.getSlotId() > 0 && J1.getSlotId() == J2.getSlotId()) return false;
		
		int[] p = J1.getJuryMemberIDs();
		HashSet<Integer> S1 = new HashSet<Integer>();
		for(int i = 0; i < p.length; i++)if(p[i] > 0) S1.add(p[i]);
		//S1.add(J1.getSupervisorId());
		
		p = J2.getJuryMemberIDs();
		HashSet<Integer> S2 = new HashSet<Integer>();
		for(int i = 0; i < p.length; i++)if(p[i] > 0) S2.add(p[i]);
		//S2.add(J2.getSupervisorId());
		
		Iterator it = S1.iterator();
		while(it.hasNext()){
			int pid = (Integer)it.next();
			if(S2.contains(pid)) return true;
		}
		
		//if(S1.contains(J2.getSupervisorId())) return true;
		//if(S2.contains(J1.getSupervisorId())) return true;
		
		
		if(J1.getRoomId() > 0 && J2.getRoomId() > 0 && J1.getRoomId() == J2.getRoomId()) return true;
		
		return false;
	}
	private void preprocessAndReorder(){
		Vector[] A = new Vector[jury.size()];
		for(int i = 0; i < jury.size(); i++)
			A[i] = new Vector<Integer>();
		
		for(int i = 0; i < jury.size()-1; i++){
			JuryInfo J1 = jury.get(i);
			for(int j = i+1; j < jury.size(); j++){
				JuryInfo J2 = jury.get(j);
				if(checkCommon(J1, J2)){
					A[i].add(j);
					A[j].add(i);
				}
				
			}
		}
		
		ConnectedComponents algo = new ConnectedComponents();
		Vector<Vector<Integer>> CC = algo.computeConnectedComponents(A);
		HashSet<Integer> assigned = new HashSet<Integer>();
		
		int maximumJuriesPerRoom = 0;
		for(int r = 1; r <= rooms.size(); r++)
			if(maxNbJuriesOfRoom.get(r) > maximumJuriesPerRoom)
				maximumJuriesPerRoom = maxNbJuriesOfRoom.get(r);
		int countRoomsHavingMoreThan4Hust = 0;
		for(int r = 1; r <= rooms.size(); r++){
			if(maxHustOfRoom.get(r) > 3) countRoomsHavingMoreThan4Hust++;
		}
		
		int countExceed = 0;
		for(int i = 0; i < CC.size(); i++){
			Vector<Integer> C = CC.get(i);
			//System.out.print("PartitionJuriesToRoomsTeachersNotMove::preprocessAndReorder, CC[" + i + "] = ");
			int r = -1;
			for(int j = 0; j < C.size(); j++){
				//System.out.print(C.get(j) + ",");
				//System.out.println();
				int v = C.get(j);
				JuryInfo J = jury.get(v);
				if(J.getRoomId() > 0){
					r = J.getRoomId();
					break;
				}
			}
			
			if(r > 0){
				if(C.size() > maxNbJuriesOfRoom.get(r)){
					preProcessCheck = false;
					retMsg = "Qua nhieu juries duoc phan trong 1 phong " + r;
					return;
				}
			}
			if(C.size() > maximumJuriesPerRoom){
				preProcessCheck = false;
				retMsg = "Qua nhieu juries duoc phan trong 1 phong";
				return;
			}
			
			HashSet<Integer> T = new HashSet<Integer>();
			for(int j = 0; j < C.size(); j++){
				JuryInfo J = jury.get(j);
				int[] p = J.getJuryMemberIDs();
				for(int j1 = 0; j1 < p.length; j1++)
					if(p[j1] > 0) T.add(p[j1]);
			}
			for(int j = 0; j < C.size(); j++){
				JuryInfo J = jury.get(j);
				if(T.contains(J.getSupervisorId())){
					if(r > 0){
						if(maxHustOfRoom.get(r) == 3){
							preProcessCheck = false;
							retMsg = "Phong " + r + " co 3 GV, nhung GV huong dan " + J.getSupervisorId() + " cua SV " + J.getStudentID() + " trung voi thanh vien hoi dong";
							return;
						}
					}else{
						countExceed++;
					}
				}
			}
			if(countExceed > countRoomsHavingMoreThan4Hust){
				preProcessCheck = false;
				retMsg = "GV duoc du kien phan cong bi trung voi GV huong dan cua hoi dong SV";
				return;
			}
			
			if(r > 0)for(int j = 0; j < C.size(); j++){
				int v = C.get(j);
				int var_idx = 7*v+6;
				if(x[var_idx] == 0){
					x[var_idx] = r;
					opt_x[var_idx] = r;
					assigned.add(var_idx);
					propagate(v,6,r,0,10000);
					JuryInfo J = jury.get(v);
					J.setRoomId(r);
					System.out.println("PartitionJuriesToRoomsTeachersNotMove::preprocessAndReorder, assigned.add(" + var_idx + "), v = " + v + ", r = " + r);
				}
			}
		}
		Vector<Integer> nonAssigned = new Vector<Integer>();
		/*
		for(int i = 0; i < jury.size(); i++){
			int var_idx = 7*i+6;
			if(!assigned.contains(var_idx))
				nonAssigned.add(var_idx);
		}
		*/
		
		for(int i = 0; i < CC.size(); i++){
			Vector<Integer> C = CC.get(i);
			for(int j = 0; j < C.size(); j++){
				int v = C.get(j);
				int var_idx = 7*v+6;
				if(!assigned.contains(var_idx)){
					nonAssigned.add(var_idx);
				}
			}
		}
		order = new int[nonAssigned.size()];
		for(int i = 0; i < nonAssigned.size(); i++){
			order[i] = nonAssigned.get(i);
		}
		setNbVariablesInstantiated(order.length);
		
		System.out.print("PartitionJuriesToRoomsTeachersNotMove::preprocessAndReorder, nbVars = " + nbVars + ", order = ");
		for(int i = 0; i < order.length; i++) System.out.print("[" + order[i] + "," + order[i]/7 + "]");
		System.out.println();
	}
	protected void initDataStructuresBeforeSearch(Vector<JuryInfo> jury, Vector<Room> rooms, Vector<Teacher> hustTeachers, Vector<Teacher> nonHustTeachers, int timeLimit){
		super.initDataStructuresBeforeSearch(jury, rooms, hustTeachers, nonHustTeachers, timeLimit);
		System.out.println("PartitionJuriesToRoomsTeacherNotMove::initDataStructuresBeforeSearch...");
		preprocessAndReorder();
		if(!preProcessCheck()){
			System.out.println("PartitionJuriesToRoomsTeacherNotMove::initDataStructuresBeforeSearch, preProcessCheck failed --> STOP search");
			stop = true;
		}
	}
	
	public void search(Vector<JuryInfo> jury, Vector<Room> rooms, Vector<Teacher> hustTeachers, Vector<Teacher> nonHustTeachers, int timeLimit){
		System.out.println("PartitionJuriesToRoomsTeachersNotMove::search...");
		super.search(jury, rooms, hustTeachers, nonHustTeachers, timeLimit);
		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
