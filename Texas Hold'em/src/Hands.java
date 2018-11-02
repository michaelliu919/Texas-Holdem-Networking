import java.math.BigDecimal;

//this class is for the sole purpose of poker hand recognition
public class Hands {
	/*
	 * Method isFlush
	 * 
	 * @parameters a possible combination of cards from player hand and table
	 * 
	 * @return true if it (method title)
	 */
	public static boolean isFlush(Card[] h) {
		Suit suit = h[0].getSuit();
		int i = 1;
		while (i < h.length) {
			if (h[i].getSuit() == suit) {
				i++;
			} else {
				return false;
			}
		}
		return true;
	}

	/*
	 * Method isStraight
	 * 
	 * @parameters a possible combination of cards from player hand and table
	 * 
	 * @return true if it (method title)
	 */
	public static boolean isStraight(Card[] h) {
		Card temp = h[0];
		for (int i = 1; i < h.length; i++) {
			if (temp.getNum() != h[i].getNum() - 1) {
				return false;
			}
			temp = h[i];
		}
		return true;
	}

	/*
	 * Method isFourOfAKind
	 * 
	 * @parameters a possible combination of cards from player hand and table
	 * 
	 * @return true if it (method title)
	 */
	public static boolean isFourOfAKind(Card[] h) {
		return (h[0].getNum() == h[1].getNum() && h[1].getNum() == h[2].getNum() && h[2].getNum() == h[3].getNum())
				|| (h[3].getNum() == h[4].getNum() && h[1].getNum() == h[2].getNum() && h[2].getNum() == h[3].getNum());
	}

	/*
	 * Method isFullHouse
	 * 
	 * @parameters a possible combination of cards from player hand and table
	 * 
	 * @return true if it (method title)
	 */
	public static boolean isFullHouse(Card[] h) {
		return (h[0].getNum() == h[1].getNum() && h[2].getNum() == h[3].getNum() && h[3].getNum() == h[4].getNum())
				|| (h[0].getNum() == h[1].getNum() && h[1].getNum() == h[2].getNum() && h[3].getNum() == h[4].getNum());
	}

	/*
	 * Method isThreeOfAKind
	 * 
	 * @parameters a possible combination of cards from player hand and table
	 * 
	 * @return true if it (method title)
	 */
	public static boolean isThreeOfAKind(Card[] h) {
		return (h[0].getNum() == h[1].getNum() && h[1].getNum() == h[2].getNum())
				|| (h[2].getNum() == h[3].getNum() && h[1].getNum() == h[2].getNum())
				|| (h[3].getNum() == h[4].getNum() && h[2].getNum() == h[3].getNum());

	}

	/*
	 * Method isTwoPair
	 * 
	 * @parameters a possible combination of cards from player hand and table
	 * 
	 * @return true if it (method title)
	 */
	public static boolean isTwoPair(Card[] h) {
		return (h[0].getNum() == h[1].getNum() && h[2].getNum() == h[3].getNum()
				|| h[1].getNum() == h[2].getNum() && h[3].getNum() == h[4].getNum()
				|| h[0].getNum() == h[1].getNum() && h[3].getNum() == h[4].getNum());
	}

	/*
	 * Method isPair
	 * 
	 * @parameters a possible combination of cards from player hand and table
	 * 
	 * @return true if it (method title)
	 */
	public static boolean isPair(Card[] h) {
		return (h[0].getNum() == h[1].getNum() || h[1].getNum() == h[2].getNum() || h[2].getNum() == h[3].getNum()
				|| h[3].getNum() == h[4].getNum());
	}

	/*
	 * 
	 */
	public static int compareHand(Card[] j, Card[] h) {
		if (valueOfHand(j).compareTo(valueOfHand(h)) > 0) {
			return 1;
		} else if (valueOfHand(j).compareTo(valueOfHand(h)) < 0) {
			return 2;
		} else {
			return 0;
		}
	}

