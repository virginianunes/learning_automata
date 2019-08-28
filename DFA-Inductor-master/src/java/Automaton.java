import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Automaton {
	private Node start;
	private List<Node> states;

	public Automaton(Automaton automaton) {
		this(automaton.size());

		for (int i = 0; i < automaton.getStates().size(); i++) {
			Node thisNode = this.states.get(i);
			Node otherNode = automaton.getStates().get(i);
			thisNode.setStatus(otherNode.getStatus());
			for (Map.Entry<String, Node> entry : otherNode.getChildren().entrySet()) {
				thisNode.addChild(entry.getKey(), this.states.get(entry.getValue().getNumber()));
			}
			for (Map.Entry<String, Node> entry : otherNode.getParents().entrySet()) {
				thisNode.addParent(entry.getKey(), this.states.get(entry.getValue().getNumber()));
			}
		}
	}

	public Automaton(int size) {
		int cur = 0;
		this.start = new Node(cur++);
		this.states = new ArrayList<>();
		this.states.add(this.start);

		while (cur < size) {
			this.states.add(new Node(cur++));
		}
	}

	public Automaton(File file) throws IOException {
		this.start = new Node(0);
		this.states = new ArrayList<>();
		this.states.add(this.start);

		try (BufferedReader automatonBR = new BufferedReader(new FileReader(file))) {
			Pattern transitionPattern = Pattern.compile("\\s+(\\d+) -> (\\d+) \\[label = \\\"([a-zA-Z0-9-_]+)\\\"\\];");
			Pattern acceptingPattern = Pattern.compile("\\s+(\\d+) \\[peripheries=2\\]");

			String line;
			Matcher matcher;
			while ((line = automatonBR.readLine()) != null) {
				if ((matcher = transitionPattern.matcher(line)).matches()) {
					addTransition(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)),
							matcher.group(3));
				} else if ((matcher = acceptingPattern.matcher(line)).matches()) {
					getState(Integer.parseInt(matcher.group(1))).setStatus(Node.Status.ACCEPTABLE);
				}
			}
			for (Node node : states) {
				if (!node.isAcceptable()) {
					node.setStatus(Node.Status.REJECTABLE);
				}
			}
		}
	}

	public Node getStart() {
		return start;
	}

	public Node getState(int i) {
		return states.get(i);
	}

	public List<Node> getStates() {
		return states;
	}

	public int size() {
		return states.size();
	}

	public void addTransition(int from, int to, String label) {
		if (from >= states.size()) {
			addState(from);
		}
		if (to >= states.size()) {
			addState(to);
		}
		Node fromNode = states.get(from);
		Node toNode = states.get(to);

		fromNode.addChild(label, toNode);
		toNode.addParent(label, fromNode);
	}

	public void addChildren(int num, Map<String, Node> children) {
		Node numNode = states.get(num);
		numNode.getChildren().putAll(children);
	}

	public void addParents(int num, Map<String, Node> children) {
		Node numNode = states.get(num);
		numNode.getParents().putAll(children);
	}

	public Node.Status proceedWord(List<String> word) {
		Node curNode = start;
		for (String label : word) {
			curNode = curNode.getChild(label);
			if (curNode == null)
				return null;
		}
		return curNode.getStatus();
	}

	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("digraph Automat {\n");
		s.append("    node [shape = circle];\n");
		s.append("    0 [style = \"bold\"];\n");

		for (Node state : states) {
			if (state.isAcceptable()) {
				s.append("    ");
				s.append(state.getNumber());
				s.append(" [peripheries=2]\n");
			}
			for (Entry<String, Node> e : state.getChildren().entrySet()) {
				s.append("    ");
				s.append(state.getNumber());
				s.append(" -> ");
				s.append(e.getValue().getNumber());
				s.append(" [label = \"");
				s.append(e.getKey());
				s.append("\"];\n");
			}
		}
		s.append("}");

		return s.toString();
	}

	private String enumerate() {
		Queue<Node> queue = new LinkedList<>();
		boolean[] visited = new boolean[this.size()];
		List<String> alphabet = new ArrayList<>(this.getStart().getChildren().keySet());
		Collections.sort(alphabet);
		int cur_num = 0;

		Map<Integer, Integer> enumeration = new HashMap<>();

		queue.add(this.getStart());
		visited[this.getStart().getNumber()] = true;
		Node cur;
		List<Integer> hash = new ArrayList<>();
		while (!queue.isEmpty()) {
			cur = queue.remove();
			enumeration.put(cur.getNumber(), cur_num++);
			for (String label : alphabet) {
				hash.add(cur.getChild(label).getNumber());
			}
			for (String label : alphabet) {
				Node child = cur.getChild(label);
				if (!visited[child.getNumber()]) {
					visited[child.getNumber()] = true;
					queue.add(child);
				}
			}
		}
		for (int i = 0; i < hash.size(); i++) {
			hash.set(i, enumeration.get(hash.get(i)));
		}
		return hash.toString();
	}

	@Override
	public int hashCode() {
		return enumerate().hashCode();
	}

	@Override
	public boolean equals(Object arg) {
		Automaton obj = (Automaton) arg;
		return this.hashCode() == obj.hashCode();
	}

	private boolean addState(int number) {
		int cur = states.size();
		if (cur <= number) {
			while (cur <= number) {
				this.states.add(new Node(cur++));
			}
			return true;
		}
		return false;
	}
}
