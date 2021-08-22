package sudoku;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Sudoku extends Frame {
	
	private Table table;
	private Timer timer;
	
	private Panel upper;
	private Panel lower;
	
	private Label label = new Label("Time: 00:00");
	
	private Label wrong = new Label("  ");
	
	public Sudoku(int [][] init) {
		
		table = new Table(init, this);
		setTitle("Sudoku");
		setBounds(100,100,400,400);
		setResizable(false);
		populateWindow();
		startTimer();
		setVisible(true);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				finish();
			}
		});
	}
	
	private void finish() {
		timer.interrupt();
		dispose();
	}

	private void startTimer() {
		timer = new Timer(label);
		timer.start();
		
	}

	private void populateWindow() {
		upper = new Panel(new GridLayout(1,1,3,3));
		
		upper.add(table);
		
		lower = new Panel(new BorderLayout());
		lower.add(label, BorderLayout.EAST);
		lower.add(wrong, BorderLayout.CENTER );
		
		
		setFont(new Font(null, Font.BOLD, 14));
		
		label.setAlignment(Label.CENTER);
		wrong.setAlignment(Label.LEFT);
		wrong.setForeground(Color.RED);
		
		add(upper, BorderLayout.CENTER);
		add(lower, BorderLayout.SOUTH);
		
		MenuBar mb = new MenuBar();
		setFont(new Font("Calibri", Font.PLAIN, 14));
		Menu m = new Menu("Options");
		MenuItem m1 = new MenuItem("Show solution", new MenuShortcut('S'));
		MenuItem m2 = new MenuItem("New game", new MenuShortcut('G'));
		MenuItem m3 = new MenuItem("Exit", new MenuShortcut('E'));
		
		m.add(m1);
		m.add(m2);
		m.addSeparator();
		m.add(m3);
		
		mb.add(m);
		setMenuBar(mb);
		
		m1.addActionListener((ae) ->
		{
			table.solveFinalUtil();
		}
		);
		m2.addActionListener((ae) ->
		{
			table.newGame(GridGenerator.generate());
		}
		);
		m3.addActionListener((ae) ->
		{
			finish();
		}
		);
		
	}
	public static void main(String[] args) {
		int [][] table =  {
				{5,3,0,0,7,0,0,0,0},
				{6,0,0,1,9,5,0,0,0},
				{0,9,8,0,4,0,0,6,0},
				{8,0,0,0,6,1,0,0,3},
				{4,0,0,8,0,3,0,0,1},
				{7,0,0,0,2,0,0,0,6},
				{0,6,0,0,0,0,2,8,0},
				{0,0,0,4,1,9,0,0,5},
				{0,0,0,0,8,0,0,7,9}
		};
		new Sudoku(GridGenerator.generate());
	}

	public void addWrong() {
		wrong.setFont(new Font("Calibri", Font.BOLD, 15));
		wrong.setText(wrong.getText() + "X");
		revalidate();
	}
	public void clearWrong() {
		wrong.setFont(new Font("Calibri", Font.BOLD, 15));
		wrong.setText("");
		revalidate();
	}
}
