/*
 * Copyright (c) 2017 Henry C Goldsack
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package ordervschaos;

import java.util.BitSet;

/**
 * Implements the internals of the Game
 * Stores board state
 * Calculates if and by whom the Game is won
 * 
 * @author Henry Goldsack
 * 
 */
public class Bord {
	private boolean won = false;
	private boolean order;
	
	private int gridlength = 36;
	/* the visual game is played on a six by six grid
	 * the grid is then treated as 1d by taking the rows end to end in a normal fashion to produce 36 addresses
	 * we then have the first 36 bits hold if red a red  and the second 36 bit hold blue
	 * we assert that for 0 <= x <= 35 it is not the case that grid x and grid
	 */
	private BitSet gridColor = new BitSet(72);
	
	/* precalculated lines see line colour for numbering
	 * there are 32 lines on the 6 by 6 grid
	 * the lines in the order
	 * 0-5 the left (staring in (*,1)) horizontal lines top/North to bottom/South
	 * 6-11 the right (staring in (*,2)) horizontal lines top/North to bottom/South
	 * 12-23 the vertical lines left/East to right/West
	 * on the top and then again on the bottom
	 * 24-27 the diagonal lines starting in (0,0),(0,1),(1,0),(1,1)
	 * 27-31 the reverse_diagonal lines starting in (0,4),(0,5),(1,4),(1,5)
	 */
	private static final int[][] LINES = {{0,1,2,3,4},{6,7,8,9,10},{12,13,14,15,16},{18,19,20,21,22},{24,25,26,27,28},
			{30,31,32,33,34},{1,2,3,4,5},{7,8,9,10,11},{13,14,15,16,17},{19,20,21,22,23},{25,26,27,28,29},
			{31,32,33,34,35},{0,6,12,18,24},{1,7,13,19,25},{2,8,14,20,26},{3,9,15,21,27},{4,10,16,22,28},
			{5,11,17,23,29},{6,12,18,24,30},{7,13,19,25,31},{8,14,20,26,32},{9,15,21,27,33},{10,16,22,28,34},
			{11,17,23,29,35},{0,7,14,21,28},{1,8,15,22,29},{6,13,20,27,34},{7,14,21,28,35},{4,9,14,19,24},
			{5,10,15,20,25},{10,15,20,25,30},{11,16,21,26,31}};
	//precalculated array membership
	//this is presently a pointer array in later versions we will have [][] were the [] part is a member of lines
	private static final int[][] MEMBERSHIP = {{0,12,24},{0,6,13,25},{0,6,14},{0,6,15},{0,6,16,28},{6,17,29},{1,12,18,26},
			{1,7,13,19,24,27},{1,7,14,20,25},{1,7,15,21,28},{1,7,16,22,29,30},{7,17,23,31,},{2,12,18},{2,8,13,19,26},
			{2,8,14,20,24,27,28},{2,8,15,21,25,29,30},{2,8,16,22,31,},{8,17,23},{3,12,18},{3,9,13,19,28},
			{3,9,14,20,26,29,30},{3,9,15,21,24,27,31,},{3,9,16,22,25},{9,17,23},{4,12,18,28},{4,10,13,19,29,30},
			{4,10,14,20,31,},{4,10,15,21,26},{4,10,16,22,24,27},{10,17,23,25},{5,18,30},{5,11,19,31,},{5,11,20},{5,11,21},
			{5,11,22,26},{11,23,27}};
	
	/*
	 * slightly like with grid the first 36 bits hold if a line contains red and the second is for it to hold blue
	 */
	private BitSet lineColor = new BitSet(64);
	/**
	 * The method called to take a single turn: placing one token
	 * Attempts to place token in specified spot
	 * returns false if there is token already in spot (ask the player again)
	 * Note: this also updates the state off play that is if someone has one
	 * Note: this class is agnostic to the player whos turn it is that is to be handed by interface control
	 * 
	 * @parm color the Token holding the colour to be placed (tokens in a row must be the same type for order victory)
	 * @param position an int the "vector position" of the token to be placed: x+6y
	 * @return true if token is added, false if not
	 * @effect changes the result of getWon() and getOrder() to reflect move, changes internal board state
	 */
	public boolean turn(Token color, int position) {
		if(gridColor.get(position) || gridColor.get(position+gridlength))
			return false;
		gridColor.set(position+gridlength*color.position);
		for(int l : MEMBERSHIP[position]){
			lineColor.set(l+gridlength*color.position);
			if(checkline(color,l)){
				OrderWin();
			}
		}
		if(lineColor.nextClearBit(0)==32) {
			ChaosWin();
		}
		return true;
	}
	
	private boolean checkline(Token c, int l) {
		for(int p : LINES[l]) {
			if(!gridColor.get(p+gridlength*c.position)) {
				return false;
			}
		}
		return true;
	}
	private void OrderWin() {
		won = true;
		order = true;
	}
	private void ChaosWin() {
		won = true;
		order = false;
	}
	/**
	 * To report when no more turns are necessary to declare a winner
	 * @return true if the game is over, false otherwise
	 */
	public boolean getWon() {
		return won;
	}
	/**
	 * to report the wining player
	 * @return true if Order has won, false if Chaos has won, (undefined if won = false)
	 */
	public boolean getOrder() {
		return order;
	}
}