import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

public class LRU1 {

    public static void main(String[] args) throws IOException {
        int[] numFrames = {1, 2, 3, 4, 5}; // Números de frames livres a serem testados
        int pageSize = 4096; // Tamanho da página em bytes
        String traceFile = "traces/trace.txt"; // Arquivo de log de acesso à memória
        String referenceString = generateReferenceString(traceFile, pageSize);
        for (int numFrame : numFrames) {
            int pageFaults = runLRU(referenceString, numFrame);
            System.out.println("Número de frames livres: " + numFrame + ", Número de falhas de página: " + pageFaults);
        }
    }

    private static String generateReferenceString(String traceFile, int pageSize) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(traceFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                int address = Integer.parseInt(line.trim(), 16);
                int pageNum = address / pageSize;
                sb.append(pageNum).append(",");
            }
        }
        return sb.toString();
    }

    private static int runLRU(String referenceString, int numFrames) {
        Deque<Integer> pageQueue = new ArrayDeque<>(); // Fila de páginas
        Set<Integer> pageSet = new HashSet<>(); // Conjunto de páginas presentes na memória
        String[] pages = referenceString.split(",");
        int pageFaults = 0; // Número de falhas de página
        for (String page : pages) {
            int pageNum = Integer.parseInt(page);
            if (!pageSet.contains(pageNum)) { // Página não está na memória
                pageFaults++;
                if (pageQueue.size() == numFrames) { // Fila de páginas está cheia
                    int removedPage = pageQueue.removeLast();
                    pageSet.remove(removedPage);
                }
                pageQueue.addFirst(pageNum);
                pageSet.add(pageNum);
            } else { // Página está na memória
                pageQueue.remove(pageNum);
                pageQueue.addFirst(pageNum);
            }
        }
        return pageFaults;
    }
}
"O programa recebe como entrada o número de frames livres a serem testados, 
o tamanho da página em bytes e o arquivo de log de acesso à memória, e gera 
uma string de referência considerando o tamanho da página. Em seguida, para cada número de frames, 
executa o algoritmo LRU e exibe o número de falhas de página. A implementação utiliza uma fila de páginas
e um conjunto de páginas presentes na memória, e para cada página na string de referência, 
verifica se a página está presente na memória. Se a página não está presente, adiciona à fila de páginas
e ao conjunto de páginas presentes na memória, e se a fila de páginas está cheia,
remove a página menos recentemente usada (LRU) da fila e do conjunto. Se a página está presente,
remove da fila e adiciona novamente ao início da fila."