package Pool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BounceFrame extends JFrame {
    private BallCanvas canvas;
    public static final int WIDTH = 500;
    public static final int HEIGHT = 500;
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
        JButton buttonStart = new JButton("G");
        JButton buttonRed = new JButton("R");
        JButton buttonBlue = new JButton("B");
        JButton buttonSnake = new JButton("Snake");
        JButton buttonJoin = new JButton("Join");
        JButton buttonStop = new JButton("X");


        // score
        JLabel labelScore = new JLabel("In pockets: " + Score.get());
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
                System.out.println("👋");
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
        // red and blue balls
        // red - max priority
        buttonRed.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newBallRandom(Color.RED, Thread.MAX_PRIORITY);
            }
        });
        // blue - min priority
        buttonBlue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newBallRandom(Color.BLUE, Thread.MIN_PRIORITY);
            }
        });
        // red n blue experiment, snake shape
        buttonSnake.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    for (int i = 0; i < 1000; i++){
                    newBallFixed(Color.BLUE, Thread.MIN_PRIORITY, 0,50);
                }
//                for (int i = 0; i < 5; i++){
//                    newBallFixed(Color.RED, Thread.MAX_PRIORITY, 60,60);
//                }
                // one ball
                // at the tail of the snake
                newBallFixed(Color.RED, Thread.MAX_PRIORITY, 0, 50);
            }
        });
        // join experiment
        buttonJoin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BallThread thread = null;
                for (int i = 0; i < 4; i++) {
                    // blue -> red -> blue -> red
                    Color color = (i % 2 == 0) ? Color.BLUE : Color.RED;
                    Ball b = new Ball(canvas, color, 50, 50);
                    canvas.add(b);

                    if (thread == null) {
                        thread = new BallThread(b);
                    }
                    else {
                        thread = new BallThread(b, thread);
                    }
                    // ball life-time
                    thread.setIterations(500);
                    thread.setPriority(Thread.NORM_PRIORITY);
                    thread.start();
                    System.out.println("Thread name = " +
                            thread.getName());
                }
            }
        });

        buttonPanel.add(buttonStart);
        buttonPanel.add(buttonRed);
        buttonPanel.add(buttonBlue);
        buttonPanel.add(buttonSnake);
        buttonPanel.add(buttonJoin);
        buttonPanel.add(buttonStop);
        buttonPanel.add(labelScore);

        content.add(buttonPanel, BorderLayout.SOUTH);
    }

    // DRY is for losers 👻
    public void newBallRandom(Color color, int prior) {
        Ball b = new Ball(canvas, color);
        canvas.add(b);

        BallThread thread = new BallThread(b);
        thread.setPriority(prior);
        thread.start();
        System.out.println("Thread name = " +
                thread.getName());
    }
    public void newBallFixed(Color color, int prior, int x, int y) {
        Ball b = new Ball(canvas, color, x, y);
        canvas.add(b);

        BallThread thread = new BallThread(b);
        thread.setPriority(prior);
        thread.start();
        System.out.println("Thread name = " +
                thread.getName());
    }
}