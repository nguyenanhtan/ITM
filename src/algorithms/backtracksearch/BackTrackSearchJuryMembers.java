package algorithms.backtracksearch;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;
import java.util.HashSet;
import java.util.Iterator;

import algorithms.localsearch.AssignRooms;

import DataEntity.*;
import java.util.HashMap;

public class BackTrackSearchJuryMembers {

	/**
	 * @param args
	 */

	private int sz_jury;
	private int sz_non_hust_members;
	private int sz_hust_members;
	private int[] v_examiner1;
	private int[] v_examiner2;
	private int[] v_president;
	private int[] v_secretary;
	private int[] v_add_member;
	
	private int[] sol_examiner2;
	private int[] sol_examiner1;
	private int[] sol_president;
	private int[] sol_secretary;
	private int[] sol_add_member;
	private boolean foundSolution;
	private boolean stop;
	
	private Vector<Integer> nonHustMembers;
	private Vector<Integer> hustMembers;
	private Vector<JuryInfo> jury;
	private Vector<Teacher> teachers;
	
	private HashSet<Integer>[] domain_examiner1;
	private HashSet<Integer>[] domain_examiner2;
	private HashSet<Integer>[] domain_president;
	private HashSet<Integer>[] domain_secretary;
	private HashSet<Integer>[] domain_add_member;
	
	private HashMap<Integer, Integer> occ;//occ[t] is the number of occurrences of teacher t in the juries
	private int maxOcc; //maximum occurrences of each teacher
	private HashMap<Integer, Integer>[] count;//count[r][t] is the number of occurrences of t in row r
	
	int obj0;
	int obj1;
	int obj2;
	int minObj0;
	int minObj1;
	int minObj2;
	
	private String retMsg = "Khong tim thay loi giai";
	double t0;
	double timeLimit;
	
