package lab3_3;

import java.util.ArrayList;

public class Group {

    private final String groupName;
    private final ArrayList<Student> studentList;

    public Group(String group){
        this.groupName = group;
        this.studentList = new ArrayList<>();
    }

    public String getNameOfGroup(){
        return this.groupName;
    }

    public ArrayList<Student> getStudents(){
        return this.studentList;
    }

    public void generateGroup(int sizeOfGroup) {
        for (int i = 0; i < sizeOfGroup; i++) {
            this.studentList.add(new Student(i+1));
        }
    }
}
