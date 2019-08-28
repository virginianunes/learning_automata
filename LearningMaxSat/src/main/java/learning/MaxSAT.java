package learning;

import org.junit.jupiter.api.Test;
import org.sat4j.core.VecInt;
import org.sat4j.maxsat.SolverFactory;
import org.sat4j.maxsat.WeightedMaxSatDecorator;
import org.sat4j.pb.OptToPBSATAdapter;
import org.sat4j.pb.PseudoOptDecorator;
import org.sat4j.reader.DimacsReader;
import org.sat4j.reader.Reader;
import org.sat4j.specs.*;

import java.util.ArrayList;
import java.util.List;

public class MaxSAT {
        public static List<VecInt> hardclauses;
        public static List<VecInt> softclauses;
        public static Reader reader;

    public MaxSAT() {
        hardclauses =  new ArrayList<VecInt>();
        softclauses = new ArrayList<VecInt>();
    }

    @Test
    public int[] solver (int qtdvar) throws ContradictionException, TimeoutException {
        WeightedMaxSatDecorator maxsat = new WeightedMaxSatDecorator(SolverFactory.newLight());
        maxsat.newVar(qtdvar);
        reader = new DimacsReader(maxsat);

        for (IVecInt c: hardclauses) {
            maxsat.addHardClause(c);
        }

        for (IVecInt c: softclauses) {
            maxsat.addSoftClause(c);
        }

        OptToPBSATAdapter opt = new OptToPBSATAdapter(new PseudoOptDecorator(maxsat));

        try {
            if (opt.isSatisfiable())
                System.out.println(reader.decode(opt.model()));
                return opt.model();
        } catch (Exception e) {
            System.out.println("Ocorreu um problema com o solver.");
        }
        return null;
    }
}