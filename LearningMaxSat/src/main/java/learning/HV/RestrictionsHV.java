package learning.HV;

import learning.Alphabet;
import learning.GLP.VariableX;
import learning.MaxSAT;
import org.sat4j.core.VecInt;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author virginia
 */
public class RestrictionsHV {
    private List<VariableD> variablesD;
    private List<VariableX> variablesX;
    private List<VariableF> variablesF;
    private int n;
    //private VecInt literals;
    private MaxSAT maxsat;

    public RestrictionsHV(int n, MaxSAT maxSAT) {
        this.variablesD = CreateVariablesHV.variablesD;
        this.variablesX = CreateVariablesHV.variablesX;
        this.variablesF = CreateVariablesHV.variablesF;
        this.n = n;
        this.maxsat = maxSAT;
        //this.literals = new VecInt();
        //this.sat = new MaxSAT();
    }
    
    public void restriction1() { //3.13 HARD CLAUSES
        //literals = new VecInt();
        //VecInt clauses = new VecInt();
        //literals.clear();
        for (int i = 0; i < variablesD.size(); ) {
            VecInt clauses = new VecInt();
            for (int j = 0; j < n; j++) {
                clauses.push(-variablesD.get(i).indice);
                i++;
            }
            maxsat.hardclauses.add(clauses);
            //literals = new VecInt();
            //literals.clear();
        }
    }
    
    public void restriction2() { //3.14 HARD CLAUSES
        //literals = new VecInt();
        //literals.clear();
        //VecInt clauses = new VecInt();
        for (int i = 0; i < variablesX.size(); ) {
            VecInt clauses = new VecInt();
            for (int j = 0; j < n; j++) {
                clauses.push(variablesX.get(i).indice);
                i++;
            }
            maxsat.hardclauses.add(clauses);
            //literals.clear();
            //literals = new VecInt();
        }
    }
    
    public void restriction3() { //3.15 HARD CLAUSES
        //literals = new VecInt();
        //literals.clear();
        //VecInt clauses = new VecInt();
        for (int i = 0; i < variablesX.size(); i++) {

            for (int j = 0; j < variablesD.size(); j++) {
                VecInt clauses = new VecInt();
                if(variablesD.get(j).p == variablesX.get(i).state) {
                    String u = variablesX.get(i).u;
                    String a = variablesD.get(j).a;

                    String ua = u+a; //Concatena u e a

                    int k;
                    //Verifica se existe alguma variável VariableX u,q , tal que u == ua
                    for (int l = 0; l < variablesX.size(); l++) {
                        if ((variablesX.get(l).u.equals(ua)) &&
                                (variablesX.get(l).state == variablesD.get(j).q)) {

                            clauses.push(-variablesX.get(i).indice);
                            clauses.push(-variablesD.get(j).indice);
                            clauses.push(variablesX.get(l).indice);
                            maxsat.hardclauses.add(clauses);
                            //clauses = new VecInt();
                            //literals.clear();
                            break;
                        }   
                    }
                }
            }
        }
    }
    
    public void restriction4(List<String> Spositive, List<String> Snegative) throws IOException { //3.16 SOFT CLAUSES
        //literals = new VecInt();
        //literals.clear();
        //VecInt clauses = new VecInt();
        for (int i = 0; i < variablesX.size();) {

            for (int j = 0; j < variablesF.size(); j++) {
                VecInt clauses = new VecInt();
                if (Spositive.contains(variablesX.get(i).u)) {
                    //literals.clear();
                    clauses.push(-variablesX.get(i).indice);
                    clauses.push(variablesF.get(j).indice);
                    maxsat.softclauses.add(clauses);
                    //clauses = new VecInt();
                    //literals.clear();
                    
                } else if (Snegative.contains(variablesX.get(i).u)) {
                    //literals.clear();
                    clauses.push(-variablesX.get(i).indice);
                    clauses.push(-variablesF.get(j).indice);
                    maxsat.softclauses.add(clauses);
                    //clauses = new VecInt();
                    //literals.clear();
                }
                i++;                     
            }
        }
    }
    
