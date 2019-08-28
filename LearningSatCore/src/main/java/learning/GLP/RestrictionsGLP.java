package learning.GLP;

import learning.AcessFile;
import learning.Alphabet;
import learning.Prefixes;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author virginia
 */
public class RestrictionsGLP {
    private ArrayList<Integer> clauses;
    private ArrayList<VariableX> variablesX;
    private int n;
    private int count_clauses = 0;
    
    public RestrictionsGLP(int n) throws IOException {
        this.clauses = new ArrayList<Integer>();
        this.variablesX = CreateVariablesGLP.variables;
        this.n = n;
    }
    public void restriction1(){ //Restrição 3.4
        for (int i = 0; i < variablesX.size();) {
            for (int j = 0; j < n; j++) {
                clauses.add(variablesX.get(i).indice);
                i++;
            }
            clauses.add(0); // Disjunção = 0. Quebra de linha.
            count_clauses++;
        }
    }
    public void restriction2(){ //Restrição 3.5
        for (int i = 0; i < variablesX.size();) {
            for (int j = 0; j < n; j++) {
                clauses.add(-variablesX.get(i).indice);
                i++;
            }
            clauses.add(0);
            count_clauses++;
        }
    }

    public void restriction3() throws IOException { //Restrição 3.6
        ArrayList<String> prefixos = Prefixes.prefixes;
        String prefixo1;
        String prefixo2;
        int u;
        int ul;
        int ua;
        int ula;


        for(int i = 1; i < prefixos.size()-1; i++){
            for (int j = i+1; j < prefixos.size(); j++) {
                prefixo1 = prefixos.get(i);
                prefixo2 = prefixos.get(j);

                if (prefixo1.substring(prefixo1.length()-1).equals(prefixo2.substring(prefixo2.length()-1))){

                    for (int e = 0; e < n; e++) {////////// externo
                        for (u = 0; u < variablesX.size()-1; u++) {
                            if (CreateVariablesGLP.variables.get(u).u.equals(prefixo1.substring(0 , prefixo1.length()-1)) && variablesX.get(u).state == e) {
                                break;
                            }
                        }


                        for (ul = 0; ul < variablesX.size(); ul++) {
                            if (variablesX.get(ul).u.equals(prefixo2.substring(0, prefixo2.length()-1)) && variablesX.get(ul).state == e) {
                                break;
                            }
                        }

                        for (int f = 0; f < n; f++) { ////// interno

                            for (ua = 0; ua < variablesX.size()-1; ua++) {
                                if (variablesX.get(ua).u.equals(prefixo1) && variablesX.get(ua).state == f) {
                                    break;
                                }
                            }


                            for (ula = 0; ula < CreateVariablesGLP.variables.size(); ula++) {
                                if (variablesX.get(ula).u.equals(prefixo2) && variablesX.get(ula).state == f) {
                                    break;
                                }
                            }

                            clauses.add(-variablesX.get(u).indice);
                            clauses.add(-variablesX.get(ul).indice);
                            clauses.add(-variablesX.get(ua).indice);
                            clauses.add(variablesX.get(ula).indice);
                            clauses.add(0);

                            clauses.add(-variablesX.get(u).indice);
                            clauses.add(-variablesX.get(ul).indice);
                            clauses.add(-variablesX.get(ula).indice);
                            clauses.add(variablesX.get(ua).indice);
                            clauses.add(0);

                            this.count_clauses = this.count_clauses+2;
                        }
                    }
                }
            }
        }
    }
    public void restriction4(List<String> Spositive, List<String> Snegative) throws IOException{ //Restrição 3.7
        int x, y;
        for (int i = 0; i < Spositive.size(); i++) {
            for (int j = 0; j < Snegative.size(); j++){
                for (x = 0, y = 0; x < variablesX.size();) {
                    if (variablesX.get(x).u.equals(Spositive.get(i))){
                        if (variablesX.get(y).u.equals(Snegative.get(j))){
                            clauses.add(-variablesX.get(x).indice);
                            clauses.add(-variablesX.get(y).indice);
                            clauses.add(0); // Disjunção = 0. Quebra de linha.
                            this.count_clauses++;
                            x++; y++;
                        } else {
                            y++;
                        }
                    } else {
                        x++;
                    }
                }
                x = 0; y = 0;
            }
        }
        new AcessFile().writeDimacsFile(clauses, variablesX.size(), count_clauses);
    }
}

