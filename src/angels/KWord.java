package angels;

import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.KeywordsResult;

class Property {
	double relev;

	public Property() {
		relev = 1;
	}

}

public class KWord {
	double joy, anger, disgust, sadness, fear, sentiment, relev;
	String text;
	Property property;
	
	
	public Pair<String, Integer, Double> getMax() {
		Double max=joy;
		int index=0;
		if (anger>joy){
			max=anger;
			index=1;
		}
		if (disgust>anger){
			max=disgust;
			index=2;
		}
		if (sadness>disgust){
			max=sadness;
			index=3;
		}
		if (fear>sadness){
			max=fear;
			index=4;
		}
		//return index;
		String ans;
		switch (index) {
		case 0:
			ans= "anger";
		case 1:
			ans= "disgust";
		case 2:
			ans= "fear";
		case 3:
			ans= "joy";
		case 4:
			ans= "sadness";
		default:
			ans= "";
		}
		return new Pair<String, Integer, Double>(ans,index,max);
	}

	public KWord(KeywordsResult x) {
		this.joy = x.getEmotion().getJoy();
		this.anger = x.getEmotion().getAnger();
		this.disgust = x.getEmotion().getDisgust();
		this.sadness = x.getEmotion().getSadness();
		this.fear = x.getEmotion().getFear();
		this.sentiment = x.getSentiment().getScore();
		this.text = x.getText();
		this.relev = x.getRelevance();
	}

	public double getJoy() {
		return joy;
	}

	public double getAnger() {
		return anger;
	}

	public double getDisgust() {
		return disgust;
	}

	public double getSadness() {
		return sadness;
	}

	public double getFear() {
		return fear;
	}

	public double getSentiment() {
		return sentiment;
	}

	public double getRelev() {
		return relev;
	}

	public String getText() {
		return text;
	}

	public String toString() {
		return "Text= " + text + " [joy=" + joy + ", anger=" + anger + ", disgust=" + disgust + ", sadness=" + sadness
				+ ", fear=" + fear + ", sentiment=" + sentiment + ", relev=" + relev + "]";
	}

}
