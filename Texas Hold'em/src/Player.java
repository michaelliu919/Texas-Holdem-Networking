//player class holds info of the player and actions a player can make in a game of texas hold'em
public class Player {

	private String name;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	private int totalChip;
	private int playerPot;
	private Card[] hand = new Card[2];
	private boolean inGame = true;
	private boolean playerTurn = true;
	private int moves = 0;

	/*
	 * Constructor
	 * 
	 * @parameters: name of player, starting ammount of chips
	 */
	public Player(String name, int totalChip) {
		this.name = name;
		this.totalChip = totalChip;
		playerPot = 0;
	}

	/*
	 * @Method: call
	 * 
	 * @raises to the bet
	 * 
	 * @parameters: other player, game
	 * 
	 * @returns: false when player don't have enough chips to bet(it should
	 * never need to do this)
	 */
	public boolean call(Player player) {
		totalChip -= player.getPlayerPot() - playerPot;
		playerPot += player.getPlayerPot() - playerPot;
		setPlayerTurn(false);
		if (moves == 0 && player.getMoves() == 0) {
			player.setPlayerTurn(true);
		}
		moves++;
		return true;
	}

	/*
	 * Method: reset
	 * 
	 * @moves the player to the next game, reseting bets and hand
	 * 
	 * @Parameters: boolean of win, game
	 * 
	 * @return:
	 */
	public void reset(boolean won, Game game) {
		resetHand();
		moves = 0;
		inGame = true;
		if (won) {
			totalChip += game.getPlayer1().getPlayerPot() + game.getPlayer2().getPlayerPot();
		}
		playerPot = 0;
	}

	// invalidates player from game
	public void fold() {
		setInGame(false);
	}

	/*
	 * Method: bet and raise
	 * 
	 * @they are identical methods, however two methods were made so that poker
	 * terminology would be correct
	 * 
	 * @increases pot, upper limit is the other player's total chips,
	 * 
	 * @Parameters: chip raised, second player chips
	 * 
	 * @Return:returns false when chip betted greater than the other player's
	 * chips
	 */
	public boolean bet(int chip, Player player) {
		if (chip > player.getTotalChip() || totalChip - chip < 0 || chip <= player.playerPot) {
			return false;
		}
		totalChip -= chip;
		playerPot += chip;
		playerTurn = false;
		player.setPlayerTurn(true);
		moves++;
		return true;
	}

	public boolean raise(int chip, Player player) {
		if (chip + playerPot > player.getTotalChip() + player.getPlayerPot() || totalChip - chip < 0
				|| playerPot + chip <= player.playerPot) {
			return false;
		}
		if (playerPot + chip == player.playerPot) {
			return call(player);
		} else {
			totalChip -= chip;
			playerPot += chip;
			playerTurn = false;
			setPlayerTurn(false);
			player.setPlayerTurn(true);
			moves++;
			return true;
		}
	}

	/*
	 * @Method: deal
	 * 
	 * @deals a card to player hand
	 * 
	 * @parameters: card being dealt
	 * 
	 * @return: true if card has been dealt
	 */
	public boolean deal(Card card) {
		int i = 0;
		while (i < hand.length && hand[i] != null) {
			i++;
		}
		if (i < hand.length) {
			hand[i] = card;
			return true;
		} else {
			return false;

		}
	}

	/*
	 * @Method: check
	 * 
	 * @gets next card to be put on table
	 * 
	 * @parameters: other player
	 * 
	 * @return:
	 */
	public void check(Player player) {
		setPlayerTurn(false);
		if (moves == 0 && player.getMoves() == 0) {
			player.setPlayerTurn(true);
		}
		moves++;
	}

	/*
	 * @Method: resetHand
	 * 
	 * reset hand of player
	 */
	public void resetHand() {
		hand = new Card[2];
	}

	// toString
	public String toString() {
		String str = "";
		str += name + "\nChips: " + totalChip + "\n";
		for (int i = 0; i < hand.length; i++)
			str += "Card " + (i + 1) + ". " + hand[i] + "|\n";
		str += "Bet: " + playerPot + "\n";
		return str;
	}

	public static void main(String[] args) {
		Player p = new Player("a", 10);
		Deck deck = new Deck();
		p.deal(deck.deal());
		p.deal(deck.deal());
		System.out.println(p);
	}

	/**
	 * 
	 * @return inGame
	 */
	public boolean isInGame() {
		return inGame;
	}

	/**
	 * 
	 * @param inGame
	 */
	public void setInGame(boolean inGame) {
		this.inGame = inGame;
	}

	/**
	 * 
	 * @return playerTurn
	 */
	public boolean isPlayerTurn() {
		return playerTurn;
	}

	/**
	 * 
	 * @param playerTurn
	 */
	public void setPlayerTurn(boolean playerTurn) {
		this.playerTurn = playerTurn;
	}

	/**
	 * 
	 * @return hand
	 */
	public Card[] getHand() {
		return hand;
	}

	/**
	 * 
	 * @return playerPot
	 */
	public int getPlayerPot() {
		return playerPot;
	}

	/**
	 * 
	 * @return totalChip
	 */
	public int getTotalChip() {
		return totalChip;
	}

	/**
	 * @param moves
	 *            the moves to set
	 */
	public void setMoves(int moves) {
		this.moves = moves;
	}

	/**
	 * @return the moves
	 */
	public int getMoves() {
		return moves;
	}

}