	public void search(Vector<JuryInfo> jury, Vector<Integer> juryMembers, Vector<Teacher> teachers, int timeLimit){
		this.jury = jury;
		this.teachers = teachers;
		this.timeLimit = timeLimit*1000;
		
		for(int i = 0; i < jury.size(); i++){
			JuryInfo jr = jury.get(i);
			
			System.out.println("jury " + i + ": " + jr.getSupervisorId() + "\t" +
			jr.getExaminerId1() + "\t" + jr.getExaminerId2() + "\t" + jr.getPresidentId() + "\t" +
					jr.getSecretaryId() + "\t" + jr.getAdditionalMemberId());
		}
		
		HashMap<Integer, Teacher> mTeacher = new HashMap<Integer, Teacher>();
		for(int i = 0; i < teachers.size(); i++){
			Teacher t = teachers.get(i);
			mTeacher.put(t.getID(), t);
		}
		nonHustMembers = new Vector<Integer>();
		hustMembers = new Vector<Integer>();
		
		for(int i = 0; i < juryMembers.size(); i++){
			Teacher t = mTeacher.get(juryMembers.get(i));
			System.out.println(t.getID() + "\t" + t.getInstitute());
			if(t.getInstitute().equals("HUST"))
				hustMembers.add(t.getID());
			else
				nonHustMembers.add(t.getID());
		}
		
		if(nonHustMembers.size() < 2){
			retMsg += "\nChua du 2 thanh vien ngoai truong DHBK\n hien tai moi chi co " + nonHustMembers.size() + " thanh vien:\n ";
			for(int i = 0; i < nonHustMembers.size(); i++){
				int t_id = nonHustMembers.get(i);
				Teacher t = mTeacher.get(t_id);
				retMsg += t.getName() + "(" + t.getInstitute() + ")\n";
			}
			return;
		}
		if(hustMembers.size() < 3){
			retMsg += "\nCan it nhat 3 thanh vien truong DHBK\n hien tai chi co " + hustMembers.size() + " thanh vien:\n ";
			for(int i = 0; i < hustMembers.size(); i++){
				int t_id = hustMembers.get(i);
				Teacher t = mTeacher.get(t_id);
				retMsg += t.getName() + "(" + t.getInstitute() + ")\n";
			}
			return;
		}
		domain_examiner2 = new HashSet[jury.size()];
		domain_president = new HashSet[jury.size()];
		domain_secretary = new HashSet[jury.size()];
		count = new HashMap[jury.size()];
		
		for(int i = 0; i < jury.size(); i++){
			JuryInfo jr = jury.get(i);
			int supervisorID = jr.getSupervisorId();
			
			domain_examiner2[i] = new HashSet<Integer>();
			domain_president[i] = new HashSet<Integer>();
			domain_secretary[i] = new HashSet<Integer>();
			
			count[i] = new HashMap<Integer, Integer>();
			
			for(int j = 0; j < hustMembers.size(); j++){
				int tid = hustMembers.get(j);
				if(tid != supervisorID){
					domain_examiner2[i].add(tid);
					domain_president[i].add(tid);
					domain_secretary[i].add(tid);
				}
					
			}
			/*
			System.out.println("domain_examiner2 = ");
			Iterator it = domain_examiner2[i].iterator();
			while(it.hasNext()){
				System.out.print((Integer)it.next() + " ");
			}
			System.out.println();
			System.out.println("domain_president = ");
			it = domain_president[i].iterator();
			while(it.hasNext()){
				System.out.print((Integer)it.next() + " ");
			}
			System.out.println();
			System.out.println("domain_secretary = ");
			it = domain_secretary[i].iterator();
			while(it.hasNext()){
				System.out.print((Integer)it.next() + " ");
			}
			System.out.println();
			*/
		}
		
		
		
		//System.out.print("nonHust = ");
		//for(int i = 0; i < nonHustMembers.size(); i++){
			//System.out.print(nonHustMembers.get(i) + " ");
		//}
		//System.out.println();
		//System.out.print("Hust = ");
		//for(int i = 0; i < hustMembers.size(); i++){
			//System.out.print(hustMembers.get(i) + " ");
		//}
		//System.out.println();
		
		foundSolution = false;
		stop = false;
		minObj1 = 0;
		initSearch();
		
		System.out.println("Start Search....");
		obj0 = 100000;
		obj1 = 100000;
		obj2 = 100000;
		t0 = System.currentTimeMillis();
		TRY(0);
		
		
		for(int i = 0; i < jury.size(); i++){
			v_examiner2[i] = sol_examiner2[i];
			v_president[i] = sol_president[i];
			v_secretary[i] = sol_secretary[i];
		}
		
		System.out.println("Solution = " + foundSolution);
		for(int i = 0; i < jury.size(); i++){
			System.out.println(v_examiner1[i] + "\t" + v_examiner2[i] + "\t" + v_president[i] + "\t" + v_secretary[i] + "\t" + v_add_member[i]);
			
			JuryInfo jr = jury.get(i);
			jr.setSlotId(i+1);
			
			jr.setExaminerId1(v_examiner1[i]);
			jr.setExaminerName1(mTeacher.get(v_examiner1[i]).getName());

			jr.setExaminerId2(v_examiner2[i]);
			jr.setExaminerName2(mTeacher.get(v_examiner2[i]).getName());
			
			jr.setPresidentId(v_president[i]);
			jr.setPresidentName(mTeacher.get(v_president[i]).getName());
			
			jr.setSecretaryId(v_secretary[i]);
			jr.setSecretaryName(mTeacher.get(v_secretary[i]).getName());
			
			jr.setAdditionalMemberId(v_add_member[i]);
			jr.setAdditionalMemberName(mTeacher.get(v_add_member[i]).getName());
		}
		
	}

