package learning;

import learning.DFA.DFA;
import learning.DFA.State;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map.Entry;

public class GraphvizDiagram {
    public static String diagramFile = AcessFile.image;
    public static File file;
    public static void createAutomataDiagram(DFA automata) throws IOException {
        clearDiagram();
        StringBuilder content = new StringBuilder();
        StringBuilder finalStates = new StringBuilder();
        StringBuilder states = new StringBuilder();
        StringBuilder initialState = new StringBuilder();
        StringBuilder transitions = new StringBuilder();

        content.append("digraph Finite_Automata {\n");
        content.append("    rankdir=LR;\n");
        content.append("    node [shape = point, color=white, fontcolor=white]; start;\n");

        for (int i = 0; i < automata.states.size()-1; i++) {
            if (automata.states.get(i).ffinal)
                finalStates.append("    node [shape = doublecircle, color=black, fontcolor=black]; q"+automata.states.get(i).state+";\n");
            if (!automata.states.get(i).ffinal && !automata.states.get(i).initial)
                states.append("    node [shape = circle]; q"+automata.states.get(i).state+";\n");
            if (automata.states.get(i).initial)
                initialState.append("    start -> q"+automata.states.get(i).state+";\n");

            ArrayList<Integer> saw = new ArrayList<>();
            for (Entry<String,State> transition: automata.states.get(i).transitions.entrySet()) {
                if (saw.contains(transition.getValue().state))
                    continue;
                saw.add(transition.getValue().state);
                if (transition.getKey().equals("#"))
                    continue;
                for (State state1: automata.states) {
                    if (transition.getValue().state == state1.state) {
                        StringBuilder label = new StringBuilder();
                        automata.states.get(i).transitions.forEach((key, value) -> {
                            if (value.state == state1.state) {
                                label.append(" ");
                                label.append(key);
                            }
                        });
                        transitions.append(
                                "    q"+ automata.states.get(i).state +
                                        " -> " +
                                        "q" + transition.getValue().state +
                                        " [ label = " +
                                        "\"" + label + "\"" +
                                        " ];\n");
                    }
                }
            }
        }
        content.append(finalStates);
        content.append(states);
        content.append(initialState);
        content.append(transitions);
        content.append("}");
        writer(content);
        saveAsPNG();
    }

    static void writer(StringBuilder content) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
            bw.write(String.valueOf(content));
            bw.flush();
            bw.close();

        } catch (IOException ioe) {
            System.out.println("# Ocorreu um erro durante a gravação do arquivo.");
            ioe.printStackTrace();
        }

    }
    static void saveAsPNG() throws IOException {
        String command = "dot -Tpng "+AcessFile.image +" -o " +AcessFile.image;
        Runtime.getRuntime().exec(command);
    }
    static void clearDiagram() throws IOException {
        file = new File(diagramFile);
        if (!file.exists()) { //Se arquivo não existe
        } else { //Se existir, exclue e cria um novo
            file.delete();
        }
        file.createNewFile();
    }
}
