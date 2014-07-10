package DataEntity;

public class SlotRoom {

	/**
	 * @param args
	 */
	private int slotID;
	private int roomID;
	public SlotRoom(int slotID, int roomID){
		this.slotID = slotID;
		this.roomID = roomID;
	}
	public int getSlotID(){ return slotID;}
	public int getRoomID(){ return roomID;}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
