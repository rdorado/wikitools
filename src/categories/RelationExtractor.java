package categories;

import java.util.ArrayList;
import java.util.Collections;

class Entity implements Comparable<Entity>{
	int index;
	String text;
	String type;
	public Entity(String type){
		this.type=type;
	}
	public String toString(){
		return "["+type+":"+index+", '"+text+"']";
	}

	public int compareTo(Entity oe) {
		
		return index-oe.index;
	}
}

public class RelationExtractor{

	String text;
	String document;
	int offset;
	public RelationExtractor(String document){
		this.document=document;
	}

	public ArrayList<Entity> extract(String startSymbol, String endSymbol, String type) {
		ArrayList<Entity> resp = new ArrayList<Entity>(); 
		Entity next = new Entity(type);
		offset=0;
		text=document;
		while( (next=extractNext(startSymbol,endSymbol,type))!=null ){
			resp.add(next);
		}
		return resp;
	}

	public ArrayList<Entity> extract(String startSymbol, String endSymbol, String separator, String type) {
		ArrayList<Entity> resp = new ArrayList<Entity>(); 
		Entity next = new Entity(type);
		offset=0;
		text=document;
		while( (next=extractNext(startSymbol,endSymbol,separator,type))!=null ){
			resp.add(next);
		}
		return resp;
	}

	public Entity extractNext(String startSymbol, String endSymbol, String type) {
		Entity resp = new Entity(type);
		int stIndx=-1;
		int endIndx=0;
		do{
			endIndx = text.indexOf(endSymbol, endIndx+endSymbol.length());
			if(endIndx==-1) return null;

			stIndx = text.substring(0, endIndx).lastIndexOf(startSymbol);

		}while(stIndx==-1);

		resp.text = text.substring(stIndx+startSymbol.length(), endIndx);
		resp.index = stIndx+offset;
		offset=offset+endIndx+endSymbol.length();

		text = text.substring(endIndx+endSymbol.length());
		return resp;
	}	

	public Entity extractNext(String startSymbol, String endSymbol, String separator, String type) {
		int stIndx=-1;
		int endIndx=0;
		Entity resp = new Entity(type);
		do{
			endIndx = text.indexOf(endSymbol, endIndx+endSymbol.length());
			if(endIndx==-1) return null;

			stIndx = text.substring(0, endIndx).lastIndexOf(startSymbol);

		}while(stIndx==-1);


		String tmp = text.substring(stIndx+startSymbol.length(), endIndx);
		if(tmp.contains(separator)) tmp = tmp.substring(tmp.indexOf(separator)+separator.length());
		resp.text = tmp;
		resp.index = stIndx+offset;
		offset=offset+endIndx+endSymbol.length();

		text = text.substring(endIndx+endSymbol.length());

		return resp;
	}


	public Entity extractTitle(String symbol, String type) {
		int stIndex = document.indexOf(symbol);		
		if(stIndex==-1) return null;
		
		String tmp = document.substring(stIndex+symbol.length());
		int enIndex = tmp.indexOf(symbol);
		if(enIndex==-1) return null;
		
		Entity resp = new Entity(type);
		resp.text = tmp.substring(0,enIndex).trim(); 
		resp.index = stIndex;
		
		return resp;
		
	}	


	public String next() {
		int stIndx=-1;
		int endIndx=0;
		do{
			endIndx = text.indexOf("]]", endIndx+2);
			if(endIndx==-1) return null;

			stIndx = text.substring(0, endIndx).lastIndexOf("[[");

		}while(stIndx==-1);
		String resp = text.substring(stIndx+2, endIndx);
		text = text.substring(0,stIndx)+text.substring(endIndx+2);
		return resp;
	}


	public static ArrayList<String> getAsArray(String text){
		boolean last=false;
		ArrayList<String> resp=new ArrayList<String>();

		do{
			int beginIndex = text.indexOf("[[");
			int endIndex = text.indexOf("]]", beginIndex);

			if(beginIndex==-1||endIndex==-1) last=true;
			else{
				String relstr = text.substring(beginIndex+2, endIndex);
				if(relstr.contains("[[")){
					text=text.substring(beginIndex+2);
				}
				else{
					text=text.substring(endIndex);
					if(relstr.contains("|")) relstr=relstr.substring(relstr.indexOf("|")+1);
					resp.add(relstr);
				}
			}

		}while(!last);

		return resp;
	}

	public static void main(String[] args){
		//RelationExtractor extractor = new RelationExtractor("Poirot also bears a striking resemblance to [[A. E. W. Mason]]'s fictional detective, [[Inspector Hanaud]] of the French [[Sûreté]], who first appeared in the 1910 novel ''[[At the Villa Rose (novel)|At the Villa Rose]]'' and predates the first Poirot novel by ten years.");
		RelationExtractor extractor = new RelationExtractor("=== Popularity === [[A. E. W. Mason]]");
		//System.out.println(extractor.extract("[[","]]","rel"));
		//System.out.println(extractor.extractTitle("==","t1"));
		ArrayList<Entity> entities = new ArrayList<Entity>();
		
		entities.addAll( extractor.extract("[[","]]","rel") );
		entities.add( extractor.extractTitle("===","t2") );
		
		Collections.sort(entities);
		
		System.out.println( entities );
		
		//System.out.println(extractor.extractTitle("===","t2"));
		//System.out.println(extractor.extractTitle("====","t3"));
	}

}