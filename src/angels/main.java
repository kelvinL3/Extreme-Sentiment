package angels;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalyzeOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.EntitiesOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.Features;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.KeywordsOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.KeywordsResult;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.SentimentOptions;
import com.sparkpost.Client;
import com.sparkpost.exception.SparkPostException;

class Pair<String, Integer, Double> {
	public final String t;
	public final Integer i;
	public final double u;

	public Pair(String t, Integer i, double u) {
		this.t = t;
		this.i = i;
		this.u = u;
	}
}
/*
 * Irrelavant Happy Birthday to my mom. Hope she is doing well! I'm not sure how
 * to post this as a picture but if I go to post a picture and scroll back too
 * far in my camera roll it only shows my videos but none of my pictures intern
 * reacts only please Bought last week for my 18 month old grandson but never
 * used. manufacturer date is jan. 2017 ... So my son woke up the other morning
 * but not for long. He laughed really hard and said your wearing glasses, then
 * said where'd he go. I had to ask him after he got up what his dream was
 * about, he said a bad guy tried to hide by putting glasses on. He didn't
 * remember talking not did he ask how I knew about his dream. Lol Visual Studio
 * Code is now made available on ARM platforms like Chromebooks and Raspberry Pi
 * The album is currently in the mixing process. Thanks for being patient. I
 * really like how it is turning out and am excited for you all to hear it.
 * Constructive criticism but it involves building a statue that tells you how
 * you suck
 */

// Negative
// Why do we allow other people to take our hard earned money?
// Welfare is a leech on society! Must stop!
// We should burn Washington to the ground and rebuild it with actual integrity.
// #Paris Temper tantrum Muslim telling ppl not to eat but not everyone is
// Muslim & that jackass doesn't rule the world!! #Ramadan #Extremists
// 23,000 jihadist #extremists in Britain. THAT really explains why you have to
// get rid of #EU immigrants from Poland.
// A short term ban @potus sought of those wanting to enter our country from
// majority Muslim countries,make it happen #extremists #Manchester
// Meddling foreign powers is the reason why the Middle East has been destroyed.
// They should pay for the damages.
// Nobody in Washington cares about Americans or any other citizen in other
// countries.
//
//
//
//
public class main {

	public static ArrayList<String> specialPos = new ArrayList<String>();
	public static ArrayList<String> specialNeg = new ArrayList<String>();

	public static void main(String[] args) throws Throwable {
		Person p = new Person();
		System.out.println("Please enter a file name for social media behavior: ");
		System.out.println("Using p1twits.txt test data...");
		Scanner sc = new Scanner(new File("p1twits.txt"));
		while (sc.hasNextLine()) {
			p.addTwit(sc.nextLine());
		}
		sc.close();
		sc = new Scanner(new File("negWords.txt"));
		while (sc.hasNextLine()) {
			specialNeg.add(sc.nextLine());
		}
		sc.close();
		sc = new Scanner(new File("posWords.txt"));
		while (sc.hasNextLine()) {
			specialPos.add(sc.nextLine());
		}
		sc.close();

		processData(p);
	}

	public static void processData(Person p) {
		System.out.println("Analyzing Social Media Behavior and Populating Understanding...");
		p.populateUnderstanding();
		System.out.println("Aggregating Analysis, please wait...");
		p.populateProperties();
		String dom = dominantEmotion(p, 0.25);
		Pair<String, Integer, Double> pair = analysis(p);
		String additional = additionalUnderstanding(p, "", pair.t);
		
		String t = findHighestEmotion(dom, p);
		
		
		if (dom.contains("joy"))
			System.out.println("No signs of significant emotions found, please try additonal social media profiles.");
		if (dom.contains("sad"))
			dom = "sadness and/or anger";
		postman("This person is generally feeling a high degree of " + dom
				+ " in their social media expressions that may cause them to take upon extremist views.");
		System.out.println("This person is generally feeling a high degree of " + dom
				+ " in their social media expressions that may cause them to take upon extremist views.");
		postman("The primary source of the emotion arises from " + t.split("`")[0]
				+ " and throughout their social media activity, we saw they felt strongly about their position.");
		System.out.println("The primary source of the emotion arises from " + t.split("`")[0]
				+ " and throughout their social media activity, we saw they felt strongly about their position.");
		postman("Statistically, they seem to believe in this idea with " + t.split("`")[1] + " certainty.");
		System.out.println("Statistically, they seem to believe in this idea with " + t.split("`")[1] + " certainty.");
		if (additional.length() > 6){
			System.out.println(
					"We also found additional sources of strong emotions regarding these topics | " + additional);
			postman("We also found additional sources of strong emotions regarding these topics | " + additional);
		}
		
		System.out.println("We recommend reaching out to some of the services listed here http://www.ltai.info/spotting-signs/ with our analysis.");
		postman("We recommend reaching out to some of the services listed here http://www.ltai.info/spotting-signs/ with our analysis.");

	}

