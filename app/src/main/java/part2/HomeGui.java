package part2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class HomeGui extends JFrame {

    private String selectedGame;

    public HomeGui(List<String> liPlayers, String myId) {
        setTitle("Game Selector");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton createGameButton = new JButton("CREATE GAME");

        createGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedGame = myId;
                PlayerImpl pl = new PlayerImpl();
                pl.startGame(selectedGame, myId);
                dispose();
            }
        });

        topPanel.add(createGameButton);
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton selectExistingButton = new JButton("SELECT EXISTING");
        String[] existingGames = liPlayers.toArray(new String[0]);
        JComboBox<String> existingGamesComboBox = new JComboBox<>(existingGames);

        selectExistingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    selectedGame = existingGamesComboBox.getSelectedItem().toString();
                    PlayerImpl pl = new PlayerImpl();
                    pl.startGame(selectedGame, myId); 
                    dispose();
                } catch (Exception e1) {
                }
            }
        });

        bottomPanel.add(selectExistingButton);
        bottomPanel.add(existingGamesComboBox);
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(bottomPanel, BorderLayout.CENTER);
        add(mainPanel);
        setSize(400, 200);
        setLocationRelativeTo(null);

        
    }

    public String getSelectedGame(){
        return this.selectedGame;
    }
    
}