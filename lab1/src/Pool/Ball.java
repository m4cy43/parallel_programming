package Pool;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Random;

public class Ball {
    private Component canvas;
    private static final int XSIZE = 20;
    private static final int YSIZE = 20;
    private int x = 0;
    private int y = 0;
    private int dx = 2;
    private int dy = 2;
    public boolean isPocked = false;
    public Color color = Color.darkGray; // default

    public Ball(Component c){
        this.canvas = c;

        if(Math.random()<0.5){
            x = new Random().nextInt(this.canvas.getWidth());
            y = 50;
        }else{
            x = 50;
            y = new Random().nextInt(this.canvas.getHeight());
        }
    }

    // for blue and red balls, random start pos
    public Ball(Component c, Color color) {
        this.canvas = c;
        this.color = color;

        if(Math.random()<0.5){
            x = new Random().nextInt(this.canvas.getWidth());
            y = 50;
        }else{
            x = 50;
            y = new Random().nextInt(this.canvas.getHeight());
        }
    }
    // for blue and red balls fix start pos
    public Ball(Component c, Color color, int x, int y) {
        this.canvas = c;
        this.color = color;

        this.x = x;
        this.y = y;
    }

    public void draw (Graphics2D g2){
        g2.setColor(this.color);
        g2.fill(new Ellipse2D.Double(x,y,XSIZE,YSIZE));
    }

    public void move(){
        x+=dx;
        y+=dy;
        if(x<0){
            x = 0;
            dx = -dx;
        }
        if(x+XSIZE>=this.canvas.getWidth()){
            x = this.canvas.getWidth()-XSIZE;
            dx = -dx;
        }
        if(y<0){
            y=0;
            dy = -dy;
        }
        if(y+YSIZE>=this.canvas.getHeight()){
            y = this.canvas.getHeight()-YSIZE;
            dy = -dy;
        }
        this.canvas.repaint();
    }

    public boolean hitThePocket(Pocket pocket) {
        // the formula for the distance between two points (centers of circles)
        double distance = Formulas.TwoPointsDistance((pocket.getX()+pocket.getSize()/2),
                (this.x+this.XSIZE/2),(pocket.getY()+pocket.getSize()/2),(this.y+this.YSIZE/2));
        boolean isInPocket = (distance + this.XSIZE/2) < (pocket.getSize()/2);
        return isInPocket;
    }
}
