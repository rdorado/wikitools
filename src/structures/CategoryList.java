package structures;

import java.util.ArrayList;

public class CategoryList {
	ArrayList<CategoryNode> categories = new ArrayList<CategoryNode>();
	void addCategoryFromString(String name, int level){
		CategoryNode cn = new CategoryNode(name, level);
	}
}
