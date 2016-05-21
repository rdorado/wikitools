package dorado.nlp.models;

import java.util.Enumeration;
import java.util.Hashtable;

import dorado.utils.Counter;

public class WordModel {

	int nWords;
	Hashtable<String, Counter> bag = new Hashtable<String, Counter>(); 
	
	public void addwordsAsText(String text, PhraseModel phraseModel) {
		
		String[] words = phraseModel.chunk(text);
		for (String word : words) {
			if(!word.equals("")){
				addWord(word);
				nWords++;
			}
		}
		
	}
	
	public void addWord(String word){
		if(bag.containsKey(word)){
			bag.get(word).increment();
		}
		else{
			bag.put(word, new Counter(1));
		}
	}

	public void print(int n) {
		Enumeration<String> keys = bag.keys();
		while(keys.hasMoreElements()){
			String key = keys.nextElement();
			if(bag.get(key).getVal() > n){
				System.out.println(key+" "+bag.get(key));
			}
		}
		System.out.println("Number of words: "+nWords);
	}
	
	

}
