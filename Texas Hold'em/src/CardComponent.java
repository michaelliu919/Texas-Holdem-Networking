import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 * this class renders an image of the card and it is connected to the nuts and bolts part of the project with methods
 * @author michaelliu
 *
 */
public class CardComponent extends JComponent {
	//data field
	private Card card;
	private BufferedImage myImage;
	private BufferedImage pokerBack;
	private boolean cardFlipped=true;
	
	//constructor
	public CardComponent(Card card){
		String path = "";
		if(card.getSuit().getSuit().compareTo("Spade")==0){
			path = "res/"+card.getNum()+"_"+"of"+"_"+"spades"+".png";
		}
		else if(card.getSuit().getSuit().compareTo("Heart")==0){
			path = "res/"+card.getNum()+"_"+"of"+"_"+"hearts"+".png";
		}else if(card.getSuit().getSuit().compareTo("Club")==0){
			path = "res/"+card.getNum()+"_"+"of"+"_"+"clubs"+".png";
		} else{
			path = "res/"+card.getNum()+"_"+"of"+"_"+"diamonds"+".png";
		}
		
		try {
			myImage = ImageIO.read((new FileInputStream(path)));
		} catch (IOException exp) {
			exp.printStackTrace();
		}
	}
	
	//paints the image
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);	
		if (myImage != null) {
			Graphics2D g2d = (Graphics2D) g;
			AffineTransform trans = new AffineTransform();
			BufferedImage before = myImage;
			int w = before.getWidth();
			int h = before.getHeight();
			BufferedImage after = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			AffineTransform at = new AffineTransform();
			at.scale(0.3, 0.3);
			AffineTransformOp scaleOp = 
			   new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
			after = scaleOp.filter(before, after);
			g2d.drawImage(after, trans, null);
		}
	}
	
	//main
	public static void main(String[] args) {
		JFrame frame = new JFrame("Frame");
		frame.setSize(600, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// JPanel puzzlePieces = new JPanel(new GridLayout(1, 9));
		// JPanel puzzle = new JPanel();
		CardComponent piece1 =new CardComponent(new Card(14, Suit.SPADE));
		frame.add(piece1);
		frame.setVisible(true);
	}
	





}
