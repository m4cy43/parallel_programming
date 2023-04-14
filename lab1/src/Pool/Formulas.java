package Pool;

public class Formulas {
    public static double TwoPointsDistance (double x1, double x2, double y1, double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }
}
