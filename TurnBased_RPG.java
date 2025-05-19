package com.mycompany.turnbased_rpg;

import java.util.Random;
import java.util.Stack;
import java.util.Scanner;

public class TurnBased_RPG {

    Stack<Integer> lastPlayerHP = new Stack<>(); 
    Stack<Integer> lastEnemyHP = new Stack<>();  
    int gameTimer = 2;
    Scanner scan = new Scanner(System.in);
    Random random = new Random();

    int playerHP = 500;
    int botHP = 500;
    int playerMaxDmg = 90;
    int playerMinDmg = 10;
    int botMaxDmg = 90;
    int botMinDmg = 10;

    public static void main(String[] args) {
        TurnBased_RPG game = new TurnBased_RPG();
        game.startGame();
    }

    public void startGame() {
        lastPlayerHP.push(playerHP);
        lastEnemyHP.push(botHP);

        while (playerHP > 0 && botHP > 0) {
            if (isOddOrEven(gameTimer)) {

               
                if (!lastPlayerHP.isEmpty() && random.nextInt(100) < 25) {
                    int restoredHP = lastPlayerHP.pop();
                    if (restoredHP > playerHP) {
                        System.out.println("[Passive Triggered] Your HP was restored from " + playerHP + " to " + restoredHP + "!");
                        playerHP = restoredHP;
                    } else {
                        lastPlayerHP.push(restoredHP);
                    }
                }

                System.out.println("You encountered an enemy!");
                System.out.println("Player HP: " + playerHP);
                System.out.println("Monster HP: " + botHP);
                System.out.println(" ");
                System.out.println("What would you like to do?");
                System.out.println(">> Attack");
                System.out.println(">> Stun");
                System.out.println(">> Skip");

                System.out.print("Pick a choice: ");
                String in = scan.nextLine();

                if (in.equalsIgnoreCase("Attack")) {
                    int playerDmg = random.nextInt(playerMaxDmg - playerMinDmg + 1) + playerMinDmg;
                    System.out.println(" ");
                    System.out.println("You dealt " + playerDmg + " damage to the enemy.");
                    botHP -= playerDmg;
                    if (botHP < 0) botHP = 0;
                    lastEnemyHP.push(botHP); 
                    System.out.println("The enemy has " + botHP + " HP remaining.");
                } else if (in.equalsIgnoreCase("Stun")) {
                    System.out.println(" ");
                    System.out.println("The enemy has been stunned!");
                    System.out.println("Your turn to attack.");
                } else {
                    System.out.println(" ");
                    System.out.println("You skip your turn.");
                    System.out.println(" ");
                }
            } else {
                
                lastEnemyHP.push(botHP); 

                
                if (!lastEnemyHP.isEmpty() && random.nextInt(100) < 25) {
                    int restoredHP = lastEnemyHP.pop();
                    System.out.println("DEBUG: Enemy HP stack popped value: " + restoredHP + ", current HP: " + botHP);
                    if (restoredHP >= botHP) { // Changed to >= for more chances
                        System.out.println("[Passive Triggered] Enemy's HP restored from " + botHP + " to " + restoredHP + "!");
                        botHP = restoredHP;
                    } else {
                        lastEnemyHP.push(restoredHP);
                    }
                }

                int botDmg = random.nextInt(botMaxDmg - botMinDmg + 1) + botMinDmg;
                System.out.println("It is the monster's turn now.");
                System.out.println("The monster dealt " + botDmg + " damage to the player.");

                lastPlayerHP.push(playerHP); 
                playerHP -= botDmg;
                if (playerHP < 0) playerHP = 0;

                System.out.println("The player has " + playerHP + " HP remaining.");
            }

            gameTimer++;
            if (playerHP <= 0) {
                System.out.println("Defeat! The player has been slain!");
            } else if (botHP <= 0) {
                System.out.println("The enemy has died!");
            }
        }
    }

    static boolean isOddOrEven(int i) {
        return i % 2 == 0; 
    }
}
