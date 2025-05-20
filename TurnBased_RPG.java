package com.mycompany.turnbased_rpg;

import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

public class TurnBased_RPG {
    Scanner scan = new Scanner(System.in);
    Random random = new Random();

    Character player;
    Enemy enemy;

    Stack<Integer> lastPlayerHP = new Stack<>();
    Stack<Integer> lastEnemyHP = new Stack<>();

    int gameTimer = 2;

    public static void main(String[] args) {
        TurnBased_RPG game = new TurnBased_RPG();
        game.startGame();
    }

    public void startGame() {
        // Classes for the player to choose. Each class has their own unique skills
        System.out.println("Pick your class:");
        System.out.println("1. Knight (Protector of the 7 kingdoms)");
        System.out.println("2. Mage (Master of the mystic arts)");
        System.out.println("3. Archer (Hunter of the wind)");
        System.out.println("4. Rook (Destroyer of mountains)");

        int choice = 0;
        while (choice < 1 || choice > 4) {
            System.out.print("Enter number (1-4): "); // Key selection command for the classes
            try {
                choice = Integer.parseInt(scan.nextLine());
            } catch (Exception e) {
                System.out.println("Please enter a number between 1 and 4.");
            }
        }

        if (choice == 1) player = new Knight(random);
        else if (choice == 2) player = new Mage(random);
        else if (choice == 3) player = new Archer(random);
        else player = new Rook(random);

        enemy = new Enemy(random);

        System.out.println("You picked: " + player.getClassName());
        System.out.println("Fight starts now!");
        System.out.println();

        lastPlayerHP.push(player.hp);
        lastEnemyHP.push(enemy.hp);

        while (player.isAlive() && enemy.isAlive()) {
            if (isPlayerTurn(gameTimer)) {
                if (!lastPlayerHP.isEmpty() && random.nextInt(100) < 25) {
                    int oldHP = lastPlayerHP.pop();
                    if (oldHP > player.hp) {
                        System.out.println("[Passive] You feel revitalized! HP restored from " + player.hp + " to " + oldHP);
                        player.hp = oldHP;
                    } else {
                        lastPlayerHP.push(oldHP);
                    }
                }
                // Displays the player and enemy stats
                System.out.println("Your HP: " + player.hp + "/" + player.maxHP);
                System.out.println("Enemy HP: " + enemy.hp + "/" + enemy.maxHP);
                System.out.println("What do you want to do?");
                
                // Choices for the players
                System.out.println(">> Attack");
                System.out.println(">> Stun");
                if (player.canUsePhalanx())
                    System.out.println(">> Phalanx (press Q)");
                else
                    System.out.println("Phalanx on cooldown: " + player.phalanxCooldown + " turn(s)"); // skill cooldown when player tries to use the skill more than once in a turn
                System.out.println(">> Skip"); // skips any move

                System.out.print("Choice: ");
                String input = scan.nextLine().trim();

                if (input.equalsIgnoreCase("Attack")) { // if pressed each class will showcase 5 unique attack skills
                    System.out.println("Choose your attack:");
                    player.showAttacks();
                    System.out.print("Attack number (1-5): "); // Key command for attacks
                    String atkInput = scan.nextLine().trim();
                    int atkNum;
                    try {
                        atkNum = Integer.parseInt(atkInput);
                    } catch (Exception e) {
                        System.out.println("Not a valid number! You missed your attack.");
                        atkNum = -1;
                    }
                    if (atkNum >= 1 && atkNum <= 5) {
                        int dmg = player.useAttack(atkNum);
                        enemy.receiveDamage(dmg);
                        System.out.println("You used " + player.getAttackName(atkNum) + " and hit enemy for " + dmg + " damage.");
                        player.addJinguStack(); // Add Jingu stack on attack
                    } else {
                        System.out.println("Invalid attack choice, you wasted your turn."); // when player uses invalid input
                    }
                }
                else if (input.equalsIgnoreCase("Stun")) { // allows you to prevent the enemy from attacking
                    System.out.println("You stunned the enemy next turn!");
                    enemy.stunnedForNextTurn = true;
                }
                else if (input.equalsIgnoreCase("Q") && player.canUsePhalanx()) {
                    player.activatePhalanx(enemy);
                }
                else if(input.equalsIgnoreCase("Q")) {
                    System.out.println("Phalanx still on cooldown.");
                }
                else if (input.equalsIgnoreCase("Skip")) {
                    System.out.println("You skipped your turn.");
                }
                else {
                    System.out.println("Invalid input, turn wasted.");
                }

                player.decrementCooldowns();
                lastPlayerHP.push(player.hp);
            }
            else {
                // Enemy's passive skill
                if (!lastEnemyHP.isEmpty() && random.nextInt(100) < 25) {
                    int oldHP = lastEnemyHP.pop();
                    if (oldHP > enemy.hp) {
                        System.out.println("[Passive] Enemy healed from " + enemy.hp + " to " + oldHP);
                        enemy.hp = oldHP;
                    } else {
                        lastEnemyHP.push(oldHP);
                    }
                }

                System.out.println();
                System.out.println("Enemy's turn:");

                if (enemy.stunnedForNextTurn) { // if stun lands the enemy loses their turn
                    System.out.println("Enemy is stunned and loses his turn!");
                    enemy.stunnedForNextTurn = false;
                } else {
                    enemy.takeTurn(player);
                }
                System.out.println("Your HP: " + player.hp + "/" + player.maxHP);
                System.out.println("Enemy HP: " + enemy.hp + "/" + enemy.maxHP);
                System.out.println();

                lastEnemyHP.push(enemy.hp); // Push enemy HP after their turn
            }

            gameTimer++;
            if (!player.isAlive()) { // if you lose
                System.out.println("You lost! Better luck next time.");
                break;
            }
            if (!enemy.isAlive()) { // if you win
                System.out.println("You won! Enemy died.");
                break;
            }
        }
    }

