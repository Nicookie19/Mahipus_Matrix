/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.undo_reversal;
import java.util.Scanner;
import java.util.Stack;

public class Undo_Reversal {
public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Stack<String> history = new Stack<>();
        System.out.println("Enter a sentence to reverse or type 'undo' to revert to the previous sentence. Type 'exit' to quit.");
        String currentInput = "";
        String reversed = "";
        while (true) {
            System.out.print("> ");
            String inputSentence = scanner.nextLine();
            if (inputSentence.equalsIgnoreCase("exit")) {
                break;
            }
            if (inputSentence.equalsIgnoreCase("undo")) {
                if (!history.isEmpty()) {
                    reversed = history.pop();
                    System.out.println("Undo performed.");
                    System.out.println("Reversed Sentence: " + reversed);
                    currentInput = reversed;
                } else {
                    System.out.println("No previous sentence to undo.");
                }
                continue;
            }
        
            if (!reversed.isEmpty()) {
                history.push(reversed);
            }
            currentInput = inputSentence;
            reversed = reverseWordsWithoutSplit(currentInput);
            System.out.println("Reversed Sentence: " + reversed);
        }
        scanner.close();
    }
    public static String reverseWordsWithoutSplit(String sentence) {
        Stack<String> stack = new Stack<>();
        StringBuilder wordBuilder = new StringBuilder();
        for (int i = 0; i < sentence.length(); i++) {
            char c = sentence.charAt(i);
            if (c != ' ') {
                wordBuilder.append(c);
            } else {
                if (wordBuilder.length() > 0) {
                    stack.push(wordBuilder.toString());
                    wordBuilder.setLength(0);
                }
            }
        }
        // Push the last word if any
        if (wordBuilder.length() > 0) {
            stack.push(wordBuilder.toString());
        }
        StringBuilder reversedSentence = new StringBuilder();
        while (!stack.isEmpty()) {
            reversedSentence.append(stack.pop());
            if (!stack.isEmpty()) {
                reversedSentence.append(" ");
            }
        }
        return reversedSentence.toString();
    }
}
