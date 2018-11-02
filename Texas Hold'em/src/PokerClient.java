import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class PokerClient {

	BufferedReader in;
	PrintWriter out;
	String playerName = "Poker";
	JFrame frame = new JFrame(playerName);

	JPanel playerHandPanel, controlPanel, bottomPanel, topPanel, messageBoard;
	JButton check, fold, playNextRound;
	JTextField textField = new JTextField(10);
	JLabel firstLine = new JLabel("");
	JLabel secondLine = new JLabel("");
	JLabel stats = new JLabel("");
	JFrame endFrame = new JFrame("");



	String serverAddress = "localhost";

	Card playerHand1, playerHand2;

	public PokerClient() {

		// Layout GUI
		frame.setSize(1100, 700);
		frame.setResizable(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		playerHandPanel = new JPanel(new GridLayout(1, 0));
		playerHandPanel.setPreferredSize(new Dimension(600, 300));
		playerHandPanel.setVisible(true);

		topPanel = new JPanel(new GridLayout(1, 0));
		topPanel.setPreferredSize(new Dimension(900, 300));
		topPanel.setVisible(true);

		messageBoard = new JPanel(new GridLayout(0, 1));
		messageBoard.setPreferredSize(new Dimension(200, 300));
		messageBoard.add(firstLine);
		messageBoard.add(secondLine);
		messageBoard.add(stats);

		controlPanel = new JPanel();
		controlPanel.setPreferredSize(new Dimension(600, 40));
		controlPanel.setVisible(true);

		topPanel.add(messageBoard);

		check = new JButton("Check/Call");
		check.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				out.println(playerName + "CHECK");
			}
		});
		
		playNextRound = new JButton("Next Round");
		playNextRound.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				out.println(playerName + "PLAY");
			}
		});

		fold = new JButton("fold");
		fold.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				out.println(playerName + "FOLD");
			}
		});

		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isInt(textField.getText())) {
					out.println(playerName + "RAISE" + textField.getText());
					textField.setText("");
				} else {
					JFrame frame = new JFrame("Not a number");
					frame.setSize(600, 70);
					frame.setLocation(400, 250);
					frame.setResizable(false);
					JLabel label = new JLabel("You must enter a number in order to raise");
					frame.add(label);
					frame.setVisible(true);
					frame.toFront();
					frame.repaint();
					textField.setText("");
				}
			}
		});

		controlPanel.add(check);
		controlPanel.add(fold);
		controlPanel.add(new JLabel(" Raise:"));
		controlPanel.add(textField);

		bottomPanel = new JPanel();
		bottomPanel.add(controlPanel, BorderLayout.SOUTH);
		bottomPanel.add(playerHandPanel, BorderLayout.NORTH);

		frame.add(topPanel, BorderLayout.NORTH);
		frame.add(bottomPanel, BorderLayout.SOUTH);
		// frame.add(firstLine, "SOUTH");
		frame.setVisible(true);
	}

	private static boolean isInt(String s) {
		try {
			int i = Integer.parseInt(s);
			return true;
		}

		catch (NumberFormatException er) {
			return false;
		}
	}

	/**
	 * Prompt for and return the desired screen name.
	 */
	private String getName() {

		return JOptionPane.showInputDialog(frame, "Choose a screen name:", "Screen name selection",
				JOptionPane.PLAIN_MESSAGE);
	}

	private Card constructCard(String line) {
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
		System.out.println(card.toString());
		return card;
	}

	private Card[] readHand(String str, Card[] cards){
		System.out.println(str);
		if(str.length()<2){
			return cards;
		}
		int split = str.indexOf(",");
		for(int i = 0; i<5; i++){
			if(cards[i]==null){
				cards[i] = constructCard(str.substring(0, split));
				break;
			}
		}
		return readHand(str.substring(split+1), cards);
	}
	
	/**
	 * Connects to the server then enters the processing loop.
	 */
	private void run() throws IOException {

		Socket socket = new Socket(serverAddress, 9020);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);

		// Process all messages from server, according to the protocol.
		while (true) {
			String line = in.readLine();
			System.out.println(line);
			if (line != null) {
				if(line.startsWith("Next Round")){
					bottomPanel.removeAll();
					bottomPanel.add(controlPanel, BorderLayout.SOUTH);
					bottomPanel.add(playerHandPanel, BorderLayout.NORTH);
					bottomPanel.revalidate();
					topPanel.removeAll();
					topPanel.add(messageBoard);
					topPanel.revalidate();
				}
				
				if (line.startsWith(playerName + "PLAYERTURN")) {
					firstLine.setText("YOUR TURN");
				}
				else if (line.startsWith("OPPONENT'S TURN")) {
					firstLine.setText(line.substring(15));
					secondLine.setText("");
				}
				
				if(line.startsWith("Stats")){
					stats.setText("<html>"+line.substring(5)+"<html>");
				}
				
				if(line.startsWith("OPPONENT ACTION")){
					secondLine.setText(line.substring(15));
				}
				
				if(line.startsWith("WINS")){
					JFrame victory = new JFrame(playerName+": You win this round! You take the pot!");
					victory.setLayout(new GridLayout(1,0));
					JLabel label = new JLabel("Opponent's best hand");
					Card[] opponentHand = new Card[5];
					opponentHand = readHand(line.substring(4), opponentHand);
					victory.setSize(1000, 400);
					victory.setLocation(400, 250);
					victory.setResizable(false);
					victory.add(label);
					for(int i=0; i<5; i++){
						victory.add(new CardComponent(opponentHand[i]));
					}
					victory.add(playNextRound);
					victory.setVisible(true);
					victory.toFront();
					victory.repaint();

				}
				
				if(line.startsWith("LOSES")){
					JFrame lose = new JFrame(playerName+": You lose this round!");
					lose.setLayout(new GridLayout(1,0));
					JLabel label = new JLabel("Opponent's best hand");
					Card[] opponentHand = new Card[5];
					opponentHand = readHand(line.substring(5), opponentHand);
					lose.setSize(1000, 400);
					lose.setLocation(400, 250);
					lose.setResizable(false);
					lose.add(label);
					for(int i=0; i<5; i++){
						lose.add(new CardComponent(opponentHand[i]));
					}
					lose.add(playNextRound);
					lose.setVisible(true);
					lose.toFront();
					lose.repaint();
				}
				if(line.startsWith(playerName+"Waiting")){
					firstLine.setText(line.substring(playerName.length()));
				}
				if (line.startsWith("SUBMITNAME")) {
					String name = getName();
					playerName = name;
					out.println(name);
					frame.setTitle(playerName);
				}  else if (line.startsWith(playerName + "Raised amount illegal")) {
					JFrame frame = new JFrame("Illegal amount");
					frame.setSize(600, 70);
					frame.setLocation(400, 250);
					frame.setResizable(false);
					JLabel label = new JLabel("Raised amount illegal");
					frame.add(label);
					frame.setVisible(true);
					frame.toFront();
					frame.repaint();
				} else if (line.startsWith(playerName + "CARD")) {
					firstLine.setText("");
					playerHandPanel.add(new CardComponent(constructCard(line.substring(playerName.length() + 4))));
					playerHandPanel.revalidate();
					playerHandPanel.repaint();
				} else if (line.startsWith("CARD")) {
					topPanel.add(new CardComponent(constructCard(line.substring(4))));
					System.out.println("this is the card constructed " + constructCard(line.substring(4)));
					topPanel.revalidate();
					topPanel.repaint();
				} else if (line.startsWith("MESSAGE")) {
					firstLine.setText(line.substring(7));
				}
			}
		}
	}

	public static void main(String[] args) throws Exception {
		PokerClient client = new PokerClient();
		client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		client.frame.setVisible(true);
		client.run();
	}
}