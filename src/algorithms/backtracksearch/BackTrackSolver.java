package algorithms.backtracksearch;

import java.util.*;

import utils.Configure;
import utils.Utility;

import DataEntity.*;
import utils.*;
public class BackTrackSolver {

	/**
	 * @param args
	 */
	
	public BackTrackSolver(){
		
	}
	
	public BackTrackSearchReturnCode searchAssignAll(Vector<JuryInfo> listJury, Vector<Room> rooms, Vector<Teacher> hustTeachers, Vector<Teacher> nonHustTeachers, int timeLimit){
		BackTrackSearchReturnCode ret = new BackTrackSearchReturnCode();
		boolean foundSolution = false;
		String retMsg = "";
		
				System.out.println("BackTrackSolver::search, algorithms = AssignAll");
				//JuryPartitionerBasedOnSlots jpbos = new JuryPartitionerBasedOnSlots();
				//jpbos.partitionJury(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
				
				// assign president, rooms, slots
				AssignPresidentsRooms apr = new AssignPresidentsRooms();
				apr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
				if(!apr.foundSolution()){
					System.out.println("BackTrackSolver::search, algorithms = AssignAll, start assigning presidents");
	
					BackTrackSearchTeachersSlotsRoomsObjectiveMatch btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatch();
					btsatsr.setNbVariablesInstantiated(listJury.size());
					int[] order = new int[listJury.size()];
					for(int i = 0; i < listJury.size(); i++){
						order[i] = 7*i+2;
					}
					btsatsr.setOrder(order);
					btsatsr.setObjectiveType("expert-level");
					btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
					if(!btsatsr.foundSolution()){
						foundSolution = false;
						retMsg = "Khong the gan chu tich";
					}else{
						foundSolution = true;
					}
					
					if(foundSolution){
						System.out.println("BackTrackSolver::search, algorithms = AssignAll, start assigning rooms");
	
							//BackTrackSearchTeachersSlotsRoomsObjectiveMatch btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatch();
							//btsatsr.setNbVariablesInstantiated(listJury.size());
							//int[] order = new int[listJury.size()];
							for(int i = 0; i < listJury.size(); i++){
								order[i] = 7*i+6;
							}
							btsatsr.setOrder(order);
							btsatsr.setObjectiveType("movements");
							btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
							
							if(!btsatsr.foundSolution()){
								foundSolution = false;
								retMsg = "Khong the xep duoc phong (da xep duoc chu tich)";
							}else{
								foundSolution = true;
							}
							
							
					}
					if(foundSolution){
						System.out.println("BackTrackSolver::search, algorithms = AssignAll, start assigning slots");
						//BackTrackSearchTeachersSlotsRoomsObjectiveMatch btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatch();
						//btsatsr.setNbVariablesInstantiated(listJury.size());
						//int[] order = new int[listJury.size()];
						for(int i = 0; i < listJury.size(); i++){
							order[i] = 7*i+5;
						}
						btsatsr.setOrder(order);
						//btsatsr.setObjectiveType("movements");
						btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
						
						if(!btsatsr.foundSolution()){
							foundSolution = false;
							retMsg = "Khong the xep kip (da xep duoc chu tich va phong)";
						}else{
							foundSolution = true;
						}
					}
	
				}else{
					foundSolution = true;
				}
				
				if(!foundSolution){
					retMsg = "Khong the gan chu tich va phong truoc";
					//System.out.println(retMsg);
				}else{
					// assign examiner2
					System.out.println("BackTrackSolver::search, start assigning examiner 2");
					BackTrackSearchTeachersSlotsRoomsObjectiveMatch btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatch();
					btsatsr.setNbVariablesInstantiated(listJury.size());
					int[] order = new int[listJury.size()];
					for(int i = 0; i < listJury.size(); i++){
						order[i] = 7*i+1;
					}
					btsatsr.setOrder(order);
					btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
					
					if(!btsatsr.foundSolution()){
						foundSolution = false;
						retMsg = "Khong the xep phan bien 2";
					}else{
						// assign secretary
						btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatch();
						btsatsr.setNbVariablesInstantiated(listJury.size());
						//int[] order = new int[listJury.size()];
						
						System.out.println("BackTrackSolver::search, start assigning secretary");
						for(int i = 0; i < listJury.size(); i++){
							order[i] = 7*i+3;
						}
						btsatsr.setOrder(order);
						btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
						
						if(!btsatsr.foundSolution()){
							foundSolution = false;
							retMsg = "Khong the xep thu ky";
						}else{
							
						
							// assign examiner 1
							btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatch();
							btsatsr.setNbVariablesInstantiated(listJury.size());
							//int[] order = new int[listJury.size()];
							
							System.out.println("BackTrackSolver::search, start assigning examiner 1");
							for(int i = 0; i < listJury.size(); i++){
								order[i] = 7*i;
							}
							btsatsr.setOrder(order);
							btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
							
							if(!btsatsr.foundSolution()){
								foundSolution = false;
								retMsg = "Khong the xep phan bien 1";
							}else{
								// assign additional members
								btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatch();
								btsatsr.setNbVariablesInstantiated(listJury.size());
								//int[] order = new int[listJury.size()];
								System.out.println("BackTrackSolver::search, start assigning additional members");
								for(int i = 0; i < listJury.size(); i++){
									order[i] = 7*i+4;
								}
								btsatsr.setOrder(order);
								btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
								
								if(!btsatsr.foundSolution()){
									foundSolution = false;
									retMsg = "Khong the xep uy vien";
								}else{
									// balance examiner 1
									AssignExaminer1 ae1 = new AssignExaminer1();
									ae1.balances(listJury, nonHustTeachers);
									
									// balance examiner 2
									AssignExaminer2 ae2 = new AssignExaminer2();
									ae2.balances(listJury, hustTeachers);
								}
							}
						}
					}
				}

		System.out.println("BackTrackSolver::searchAssignAll finishes, foundSolution = " + foundSolution);
		ret.found = foundSolution;
		ret.returnMsg = retMsg;
		return ret;

	}
	
