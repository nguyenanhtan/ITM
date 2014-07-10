package algorithms.backtracksearch;

import java.util.HashMap;
import java.util.Vector;

import DataEntity.JuryInfo;
import DataEntity.Room;
import DataEntity.Teacher;

public class AssignSlot {

	/**
	 * @param args
	 */
	private Vector<JuryInfo> jury;
	private Vector<Room> rooms;
	private double timeLimit;
	private double t0;
	
	public void search(Vector<JuryInfo> jury, Vector<Room> rooms, int timeLimit){
		this.jury = jury;
		this.rooms = rooms;
		this.timeLimit = timeLimit*1000;
		for(int i = 0; i < rooms.size(); i++){
			Room r = rooms.get(i);
			int id = 0;
			for(int j = 0; j < jury.size(); j++){
				JuryInfo J = jury.get(j);
				if(J.getRoomId() == r.getID()){
					id++;
					J.setSlotId(id);
				}
			}
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
