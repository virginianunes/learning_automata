package learning;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author virginia
 */
public class Alphabet {

    public static ArrayList<String> alphabet = new ArrayList<String>();;

    public static void createAlphabet() {
        alphabet.clear();
        char[] symbols = null;
        for (String string: Prefixes.prefixes) {
            symbols = string.toCharArray();
            for (char symbol: symbols) {
                if (!alphabet.contains(String.valueOf(symbol)))
                    alphabet.add(String.valueOf(symbol));
            }
        }
        System.out.println("Size of the alphabet: "+alphabet.size());
        //To see the alphabet.
        /*for (Iterator<String> iterator = alphabet.iterator(); iterator.hasNext();) {
            String next = iterator.next();
            System.out.println(next);
        }*/
    }
}