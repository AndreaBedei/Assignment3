package part2;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

public class LaunchPlayer {
    public static void main(String[] args) {

        Registry registry;
        try {
            registry = LocateRegistry.getRegistry();
            Server server = (Server) registry.lookup(ServerImpl.SERVER_NAME);

            List<String> li = server.getPlayers();

            String id = "p"+ li.size();

            // Create and display the form
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }        
    }
}
