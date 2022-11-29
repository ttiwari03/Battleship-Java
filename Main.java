package battleship;

import java.util.Scanner;

/*
 *  This program facilitate playing of two player battleship game.
 *
 */
public class Main {

    public static void main(String[] args) {

        Scanner readIp = new Scanner(System.in);

        final int fieldSize = 10;
        final int aircraftCarrierSize = 5;
        final int battleshipSize = 4;
        final int submarineSize = 3;
        final int cruiserSize = 3;
        final int destroyerSize = 2;
        final String[] weaponTypes = {"Aircraft Carrier", "Battleship", "Submarine", "Cruiser", "Destroyer"};
        final int[] weaponSizes = {aircraftCarrierSize, battleshipSize, submarineSize, cruiserSize, destroyerSize};
        String weaponPlaced;
        final int totalWeapons = weaponTypes.length;
        boolean playerOneTurn = true;
        boolean gameEnded = false;
        int playerNumber;
        int playerCount = 2;
        int weaponCount;

        //Player battlefield arrangements.
        Battleship playerOne = new Battleship(fieldSize);
        Battleship playerTwo = new Battleship(fieldSize);
        Battleship player;
        Battleship opponentPlayer;

        while (playerCount > 0) {
            if (playerOneTurn) {
                player = playerOne;
                playerNumber = 1;
            } else {
                player = playerTwo;
                playerNumber = 2;
            }
            player.printBattleField();
            System.out.println("Player " + playerNumber + ", place your ships to the game field ");
            weaponCount = 0;
            while (weaponCount < totalWeapons) {
                System.out.printf("Enter the coordinate of the %s (%d cells): ", weaponTypes[weaponCount], weaponSizes[weaponCount]);
                System.out.println();
                String[] weapon = readIp.nextLine().split(" ");
                weaponPlaced = player.placeWeapon(weapon, weaponSizes[weaponCount]);

                if ("placed".equals(weaponPlaced)) {
                    weaponCount += 1;
                    player.printBattleField();
                } else {
                    System.out.println(weaponPlaced);
                    System.out.println();
                }

            }
            playerOneTurn = !playerOneTurn;
            playerCount--;
            System.out.println("Press Enter and pass the move to another player ");
            readIp.nextLine();
        }

        // Game begins with player one's turn.
        while (!gameEnded) {
            if (playerOneTurn) {
                player = playerOne;
                playerNumber = 1;
            } else {
                player = playerTwo;
                playerNumber = 2;
            }
            opponentPlayer = playerOneTurn ? playerTwo : playerOne;
            opponentPlayer.printFoggedBattleField();
            System.out.println("---------------------\n");
            player.printBattleField();
            System.out.println("Player " + playerNumber + ", it's your turn:");
            String shot = readIp.nextLine();
            String[] shotPosition = {shot.substring(0, 1), shot.substring(1)};
            gameEnded = opponentPlayer.shotFired(shotPosition);
            playerOneTurn = !playerOneTurn;
            System.out.println("Press Enter and pass the move to another player ");
            readIp.nextLine();
        }
    }
}