	/*
	 * Method valueOfHand
	 * 
	 * @parameters a possible combination of cards from player hand and table
	 * 
	 * @return numerical value of specified hand
	 */
	public static BigDecimal valueOfHand(Card[] h) {
		BigDecimal value = new BigDecimal("0");
		//
		if (isFlush(h) && isStraight(h)) {
			value = value.add(new BigDecimal("10000000000000000"));
			value = value.add(firstCard(h));
			//
		} else if (isFourOfAKind(h)) {
			value = value.add(new BigDecimal("100000000000000"));
			if (h[0] == h[1]) {
				value = value.multiply(firstCard(h));
				int i = h[5].getNum();
				String a = Integer.toString(i);
				BigDecimal val = new BigDecimal(a);
				value = value.add(val);
			} else if (h[3] == h[4]) {
				int i = h[4].getNum();
				String a = Integer.toString(i);
				BigDecimal val = new BigDecimal(a);
				value = value.multiply(val);
				value = value.add(firstCard(h));
			}
		} else if (isFullHouse(h)) {
			value = value.add(new BigDecimal("1000000000000"));
			if (h[0] == h[1] && h[1] == h[2]) {
				value = value.multiply(firstCard(h));
				int i = h[5].getNum();
				String a = Integer.toString(i);
				BigDecimal val = new BigDecimal(a);
				value = value.add(val);
			} else if (h[2] == h[3] && h[3] == h[4]) {
				int i = h[4].getNum();
				String a = Integer.toString(i);
				BigDecimal val = new BigDecimal(a);
				value = value.multiply(val);
				value = value.add(firstCard(h));
			}
		} else if (isFlush(h)) {
			value = value.add(new BigDecimal("10000000000"));
			value = value.multiply(firstCard(h));
			value = value.add(secondCard(h));
			value = value.add(thirdCard(h).divide(new BigDecimal("100")));
			value = value.add(fourthCard(h).divide(new BigDecimal("10000")));
			value = value.add(fifthCard(h).divide(new BigDecimal("1000000")));
		} else if (isStraight(h)) {
			value = value.add(new BigDecimal("100000000"));
			value = value.add(firstCard(h));
		} else if (isThreeOfAKind(h)) {
			value = value.add(new BigDecimal("1000000"));
			if (h[0].getNum() == h[1].getNum() && h[1].getNum() == h[2].getNum()) {
				value = value.multiply(firstCard(h));
				value = value.add(fourthCard(h));
				value = value.add(fifthCard(h).divide(new BigDecimal("100")));
			} else if ((h[2].getNum() == h[3].getNum() && h[1].getNum() == h[2].getNum())) {
				value = value.multiply(secondCard(h));
				value = value.add(firstCard(h));
				value = value.add(fifthCard(h).divide(new BigDecimal("100")));
			} else if ((h[3].getNum() == h[4].getNum() && h[2].getNum() == h[3].getNum())) {
				value = value.multiply(thirdCard(h));
				value = value.add(firstCard(h));
				value = value.add(secondCard(h).divide(new BigDecimal("100")));
			}
		} else if (isTwoPair(h)) {
			value = value.add(new BigDecimal("10000"));
			if (h[0].getNum() == h[1].getNum() && h[2].getNum() == h[3].getNum()) {
				value = value.multiply(firstCard(h));
				value = value.add(thirdCard(h));
				value = value.add(fifthCard(h).divide(new BigDecimal("100")));
			} else if (h[1].getNum() == h[2].getNum() && h[3].getNum() == h[4].getNum()) {
				value = value.multiply(secondCard(h));
				value = value.add(fourthCard(h));
				value = value.add(firstCard(h).divide(new BigDecimal("100")));
			} else if (h[0].getNum() == h[1].getNum() && h[3].getNum() == h[4].getNum()) {
				value = value.multiply(firstCard(h));
				value = value.add(fourthCard(h));
				value = value.add(thirdCard(h).divide(new BigDecimal("100")));
			}
		} else if (isPair(h)) {
			value = value.add(new BigDecimal("100"));
			if (h[0].getNum() == h[1].getNum()) {
				value = value.multiply(firstCard(h));
				value = value.add(thirdCard(h));
				value = value.add(fourthCard(h).divide(new BigDecimal("100")));
				value = value.add(fifthCard(h).divide(new BigDecimal("10000")));
			} else if (h[1].getNum() == h[2].getNum()) {
				value = value.multiply(secondCard(h));
				value = value.add(firstCard(h));
				value = value.add(fourthCard(h).divide(new BigDecimal("100")));
				value = value.add(fifthCard(h).divide(new BigDecimal("10000")));
			} else if (h[2].getNum() == h[3].getNum()) {
				value = value.multiply(thirdCard(h));
				value = value.add(firstCard(h));
				value = value.add(secondCard(h).divide(new BigDecimal("100")));
				value = value.add(fifthCard(h).divide(new BigDecimal("10000")));
			} else if (h[3].getNum() == h[4].getNum()) {
				value = value.multiply(fourthCard(h));
				value = value.add(firstCard(h));
				value = value.add(secondCard(h).divide(new BigDecimal("100")));
				value = value.add(thirdCard(h).divide(new BigDecimal("10000")));
			}
		} else {
			value = value.add(firstCard(h));
			value = value.add(secondCard(h).divide(new BigDecimal("100")));
			value = value.add(thirdCard(h).divide(new BigDecimal("10000")));
			value = value.add(fourthCard(h).divide(new BigDecimal("1000000")));
			value = value.add(fifthCard(h).divide(new BigDecimal("100000000")));
		}
		return value;
	}

