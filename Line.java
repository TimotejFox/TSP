import java.util.ArrayList;

public class Line {

	private Long[] prices;
	private ArrayList<String> history;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Line(Long[] prices, ArrayList history) {
		this.prices = prices;
		this.history = history;
	}

	public Long[] getPrices() {
		return prices;
	}

	public void setPrices(Long[] prices) {
		this.prices = prices;
	}

	@SuppressWarnings("rawtypes")
	public ArrayList getHistory() {
		return history;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setHistory(ArrayList history) {
		this.history = history;
	}
	
	@Override
	public String toString(){
		String ret = this.prices[1] + System.lineSeparator();
		for(int i = 0; i < this.history.size(); i++) {
			ret += this.history.get(i) + System.lineSeparator();
		}
		return ret;
	}
}