	public static void generateUnderstanding(String text, Person p) {
		if (text.isEmpty())
			return;
		NaturalLanguageUnderstanding service = new NaturalLanguageUnderstanding(
				NaturalLanguageUnderstanding.VERSION_DATE_2017_02_27, "21ba6828-ccf6-4c8b-a24c-cc785f789719",
				"PQe2xwEkOJOA");

		EntitiesOptions entitiesOptions = new EntitiesOptions.Builder().emotion(true).sentiment(true).limit(5).build();

		KeywordsOptions keywordsOptions = new KeywordsOptions.Builder().emotion(true).sentiment(true).limit(5).build();

		SentimentOptions so = new SentimentOptions.Builder().build();

		Features features = new Features.Builder().entities(entitiesOptions).keywords(keywordsOptions).sentiment(so)
				.build();

		AnalyzeOptions parameters = new AnalyzeOptions.Builder().text(text).features(features).build();

		AnalysisResults response = service.analyze(parameters).execute();
		if (response.getSentiment().getDocument().getScore() > 0)
			return;
		for (KeywordsResult kr : response.getKeywords()) {
			if (kr.getRelevance() > 0.2) {
				System.out.println("Keyword added: " + kr.getText());
				kr.getEmotion().setJoy(0.0);
				p.addKeyword(kr);
			}
		}
		System.out.println(p.relevKeywords);
	}

	public static String additionalUnderstanding(Person p, String keyword, String emotion) {
		String text = "";
		for (Post post : p.twits) {
			text += post.getContent() + ".";
		}
		NaturalLanguageUnderstanding service = new NaturalLanguageUnderstanding(
				NaturalLanguageUnderstanding.VERSION_DATE_2017_02_27, "21ba6828-ccf6-4c8b-a24c-cc785f789719",
				"PQe2xwEkOJOA");

		EntitiesOptions entitiesOptions = new EntitiesOptions.Builder().emotion(true).sentiment(true).limit(1).build();

		KeywordsOptions keywordsOptions = new KeywordsOptions.Builder().emotion(true).sentiment(true).limit(1).build();

		Features features = new Features.Builder().entities(entitiesOptions).keywords(keywordsOptions).build();

		AnalyzeOptions parameters = new AnalyzeOptions.Builder().text(text).features(features).build();

		AnalysisResults response = service.analyze(parameters).execute();
		String ret = " ";
		for (KeywordsResult kr : response.getKeywords()) {
			ret += kr.getText() + " | ";
		}
		return ret;
	}

	public static boolean checkEmotion(KeywordsResult k, String e) {
		Double max = k.getEmotion().getJoy();
		String maxemotion = "joy";
		if (k.getEmotion().getAnger() > k.getEmotion().getJoy()) {
			max = k.getEmotion().getAnger();
			maxemotion = "anger";
		}
		if (k.getEmotion().getDisgust() > k.getEmotion().getAnger()) {
			max = k.getEmotion().getDisgust();
			maxemotion = "disgust";
		}
		if (k.getEmotion().getSadness() > k.getEmotion().getDisgust()) {
			max = k.getEmotion().getSadness();
			maxemotion = "sadness";
		}
		if (k.getEmotion().getFear() > k.getEmotion().getSadness()) {
			max = k.getEmotion().getFear();
			maxemotion = "fear";
		}
		return e.equals(maxemotion);
	}

