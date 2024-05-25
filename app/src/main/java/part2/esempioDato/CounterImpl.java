package part2.esempioDato;

public class CounterImpl implements Counter {
	private int value;
	
	public CounterImpl(int value){
		this.value = value;
	}
	
	public void inc(){
		value++;
	}
	
	public int getValue(){
		return value;
	}

}