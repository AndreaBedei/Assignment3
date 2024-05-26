package part2;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


public class SudokuBoardImpl implements SudokuBoard, Serializable{
    
    private Map<Pair<Integer,Integer>, Integer> board = new HashMap<>();
    private Set<Pair<Integer,Integer>> selected = new HashSet<>();

    public SudokuBoardImpl(){
    }

    @Override
    public void setCell(int x, int y, int value) {
        this.board.put(new Pair<>(x,y), value);
    }

    @Override
    public void toggleSelectedCell(int x, int y) {
        var p = new Pair<>(x,y);
        if(this.selected.contains(p)){
            this.selected.add(p);
        } else {
            this.selected.remove(p);
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
}
