package dorado.utils;

public class WordCount {
	String word;
	int count;
	
	public WordCount(String word, int count) {
		this.word=word;
		this.count=count;
	}

	public int getCount() {
		return count;
	}

	public String getWord() {
		return word;
	}
}
