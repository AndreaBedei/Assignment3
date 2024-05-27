package part2;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


public class SudokuBoardImpl implements SudokuBoard, Serializable, CallbackInterface{
    
    private Map<Pair<Integer,Integer>, Integer> board = new HashMap<>();
    private Map<Pair<Integer,Integer>, Integer> selected = new HashMap<>();
    private List<CallbackInterface> callbacks = new ArrayList<>();

    public SudokuBoardImpl(){
        SudokuGenerator.getSudokuGrid().entrySet().forEach(x -> board.put(x.getKey(), x.getValue()));
    }

    @Override
    public synchronized void setCell(int x, int y, int value) throws RemoteException{
        if(value != 0){
            this.board.put(new Pair<>(x,y), value);
            this.notifyClient(new Pair<>(x,y), value, null);
        } else{
            if(this.board.remove(new Pair<>(x,y)) != null){
                this.notifyClient(new Pair<>(x,y), value, null);
            }
        }
    }

    @Override
    public synchronized void selectedCell(int x, int y) throws RemoteException {
        var p = new Pair<>(x,y);
        if(!this.selected.containsKey(p)){
            this.selected.put(p, 1);
        } else {
            var v = this.selected.get(p);
            this.selected.put(p, v+1);
        }
        this.notifyClient(new Pair<>(x,y), null, true);
    }

    @Override
    public synchronized void deselectedCell(int x, int y) throws RemoteException {
        var p = new Pair<>(x,y);
        var v = this.selected.get(p);
        if(v == 1){
            this.selected.remove(p);
        } else {
            this.selected.put(p, v-1);
        }
        this.notifyClient(new Pair<>(x,y), null, false);
    }
    

    @Override
    public ArrayList<Pair<Pair<Integer, Integer>, Integer>> getCells() throws RemoteException {
        return board.entrySet().stream().map(e -> new Pair<>(e.getKey(), e.getValue())).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public ArrayList<Pair<Integer, Integer>> getSelectedCells() throws RemoteException {
        return selected.keySet().stream().collect(Collectors.toCollection(ArrayList::new));
    }

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

    @Override
    public void registerCallback(CallbackInterface callback) throws RemoteException {
        callbacks.add(callback);
    }
}
