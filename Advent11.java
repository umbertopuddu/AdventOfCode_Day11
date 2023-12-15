import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;
import java.util.stream.Collectors;

public class Advent11
{
    public static void main(String[] args)
    {
        ArrayList<ArrayList<String>> table = null;
        ArrayList<Galaxy> galaxies = null;
        ArrayList<ArrayList<Integer>> blanks = null;

        try{
            table = fileToTable("puzzle.txt");
            galaxies = findGalaxies(table);
            blanks = findBlank(table);
            for(Galaxy cur: galaxies){

            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

        System.out.println("Rows:");
        for(int num : blanks.get(0)){
            System.out.print(num + " ");
        }
        System.out.println();
        
        System.out.println("Coloms:");
        for(int num : blanks.get(1)){
            System.out.print(num + " ");
        }
        System.out.println();

        int rateOne = 2;
        Galaxy.maxValues(rateOne, blanks, table.size(), table.get(0).size());
        ArrayList<Galaxy> expandedGalaxies = expandGalaxies(galaxies, rateOne);
        System.out.println();
        System.out.println("Part 1: " + calcPaths(expandedGalaxies));
        System.out.println();

        int rateTwo = 1000000;
        Galaxy.maxValues(rateTwo, blanks, table.size(), table.get(0).size());
        ArrayList<Galaxy> expandedGalaxiesTwo = expandGalaxies(galaxies, rateTwo);
        System.out.println();
        System.out.println("Part 2: " + calcPaths(expandedGalaxiesTwo));
        System.out.println();
    }

    private static ArrayList<Galaxy> expandGalaxies(ArrayList<Galaxy> galaxies, int rate) {
        return galaxies.parallelStream()
                .map(galaxy -> galaxy.expand(rate))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private static long calcPaths(ArrayList<Galaxy> expandedGalaxies) {
        AtomicLong sum = new AtomicLong(0);

        // Parallelize the outer loop
        AtomicLong count = new AtomicLong(0);
        IntStream.range(0, expandedGalaxies.size() - 1).parallel().forEach(i -> {
            for (int k = i + 1; k < expandedGalaxies.size(); k++) {
                sum.addAndGet(expandedGalaxies.get(i).path(expandedGalaxies.get(k)));
            }
            count.incrementAndGet();
            System.out.println("All paths done for galaxy " + count + "/" + (expandedGalaxies.size() - 1));
        });

        return sum.get();
    }

    private static boolean contain(Galaxy tar, ArrayList<Galaxy> list){
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).x == tar.x && list.get(i).y == tar.y){
                return true;
            }
        }
        return false;
    }
    private static ArrayList<ArrayList<String>> fileToTable(String filePath) throws IOException 
    {
        String text = new String(Files.readAllBytes(Paths.get(filePath)));
        String[] lines = text.split("\n");
        ArrayList<ArrayList<String>> arrayOfArrays = new ArrayList<>();
        for (String line : lines) {
            ArrayList<String> lineList = new ArrayList<>(Arrays.asList(line.trim().split("")));
            arrayOfArrays.add(lineList);
        }
        return arrayOfArrays;
    }
    

    private static boolean numContains(int num, ArrayList<Integer> domain) {
        for (int i = 0; i < domain.size(); i++) {
            if (domain.get(i) == num) {
                return true;
            }
        }
        return false;
    }
    private static ArrayList<ArrayList<Integer>> findBlank(ArrayList<ArrayList<String>> table) {
        ArrayList<Integer> rows = new ArrayList<>();
        ArrayList<Integer> cols = new ArrayList<>();
    
        // Check rows
        for (int i = 0; i < table.size(); i++) {
            boolean blank = true;
            for (int k = 0; k < table.get(i).size(); k++) {
                if (!table.get(i).get(k).equals(".")) {
                    blank = false;
                    break;
                }
            }
            if (blank) {
                rows.add(i);
            }
        }
    
        // Check columns
        for (int i = 0; i < table.get(0).size(); i++) {
            boolean blank = true;
            for (ArrayList<String> row : table) {
                if (!row.get(i).equals(".")) {
                    blank = false;
                    break;
                }
            }
            if (blank) {
                cols.add(i);
            }
        }
    
        // Create and return the result array
        ArrayList<ArrayList<Integer>> blanks = new ArrayList<ArrayList<Integer>>();
        blanks.add(rows);
        blanks.add(cols);
        return blanks;
    }
    private static void printTable(ArrayList<ArrayList<String>> table){
        for(int i = 0; i < table.size(); i++){
            for(int k = 0; k < table.get(i).size(); k++){
                System.out.print(table.get(i).get(k));
            }
            System.out.println();
        }
    }
    
    private static ArrayList<Galaxy> findGalaxies(ArrayList<ArrayList<String>> table) {
        ArrayList<Galaxy> galaxies = new ArrayList<Galaxy>();
        for (int y = 0; y < table.size(); y++) {
            for (int x = 0; x < table.get(y).size(); x++) {
                if(table.get(y).get(x).equals("#")){
                    Galaxy found = new Galaxy(x, y);
                    galaxies.add(found);
                }
            }
        }
        return galaxies;
    }

    
}