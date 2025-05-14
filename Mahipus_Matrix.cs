using Godot;
using System;

public partial class NewScript : Node
 public override void _Ready()
	{
		Console.Write("Enter the size of the matrix: ");
		int size = Convert.ToInt32(Console.ReadLine());

		// Initialize the matrix
		int[,] matrix = new int[size, size];

		for (int i = 0; i < size; i++)
		{
			for (int j = 0; j < size; j++)
			{
				matrix[i, j] = (i + 1) * (j + 1);
			}
		}
		Console.WriteLine("Multiplication Matrix:");
		for (int i = 0; i < size; i++)
		{
			for (int j = 0; j < size; j++)
			{
				Console.Write($"{matrix[i, j],4}"); // Formatting for better alignment
			}
			Console.WriteLine(); // Move to the next line
		}
	}
