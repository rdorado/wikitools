package filesystem;
import java.io.BufferedWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;
import org.xml.sax.helpers.DefaultHandler;

import dorado.utils.TimeFormatter;

public class WikiFileSplitter extends DefaultHandler2{
	int nfile=0;
	//long N_FILE_SIZE = 1000;
	StringBuilder content;	
	File file;
	BufferedWriter writer;
	BufferedWriter listWriter;
	String directory;
	int n=1;
	int narticle=0;
	long startTime;
	boolean debug=true;

	boolean readCharacters=false;
	boolean readId=false;
	boolean readRev=false;
	boolean readTitle=false;
	String articleid;
	String title;

	public WikiFileSplitter(String outputDirectory){
		directory=outputDirectory;
		content = new StringBuilder();

		/*
		 * Directory creation
		 */
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				for (int k = 0; k < 10; k++) {
					File f = new File(directory+"/"+i+""+j+""+k+"/");
					f.mkdirs();
				}
			}
		}
		try {
			listWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(directory+"/list.xml")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		startTime=System.currentTimeMillis();
	}

	@Override
	public void endDocument() throws SAXException {
		try {
			
			File f = new File(directory+"/tmp.xml");
			f.delete();
		
			listWriter.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
		 System.out.println(narticle+" articles parsed, elapsed time: "+TimeFormatter.toMSM(System.currentTimeMillis()-startTime));
	}

	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		try{
			if(qName.equals("page")){
				narticle++;
				
				file = new File(directory+"/tmp.xml");
				writer= new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
				nfile++;

				writer.write("<?xml version=\"1.0\"?>");
				writer.newLine();
				writer.write("<document>");
				writer.newLine();

				writer.append(" <page>");
				writer.newLine();

			}
			else if(qName.equals("title")){
				readCharacters = true;
				readTitle = true;
				writer.append("  <title><![CDATA[");
			}
			else if(qName.equals("text")){
				readCharacters = true;
				writer.append("  <text><![CDATA[");
			}
			else if(qName.equals("revision")){
				readRev=true;
			}
			else if(qName.equals("id")){
				readId=true;
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
				writer.write("</document>");
				writer.newLine();
				writer.close();

				int len = articleid.length();
				String folder="";
				if(len<3) folder = completeWithZeros(articleid,3);
				else folder = articleid.substring(len-3, len);

				file.renameTo(new File(directory+"/"+folder+"/"+articleid+".xml"));
				
				listWriter.write(articleid+"="+title);
				listWriter.newLine();
				
				if(debug && narticle%1000==0) System.out.println(narticle+" articles parsed, elapsed time: "+TimeFormatter.toMSM(System.currentTimeMillis()-startTime));
			}
			else if(qName.equals("title")){
				readTitle = false;
				writer.append("]]></title>");
				writer.newLine();
			}
			else if(qName.equals("text")){
				writer.append("]]></text>");
				writer.newLine();
			}
			else if(qName.equals("id")){
				readId=false;
			}
			else if(qName.equals("revision")){
				readRev=false;
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		readCharacters = false;
	}

	String[] zeros = new String[]{"","0","00","000","0000","00000","000000","0000000"};
	private String completeWithZeros(int number, int len) {
		if((number+"").length()>=len) return number+"";
		return zeros[len-(number+"").length()]+number;
	}

	private String completeWithZeros(String number, int len) {
		if((number).length()>=len) return number;
		return zeros[len-(number).length()]+number;
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if(readId&&!readRev){
			articleid = new String(ch, start, length);
		}
		else if(readCharacters){
			try {

				String text = new String(ch, start, length);
				text = text.replaceAll(">", "&gt;");
				writer.write(text);
				
				if(readTitle) title = text;

			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	
	public static void execute(String wikifile, String outputDirectory){
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			/*InputStream inputStream = null;
			if(wikifile.endsWith(".xml")){
				inputStream = new FileInputStream(wikifile);
			}
			else if(wikifile.endsWith(".bz2")){
				
			}*/
			
			parser.parse(wikifile, new WikiFileSplitter(outputDirectory));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

/*	public static void main(String[] args) throws Exception{
		System.out.println("Activity started...");
		long start = System.currentTimeMillis();
		
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		parser.parse("/home/rdorado/Downloads/eswiki-latest-pages-articles.xml", new WikiFileSplitter("/home/rdorado/Downloads/wikipedia/output/"));
		
		System.out.println("Time spent: "+TimeFormatter.toMSM(System.currentTimeMillis()-start));
	}
*/
}