	public BackTrackSearchReturnCode searchAssignSlots(Vector<JuryInfo> listJury, Vector<Room> rooms, Vector<Teacher> hustTeachers, Vector<Teacher> nonHustTeachers, int timeLimit){
		BackTrackSearchReturnCode ret = new BackTrackSearchReturnCode();
		boolean foundSolution = false;
		String retMsg = "";
		
				/*
				JuryPartitionerBasedOnSlots jpbos = new JuryPartitionerBasedOnSlots();
				jpbos.partitionJury(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
				if(!jpbos.foundSolution()){
					foundSolution = false;
					retMsg = "Khong the xep kip";
				}
				*/
				System.out.println("BackTrackSolver::search, start assigning slots");
				BackTrackSearchTeachersSlotsRoomsObjectiveMatch btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatch();
				btsatsr.setNbVariablesInstantiated(listJury.size());
				int[] order = new int[listJury.size()];
				for(int i = 0; i < listJury.size(); i++){
					order[i] = 7*i+5;
				}
				btsatsr.setOrder(order);
				//btsatsr.setObjectiveType("movements");
				btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
				
				if(!btsatsr.foundSolution()){
					foundSolution = false;
					retMsg = "Khong the xep kip";
				}

		ret.found = foundSolution;
		ret.returnMsg = retMsg;
		return ret;
	}

	public BackTrackSearchReturnCode searchAssignRoom(Vector<JuryInfo> listJury, Vector<Room> rooms, Vector<Teacher> hustTeachers, Vector<Teacher> nonHustTeachers, int timeLimit){
		BackTrackSearchReturnCode ret = new BackTrackSearchReturnCode();
		boolean foundSolution = false;
		String retMsg = "";
		
						System.out.println("BackTrackSolver::search, start assigning rooms");
				BackTrackSearchTeachersSlotsRoomsObjectiveMatch btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatch();
				btsatsr.setNbVariablesInstantiated(listJury.size());
				int[] order = new int[listJury.size()];
				for(int i = 0; i < listJury.size(); i++){
					order[i] = 7*i+6;
				}
				btsatsr.setOrder(order);
				btsatsr.setObjectiveType("movements");
				btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
				
				if(!btsatsr.foundSolution()){
					foundSolution = false;
					retMsg = "Khong the xep phong";
				}

		ret.found = foundSolution;
		ret.returnMsg = retMsg;
		return ret;
	}

	public BackTrackSearchReturnCode searchAssignPresidentsRooms(Vector<JuryInfo> listJury, Vector<Room> rooms, Vector<Teacher> hustTeachers, Vector<Teacher> nonHustTeachers, int timeLimit){
		BackTrackSearchReturnCode ret = new BackTrackSearchReturnCode();
		boolean foundSolution = false;
		String retMsg = "";
		
		System.out.println("BackTrackSolver::search, start assigning presidents rooms");
		
		/*
		AssignPresidentsRooms apr = new AssignPresidentsRooms();
		//AssignPresidentsRoomsV2 apr = new AssignPresidentsRoomsV2();
		apr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
		*/
		
		PartitionJuriesToRoomsTeachersNotMove pjtr = new PartitionJuriesToRoomsTeachersNotMove();
		pjtr.setNbVariablesInstantiated(listJury.size());
		int[] order = new int[listJury.size()];
		for(int i = 0; i < listJury.size(); i++){
			order[i] = 7*i+6;
		}
		pjtr.setOrder(order);
		pjtr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
		//if(!apr.foundSolution()){
		if(!pjtr.foundSolution()){
			foundSolution = false;
			retMsg = "Khong the phan chia duoc phong";
			retMsg += "\n" + pjtr.getReturnedMsg();
		}else{
			JuryInfo[] sj = new JuryInfo[listJury.size()];
			for(int i = 0; i < listJury.size(); i++)
				sj[i] = listJury.get(i);
			for(int i = 0; i < sj.length-1; i++)
				for(int j = i+1; j < sj.length; j++)
					if(sj[i].getRoomId() > sj[j].getRoomId()){
						JuryInfo tj = sj[i]; sj[i] = sj[j]; sj[j] = tj;
					}
			listJury.clear();
			for(int i = 0; i < sj.length; i++)
				listJury.add(sj[i]);
			
			BackTrackSearchTeachersSlotsRoomsObjectiveMatchNotMove btsnm = new BackTrackSearchTeachersSlotsRoomsObjectiveMatchNotMove();
			btsnm.setNbVariablesInstantiated(listJury.size());
			order = new int[listJury.size()];
			for(int i = 0; i < listJury.size(); i++){
				order[i] = 7*i+2;
			}
			btsnm.setObjectiveType("expert-level");
			btsnm.setOrder(order);
			btsnm.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
			if(!btsnm.foundSolution()){
				foundSolution = false;
				retMsg += "Sau khi phan chia phong, khong the xep duoc chu tich";
				retMsg += "\n" + btsnm.getReturnedMsg();
			}
		}


		ret.found = foundSolution;
		ret.returnMsg = retMsg;
		return ret;
	}

	public BackTrackSearchReturnCode searchAssignExaminer1(Vector<JuryInfo> listJury, Vector<Room> rooms, Vector<Teacher> hustTeachers, Vector<Teacher> nonHustTeachers, int timeLimit){
		BackTrackSearchReturnCode ret = new BackTrackSearchReturnCode();
		boolean foundSolution = false;
		String retMsg = "";
		
		System.out.println("BackTrackSolver::search, start assigning examiner 1");
		BackTrackSearchTeachersSlotsRoomsObjectiveMatch btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatchNotMove();
		btsatsr.setNbVariablesInstantiated(listJury.size());
		int[] order = new int[listJury.size()];
		for(int i = 0; i < listJury.size(); i++){
			order[i] = 7*i;
		}
		btsatsr.setOrder(order);
		btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
		
		if(!btsatsr.foundSolution()){
			
			btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatch();
			btsatsr.setNbVariablesInstantiated(listJury.size());
			order = new int[listJury.size()];
			for(int i = 0; i < listJury.size(); i++){
				order[i] = 7*i;
			}
			btsatsr.setOrder(order);
			btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
			
			if(!btsatsr.foundSolution()){
				foundSolution = false;
				retMsg = "Khong the xep phan bien 1";
			}
		}

		

		ret.found = foundSolution;
		ret.returnMsg = retMsg;
		return ret;
	}

