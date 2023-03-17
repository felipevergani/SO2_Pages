import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LRU {

    private int pageSize; // tamanho da página em bytes
    private int pageBits; // número de bits utilizados para representar o número da página
    private int frameCount; // número de frames disponíveis na memória física
    private List<Integer> referenceString; // lista com os números das páginas acessadas

    public LRU(int pageSize, int frameCount) {
        this.pageSize = pageSize;
        this.pageBits = (int) (Math.log(pageSize) / Math.log(2));
        this.frameCount = frameCount;
        this.referenceString = new ArrayList<Integer>();
    }

    public void readTrace(String traceFile) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(traceFile));
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.replace(" ", "0");
            int address = Integer.parseInt(line, 16);
            int page = address >> pageBits;
            referenceString.add(page);
        }
        reader.close();
    }

    public int run() {
        LinkedList<Integer> frames = new LinkedList<Integer>();
        int faults = 0;
        for (int page : referenceString) {
            if (frames.contains(page)) { // hit
                frames.removeFirstOccurrence(page);
                frames.addLast(page);
            } else { // miss
                if (frames.size() < frameCount) { // there are free frames
                    frames.addLast(page);
                } else { // all frames are occupied
                    frames.removeFirst();
                    frames.addLast(page);
                }
                faults++;
            }
        }
        return faults;
    }

    public static void main(String[] args) throws IOException {
        LRU lru = new LRU(4096, 4); // tamanho da página: 4096 bytes, 4 frames livres
        lru.readTrace("traces/lu.txt");
        int faults = lru.run();
        System.out.println("Número de falhas de página: " + faults);
    }

}
