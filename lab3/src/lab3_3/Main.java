package lab3_3;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        final int WEEK_NUM = 3;

        Group g1 = new Group("01");
        g1.generateGroup(20);
        Group g2 = new Group("02");
        g2.generateGroup(23);
        Group g3 = new Group("03");
        g3.generateGroup(26);

        ArrayList<Group> g123 = new ArrayList<>();
        g123.add(g1);
        g123.add(g2);
        g123.add(g3);
        Journal j = new Journal(g123);

        Runnable r = new Runnable() {
            @Override
            public void run() {
                (new TeacherThread("L1", j, WEEK_NUM)).start();
                (new TeacherThread("A1", j, WEEK_NUM)).start();
                (new TeacherThread("A2", j, WEEK_NUM)).start();
                (new TeacherThread("A3", j, WEEK_NUM)).start();
            }
        };
        Thread tr = new Thread(r);
        tr.start();
        try {
            tr.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        j.printJournal();
    }
}
