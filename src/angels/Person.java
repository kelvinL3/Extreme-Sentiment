package angels;

import java.util.ArrayList;

import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.KeywordsResult;

public class Person {
	ArrayList<KWord> relevKeywords;
	ArrayList<Post> twits = new ArrayList<>();

	public Person() {
		relevKeywords = new ArrayList<>();
		twits = new ArrayList<>();
	}

	public void addKeyword(KeywordsResult x) {
		relevKeywords.add(new KWord(x));
	}

	public void addTwit(String s) {
		twits.add(new Post(s));
	}

	public void populateProperties() {
		double t = 0.4;
		for (int x = 0; x < this.relevKeywords.size(); x++) {
			KWord k = relevKeywords.get(x);
			if (k.property != null) {
				continue;
			}
			Property p = new Property();
			k.property = p;
			for (int y = x; y < this.relevKeywords.size(); y++) {
				if(main.stringMatch(k.getText(), relevKeywords.get(y).getText())>=0.4){
					p.relev++;
					if(relevKeywords.get(y).property==null){
						relevKeywords.get(y).property = p;
					}
				}
			}
		}
	}

	public void populateUnderstanding() {
		this.twits.forEach(e -> main.generateUnderstanding(e.getContent(), this));
	}

	public ArrayList<KWord> getRelevKeywords() {
		return relevKeywords;
	}

	public ArrayList<Post> getTwits() {
		return twits;
	}

}
