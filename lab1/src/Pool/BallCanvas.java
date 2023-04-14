package Pool;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class BallCanvas extends JPanel {
    private ArrayList<Ball> balls = new ArrayList<>();
    private ArrayList<Pocket> pockets = new ArrayList<>();

    public void add(Ball b){
        this.balls.add(b);
    }
    public void remove(Ball b){
        Score.inc();
        this.balls.remove(b);
    }
    public void add(Pocket p){
        this.pockets.add(p);
    }
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        for (int j=0; j<pockets.size(); j++) {
            Pocket p = pockets.get(j);
            p.draw(g2);
        }
        for(int i=0; i<balls.size(); i++){
            Ball b = balls.get(i);
            for(int j=0; j<pockets.size(); j++){
                Pocket p = pockets.get(j);
                // ball in the pocket
                if (b.hitThePocket(p)){
                    b.isPocked = true;
                    remove(b);
                    break;
                }
                else if (!b.isPocked) {
                    b.draw(g2);
                }
            }
        }
    }
}