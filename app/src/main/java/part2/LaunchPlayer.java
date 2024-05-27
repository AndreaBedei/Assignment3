package part2;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

// Classe per lanciare un nuovo player.
public class LaunchPlayer {
    public static void main(String[] args) {

        Registry registry;
        try {
            registry = LocateRegistry.getRegistry(); // Prendo il registro.
            Server server = (Server) registry.lookup(ServerImpl.SERVER_NAME); // Registrazione del server.

            List<String> li = server.getPlayers(); // Prendo i players dal server.
            String id = "p"+ li.size();
            // Faccio partire la gui.
            SwingUtilities.invokeLater(() -> {
                HomeGui frame;
                try {
                    frame = new HomeGui(new ArrayList<>(server.getCreators()), id); 
                    frame.setVisible(true);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
        });
        } catch (Exception e) {
            e.printStackTrace();
        }        
    }
}