	public BackTrackSearchReturnCode searchAssignExaminer2(Vector<JuryInfo> listJury, Vector<Room> rooms, Vector<Teacher> hustTeachers, Vector<Teacher> nonHustTeachers, int timeLimit){
		BackTrackSearchReturnCode ret = new BackTrackSearchReturnCode();
		boolean foundSolution = false;
		String retMsg = "";
		
		System.out.println("BackTrackSolver::search, start assigning examiner 2");
		BackTrackSearchTeachersSlotsRoomsObjectiveMatch btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatchNotMove();
		btsatsr.setNbVariablesInstantiated(listJury.size());
		int[] order = new int[listJury.size()];
		for(int i = 0; i < listJury.size(); i++){
			order[i] = 7*i+1;
		}
		btsatsr.setOrder(order);
		btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
		if(!btsatsr.foundSolution()){
			btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatch();
			btsatsr.setNbVariablesInstantiated(listJury.size());
			order = new int[listJury.size()];
			for(int i = 0; i < listJury.size(); i++){
				order[i] = 7*i+1;
			}
			btsatsr.setOrder(order);
			btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
			if(!btsatsr.foundSolution()){
				foundSolution = false;
				retMsg = "Khong the xep phan bien 2";
			}
		}

		

		ret.found = foundSolution;
		ret.returnMsg = retMsg;
		return ret;
	}

	public BackTrackSearchReturnCode searchAssignAdditionalMember(Vector<JuryInfo> listJury, Vector<Room> rooms, Vector<Teacher> hustTeachers, Vector<Teacher> nonHustTeachers, int timeLimit){
		BackTrackSearchReturnCode ret = new BackTrackSearchReturnCode();
		boolean foundSolution = false;
		String retMsg = "";
		
		
		System.out.println("BackTrackSolver::search, start assigning additional members");
		BackTrackSearchTeachersSlotsRoomsObjectiveMatch btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatchNotMove();
		btsatsr.setNbVariablesInstantiated(listJury.size());
		int[] order = new int[listJury.size()];
		for(int i = 0; i < listJury.size(); i++){
			order[i] = 7*i+4;
		}
		btsatsr.setOrder(order);
		btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
		if(!btsatsr.foundSolution()){
			
			btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatch();
			btsatsr.setNbVariablesInstantiated(listJury.size());
			order = new int[listJury.size()];
			for(int i = 0; i < listJury.size(); i++){
				order[i] = 7*i+4;
			}
			btsatsr.setOrder(order);
			btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
			
			if(!btsatsr.foundSolution()){
				foundSolution = false;
				retMsg = "Khong the xep uy vien";
			}
		}

		

		ret.found = foundSolution;
		ret.returnMsg = retMsg;
		return ret;
	}

	public BackTrackSearchReturnCode searchAssignPresident(Vector<JuryInfo> listJury, Vector<Room> rooms, Vector<Teacher> hustTeachers, Vector<Teacher> nonHustTeachers, int timeLimit){
		BackTrackSearchReturnCode ret = new BackTrackSearchReturnCode();
		boolean foundSolution = false;
		String retMsg = "";
		
		System.out.println("BackTrackSolver::search, start assigning presidents");
		BackTrackSearchTeachersSlotsRoomsObjectiveMatch btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatch();
		btsatsr.setNbVariablesInstantiated(listJury.size());
		int[] order = new int[listJury.size()];
		for(int i = 0; i < listJury.size(); i++){
			order[i] = 7*i+2;
		}
		btsatsr.setOrder(order);
		btsatsr.setObjectiveType("expert-level");
		btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
		if(!btsatsr.foundSolution()){
			foundSolution = false;
			retMsg = "Khong the xep chu tich";
		}

		ret.found = foundSolution;
		ret.returnMsg = retMsg;
		return ret;
	}
	public BackTrackSearchReturnCode searchAssignSecretary(Vector<JuryInfo> listJury, Vector<Room> rooms, Vector<Teacher> hustTeachers, Vector<Teacher> nonHustTeachers, int timeLimit){
		BackTrackSearchReturnCode ret = new BackTrackSearchReturnCode();
		boolean foundSolution = false;
		String retMsg = "";
		
		System.out.println("BackTrackSolver::search, start assigning secretary");
		BackTrackSearchTeachersSlotsRoomsObjectiveMatch btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatchNotMove();
		btsatsr.setNbVariablesInstantiated(listJury.size());
		int[] order = new int[listJury.size()];
		for(int i = 0; i < listJury.size(); i++){
			order[i] = 7*i+3;
		}
		btsatsr.setOrder(order);
		btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
		if(!btsatsr.foundSolution()){
			btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatch();
			btsatsr.setNbVariablesInstantiated(listJury.size());
			order = new int[listJury.size()];
			for(int i = 0; i < listJury.size(); i++){
				order[i] = 7*i+3;
			}
			btsatsr.setOrder(order);
			btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
			if(!btsatsr.foundSolution()){
				foundSolution = false;
				retMsg = "Khong the xep thu ky";
			}
		}


		ret.found = foundSolution;
		ret.returnMsg = retMsg;
		return ret;
	}