/*public void restriction4(int num) throws IOException{ //Restrição 3.6
        ArrayList<String> prefixes = Prefixes.getPref();
        String prefixo1;
        String prefixo2;
        int u;
        int ul;
        int ua;
        int ula;


        for(int i = 1; i < prefixes.size()-1; i++){
            for (int j = i+1; j < prefixes.size(); j++) {
                prefixo1 = prefixes.get(i);
                prefixo2 = prefixes.get(j);

                if (prefixo1.substring(prefixo1.length()-1).equals(prefixo2.substring(prefixo2.length()-1))){

                    for (int e = 0; e < num; e++) {////////// externo
                        for (u = 0; u < variablesX.size()-1; u++) {
                            if (CreateVariablesGLP.getVariables().get(u).getU().equals(prefixo1.substring(0 , prefixo1.length()-1)) && variablesX.get(u).getEstado() == e) {
                                break;
                            }
                        }


                        for (ul = 0; ul < variablesX.size(); ul++) {
                            if (variablesX.get(ul).getU().equals(prefixo2.substring(0, prefixo2.length()-1)) && variablesX.get(ul).getEstado() == e) {
                                break;
                            }
                        }

                        for (int f = 0; f < num; f++) { ////// interno

                            for (ua = 0; ua < variablesX.size()-1; ua++) {
                                if (variablesX.get(ua).getU().equals(prefixo1) && variablesX.get(ua).getEstado() == f) {
                                    break;
                                }
                            }


                            for (ula = 0; ula < CreateVariablesGLP.getVariables().size(); ula++) {
                                if (variablesX.get(ula).getU().equals(prefixo2) && variablesX.get(ula).getEstado() == f) {
                                    break;
                                }
                            }

                            clauses.add(-variablesX.get(u).getIndice());
                            clauses.add(-variablesX.get(ul).getIndice());
                            clauses.add(-variablesX.get(ua).getIndice());
                            clauses.add(variablesX.get(ula).getIndice());
                            clauses.add(0);

                            clauses.add(-variablesX.get(u).getIndice());
                            clauses.add(-variablesX.get(ul).getIndice());
                            clauses.add(-variablesX.get(ula).getIndice());
                            clauses.add(variablesX.get(ua).getIndice());
                            clauses.add(0);

                            this.count_clauses = this.count_clauses+2;
                        }
                    }
                }
            }
        }
        new AcessFile().writeDimacsFile(clauses, variablesX.size(), count_clauses);
    }*/



/*    public void restriction4(int num) throws IOException{ //Restrição 3.6
        ArrayList<String> prefixes = Prefixes.getPref();
        ArrayList<String> alphabet = Alphabet.getAlfabeto();
        
        for (String a : alphabet) {
            for (VariableX u : variablesX) {
                for (VariableX u1 : variablesX) {
                    if (!u.getU().equals(u1.getU()) &&
                            u.getEstado() == u1.getEstado()) {
                        for (VariableX x : variablesX) {
                            if (x.getU().equals(u.getU()+a)) {
                                for (VariableX x1 : variablesX) {
                                    if (x1.getU().equals(u1.getU()+a) &&
                                            x1.getEstado() == x.getEstado()) {
                                        clauses.add(-u.getIndice());
                                        clauses.add(-u1.getIndice());
                                        clauses.add(-x.getIndice());
                                        clauses.add(x1.getIndice());
                                        clauses.add(0);

                                        clauses.add(-u.getIndice());
                                        clauses.add(-u1.getIndice());
                                        clauses.add(-x.getIndice());
                                        clauses.add(x1.getIndice());
                                        clauses.add(0);

                                        this.count_clauses = this.count_clauses+2;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        new AcessFile().writeDimacsFile(clauses, variablesX.size(), count_clauses);
    }
}*/



//lastt
/*
*/
