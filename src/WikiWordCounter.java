import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;

import dorado.nlp.models.PhraseModel;
import dorado.nlp.models.WordModel;


public class WikiWordCounter extends DefaultHandler2{

	boolean readText=false;
	BufferedWriter output;
	WordModel model = new WordModel();
	PhraseModel phraseModel = new PhraseModel();
	
	public WikiWordCounter() {
		try {
			output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("/home/rdorado/Downloads/wikipedia/output.txt")));
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if(qName.equals("text")){
			readText = true;			
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if(qName.equals("text")){
			readText = true;			
		}
	}
	
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if(readText){
			String text = new String(ch, start, length);
			model.addwordsAsText(text, phraseModel);
		}
		
		
	}
	
	
	public static void main(String[] args) throws Exception{
		System.out.println("Activity started...");
		long start = System.currentTimeMillis();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setValidating(false);
		SAXParser parser = factory.newSAXParser();
		WikiWordCounter wwc = new WikiWordCounter();
		parser.parse( "/home/rdorado/Downloads/wikipedia/eswiki-chunk-1.xml",  wwc);
		wwc.print();
	}

	private void print() {
		model.print(10);
	}

}
