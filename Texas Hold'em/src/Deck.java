// array of Card used in game
public class Deck {

	private Card[] deck = new Card[52];
	private int cardCount;
	/*
	 * constructor
	 * 
	 * @full deck of cards
	 */
	public Deck() {
		cardCount = 0;
		for (int i = 0; i < 13; i++) {
			deck[i] = new Card(i + 2, Suit.SPADE);
			deck[i + 13] = new Card(i + 2, Suit.CLUB);
			deck[i + 26] = new Card(i + 2, Suit.DIAMOND);
			deck[i + 39] = new Card(i + 2, Suit.HEART);
		}
		shuffle();
	}

	/*
	 * @method shuffle
	 * 
	 * @parameters:
	 * 
	 * @return
	 */
	public void shuffle() {
		for (int i = 0; i < deck.length; i++) {
			int swapIndex = (int) (52 * Math.random());
			Card temp = deck[i];
			deck[i] = deck[swapIndex];
			deck[swapIndex] = temp;
		}
	}

	// toString
	public String toString() {
		String str = "";
		for (int i = 0; i < deck.length; i++) {
			str += deck[i].toString() + "\n";
		}
		return str;
	}

	/*
	 * @method: deal
	 * 
	 * @deals card from deck
	 */
	public Card deal() {
		Card temp = deck[cardCount];
		cardCount++;
		return temp;
	}

	/*
	 * @method: reset
	 * 
	 * @resets deck
	 */
	public void reset() {
		this.shuffle();
		cardCount = 0;
	}

	public static void main(String[] args) {
		Deck d = new Deck();
		System.out.println(d);

	}

}