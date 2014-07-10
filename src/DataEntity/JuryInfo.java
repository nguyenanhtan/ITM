package DataEntity;

public class JuryInfo {
	private String slotDescription;

	private int id;// ID of the jury
	private String roomName;

	private int roomId;

	private int slotId;
	private int slotIndex;

	private int	studentID;
	
	private String name;//student name

	private String title;// thesis title

	private String supervisorName;

	private int supervisorId;

	private String examinerName1;

	private int examinerId1;

	private String examinerName2;

	private int examinerId2;

	private String presidentName;

	private int presidentId;

	private String secretaryName;

	private int secretaryId;

	private String additionalmemberName;

	private int additionalMemberId;

	private int idDataSet;
	
	private int[] professors;
	
	public JuryInfo() {
	}

	public JuryInfo(int id, int studentID, String title, int examiner1ID, int examiner2ID, int presidentID,
			int secretaryID, int memberID, int idDataSet){
		this.id = id;
		this.studentID = studentID;
		this.title = title;
		
		this.examinerId1 = examiner1ID;
		this.examinerId2 = examiner2ID;
		this.presidentId = presidentID;
		this.secretaryId = secretaryID;
		this.additionalMemberId = memberID;
		this.idDataSet = idDataSet;
		
		professors = new int[5];
		professors[0] = getExaminerId1();
		professors[1] = getExaminerId2();
		professors[2] = getPresidentId();
		professors[3] = getSecretaryId();
		professors[4] = getAdditionalMemberId();
		
	}
	public int[] getJuryMemberIDs(){ return professors;}
	
	public JuryInfo(int id, int studentID, String title, int supervisorID, int examiner1ID, int examiner2ID, int presidentID,
			int secretaryID, int memberID, int idDataSet){
		this.id = id;
		this.studentID = studentID;
		this.title = title;
		this.supervisorId = supervisorID;
		this.examinerId1 = examiner1ID;
		this.examinerId2 = examiner2ID;
		this.presidentId = presidentID;
		this.secretaryId = secretaryID;
		this.additionalMemberId = memberID;
		this.idDataSet = idDataSet;
		
		professors = new int[5];
		professors[0] = getExaminerId1();
		professors[1] = getExaminerId2();
		professors[2] = getPresidentId();
		professors[3] = getSecretaryId();
		professors[4] = getAdditionalMemberId();
	}
	public JuryInfo(int id, String name, String title, String supervisorName,
			String examinerName1, String examinerName2, String presidentName,
			String secretaryName, String additionalmemberName, int idDataSet) {

		this.id = id;
		this.name = name;
		this.title = title;
		this.supervisorName = supervisorName;
		this.examinerName1 = examinerName1;
		this.examinerName2 = examinerName2;
		this.presidentName = presidentName;
		this.secretaryName = secretaryName;
		this.additionalmemberName = additionalmemberName;
		this.idDataSet = idDataSet;
		
		professors = new int[5];
		professors[0] = getExaminerId1();
		professors[1] = getExaminerId2();
		professors[2] = getPresidentId();
		professors[3] = getSecretaryId();
		professors[4] = getAdditionalMemberId();
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStudentName(){
		return this.name;
	}
	public void setStudentName(String name){
		this.name = name;
	}
	public void setStudentID(int id){
		this.studentID = id;
	}
	public int getStudentID(){
		return studentID;
	}
	public String getName() {
		return name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSupervisorName() {
		return supervisorName;
	}

	public void setSupervisorName(String supervisorName) {
		this.supervisorName = supervisorName;
	}

	public String getExaminerName1() {
		return examinerName1;
	}

	public void setExaminerName1(String examinerName1) {
		this.examinerName1 = examinerName1;
	}

	public String getExaminerName2() {
		return examinerName2;
	}

	public void setExaminerName2(String examinerName2) {
		this.examinerName2 = examinerName2;
	}

	public String getPresidentName() {
		return presidentName;
	}

	public void setPresidentName(String presidentName) {
		this.presidentName = presidentName;
	}

	public String getSecretaryName() {
		return secretaryName;
	}

	public void setSecretaryName(String secretaryName) {
		this.secretaryName = secretaryName;
	}

	public String getAdditionalMemberName() {
		return additionalmemberName;
	}

	public void setAdditionalMemberName(String additionalmemberName) {
		this.additionalmemberName = additionalmemberName;
	}

	public boolean juryProfessor(int pId) {
		if(pId == 0) return false;
		if (pId == presidentId || pId == secretaryId
				|| pId == additionalMemberId) {
			return true;
		}
		if (pId == examinerId1) {
			return true;
		}
		if (pId == examinerId2) {
			return true;
		}
		return false;
	}

	public void setExaminerId1(int examinerId1) {
		this.examinerId1 = examinerId1;
		professors[0] = this.examinerId1; 
	}

	public void setExaminerId2(int examinerId2) {
		this.examinerId2 = examinerId2;
		professors[1] = this.examinerId2;
	}

	public void setAdditionalMemberId(int additionalMemberId) {
		this.additionalMemberId = additionalMemberId;
		professors[4] = this.additionalMemberId;
	}

	public void setPresidentId(int presidentId) {
		this.presidentId = presidentId;
		professors[2] = this.presidentId;
	}

	public void setSecretaryId(int secretaryId) {
		this.secretaryId = secretaryId;
		professors[3] = this.secretaryId;
	}

	public void setSupervisorId(int supervisorId) {
		this.supervisorId = supervisorId;
	}

	public int getSecretaryId() {
		return secretaryId;
	}

	public int getAdditionalMemberId() {
		return additionalMemberId;
	}

	public int getExaminerId1() {
		return examinerId1;
	}

	public int getExaminerId2() {
		return examinerId2;
	}

	public int getPresidentId() {
		return presidentId;
	}

	public int getSupervisorId() {
		return supervisorId;
	}

	public void setSlotId(int slotId) {
		//this.slotId = slotId+1;
		this.slotId = slotId;
	}

	public int getSlotId() {
		return slotId;
	}

	public void setRoomId(int roomId) {
		//this.roomId = roomId+1;
		this.roomId = roomId;
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setSlotDescription(String slotDescription) {
		this.slotDescription = slotDescription;
	}

	public String getSlotDescription() {
		return slotDescription;
	}

	public void setSlotIndex(int sli){
		this.slotIndex = sli;
	}
	public int getSlotIndex(){ return this.slotIndex;}
	
	public void setIdDataSet(int idDataSet) {
		this.idDataSet = idDataSet;
	}
	public int getIdDataSet() {
		return idDataSet;
	}
	
	public boolean conflict(JuryInfo jury){
		if(this.juryProfessor(jury.getExaminerId1())) return true;
		if(this.juryProfessor(jury.getExaminerId2())) return true;
		if(this.juryProfessor(jury.getPresidentId())) return true;
		if(this.juryProfessor(jury.getSecretaryId())) return true;
		if(this.juryProfessor(jury.getAdditionalMemberId())) return true;
		
		return false;
	}
}
