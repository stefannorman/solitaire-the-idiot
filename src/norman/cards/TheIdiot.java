package norman.cards;

import java.util.Stack;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * todo continue rad 117!!!
 * - fix double start click bug
 * - executable JAR
 * - commenting
 */
public class TheIdiot
{

	private final int MAXROUNDS = 200;

	private Deck deck;
	private Stack stacks[];
	private JFrame daframe;
	private JLabel cardicons[] = new JLabel[4];
	private JLabel roundcountlabel;
	private boolean updateGUI;
	private JTextArea resultsarea;
	private JLayeredPane layeredPane;

	public TheIdiot()
	{


		// create 4 stacks to put cards in
		stacks	= new Stack[4];
		for(int i = 0; i < stacks.length; i++)
			stacks[i] = new Stack();
	}

	public void startPlaying()
	{

		// get a new Deck of Cards
		deck = new Deck();

		// shuffle deck
		deck.shuffle();

		// debug
		//System.err.println(deck);

		// empty resultarea
		resultsarea.setText("");

		// empty 4 of skind won?!
		layeredPane.removeAll();
		layeredPane.paint(layeredPane.getGraphics());


		int fourOfAKindCount = 0;
		int roundCount = 0;

		while(fourOfAKindCount < 13 && roundCount < MAXROUNDS)
		{
			roundCount++;
			//System.err.println("Round no " + roundCount);

			// update UI
			roundcountlabel.setText("Round: " + roundCount);

			// empty stacks
			for(int i = 0; i < stacks.length; i++)
				stacks[i].removeAllElements();

			while(deck.size() > 0)
			{
				// put 1 card on each stack
				for(int i = 0; i < stacks.length; i++)
				{
					stacks[i].push(deck.pop());
					if(updateGUI)
					{
						cardicons[i].setIcon(((Card) stacks[i].peek()).icon());
						cardicons[i].paint(cardicons[i].getGraphics());
						// sleep a while for GUI to catch up
						try
						{
							Thread.sleep(50);
						}
						catch (InterruptedException e)
						{
							e.printStackTrace();
						}
					}
				}

				// check if all 4 is the same
				int number = ((Card) stacks[0].peek()).number();
				if(
						number == ((Card) stacks[1].peek()).number() &&
						number == ((Card) stacks[2].peek()).number() &&
						number == ((Card) stacks[3].peek()).number()
					)
				{
					// sleep 1 sec to show the 4 of a kind.
					if(updateGUI)
					{
						try
						{
							Thread.sleep(1000);
						}
						catch (InterruptedException e)
						{
							e.printStackTrace();
						}
						JLabel label = new JLabel(new ImageIcon("cards/images/back.gif"));
						int hilayer = layeredPane.highestLayer();
						label.setBounds(hilayer*25, -50, 100, 200);
						//label.addMouseListener();
						layeredPane.add(label, new Integer(hilayer + 1));

					}

					String s = number + "s";
					if(number == 1) s = "aces";
					if(number == 11) s = "jacks";
					if(number == 12) s = "queens";
					if(number == 13) s = "kings";
					System.err.println("Removed " + s + " in round " + roundCount + ".");
					resultsarea.append("Removed " + s + " in round " + roundCount + ".\n");
					resultsarea.paint(resultsarea.getGraphics());
					for(int i = 0; i < stacks.length; i++)
						stacks[i].pop();
					fourOfAKindCount++;
				}

				// move alikes
				moveAlikes();

			}

			// display empty stacks for 1/4 sec
			if(updateGUI)
			{
				for(int i = 0; i < stacks.length; i++)
					cardicons[i].setIcon(null);

				try
				{
					Thread.sleep(250);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}


			// put stacks on top each other
			// to form a new deck
			for(int i = stacks.length - 1; i >= 0; i--)
				deck.addAll(0, stacks[i]);

			// turn deck around
			java.util.Collections.reverse(deck);

		}

		// print result
		String message = "";
		if(roundCount < MAXROUNDS)
			message = "\nSUCCESS after " + roundCount + " rounds!";
		else
			message = "\nNO SUCCESS!! Aborted after " + roundCount + " rounds!\nOnly an IDIOT is still going after that...\n\n" + deck;

		System.out.println(message);
		JOptionPane.showMessageDialog(daframe, message);

	}


	/**
	 * moves the cards of the same number to the right
	 */
	private void moveAlikes()
	{
		boolean restart = true;

		while(restart)
		{
			restart = false;

			// check for doubles on 3 right-hand stacks
			for(int i = stacks.length-1; i > 0 && !stacks[i].empty(); i--)
			{
				int number = ((Card) stacks[i].peek()).number();
				for(int j = i-1; j >= 0 && !stacks[j].empty(); j--)
				{
					int number2 = ((Card) stacks[j].peek()).number();
					if(number == number2)
					{
						// debug
						// System.err.println("moved: " + stacks[j].peek() + " ontop " + stacks[i].peek());
						stacks[i].push(stacks[j].pop());
						restart = true;
					}
				}
			}
		}
	}

	public Component createComponents()
  {
		JPanel pane = new JPanel();

		JPanel buttonpane = new JPanel();
    buttonpane.setLayout(new FlowLayout());
    buttonpane.setBackground(new Color(33, 99, 33));
    JButton button = new JButton("Start");
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
      	RunningThread t = new RunningThread();
      	try {
      		t.start();
      	} catch (IllegalThreadStateException itse)
      	{
      		System.err.println("Already started");
      	}
      	//start();

      }
    });
    buttonpane.add(button);


		JPanel roundcountpane = new JPanel();
    roundcountpane.setLayout(new FlowLayout(FlowLayout.RIGHT));
    roundcountpane.setBackground(new Color(33, 99, 33));
    roundcountlabel = new JLabel();
    roundcountlabel.setForeground(Color.black);
    roundcountpane.add(roundcountlabel);


		JPanel optionpane = new JPanel();
    optionpane.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
    optionpane.setBackground(new Color(33, 99, 33));
    updateGUI = true;
    JCheckBox updateGUIcheckbox = new JCheckBox("update GUI (slower)", true);
    updateGUIcheckbox.setBackground(new Color(33, 99, 33));
    updateGUIcheckbox.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        updateGUI = (e.getStateChange() == ItemEvent.SELECTED);
      }
    });

    buttonpane.add(updateGUIcheckbox);

		resultsarea = new	JTextArea(10, 30);
		resultsarea.setEditable(false);
		JScrollPane resultspane = new JScrollPane(resultsarea);

		JPanel cardpane = new JPanel();
    cardpane.setLayout(new FlowLayout(FlowLayout.CENTER));
    cardpane.setBackground(new Color(33, 99, 33));
    //ImageIcon initicon = new ImageIcon("cards/images/back.gif");
    ImageIcon initicon = null;
		for(int i = 0; i < 4; i++)
  	{
    	cardicons[i] = new JLabel(initicon);
    	cardpane.add(cardicons[i]);
  	}

		layeredPane = new JLayeredPane();

    pane.setBorder(BorderFactory.createEmptyBorder(
                                        50, //top
                                        100, //left
                                        50, //bottom
                                        100) //right
                                      );
		pane.setPreferredSize(new Dimension(600, 600));
    pane.setLayout(new GridLayout(0	,1));
    //pane.setLayout(new GridBagLayout());
    pane.setBackground(new Color(33, 99, 33));

    pane.add(roundcountpane);
    pane.add(cardpane);
    pane.add(buttonpane);
    //pane.add(optionpane);
		pane.add(layeredPane);
		pane.add(resultspane);

    daframe = (JFrame) pane.getParent();

    return pane;
  }

	public static void main(String args[])
	{
		JFrame frame = new JFrame("Solitaire \"The Idiot\"");
		TheIdiot idiot = new TheIdiot();

		// Non GUI
		//idiot.start();

    Component contents = idiot.createComponents();
    frame.getContentPane().add(contents, BorderLayout.CENTER);

    //Finish setting up the frame, and show it.
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
	  });
    frame.pack();
    frame.setVisible(true);


	}
  protected class RunningThread extends Thread
  {
    public void run()
    {
			startPlaying();
		}
  }

}
