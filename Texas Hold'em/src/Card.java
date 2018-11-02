import java.io.Serializable;

//card used in the game, has int value and suit
public class Card implements Serializable{
	private int num;
	private Suit suit;

	/*
	 * constructor
	 * 
	 * @parameters: number of card and suit
	 */
	public Card(int num, Suit suit) {
		this.num = num;
		this.suit = suit;
	}
	
	public String transferStr(){
		return getNum()+"/"+suit.getSuit();
	}

	private static Card constructCard(String line) {
		System.out.println(line);
		int seperator = line.indexOf('/');
		int cardNum = Integer.parseInt(line.substring(0, seperator));
		Card card;
		if (line.substring(seperator + 1).startsWith("S")) {
			card = new Card(cardNum, Suit.SPADE);
		} else if (line.substring(seperator + 1).startsWith("C")) {
			card = new Card(cardNum, Suit.CLUB);
		} else if (line.substring(seperator + 1).startsWith("D")) {
			card = new Card(cardNum, Suit.DIAMOND);
		} else {
			card = new Card(cardNum, Suit.HEART);
		}
		//System.out.println(card.toString());
		return card;
	}

	public static Card[] readHand(String str, Card[] cards){
		System.out.println(str);
		if(str.length()<2){
			return cards;
		}
		int split = str.indexOf(",");
		System.out.println(split);
		for(int i = 0; i<5; i++){
			if(cards[i]==null){
				cards[i] = constructCard(str.substring(0, split));
				break;
			}
		}
		return readHand(str.substring(split+1), cards);
	}
	
	// toString
	public String toString() {
		String str = "";
		if (getNum() == 14) {
			str += "A of " + suit.getSuit();
		} else if (getNum() < 11) {
			str += getNum() + " of " + suit.getSuit();
		} else if (getNum() == 11) {
			str += "J of " + suit.getSuit();
		} else if (getNum() == 12) {
			str += "Q of " + suit.getSuit();
		} else if (getNum() == 13) {
			str += "K of " + suit.getSuit();
		} else {
			return "No Card Exists";
		}
		return str;
	}

	/**
	 * 
	 * @return
	 */
	public int getNum() {
		return num;
	}

	/**
	 * 
	 * @return
	 */
	public Suit getSuit() {
		return suit;
	}
	
	
	public static void main(String[] args){
		String str = "5/Club,6/Spade,10/Diamond,2/Club,9/Heart,";
		Card[] card = new Card[5];
		card = readHand(str, card);
		String b = "";
		for(int i = 0; i<5; i++){
			b+=card[i].toString()+", ";
		}
		System.out.println(b);
	}

}