	public static String findHighestEmotion(String n, Person poi) {
		String emotionID = "";
		double max = 0;
		String returned = "";
		double relavancy=0;
		ArrayList<KWord> input = poi.relevKeywords;
		switch (n) {
		case "anger":
			emotionID = "anger";
			for (int i = 0; i < input.size(); i++) {
				double x = input.get(i).anger;
				if (x > max) {
					max = x;
					returned = input.get(i).text;
					relavancy=input.get(i).property.relev;
				}
			}break;
		case "disgust":
			emotionID = "disgust";
			for (int i = 0; i < input.size(); i++) {
				double x = input.get(i).disgust;
				if (x > max) {
					max = x;
					returned = input.get(i).text;
					relavancy=input.get(i).property.relev;
				}
			}break;
		case "fear":
			emotionID = "fear";
			for (int i = 0; i < input.size(); i++) {
				double x = input.get(i).fear;
				if (x > max) {
					max = x;
					returned = input.get(i).text;
					relavancy=input.get(i).property.relev;
				}
			}break;
		case "joy":
			emotionID = "joy";
			for (int i = 0; i < input.size(); i++) {
				double x = input.get(i).joy;
				if (x > max) {
					max = x;
					returned = input.get(i).text;
					relavancy=input.get(i).property.relev;
				}
			}break;
		case "sadness":
			emotionID = "anger";
			for (int i = 0; i < input.size(); i++) {
				double x = input.get(i).anger;
				if (x > max) {
					max = x;
					returned = input.get(i).text;
					relavancy=input.get(i).property.relev;
				}
			} break;
		default:
			emotionID = "";
		}
		
		
		return (returned + "`" + max);
	}

	public static Pair<String, Integer, Double> analysis(Person poi) {
		ArrayList<KWord> input = poi.relevKeywords;
		double[] max = new double[input.size()]; // stores the strongest emotion
													// for every entry
		String[] maxLabel = new String[input.size()]; // stores the value of the
														// strongest emotion for
														// every entry
		int[] overallCounter = { 0, 0, 0, 0, 0 };

		for (int i = 0; i < input.size(); i++) {
			Pair<String, Integer, Double> x = input.get(i).getMax();
			maxLabel[i] = x.t;
			max[i] = x.u;
			overallCounter[x.i] += 1;
		}
		int m = 0;
		int ind = 0;
		for (int i = 0; i < 5; i++) {
			if (overallCounter[i] > m) {
				m = overallCounter[i];
				ind = i;
			}
		}
		String ans;
		switch (ind) {
		case 0:
			ans = "anger";
		case 1:
			ans = "disgust";
		case 2:
			ans = "fear";
		case 3:
			ans = "joy";
		case 4:
			ans = "sadness";
		default:
			ans = "";
		}
		// return the string of the strongest overall emotion, and the
		// percentage of kwords it dominates
		return new Pair<String, Integer, Double>(ans, ind, m / input.size());
	}

	public static String dominantEmotion(Person poi, double t) {
		ArrayList<KWord> input = poi.relevKeywords;
		int[] count = new int[5];
		count[0] = 0;
		count[1] = 0;
		count[2] = 0;
		count[3] = 0;
		count[4] = 0;
		input.forEach(e -> {
			if (e.anger > t) {
				count[0] += 1;
			}
			if (e.disgust > t) {
				count[1] += 1;
			}
			if (e.fear > t) {
				count[2] += 1;
			}
			if (e.joy > t) {
				count[3] += 1;
			}
			if (e.sadness > t) {
				count[4] += 1;
			}
		});
		int max = 0;
		int index = 0;
		for (int i = 0; i < count.length; i++) {
			if (count[i] > max) {
				max = count[i];
				index = i;
			}
		}
		switch (index) {
		case 0:
			return "anger";
		case 1:
			return "disgust";
		case 2:
			return "fear";
		case 3:
			return "joy";
		case 4:
			return "sadness";
		default:
			return "";
		}
	}

	public static Pair<String, Integer, Double> processKeywords() {

		return null;

	}

	public static double stringMatch(String a, String b) {
		double match = 0, total = a.split(" ").length + b.split(" ").length;
		a = a.toLowerCase();
		b = b.toLowerCase();
		for (String asub : a.split(" ")) {
			for (String bsub : b.split(" ")) {
				if (asub.equals(bsub))
					match += 2 / total;
			}
		}
		return match;
	}
	/* 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	
	public static void postman(String x){
		String API_KEY = "ef12fd3a72223878f8c9de65b56ae890aeaf50bf";
        Client client = new Client(API_KEY);

        try {
			client.sendMessage(
			        "admin@bleak.io",
			        "19083427522@tmomail.net",
			        "ExtremeSentiment - Advocacy Analysis",
			        x,x);
		} catch (SparkPostException e) {
			System.err.println("Error in sending email!");
			// TODO Auto-generated catch block
		}
        try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
