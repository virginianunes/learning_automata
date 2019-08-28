package learning.HV;

import learning.Alphabet;
import learning.GLP.VariableX;
import learning.AcessFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author virginia
 */
public class RestrictionsHV {
    private ArrayList<Integer> clauses; //Fórmula phi na CNF
    private ArrayList<VariableD> variablesD;
    private ArrayList<VariableX> variablesX;
    private ArrayList<VariableF> variablesF;
    private int n;
    private int count_clauses = 0;

    public RestrictionsHV(int n) throws IOException {
        this.clauses = new ArrayList<Integer>();
        this.variablesD = CreateVariablesHV.variablesD;
        this.variablesX = CreateVariablesHV.variablesX;
        this.variablesF = CreateVariablesHV.variablesF;
        this.n = n;
        this.count_clauses = 0;
    }
    
    public void restricao1 () throws IOException { //3.13
        for (int i = 0; i < variablesD.size(); ) {
            for (int j = 0; j < n; j++) {
                clauses.add(-variablesD.get(i).indice);
                i++;
            }
            clauses.add(0); // Disjunção = 0. Quebra de linha.
            count_clauses++;
        }
    }
    
    public void restricao2() throws IOException { //3.14
        for (int i = 0; i < variablesX.size(); ) {
            for (int j = 0; j < n; j++) {
                clauses.add(variablesX.get(i).indice);
                i++;
            }
            clauses.add(0);
            count_clauses++;
        }
    }
    
    public void restricao3 () throws IOException { //3.15
        for (int i = 0; i < variablesX.size(); i++) {
            for (int j = 0; j < variablesD.size(); j++) {
                if(variablesD.get(j).p == variablesX.get(i).state) {
                    String u = variablesX.get(i).u;
                    String a = variablesD.get(j).a;

                    String ua = u+a;
                    int k;
                    for (int l = 0; l <variablesX.size(); l++) {
                        if ((variablesX.get(l).u.equals(ua)) &&
                                (variablesX.get(l).state == variablesD.get(j).q)) {
                            clauses.add(-variablesX.get(i).indice);
                            clauses.add(-variablesD.get(j).indice);
                            clauses.add(variablesX.get(l).indice);
                            clauses.add(0);
                            count_clauses++;
                            break;
                        }   
                    }
                }
            }
        }
    }
    
    public void restricao4(List<String> Spositive, List<String> Snegative) throws IOException { //3.16
        for (int i = 0; i < variablesX.size();) {
            for (int j = 0; j < variablesF.size();j++) {
                if (Spositive.contains(variablesX.get(i).u)) {
                    clauses.add(-variablesX.get(i).indice);
                    clauses.add(variablesF.get(j).indice);
                    clauses.add(0);
                    count_clauses++;
                    
                } else if (Snegative.contains(variablesX.get(i).u)) {
                    clauses.add(-variablesX.get(i).indice);
                    clauses.add(-variablesF.get(j).indice);
                    clauses.add(0);
                    count_clauses++;
                    
                }
                i++;                     
            }
        }
    }
    
    public void restricao5 () throws IOException { //Restrição adicional 3.17
        for (int i = 0; i < variablesD.size();) {
            for (int j = 0; j < n; j++) {
                clauses.add(variablesD.get(i).indice);
                i++;
            }
            clauses.add(0);
            count_clauses++;
        }
    }
  
    public void restricao6 () throws IOException { //Restrição adicional 3.18
        for (int i = 0; i < variablesX.size();) {
            for (int j = 0; j < n; j++) {
                clauses.add(-variablesX.get(i).indice);
                i++;
            }
            clauses.add(0);
            count_clauses++;
        }
    }
    
    public void restricao7 () throws IOException { //Restrição adicional 3.19
        ArrayList<String> alfabeto = Alphabet.alphabet;
        
        for (int k = 0; k < alfabeto.size(); k++) {
            for (int i = 0; i < variablesX.size(); i++) {

                String u = variablesX.get(i).u;
                String a = alfabeto.get(k);
                for (int m = 0; m < variablesX.size(); m++) {
                    if (variablesX.get(m).u.equals(u+a)) {
                        clauses.add(-variablesX.get(i).indice);
                        clauses.add(-variablesX.get(m).indice);

                        for (VariableD d : variablesD) {
                            if (d.p == variablesX.get(i).state &&
                                d.q == variablesX.get(m).state &&
                                d.a.equals(a)) {
                                clauses.add(d.indice);
                                clauses.add(0);
                                count_clauses++;
                                break;

                            }
                        }

                    }

                }
            }
        }
        new AcessFile().writeDimacsFile(clauses, variablesD.size()+
            variablesX.size()+variablesF.size(), count_clauses);
        }
}
    
    

/*public static void restricao7 () throws IOException { //Restrição adicional 3.19
        
        ArrayList<String> alphabet = Alphabet.getAlfabeto();
       
        
            for (int k = 0; k < alphabet.size(); k++) {
                for (int i = 0; i < variablesX.size(); i++) {
                 
                    String u = variablesX.get(i).getU();
                    String a = alphabet.get(k);
                    for (int m = 0; m < variablesX.size(); m++) {
                        if (variablesX.get(m).getU().equals(u+a)) {
                            clauses.add(-variablesX.get(i).getIndice());
                            clauses.add(-variablesX.get(m).getIndice());
                            for (VariableD d : variablesD) {
                                if (d.getP() == variablesX.get(i).getEstado() &&
                                    d.getQ() == variablesX.get(m).getEstado() && 
                                    d.getLetter().equals(a)) {
                                    clauses.add(d.getIndice());
                                    clauses.add(0); // Disjunção = 0. Quebra de linha.
                                    count_clauses++;
                                    break;
                                }
                            }

                        }
                
                }
            }
        }
        //new AcessFile().writeDimacsFile(clauses, variablesD.size()+variablesX.size(), count_clauses);
        new AcessFile().writeDimacsFile(clauses, variablesD.size()+
                variablesX.size()+variablesF.size(), count_clauses);
    }*/