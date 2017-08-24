package ordervschaos;

import java.awt.Color;

public enum Token {
	RED (0,"Red",0xff0000,"✿"),
	BLUE (1, "Blue",0x0000ff,"❂");
	
	public final int position;
	public final String name;
	public final Color color;
	public final String symble;
	
	Token(int position, String name, int color, String symble){
		this.position = position;
		this.name = name;
		this.color = new Color(color, false);
		this.symble = symble;
	}
}
