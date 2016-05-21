package filesystem;

import java.io.File;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.ext.DefaultHandler2;

import dorado.utils.TimeFormatter;

import parser.WikiBaseParser;

public class ParserRunner {

	public static void execute(String inputDirectory, String outputDirectoy, Class<? extends WikiBaseParser> handlerClass) {
		execute(inputDirectory, outputDirectoy, handlerClass, false);
	}
	
	public static void execute(String inputDirectory, String outputDirectoy, Class<? extends WikiBaseParser> handlerClass, boolean debug) {
		SAXParser parser=null;
		int narticle=0;
		long startTime = System.currentTimeMillis();
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setValidating(false);
			parser = factory.newSAXParser();
			
		} 
		catch (Exception e) {
			e.printStackTrace();
			return;
		}
		System.out.println();
		File f = new File(inputDirectory);
		if(f.isDirectory()){
			File[] directories = f.listFiles();
			for (File directory : directories) {
				if(directory.isDirectory()){
					File[] files = directory.listFiles();
					String dirTmp = directory.toString().replaceAll(inputDirectory, "");
					new File(outputDirectoy+"/"+dirTmp).mkdirs();
					for (File file : files) {
						narticle++;
						try {
							
							WikiBaseParser objParser = handlerClass.getConstructor(String.class).newInstance(outputDirectoy+"/"+dirTmp+"/"+file.getName());
							parser.parse(file, objParser);
							
							if(debug && narticle%1000==0) System.out.println(narticle+" articles parsed, elapsed time: "+TimeFormatter.toMSM(System.currentTimeMillis()-startTime));
														
						} 
						catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		
		if(debug && narticle%1000!=0) System.out.println(narticle+" articles parsed, elapsed time: "+TimeFormatter.toMSM(System.currentTimeMillis()-startTime));
		
	}	
	void execute(String inputDirectory, String outputDirectoy, WikiBaseParser handler){
		SAXParser parser=null;
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setValidating(false);
			parser = factory.newSAXParser();
		} 
		catch (Exception e) {
			return;
		}

		File f = new File(inputDirectory);
		if(f.isDirectory()){
			File[] directories = f.listFiles();
			for (File directory : directories) {
				if(directory.isDirectory()){
					File[] files = directory.listFiles();
					for (File file : files) {
						try {
							
							System.out.println(file);
							
							/*WikiBaseParser objParser = (handler.getClass().getConstructor(String.class)).newInstance(file.toString());
							parser.parse(file, objParser);*/
							
						} 
						catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		
		
		//handler.getClass().
		//parser.parse(, handler);
	}




	
}
