package part2;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SudokuGenerator {
    private static final int SIZE = 9;
    private static final int SUBGRID_SIZE = 3;
    private static final Random random = new Random();

    public static Map<Pair<Integer, Integer>, Integer> getSudokuGrid() {
        int[][] board = generateSudoku();
        return generateSudokuMap(board);
    }

    public static int[][] generateSudoku() {
        int[][] board = new int[SIZE][SIZE];
        fillDiagonalSubgrids(board);
        fillRemaining(board, 0, SUBGRID_SIZE);
        return board;
    }

    private static void fillDiagonalSubgrids(int[][] board) {
        for (int i = 0; i < SIZE; i += SUBGRID_SIZE) {
            fillSubgrid(board, i, i);
        }
    }

    private static void fillSubgrid(int[][] board, int row, int col) {
        boolean[] used = new boolean[SIZE + 1];
        for (int i = 0; i < SUBGRID_SIZE; i++) {
            for (int j = 0; j < SUBGRID_SIZE; j++) {
                int num;
                do {
                    num = random.nextInt(SIZE) + 1;
                } while (used[num]);
                used[num] = true;
                board[row + i][col + j] = num;
            }
        }
    }

    private static boolean fillRemaining(int[][] board, int i, int j) {
        if (j >= SIZE && i < SIZE - 1) {
            i++;
            j = 0;
        }
        if (i >= SIZE && j >= SIZE) {
            return true;
        }
        if (i < SUBGRID_SIZE) {
            if (j < SUBGRID_SIZE) {
                j = SUBGRID_SIZE;
            }
        } else if (i < SIZE - SUBGRID_SIZE) {
            if (j == (i / SUBGRID_SIZE) * SUBGRID_SIZE) {
                j += SUBGRID_SIZE;
            }
        } else {
            if (j == SIZE - SUBGRID_SIZE) {
                i++;
                j = 0;
                if (i >= SIZE) {
                    return true;
                }
            }
        }

        for (int num = 1; num <= SIZE; num++) {
            if (isSafeToPlace(board, i, j, num)) {
                board[i][j] = num;
                if (fillRemaining(board, i, j + 1)) {
                    return true;
                }
                board[i][j] = 0;
            }
        }
        return false;
    }

    private static boolean isSafeToPlace(int[][] board, int row, int col, int num) {
        return !usedInRow(board, row, num) && !usedInColumn(board, col, num) && !usedInSubgrid(board, row - row % SUBGRID_SIZE, col - col % SUBGRID_SIZE, num);
    }

    private static boolean usedInRow(int[][] board, int row, int num) {
        for (int col = 0; col < SIZE; col++) {
            if (board[row][col] == num) {
                return true;
            }
        }
        return false;
    }

    private static boolean usedInColumn(int[][] board, int col, int num) {
        for (int row = 0; row < SIZE; row++) {
            if (board[row][col] == num) {
                return true;
            }
        }
        return false;
    }

    private static boolean usedInSubgrid(int[][] board, int startRow, int startCol, int num) {
        for (int row = 0; row < SUBGRID_SIZE; row++) {
            for (int col = 0; col < SUBGRID_SIZE; col++) {
                if (board[row + startRow][col + startCol] == num) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void printBoard(int[][] board) {
        for (int r = 0; r < SIZE; r++) {
            for (int d = 0; d < SIZE; d++) {
                System.out.print(board[r][d] + " ");
            }
            System.out.println();
        }
    }

    public static Map<Pair<Integer, Integer>, Integer> generateSudokuMap(int[][] board) {
        Map<Pair<Integer, Integer>, Integer> map = new HashMap<>();
        int cellsToCopy = (SIZE * SIZE) /2;
        while (cellsToCopy > 0) {
            int row = random.nextInt(SIZE);
            int col = random.nextInt(SIZE);
            if (board[row][col] != 0) {
                map.put(new Pair<>(row, col), board[row][col]);
                board[row][col] = 0;  // Remove number from the board after adding to map
                cellsToCopy--;
            }
        }
        return map;
    }

    private static void printMap(Map<Pair<Integer, Integer>, Integer> map) {
        for (Map.Entry<Pair<Integer, Integer>, Integer> entry : map.entrySet()) {
            System.out.println("Cell: (" + entry.getKey().first() + "," + entry.getKey().second() + ") - Value: " + entry.getValue());
        }
    }
}
