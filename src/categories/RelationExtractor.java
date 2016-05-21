package categories;

import java.util.ArrayList;

public class RelationExtractor{
	
	String text;
	public RelationExtractor(String text){
		this.text=text;
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
}