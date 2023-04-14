package Pool;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Random;

public class Pocket {
    private Component canvas;
    private static final int XSIZE = 50;
    private static final int YSIZE = 50;
    private int x = 0;
    private int y = 0;

    public Pocket(Component c, int x, int y){
        this.canvas = c;
        this.x = x;
        this.y = y;
    }

    public void draw (Graphics2D g2){
        g2.setColor(Color.GREEN);
        g2.fill(new Ellipse2D.Double(x,y,XSIZE,YSIZE));
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getSize() {
        return this.XSIZE;
    }
}
