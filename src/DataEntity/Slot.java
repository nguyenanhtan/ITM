package DataEntity;

public class Slot {

	/**
	 * @param args
	 */
	int id;
	String description;
	int slotIndex;
	int roomID;
	
	public Slot(int id, int slotIndex, String description, int roomID){
		this.id = id;
		this.description = description;
		this.slotIndex = slotIndex;
		this.roomID = roomID;
	}
	public int getID(){ return id;}
	public void setID(int id){
		this.id = id;
	}
	public int getSlotIndex(){ return this.slotIndex;}
	public int getRoomID(){ return this.roomID;}
	
	public String getDescription(){ return description;}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