	public BackTrackSearchReturnCode searchAssignAllProfessorsNotMove(Vector<JuryInfo> listJury, Vector<Room> rooms, Vector<Teacher> hustTeachers, Vector<Teacher> nonHustTeachers, int timeLimit){
		BackTrackSearchReturnCode ret = new BackTrackSearchReturnCode();
		boolean foundSolution = false;
		String retMsg = "";
		
		System.out.println("BackTrackSolver::searchAssignAllProfessorsNotMove");
		PartitionJuriesToRoomsTeachersNotMove pjtr = new PartitionJuriesToRoomsTeachersNotMove();
		pjtr.setNbVariablesInstantiated(listJury.size());
		int[] order = new int[listJury.size()];
		for(int i = 0; i < listJury.size(); i++){
			order[i] = 7*i+6;
		}
		pjtr.setOrder(order);
		pjtr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
		//if(!apr.foundSolution()){
		if(!pjtr.foundSolution()){
			foundSolution = false;
			retMsg = "Khong the phan chia duoc phong";
			retMsg += "\n" + pjtr.getReturnedMsg();
		}else{
			foundSolution = true;
			JuryInfo[] sj = new JuryInfo[listJury.size()];
			for(int i = 0; i < listJury.size(); i++)
				sj[i] = listJury.get(i);
			for(int i = 0; i < sj.length-1; i++)
				for(int j = i+1; j < sj.length; j++)
					if(sj[i].getRoomId() > sj[j].getRoomId()){
						JuryInfo tj = sj[i]; sj[i] = sj[j]; sj[j] = tj;
					}
			listJury.clear();
			for(int i = 0; i < sj.length; i++)
				listJury.add(sj[i]);
			
			System.out.println("BackTrackSolver::searchAssignAllProfessorsNotMove, finished partitioning rooms, start assinging presidents");
			BackTrackSearchTeachersSlotsRoomsObjectiveMatchNotMove btsnm = new BackTrackSearchTeachersSlotsRoomsObjectiveMatchNotMove();
			btsnm.setNbVariablesInstantiated(listJury.size());
			order = new int[listJury.size()];
			for(int i = 0; i < listJury.size(); i++){
				order[i] = 7*i+2;
			}
			btsnm.setObjectiveType("expert-level");
			btsnm.setOrder(order);
			btsnm.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
			if(!btsnm.foundSolution()){
				foundSolution = false;
				retMsg += "Sau khi phan chia phong, khong the xep duoc chu tich";
				retMsg += "\n" + btsnm.getReturnedMsg();
			}
		}
		
		if(foundSolution){
			/*
			AssignPresidentsRooms apr = new AssignPresidentsRooms();
			//AssignPresidentsRoomsV2 apr = new AssignPresidentsRoomsV2();
			apr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
			
			//AssignPresidentsRooms apr = new AssignPresidentsRooms();
			//apr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
			if(!apr.foundSolution()){
				System.out.println("BackTrackSolver::search, AssignPresidentsRooms failed -> start assigning presidents");

				BackTrackSearchTeachersSlotsRoomsObjectiveMatch btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatch();
				btsatsr.setNbVariablesInstantiated(listJury.size());
				order = new int[listJury.size()];
				for(int i = 0; i < listJury.size(); i++){
					order[i] = 7*i+2;
				}
				btsatsr.setOrder(order);
				btsatsr.setObjectiveType("expert-level");
				btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
				if(!btsatsr.foundSolution()){
					foundSolution = false;
					retMsg = "Khong the xep chu tich";
				}else{
					foundSolution = true;
				}
				
				if(foundSolution){
					System.out.println("BackTrackSolver::search, start assigning rooms");

						//BackTrackSearchTeachersSlotsRoomsObjectiveMatch btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatch();
						//btsatsr.setNbVariablesInstantiated(listJury.size());
						//int[] order = new int[listJury.size()];
						for(int i = 0; i < listJury.size(); i++){
							order[i] = 7*i+6;
						}
						btsatsr.setOrder(order);
						btsatsr.setObjectiveType("movements");
						btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
						
						if(!btsatsr.foundSolution()){
							foundSolution = false;
							retMsg = "Khong the xep phong (xep duoc chu tich)";
						}else{
							foundSolution = true;
						}
						
						
				}
				

			}
			*/
			
			if(foundSolution){
				System.out.println("BackTrackSolver::searchAssignAllProfessorsNotMove, finished partitioning rooms, assigning presidents, start assigning slots");
				BackTrackSearchTeachersSlotsRoomsObjectiveMatch btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatch();
				btsatsr.setNbVariablesInstantiated(listJury.size());
				order = new int[listJury.size()];
				for(int i = 0; i < listJury.size(); i++){
					order[i] = 7*i+5;
				}
				btsatsr.setOrder(order);
				//btsatsr.setObjectiveType("movements");
				btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
				
				if(!btsatsr.foundSolution()){
					foundSolution = false;
					retMsg = "Khong the xep kip (xep duoc chu tich va phong)";
				}else{
					foundSolution = true;
				}
			}
			
			if(foundSolution){
				AssignNonHustToJuryRooms AN = new AssignNonHustToJuryRooms();
				AN.assignNonHustMembersToJuryRooms(listJury, nonHustTeachers, rooms, timeLimit);
				
				if(AN.foundSolution()){
					AssignHustToJuryRooms A = new AssignHustToJuryRooms();
					A.assignHustMembersToJuryRooms(listJury, hustTeachers, rooms, timeLimit);
					
					if(!A.foundSolution()){
						foundSolution = false;
						retMsg = "Khong the xep het thanh vien trong truong";
					}
				}else{
					foundSolution = false;
					retMsg = "Khong the xep het thanh vien ngoai truong";
				}
			}else{
				retMsg = "Khong the xep chu tich va phong";
				foundSolution = false;
			}
		}



		ret.found = foundSolution;
		ret.returnMsg = retMsg;
		return ret;
	}

