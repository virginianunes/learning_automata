package learning.HV;

import learning.Variable;

/**
 *
 * @author virginia
 */
public class VariableD extends Variable {
    public String a;
    public int p;
    public int q;

    public VariableD(int p, String a, int q, int indice) {
        this.p = p;
        this.a = a;
        this.q = q;
        super.indice = indice;
    }
}