    static boolean isPlayerTurn(int curTurn) {
        return curTurn % 2 == 0;
    }
}

abstract class Character { // The stats for the different classes
    Random random;
    int hp;
    int maxHP;
    int minDmg;
    int maxDmg;

    int phalanxCooldown = 0;
    boolean thornsActive = false;

    String[] attackNames = new String[5];

    // Jingu Mastery variables
    private int jinguStacks = 0;
    private final int maxJinguStacks = 3; // Maximum stacks for Jingu Mastery
    private final int jinguBonusDamage = 20; // Bonus damage from Jingu Mastery
    private final int jinguHealAmount = 30; // Healing amount when Jingu Mastery activates

    public Character(Random random) {
        this.random = random;
    }

    public abstract String getClassName();

    public void showAttacks() { // once player chooses attack, each class will show their own unique attacks
        for (int i = 0; i < attackNames.length; i++) {
            System.out.println((i + 1) + ". " + attackNames[i]);
        }
    }

    public int useAttack(int attackNumber) {
        int dmg = 0;
        String cls = getClassName();
        if (cls.equals("Knight")) dmg = knightAttackDamage(attackNumber);
        else if (cls.equals("Mage")) dmg = mageAttackDamage(attackNumber);
        else if (cls.equals("Archer")) dmg = archerAttackDamage(attackNumber);
        else if (cls.equals("Rook")) dmg = rookAttackDamage(attackNumber);
        else dmg = random.nextInt(maxDmg - minDmg + 1) + minDmg;

        // Check if Jingu Mastery activates
        if (jinguStacks >= maxJinguStacks) {
            dmg += jinguBonusDamage; // Add bonus damage
            hp += jinguHealAmount; // Heal the player
            if (hp > maxHP) hp = maxHP; // Ensure HP does not exceed max
            System.out.println("Jingu Mastery activated! You heal for " + jinguHealAmount + " HP and deal an extra " + jinguBonusDamage + " damage!");
            jinguStacks = 0; // Reset stacks after activation
        }

        return dmg;
    }

    public void addJinguStack() {
        if (jinguStacks < maxJinguStacks) {
            jinguStacks++;
            System.out.println("Jingu Mastery stack gained! Current stacks: " + jinguStacks);
        }
    }

    public String getAttackName(int attackNum) {
        if (attackNum >= 1 && attackNum <= attackNames.length) return attackNames[attackNum - 1];
        return "Unknown Attack";
    }

    private int knightAttackDamage(int n) { // damage stats for knight
        switch (n) {
            case 1: return randomValue(35, 65);
            case 2: return randomValue(40, 70);
            case 3: return randomValue(25, 55);
            case 4: return randomValue(30, 60);
            case 5: return randomValue(45, 75);
            default: return randomValue(minDmg, maxDmg);
        }
    }

    private int mageAttackDamage(int n) { // damage stats for mage
        switch (n) {
            case 1: return randomValue(40, 75);
            case 2: return randomValue(35, 65);
            case 3: return randomValue(30, 60);
            case 4: return randomValue(25, 55);
            case 5: return randomValue(50, 80);
            default: return randomValue(minDmg, maxDmg);
        }
    }

    private int archerAttackDamage(int n) { // damage stats for archer
        switch (n) {
            case 1: return randomValue(30, 65);
            case 2: return randomValue(35, 70);
            case 3: return randomValue(25, 55);
            case 4: return randomValue(40, 75);
            case 5: return randomValue(45, 80);
            default: return randomValue(minDmg, maxDmg);
        }
    }

    private int rookAttackDamage(int n) { // damage stats for rook
        switch (n) {
            case 1: return randomValue(25, 55);
            case 2: return randomValue(30, 60);
            case 3: return randomValue(20, 50);
            case 4: return randomValue(35, 65);
            case 5: return randomValue(40, 70);
            default: return randomValue(minDmg, maxDmg);
        }
    }

    private int randomValue(int min, int max) { // dmg randomizer
        return random.nextInt(max - min + 1) + min;
    }

