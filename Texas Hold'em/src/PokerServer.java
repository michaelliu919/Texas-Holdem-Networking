import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class PokerServer {

	/**
	 * The port that the server listens on.
	 */
	private static final int PORT = 9020;
	// keeps track of all the names
	private static ArrayList<String> names = new ArrayList<String>();
	// broadcast messages
	private static ArrayList<PrintWriter> writers = new ArrayList<PrintWriter>();
	static Game game = new Game("p1", "p2", 1000);
	static Boolean flop = true;
	static Boolean turn = true;
	static Boolean river = true;
	static Boolean playerCardsDealt = false;
	static Boolean p1NextRound = false;
	static Boolean p2NextRound = false;

	/**
	 * The appplication main method, which just listens on a port and spawns
	 * handler threads.
	 */
	public static void main(String[] args) throws Exception {

		System.out.println("The poker server is running.");
		ServerSocket listener = new ServerSocket(PORT);
		try {
			while (true) {
				new GameThread(listener.accept()).start();
			}
		} finally {
			listener.close();
		}
	}

	private static class GameThread extends Thread {
		private String name;
		private Socket socket;
		private BufferedReader in;
		private PrintWriter out;

		public GameThread(Socket socket) {
			this.socket = socket;
		}

		private void progress() {
			if (game.getPlayer1().isPlayerTurn() == false && game.getPlayer2().isPlayerTurn() == false) {
				if (game.getRoundInGame() == 1) {
					game.flop();
					writers.get(0).println("CARD" + game.getTableCards()[0].transferStr());
					writers.get(0).println("CARD" + game.getTableCards()[1].transferStr());
					writers.get(0).println("CARD" + game.getTableCards()[2].transferStr());
					writers.get(1).println("CARD" + game.getTableCards()[0].transferStr());
					writers.get(1).println("CARD" + game.getTableCards()[1].transferStr());
					writers.get(1).println("CARD" + game.getTableCards()[2].transferStr());
				} else if (game.getRoundInGame() == 2) {
					game.deal();
					writers.get(0).println("CARD" + game.getTableCards()[3].transferStr());
					writers.get(1).println("CARD" + game.getTableCards()[3].transferStr());

				} else if (game.getRoundInGame() == 3) {
					game.deal();
					writers.get(0).println("CARD" + game.getTableCards()[4].transferStr());
					writers.get(1).println("CARD" + game.getTableCards()[4].transferStr());

				} else if (game.getRoundInGame() == 4) {
					Card[] player1BestHand = game.highestHand(game.getPlayer1());
					Card[] player2BestHand = game.highestHand(game.getPlayer2());
					String player1Hand = "";
					String player2Hand = "";
					for (int i = 0; i < 5; i++) {
						player1Hand += player1BestHand[i].transferStr() + ",";
						player2Hand += player2BestHand[i].transferStr() + ",";
					}
					if (game.compareHands(game.getPlayer1(), game.getPlayer2()) == 1) {
						writers.get(0).println("WINS" + player2Hand);
						writers.get(1).println("LOSES" + player1Hand);
					} else {
						writers.get(0).println("LOSES" + player2Hand);
						writers.get(1).println("WINS" + player1Hand);
					}
				}
			}
		}

		public void notifyPlayerTurn() {
			if (game.getPlayer1().isPlayerTurn()) {
				writers.get(0).println(names.get(0) + "PLAYERTURN");
				writers.get(1).println("OPPONENT'S TURN" + names.get(0) + "'s Turn");
			} else if (game.getPlayer2().isPlayerTurn()) {
				writers.get(1).println(names.get(1) + "PLAYERTURN");
				writers.get(0).println("OPPONENT'S TURN" + names.get(1) + "'s Turn");

			} else {
				writers.get(1).println("Neither player turn");
				writers.get(0).println("Neither player turn");
			}
		}

		public void updateStats() {
			writers.get(0)
					.println("Stats" + "Pot: " + (game.getPlayer1().getPlayerPot() + game.getPlayer2().getPlayerPot())
							+ "<br>" + names.get(0) + "'s chips: " + game.getPlayer1().getTotalChip() + "<br>"
							+ names.get(1) + "'s chips: " + game.getPlayer2().getTotalChip() + "<br>" + names.get(0)
							+ "'s bet: " + game.getPlayer1().getPlayerPot() + "<br>" + names.get(1) + "'s bet: "
							+ game.getPlayer2().getPlayerPot());
			writers.get(1)
					.println("Stats" + "Pot: " + (game.getPlayer1().getPlayerPot() + game.getPlayer2().getPlayerPot())
							+ "<br>" + names.get(0) + "'s chips: " + game.getPlayer1().getTotalChip() + "<br>"
							+ names.get(1) + "'s chips: " + game.getPlayer2().getTotalChip() + "<br>" + names.get(0)
							+ "'s bet: " + game.getPlayer1().getPlayerPot() + "<br>" + names.get(1) + "'s bet: "
							+ game.getPlayer2().getPlayerPot());
		}

		public void ifNextRound() {
			if (p1NextRound == true && p2NextRound == true) {
				if (game.compareHands(game.getPlayer1(), game.getPlayer2()) == 1) {
					game.nextRound(1);
					writers.get(0).println("Next Round");
					writers.get(1).println("Next Round");
					p1NextRound = false;
					p2NextRound = false;
					writers.get(0).println(names.get(0) + "CARD" + game.getPlayer1().getHand()[0].transferStr());
					writers.get(0).println(names.get(0) + "CARD" + game.getPlayer1().getHand()[1].transferStr());
					writers.get(1).println(names.get(1) + "CARD" + game.getPlayer2().getHand()[0].transferStr());
					writers.get(1).println(names.get(1) + "CARD" + game.getPlayer2().getHand()[1].transferStr());

				} else {
					game.nextRound(2);
					writers.get(0).println("Next Round");
					writers.get(1).println("Next Round");
					p1NextRound = false;
					p2NextRound = false;
					writers.get(0).println(names.get(0) + "CARD" + game.getPlayer1().getHand()[0].transferStr());
					writers.get(0).println(names.get(0) + "CARD" + game.getPlayer1().getHand()[1].transferStr());
					writers.get(1).println(names.get(1) + "CARD" + game.getPlayer2().getHand()[0].transferStr());
					writers.get(1).println(names.get(1) + "CARD" + game.getPlayer2().getHand()[1].transferStr());
				}
				notifyPlayerTurn();

				updateStats();

			} else {
				writers.get(0).println(names.get(0) + "Waiting for opponent");
				writers.get(1).println(names.get(1) + "Waiting for opponent");

			}
		}

		/**
		 * Services this thread's client by repeatedly requesting a screen name
		 * until a unique one has been submitted, then acknowledges the name and
		 * registers the output stream for the client in a global set, then
		 * repeatedly gets inputs and broadcasts them.
		 */
		public void run() {
			try {
				// Create character streams for the socket.
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);
				// get the name of the player
				out.println("SUBMITNAME");
				name = in.readLine();
				names.add(name);
				writers.add(out);
				while (names.size() < 2) {
					out.println("MESSAGE waiting for opponent to connect");
				}

				// Now that a successful name has been chosen, add the
				// socket's print writer to the set of all writers so
				// this client can receive broadcast messages.
				// out.println("NAMEACCEPTED");

				if (!playerCardsDealt) {
					game.dealToPlayers();
					playerCardsDealt = true;
				}

				out.println(names.get(0) + "CARD" + game.getPlayer1().getHand()[0].transferStr());
				out.println(names.get(0) + "CARD" + game.getPlayer1().getHand()[1].transferStr());
				out.println(names.get(1) + "CARD" + game.getPlayer2().getHand()[0].transferStr());
				out.println(names.get(1) + "CARD" + game.getPlayer2().getHand()[1].transferStr());

				notifyPlayerTurn();

				updateStats();

				while (true) {
					String input = in.readLine();
					notifyPlayerTurn();
					updateStats();
					if (input == null) {
						return;
					} else if (input.startsWith(names.get(0))) {
						if (input.contains("PLAY")) {
							p1NextRound = true;
							ifNextRound();
						}
						if (game.getPlayer1().isPlayerTurn()) {
							if (input.contains("CHECK")) {
								if (game.getPlayer2().getPlayerPot() > game.getPlayer1().getPlayerPot()) {
									game.getPlayer1().call(game.getPlayer2());
									writers.get(1).println("OPPONENT ACTIONOpponent Called");
								} else {
									game.getPlayer1().check(game.getPlayer2());
									writers.get(1).println("OPPONENT ACTIONOpponent Checked");
								}
								progress();
							} else if (input.contains("FOLD")) {
								game.getPlayer1().fold();
								writers.get(1).println("OPPONENT ACTIONOpponent Folded");
								progress();
							} else if (input.contains("RAISE")) {
								int amount = Integer.parseInt(input.substring(names.get(0).length() + 5));
								if (!game.getPlayer1().raise(amount, game.getPlayer2())) {
									writers.get(0).println(names.get(0) + "Raised amount illegal");
								} else {
									writers.get(1).println("OPPONENT ACTION" + names.get(0) + " Raised " + amount);
								}
							}
							updateStats();
							writers.get(0).println(names.get(0) + "Waiting for opponent");
						} else {
							if (game.getPlayer2().isPlayerTurn()) {
								writers.get(0).println(names.get(0) + "Waiting for opponent");
							}
						}
					} else if (input.startsWith(names.get(1))) {
						if (input.contains("PLAY")) {
							p2NextRound = true;
							ifNextRound();
						}
						if (game.getPlayer2().isPlayerTurn()) {
							if (input.contains("CHECK")) {
								if (game.getPlayer1().getPlayerPot() > game.getPlayer2().getPlayerPot()) {
									game.getPlayer2().call(game.getPlayer1());
									writers.get(0).println("OPPONENT ACTIONOpponent Called");
								} else {
									game.getPlayer2().check(game.getPlayer1());
									writers.get(0).println("OPPONENT ACTIONOpponent Checked");
								}
								progress();
							} else if (input.contains("FOLD")) {
								game.getPlayer2().fold();
								writers.get(0).println("OPPONENT ACTIONOpponent Folded");
								progress();
							} else if (input.contains("RAISE")) {
								int amount = Integer.parseInt(input.substring(names.get(1).length() + 5));
								if (!game.getPlayer2().raise(amount, game.getPlayer1())) {
									out.println(names.get(1) + "Raised amount illegal");
								} else {
									writers.get(0).println("OPPONENT ACTION" + names.get(1) + " Raised " + amount);
								}
							}
							updateStats();
							writers.get(1).println(names.get(1) + "Waiting for opponent");
						} else {
							if (game.getPlayer1().isPlayerTurn()) {
								writers.get(1).println(names.get(1) + "Waiting for opponent");
							}
						}
					}
					notifyPlayerTurn();
				}
			} catch (IOException e) {
				System.out.println(e);
			} finally {
				// This client is going down! Remove its name and its print
				// writer from the sets, and close its socket.
				if (name != null) {
					names.remove(name);
				}
				if (out != null) {
					writers.remove(out);
				}
				try {
					socket.close();
				} catch (IOException e) {
				}
			}

		}

	}

}
