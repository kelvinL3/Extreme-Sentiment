package angels;

public class Post {
	String content;
	double r;

	public Post(String text) {
		this.content = text;
		this.r = 1;
	}

	public void mutiplyR(double factor) {
		this.r *= factor;
	}

	public String getContent() {
		return content;
	}

	public String toString() {
		return content;
	}
}
