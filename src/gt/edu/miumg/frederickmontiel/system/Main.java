/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package gt.edu.miumg.frederickmontiel.system;

import java.util.Scanner;
import gt.edu.miumg.frederickmontiel.objects.ExpressionTree;
import gt.edu.miumg.frederickmontiel.objects.Node;
import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Ingrese una expresion matematica como \"a+b-(c-b)+e\":");
        String expression = input.nextLine().replace(" ", "");

        ExpressionTree tree = new ExpressionTree(expression);
        Set<Character> requestedVars = new HashSet<>();

        for (char c : expression.toCharArray()) {
            if (Character.isLetter(c) && !requestedVars.contains(c)) {
                requestedVars.add(c);
                System.out.print("Brindame el valor de " + c + ": ");
                double value = input.nextDouble();
                tree.assignVariable(Character.toString(c), value);
            }
        }

        tree.printAsciiTree();

        System.out.println("\nRecorrido Preorden:");
        tree.traversePreorder();

        System.out.println("Recorrido Postorden:");
        tree.traversePostorder();

        System.out.println("Resultado de la evaluacion: " + tree.evaluate());
    }
}