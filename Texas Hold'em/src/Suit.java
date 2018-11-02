//suit enum, used in card class
public enum Suit {
	SPADE("Spade"), CLUB("Club"), HEART("Heart"), DIAMOND("Diamond");
	private final String suit;

	/*
	 * constructor
	 * 
	 * parameters: String of suit name
	 */
	Suit(String a) {
		this.suit = a;
	}

	/**
	 * 
	 * @return 
	 */
	public String getSuit() {
		return suit;
	}

}