	public void searchPartial(Vector<JuryInfo> jury, Vector<Integer> juryMembers, Vector<Teacher> teachers, int timeLimit){
		this.jury = jury;
		this.teachers = teachers;
		this.timeLimit = timeLimit*1000;
		
		for(int i = 0; i < jury.size(); i++){
			JuryInfo jr = jury.get(i);
			
			System.out.println("jury " + i + ": " + jr.getSupervisorId() + "\t" +
			jr.getExaminerId1() + "\t" + jr.getExaminerId2() + "\t" + jr.getPresidentId() + "\t" +
					jr.getSecretaryId() + "\t" + jr.getAdditionalMemberId() + "\t slot -- " + jr.getSlotId());
		}
		
		HashMap<Integer, Teacher> mTeacher = new HashMap<Integer, Teacher>();
		for(int i = 0; i < teachers.size(); i++){
			Teacher t = teachers.get(i);
			mTeacher.put(t.getID(), t);
		}
		nonHustMembers = new Vector<Integer>();
		hustMembers = new Vector<Integer>();
		
		for(int i = 0; i < juryMembers.size(); i++){
			Teacher t = mTeacher.get(juryMembers.get(i));
			System.out.println(t.getID() + "\t" + t.getInstitute());
			if(t.getInstitute().equals("HUST"))
				hustMembers.add(t.getID());
			else
				nonHustMembers.add(t.getID());
		}
		
		if(nonHustMembers.size() < 2){
			retMsg += "\nChua du 2 thanh vien ngoai truong DHBK\n hien tai moi chi co " + nonHustMembers.size() + " thanh vien:\n ";
			for(int i = 0; i < nonHustMembers.size(); i++){
				int t_id = nonHustMembers.get(i);
				Teacher t = mTeacher.get(t_id);
				retMsg += t.getName() + "(" + t.getInstitute() + ")\n";
			}
			return;
		}
		if(hustMembers.size() < 3){
			retMsg += "\nCan it nhat 3 thanh vien truong DHBK\n hien tai chi co " + hustMembers.size() + " thanh vien:\n ";
			for(int i = 0; i < hustMembers.size(); i++){
				int t_id = hustMembers.get(i);
				Teacher t = mTeacher.get(t_id);
				retMsg += t.getName() + "(" + t.getInstitute() + ")\n";
			}
			return;
		}
		domain_examiner1 = new HashSet[jury.size()];
		domain_examiner2 = new HashSet[jury.size()];
		domain_president = new HashSet[jury.size()];
		domain_secretary = new HashSet[jury.size()];
		domain_add_member = new HashSet[jury.size()];
		
		count = new HashMap[jury.size()];
		
		for(int i = 0; i < jury.size(); i++){
			JuryInfo jr = jury.get(i);
			int supervisorID = jr.getSupervisorId();
			
			domain_examiner1[i] = new HashSet<Integer>();
			domain_examiner2[i] = new HashSet<Integer>();
			domain_president[i] = new HashSet<Integer>();
			domain_secretary[i] = new HashSet<Integer>();
			domain_add_member[i] = new HashSet<Integer>();
			
			count[i] = new HashMap<Integer, Integer>();
			
			if(jr.getExaminerId1() > 0){
				domain_examiner1[i].add(jr.getExaminerId1());
			}else{
				for(int j = 0; j < nonHustMembers.size(); j++){
					int tid = nonHustMembers.get(j);
					if(tid != supervisorID){
						domain_examiner1[i].add(tid);
					}
				}		
			}
			if(jr.getExaminerId2() > 0){
				domain_examiner2[i].add(jr.getExaminerId2());
			}else{
				for(int j = 0; j < hustMembers.size(); j++){
					int tid = hustMembers.get(j);
					if(tid != supervisorID){
						domain_examiner2[i].add(tid);
					}
				}		
			}
			if(jr.getPresidentId() > 0){
				domain_president[i].add(jr.getPresidentId());
			}else{
				for(int j = 0; j < hustMembers.size(); j++){
					int tid = hustMembers.get(j);
					if(tid != supervisorID){
						domain_president[i].add(tid);
					}
				}		
			}
			if(jr.getSecretaryId() > 0){
				domain_secretary[i].add(jr.getSecretaryId());
			}else{
				for(int j = 0; j < hustMembers.size(); j++){
					int tid = hustMembers.get(j);
					if(tid != supervisorID){
						domain_secretary[i].add(tid);
					}
				}		
			}
			if(jr.getAdditionalMemberId() > 0){
				domain_add_member[i].add(jr.getAdditionalMemberId());
			}else{
				for(int j = 0; j < nonHustMembers.size(); j++){
					int tid = nonHustMembers.get(j);
					if(tid != supervisorID){
						domain_add_member[i].add(tid);
					}
				}		
			}
			
			
			System.out.print("domain_examiner1[" + i + "] = ");
			Iterator it = domain_examiner1[i].iterator();
			while(it.hasNext()){
				System.out.print((Integer)it.next() + " ");
			}
			System.out.println();
			System.out.print("domain_examiner2[" + i + "] = ");
			it = domain_examiner2[i].iterator();
			while(it.hasNext()){
				System.out.print((Integer)it.next() + " ");
			}
			System.out.println();
			System.out.print("domain_president[" + i + "] = ");
			it = domain_president[i].iterator();
			while(it.hasNext()){
				System.out.print((Integer)it.next() + " ");
			}
			System.out.println();
			System.out.print("domain_secretary[" + i + "] = ");
			it = domain_secretary[i].iterator();
			while(it.hasNext()){
				System.out.print((Integer)it.next() + " ");
			}
			System.out.println();
			System.out.print("domain_add_member[" + i + "] = ");
			it = domain_add_member[i].iterator();
			while(it.hasNext()){
				System.out.print((Integer)it.next() + " ");
			}

		}
		
		
		
		//System.out.print("nonHust = ");
		//for(int i = 0; i < nonHustMembers.size(); i++){
			//System.out.print(nonHustMembers.get(i) + " ");
		//}
		//System.out.println();
		//System.out.print("Hust = ");
		//for(int i = 0; i < hustMembers.size(); i++){
			//System.out.print(hustMembers.get(i) + " ");
		//}
		//System.out.println();
		
		foundSolution = false;
		stop = false;
		minObj1 = 0;
		initSearchPartial();
		
		System.out.println("Start Search Partial....");
		obj0 = 100000;
		obj1 = 100000;
		obj2 = 100000;
		t0 = System.currentTimeMillis();
		TRYPartial(0);
		
		
		for(int i = 0; i < jury.size(); i++){
			v_examiner1[i] = sol_examiner1[i];
			v_examiner2[i] = sol_examiner2[i];
			v_president[i] = sol_president[i];
			v_secretary[i] = sol_secretary[i];
			v_add_member[i] = sol_add_member[i];
		}
		
		// complete slots
		System.out.println("BackTrackSearchJury::serchPartial, START completing slots....");
		HashSet<Integer> slots_used = new HashSet<Integer>();
		for(int i = 0; i < jury.size(); i++){
			JuryInfo jr = jury.get(i);
			slots_used.add(jr.getSlotId());
			System.out.println("jury[" + i + "], slot = " + jr.getSlotId());
		}
		int slotID = 0;
		for(int i = 0; i < jury.size(); i++){
			JuryInfo jr = jury.get(i);
			if(jr.getSlotId() == 0){
				slotID++;
				while(slots_used.contains(slotID) && slotID <= jury.size()){
					slotID++;
				}
				jr.setSlotId(slotID);
				System.out.println("set jury[" + i + "], slot = " + jr.getSlotId());
			}
		}
		
		System.out.println("Solution = " + foundSolution);
		for(int i = 0; i < jury.size(); i++){
			System.out.println(v_examiner1[i] + "\t" + v_examiner2[i] + "\t" + v_president[i] + "\t" + v_secretary[i] + "\t" + v_add_member[i]);
			
			JuryInfo jr = jury.get(i);
			//jr.setSlotId(i+1);
			
			jr.setExaminerId1(v_examiner1[i]);
			jr.setExaminerName1(mTeacher.get(v_examiner1[i]).getName());

			jr.setExaminerId2(v_examiner2[i]);
			jr.setExaminerName2(mTeacher.get(v_examiner2[i]).getName());
			
			jr.setPresidentId(v_president[i]);
			jr.setPresidentName(mTeacher.get(v_president[i]).getName());
			
			jr.setSecretaryId(v_secretary[i]);
			jr.setSecretaryName(mTeacher.get(v_secretary[i]).getName());
			
			jr.setAdditionalMemberId(v_add_member[i]);
			jr.setAdditionalMemberName(mTeacher.get(v_add_member[i]).getName());
		}
		
	}

