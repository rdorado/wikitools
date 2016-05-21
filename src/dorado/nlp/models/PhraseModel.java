package dorado.nlp.models;

import java.util.StringTokenizer;

public class PhraseModel {

	public String[] chunk(String text) {		
		text = text.replaceAll("  ", " ");
		text = text.replaceAll("	", " ");		
		
		if(text== null) return null;
		
		return text.split("[ \n]");
	}

	
	
}
