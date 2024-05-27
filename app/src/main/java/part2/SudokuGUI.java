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
    private Pair<Integer, Integer> selectedCell;
    private SudokuBoard board;

    public SudokuGUI(Player player) throws RemoteException {
        JFrame frame = new JFrame("Sudoku");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        this.board = player.getSudokuBoard();

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

                    // Aggiunta di un FocusListener per gestire gli eventi di focus.
                    @Override
                    public void focusGained(FocusEvent e) {
                        try {
                            // Deseleziona la cella precedente, se esiste.
                            if(selectedCell != null){
                                board.deselectedCell(selectedCell.first(), selectedCell.second());
                            }
                            // Seleziona la nuova cella.
                            selectedCell = new Pair<>(r, c);
                            board.selectedCell(r, c);
                        } catch (RemoteException e1) {
                            gridPanel.setVisible(false);
                            JOptionPane.showMessageDialog(frame, "I'm sorry the main host has been disconnected!");
                            System.exit(0); // Uscita dall'applicazione, il client creatore si è disconesso.
                        }
                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                        try {
                            String text = cells[r][c].getText();  // Ottiene il testo dalla cella.
                            if(text.length() == 0){
                                board.setCell(r, c, 0); // Imposta il valore della cella a 0 se il testo è vuoto.
                                return;
                            }
                            int v = Integer.parseInt(text);
                            if(v > 0 && v<10){
                                board.setCell(r, c, v); // Imposta il valore della cella se è valido.
                            } else {
                                cells[r][c].setText(""); // Resetta il testo della cella se il valore non è valido.
                                board.setCell(r, c, 0);
                            }
                        } catch (RemoteException e1) {
                        } catch(NumberFormatException e1){
                            // Resetta il testo della cella in caso di eccezione di formato.
                            try {
                                cells[r][c].setText("");
                                board.setCell(r, c, 0);
                            } catch (RemoteException e2) {
                                e2.printStackTrace();
                            }
                        }
                    }
                });
                gridPanel.add(cells[row][col]);
            }
        }

        // Imposta i valori iniziali delle celle sulla base della scacchiera del Sudoku
        board.getCells().forEach(e -> {
            var pos = e.first();
            var value = e.second();
            cells[pos.first()][pos.second()].setText(Integer.toString(value));
        });

        // Imposta le celle selezionate.
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

    // Gestisce la selezione di una nuova cella.
    public void newCellSelected(Pair<Integer, Integer> position, boolean selected) throws RemoteException{
        int row = position.first();
        int col = position.second();
        if(selected){
            cells[row][col].setBackground(Color.GREEN);
        } else if(!this.board.getSelectedCells().contains(new Pair<>(row, col))){
            if (this.isColoredDifferently(row, col)) {
                cells[row][col].setBackground(new Color(220, 220, 220));
            } else {
                cells[row][col].setBackground(Color.WHITE);
            }
        }
    }

    public void newCellWritten(Pair<Integer, Integer> position, int val){
        cells[position.first()][position.second()].setText(val == 0 ? "" : Integer.toString(val));
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

    


