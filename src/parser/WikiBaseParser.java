package parser;
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


public class WikiBaseParser extends DefaultHandler2{

	
	boolean readText=false;
	boolean readTitle=false;
	protected BufferedWriter output;
	
	String title="";
	String text="";

	public WikiBaseParser(String outfilename, String... options) {
		try {
			output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outfilename)));
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		try {
			if(qName.equals("text")){
				output.write("  <text>");
				readText=true;
			}
			else if(qName.equals("title")){
				output.write("  <title>");
				readTitle=true;
			}
			else if(qName.equals("page")){
				output.write(" <page>");
				output.newLine();
			}
			else if(qName.equals("document")){
				output.write("<document>");
				output.newLine();
			}

			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		try {
			
			if(qName.equals("text")){
				
				/**
				 * LOGIC HERE
				 *
				 * Modify text variable and write the result
				 */
				
				
				
				output.write(text);
				output.write("</text>");
				output.newLine();
				text="";
				readText=false;
				
			}
			else if(qName.equals("page")){
				output.write(" </page>");
				output.newLine();
			}
			else if(qName.equals("title")){
				
				output.write(title+"</title>");
				output.newLine();
				readTitle=false;
				
			}
			else if(qName.equals("document")){
				output.write("</document>");
				output.newLine();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void endDocument() throws SAXException {
		try {
			output.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		try {
			
			if(readTitle){
				title = new String(ch, start, length);
			}
			else if(readText){
				
				/**
				 * Read each line
				 */
				String line = new String(ch, start, length);
				text = text + line;
			}
			
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		/*if(readText){
			String text = new String(ch, start, length);
			//model.addwordsAsText(text, phraseModel);
		}*/
		
		
	}
	
	
	public static void main(String[] args) throws Exception{
		System.out.println("Activity started...");
		long start = System.currentTimeMillis();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setValidating(false);
		SAXParser parser = factory.newSAXParser();
		WikiBaseParser wwc = new WikiBaseParser("/home/rdorado/Downloads/wikipedia/text/eswiki-chunk-tmp-1.xml");
		parser.parse( "/home/rdorado/Downloads/wikipedia/text/eswiki-chunk-1.xml",  wwc);
		//wwc.print();
	}



}