	private void retrieveSolution(){
		if(!stop){
			foundSolution = true;
			int e1 = eval1();
			int e2 = eval2();
			if(obj1 > e1 || (obj1 == e1 && obj2 > e2)){
				obj1 = e1;
				obj2 = e2;
				if(obj1 <= minObj1) stop = true;
				System.out.println("UpdateBest obj1 = " + obj1 + ", obj2 = " + obj2);
				for(int i = 0; i < jury.size(); i++){
					sol_examiner2[i] = v_examiner2[i];
					sol_president[i] = v_president[i];
					sol_secretary[i] = v_secretary[i];
				}
			}
		}
	}
	private void retrieveSolutionPartial(){
		if(!stop){
			foundSolution = true;
			int e0 = eval0();
			int e1 = eval1();
			int e2 = eval2();
			if(obj0 > e0 || (obj0 == e0 && obj1 > e1) || (obj0 == e0 && obj1 == e1 && obj2 > e2)){
				obj0 = e0;
				obj1 = e1;
				obj2 = e2;
				if(obj1 <= minObj1) stop = true;
				System.out.println("UpdateBest obj0 = " + obj0 + ", obj1 = " + obj1 + ", obj2 = " + obj2);
				for(int i = 0; i < jury.size(); i++){
					sol_examiner1[i] = v_examiner1[i];
					sol_examiner2[i] = v_examiner2[i];
					sol_president[i] = v_president[i];
					sol_secretary[i] = v_secretary[i];
					sol_add_member[i] = v_add_member[i];
				}
			}
		}
	}
	private int eval0(){
		
		HashMap<Integer, Integer> occExam = new HashMap<Integer, Integer>();
		int minOcc = 1000;
		int maxOcc = -minOcc;
		for(int i = 0; i < nonHustMembers.size(); i++)
			occExam.put(nonHustMembers.get(i), 0);
		for(int i = 0; i < v_examiner1.length; i++){
			int t = v_examiner1[i];
			occExam.put(t, occExam.get(t)+1);
			
		}
		for(int i = 0; i < nonHustMembers.size(); i++){
			int t = nonHustMembers.get(i);
			if(minOcc > occExam.get(t)) minOcc = occExam.get(t);
			if(maxOcc < occExam.get(t)) maxOcc = occExam.get(t);
		}
		return maxOcc-minOcc;
	}
	
