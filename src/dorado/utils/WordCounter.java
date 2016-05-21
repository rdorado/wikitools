package dorado.utils;

import java.util.ArrayList;
import java.util.Collections;

public class WordCounter {
	TNode root = new TNode((char)0);
	
	public void put(String word){
		root.put(word, 0);
	}
	
	public ArrayList<String> getWords() {
		return getWords(true);
	}
	public ArrayList<String> getWords(boolean order) {
		ArrayList<String> resp = new ArrayList<String>();
		root.getWords(resp, order, "");
		return resp;
	}
	public ArrayList<WordCount> getWordsAsWordCounts(boolean order) {
		ArrayList<WordCount> resp = new ArrayList<WordCount>();
		root.getWordsAsWordCounts(resp,order,"");
		return resp;
	}
	
	private int getCount(String string) {
		return root.getCount(string,0);
	}
	
	class TNode implements Comparable<TNode>{
		ArrayList<TNode> children = new ArrayList<TNode>();
		char key;
		int value;
		public TNode(char key){
			this.key=key;
		}
		public void getWordsAsWordCounts(ArrayList<WordCount> resp, boolean order, String string) {
			if(order) Collections.sort(children);
			for (TNode node : children) {
				if(node.value>0) resp.add(new WordCount(string+node.key,node.value));
				node.getWordsAsWordCounts(resp, order, string+node.key);
			}
		}
		public ArrayList<String> getWords(ArrayList<String> resp, boolean order,String string) {
			if(order) Collections.sort(children);
			for (TNode node : children) {
				if(node.value>0) resp.add(string+node.key+" "+node.value);
				node.getWords(resp, order, string+node.key);
			}
			return null;
		}
		public int getCount(String word, int indx) {
			int indxTmp = children.indexOf( new TNode( word.charAt(indx) ) );
			if(indxTmp!=-1){
				TNode tNode = children.get( indxTmp );
				if(word.length()==indx+1){
					return tNode.value;
				}
				else{
					return tNode.getCount(word, indx+1);
				}
			}
			return 0;
		}
		public TNode(char key, int value){
			this.key=key;
			this.value=value;
		}
		public void put(String word, int indx){
			
			int indxTmp = children.indexOf(new TNode( word.charAt(indx) ));
			//System.out.println(children+" "+indxTmp+" "+indx+" "+word.charAt(indx));
			if(indxTmp!=-1){
				TNode tNode = children.get( indxTmp );
				if(word.length()==indx+1){
					tNode.value++;
				}
				else{
					tNode.put(word, indx+1);
				}
			}
			else{
				TNode tNode = new TNode(word.charAt(indx));
				children.add( tNode );
				
				if(word.length()==indx+1){
					tNode.value++;
				}
				else{
					tNode.put(word, indx+1);
				}
			}
		}
		
		@Override
		public boolean equals(Object obj) {
			if(!(obj instanceof TNode)) return false;
			return ((TNode)obj).key==key;
		}
		@Override
		public int compareTo(TNode o) {
			return (new Character(key)).compareTo(o.key);
		}
		
		
	
	}

	
	public static void main(String[] args) {
		WordCounter wc = new WordCounter();
		wc.put("perro");
		wc.put("gato");
		wc.put("perros");
		
		
		System.out.println( wc.getCount("perro") );
		System.out.println( wc.getCount("per") );
		System.out.println( wc.getCount("perros") );
		System.out.println( wc.getCount("perrosos") );
		System.out.println( wc.getCount("gato") );
		
		System.out.println( wc.getWords() );
	}

	public static WordCounter fromString(String text) {
		WordCounter resp = new WordCounter();
		String[] words = text.split(" ");
		for (String word : words) {
			if(!word.trim().equals("")) resp.put(word);
		}
		
		return resp;
	}




	
}
