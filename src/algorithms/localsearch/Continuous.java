/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms.localsearch;



import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import DataEntity.JuryInfo;
import DataEntity.Teacher;



/**
 *
 * @author daz
 */
public class Continuous implements IConstraint {

    private VarInt x;
    private VarInt y;
    private int violations;
//    private List<Students> students;
    private VarInt[] varSlots;
    private int numberStudents;
//    List<ProfessorDuration> listProfessorDurations;
    private HashMap<Integer, ProfessorDuration> timeMap;
    private List<Teacher> listProfessors;
    private int currentSlot;
    private int currentVal;
    private List<JuryInfo> listJuryInfos;

    public Continuous(List<JuryInfo> listJuryInfos,List<Teacher> listProfessors, VarInt[] varSlots) {
        this.listProfessors = listProfessors;
        this.listJuryInfos=listJuryInfos;
        this.varSlots = varSlots;
        this.timeMap = new HashMap<Integer, ProfessorDuration>();
        this.numberStudents=listJuryInfos.size();
    }

    @Override
    public int violations() {
        return this.violations;
    }

    @Override
    public int getAssignDelta(VarInt x, int val) {
        int slotId = -1;
        int delta = 0;
        for (int i = 0; i < varSlots.length; i++) {
            if (varSlots[i].equals(x)) {
                slotId = i;
                break;
            }
        }

        if (slotId != -1) {
            List<Integer> allJury = getAllJury(listJuryInfos.get(slotId));
            for (Integer pId : allJury) {
                ProfessorDuration pd = timeMap.get(pId);
                List<VarInt> listTimes = pd.getListTimes();
                int length = listTimes.size();
                if (length > 1) {
                    for (int i = 0; i < length; i++) {
                        VarInt vari = listTimes.get(i);
                        if (vari.equals(x)) {
                            List<Integer> newList = new LinkedList<Integer>();
                            for (VarInt varInt : listTimes) {
                                if (varInt.getID() != x.getID()) {
                                    newList.add(varInt.getValue());
                                }
                            }
                            newList.add(val);
                            sortList(newList);
                            int pViolation = 0;
                            for (int j = 0, l = newList.size(); j < l - 1; j++) {
                                int var1 = newList.get(j + 1);
                                int var2 = newList.get(j);
                                if (var1 > var2) {
                                    pViolation += var1 - var2 - 1;
                                }

                            }
                            delta += pViolation - pd.getViolations();
                            break;
                        }
                    }
                }
            }
        }
        return delta;
    }

    private void writeDataCheck(int slotId, int val, String fileName) {


//        Date date = new Date();
//        Common.addToFile("propagate sel_i=" + slotId + ", sel_sl=" + val, fileName);
//        for (Professor p : listProfessors) {
//            ProfessorDuration pd = timeMap.get(p.getID());
//            if (pd != null) {
//                pd.sortTime();
//                Common.addToFile(pd.toString() + " ------ " + calculateViolation(pd.getListTimes()), fileName);
//            }
//        }

    }


    @Override
    public void propagate(VarInt x, int val) {
        int slotId = -1;
        for (int i = 0; i < varSlots.length; i++) {
            if (varSlots[i].equals(x)) {
                slotId = i;
                break;
            }
        }
        currentSlot = slotId;
        currentVal = val;

        if (slotId != -1) {
            List<Integer> allJury = getAllJury(listJuryInfos.get(slotId));
            for (Integer pId : allJury) {
                ProfessorDuration pd = timeMap.get(pId);
                List<VarInt> listTimes = pd.getListTimes();
                int pViolation = 0;
                int length = listTimes.size();
                if (length > 1) {
//                    int maxSlot = listTimes.get(length - 1).getValue();
//                    int minSlot = listTimes.get(0).getValue();
                    for (int i = 0; i < length; i++) {
                        VarInt vari = listTimes.get(i);
                        if (vari.equals(x)) {
                            List<Integer> newList = new LinkedList<Integer>();
                            for (VarInt varInt : listTimes) {
                                if (varInt.getID() != x.getID()) {
                                    newList.add(varInt.getValue());
                                }
                            }
                            newList.add(val);
                            sortList(newList);
                            for (int j = 0, l = newList.size(); j < l - 1; j++) {
                                int var1 = newList.get(j + 1);
                                int var2 = newList.get(j);
                                if (var1 > var2) {
                                    pViolation += var1 - var2 - 1;
                                }

                            }
                            violations += pViolation - pd.getViolations();
                            pd.setViolations(pViolation);
                            break;
                        }
                    }
                }
            }
        }

        //varSlots[slotId].setValue(val);
    }

    private void sortList(List<Integer> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = i + 1; j < list.size(); j++) {
                int val1 = list.get(i);
                int val2 = list.get(j);
                if (val1 > val2) {
                    list.set(i, val2);
                    list.set(j, val1);
                }
            }
        }
    }

    private List<Integer> getAllJury(JuryInfo juryInfo) {
        List<Integer> allJury = new LinkedList<Integer>();
        allJury.add(juryInfo.getAdditionalMemberId());
        allJury.add(juryInfo.getExaminerId1());
        allJury.add(juryInfo.getExaminerId2());
        allJury.add(juryInfo.getPresidentId());
        allJury.add(juryInfo.getSecretaryId());
        return allJury;
    }

    @Override
    public void initPropagate() {
        for (int i = 0; i < numberStudents; i++) {
          JuryInfo juryInfo=listJuryInfos.get(i);
            List<Integer> allJury = getAllJury(juryInfo);
            for (Integer pId : allJury) {
                ProfessorDuration pdFound = timeMap.get(pId);
                if (pdFound == null) {
                    pdFound = new ProfessorDuration(pId);
                    timeMap.put(pId, pdFound);
                }
                pdFound.addTime(varSlots[i]);
            }
        }

        //tinh violation
        violations = 0;
        for (Teacher p : listProfessors) {
            //xep thoi gian theo do tang dan
            ProfessorDuration pd = timeMap.get(p.getID());
            if (pd != null) {
                pd.sortTime();
                List<VarInt> listTimes = pd.getListTimes();
                int pViolation = 0;
                if (listTimes.size() > 1) {
                    for (int i = 0, l = listTimes.size(); i < l - 1; i++) {
                        int var1 = listTimes.get(i + 1).getValue();
                        int var2 = listTimes.get(i).getValue();
                        if (var1 > var2) {
                            pViolation += var1 - var2 - 1;
                        }
                    }
                }
                pd.setViolations(pViolation);
                violations += pViolation;
            }
        }
    }

    @Override
    public boolean verify() {
        //tinh violation
        int v = 0;
        for (Teacher p : listProfessors) {
            ProfessorDuration pd = timeMap.get(p.getID());
            if (pd != null) {

                pd.sortTime();
                List<VarInt> listTimes = pd.getListTimes();
                int pViolation = 0;
                for (int i = 0, l = listTimes.size(); i < l - 1; i++) {
                    int var1 = listTimes.get(i + 1).getValue();
                    int var2 = listTimes.get(i).getValue();
                    if (var1 > var2) {
                        pViolation += var1 - var2 - 1;
                    }

                }
                v += pViolation;
            }
        }
        if (v != violations) {
//            Debug.d("v :" + v);
//            Debug.d("violation :" + violations);
            return false;
        }
        return true;
    }

    void printResult() {
        writeDataCheck(currentSlot, currentVal, "final_result.txt");
    }
}
