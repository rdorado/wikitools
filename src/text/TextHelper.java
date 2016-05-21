package text;

public class TextHelper {
	
	static enum HTMLRemoverDFAState{READING_TEXT, READ_START_TAG} 

	public static String removeCDATA(String text) {
		String resp=text.replaceAll("<!\\[CDATA\\[", "");
		return resp.replaceAll("\\]\\]>", "");
	}
	
	
	public static String removeAllWikiInfo(String text, String startSymbol, String endSymbol) {
		String strtmp = text;
		while( (strtmp=TextHelper.removeWikiInfo(text,startSymbol,endSymbol))!=null ){
			text=strtmp;
		}
		return text;
	}
	public static String removeNonAlphabetical(String text){
		text=text.replaceAll("\\[", " ");
		text=text.replaceAll("\\]", " ");
		text=text.replaceAll(";", " ");
		text=text.replaceAll(":", " ");
		text=text.replaceAll("\\.", " ");
		text=text.replaceAll(",", " ");
		text=text.replaceAll("\\(", " ");
		text=text.replaceAll("\\)", " ");
		text=text.replaceAll("\\|", " ");
		text=text.replaceAll("»", " ");
		text=text.replaceAll("«", " ");
		text=text.replaceAll("\"", " ");
		text=text.replaceAll("_", " ");
		text=text.replaceAll("—", " ");
		text=text.replaceAll("'", " ");
		text=text.replaceAll("#", " ");
		text=text.replaceAll("  ", " ");
		return text;
	}
	
	public static String removeWikiInfo(String text, String startSymbol, String endSymbol) {
		/*int endIndx = text.indexOf(endSymbol);
		if(endIndx==-1) return null;
		
		while(endIndx!=-1){
			
		}
		int stIndx = text.substring(0, endIndx).lastIndexOf(startSymbol);
		if(stIndx==-1) return null;
		
		return text.substring(0,stIndx)+text.substring(endIndx+endSymbol.length());*/
		int stIndx=-1;
		int endIndx=0;
		do{
			endIndx = text.indexOf(endSymbol, endIndx+endSymbol.length());
			if(endIndx==-1) return null;
			
			stIndx = text.substring(0, endIndx).lastIndexOf(startSymbol);
			
		}while(stIndx==-1);
		
		
		return text.substring(0,stIndx)+text.substring(endIndx+endSymbol.length());
	}
	
	public static String removeHTMLTagsInfo(String text) {
		int indx=0;
		HTMLRemoverDFAState state=HTMLRemoverDFAState.READING_TEXT;
		StringBuffer resp=new StringBuffer(text.length());
		String tagName="";
		while(indx<text.length()){
			char nchar = text.charAt(indx);
			
			if(nchar=='<'){
				if(state==HTMLRemoverDFAState.READING_TEXT){
					state=HTMLRemoverDFAState.READ_START_TAG;
				}
			}
			else if(state==HTMLRemoverDFAState.READ_START_TAG){
				
			}
			else if(state==HTMLRemoverDFAState.READING_TEXT){
				resp.append(nchar);
			}
			
			indx++;
		}
		
		return resp.toString();
	}
	
	public static String replaceXMLSymbols(String text) {
		text = text.replaceAll("&gt;", ">");
		text = text.replaceAll("&nbsp;", " ");
		return text;
	}

	public static void main(String[] args) {
		System.out.println(removeWikiInfo("es to ejemplo {{borrar esto}}fin","{{","}}"));
		System.out.println(removeWikiInfo("{{borrar esto}}","{{","}}"));
		System.out.println(removeWikiInfo("esto es {{un ejemplo {{borrar esto}} fin }}.","{{","}}"));
		System.out.println(removeAllWikiInfo("esto es {{un ejemplo {{borrar esto}} fin }}.","{{","}}"));
		
	}


}

