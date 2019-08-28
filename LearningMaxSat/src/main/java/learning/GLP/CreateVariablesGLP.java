package learning.GLP;

import learning.Prefixes;
import java.util.ArrayList;

/**
 *
 * @author virginia
 */
public class CreateVariablesGLP {
    public static ArrayList<VariableX> variables = new ArrayList<VariableX>();

    public static void createVariables(int n) {
        variables.clear();
        int index = 1;
        for (int i = 0; i < Prefixes.prefixes.size(); i++) {
            for (int j = 0; j < n; j++) {
                variables.add(new VariableX(Prefixes.prefixes.get(i), j, index));
                index++;
            }
        }
        /*System.out.println("Variables GLP");
        for (int i = 0; i < variables.size(); i++) {
            System.out.print(variables.get(i).indice+"  ");
            System.out.println(variables.get(i).u + " q" +variables.get(i).state);
        }*/
    }
}