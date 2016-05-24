import categories.WikiKeywordsExtractorParser;
import filesystem.ParserRunner;
import filesystem.WikiFileSplitter;


public class Main {

	public static void main(String[] args) {
		/* Create wiki file system*/
		
		if(args.length < 3){
			printUsage();
		}
		else if(args[0].equals("split")){
			String wikifile = args[1];			
			String outputdir = args[2];
			//wikifile = "/home/rdorado/Downloads/eswiki-latest-pages-articles.xml";
			//outputdir = "/home/rdorado/Downloads/wikipedia/output/";
			WikiFileSplitter.execute(wikifile, outputdir);
		}
		else if(args[0].equals("clean")){
			String wikifile = args[1];			
			String outputdir = args[2];
			ParserRunner.execute(wikifile, outputdir, WikiTextTransformer.class, true);
		}
		else{
			printUsage();
		}
		//
		//WikiFileSplitter.execute("/home/rdorado/Downloads/enwiki-latest-pages-articles.xml", "/home/rdorado/Downloads/enwiki/output/");
		
		
		//ParserRunner.execute("/home/rdorado/Downloads/wikipedia/output/", "/home/rdorado/Downloads/wikipedia/text/", WikiTextTransformer.class, true);
		//ParserRunner.execute("/home/rdorado/Downloads/wikipedia/text/", "/home/rdorado/Downloads/wikipedia/keywords/", WikiKeywordsExtractorParser.class, true);
	}

	private static void printUsage() {
		System.out.println("Usage: java -jar wikitools.jar [split path/to/wikifile.xml path/to/outputDirectory|clean options inputDirectory outputDirectory]");
		
	}
	
}
