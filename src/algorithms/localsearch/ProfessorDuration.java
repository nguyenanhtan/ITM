/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms.localsearch;

import java.util.LinkedList;
import java.util.List;


/**
 * 
 * @author daz
 */
public class ProfessorDuration {

	private List<VarInt> listTimes;
	private int violations;
	private int professorId;

	public int getProfessorId() {
		return professorId;
	}

	public void setProfessorId(int professorId) {
		this.professorId = professorId;
	}

	public ProfessorDuration(int professorId) {
		this.professorId = professorId;
		this.listTimes = new LinkedList<VarInt>();
	}

	void addTime(VarInt time) {
		this.listTimes.add(time);
	}

	public List<VarInt> getListTimes() {
		return listTimes;
	}

	public int getViolations() {
		return violations;
	}

	public void setViolations(int violations) {
		this.violations = violations;
	}

	public void sortTime() {
		for (int i = 0; i < listTimes.size() - 1; i++) {
			for (int j = i + 1; j < listTimes.size(); j++) {
				VarInt vari = listTimes.get(i);
				VarInt varj = listTimes.get(j);
				if (vari.getValue() > varj.getValue()) {
					listTimes.set(i, varj);
					listTimes.set(j, vari);
				}
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(professorId).append(" ==>");
		for (VarInt time : listTimes) {
			builder.append(time.getValue()).append(",");
		}
		return builder.toString();
	}
}
