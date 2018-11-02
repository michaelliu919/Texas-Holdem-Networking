//Game class, has functionalities of the game so that it runs
public class Game {
	private Player player1, player2;
	private Boolean dealtToPlayers = false;

	/**
	 * @return the player1
	 */
	public Player getPlayer1() {
		return player1;
	}

	/**
	 * @return the player2
	 */
	public Player getPlayer2() {
		return player2;
	}

	private Deck deck;
	private int rounds;
	// keep tracks of raiseting rounds
	private int roundInGame = 0;
	private Card[] tableCards = new Card[5];

	/*
	 * @Constructor
	 * 
	 * @Strings of both players names and starting chip amount
	 */
	public Game(String playerOneName, String playerTwoName, int totalChips) {
		// TODO Auto-generated constructor stub
		player1 = new Player(playerOneName, totalChips);
		player2 = new Player(playerTwoName, totalChips);
		player2.setPlayerTurn(false);
		deck = new Deck();
		rounds = 0;

	}

	/*
	 * @Method: dealToPlayers
	 * 
	 * @Deals two cards to each players' hand and set the blind
	 * 
	 * @delegates to the blind method
	 * 
	 * @parameters:
	 * 
	 * @return:
	 */
	public void dealToPlayers() {
		if (!dealtToPlayers) {
			blind();
			player1.deal(deck.deal());
			player2.deal(deck.deal());
			player1.deal(deck.deal());
			player2.deal(deck.deal());
			roundInGame++;
			dealtToPlayers = true;
		}
	}

	/*
	 * @Method: blind
	 * 
	 * @Sets big/small blind for both players (small blind gets first move) *
	 * 
	 * @parameters:
	 * 
	 * @return:
	 */
	private void blind() {
		int bigBlind = 100;
		int smallBlind = 50;
		if (player1.getTotalChip() >= 4) {
			if (rounds % 2 == 0) {
				player2.raise(smallBlind, player1);
				player1.raise(bigBlind, player2);
				player2.setPlayerTurn(true);
				player1.setPlayerTurn(false);
			} else if (rounds % 2 == 1) {
				player1.raise(smallBlind, player2);
				player2.raise(bigBlind, player1);
				player1.setPlayerTurn(true);
				player2.setPlayerTurn(false);
			}
		}
	}

	/*
	 * @method: deal
	 * 
	 * @deals one card to the table
	 * 
	 * @parameters:
	 * 
	 * @return:
	 */
	public void deal() {
		if (checkPots()) {
			int i = 0;
			while (tableCards[i] != null && i < tableCards.length) {
				i++;
			}
			tableCards[i] = deck.deal();
		}
		roundInGame++;
		player1.setMoves(0);
		player2.setMoves(0);
		if (rounds % 2 == 0) {
			player2.setPlayerTurn(true);
		} else if (rounds % 2 == 1) {
			player1.setPlayerTurn(true);
		}
	}

	/*
	 * @method: flop
	 * 
	 * @deals three cards to table
	 * 
	 * @parameters:
	 * 
	 * @return:
	 * 
	 */
	public void flop() {
		if (checkPots()) {
			for (int i = 0; i < 3; i++) {
				tableCards[i] = deck.deal();
			}
		}
		roundInGame++;
		player1.setMoves(0);
		player2.setMoves(0);
		if (rounds % 2 == 0) {
			player2.setPlayerTurn(true);
		} else if (rounds % 2 == 1) {
			player1.setPlayerTurn(true);
		}
	}

	/*
	 * @method: checkPots
	 * 
	 * @checks if player pots are both equal
	 * 
	 * @parameters:
	 * 
	 * @return: true if they are, false if not
	 * 
	 */
	public boolean checkPots() {
		if (player1.getPlayerPot() == player2.getPlayerPot()) {
			return true;
		}
		return false;
	}

	/**
	 * Important - needs to be fixed along with reset in player class
	 *
	 */
	/*
	 * @method: nextRound
	 * 
	 * @moves on to the next Round, 1 if player 1 win, 2 if player2 win
	 * 
	 * @parameters: player who wins
	 * 
	 * @return:
	 */
	public void nextRound(int playerWhoWon) {
		if (playerWhoWon == 1) {
			player1.reset(true, this);
			player2.reset(false, this);
		} else if (playerWhoWon == 2) {
			player2.reset(true, this);
			player1.reset(false, this);
		} else {
			player2.reset(false, this);
			player1.reset(false, this);
		}
		deck.reset();
		deck.shuffle();
		tableCards = new Card[5];
		rounds++;
		dealtToPlayers = false;
		dealToPlayers();
		roundInGame = 0;
	}

