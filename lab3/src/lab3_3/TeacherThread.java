package lab3_3;

public class TeacherThread extends Thread {
    private final String teacher;
    private final Journal journal;
    private final int weeksNum;

    public TeacherThread(String name, Journal journal, int weeks) {
        this.teacher = name;
        this.journal = journal;
        this.weeksNum = weeks;
    }

    @Override
    public void run() {
        for (int i = 0; i < weeksNum; i++) {
            for (Group group: journal.getGroups()) {
                for (Student student: group.getStudents()) {
                    int mark = (int) Math.ceil(Math.random() * 100);
//                    int mark = 100;
                    student.setMark(mark, teacher, i + 1);
                }
            }
        }
    }
}
