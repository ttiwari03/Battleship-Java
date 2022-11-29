package  battleship;


/*
 *  This class store information regarding a player's battlefield.
 */
class Battleship {
    private final int one = 1;
    private final int valueOfA = 65;
    final int aircraftCarrierSize = 5;
    final int battleshipSize = 4;
    final int submarineSize = 3;
    final int cruiserSize = 3;
    final int destroyerSize = 2;
    private boolean gameEnd = false;
    private int aircraftCarrierCounter = 0;
    private int battleshipCounter = 0;
    private int submarineCounter = 0;
    private int cruiserCounter = 0;
    private int destroyerCounter = 0;
    private final int fieldSize;
    String[][] battleField;
    String[][] foggedBattleField;
    private int[] aircraftPosition;
    private int[] battleshipPosition;
    private int[] cruiserPosition;
    private int[] submarinePosition;
    private int[] destroyerPosition;

    // Constructor to initialize player's battlefield and fogged battlefield.
    Battleship(int fieldSize) {
        this.fieldSize = fieldSize + one;
        this.battleField = new String[this.fieldSize][this.fieldSize];
        this.foggedBattleField = new String[this.fieldSize][this.fieldSize];

        for (int r = 0; r < this.fieldSize; r++) {
            for (int c = 0; c < this.fieldSize; c++) {
                if (r == 0 && c == 0) {
                    this.battleField[r][c] = " ";
                    this.foggedBattleField[r][c] = " ";
                } else if (r == 0) {
                    this.battleField[r][c] = Integer.toString(c);
                    this.foggedBattleField[r][c] = Integer.toString(c);
                } else if (c == 0) {
                    this.battleField[r][c] = (char) (r + valueOfA - one) + "";
                    this.foggedBattleField[r][c] = (char) (r + valueOfA - one) + "";
                } else {
                    this.battleField[r][c] = "~";
                    this.foggedBattleField[r][c] = "~";
                }
            }
        }
    }

