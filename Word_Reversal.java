package com.mycompany.word_reversal;

import java.util.Scanner;
import java.util.Stack;

public class Word_Reversal {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a sentence to reverse: ");
            String inputSentence = scanner.nextLine();
            String reversed = reverseWordsWithoutSplit(inputSentence);
                System.out.println("Reversed Sentence: " + reversed);
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