	public BackTrackSearchReturnCode search(Vector<JuryInfo> listJury, int nbRooms, Vector<Teacher> hustTeachers, Vector<Teacher> nonHustTeachers, String algorithm, int timeLimit){
		BackTrackSearchReturnCode ret = new BackTrackSearchReturnCode();
		boolean foundSolution = false;
		String retMsg = "";
		
		int n = listJury.size();
		int sz_of_jury = n/nbRooms;
		int d = n%nbRooms;

		Vector<Room> rooms = new Vector<Room>();
		for(int i = 1; i <= nbRooms; i++){
			rooms.add(new Room(i,"-",0));
		}
		
		for(int i = 0; i < rooms.size(); i++){
			Room r = rooms.get(i);
			r.setNbJuries(sz_of_jury);
			r.setMaxNbHustMembers(hustTeachers.size()/nbRooms);
			r.setMaxNbNonHustMembers(nonHustTeachers.size()/nbRooms);
		}
		for(int i = 0; i < d; i++){
			Room r  = rooms.get(i);
			r.setNbJuries(r.getMaxNbJuries()+1);
			
		}
		for(int i = 0; i < hustTeachers.size()%nbRooms; i++){
			Room r = rooms.get(i);
			r.setMaxNbHustMembers(r.getMaxNbHustMembers()+1);
		}
		for(int i = 0; i < nonHustTeachers.size()%nbRooms; i++){
			Room r = rooms.get(i);
			r.setMaxNbNonHustMembers(r.getMaxNbNonHustMembers()+1);
		}
		
		for(int i = 0; i < rooms.size(); i++){
			Room r = rooms.get(i);
			System.out.println("Room[" + i + "] " + ", nbJuries = " + r.getMaxNbJuries() + ", maxHust = " + r.getMaxNbHustMembers() + ", nonHust = " + r.getMaxNbNonHustMembers());
		}
		System.out.println("hust.sz = " + hustTeachers.size() + " nonHust.sz = " + nonHustTeachers.size());
		
		
		PreCheckFeasibility pc = new PreCheckFeasibility();
		//write data to text file
		pc.writeDataToFile(listJury, rooms, hustTeachers, nonHustTeachers, "jury.txt");
		
		if(!pc.check(listJury, rooms, hustTeachers, nonHustTeachers)){
			foundSolution = false;
			retMsg = pc.getMsg();
		}else{
			
			if(algorithm.equals("AssignAll")){
				/*
				System.out.println("BackTrackSolver::search, algorithms = AssignAll");
				//JuryPartitionerBasedOnSlots jpbos = new JuryPartitionerBasedOnSlots();
				//jpbos.partitionJury(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
				
				// assign president, rooms, slots
				AssignPresidentsRooms apr = new AssignPresidentsRooms();
				apr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
				if(!apr.foundSolution()){
					System.out.println("BackTrackSolver::search, algorithms = AssignAll, start assigning presidents");
	
					BackTrackSearchTeachersSlotsRoomsObjectiveMatch btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatch();
					btsatsr.setNbVariablesInstantiated(listJury.size());
					int[] order = new int[listJury.size()];
					for(int i = 0; i < listJury.size(); i++){
						order[i] = 7*i+2;
					}
					btsatsr.setOrder(order);
					btsatsr.setObjectiveType("expert-level");
					btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
					if(!btsatsr.foundSolution()){
						foundSolution = false;
						retMsg = "Khong the gan chu tich";
					}else{
						foundSolution = true;
					}
					
					if(foundSolution){
						System.out.println("BackTrackSolver::search, algorithms = AssignAll, start assigning rooms");
	
							//BackTrackSearchTeachersSlotsRoomsObjectiveMatch btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatch();
							//btsatsr.setNbVariablesInstantiated(listJury.size());
							//int[] order = new int[listJury.size()];
							for(int i = 0; i < listJury.size(); i++){
								order[i] = 7*i+6;
							}
							btsatsr.setOrder(order);
							btsatsr.setObjectiveType("movements");
							btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
							
							if(!btsatsr.foundSolution()){
								foundSolution = false;
								retMsg = "Khong the xep duoc phong (da xep duoc chu tich)";
							}else{
								foundSolution = true;
							}
							
							
					}
					if(foundSolution){
						System.out.println("BackTrackSolver::search, algorithms = AssignAll, start assigning slots");
						//BackTrackSearchTeachersSlotsRoomsObjectiveMatch btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatch();
						//btsatsr.setNbVariablesInstantiated(listJury.size());
						//int[] order = new int[listJury.size()];
						for(int i = 0; i < listJury.size(); i++){
							order[i] = 7*i+5;
						}
						btsatsr.setOrder(order);
						//btsatsr.setObjectiveType("movements");
						btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
						
						if(!btsatsr.foundSolution()){
							foundSolution = false;
							retMsg = "Khong the xep kip (da xep duoc chu tich va phong)";
						}else{
							foundSolution = true;
						}
					}
	
				}
				if(!foundSolution){
					//retMsg = "Khong the gan chu tich va phong truoc";
				}else{
					// assign examiner2
					System.out.println("BackTrackSolver::search, start assigning examiner 2");
					BackTrackSearchTeachersSlotsRoomsObjectiveMatch btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatch();
					btsatsr.setNbVariablesInstantiated(listJury.size());
					int[] order = new int[listJury.size()];
					for(int i = 0; i < listJury.size(); i++){
						order[i] = 7*i+1;
					}
					btsatsr.setOrder(order);
					btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
					
					if(!btsatsr.foundSolution()){
						foundSolution = false;
						retMsg = "Khong the xep phan bien 2";
					}else{
						// assign secretary
						btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatch();
						btsatsr.setNbVariablesInstantiated(listJury.size());
						//int[] order = new int[listJury.size()];
						
						System.out.println("BackTrackSolver::search, start assigning secretary");
						for(int i = 0; i < listJury.size(); i++){
							order[i] = 7*i+3;
						}
						btsatsr.setOrder(order);
						btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
						
						if(!btsatsr.foundSolution()){
							foundSolution = false;
							retMsg = "Khong the xep thu ky";
						}else{
							
						
							// assign examiner 1
							btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatch();
							btsatsr.setNbVariablesInstantiated(listJury.size());
							//int[] order = new int[listJury.size()];
							
							System.out.println("BackTrackSolver::search, start assigning examiner 1");
							for(int i = 0; i < listJury.size(); i++){
								order[i] = 7*i;
							}
							btsatsr.setOrder(order);
							btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
							
							if(!btsatsr.foundSolution()){
								foundSolution = false;
								retMsg = "Khong the xep phan bien 1";
							}else{
								// assign additional members
								btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatch();
								btsatsr.setNbVariablesInstantiated(listJury.size());
								//int[] order = new int[listJury.size()];
								System.out.println("BackTrackSolver::search, start assigning additional members");
								for(int i = 0; i < listJury.size(); i++){
									order[i] = 7*i+4;
								}
								btsatsr.setOrder(order);
								btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
								
								if(!btsatsr.foundSolution()){
									foundSolution = false;
									retMsg = "Khong the xep uy vien";
								}else{
									// balance examiner 1
									AssignExaminer1 ae1 = new AssignExaminer1();
									ae1.balances(listJury, nonHustTeachers);
									
									// balance examiner 2
									AssignExaminer2 ae2 = new AssignExaminer2();
									ae2.balances(listJury, hustTeachers);
								}
							}
						}
					}
				}
				*/
				return searchAssignAll(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
			}else if(algorithm.equals("AssignSlots")){
				/*
				System.out.println("BackTrackSolver::search, start assigning slots");
				BackTrackSearchTeachersSlotsRoomsObjectiveMatch btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatch();
				btsatsr.setNbVariablesInstantiated(listJury.size());
				int[] order = new int[listJury.size()];
				for(int i = 0; i < listJury.size(); i++){
					order[i] = 7*i+5;
				}
				btsatsr.setOrder(order);
				//btsatsr.setObjectiveType("movements");
				btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
				
				if(!btsatsr.foundSolution()){
					foundSolution = false;
					retMsg = "Khong the xep kip";
				}
				*/
				return searchAssignSlots(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
			}else if(algorithm.equals("AssignRooms")){
				/*
				System.out.println("BackTrackSolver::search, start assigning rooms");
				BackTrackSearchTeachersSlotsRoomsObjectiveMatch btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatch();
				btsatsr.setNbVariablesInstantiated(listJury.size());
				int[] order = new int[listJury.size()];
				for(int i = 0; i < listJury.size(); i++){
					order[i] = 7*i+6;
				}
				btsatsr.setOrder(order);
				btsatsr.setObjectiveType("movements");
				btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
				
				if(!btsatsr.foundSolution()){
					foundSolution = false;
					retMsg = "Khong the xep phong";
				}
				*/
				return searchAssignRoom(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
				
			}else if(algorithm.equals("AssignPresidentsRooms")){
				/*
				System.out.println("BackTrackSolver::search, start assigning presidents rooms");
				
				
				
				PartitionJuriesToRoomsTeachersNotMove pjtr = new PartitionJuriesToRoomsTeachersNotMove();
				pjtr.setNbVariablesInstantiated(listJury.size());
				int[] order = new int[listJury.size()];
				for(int i = 0; i < listJury.size(); i++){
					order[i] = 7*i+6;
				}
				pjtr.setOrder(order);
				pjtr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
				//if(!apr.foundSolution()){
				if(!pjtr.foundSolution()){
					foundSolution = false;
					retMsg = "Khong the phan chia duoc phong";
					retMsg += "\n" + pjtr.getReturnedMsg();
				}else{
					JuryInfo[] sj = new JuryInfo[listJury.size()];
					for(int i = 0; i < listJury.size(); i++)
						sj[i] = listJury.get(i);
					for(int i = 0; i < sj.length-1; i++)
						for(int j = i+1; j < sj.length; j++)
							if(sj[i].getRoomId() > sj[j].getRoomId()){
								JuryInfo tj = sj[i]; sj[i] = sj[j]; sj[j] = tj;
							}
					listJury.clear();
					for(int i = 0; i < sj.length; i++)
						listJury.add(sj[i]);
					
					BackTrackSearchTeachersSlotsRoomsObjectiveMatchNotMove btsnm = new BackTrackSearchTeachersSlotsRoomsObjectiveMatchNotMove();
					btsnm.setNbVariablesInstantiated(listJury.size());
					order = new int[listJury.size()];
					for(int i = 0; i < listJury.size(); i++){
						order[i] = 7*i+2;
					}
					btsnm.setObjectiveType("expert-level");
					btsnm.setOrder(order);
					btsnm.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
					if(!btsnm.foundSolution()){
						foundSolution = false;
						retMsg += "Sau khi phan chia phong, khong the xep duoc chu tich";
						retMsg += "\n" + btsnm.getReturnedMsg();
					}
				}
				
				
				*/
				
				return searchAssignPresidentsRooms(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
			}else if(algorithm.equals("AssignExaminer1")){
				/*
				System.out.println("BackTrackSolver::search, start assigning examiner 1");
				BackTrackSearchTeachersSlotsRoomsObjectiveMatch btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatchNotMove();
				btsatsr.setNbVariablesInstantiated(listJury.size());
				int[] order = new int[listJury.size()];
				for(int i = 0; i < listJury.size(); i++){
					order[i] = 7*i;
				}
				btsatsr.setOrder(order);
				btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
				
				if(!btsatsr.foundSolution()){
					
					btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatch();
					btsatsr.setNbVariablesInstantiated(listJury.size());
					order = new int[listJury.size()];
					for(int i = 0; i < listJury.size(); i++){
						order[i] = 7*i;
					}
					btsatsr.setOrder(order);
					btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
					
					if(!btsatsr.foundSolution()){
						foundSolution = false;
						retMsg = "Khong the xep phan bien 1";
					}
				}
				*/
				return searchAssignExaminer1(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
			}else if(algorithm.equals("AssignExaminer2")){
				/*
				System.out.println("BackTrackSolver::search, start assigning examiner 2");
				BackTrackSearchTeachersSlotsRoomsObjectiveMatch btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatchNotMove();
				btsatsr.setNbVariablesInstantiated(listJury.size());
				int[] order = new int[listJury.size()];
				for(int i = 0; i < listJury.size(); i++){
					order[i] = 7*i+1;
				}
				btsatsr.setOrder(order);
				btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
				if(!btsatsr.foundSolution()){
					btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatch();
					btsatsr.setNbVariablesInstantiated(listJury.size());
					order = new int[listJury.size()];
					for(int i = 0; i < listJury.size(); i++){
						order[i] = 7*i+1;
					}
					btsatsr.setOrder(order);
					btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
					if(!btsatsr.foundSolution()){
						foundSolution = false;
						retMsg = "Khong the xep phan bien 2";
					}
				}
				*/
				return searchAssignExaminer2(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
			}else if(algorithm.equals("AssignAdditionalMember")){
				
				/*
				System.out.println("BackTrackSolver::search, start assigning additional members");
				BackTrackSearchTeachersSlotsRoomsObjectiveMatch btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatchNotMove();
				btsatsr.setNbVariablesInstantiated(listJury.size());
				int[] order = new int[listJury.size()];
				for(int i = 0; i < listJury.size(); i++){
					order[i] = 7*i+4;
				}
				btsatsr.setOrder(order);
				btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
				if(!btsatsr.foundSolution()){
					
					btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatch();
					btsatsr.setNbVariablesInstantiated(listJury.size());
					order = new int[listJury.size()];
					for(int i = 0; i < listJury.size(); i++){
						order[i] = 7*i+4;
					}
					btsatsr.setOrder(order);
					btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
					
					if(!btsatsr.foundSolution()){
						foundSolution = false;
						retMsg = "Khong the xep uy vien";
					}
				}
				*/
				
				return searchAssignAdditionalMember(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
			}else if(algorithm.equals("AssignPresident")){
				/*
				System.out.println("BackTrackSolver::search, start assigning presidents");
				BackTrackSearchTeachersSlotsRoomsObjectiveMatch btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatch();
				btsatsr.setNbVariablesInstantiated(listJury.size());
				int[] order = new int[listJury.size()];
				for(int i = 0; i < listJury.size(); i++){
					order[i] = 7*i+2;
				}
				btsatsr.setOrder(order);
				btsatsr.setObjectiveType("expert-level");
				btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
				if(!btsatsr.foundSolution()){
					foundSolution = false;
					retMsg = "Khong the xep chu tich";
				}
				*/
				return searchAssignPresident(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
			}else if(algorithm.equals("AssignSecretary")){
				/*
				System.out.println("BackTrackSolver::search, start assigning secretary");
				BackTrackSearchTeachersSlotsRoomsObjectiveMatch btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatchNotMove();
				btsatsr.setNbVariablesInstantiated(listJury.size());
				int[] order = new int[listJury.size()];
				for(int i = 0; i < listJury.size(); i++){
					order[i] = 7*i+3;
				}
				btsatsr.setOrder(order);
				btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
				if(!btsatsr.foundSolution()){
					btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatch();
					btsatsr.setNbVariablesInstantiated(listJury.size());
					order = new int[listJury.size()];
					for(int i = 0; i < listJury.size(); i++){
						order[i] = 7*i+3;
					}
					btsatsr.setOrder(order);
					btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
					if(!btsatsr.foundSolution()){
						foundSolution = false;
						retMsg = "Khong the xep thu ky";
					}
				}
				*/
				
				return searchAssignSecretary(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
			}else if(algorithm.equals("AssignAllProfessorsNotMove")){
				/*	
				
				PartitionJuriesToRoomsTeachersNotMove pjtr = new PartitionJuriesToRoomsTeachersNotMove();
				pjtr.setNbVariablesInstantiated(listJury.size());
				int[] order = new int[listJury.size()];
				for(int i = 0; i < listJury.size(); i++){
					order[i] = 7*i+6;
				}
				pjtr.setOrder(order);
				pjtr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
				//if(!apr.foundSolution()){
				if(!pjtr.foundSolution()){
					foundSolution = false;
					retMsg = "Khong the phan chia duoc phong";
					retMsg += "\n" + pjtr.getReturnedMsg();
				}else{
					JuryInfo[] sj = new JuryInfo[listJury.size()];
					for(int i = 0; i < listJury.size(); i++)
						sj[i] = listJury.get(i);
					for(int i = 0; i < sj.length-1; i++)
						for(int j = i+1; j < sj.length; j++)
							if(sj[i].getRoomId() > sj[j].getRoomId()){
								JuryInfo tj = sj[i]; sj[i] = sj[j]; sj[j] = tj;
							}
					listJury.clear();
					for(int i = 0; i < sj.length; i++)
						listJury.add(sj[i]);
					
					BackTrackSearchTeachersSlotsRoomsObjectiveMatchNotMove btsnm = new BackTrackSearchTeachersSlotsRoomsObjectiveMatchNotMove();
					btsnm.setNbVariablesInstantiated(listJury.size());
					order = new int[listJury.size()];
					for(int i = 0; i < listJury.size(); i++){
						order[i] = 7*i+2;
					}
					btsnm.setObjectiveType("expert-level");
					btsnm.setOrder(order);
					btsnm.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
					if(!btsnm.foundSolution()){
						foundSolution = false;
						retMsg += "Sau khi phan chia phong, khong the xep duoc chu tich";
						retMsg += "\n" + btsnm.getReturnedMsg();
					}
				}
				
				if(foundSolution){
					AssignPresidentsRooms apr = new AssignPresidentsRooms();
					//AssignPresidentsRoomsV2 apr = new AssignPresidentsRoomsV2();
					apr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
					
					//AssignPresidentsRooms apr = new AssignPresidentsRooms();
					//apr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
					if(!apr.foundSolution()){
						System.out.println("BackTrackSolver::search, AssignPresidentsRooms failed -> start assigning presidents");
	
						BackTrackSearchTeachersSlotsRoomsObjectiveMatch btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatch();
						btsatsr.setNbVariablesInstantiated(listJury.size());
						order = new int[listJury.size()];
						for(int i = 0; i < listJury.size(); i++){
							order[i] = 7*i+2;
						}
						btsatsr.setOrder(order);
						btsatsr.setObjectiveType("expert-level");
						btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
						if(!btsatsr.foundSolution()){
							foundSolution = false;
							retMsg = "Khong the xep chu tich";
						}else{
							foundSolution = true;
						}
						
						if(foundSolution){
							System.out.println("BackTrackSolver::search, start assigning rooms");
	
								//BackTrackSearchTeachersSlotsRoomsObjectiveMatch btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatch();
								//btsatsr.setNbVariablesInstantiated(listJury.size());
								//int[] order = new int[listJury.size()];
								for(int i = 0; i < listJury.size(); i++){
									order[i] = 7*i+6;
								}
								btsatsr.setOrder(order);
								btsatsr.setObjectiveType("movements");
								btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
								
								if(!btsatsr.foundSolution()){
									foundSolution = false;
									retMsg = "Khong the xep phong (xep duoc chu tich)";
								}else{
									foundSolution = true;
								}
								
								
						}
						
	
					}
					
					if(foundSolution){
						System.out.println("BackTrackSolver::search, start assigning slots");
						BackTrackSearchTeachersSlotsRoomsObjectiveMatch btsatsr = new BackTrackSearchTeachersSlotsRoomsObjectiveMatch();
						btsatsr.setNbVariablesInstantiated(listJury.size());
						order = new int[listJury.size()];
						for(int i = 0; i < listJury.size(); i++){
							order[i] = 7*i+5;
						}
						btsatsr.setOrder(order);
						//btsatsr.setObjectiveType("movements");
						btsatsr.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
						
						if(!btsatsr.foundSolution()){
							foundSolution = false;
							retMsg = "Khong the xep kip (xep duoc chu tich va phong)";
						}else{
							foundSolution = true;
						}
					}
					
					if(foundSolution){
						AssignNonHustToJuryRooms AN = new AssignNonHustToJuryRooms();
						AN.assignNonHustMembersToJuryRooms(listJury, nonHustTeachers, rooms, timeLimit);
						
						if(AN.foundSolution()){
							AssignHustToJuryRooms A = new AssignHustToJuryRooms();
							A.assignHustMembersToJuryRooms(listJury, hustTeachers, rooms, timeLimit);
							
							if(!A.foundSolution()){
								foundSolution = false;
								retMsg = "Khong the xep het thanh vien trong truong";
							}
						}else{
							foundSolution = false;
							retMsg = "Khong the xep het thanh vien ngoai truong";
						}
					}else{
						retMsg = "Khong the xep chu tich va phong";
						foundSolution = false;
					}
				}
				*/
				
				return searchAssignAllProfessorsNotMove(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
			}else if(algorithm.equals("BackTrackSearchAssignTeachersSlotsRooms")){
				
				BackTrackSearchAssignTeachersSlotsRooms bs = new BackTrackSearchAssignTeachersSlotsRooms();
				bs.search(listJury, rooms, hustTeachers, nonHustTeachers, timeLimit);
			}
		}

		ret.found = foundSolution;
		ret.returnMsg = retMsg;
		return ret;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Vector<JuryInfo> jury = Utility.getListJuryInfo("none", "TTM");
		for(int i = 0; i < jury.size(); i++){
			JuryInfo J = jury.get(i);
			System.out.println(J.getSupervisorId() + "\t" + J.getExaminerId1() + "\t" + 
			J.getExaminerId2() + "\t" + J.getPresidentId() + "\t" + J.getSecretaryId() + "\t" + 
					J.getAdditionalMemberId() + "\t" + J.getSlotId() + "\t" + J.getRoomId());
		}
		System.out.println(jury.size());
		Vector<Teacher> ahust = Utility.getHustTeachers();
		for(int i = 0; i < ahust.size(); i++){
			Teacher T = ahust.get(i);
			System.out.println(T.getID() + "\t" + T.getInstitute() + "\t");
		}
		System.out.println(ahust.size());
		Vector<Teacher> anonHust = Utility.getNonHustTeachers();
		
		
		for(int i = 0; i < anonHust.size(); i++){
			Teacher T = anonHust.get(i);
			System.out.println(T.getID() + "\t" + T.getInstitute() + "\t");
		}
		System.out.println(anonHust.size());
		
		Vector<Teacher> hust = new Vector<Teacher>();
		Vector<Teacher> nonHust = new Vector<Teacher>();
		
		HashSet<Integer> UT = new HashSet<Integer>();
		for(int i = 0; i < jury.size(); i++){
			JuryInfo J = jury.get(i);
			UT.add(J.getExaminerId1());
			UT.add(J.getExaminerId2());
			UT.add(J.getPresidentId());
			UT.add(J.getSecretaryId());
			UT.add(J.getAdditionalMemberId());
		}
		
		for(int i = 0; i < ahust.size(); i++){
			Teacher T = ahust.get(i);
			if(UT.contains(T.getID()))
				hust.add(T);
		}
		for(int i = 0; i < anonHust.size(); i++){
			Teacher T = anonHust.get(i);
			if(UT.contains(T.getID()))
				nonHust.add(T);
		}
		
		for(int i = 0; i < jury.size(); i++){
			JuryInfo J = jury.get(i);
			J.setExaminerId1(0);
			J.setExaminerId2(0);
			J.setPresidentId(0);
			J.setSecretaryId(0);
			J.setAdditionalMemberId(0);
			J.setSlotId(0);
			J.setRoomId(0);
		}
		
		BackTrackSolver S = new BackTrackSolver();
		//BackTrackSearchReturnCode r = S.search(jury, 5, hust, nonHust, "AssignAll", 1);
		BackTrackSearchReturnCode r = S.search(jury, 5, hust, nonHust, "AssignAllProfessorsNotMove", 5);
		for(int i = 0; i < jury.size(); i++){
			JuryInfo J = jury.get(i);
			System.out.println(J.getSupervisorId() + "\t" + J.getExaminerId1() + "\t" + 
			J.getExaminerId2() + "\t" + J.getPresidentId() + "\t" + J.getSecretaryId() + "\t" + 
					J.getAdditionalMemberId() + "\t" + J.getSlotId() + "\t" + J.getRoomId());
		}
		System.out.println("ret = " + r.found + ", msg = " + r.returnMsg);
		
		
		Configure.path = "pham quang dung";
		Utility.test();
		
		DataBaseQuery q = new DataBaseQuery();
	}

}
