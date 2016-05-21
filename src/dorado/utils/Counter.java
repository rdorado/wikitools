package dorado.utils;

public class Counter {

	int val=0;
	public Counter(int init){
		val=init;
	}
	public void increment(){
		val++;
	}
	@Override
	public String toString() {
		return val+"";
	}
	public int getVal() {
		return val;
	}
}