    public void restriction5() { //Restrição adicional 3.17 HARD CLAUSES
        //literals = new VecInt();
        //literals.clear();
        //VecInt clauses = new VecInt();
        for (int i = 0; i < variablesD.size();) {
            VecInt clauses = new VecInt();
            for (int j = 0; j < n; j++) {
                //clauses.clear();
                clauses.push(variablesD.get(i).indice);
                i++;
            }
            maxsat.hardclauses.add(clauses);
            //clauses = new VecInt();
            //literals.clear();
        }
    }
  
    public void restriction6() throws IOException { //Restrição adicional 3.18 HARD CLAUSES
        //literals = new VecInt();
        //VecInt clauses = new VecInt();
        //literals.clear();
        for (int i = 0; i < variablesX.size();) {
            VecInt clauses = new VecInt();
            for (int j = 0; j < n; j++) {
                //clauses.clear();
                clauses.push(-variablesX.get(i).indice);
                i++;
            }
            maxsat.hardclauses.add(clauses);
            //literals.clear();
            //literals = new VecInt();
        }
    }
    
    public void restriction7() { //Restrição adicional 3.19 HARD CLAUSES
        ///literals = new VecInt();
        //ArrayList<String> alfabeto = alfabet.alfabeto;
        //VecInt clauses = new VecInt();
        //literals.clear();

        for (int k = 0; k < Alphabet.alphabet.size(); k++) {
            for (int i = 0; i < variablesX.size(); i++) {

                String u = variablesX.get(i).u;
                String a = Alphabet.alphabet.get(k);
                for (int m = 0; m < variablesX.size(); m++) {
                    VecInt clauses = new VecInt();
                    if (variablesX.get(m).u.equals(u+a)) {
                        //clauses.clear();
                        clauses.push(-variablesX.get(i).indice);
                        clauses.push(-variablesX.get(m).indice);
                        for (VariableD variableD : variablesD) {
                            if (variableD.p == variablesX.get(i).state &&
                                variableD.q == variablesX.get(m).state &&
                                variableD.a.equals(a)) {
                                clauses.push(variableD.indice);
                                maxsat.hardclauses.add(clauses);
                                //literals = new VecInt();
                                //literals.clear();
                                /*clauses.clear();*/
                                break;
                            }
                        }

                    }
                }
            }
        }
       // return sat.solver(variablesD.size()+ variablesX.size()+ variablesF.size());
     }
}
    
    

/*public static void restriction7 () throws IOException { //Restrição adicional 3.19
        
        ArrayList<String> alfabeto = Alphabet.getAlfabeto();
       
        
            for (int k = 0; k < alfabeto.size(); k++) {
                for (int i = 0; i < variablesX.size(); i++) {
                 
                    String u = variablesX.get(i).getU();
                    String a = alfabeto.get(k);
                    for (int m = 0; m < variablesX.size(); m++) {
                        if (variablesX.get(m).getU().equals(u+a)) {
                            indices.add(-variablesX.get(i).getIndice());
                            indices.add(-variablesX.get(m).getIndice());
                            for (VariableD d : variablesD) {
                                if (d.getP() == variablesX.get(i).getEstado() &&
                                    d.getQ() == variablesX.get(m).getEstado() &&
                                    d.getLetter().equals(a)) {
                                    indices.add(d.getIndice());
                                    indices.add(0); // Disjunção = 0. Quebra de linha.
                                    count_clauses++;
                                    break;
                                }
                            }

                        }
                
                }
            }
        }
        //new AcessFile().writer(indices, variablesD.size()+variablesX.size(), count_clauses);
        new AcessFile().writer(indices, variablesD.size()+
                variablesX.size()+variablesF.size(), count_clauses);
    }*/