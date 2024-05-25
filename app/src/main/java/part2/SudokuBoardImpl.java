package part2;
import java.util.Map;
import java.util.HashMap;


public class SudokuBoardImpl implements SudokuBoard {
    
    private Map<Pair<Integer,Integer>, Integer> board = new HashMap<>();

    //-1 vuota, non selezionata
    //0 vuota, selezionata
    //1 a 9 valore inserito
    public SudokuBoardImpl(){
        for(int i = 0; i<9; i++){
            for(int j = 0; j<9; j++){
                board.put(new Pair<Integer,Integer>(i, j), -1);
            }
        }
    }

    @Override
    public boolean setCell(int x, int y, int value) {
        if(board.get(new Pair<>(x, y)) == 0){
            this.board.remove(new Pair<>(x,y), 0);
            this.board.put(new Pair<>(x,y), value);
            return true;   
        }
        return false;
    }

    @Override
    public boolean selectCell(int x, int y) {
        if(board.get(new Pair<>(x, y)) == -1){
            this.board.remove(new Pair<>(x,y), -1);
            this.board.put(new Pair<>(x,y), 0);
            return true;   
        }
        return false;
    }

}
