import categories.WikiKeywordsExtractorParser;
import filesystem.ParserRunner;
import filesystem.WikiFileSplitter;


public class Main {

	public static void main(String[] args) {
		/* Create wiki file system*/
		
		
		/*if(args[1].equals("split")){
			WikiFileSplitter.execute("/home/rdorado/Downloads/eswiki-latest-pages-articles.xml", "/home/rdorado/Downloads/wikipedia/output/");
		}
		else if(args[2].equals("clean")){
			ParserRunner.execute("/home/rdorado/Downloads/wikipedia/output/", "/home/rdorado/Downloads/wikipedia/text/", WikiTextTransformer.class, true);
		}*/
		//
		//WikiFileSplitter.execute("/home/rdorado/Downloads/enwiki-latest-pages-articles.xml", "/home/rdorado/Downloads/enwiki/output/");
		
		
		//ParserRunner.execute("/home/rdorado/Downloads/wikipedia/output/", "/home/rdorado/Downloads/wikipedia/text/", WikiTextTransformer.class, true);
		ParserRunner.execute("/home/rdorado/Downloads/wikipedia/text/", "/home/rdorado/Downloads/wikipedia/keywords/", WikiKeywordsExtractorParser.class, true);
	}
	
}
