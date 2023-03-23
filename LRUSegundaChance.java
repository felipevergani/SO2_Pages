import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class LRUSegundaChance {
    public static void main(String[] args) throws IOException {

        int[] numFreeFrames = {2, 4, 8, 16, 32, 64};

        String traceFile = args[0];

        for (int n : numFreeFrames) {

           // Inicializando a lista de frames de página,
           // a tabela de páginas e a lista de segunda chance para cada teste
            List<Integer> pageFrames = new ArrayList<>(n);
            Map<Integer, Integer> pageTable = new HashMap<>();
            Map<Integer, Boolean> pageSecondChance = new HashMap<>();
            BufferedReader reader = new BufferedReader(new FileReader(traceFile));
            String line;
            int numFaults = 0;

            while ((line = reader.readLine()) != null) {
                line = line.replace(" ", "0");
                // Convertendo o endereço de página hexadecimal
                // em um número inteiro de 32 bits e obtendo o número da página
                int pageNum = Integer.parseInt(line, 16) >> 12;
                //System.out.println("Linha: " + line + " Numero de Pagina: " + pageNum);
                if (!pageTable.containsKey(pageNum)) {
                   // A página não está presente na tabela de páginas, 
                   //portanto ocorreu uma falha de página

                    numFaults++;
                    if (pageFrames.size() == n) {
                        // Não há espaço livre na lista de frames de página,
                        // portanto é necessário substituir uma página existente
                        int lruPage = -1;
                        while (lruPage == -1) {
                            int oldestPage = pageFrames.get(0);
                         // Verificando se a página tem uma segunda chance    
                            boolean secondChance = pageSecondChance.get(oldestPage);
                            if (secondChance) {
                                // A página tem uma segunda chance, portanto ela é movida para
                                // o final da lista de frames de página e recebe uma nova chance   
                                pageSecondChance.put(oldestPage, false);
                                pageFrames.remove(0);
                                pageFrames.add(oldestPage);
                            } else {
                              // A página não tem uma segunda chance, 
                              //portanto é selecionada para substituição 
                                pageTable.remove(oldestPage);
                                pageSecondChance.remove(oldestPage);
                                lruPage = oldestPage;
                            }
                        }
                    }
                     // Adicionando a nova página à tabela de páginas, 
                     // a lista de frames de página e marcando-a como sem segunda chance
                    pageTable.put(pageNum, pageFrames.size());
                    pageSecondChance.put(pageNum, false);
                    pageFrames.add(pageNum);
                } else {
                    // A página já está presente na lista de frames de página,
                    // portanto ela recebe uma segunda chance
                    int frameIndex = pageTable.get(pageNum);
                    pageSecondChance.put(pageNum, true);
                    // Atualizando a tabela de páginas para as 
                    // páginas subsequentes na lista de frames de página
                    for (int i = frameIndex + 1; i < pageFrames.size(); i++) {
                        int nextPage = pageFrames.get(i);
                        boolean secondChance = pageSecondChance.get(nextPage);
                        if (secondChance) {
                            pageSecondChance.put(nextPage, false);
                        } else {
                            pageTable.put(nextPage, i - 1);
                        }
                    }
                }
            }
            reader.close();
            System.out.printf("Número de frames livres: %d | Número de falhas de página: %d%n", n, numFaults);
        }
    }
}
