import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class LRU {
    
    private int pageSize;
    private List<Integer> referenceString;
    
    public LRU(int pageSize, List<Integer> referenceString) {
        this.pageSize = pageSize;
        this.referenceString = referenceString;
    }
    
    public int run(int numFrames) {
        int pageFaults = 0;
        LinkedList<Integer> frames = new LinkedList<>();
        
        for (int i = 0; i < referenceString.size(); i++) {
            int page = referenceString.get(i) / pageSize;
            
            if (!frames.contains(page)) {
                pageFaults++;
                if (frames.size() == numFrames) {
                    frames.removeLast();
                }
                frames.addFirst(page);
            } else {
                frames.remove(frames.indexOf(page));
                frames.addFirst(page);
            }
        }
        
        return pageFaults;
    }
    
    public static void main(String[] args) {
        int[] numFrames = {2, 4, 8, 16, 32, 64, 128, 256, 512, 1024}; // Altere para outros valores de frames livres, se desejar
        
        for (int i = 0; i < numFrames.length; i++) {
            int pageFaults = 0;
            String filename = "traces/lu.txt"; // nome do arquivo
            File file = new File(filename);
            List<Integer> referenceString = new ArrayList<>();
            
            try {
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                    int address = Integer.parseInt(scanner.nextLine(), 16);
                    referenceString.add(address);
                }
                scanner.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            LRU lru = new LRU(4096, referenceString);
            pageFaults = lru.run(numFrames[i]);
            
            System.out.println("Num frames: " + numFrames[i] + " Page faults: " + pageFaults);
        }
    }
}
