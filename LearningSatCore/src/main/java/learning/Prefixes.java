package learning;

import java.util.ArrayList;

/**
 * Essa classe recebe as amostras S+ S- e calcula seus prefixes.
 */
public class Prefixes {
    public static ArrayList<String> prefixes = new ArrayList<String>();
    
    public static void createPrefixes() {
        prefixes.clear();
        //Add the string ε.
        prefixes.add("");
        for (String string: AcessFile.Spositive) {
            for (int j = 1; j <= string.length(); j++) {
                if (!prefixes.contains(string.substring(0, j))) {
                    prefixes.add(string.substring(0,j));
                }
            }
        }
        for (String string: AcessFile.Snegative) {
            for (int j = 1; j <= string.length(); j++) {
                if (!prefixes.contains(string.substring(0, j))) {
                    prefixes.add(string.substring(0,j));
                }
            }
        }
        /*//Trecho de código apenas para ilustrar o vetor de prefixes
        System.out.println("\nVetor de Prefixos das Amostras");
        for (int i = 0; i < prefixes.size(); i++) {
            System.out.print(prefixes.get(i) + " ");
        }
        System.out.println();*/
    }
}
