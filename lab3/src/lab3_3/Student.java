package lab3_3;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class Student {
    private final ArrayList<Mark> marks;
    private final int position;
    private final ReentrantLock lock = new ReentrantLock();
    public Student() {
        this.position = 0;
        marks = new ArrayList<>();
    }
    public Student(int pos) {
        this.position = pos;
        marks = new ArrayList<>();
    }

    public void setMark(int mark, String teacher, int week) {
        lock.lock();
        marks.add(new Mark(mark, teacher, week));
        lock.unlock();
    }

    public int sumMarks() {
        int sum = 0;
        for (Mark m:
             marks) {
            sum += m.mark;
        }
        return sum;
    }

    public int getPosition(){
        return this.position;
    }

    public ArrayList<Mark> getMarks(){
        return this.marks;
    }

}
