package sudoku;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Table extends Panel implements Runnable {
	
	private int [][] solved = new int[9][9];
	private int [][] completed = new int[9][9];
	private int [][] presumed = new int[9][9];
	private int [][] init = new int[9][9];
	private Field[][] fields = new Field[9][9];
	private Panel[] squares = new Panel[9];
	
	private int[] clickedSquare = new int[2];
	private int ySpacing;
	private int xSpacing;
	private int yReduction;
	private int xReduction;
	private final int rectWidth = 4;
	private final int rectHeight = 4;
	private boolean showField = false;
	private boolean showingSolution = false;
	private Sudoku owner;
	private Thread solveThread;

	public Table(int[][] init, Sudoku owner) {
		solved = init;
		this.owner = owner;
		
		addListeners();
		setFocusable(true);
		setEnabled(true);
		for(int i = 0; i< 9; i++)
			completed[i] = solved[i].clone();
		solve(0,0, completed);
		populatePanel();
		setBackground(Color.BLACK);
			
	}
	

	
	private void addListeners() {
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				int row = e.getX()*9/getWidth();
				int col = e.getY()*9/getHeight();
				fields[row][col].setSelected(true);
				setClicked(row, col);
				
				fields[row][col].repaint();
			}
		});
		
		addKeyListener(new KeyAdapter() {
			
			
			@Override
			public void keyPressed(KeyEvent e) {
				switch(e.getKeyCode()) {
				case KeyEvent.VK_ESCAPE: 
					if (showField)
						fields[clickedSquare[0]][clickedSquare[1]].setSelected(false);
					showField = false;
					fields[clickedSquare[0]][clickedSquare[1]].repaint();
					break;
				case KeyEvent.VK_ENTER: 
					if (showField) {
						int row = clickedSquare[0];
						int col = clickedSquare[1];
						if (presumed[row][col] != 0) {
							if (completed[row][col] == presumed[row][col])
								solved[row][col] = presumed[row][col];
							else {
								presumed[row][col] = 0;
								fields[row][col].setNumber(0);
								owner.addWrong();
							}
							fields[row][col].setPresumed(false);
							presumed[row][col] = 0;
						}
							
						fields[row][col].repaint();
					}
					break;
				case KeyEvent.VK_DOWN:
					if (clickedSquare[0] == 8)
						break;
					fields[clickedSquare[0]][clickedSquare[1]].setSelected(false);
					fields[clickedSquare[0]][clickedSquare[1]].repaint();
					clickedSquare[0]++;
					fields[clickedSquare[0]][clickedSquare[1]].setSelected(true);
					fields[clickedSquare[0]][clickedSquare[1]].repaint();
					break;
				case KeyEvent.VK_UP:
					if (clickedSquare[0] == 0)
						break;
					fields[clickedSquare[0]][clickedSquare[1]].setSelected(false);
					fields[clickedSquare[0]][clickedSquare[1]].repaint();
					clickedSquare[0]--;
					fields[clickedSquare[0]][clickedSquare[1]].setSelected(true);
					fields[clickedSquare[0]][clickedSquare[1]].repaint();
					break;
				case KeyEvent.VK_RIGHT:
					if (clickedSquare[1] == 8)
						break;
					fields[clickedSquare[0]][clickedSquare[1]].setSelected(false);
					fields[clickedSquare[0]][clickedSquare[1]].repaint();
					clickedSquare[1]++;
					fields[clickedSquare[0]][clickedSquare[1]].setSelected(true);
					fields[clickedSquare[0]][clickedSquare[1]].repaint();
					break;
				case KeyEvent.VK_LEFT:
					if (clickedSquare[1] == 0)
						break;
					fields[clickedSquare[0]][clickedSquare[1]].setSelected(false);
					fields[clickedSquare[0]][clickedSquare[1]].repaint();
					clickedSquare[1]--;
					fields[clickedSquare[0]][clickedSquare[1]].setSelected(true);
					fields[clickedSquare[0]][clickedSquare[1]].repaint();
					break;
				}
				if (e.getKeyChar() >= '0' && e.getKeyChar() <= '9' ) {
					int row = clickedSquare[0];
					int col = clickedSquare[1];
					if (solved[row][col] == 0) {
						presumed[row][col] = e.getKeyChar() - '0';
						fields[row][col].setNumber(e.getKeyChar()-'0');
						fields[row][col].setPresumed(true);
						fields[row][col].repaint();
					}
					
				}
			}

			
			});
		
	}
	
	public void solveFinalUtil() {
		if (!showingSolution) {
			showingSolution = true;
			solveThread = new Thread(Table.this);
			solveThread.start();
		}
		
	}
	
	
	public void setClicked(int row, int col) {
		fields[clickedSquare[0]][clickedSquare[1]].setSelected(false);
		fields[clickedSquare[0]][clickedSquare[1]].repaint();
		clickedSquare[0] = row;
		clickedSquare[1] = col;
		showField = true;
		setFocusable(true);
	}

	private void populatePanel() {
		setLayout(new GridLayout(3,3,3,3));
		for(int i = 0; i<9; i++) {
			for(int j = 0; j<9; j++)
				fields[i][j] = new Field(solved[i][j],completed[i][j], this,i, j);
		}
		for(int i = 0; i<9; i++)
			squares[i] = new Panel(new GridLayout(3,3,1,1));
		for(int i = 0; i<9; i++) {
			for(int j = i-i%3; j< i-i%3 + 3; j++)
				for(int k = 3*(i%3); k< 3*(i%3) + 3; k++) {
					squares[i].add(fields[j][k]);
				}
		}
		for(int i = 0; i<9; i++) {
			add(squares[i]);
		}
		
		
		
	}
	
	public static boolean solve(int row,int col, int [][] completed) {
		
		if (row >= 9 || col >= 9)
			return true;
		if (completed[row][col] != 0) {
			return solve(row + ((col == 8) ? 1:0), (col == 8) ? 0:col+1, completed);
		}
		else {
			for(int i = 1; i<=9; i++) {
				
				if (valid(row,col, i, completed)) {
					
					completed[row][col] = i;
					if (row == 8 && col == 8) {
						return true;
						
					}
					if (solve(row + ((col == 8) ? 1:0), (col == 8) ? 0:col+1, completed))
						return true;
				}
				
				
			}
			completed[row][col] = 0;
			return false;
		}
		
	}
	
	@Override
	public void run() {
		for(int i = 0; i< 9; i++)
			completed[i] = solved[i].clone();
		
		numOfCalls = 0;
		threshhold = 100;
		pauseTime = STARTING_PAUSE_TIME;
		
		fields[clickedSquare[0]][clickedSquare[1]].setSelected(false);
		fields[clickedSquare[0]][clickedSquare[1]].repaint();
		
		for(int i = 0; i<9; i++)
			for(int j = 0; j<9; j++)
				fields[i][j].setEnabled(false);
		solveFinal(0,0);
		for(int i = 0; i< 9; i++)
			solved[i] = completed[i].clone();
		
	}
	
	int numOfCalls;
	int pauseTime;
	int threshhold;
	final int STARTING_PAUSE_TIME = 40;
	
	private boolean solveFinal(int row,int col) {
		//System.out.println(row + " " + col);
		if (row == 9 || col == 9) {
			for(int j = 0; j<9; j++)
				for(int k =0; k<9; k++) {
					fields[j][k].setSelected(false);
					fields[j][k].setValid(false);
					fields[j][k].repaint();
				}
			
			return true;
		}
			
		if (completed[row][col] != 0) {
			return solveFinal(row + ((col == 8) ? 1:0), (col == 8) ? 0:col+1);
		}
		else {
			for(int i = 1; i<=9; i++) {
				
				if (valid(row,col, i, completed)) {
					
					completed[row][col] = i;
					fields[row][col].setNumber(i);
					fields[row][col].setValid(true);
					fields[row][col].setSelected(true);
					fields[row][col].setPresumed(false);
					fields[row][col].repaint();
					
					try {
						if (pauseTime > 0) {
							Thread.sleep(pauseTime);
							numOfCalls++;
							if (numOfCalls > threshhold) {
								pauseTime = pauseTime - 10;
								threshhold *= 2;
							}
						}
					}
					catch(Exception e) {}
					
					if (solveFinal(row + ((col == 8) ? 1:0), (col == 8) ? 0:col+1))
						return true;
				}
				
				
			}
			completed[row][col] = 0;
			fields[row][col].setNumber(0);
			fields[row][col].setValid(false);
			fields[row][col].repaint();
			return false;
		}
		
	}

	public static boolean valid(int row, int col, int val, int[][] completed) {
		for(int i = 0; i< 9; i++)
			if (completed[row][i] == val || completed[i][col] == val) {
				return false;
			}
		for(int i = row - row %3; i < row - row%3 + 3; i++)
			for(int j = col - col%3; j < col - col%3 + 3; j++) {
				if (completed[i][j] == val)
					return false;
			}
		return true;
	}



	public void newGame(int[][] init) {
		showingSolution = false;
		owner.clearWrong();
		for(int i = 0; i<9; i++)
			solved[i] = init[i].clone();
		initAllMatrices();
	}



	private void initAllMatrices() {
		for(int i = 0; i<9; i++)
			for(int j = 0; j<9; j++)
				fields[i][j].setEnabled(true);
		for(int i = 0; i< 9; i++)
			completed[i] = solved[i].clone();
		solve(0,0, completed);
		for(int i = 0; i<9; i++) {
			for(int j = 0; j<9; j++) {
				presumed[i][j] = 0;
				fields[i][j].setNumber(solved[i][j]);
				fields[i][j].setPresumed(false);
				fields[i][j].setSelected(false);
				fields[i][j].repaint();
				
			}
			
		}
		
	}
}
