import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class LRUtest {
    public static void main(String[] args) throws IOException {
        // Tamanho da página em bytes
        int pageSize = 4096;
        // Número de frames livres
        int[] numFreeFrames = { 1, 2, 4, 8, 16, 32, 65536 };
        // Arquivo de log de acesso à memória
        String traceFile = "traces/lu.txt";

        for (int n : numFreeFrames) {
            int numPages = n; 
            List<Integer> pageFrames = new ArrayList<>(numPages); // Lista de quadros de página
            Map<Integer, Integer> pageTable = new HashMap<>(); // Tabela de páginas
            BufferedReader reader = new BufferedReader(new FileReader(traceFile));
            String line;
            int numFaults = 0; // Contagem de falhas de página
            
            while ((line = reader.readLine()) != null) {
                line = line.replace(" ", "0"); // espaço em branco convertido em 0
                int pageNum = Integer.parseInt(line, 16) / pageSize; // Número da página acessada
                if (!pageTable.containsKey(pageNum)) { // Se a página não estiver na tabela de páginas
                    numFaults++; // Incrementa a contagem de falhas de página
                    if (pageFrames.size() == numPages) { // Se a lista de quadros de página estiver cheia
                        int lruPage = pageFrames.get(0); // Remove a página menos recentemente usada
                        pageTable.remove(lruPage); // Remove a página da tabela de páginas
                        pageFrames.remove(0); // Remove o quadro de página da lista
                    }
                    pageTable.put(pageNum, pageFrames.size()); // Adiciona a página à tabela de páginas
                    pageFrames.add(pageNum); // Adiciona o quadro de página à lista
                } else { // Se a página estiver na tabela de páginas
                    int frameIndex = pageTable.get(pageNum); // Obtém o índice do quadro de página
                    pageFrames.remove(frameIndex); // Remove o quadro de página da lista
                    pageFrames.add(pageNum); // Adiciona o quadro de página à lista
                    for (int i = frameIndex; i < pageFrames.size(); i++) {
                        pageTable.put(pageFrames.get(i), i); // Atualiza a tabela de páginas
                    }
                }
            }
            reader.close();
            System.out.printf("Número de frames livres: %d | Número de falhas de página: %d%n", n, numFaults);
        }
    }
}