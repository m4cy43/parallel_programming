package lab3_3;

import java.util.ArrayList;

public class Journal {

    private final ArrayList<Group> groupList;

    public Journal(ArrayList<Group> groups) {
        this.groupList = groups;
    }

    public ArrayList<Group> getGroups() {
        return this.groupList;
    }

    public void printJournal() {
        for (Group g : this.groupList) {
            System.out.println("Group:"+g.getNameOfGroup());
            for (Student s : g.getStudents()) {
                System.out.print("Student№"+s.getPosition()+" Sum:"+s.sumMarks() + "\t");
                for (Mark m : s.getMarks()) {
                    System.out.print("|w:"+m.week+" t:"+m.teacher+" m:"+m.mark + "\t");
                }
                System.out.println();
            }
            System.out.println();
        }
    }
}
