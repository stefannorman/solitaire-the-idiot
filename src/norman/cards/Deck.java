package norman.cards;

public class Deck
	extends java.util.Stack
{
		
	public Deck()
	{
		super();

		for(int i = 1; i < 5; i++)
		{
			for(int j = 1; j < 14; j++)
			{
				this.push(new Card(j, i));
			}
		}

	}
		
	public String toString()
	{
		String s = "Current deck:\n";
		for(int i = this.size()-1; i >= 0; i--)
			s += get(i) + "\n";
			
		return s;
	}
	
	public void shuffle()
	{
		java.util.Collections.shuffle(this);
	}
}