	private int eval1(){
		
		HashMap<Integer, Integer> occExam = new HashMap<Integer, Integer>();
		int minOcc = 1000;
		int maxOcc = -minOcc;
		for(int i = 0; i < hustMembers.size(); i++)
			occExam.put(hustMembers.get(i), 0);
		for(int i = 0; i < v_examiner2.length; i++){
			int t = v_examiner2[i];
			occExam.put(t, occExam.get(t)+1);
			
		}
		for(int i = 0; i < hustMembers.size(); i++){
			int t = hustMembers.get(i);
			if(minOcc > occExam.get(t)) minOcc = occExam.get(t);
			if(maxOcc < occExam.get(t)) maxOcc = occExam.get(t);
		}
		return maxOcc-minOcc;
	}
	
	private int eval2(){
		int minOcc = 1000;
		int maxOcc = -minOcc;
		for(int i = 0; i < hustMembers.size(); i++){
			int t = hustMembers.get(i);
			if(minOcc > occ.get(t)) minOcc = occ.get(t);
			if(maxOcc < occ.get(t)) maxOcc = occ.get(t);
		}
		return maxOcc-minOcc;
	}
	private void TRY(int k){
		if(stop) return;
		if(System.currentTimeMillis() - t0 > timeLimit) return;
		int r = k/3;
		int c = k%3;
		//System.out.println("TRY(" + k + "), r = " + r + " c = " + c);
		Iterator it = domain_examiner2[r].iterator();
		
		while(it.hasNext()){
			int tid = (Integer)it.next();
			//if(occ.get(tid) > maxOcc) continue;
			if(count[r].get(tid) > 0) continue;
			
			if(c == 0){
				v_examiner2[r] = tid;
			}else if(c == 1){
				v_president[r] = tid;
			}else{
				v_secretary[r] = tid;
			}
			
			count[r].put(tid,count[r].get(tid)+1);
			//int to = (Integer)occ.get(tid);
			occ.put(tid,occ.get(tid)+1);
			if(k == jury.size()*3-1){
				retrieveSolution();
			}else{
				TRY(k+1);
			}
			count[r].put(tid, count[r].get(tid)-1);
			occ.put(tid, occ.get(tid)-1);
		}
	}
	private void TRYPartial(int k){
		if(stop) return;
		if(System.currentTimeMillis() - t0 > timeLimit) return;
		int r = k/5;
		int c = k%5;
		//System.out.println("TRY(" + k + "), r = " + r + " c = " + c);
		Iterator it = null;
		if(c == 0) it = domain_examiner1[r].iterator();
		else if(c == 1) it = domain_examiner2[r].iterator();
		else if(c == 2) it = domain_president[r].iterator();
		else if(c == 3) it = domain_secretary[r].iterator();
		else if(c == 4) it = domain_add_member[r].iterator();
		
		while(it.hasNext()){
			int tid = (Integer)it.next();
			//if(occ.get(tid) > maxOcc) continue;
			if(count[r].get(tid) > 0) continue;
			
			if(c == 0){
				v_examiner1[r] = tid;
				//System.out.println("TRY(" + k + "), r = " + r + " c = " + c + " try examiner1 = " + tid);
			}else if(c == 1){
				v_examiner2[r] = tid;
				//System.out.println("TRY(" + k + "), r = " + r + " c = " + c + " try examiner2 = " + tid);
			}else if(c == 2){
				v_president[r] = tid;
				//System.out.println("TRY(" + k + "), r = " + r + " c = " + c + " try president = " + tid);
			}else if(c == 3){
				v_secretary[r] = tid;
				//System.out.println("TRY(" + k + "), r = " + r + " c = " + c + " try secretary = " + tid);
			}else if(c == 4){
				v_add_member[r] = tid;
				//System.out.println("TRY(" + k + "), r = " + r + " c = " + c + " try additional member = " + tid);
			}
			
			count[r].put(tid,count[r].get(tid)+1);
			//int to = (Integer)occ.get(tid);
			occ.put(tid,occ.get(tid)+1);
			if(k == jury.size()*5-1){
				retrieveSolutionPartial();
			}else{
				TRYPartial(k+1);
			}
			count[r].put(tid, count[r].get(tid)-1);
			occ.put(tid, occ.get(tid)-1);
		}
	}

