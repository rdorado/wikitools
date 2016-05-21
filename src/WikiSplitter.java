import java.io.BufferedWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;
import org.xml.sax.helpers.DefaultHandler;

import dorado.utils.TimeFormatter;

public class WikiSplitter extends DefaultHandler2{
	
	long FILE_SIZE = 10000000;
	StringBuilder content;
	File file;
	BufferedWriter writer;
	String patternName = "/home/rdorado/Downloads/wikipedia/eswiki-chunk-";
	int n=1;
	boolean readCharacters = false;
	
	public WikiSplitter(){
		content = new StringBuilder();
	}

	@Override
	public void startDocument() throws SAXException {
		file = new File(patternName+n+".xml");
		try {
			writer= new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
			writer.write("<?xml version=\"1.0\"?>");
			writer.newLine();
			writer.write("<document>");
			writer.newLine();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void endDocument() throws SAXException {
		try {
			writer.write("</document>");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.endDocument();
	}
	
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		//super.startElement(uri, localName, qName, attributes);
		//System.out.println(uri+", "+localName+", "+qName);

		try{
			if(qName.equals("page")){

				if(file.length() > FILE_SIZE){

					writer.write("</document>");
					writer.close();
					file = new File(patternName+(++n)+".xml");
					writer= new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));	
					
					writer.write("<?xml version=\"1.0\"?>");
					writer.newLine();
					writer.write("<document>");
					writer.newLine();
				}
				
				writer.append(" <page>");
				writer.newLine();
				
			}
			else if(qName.equals("title")){
				readCharacters = true;
				writer.append("  <title><![CDATA[");
			}
			else if(qName.equals("text")){
				readCharacters = true;
				writer.append("  <text><![CDATA[");
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}

	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		try{
			if(qName.equals("page")){
				writer.append(" </page>");
				writer.newLine();
			}
			else if(qName.equals("title")){
				writer.append("]]></title>");
				writer.newLine();
			}
			else if(qName.equals("text")){
				writer.append("]]></text>");
				writer.newLine();
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		readCharacters = false;
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if(readCharacters){
			try {
				String text = new String(ch, start, length);
				text = text.replaceAll(">", "&gt;");
				writer.write(text);
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws Exception{
		System.out.println("Activity started...");
		long start = System.currentTimeMillis();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		parser.parse("/home/rdorado/Downloads/eswiki-latest-pages-articles.xml", new WikiSplitter());

		System.out.println("Time spent: "+TimeFormatter.toMSM(System.currentTimeMillis()-start));
	}

}
