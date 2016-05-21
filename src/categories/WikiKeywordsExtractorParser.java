package categories;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import dorado.utils.WordCounter;

import parser.WikiBaseParser;
import text.TextHelper;


public class WikiKeywordsExtractorParser extends WikiBaseParser{

	ArrayList<String> notValidCategories = new ArrayList<String>();
	boolean readText=false;
	boolean readTitle=false;
	//BufferedWriter output;
	
	String title="";
	String text="";
	StringBuffer buffer = new StringBuffer();

	public WikiKeywordsExtractorParser(String outfilename) {
		super(outfilename);
		notValidCategories.add("Véase también");
		notValidCategories.add("Enlaces externos");
		notValidCategories.add("Referencias");
		notValidCategories.add("Bibliografía");
		notValidCategories.add("Notas");
		notValidCategories.add("Fuentes");
		notValidCategories.add("References");
		notValidCategories.add("Referencias citadas");
		notValidCategories.add("Enlaces en otros idiomas");
		
		
		
		
		/*try {
			output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outfilename)));
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}*/
	}
	
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		try {
			if(qName.equals("text")){
				output.write("  <text>");
				output.newLine();
				readText=true;
			}
			else if(qName.equals("title")){
				output.write("  <title>");
				readTitle=true;
			}
			else if(qName.equals("page")){
				output.write(" <page>");
				output.newLine();
				buffer=new StringBuffer();
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
				
				
				
				//output.write(text);
				output.write("</text>");
				output.newLine();
				//text="";
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
				
				String text = new String(ch, start, length).trim();
				String[] lines = text.split("\n");
				
				boolean skipSeccion=false;
				for (String line : lines) {
					if(line.startsWith("==") && line.endsWith("==")){

						
						if(!skipSeccion){
							
							String seccion = buffer.toString();
							ArrayList<String> arrayList = RelationExtractor.getAsArray(seccion);
							for (String str : arrayList) {
							//	System.out.println("[["+str+"]]");
								output.write("[["+str+"]]");
								output.newLine();
							}
							
							seccion=TextHelper.removeNonAlphabetical(seccion);
							seccion=seccion.toLowerCase();
							WordCounter wc = WordCounter.fromString(seccion);
							ArrayList<String> wordcounts = wc.getWords();
							for (String wordcnt : wordcounts) {
						//		System.out.println(wordcnt);
								output.write(wordcnt);
								output.newLine();
							}
							
						}
						

						if(!notValidCategories.contains(line.replaceAll("=", "").trim())){
							
							
							/*System.out.println();
							System.out.println(line);	*/
							
							output.newLine();
							output.write(line);
							output.newLine();
							skipSeccion=false;
						}
						else skipSeccion=true;
						
						buffer=new StringBuffer();
					}
					else{
						if(!skipSeccion) buffer.append(line+" ");
					}
		
				}
				
				
				
				/**/
				if(buffer.length()>0){
					String seccion = buffer.toString();
					ArrayList<String> arrayList = RelationExtractor.getAsArray(seccion);
					if(!skipSeccion){
						for (String str : arrayList) {
							//System.out.println("[["+str+"]]");
							output.write("[["+str+"]]");
							output.newLine();
						}
						
						seccion=TextHelper.removeNonAlphabetical(seccion);
						seccion=seccion.toLowerCase();
						WordCounter wc = WordCounter.fromString(seccion);
						ArrayList<String> wordcounts = wc.getWords();
						for (String wordcnt : wordcounts) {
					//		System.out.println(wordcnt);
							output.write(wordcnt);
							output.newLine();
						}
						
					}
				}
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
	
	
	private ArrayList<String> extractRelations(String line) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void main(String[] args) throws Exception{
		
		//String id = "25147";   // Perro
		//String id = "409000";  // Gato
		//String id = "25116";  // León
		
		//String id = "5131";  // Bogotá
		//String id = "13527";  // Tokio
		//String id = "2450";  // Quebec
		
		//String id = "53";  // Albert Einstein
		//String id = "18648";  // Pelé
		//String id = "15309";  // Isaac Asimov
		String id = "5692"; //Gabriel García Márquez
		
		
		System.out.println("Activity started...");
		long start = System.currentTimeMillis();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setValidating(false);
		SAXParser parser = factory.newSAXParser();
		
		
		WikiKeywordsExtractorParser wwc = new WikiKeywordsExtractorParser("/home/rdorado/Downloads/wikipedia/test/cat-"+id+".xml");
		parser.parse( "/home/rdorado/Downloads/wikipedia/test/wikitags-text-"+id+".xml",  wwc);
		//parser.parse( "/home/rdorado/Downloads/wikipedia/output/"+id.substring(id.length()-3)+"/"+id+".xml",  wwc);
		//wwc.print();
		
		
		
		//ParserRunner.execute("/home/rdorado/Downloads/wikipedia/output", "/home/rdorado/Downloads/wikipedia/categories", WikiCategoryExtractorParser.class);
	}



}

