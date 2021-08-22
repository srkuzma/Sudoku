package sudoku;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Field extends Canvas {
	
	private int number = 0;
	private boolean isSelected = false;
	private boolean isPresumed = false;
	private int correct;
	private Table owner;
	private int row;
	private int col;
	private boolean isValid = false;
	
	public Field(int number, int correct, Table owner, int row, int col) {
		this.correct = correct;
		this.number = number;
		this.owner = owner;
		this.row = row;
		this.col = col;
		setFocusable(false);
		//setEnabled(false);
		addListeners();
		setBackground(Color.WHITE);
		repaint();
	}
	
	
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		int height = getHeight();
		int width = getWidth();
		
		Font basicFont = new Font("Comic Sans MS", Font.BOLD, 15);
		Font littleFont = new Font("Comic Sans MS", Font.BOLD, 10);
		
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		String toDraw;
		//putting numbers to cells
		if (number != 0) {
			toDraw = ((Integer)number).toString();
			if (isPresumed) {
				//putting presumed numbers to cells
				g.setFont(littleFont);
				g.setColor(Color.GRAY);
				g.drawString(toDraw, (width)/4 - metrics.stringWidth(toDraw)/2, (height)/4 - metrics.getHeight()/2 + metrics.getAscent());
			}
			else {
				g.setFont(basicFont);
				g.setColor(Color.BLACK);
				g.drawString(toDraw, (width)/2 - metrics.stringWidth(toDraw)/2, (height)/2 - metrics.getHeight()/2 + metrics.getAscent());
			}
			
			
		}
		//reacting to a click
		if (isValid) {
			g.setColor(Color.GREEN);
			//setSpacings();
			g.drawRect(0,0,width-1, height-1);
		}
		else if (isSelected) {
			g.setColor(Color.RED);
			//setSpacings();
			g.drawRect(0,0,width-1, height-1);
		}
	}
	
	public void setSelected(boolean is) {
		isSelected = is;
	}
	public void setPresumed(boolean is) {
		isPresumed = is;
	}
	public void setNumber(int n) {
		number = n;
	}
	public void setValid(boolean is) {
		isValid = is;
	}
	
	
	private void addListeners() {
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				isSelected = true;
				owner.setClicked(row, col);
				
				repaint();
			}
		});
		
		
	}
	
}