	public boolean hasSolution(){
		return foundSolution;
	}
	public String getErrorMsg(){
		return retMsg;
	}
	private void initSearch(){
		sz_jury = jury.size();
		sz_non_hust_members = nonHustMembers.size();
		sz_hust_members = hustMembers.size();
		
		int k = sz_jury/sz_non_hust_members;
		int d = sz_jury%sz_non_hust_members;	
		
		v_examiner1 = new int[sz_jury];
		v_examiner2 = new int[sz_jury];
		v_president = new int[sz_jury];
		v_secretary = new int[sz_jury];
		v_add_member = new int[sz_jury];
		sol_examiner1 = new int[sz_jury];
		sol_examiner2 = new int[sz_jury];
		sol_president = new int[sz_jury];
		sol_secretary = new int[sz_jury];
		sol_add_member = new int[sz_jury];
		
		int idx = -1;
		for(int i = 0; i < sz_non_hust_members; i++){
			for(int j = 0; j < k; j++){
				idx++;
				v_examiner1[idx] = nonHustMembers.get(i);
				if(i==0){
					v_add_member[idx] = nonHustMembers.get(sz_non_hust_members-1);
				}else{
					v_add_member[idx] = nonHustMembers.get(i-1);
				}
			}
			if(d > 0){
				d--;
				idx++;
				v_examiner1[idx] = nonHustMembers.get(i);
				if(i==0){
					v_add_member[idx] = nonHustMembers.get(sz_non_hust_members-1);
				}else{
					v_add_member[idx] = nonHustMembers.get(i-1);
				}
			}
		}
		//for(int j = 0; j < k; j++){
			//idx++;
			//v_examiner1[idx] = nonHustMembers.get(sz_non_hust_members-1);
		//}
		
		k = sz_jury/sz_hust_members;
		d = sz_jury%sz_hust_members;	
		idx = -1;
		for(int i = 0; i < sz_hust_members; i++){
			for(int j = 0; j < k; j++){
				idx++;
				v_examiner2[idx] = hustMembers.get(i);
			}
			if(d > 0){
				d--;
				idx++;
				v_examiner2[idx] = hustMembers.get(i);
				
			}
		}
		
		int sz = jury.size()*3;
		if(sz%sz_hust_members == 0)
			maxOcc = jury.size()*3/sz_hust_members;
		else
			maxOcc = jury.size()*3/sz_hust_members+1;
		
		count = new HashMap[jury.size()];
		occ = new HashMap<Integer,Integer>();
		for(int j = 0; j < sz_hust_members; j++){
			int tid = hustMembers.get(j);
			occ.put(tid, 0);
		}

		
		
		for(int i = 0; i < jury.size(); i++){
			count[i] = new HashMap<Integer,Integer>();
			for(int j = 0; j < sz_hust_members; j++){
				int tid = hustMembers.get(j);
				count[i].put(tid, 0);
			}
		}
		for(int i = 0; i < sz_jury; i++)
			//System.out.println("examiner1[" + i + "] = " + v_examiner1[i] + "\t\t" + "add_member[" + i + "] = " + v_add_member[i]);
			System.out.println(v_examiner1[i] + "\t" + v_examiner2[i] + "\t" + v_president[i] + "\t" + v_secretary[i] + "\t" + v_add_member[i]);
		
	}
	private void initSearchPartial(){
		sz_jury = jury.size();
		sz_non_hust_members = nonHustMembers.size();
		sz_hust_members = hustMembers.size();
		
		//int k = sz_jury/sz_non_hust_members;
		//int d = sz_jury%sz_non_hust_members;	
		
		v_examiner1 = new int[sz_jury];
		v_examiner2 = new int[sz_jury];
		v_president = new int[sz_jury];
		v_secretary = new int[sz_jury];
		v_add_member = new int[sz_jury];
		sol_examiner1 = new int[sz_jury];
		sol_examiner2 = new int[sz_jury];
		sol_president = new int[sz_jury];
		sol_secretary = new int[sz_jury];
		sol_add_member = new int[sz_jury];
		
		
		int sz = jury.size()*5;
		if(sz%sz_hust_members == 0)
			maxOcc = jury.size()*5/sz_hust_members;
		else
			maxOcc = jury.size()*5/sz_hust_members+1;
		
		count = new HashMap[jury.size()];
		occ = new HashMap<Integer,Integer>();
		for(int j = 0; j < sz_hust_members; j++){
			int tid = hustMembers.get(j);
			occ.put(tid, 0);
		}
		for(int j = 0; j < sz_non_hust_members; j++){
			int tid = nonHustMembers.get(j);
			occ.put(tid, 0);
		}
		

		
		
		for(int i = 0; i < jury.size(); i++){
			count[i] = new HashMap<Integer,Integer>();
			for(int j = 0; j < sz_hust_members; j++){
				int tid = hustMembers.get(j);
				count[i].put(tid, 0);
			}
			for(int j = 0; j < sz_non_hust_members; j++){
				int tid = nonHustMembers.get(j);
				count[i].put(tid, 0);
			}
		}
		for(int i = 0; i < sz_jury; i++)
			//System.out.println("examiner1[" + i + "] = " + v_examiner1[i] + "\t\t" + "add_member[" + i + "] = " + v_add_member[i]);
			System.out.println(v_examiner1[i] + "\t" + v_examiner2[i] + "\t" + v_president[i] + "\t" + v_secretary[i] + "\t" + v_add_member[i]);
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
