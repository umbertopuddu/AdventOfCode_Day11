import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;


public class Galaxy {
    
    public long x;
    public long y;
    static long maxX;
    static long maxY;
    static ArrayList<ArrayList<Integer>> blanks;
    
    public Galaxy(long x, long y){
        this.x = x;
        this.y = y;
    }

    
    public static void maxValues(int rate, ArrayList<ArrayList<Integer>> blanks, int originalRows, int originalCols){
        Galaxy.blanks = new ArrayList<>(blanks);
        
        long addX = blanks.get(1).size() * (rate - 1);
        long addY = blanks.get(0).size() * (rate - 1);
        
        maxX = originalCols + addX;
        maxY = originalRows + addY;
    }

    public Galaxy expand(int rate) {
        long newX = this.x;
        long newY = this.y;

        for (int blankCol : blanks.get(1)) {
            if (this.x > blankCol) {
                newX += (rate - 1);
            }
        }

        for (int blankRow : blanks.get(0)) {
            if (this.y > blankRow) {
                newY += (rate - 1);
            }
        }
        // Debug print
        System.out.println("Expanding Galaxy at (" + this.x + "," + this.y + ") to (" + newX + "," + newY + ")");
        return new Galaxy(newX, newY);
    }
    
    public double distance(Galaxy other){
        return distanceBtw(this.x, this.y, other.x, other.y);
    }
    private double distanceBtw(long x, long y, long x2, long y2){
        long dX = x - x2;
        long dY = y - y2;
        
        double dist = 1.0 * Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2));
        return dist;
    }
    
    public long path(Galaxy other) {
        Galaxy shadow = new Galaxy(this.x, this.y);
        long path = 0;
    
        while (shadow.x != other.x || shadow.y != other.y) {
            double shortestDistance = Double.MAX_VALUE;
            long bestX = shadow.x;
            long bestY = shadow.y;
    
            // Check right movement
            if (shadow.x + 1 < maxX) {
                double distance = distanceBtw(shadow.x + 1, shadow.y, other.x, other.y);
                if (distance < shortestDistance) {
                    shortestDistance = distance;
                    bestX = shadow.x + 1;
                    bestY = shadow.y;
                }
            }
    
            // Check left movement
            if (shadow.x - 1 >= 0) {
                double distance = distanceBtw(shadow.x - 1, shadow.y, other.x, other.y);
                if (distance < shortestDistance) {
                    shortestDistance = distance;
                    bestX = shadow.x - 1;
                    bestY = shadow.y;
                }
            }
    
            // Check down movement
            if (shadow.y + 1 < maxY) {
                double distance = distanceBtw(shadow.x, shadow.y + 1, other.x, other.y);
                if (distance < shortestDistance) {
                    shortestDistance = distance;
                    bestX = shadow.x;
                    bestY = shadow.y + 1;
                }
            }
    
            // Check up movement
            if (shadow.y - 1 >= 0) {
                double distance = distanceBtw(shadow.x, shadow.y - 1, other.x, other.y);
                if (distance < shortestDistance) {
                    shortestDistance = distance;
                    bestX = shadow.x;
                    bestY = shadow.y - 1;
                }
            }
    
            // Move to the best position
            if (shadow.x != bestX || shadow.y != bestY) {
                shadow.x = bestX;
                shadow.y = bestY;
                
                path++;
            } else {
                // If no movement is possible, break to avoid infinite loop
                break;
            }
        }
        return path;
    }
    public String toString(){
        return "# Galaxy at line " + y + " and slot " + x + ".";
    }
}