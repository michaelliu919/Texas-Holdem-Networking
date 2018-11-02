import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
//displays the game, for the purpose of testing and demonstrating that the game works
public class GameDisplay extends JComponent {
//data field
	private JPanel tablePanel, playerDisplay, player1Display, player2Display, playerOptions, playerView, stats;
	private JButton check, fold, raiseButton, switchPlayerView;
	private JLabel turn, tablePot, player1Chips, player2Chips, player1bet, player2bet;

	private Game game;
	private String player1Name, player2Name;
	JFrame frame = new JFrame("Poker");
	private JSlider raise;

	CardLayout c1 = new CardLayout();

	//constructor
	public GameDisplay() {

	}

	//initiates the start up frames for the game
	public void startUpFrames() {

		JFrame player1NameFrame = new JFrame("player 1 Name");
		player1NameFrame.setLayout(new GridLayout(1, 2));
		player1NameFrame.setSize(225, 70);
		player1NameFrame.setLocation(400, 250);
		player1NameFrame.setResizable(false);
		JTextField textField = new JTextField(20);
		player1NameFrame.add(textField);
		JButton submit = new JButton("Submit");
		submit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				player1Name = textField.getText();
				player1NameFrame.dispose();
			}
		});
		player1NameFrame.add(submit);

		JFrame player2NameFrame = new JFrame("player 2 Name");
		player2NameFrame.setLayout(new GridLayout(1, 2));
		player2NameFrame.setSize(225, 70);
		player2NameFrame.setLocation(400, 250);
		player2NameFrame.setResizable(false);
		JTextField textField2 = new JTextField(20);
		player2NameFrame.add(textField2);
		JButton submit2 = new JButton("Submit");
		player2NameFrame.add(submit2);
		submit2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				player2Name = textField2.getText();
				player2NameFrame.dispose();
				game = new Game(player1Name, player2Name, 1000);
				GUI(game);

			}
		});

		player2NameFrame.setVisible(true);
		player1NameFrame.setVisible(true);

	}

	//graphics user interface
	public void GUI(Game game) {

		frame.setSize(1100, 700);
		frame.setResizable(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		tablePanel = new JPanel(new GridLayout(1, 6));
		tablePanel.setVisible(true);
		tablePanel.setPreferredSize(new Dimension(900, 300));
		player1Display = new JPanel(new GridLayout(1, 2));
		player1Display.setVisible(true);
		player1Display.setPreferredSize(new Dimension(600, 300));
		player2Display = new JPanel(new GridLayout(1, 2));
		player2Display.setVisible(true);
		player2Display.setPreferredSize(new Dimension(600, 300));

		game.dealToPlayers();
		CardComponent player1Card1 = new CardComponent(game.getPlayer1().getHand()[0]);
		CardComponent player1Card2 = new CardComponent(game.getPlayer1().getHand()[1]);
		CardComponent player2Card1 = new CardComponent(game.getPlayer2().getHand()[0]);
		CardComponent player2Card2 = new CardComponent(game.getPlayer2().getHand()[1]);

		// player1Display.add(stats);
		player1Display.add(player1Card1);
		player1Display.add(player1Card2);
		// player2Display.add(stats);
		player2Display.add(player2Card1);
		player2Display.add(player2Card2);

		playerDisplay = new JPanel(c1);
		playerDisplay.add(player1Display, "1");
		playerDisplay.add(player2Display, "2");
		
		stats = new JPanel(new GridLayout(6, 1));
		if (game.getPlayer2().isPlayerTurn()) {
			System.out.println(game.getPlayer2().getHand()[0]);
			turn = new JLabel(game.getPlayer2().getName() + "'s" + " Turn");
		} else {
			turn = new JLabel(game.getPlayer1().getName() + "'s" + " Turn");
		}
		tablePot = new JLabel("Pot: " + (game.getPlayer1().getPlayerPot() + game.getPlayer2().getPlayerPot()));
		player1Chips = new JLabel(game.getPlayer1().getName() + "'s chips: " + game.getPlayer1().getTotalChip());
		player2Chips = new JLabel(game.getPlayer2().getName() + "'s chips: " + game.getPlayer2().getTotalChip());
		player1bet = new JLabel(game.getPlayer1().getName() + "'s bet: " + game.getPlayer1().getPlayerPot());
		player2bet = new JLabel(game.getPlayer2().getName() + "'s bet: " + game.getPlayer2().getPlayerPot());

		switchPlayerView = new JButton(game.getPlayer2().getName() + "'s cards");
		switchPlayerView.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (switchPlayerView.getText().compareTo(game.getPlayer2().getName() + "'s cards") == 0) {
					c1.show(playerDisplay, "1");
					switchPlayerView.setText(game.getPlayer1().getName() + "'s cards");
				} else {
					c1.show(playerDisplay, "2");
					switchPlayerView.setText(game.getPlayer2().getName() + "'s cards");
				}
			}
		});

		stats.add(turn);
		stats.add(tablePot);
		stats.add(player1Chips);
		stats.add(player1bet);
		stats.add(player2Chips);
		stats.add(player2bet);

		tablePanel.add(stats);

		if (game.getPlayer1().isPlayerTurn() == true) {
			raise = new JSlider(JSlider.HORIZONTAL, 1, game.getPlayer1().getTotalChip(), 10);
		} else {
			raise = new JSlider(JSlider.HORIZONTAL, 1, game.getPlayer2().getTotalChip(), 1);
		}
		raise.setMajorTickSpacing(300);
		raise.setPaintTicks(true);
		raise.setPaintLabels(true);

		raiseButton = new JButton("Raise");
		raiseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int amount = raise.getValue();
				raise.setValue(1);
				if (game.getPlayer1().isPlayerTurn()) {
					if (amount > game.getPlayer2().getTotalChip() + game.getPlayer2().getPlayerPot()) {
						JFrame frame = new JFrame("Raising too much");
						frame.setSize(600, 70);
						frame.setLocation(400, 250);
						frame.setResizable(false);
						JLabel label = new JLabel("You are raising too much, you can only raise amounts smaller than "
								+ game.getPlayer2().getTotalChip());
						frame.add(label);
						frame.setVisible(true);
						frame.toFront();
						frame.repaint();
					} else {
						game.getPlayer1().raise(amount, game.getPlayer2());
					}
				} else {
					if (amount > game.getPlayer1().getTotalChip() + game.getPlayer1().getPlayerPot()) {
						JFrame frame = new JFrame("Raising too much");
						frame.setSize(600, 70);
						frame.setLocation(400, 250);
						frame.setResizable(false);
						JLabel label = new JLabel("You are raising too much, you can only raise amounts smaller than "
								+ game.getPlayer1().getTotalChip());
						frame.add(label);
						frame.setVisible(true);
						frame.toFront();
						frame.repaint();
					} else {
						game.getPlayer2().raise(amount, game.getPlayer1());
					}
				}
				if (game.getPlayer2().isPlayerTurn() == true) {
					c1.show(playerDisplay, "2");
					switchPlayerView.setText(game.getPlayer2().getName() + "'s cards");
				} else {
					c1.show(playerDisplay, "1");
					switchPlayerView.setText(game.getPlayer1().getName() + "'s cards");
				}
				updateStats();

			}
		});

		fold = new JButton("Fold");
		fold.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (game.getPlayer1().isPlayerTurn()) {
					game.getPlayer1().fold();
					game.nextRound(2);
				} else {
					game.getPlayer2().fold();
					game.nextRound(1);
				}
				if (game.getPlayer2().isPlayerTurn() == true) {
					c1.show(playerDisplay, "2");
					switchPlayerView.setText(game.getPlayer2().getName() + "'s cards");
				} else {
					c1.show(playerDisplay, "1");
					switchPlayerView.setText(game.getPlayer1().getName() + "'s cards");
				}
				updateStats();
			}
		});

		check = new JButton("Check/Call");
		check.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (game.getPlayer1().isPlayerTurn()) {
					if (game.getPlayer2().getPlayerPot() > game.getPlayer1().getPlayerPot()) {
						game.getPlayer1().call(game.getPlayer2());
					} else {
						game.getPlayer1().check(game.getPlayer2());
					}
				} else {
					if (game.getPlayer1().getPlayerPot() > game.getPlayer2().getPlayerPot()) {
						game.getPlayer2().call(game.getPlayer1());
					} else {
						game.getPlayer2().check(game.getPlayer1());
					}
				}

				if (game.getPlayer1().isPlayerTurn() == false && game.getPlayer2().isPlayerTurn() == false) {
					if (game.getRoundInGame() == 1) {
						game.flop();
						tablePanel.add(new CardComponent(game.getTableCards()[0]));
						tablePanel.add(new CardComponent(game.getTableCards()[1]));
						tablePanel.add(new CardComponent(game.getTableCards()[2]));
						tablePanel.repaint();
					} else if (game.getRoundInGame() == 2) {
						game.deal();
						tablePanel.add(new CardComponent(game.getTableCards()[3]));
						tablePanel.repaint();

					} else if (game.getRoundInGame() == 3) {
						game.deal();
						tablePanel.add(new CardComponent(game.getTableCards()[4]));
						tablePanel.repaint();
					} else if (game.getRoundInGame() == 4) {
						if (game.compareHands(game.getPlayer1(), game.getPlayer2()) == 1) {
							JFrame frame = new JFrame("Player1 wins this round!");
							frame.setSize(600, 70);
							frame.setLocation(400, 250);
							frame.setResizable(false);
							JLabel label = new JLabel("Player 1 wins this round, player 1 takes the pot");
							frame.add(label);
							frame.setVisible(true);
							frame.setAlwaysOnTop(true);
							frame.toFront();
							frame.repaint();
							game.nextRound(1);
							GUI(game);
						} else {
							JFrame frame = new JFrame("Player2 wins this round!");
							frame.setSize(600, 70);
							frame.setLocation(400, 250);
							frame.setResizable(false);
							JLabel label = new JLabel("Player 2 wins this round, player 2 takes the pot");
							frame.add(label);
							frame.setVisible(true);
							frame.setAlwaysOnTop(true);
							frame.toFront();
							frame.repaint();
							game.nextRound(2);
							GUI(game);

						}
					}
				}
				if (game.getPlayer2().isPlayerTurn() == true) {
					c1.show(playerDisplay, "2");
				} else {
					c1.show(playerDisplay, "1");
				}
				updateStats();
			}
		});

		playerOptions = new JPanel();

		playerOptions.add(switchPlayerView);
		playerOptions.add(raise);
		playerOptions.add(raiseButton);
		playerOptions.add(check);
		playerOptions.add(fold);
		playerOptions.setVisible(true);
		playerOptions.setPreferredSize(new Dimension(900, 50));
		playerView = new JPanel();
		playerView.add(playerOptions, BorderLayout.NORTH);
		playerView.add(playerDisplay, BorderLayout.SOUTH);

		playerView.setVisible(true);

		frame.setVisible(true);
		frame.add(tablePanel, BorderLayout.NORTH);
		frame.add(playerView, BorderLayout.SOUTH);
	}

	//updates the stats text field in game
	public void updateStats() {
		if (game.getPlayer2().isPlayerTurn()) {
			turn.setText(game.getPlayer2().getName() + "'s" + " Turn");
		} else {
			turn.setText(game.getPlayer1().getName() + "'s" + " Turn");
		}
		tablePot.setText("Pot: " + (game.getPlayer1().getPlayerPot() + game.getPlayer2().getPlayerPot()));
		player1Chips.setText(game.getPlayer1().getName() + "'s chips: " + game.getPlayer1().getTotalChip());
		player2Chips.setText(game.getPlayer2().getName() + "'s chips: " + game.getPlayer2().getTotalChip());
		player1bet.setText(game.getPlayer1().getName() + "'s bet: " + game.getPlayer1().getPlayerPot());
		player2bet.setText(game.getPlayer2().getName() + "'s bet: " + game.getPlayer2().getPlayerPot());

	}

	//main runs the game
	public static void main(String[] args) {
		GameDisplay d = new GameDisplay();
		d.startUpFrames();

	}

}
