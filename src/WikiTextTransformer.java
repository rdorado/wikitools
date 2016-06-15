import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import parser.WikiBaseParser;
import text.TextHelper;


public class WikiTextTransformer extends WikiBaseParser{

	boolean removeCDATA=false;
	
	boolean readText=false;
	boolean readTitle=false;
	
	boolean leaverel=false;
	String title="";
	String text="";
	String[] options;
	/*WordModel model = new WordModel();
	PhraseModel phraseModel = new PhraseModel();
	*/
	public WikiTextTransformer(String outfilename, String... options) {
		super(outfilename);
		 this.options=options;
		 if(options!=null && options.length>0){
			 for(String opt : options){
				 if(opt.equals("-r")){
					 leaverel=true;
				 }
			 }
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
				
				if(removeCDATA){
					text=TextHelper.removeCDATA(text);
				}
				
				text=TextHelper.removeAllWikiInfo(text,"{{","}}");
				text=TextHelper.removeAllWikiInfo(text,"{|","|}");
				
				text=TextHelper.replaceXMLSymbols(text);
				
				text=TextHelper.removeAllWikiInfo(text,"<ref","</ref>");
				text=TextHelper.removeAllWikiInfo(text,"<ref","/>");
				text=TextHelper.removeAllWikiInfo(text,"<gallery","</gallery>");
				text=TextHelper.removeAllWikiInfo(text,"<div","</div>");
				text=TextHelper.removeAllWikiInfo(text,"<math","</math>");
				text=TextHelper.removeAllWikiInfo(text,"<sup","</sup>");
				text=TextHelper.removeAllWikiInfo(text,"<br",">");
				text=TextHelper.removeAllWikiInfo(text,"</br",">");

				text=text.replaceAll("</b>", "");
				text=text.replaceAll("<center>", "");
				text=text.replaceAll("</center>", "");
				
				text=text.replaceAll("<sub>", "_");
				text=text.replaceAll("</sub>", "");
				
				text=text.replaceAll("''", "");
				text=text.replaceAll("&", "&amp;");
				text=text.replaceAll("\\*", " ");
				text=text.replaceAll("<blockquote>", "");
				text=text.replaceAll("</blockquote>", "");
				/**
				 * Relations info
				 */
				if(!leaverel){
					text=TextHelper.removeAllWikiInfo(text,"[[","]]","|");

					text=text.replaceAll("====", "");
					text=text.replaceAll("===", "");
					text=text.replaceAll("==", "");
				}
				
				
			//	text=text.replaceAll(" http://(\\w*.)+ ", " ");
				
				output.write(text+"</text>");
				output.newLine();
				text="";
				readText=false;
				
			}
			else if(qName.equals("page")){
				output.write(" </page>");
				output.newLine();
			}
			else if(qName.equals("title")){
				
				if(removeCDATA){
					title=TextHelper.removeCDATA(title);
				}
				if(title.contains("&")) title=title.replaceAll("&", "&amp;");
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
				text = text + new String(ch, start, length);
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
		String id = "5692";   // Perro
		
		System.out.println("Activity started...");
		long start = System.currentTimeMillis();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setValidating(false);
		SAXParser parser = factory.newSAXParser();
		WikiTextTransformer wwc = new WikiTextTransformer("/home/rdorado/Downloads/wikipedia/test/wikitags-text-"+id+".xml");
		parser.parse( "/home/rdorado/Downloads/wikipedia/output/692/"+id+".xml",  wwc);
		//parser.parse( "/home/rdorado/Downloads/wikipedia/output/147/"+id+".xml",  wwc);
		//wwc.print();
	}



}