	/**
	 * gets FirstCard
	 * 
	 * @param h
	 * @return BigDecimal of card value
	 */
	private static BigDecimal firstCard(Card[] h) {
		int i = h[0].getNum();
		String a = Integer.toString(i);
		BigDecimal value = new BigDecimal(a);
		return value;
	}

	/**
	 * gets SecondCard
	 * 
	 * @param h
	 * @return BigDecimal of card value
	 */
	private static BigDecimal secondCard(Card[] h) {
		int i = h[1].getNum();
		String a = Integer.toString(i);
		BigDecimal value = new BigDecimal(a);
		return value;
	}

	/**
	 * gets ThirdCard
	 * 
	 * @param h
	 * @return BigDecimal of card value
	 */
	private static BigDecimal thirdCard(Card[] h) {
		int i = h[2].getNum();
		String a = Integer.toString(i);
		BigDecimal value = new BigDecimal(a);
		return value;
	}

	/**
	 * gets FourthCard
	 * 
	 * @param h
	 * @return BigDecimal of card value
	 */
	private static BigDecimal fourthCard(Card[] h) {
		int i = h[3].getNum();
		String a = Integer.toString(i);
		BigDecimal value = new BigDecimal(a);
		return value;
	}

	/**
	 * gets FifthCard
	 * 
	 * @param h
	 * @return BigDecimal of card value
	 */
	private static BigDecimal fifthCard(Card[] h) {
		int i = h[4].getNum();
		String a = Integer.toString(i);
		BigDecimal value = new BigDecimal(a);
		return value;
	}

	public static void main(String[] args) {
		Card[] a = new Card[5];
		a[0] = new Card(10, Suit.SPADE);
		a[1] = new Card(7, Suit.HEART);
		a[2] = new Card(4, Suit.HEART);
		a[3] = new Card(4, Suit.SPADE);
		a[4] = new Card(4, Suit.DIAMOND);

		Card[] b = new Card[5];
		b[0] = new Card(10, Suit.CLUB);
		b[1] = new Card(7, Suit.CLUB);
		b[2] = new Card(4, Suit.CLUB);
		b[3] = new Card(4, Suit.CLUB);
		b[4] = new Card(3, Suit.CLUB);
		System.out.println(isThreeOfAKind(a));
		System.out.println(valueOfHand(a));
		System.out.println(valueOfHand(b));
		System.out.println(compareHand(b, a));
	}

}