    public void receiveDamage(int dmg) { // when thorns is inflicted
        if (thornsActive) {
            System.out.println(getClassName() + "'s thorns hit back for " + dmg + " damage!");
            thornsActive = false;
        } else {
            hp -= dmg;
            if (hp < 0) hp = 0;
            System.out.println(getClassName() + " takes " + dmg + " damage.");
        }
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public boolean canUsePhalanx() { // when the skill is available and is not on cooldown
        return phalanxCooldown == 0;
    }

    public void activatePhalanx(Enemy enemy) { // phalanx skill
        System.out.println(getClassName() + " tries Phalanx...");
        int chance = random.nextInt(100);
        if (chance < 25) {
            thornsActive = true;
            phalanxCooldown = 3;
            System.out.println("Phalanx success! Nullify attack chance plus reflect damage.");
        } else {
            System.out.println("Phalanx failed! Next enemy attack does double damage!");
            enemy.nextAttackIsDoubleDamage = true;
            phalanxCooldown = 3;
        }
    }

    public void decrementCooldowns() { // cooldown counter for phalanx skill
        if (phalanxCooldown > 0) phalanxCooldown--;
    }
}

class Knight extends Character { // knight stats and info display
    public Knight(Random random) {
        super(random);
        maxHP = 600;
        hp = maxHP;
        minDmg = 30;
        maxDmg = 70;
        attackNames = new String[] {"Slash", "Heavy Slash", "Shield Bash", "Piercing Thrust", "Whirlwind"};
    }
    @Override
    public String getClassName() { return "Knight"; }
}

class Mage extends Character { // mage stats and info display
    int healAmount = 50;
    public Mage(Random random) {
        super(random);
        maxHP = 450;
        hp = maxHP;
        minDmg = 40;
        maxDmg = 80;
        attackNames = new String[] {"Fireball", "Ice Spike", "Lightning Bolt", "Arcane Blast", "Meteor Shower"};
    }
    @Override
    public String getClassName() { return "Mage"; }
    @Override
    public void decrementCooldowns() {
        super.decrementCooldowns();
        if (hp < maxHP) {
            hp += healAmount;
            if (hp > maxHP) hp = maxHP;
            System.out.println("Mage heals " + healAmount + " HP.");
        }
    }
}

class Archer extends Character { // archer stats and info display
    public Archer(Random random) {
        super(random);
        maxHP = 500;
        hp = maxHP;
        minDmg = 25;
        maxDmg = 85;
        attackNames = new String[] {"Quick Shot", "Power Shot", "Multi Arrow", "Poison Arrow", "Piercing Shot"};
    }
    @Override
    public String getClassName() { return "Archer"; }
}

class Rook extends Character { // rook stats and info display
    public Rook(Random random) {
        super(random);
        maxHP = 700;
        hp = maxHP;
        minDmg = 20;
        maxDmg = 50;
        attackNames = new String[] {"Hammer Strike", "Shield Slam", "Guard Bash", "Crushing Blow", "Earthquake"};
    }
    @Override
    public String getClassName() { return "Rook"; }
}

class Enemy { // enemy stats and info display
    Random random;
    int hp;
    int maxHP = 500;
    int minDmg = 10;
    int maxDmg = 90;

    int gluttonyCooldown = 0;
    boolean bleedingActive = false;
    boolean stunnedForNextTurn = false;
    boolean nextAttackIsDoubleDamage = false;

    public Enemy(Random random) {
        this.random = random;
        hp = maxHP;
    }

    public void receiveDamage(int dmg) {
        hp -= dmg;
        if (hp < 0) hp = 0;
        System.out.println("Enemy takes " + dmg + " damage.");
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public void takeTurn(Character player) { // gluttony skill cooldown
        if (gluttonyCooldown > 0) gluttonyCooldown--;

        if (player.hp < 250 && gluttonyCooldown == 0) { // if hero HP goes below 250 gluttony activates
            boolean used = activateGluttony(player);
            if (used) gluttonyCooldown = 4;
        }

        int dmg = random.nextInt(maxDmg - minDmg + 1) + minDmg;
        if (nextAttackIsDoubleDamage) {
            dmg *= 2;
            System.out.println("Enemy attacks with doubled strength!");
            nextAttackIsDoubleDamage = false;
        }

        System.out.println("Enemy attacks and deals " + dmg + " damage.");
        player.receiveDamage(dmg);

        if (bleedingActive) {
            int bleedDmg = 10;
            System.out.println("Bleeding deals " + bleedDmg + " damage to you."); // bled status chance when gluttony hits
            player.hp -= bleedDmg;
            if (player.hp < 0) player.hp = 0;
            bleedingActive = false;
        }
    }

    private boolean activateGluttony(Character player) { // gluttony effect on player
        int chance = random.nextInt(100);
        if (chance < 25) {
            int steal = (int)(player.hp * 0.1);
            player.hp -= steal;
            if (player.hp < 0) player.hp = 0;
            hp += steal;
            if (hp > maxHP) hp = maxHP;

            System.out.println("Gluttony activated! Enemy steals " + steal + " HP and heals itself.");

            if (random.nextInt(100) < 25) {
                bleedingActive = true;
                System.out.println("Enemy inflicts bleeding on you!");
            }
            return true;
        }
        return false;
    }
}
