import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class LRUfvck{
    public static void main(String[] args) {
        int pageSize = 4096;
        int[] frameSizes = {4, 8, 16, 32, 64};
        String referenceStringFile = "traces/lu.txt";
        ArrayList<Integer> referenceString = readReferenceString(referenceStringFile);
        
        for(int frameSize : frameSizes) {
            int numFrames = frameSize * 1024 / pageSize;
            LinkedList<Integer> frames = new LinkedList<>();
            int numFaults = 0;
            
            for(int page : referenceString) {
                if(!frames.contains(page)) {
                    numFaults++;
                    if(frames.size() == numFrames) {
                        frames.removeFirst();
                    }
                    frames.addLast(page);
                } else {
                    frames.remove(frames.indexOf(page));
                    frames.addLast(page);
                }
            }
            
            System.out.println("Number of frames: " + numFrames);
            System.out.println("Number of faults: " + numFaults);
        }
    }
    
    private static ArrayList<Integer> readReferenceString(String filename) {
        ArrayList<Integer> referenceString = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                int page = Integer.parseInt(line.trim(), 16);
                referenceString.add(page);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return referenceString;
    }
}