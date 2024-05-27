package part2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class HomeGui extends JFrame {

    private String selectedGame;

    public HomeGui(List<String> liPlayers, String myId) {
        // Set the title of the window
        setTitle("Game Selector");

        // Set the default close operation
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        // Create a panel for the top section with FlowLayout
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        // Create the "CREATE GAME" button
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

        // Add the "CREATE GAME" button to the top panel
        topPanel.add(createGameButton);

        // Create a panel for the bottom section with FlowLayout
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        // Create the "SELECT EXISTING" button
        JButton selectExistingButton = new JButton("SELECT EXISTING");

        

        // Create a combo box for existing games
        String[] existingGames = liPlayers.toArray(new String[0]);
        JComboBox<String> existingGamesComboBox = new JComboBox<>(existingGames);


        selectExistingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedGame = existingGamesComboBox.getSelectedItem().toString();
                PlayerImpl pl = new PlayerImpl();
                pl.startGame(selectedGame, myId); 
                dispose();
            }
        });

        // Add the "SELECT EXISTING" button and combo box to the bottom panel
        bottomPanel.add(selectExistingButton);
        bottomPanel.add(existingGamesComboBox);

        // Add the panels to the main panel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(bottomPanel, BorderLayout.CENTER);

        // Add the main panel to the frame
        add(mainPanel);

        // Set the size of the window
        setSize(400, 200);

        // Center the window
        setLocationRelativeTo(null);

        
    }

    public String getSelectedGame(){
        return this.selectedGame;
    }
    
}