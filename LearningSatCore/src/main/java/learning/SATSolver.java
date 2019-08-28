package learning;

import java.io.FileNotFoundException;
import java.io.IOException;
import learning.AcessFile;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.reader.DimacsReader;
import org.sat4j.reader.ParseFormatException;
import org.sat4j.reader.Reader;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;

/**
 *
 * @author virginia
 */
public class SATSolver {

    public static ISolver solver;
    public static IProblem problem;
    public static Reader reader;
    public static String path;

    public SATSolver() {
        solver = SolverFactory.newDefault();
        solver.setTimeout(10800); // 1 hour timeout
        reader = new DimacsReader(solver);
        path = AcessFile.dimacsCNF;
    }
    
    public static int[] solver () throws IOException, ParseFormatException, ContradictionException, TimeoutException {
        problem = reader.parseInstance(path);
        if ( problem.isSatisfiable ()) {
            System.out.println(reader.decode(problem.model()));
            return problem.model();
        }
        return null;
    }    
    
    public static boolean isSatisfiable(int n) {
        // CNF filename is given on the command line
        try {
            problem = reader.parseInstance(path);
            if ( problem.isSatisfiable ()) {
                System.out.println(" Satisfiable with "+ (n) +" states!");
                return true;
            } else {
                System.out.println("Unsatisfiable with "+ (n) +" states!");
                return false;
            }
        } catch ( FileNotFoundException e) {
        // TODO Auto - generated catch block
        } catch ( ParseFormatException e) {
        // TODO Auto - generated catch block
        } catch ( IOException e) {
        // TODO Auto - generated catch block
        } catch ( ContradictionException e) {
            /*System .out . println (" Unsatisfiable ( trivial )!");*/
        } catch ( TimeoutException e) {
            System .out . println (" Timeout , sorry !");
        }
        return false;
    }
}   