	/*
	 * @method: compareHands
	 * 
	 * @finds who has the better hand
	 * 
	 * @parameters: both Players in game
	 * 
	 * @return: 1 if player1 win, 2 if player2 win
	 */
	public int compareHands(Player player1, Player player2) {
		if (Hands.compareHand(highestHand(player1), highestHand(player2)) == 1)
			return 1;
		else if (Hands.compareHand(highestHand(player1), highestHand(player2)) == 2) {
			return 2;
		} else
			return 0;
	}

	/*
	 * @private Method: highestHand
	 * 
	 * @finds highestHand out of all possible combinations
	 * 
	 * @parameters: Player
	 * 
	 * @return: best hand of player
	 */
	public Card[] highestHand(Player player) {
		Card[][] a = combination(player);
		int bestHand = 0;
		for (int i = 0; i < a.length; i++) {
			if (Hands.compareHand(a[bestHand], a[i]) == 2) {
				bestHand = i;
			}
		}
		return a[bestHand];
	}

	/*
	 * @private Method: combination
	 * 
	 * @return all 5 card combination
	 * 
	 * @delegates to combineArrays and sortByValue
	 * 
	 * @parameter: Player
	 * 
	 * @return: 2d array of all combinations
	 */
	private Card[][] combination(Player player) {
		int cardsSelected = 0;
		int hand = 0;
		Card[] allCards = combineArrays(player);
		Card[][] allHands = new Card[21][5];
		for (int firstCard = 0; firstCard < 7; firstCard++) {
			for (int secondCard = firstCard + 1; secondCard < 7; secondCard++) {
				for (int i = 0; i < 7; i++) {
					if (i != firstCard && i != secondCard) {
						allHands[hand][cardsSelected++] = allCards[i];
					}
				}
				sortByValue(allHands[hand]);
				cardsSelected = 0;
				hand++;
			}
		}
		return allHands;
	}

	/*
	 * @ private Method: sort by Value
	 * 
	 * @selection sort 5 cards
	 * 
	 * @parameters: array of 5 cards
	 * 
	 * @return: sorted array
	 */
	private void sortByValue(Card[] a) {
		for (int i = 0; i < a.length; i++) {
			Card min = a[i];
			int index = i;
			for (int j = i + 1; j < a.length; j++) {
				if (a[j].getNum() < min.getNum()) {
					min = a[j];
					index = j;
				}
			}
			a[index] = a[i];
			a[i] = min;
		}
	}

	/*
	 * @ private Method: combineArrays
	 * 
	 * @combines the Cards on table and in players hands
	 * 
	 * @parameters: Player who's hand is being combined with table
	 * 
	 * @return: the combined array
	 */
	private Card[] combineArrays(Player player) {
		Card[] table = new Card[7];
		table[0] = player.getHand()[0];
		table[1] = player.getHand()[1];
		for (int i = 0; i < 5; i++) {
			table[i + 2] = tableCards[i];
		}
		return table;
	}

	// toString for all the combination of hands
	private static String printAllHands(Card[][] a) {
		String str = "";
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[i].length; j++) {
				str += a[i][j].toString() + " | ";
			}
			str += "\n";
		}
		return str;
	}

	// toString for all the elements in the game
	public String toString() {
		String str = "";
		str += player1.toString();
		str += player2.toString();
		str += "--------------\n";
		for (int i = 0; i < tableCards.length; i++) {
			if (tableCards[i] != null) {
				str += "|" + tableCards[i].toString() + "\n	";
			} else {
				str += "|empty\n	";
			}
		}
		return str;
	}

	/**
	 * @return the roundInGame
	 */
	public int getRoundInGame() {
		return roundInGame;
	}

	/**
	 * @param roundInGame
	 *            the roundInGame to set
	 */
	public void setRoundInGame(int roundInGame) {
		this.roundInGame = roundInGame;
	}

	/**
	 * @return the tableCards
	 */
	public Card[] getTableCards() {
		return tableCards;
	}

	/**
	 * @param tableCards
	 *            the tableCards to set
	 */
	public void setTableCards(Card[] tableCards) {
		this.tableCards = tableCards;
	}

	public static void main(String[] args) {
		Game table = new Game("player1", "player2", 10000000);
		table.dealToPlayers();
		table.player2.call(table.player1);
		table.flop();
		table.deal();
		table.deal();
		System.out.println(table);
		Card[] a = table.combineArrays(table.player1);
		for (int i = 0; i < a.length; i++) {
			System.out.println(a[i]);
		}
		System.out.println(printAllHands(table.combination(table.player1)));

	}

}