    // Print player's battlefield with ship location and hit or miss during game play.
    protected void printBattleField() {
        for (int r = 0; r < this.fieldSize; r++) {
            for (int c = 0; c < this.fieldSize; c++) {
                System.out.print(this.battleField[r][c] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    // Check whether given co-ordinate for ship placement are valid and place ship accordingly.
    protected String placeWeapon(String[] weaponPosition, int weaponSize) {

        int row1 = (int) (weaponPosition[0].charAt(0)) - valueOfA + one;
        int row2 = (int) (weaponPosition[1].charAt(0)) - valueOfA + one;
        int col1 = Integer.parseInt(weaponPosition[0].substring(1));
        int col2 = Integer.parseInt(weaponPosition[1].substring(1));

        if (weaponSize == this.aircraftCarrierSize) {
            this.aircraftPosition = new int[] {row1, row2, col1, col2};
            this.aircraftCarrierCounter = this.aircraftCarrierSize;
        } else if (weaponSize == this.battleshipSize) {
            this.battleshipPosition = new int[] {row1, row2, col1, col2};
            this.battleshipCounter = this.battleshipSize;
        } else if (weaponSize == this.submarineSize && this.submarinePosition == null) {
            this.submarinePosition = new int[] {row1, row2, col1, col2};
            this.submarineCounter = this.submarineSize;
        } else if (weaponSize == this.cruiserSize) {
            this.cruiserPosition = new int[] {row1, row2, col1, col2};
            this.cruiserCounter = this.cruiserSize;
        } else if (weaponSize == this.destroyerSize) {
            this.destroyerPosition = new int[] {row1, row2, col1, col2};
            this.destroyerCounter = this.destroyerSize;
        }

        boolean horizontal = row1 == row2;
        boolean vertical = col1 == col2;
        if (!horizontal && !vertical) {
            return "Error! Wrong ship location! Try again: ";
        }

        boolean shipSizeHorizontal = horizontal && Math.abs(col1 - col2) + 1 == weaponSize;
        boolean shipSizeVertical = vertical && Math.abs(row1 - row2) + 1 ==weaponSize;
        if (!shipSizeVertical && !shipSizeHorizontal) {
            return "Error! Wrong length of the Submarine! Try again: ";
        }

        int startCol = Math.min(col1, col2);
        int endCol = startCol == col1 ? col2 : col1;
        int startRow = Math.min(row1, row2);
        int endRow = startRow == row1 ? row2 : row1;

        int prevCol = startCol > 1 ? startCol - 1 : startCol;
        int nextCol = endCol < 10 ? endCol + 1 : endCol;
        int prevRow = startRow > 1 ? startRow - 1 : startRow;
        int nextRow = endRow < 10 ? endRow + 1 : endRow;

        if (horizontal) {
            if (this.battleField[row1][prevCol].equals("O") || this.battleField[row1][nextCol].equals(("O"))) {
                return "Error! You placed it too close to another one. Try again: ";
            }
            for (int c = startCol; c <= endCol; c++) {
                if (this.battleField[row1][c].equals("O") || this.battleField[prevRow][c].equals("O") || this.battleField[nextRow][c].equals("O")) {
                    return "Error! You placed it too close to another one. Try again: ";
                }
            }

        }

        if (vertical) {

            if (this.battleField[prevRow][col1].equals("O") || this.battleField[nextRow][col1].equals("O")) {
                return "Error! You placed it too close to another one. Try again: ";
            }
            for (int r = startRow; r <= endRow; r++) {
                if (this.battleField[r][col1].equals("O") || this.battleField[r][prevCol].equals("O") || this.battleField[r][nextCol].equals("O")) {
                    return "Error! You placed it too close to another one. Try again: ";
                }
            }
        }

        updateBattleField(startRow, endRow, startCol, endCol);
        return "placed";
    }

    // Update battlefield for ships location during ship placement.
    private void updateBattleField(int startRow, int endRow, int startCol, int endCol) {
        for (int r = startRow; r <= endRow; r++) {
            for (int c = startCol; c <= endCol; c++) {
                this.battleField[r][c] = "O";
            }
        }
    }

    // Find if shot is fired within the battlefield and whether hit or miss the ship positions on opponent's battlefield.
    // Called by opponent player object.
    protected boolean shotFired(String[] shotPosition) {
        int rowFired = (int) (shotPosition[0].charAt(0)) - valueOfA + one;
        int colFired = Integer.parseInt(shotPosition[1]);

        if ((rowFired < 1 || rowFired > 10) || (colFired < 1 || colFired > 10)){
            System.out.println("Error! You entered the wrong coordinates! Try again: \n");
        } else if (this.battleField[rowFired][colFired].equals("O") || this.battleField[rowFired][colFired].equals("X")) {
            this.battleField[rowFired][colFired] = "X";

            if ((rowFired >= this.aircraftPosition[0] && rowFired <= this.aircraftPosition[1]) && (colFired >= this.aircraftPosition[2] && colFired <= this.aircraftPosition[3])) {
                this.aircraftCarrierCounter--;
            } else if ((rowFired >= this.battleshipPosition[0] && rowFired <= this.battleshipPosition[1]) && (colFired >= this.battleshipPosition[2] && colFired <= this.battleshipPosition[3])) {
                this.battleshipCounter--;
            } else if ((rowFired >= this.submarinePosition[0] && rowFired <= this.submarinePosition[1]) && (colFired >= this.submarinePosition[2] && colFired <= this.submarinePosition[3])) {
                this.submarineCounter--;
            } else if ((rowFired >= this.cruiserPosition[0] && rowFired <= this.cruiserPosition[1]) && (colFired >= this.cruiserPosition[2] && colFired <= this.cruiserPosition[3])) {
                this.cruiserCounter--;
            } else if ((rowFired >= this.destroyerPosition[0] && rowFired <= this.destroyerPosition[1]) && (colFired >= this.destroyerPosition[2] && colFired <= this.destroyerPosition[3])) {
                this.destroyerCounter--;
            }

            boolean shipDestroyed = this.aircraftCarrierCounter == 0 || this.battleshipCounter == 0 || this.submarineCounter == 0 || this.cruiserCounter == 0 || this.destroyerCounter == 0;
            boolean sankAllShip = this.aircraftCarrierCounter <= 0 && this.battleshipCounter <= 0 && this.submarineCounter <= 0 && this.cruiserCounter <= 0 && this.destroyerCounter <= 0;

            if (sankAllShip) {
                System.out.println("You sank the last ship. You won. Congratulations! \n");
                this.gameEnd = true;
            } else if (shipDestroyed) {
                System.out.println("You sank a ship! Specify a new target: \n");
            } else {
                System.out.println("You hit a ship! \n");
            }
        } else {
            this.battleField[rowFired][colFired] = "M";
            System.out.println("You missed!");
            System.out.println();
        }
        return this.gameEnd;
    }

    // print fogged battlefield of called player.
    protected void printFoggedBattleField() {
        for (int r = 0; r < this.fieldSize; r++) {
            for (int c = 0; c < this.fieldSize; c++) {
                System.out.print(this.foggedBattleField[r][c] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
