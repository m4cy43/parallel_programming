import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BounceFrame extends JFrame {
    private BallCanvas canvas;
    public static final int WIDTH = 450;
    public static final int HEIGHT = 350;
    public BounceFrame() {
        this.setSize(WIDTH, HEIGHT);
        this.setTitle("Bounce program");
        this.canvas = new BallCanvas();

        // 4 pockets on the corners of canvas
        Pocket p1 = new Pocket(canvas, 0, 0);
        canvas.add(p1);
        Pocket p2 = new Pocket(canvas, WIDTH-60, 0);
        canvas.add(p2);
        Pocket p3 = new Pocket(canvas, 0, HEIGHT-120);
        canvas.add(p3);
        Pocket p4 = new Pocket(canvas, WIDTH-60, HEIGHT-120);
        canvas.add(p4);

        System.out.println("In Frame Thread name = "
                + Thread.currentThread().getName());
        Container content = this.getContentPane();
        content.add(this.canvas, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.lightGray);
        JButton buttonStart = new JButton("Add one");
        JButton buttonStop = new JButton("Exit");

        // score
        JLabel labelScore = new JLabel("Balls in pockets: " + Score.get());
        buttonStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Ball b = new Ball(canvas);
                canvas.add(b);

                BallThread thread = new BallThread(b);
                thread.start();
                System.out.println("Thread name = " +
                        thread.getName());
            }
        });
        buttonStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        // score listener, as those above
        Score.addScoreListener(new ScoreListener() {
            @Override
            public void actionPerformed() {
                labelScore.setText("Balls in pockets: " + Score.get());
                labelScore.repaint();
            }
        });

        buttonPanel.add(buttonStart);
        buttonPanel.add(buttonStop);
        buttonPanel.add(labelScore);

        content.add(buttonPanel, BorderLayout.SOUTH);
    }
}