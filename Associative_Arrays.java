package com.mycompany.associative_arrays;
import java.util.HashMap;
import java.util.Scanner;
//Group Members: Mahipus & Lerasan
public class Associative_Arrays {

     public static void main(String[] args) {
        HashMap<String, Integer> studentScores = new HashMap<>();

        studentScores.put("Mitch", 85);
        studentScores.put("Reiyan", 92);
        studentScores.put("Adriane", 78);
        studentScores.put("Danica", 88);
        studentScores.put("Ethan", 95);

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter a student's name to retrieve their score: ");
        String studentName = scanner.nextLine();

        if (studentScores.containsKey(studentName)) {
            System.out.println(studentName + "'s score: " + studentScores.get(studentName));
        } else {
            System.out.println("Student not found.");
        }

        System.out.print("Enter the student's name to update their score: ");
        String updateName = scanner.nextLine();
        
        if (studentScores.containsKey(updateName)) {
            System.out.print("Enter the new score for " + updateName + ": ");
            int newScore = scanner.nextInt();
            studentScores.put(updateName, newScore);
            System.out.println(updateName + "'s score has been updated to: " + newScore);
        } else {
            System.out.println("Student not found.");
        }

        System.out.println("\nAll student scores:");
        for (String name : studentScores.keySet()) {
            System.out.println(name + ": " + studentScores.get(name));
        }

        scanner.close();
    }
}