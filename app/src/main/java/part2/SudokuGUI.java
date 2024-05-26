package part2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.rmi.RemoteException;

public class SudokuGUI {
    private static final int GRID_SIZE = 9;
    private static final int SUBGRID_SIZE = 3;
    private JTextField[][] cells;

    public SudokuGUI(Player player) throws RemoteException {
        JFrame frame = new JFrame("Sudoku");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        SudokuBoard board = player.getSudokuBoard();

        JPanel gridPanel = new JPanel(new GridLayout(GRID_SIZE, GRID_SIZE));
        cells = new JTextField[GRID_SIZE][GRID_SIZE];

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                cells[row][col] = new JTextField();
                cells[row][col].setHorizontalAlignment(JTextField.CENTER);
                cells[row][col].setFont(new Font("SansSerif", Font.BOLD, 20));
                if (this.isColoredDifferently(row, col)) {
                    cells[row][col].setBackground(new Color(220, 220, 220));
                } else {
                    cells[row][col].setBackground(Color.WHITE);
                }

                final int r = row;
                final int c = col;
                cells[row][col].addFocusListener(new FocusListener(){

                    @Override
                    public void focusGained(FocusEvent e) {
                        try {
                            board.toggleSelectedCell(r, c);
                        } catch (RemoteException e1) {
                            e1.printStackTrace();
                        }
                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                        try {
                            board.toggleSelectedCell(r, c);
                            int v = Integer.parseInt(cells[r][c].getText());
                            if(v > 0 && v<10){
                                board.setCell(r, c, v);
                            }
                        } catch (RemoteException e1) {
                            e1.printStackTrace();
                        }
                        catch(NumberFormatException e1){
                        }
                    }
                });
                gridPanel.add(cells[row][col]);
            }
        }

        // Prendiamo i valori presenti e selezionati.
        board.getCells().forEach(e -> {
            var pos = e.getKey();
            var value = e.getValue();
            cells[pos.first()][pos.second()].setText(Integer.toString(value));
        });
        board.getSelectedCells().forEach(p ->{
            cells[p.first()][p.second()].setBackground(Color.GREEN);
        });

        frame.add(gridPanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        JButton checkButton = new JButton("Check Solution");
        checkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isSolutionValid()) {
                    JOptionPane.showMessageDialog(frame, "Congratulations! The solution is correct.");
                } else {
                    JOptionPane.showMessageDialog(frame, "The solution is incorrect. Please try again.");
                }
            }
        });
        controlPanel.add(checkButton);

        frame.add(controlPanel, BorderLayout.SOUTH);

        frame.setSize(600, 600);
        frame.setVisible(true);
    }

    private boolean isSolutionValid() {
        int[][] board = new int[GRID_SIZE][GRID_SIZE];
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                try {
                    board[row][col] = Integer.parseInt(cells[row][col].getText());
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        }
        return isValidSudoku(board);
    }

    private boolean isValidSudoku(int[][] board) {
        for (int i = 0; i < GRID_SIZE; i++) {
            boolean[] rowCheck = new boolean[GRID_SIZE + 1];
            boolean[] colCheck = new boolean[GRID_SIZE + 1];
            boolean[] boxCheck = new boolean[GRID_SIZE + 1];
            for (int j = 0; j < GRID_SIZE; j++) {
                if (!check(board[i][j], rowCheck) || !check(board[j][i], colCheck) ||
                        !check(board[(i / SUBGRID_SIZE) * SUBGRID_SIZE + j / SUBGRID_SIZE][(i % SUBGRID_SIZE) * SUBGRID_SIZE + j % SUBGRID_SIZE], boxCheck)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean check(int num, boolean[] checkArray) {
        if (num < 1 || num > GRID_SIZE || checkArray[num]) {
            return false;
        }
        checkArray[num] = true;
        return true;
    }

    private boolean isColoredDifferently(int row, int col){
        return (row / SUBGRID_SIZE + col / SUBGRID_SIZE) % 2 == 0;
    }
}

    

