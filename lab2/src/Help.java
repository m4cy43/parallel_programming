public class Help {
    public static int nearestDivisor(int smallest, int dividend) {
        // Simplest way to find the nearest divisor
        int divisor = 0;
        if(smallest <= dividend) {
            divisor = smallest;
            while (dividend % divisor != 0) {
                divisor++;
            }
        } else {
            divisor = dividend;
        }
        return divisor;
    }
}
