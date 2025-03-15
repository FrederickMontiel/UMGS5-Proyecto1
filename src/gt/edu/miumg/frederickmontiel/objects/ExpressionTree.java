package gt.edu.miumg.frederickmontiel.objects;

import java.util.HashMap;
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
        System.out.println("\nDiagrama del arbol de expresion:");
        printNodeAndBranches(root, 0);
    }

    private void printNodeAndBranches(Node node, int level) {
        if (node == null)
            return;

        String indent = "  ".repeat(level);

        System.out.println(indent + node.valor);

        if (node.izquierda != null || node.derecha != null) {
            System.out.println(indent + "/" + " \\");

            String childIndent = "  ".repeat(level);

            if (node.izquierda != null && node.derecha != null) {
                System.out.print(childIndent + node.izquierda.valor);

                int spacing = 3;
                System.out.print(" ".repeat(spacing));

                System.out.println(node.derecha.valor);

                printNodeAndBranches(node.izquierda, level + 1);
                printNodeAndBranches(node.derecha, level + 1);
            } else if (node.izquierda != null) {
                System.out.println(childIndent + node.izquierda.valor);
                printNodeAndBranches(node.izquierda, level + 1);
            } else {
                System.out.println(childIndent + "  " + node.derecha.valor);
                printNodeAndBranches(node.derecha, level + 1);
            }
        }
    }

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
