package gt.edu.miumg.frederickmontiel.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class ExpressionTree {
    Node root;
    Map<String, Double> variables = new HashMap<>();

    public ExpressionTree(String expresion) {
        root = buildTree(expresion);
    }

    private int precedence(char operator) {
        switch (operator) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            case '^':
                return 3;
        }
        return -1;
    }

    private Node buildTree(String expresion) {
        Stack<Node> values = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (int i = 0; i < expresion.length(); i++) {
            char c = expresion.charAt(i);

            if (c == ' ')
                continue;

            if (c == '(') {
                operators.push(c);
            } else if (Character.isLetterOrDigit(c)) {
                values.push(new Node(Character.toString(c)));
            } else if (c == ')') {
                while (!operators.isEmpty() && operators.peek() != '(') {
                    processOperator(values, operators);
                }

                if (!operators.isEmpty() && operators.peek() == '(') {
                    operators.pop();
                }
            } else if (isOperator(c)) {
                while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(c) &&
                        operators.peek() != '(') {
                    processOperator(values, operators);
                }
                operators.push(c);
            }
        }

        while (!operators.isEmpty()) {
            processOperator(values, operators);
        }

        return values.isEmpty() ? null : values.pop();
    }

    private void processOperator(Stack<Node> values, Stack<Character> operators) {
        char operator = operators.pop();
        Node right = values.pop();
        Node left = values.pop();

        Node operatorNode = new Node(Character.toString(operator));
        operatorNode.izquierda = left;
        operatorNode.derecha = right;

        values.push(operatorNode);
    }

    public void traversePreorder() {
        traversePreorder(root);
        System.out.println();
    }

    private void traversePreorder(Node node) {
        if (node != null) {
            System.out.print(node.valor + " ");
            traversePreorder(node.izquierda);
            traversePreorder(node.derecha);
        }
    }

    public void traversePostorder() {
        traversePostorder(root);
        System.out.println();
    }

    private void traversePostorder(Node node) {
        if (node != null) {
            traversePostorder(node.izquierda);
            traversePostorder(node.derecha);
            System.out.print(node.valor + " ");
        }
    }

    public double evaluate() {
        return evaluate(root);
    }

    private double evaluate(Node node) {
        if (node == null)
            return 0;
        if (!isOperator(node.valor.charAt(0))) {
            return variables.getOrDefault(node.valor, 0.0);
        }

        double izq = evaluate(node.izquierda);
        double der = evaluate(node.derecha);
        return switch (node.valor) {
            case "+" -> izq + der;
            case "-" -> izq - der;
            case "*" -> izq * der;
            case "/" -> izq / der;
            case "^" -> Math.pow(izq, der);
            default -> 0;
        };
    }

    public void assignVariable(String variable, double valor) {
        variables.put(variable, valor);
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
    }

    public void printAsciiTree() {
        if (root == null) {
            System.out.println("El árbol está vacío.");
            return;
        }

        List<List<String>> levels = new ArrayList<>();
        buildAsciiTree(root, 0, levels);

        for (List<String> level : levels) {
            for (String str : level) {
                System.out.print(str);
            }
            System.out.println();
        }
    }

    private void buildAsciiTree(Node node, int level, List<List<String>> levels) {
        if (node == null)
            return;

        if (levels.size() == level) {
            levels.add(new ArrayList<>());
        }

        String padding = " ".repeat((int) Math.pow(2, Math.max(3 - level, 0)));

        if (levels.get(level).isEmpty()) {
            levels.get(level).add(padding + node.valor + padding);
        } else {
            levels.get(level).add(node.valor + padding);
        }

        buildAsciiTree(node.izquierda, level + 1, levels);
        buildAsciiTree(node.derecha, level + 1, levels);
    }

    HashMap<Integer, String> nivelesTextos = new HashMap<>();
    HashMap<Integer, String> nivelesSlashes = new HashMap<>();

    public void printCompactTree() {
        System.out.println("\nRepresentación compacta del arbol:");
        StringBuilder sb = new StringBuilder();
        printCompactNode(root, sb);
        System.out.println(sb.toString());
    }

    private void printCompactNode(Node node, StringBuilder sb) {
        if (node == null)
            return;

        sb.append(node.valor).append(" ");

        if (node.izquierda != null || node.derecha != null) {
            sb.append("/ \\ ");

            if (node.izquierda != null) {
                printCompactNode(node.izquierda, sb);
            } else {
                sb.append("null ");
            }

            if (node.derecha != null) {
                printCompactNode(node.derecha, sb);
            } else {
                sb.append("null ");
            }
        }
    }
}
