package part2;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


public class SudokuBoardImpl implements SudokuBoard, Serializable, CallbackInterface{
    
    private Map<Pair<Integer,Integer>, Integer> board = new HashMap<>();
    private Set<Pair<Integer,Integer>> selected = new HashSet<>();
    private List<CallbackInterface> callbacks = new ArrayList<>();

    public SudokuBoardImpl(){
    }

    @Override
    public void setCell(int x, int y, int value) throws RemoteException{
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
    public void toggleSelectedCell(int x, int y) throws RemoteException {
        var p = new Pair<>(x,y);
        if(!this.selected.contains(p)){
            this.selected.add(p);
            this.notifyClient(new Pair<>(x,y), null, true);
        } else {
            this.selected.remove(p);
            this.notifyClient(new Pair<>(x,y), null, false);
        }
    }

    @Override
    public ArrayList<Entry<Pair<Integer, Integer>, Integer>> getCells() throws RemoteException {
        return board.entrySet().stream().collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public ArrayList<Pair<Integer, Integer>> getSelectedCells() throws RemoteException {
        return selected.stream().collect(Collectors.toCollection(ArrayList::new));
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
