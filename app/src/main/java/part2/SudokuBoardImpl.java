package part2;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;


public class SudokuBoardImpl implements SudokuBoard, Serializable, CallbackInterface{
    
    private Map<Pair<Integer,Integer>, Integer> board = new HashMap<>(); // Mappa per memorizzare i valori delle celle della scacchiera.
    private Map<Pair<Integer,Integer>, Integer> selected = new HashMap<>(); // Mappa per memorizzare le celle selezionate.
    private List<CallbackInterface> callbacks = new ArrayList<>(); // Lista di callback registrati per notificare i cambiamenti della scacchiera.

    public SudokuBoardImpl(){
        // Inizializza la scacchiera con una griglia generata.
        SudokuGenerator.getSudokuGrid().entrySet().forEach(x -> board.put(x.getKey(), x.getValue()));
    }

    // Metodo synchronized per impostare il valore di una cella.
    @Override
    public synchronized void setCell(int x, int y, int value) throws RemoteException{
        if(value != 0){
            this.board.put(new Pair<>(x,y), value); // Imposta il valore della cella se diverso da 0.
            this.notifyClient(new Pair<>(x,y), value, null); // Notifica agli altri client e a se stesso la il cambiamento.
        } else{
            // Rimuove il valore della cella se uguale a 0.
            if(this.board.remove(new Pair<>(x,y)) != null){
                // Notifica agli altri client e a se stesso la rimozione.
                this.notifyClient(new Pair<>(x,y), value, null);
            }
        }
    }

    // Metodo synchronized per selezionare una cella.
    @Override
    public synchronized void selectedCell(int x, int y) throws RemoteException {
        var p = new Pair<>(x,y);
        if(!this.selected.containsKey(p)){
            // Aggiunge la cella alla mappa delle celle selezionate se non presente.
            this.selected.put(p, 1);
        } else {
            // Incrementa il conteggio delle selezioni della cella, più utenti stanno selezioanando la stessa cella.
            var v = this.selected.get(p);
            this.selected.put(p, v+1);
        }
        this.notifyClient(new Pair<>(x,y), null, true); // Notifica agli altri client e a se stesso la selezione.
    }

    // Metodo synchronized per deselezionare una cella.
    @Override
    public synchronized void deselectedCell(int x, int y) throws RemoteException {
        var p = new Pair<>(x,y);
        var v = this.selected.get(p);
        if(v == 1){
            this.selected.remove(p); // Rimuove la cella dalla mappa delle celle selezionate se il conteggio è 1.
        } else {
             // Decrementa il conteggio delle selezioni della cella.
            this.selected.put(p, v-1);
        }
        this.notifyClient(new Pair<>(x,y), null, false); // Notifica agli altri client e a se stesso la deselezione.
    }
    

    @Override
    public ArrayList<Pair<Pair<Integer, Integer>, Integer>> getCells() throws RemoteException {
        return board.entrySet().stream().map(e -> new Pair<>(e.getKey(), e.getValue())).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public ArrayList<Pair<Integer, Integer>> getSelectedCells() throws RemoteException {
        return selected.keySet().stream().collect(Collectors.toCollection(ArrayList::new));
    }

     // Metodo per notificare i client registrati di un cambiamento.
    @Override
    public void notifyClient(Pair<Integer, Integer> position, Integer value, Boolean selected) throws RemoteException {
        if(callbacks != null){
            callbacks.forEach(c -> {
                try {
                    c.notifyClient(position, value, selected);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    // Metodo per registrare un callback.
    @Override
    public void registerCallback(CallbackInterface callback) throws RemoteException {
        callbacks.add(callback);
    }
}
