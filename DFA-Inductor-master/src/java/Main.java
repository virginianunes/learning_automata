import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.BooleanOptionHandler;
import org.sat4j.reader.ParseFormatException;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.TimeoutException;
import java.io.*;
import java.util.Arrays;
import java.util.Locale;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Main {

    @Option(name = "--sizeup", aliases = {"-su"}, usage = "maximum automaton" + " size", metaVar = "<maxsimum size>",
            required = true)
    private int maxSize=8;

    @Option(name = "--sizedown", aliases = {"-sd"}, usage = "minimum automaton size", metaVar = "<minimum size>")
    private int minSize = 1;

    @Option(name = "--result", aliases = {"-r"}, usage = "write result automaton to this file with extension .dot",
            metaVar = "<result file>")
    private String resultFilePath = "/home/virginia/MEGAsync/Códigos/Datasets/automato_bfs";
    /*private String resultFilePath = "ans";*/

    @Option(name = "--strategy", aliases = {"-sb"}, usage = "symmetry breaking strategy (0 - none, 1 - BFS, " +
            "2 - DFS" + ", 3 - clique)", metaVar = "<SB strategy>")
    private int SBStrategy = 1;

    @Option(name = "--timeout", aliases = {"-t"}, usage = "timeout", metaVar = "<timeout>")
    private int timeout = 10000;

    @Option(name = "--solver", aliases = {"-sat"}, usage = "external SAT solver. using sat4j by default",
            metaVar = "<SAT solver>")
    private String externalSATSolver;

    @Option(name = "--dimacs", aliases = {"-d"}, usage = "write dimacs file with CNF to this file",
            metaVar = "<dimacs file>")
    private String dimacsFile = "/home/virginia/MEGAsync/Códigos/Datasets/dimacs_file_bfs.cnf";
    /*private String dimacsFile = "dimacsFile.cnf";*/

    @Option(name = "--log", aliases = {"-l"}, usage = "write log to this file", metaVar = "<log>")
    private String logFile;

    @Option(name = "--percent", aliases = {"-p"}, usage = "percent of noisy data", metaVar = "<noisy percent>")
    private int p = 0;
    //private int p = 20;

    @Option(name = "--findall", aliases = {"-a"}, usage = "find all mode", metaVar = "<find all>",
            handler = BooleanOptionHandler.class)
    private boolean findAllMode;

    @Option(name = "--find", aliases = {"-f"}, usage = "find COUNT or less", metaVar = "<find>")
    private int findCount = 0;

    @Option(name = "--iterativemode", aliases = {"-itm"}, usage = "iterative mode for findK; iterative SAT-solver" +
            "should be used", metaVar = "<iterative mode>", hidden = true, handler = BooleanOptionHandler.class)
    private boolean iterativeMode = false;

    @Option(name = "--iterativesolver", aliases = {"-its"}, usage = "iterative solver",
            metaVar = "<iterative solver>", hidden = true)
    private boolean iterativeSolver = false;

    @Option(name = "--loop", hidden = true, usage = "fixing free transitions into loop",
            handler = BooleanOptionHandler.class)
    private boolean loopMode = false;

    @Option(name = "--atmostone", aliases = {"-amo"}, usage = "at most one constraints encoding." +
            "1 - pairwise, 2 - binary, 3 - commander where m=sqrt(n)," +
            "4 - commander where m=n/2, 5 - product, 6 - sequential," +
            "7 - bimander where m=sqrt(n), 8 - bimander where m=n/2",
            metaVar = "<amo>")
    private int amo = 1;

    @Option(name = "--backtracking", aliases = {"-bt"}, usage = "using backtracking instead of SAT approach",
            forbids = {"--strategy", "-sb", "--solver", "-sat", "--dimacs", "-d", "--atmostone", "-amo", "--percent",
                    "-p"}, handler = BooleanOptionHandler.class)
    private boolean backtrackingMode = false;

    @Argument(usage = "dictionary file", metaVar = "<file>", required = true)
    //private String file = "/home/virginia/MEGAsync/Códigos/training_";
    private String file = "/home/virginia/MEGAsync/Códigos/Datasets/waltz_tests/training_";

    private static Logger logger = Logger.getLogger("Logger");

    private void launch(String... args) throws FileNotFoundException {
        long fullStartTime = System.currentTimeMillis();
        CmdLineParser parser = new CmdLineParser(this);
        boolean noisyMode = p > 0;
        findAllMode |= findCount > 0;
        loopMode |= findAllMode;
        if (SBStrategy == 3 && noisyMode) {
            System.err.println("You can't use CLIQUE symmetry breaking strategy during solving " +
                    "noisy DFA building problem");
        }

        if (logFile != null) {
            try {
                FileHandler fh = new FileHandler(logFile, false);
                logger.addHandler(fh);
                fh.setFormatter(new SimpleFormatter());
                logger.setUseParentHandlers(false);
                System.out.println("Log file: " + logFile);
            } catch (Exception e) {
                System.err.println("Problem with the log file: " + logFile + ". " + e.getMessage());
                return;
            }
        }

        int curDFA = 1;

        int executions = 30;
        String[][] contents = new String[executions][];
        for (int i = 0; i < executions; i++) {
            try (InputStream is = new FileInputStream(file.concat((i+1) + ".dct"))) {
                logger.info("Working with file \"" + file + "\" started");

                APTA apta = new APTA(is);
                logger.info("APTA was successfully built");

                logger.info("APTA size: " + apta.getSize());
                logger.info("Ends in APTA: " + (apta.getAcceptableNodes().size() + apta.getRejectableNodes().size()));
                logger.info("Count of words: " + apta.getCountOfWords());
                if (!backtrackingMode) {
                    ConsistencyGraph cg = new ConsistencyGraph(apta, noisyMode);
                    if (!noisyMode) {
                        logger.info("CG was successfully built");
                    }
                    if (!noisyMode) {
                        cg.findClique();
                        int cliqueSize = cg.getCliqueSize();
                        minSize = Math.max(cliqueSize, minSize);
                        logger.info("Clique was found. Its size is " + cliqueSize + ".");
                        logger.info("Searching will be started from size " + minSize + ".");
                    }


                        boolean found = false;
                        int low = 0;
                        int high = maxSize;
                        int colors = 0;
                        int mid = 0;
                        long startTime = System.currentTimeMillis();
                        while (true) {
                            mid = binarySearch(low, high);
                            if (mid == -1) {
                                DimacsFileGenerator dfg = new DimacsFileGenerator(apta, cg, colors, SBStrategy, p, dimacsFile, loopMode);
                                dfg.generateFile(amo);
                                break;
                            }
                            logger.info("Try to build automaton with " + mid + " colors");

                            try {
                                DimacsFileGenerator dfg = new DimacsFileGenerator(apta, cg, mid, SBStrategy, p, dimacsFile, loopMode);
                                dfg.generateFile(amo);
                                logger.info("SAT problem in dimacs format successfully generated --------------");
                                SATSolver solver = null;
                                if (!(findAllMode && iterativeMode && curDFA > 1)) {
                                    solver = new SATSolver(apta, mid, dimacsFile,
                                            (int) (timeout - ((System.currentTimeMillis() - fullStartTime)) / 1000.),
                                            externalSATSolver, iterativeMode, iterativeSolver);
                                }
                                if (solver == null) {
                                    throw new NullPointerException("Something gone wrong with solver initialization.");
                                }
                                logger.info("SAT solver successfully initialized");

                                logger.info("Vars in the SAT problem: " + solver.nVars());
                                logger.info("Constraints in the SAT problem: " + solver.nConstraints());

                                String DFAnumber = " ";
                                if (findAllMode) {
                                    DFAnumber = " number " + String.valueOf(curDFA) + " ";
                                }
                                if (solver.problemIsSatisfiable()) {
                                    found = true;
                                    logger.info("The automaton" + DFAnumber + "with " + mid + " colors was found! :)");
                                    logger.info("Execution time: " + (System.currentTimeMillis() - startTime) / 1000.);
                                    colors = mid;
                                    high = mid;
                                    int[] model = null;
                                    try {
                                        model = solver.getModel();
                                    } catch (Exception e) {
                                        logger.warning("Some problem with SATSolver. Shouldn't be here. " +
                                                "Exception: " + e.getMessage());
                                    }
                                    Automaton automaton = AutomatonBuilder.build(model, dfg, apta, colors, noisyMode);
                                    String fullResultFilePath = resultFilePath;
                                    if (findAllMode) {
                                        fullResultFilePath += fineNumber(curDFA);
                                    }
                                    fullResultFilePath += ".dot";
                                    try (PrintWriter pw = new PrintWriter(fullResultFilePath)) {
                                        pw.print(automaton + "\n");
                                    } catch (IOException e) {
                                        logger.info("Problem with result file: " + e.getMessage());
                                    }
                                    if (findAllMode) {
                                        dfg.banSolution(automaton, model);
                                        curDFA++;
                                        if (findCount > 0 && curDFA > findCount) {
                                            break;
                                        }
                                    }
                                } else {
                                    logger.info("The automaton with " + mid + " colors wasn't found! :(");
                                    logger.info("Execution time: " + (System.currentTimeMillis() - startTime) / 1000.);
                                    low = mid + 1;
                                }
                            } catch (TimeoutException e) {
                                logger.info("Timeout " + timeout + " seconds was reached");
                                logger.info("Execution time: " + timeout);
                                break;
                            } catch (IOException e) {
                                logger.warning("Some problem with generating dimacs file: " + e.getMessage());
                                return;
                            } catch (ParseFormatException e) {
                                logger.warning("Some problem with parsing dimacs file: " + e.getMessage());
                            } catch (ContradictionException e) {
                                e.printStackTrace();
                            }
                        }
                        Validator v = new Validator();
                        v.i = i+1;
                        v.run();
                        contents[i] = new String[]{
                                String.valueOf(p),
                                String.valueOf(colors),
                                String.valueOf(((System.currentTimeMillis() - startTime) / 1000.)),
                                String.valueOf(v.getAccurace()),
                                String.valueOf(v.getPrecision()),
                                String.valueOf(v.getRecall()),
                                String.valueOf(v.getSpecificity()),
                                String.valueOf(v.getFScore())

                        };
                    }


            } catch (IOException e) {
                logger.warning("Some unexpected problem with file \"" + file + "\":" + e.getMessage());
            }
        }
        writerInCSV("/home/virginia/MEGAsync/Códigos/Datasets/result.csv", contents);
        logger.info("Full time: " + (System.currentTimeMillis() - fullStartTime) / 1000.);
    }

    private String fineNumber(int number) {
        return (number < 10) ? "000" + number :
                number < 100 ? "00" + number :
                        number < 1000 ? "0" + number : String.valueOf(number);
    }

    private void run(String... args) {
        Locale.setDefault(Locale.US);
        try {
            launch(args);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    static int binarySearch(int low, int high) {
        int mid = 0;
        if (low >= high)
            mid = -1;
        else
            mid = (low+high)/2;
        return mid;
    }

    public static void main(String... args) {
        new Main().run(args);
    }

    public static void writerInCSV(String pathFile, String[][] contents) throws FileNotFoundException {
        String ColumnNamesList = "Example,P,States(min),Time(ms),Accuracy,Precision,Recall,Specificity,F-score";
        PrintWriter pw = new PrintWriter(new File(pathFile));
        StringBuilder builder = new StringBuilder();
        builder.append(ColumnNamesList +"\n");

        /*String[][] contents = {{"328728,0293283,38273,323121,33435"},
                {"90990", "0902098239", "29811","209083", "29820"}};*/

        for (int i = 0; i < contents.length; i++) {
            builder.append(i+1 +",");
            builder.append(Arrays.deepToString(contents[i]).replace("[", "").replace("]",""));
            builder.append('\n');
        }

        pw.write(builder.toString());
        pw.close();
        System.out.println("done!");
    }

}