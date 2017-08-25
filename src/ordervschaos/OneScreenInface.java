package ordervschaos;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextPane;

public class OneScreenInface extends JFrame{
	
	//the program dingbats ✿ (U+273F Black florette) and ❂ (U+2742 Circled open center eight pointed star)
	//they should be in a large number of fonts but find one that works and change here
	private final String font = "OpenSymble";
	
	private final Bord control = new Bord();
	private final JButton[] tiles = new JButton[36];
	private Token color = Token.BLUE;
	private int player = 0;
	private final JRadioButton rbBlue = new JRadioButton(Token.BLUE.name, true);
	private final JRadioButton rbRed = new JRadioButton(Token.RED.name, false);
	private final JTextPane textbox = new JTextPane();
	
	private static final String[] TURNMESSAGE = {"First players Turn","Second Players Turn"};
	private static final String[] RETRYMESSAGE = {"First players Turn\nPlease select an empty Tile",
			"Second Players Turn\\nPlease select an empty Tile"};
	private static final String[] WINNERMESSAGE = {"Victory for Order","Victory for Chaos"};
	
	OneScreenInface(){
		super("OrderVsChaos");
		JPanel panal = new JPanel();
		GridLayout layout = new GridLayout(6,6);
		panal.setLayout(layout);
		for (int i=0;i<tiles.length;i++) {
			tiles[i] = new JButton("    ");
			tiles[i].setFont(new Font(font, Font.PLAIN, 40));
			tiles[i].addActionListener(new TileListener(i, tiles[i]));
			panal.add(tiles[i]);
		}
		
		ButtonGroup group = new ButtonGroup();
		group.add(rbBlue);
		group.add(rbRed);
		rbBlue.addActionListener(new ColorListener(Token.BLUE));
		rbRed.addActionListener(new ColorListener(Token.RED));
		textbox.setText("START");
		
		this.setLayout(new FlowLayout());
		this.add(panal);
		this.add(rbBlue);
		this.add(rbRed);
		this.add(textbox);
	}
	
	public static void main(String[] args) {
		JFrame popup = new OneScreenInface();
		popup.setDefaultCloseOperation(EXIT_ON_CLOSE);
		popup.setSize(500, 500);
		popup.setResizable(false);
		popup.setVisible(true);
	}
	
	public class TileListener implements ActionListener {
		private final int position;
		JButton button;
		TileListener(int position, JButton button){
			this.position = position;
			this.button = button;
		}
		public void actionPerformed(ActionEvent e) {
			if(control.turn(color, position)){
				button.setText(color.symble);
				button.setForeground(color.color);
				if(control.getWon()) {
					textbox.setText(WINNERMESSAGE [control.getOrder() ? 0 : 1]);
				}else {
					player = 1 - player;
					textbox.setText(TURNMESSAGE[player]);
				}
			}else {
				textbox.setText(RETRYMESSAGE[player]);
			}
		}
	}
	
	public class ColorListener implements ActionListener {
		Token c;
		ColorListener(Token color) {
			this.c = color;
		}
		public void actionPerformed(ActionEvent e) {
			color = c;
		}
	
	}
}
