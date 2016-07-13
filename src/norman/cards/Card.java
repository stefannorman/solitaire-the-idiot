package norman.cards;

import java.awt.MediaTracker;
import javax.swing.ImageIcon;

public class Card extends java.awt.Component
{
	
	public static final int HEARTS	= 1;
	public static final int SPADES	= 2;
	public static final int DIAMONDS= 3;
	public static final int CLUBS		= 4;

	public static final int ACE			= 1;
	public static final int JACK		= 11;
	public static final int QUEEN		= 12;
	public static final int KING		= 13;
	
	private int number;
	private int color;
	private ImageIcon icon;
	
	public Card(int number, int color)
	{
		this.number = number;
		this.color	= color;
		this.icon		= new ImageIcon("cards/images/" + number + "_" + color + ".gif");
/*
	  MediaTracker mt = new MediaTracker(this);
  	mt.addImage( icon.getImage(), 1 );
	  try
		{
  		mt.waitForID( 1 );
		 }
	  catch (InterruptedException e) {}
*/
	}
	
	public int number()
	{
		return number;
	}
	
	public ImageIcon icon()
	{
		return icon;
	}
	
	public String toString()
	{
		String s;
		s = number + " of ";
		if(number == ACE)			s = "Ace of ";
		if(number == JACK)		s = "Jack of ";
		if(number == QUEEN)		s = "Queen of ";
		if(number == KING)		s = "King of ";

		if(color == HEARTS)		s += "hearts";
		if(color == SPADES)		s += "spades";
		if(color == DIAMONDS)	s += "diamond";
		if(color == CLUBS)		s += "clubs";
		
		return s;
	}
}