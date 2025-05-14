/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.mahipus_datastructures_act1;
import java.util.Scanner;
/**
 *
 * @author Students Account
 */
public class Mahipus_DataStructures_Act1 {

    public static void main(String[] args) {
       Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the size of the matrix:");
        
        int size = scanner.nextInt();
        int[][] matrix = new int[size][size];
        
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = (i + 1) * (j + 1);
            }
        }
   
        System.out.println("Multiplication Matrix:");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.printf("%4d", matrix[i][j]);
            }
            System.out.println();
        }
        scanner.close();
    }
}