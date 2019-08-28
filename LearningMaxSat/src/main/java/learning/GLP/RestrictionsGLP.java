package learning.GLP;

import learning.Prefixes;
import learning.AcessFile;
import learning.MaxSAT;
import org.sat4j.core.VecInt;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author virginia
 */
public class RestrictionsGLP {
    private ArrayList<VariableX> variablesX;
    private int n;
    private MaxSAT maxsat;

    public RestrictionsGLP(int n, MaxSAT maxSAT) {
        this.variablesX = CreateVariablesGLP.variables;
        this.n = n;
        this.maxsat = maxSAT;
    }
    public void restriction1() { //Restrição 3.4 HARD CLAUSES
        for (int i = 0; i < variablesX.size();) {
            VecInt clauses = new VecInt();
            for (int j = 0; j < n; j++) {
                clauses.push(variablesX.get(i).indice);
                i++;
            }
            maxsat.hardclauses.add(clauses);
        }
    }
    public void restriction2() { //Restrição 3.5 HARD CLAUSES
        for (int i = 0; i < variablesX.size();) {
            VecInt clauses = new VecInt();
            for (int j = 0; j < n; j++) {
                clauses.push(-variablesX.get(i).indice);
                i++;
            }
            maxsat.hardclauses.add(clauses);
        }
    }

    public void restricao3(){ //Restrição 3.7 SOFT CLAUSES
        List<String> Spositive = AcessFile.Spositive;
        List<String> Snegative = AcessFile.Snegative;
        int x, y;
        for (int i = 0; i < Spositive.size(); i++) {

            for (int j = 0; j < Snegative.size(); j++){
                for (x = 0, y = 0; x < variablesX.size();) {
                    if (variablesX.get(x).u.equals(Spositive.get(i))){
                        if (variablesX.get(y).u.equals(Snegative.get(j))){
                            VecInt literals = new VecInt();
                            literals.push(-variablesX.get(x).indice);
                            literals.push(-variablesX.get(y).indice);
                            maxsat.softclauses.add(literals);
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
    }
    public void restricao4() { //Restrição 3.6 HARD CLAUSES

        ArrayList<String> prefixos = Prefixes.prefixes;
        String prefixo1;
        String prefixo2;
        int u;
        int ul;
        int ua;
        int ula;


        for (int i = 1; i < prefixos.size() - 1; i++) {
            for (int j = i + 1; j < prefixos.size(); j++) {
                prefixo1 = prefixos.get(i);
                prefixo2 = prefixos.get(j);

                if (prefixo1.substring(prefixo1.length() - 1).equals(prefixo2.substring(prefixo2.length() - 1))) {

                    for (int e = 0; e < n; e++) {////////// externo
                        for (u = 0; u < variablesX.size() - 1; u++) {
                            if (variablesX.get(u).u.equals(prefixo1.substring(0, prefixo1.length() - 1)) && variablesX.get(u).state == e) {
                                break;
                            }
                        }


                        for (ul = 0; ul < variablesX.size(); ul++) {
                            if (variablesX.get(ul).u.equals(prefixo2.substring(0, prefixo2.length() - 1)) && variablesX.get(ul).state == e) {
                                break;
                            }
                        }

                        for (int f = 0; f < n; f++) { ////// interno

                            for (ua = 0; ua < variablesX.size() - 1; ua++) {
                                if (variablesX.get(ua).u.equals(prefixo1) && variablesX.get(ua).state == f) {
                                    break;
                                }
                            }


                            for (ula = 0; ula < variablesX.size(); ula++) {
                                if (variablesX.get(ula).u.equals(prefixo2) && variablesX.get(ula).state == f) {
                                    break;
                                }
                            }
                            VecInt literals = new VecInt();
                            literals.push(-variablesX.get(u).indice);
                            literals.push(-variablesX.get(ul).indice);
                            literals.push(-variablesX.get(ua).indice);
                            literals.push(variablesX.get(ula).indice);

                            maxsat.hardclauses.add(literals);

                            VecInt literals1 = new VecInt();
                            literals1.push(-variablesX.get(u).indice);
                            literals1.push(-variablesX.get(ul).indice);
                            literals1.push(-variablesX.get(ula).indice);
                            literals1.push(variablesX.get(ua).indice);

                            maxsat.hardclauses.add(literals1);
                        }
                    }
                }
            }
        }
    }
}

/*public void restriction4(int num) throws IOException{ //Restrição 3.6
        ArrayList<String> prefixos = Pref.getPref();
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
                
                    for (int e = 0; e < num; e++) {////////// externo
                        for (u = 0; u < variablesX.size()-1; u++) {
                            if (Variaveis_glp.getVariables().get(u).getU().equals(prefixo1.substring(0 , prefixo1.length()-1)) && variablesX.get(u).getEstado() == e) {
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
                                        

                            for (ula = 0; ula < Variaveis_glp.getVariables().size(); ula++) {
                                if (variablesX.get(ula).getU().equals(prefixo2) && variablesX.get(ula).getEstado() == f) {
                                    break;
                                }    
                            }    
                            
                            indices.add(-variablesX.get(u).getIndice());
                            indices.add(-variablesX.get(ul).getIndice());
                            indices.add(-variablesX.get(ua).getIndice());
                            indices.add(variablesX.get(ula).getIndice());
                            indices.add(0);
                            
                            indices.add(-variablesX.get(u).getIndice());
                            indices.add(-variablesX.get(ul).getIndice());
                            indices.add(-variablesX.get(ula).getIndice());
                            indices.add(variablesX.get(ua).getIndice());
                            indices.add(0);
                            
                            this.count_clauses = this.count_clauses+2;
                        }
                    }
                }
            }
        }    
        new AcessFile().writer(indices, variablesX.size(), count_clauses);
    }*/



/*    public void restriction4(int num) throws IOException{ //Restrição 3.6
        ArrayList<String> prefixos = Pref.getPref();
        ArrayList<String> alfabeto = Alphabet.getAlfabeto();
        
        for (String a : alfabeto) {
            for (VariableX u : variablesX) {
                for (VariableX u1 : variablesX) {
                    if (!u.getU().equals(u1.getU()) &&
                            u.getEstado() == u1.getEstado()) {
                        for (VariableX x : variablesX) {
                            if (x.getU().equals(u.getU()+a)) {
                                for (VariableX x1 : variablesX) {
                                    if (x1.getU().equals(u1.getU()+a) &&
                                            x1.getEstado() == x.getEstado()) {
                                        indices.add(-u.getIndice());
                                        indices.add(-u1.getIndice());
                                        indices.add(-x.getIndice());
                                        indices.add(x1.getIndice());
                                        indices.add(0);

                                        indices.add(-u.getIndice());
                                        indices.add(-u1.getIndice());
                                        indices.add(-x.getIndice());
                                        indices.add(x1.getIndice());
                                        indices.add(0);

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
        new AcessFile().writer(indices, variablesX.size(), count_clauses);
    }
}*/