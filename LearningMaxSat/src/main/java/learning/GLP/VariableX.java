package learning.GLP;

import learning.Variable;

/**
 * Essa classe é responsável por representar a estrutura de variáveis booleanas VariableX.
 * @author virginia
 */
public class VariableX extends Variable {
    public String u;
    public int state;

    public VariableX(String u, int state, int indice) {
        this.u = u;
        this.state = state;
        super.indice = indice;
    }
}
