package sudoku;
import java.awt.*;


public class Timer extends Thread {
	private int sec = 0;
	private int min = 0;
	private Label label;
	public Timer(Label label) {
		this.label = label;
	}
	@Override
	public void run() {
		try {
			while(!Thread.interrupted()) {
				
					Thread.sleep(1000);
				
				sec++;
				if (sec == 60) {
					sec = 0;
					min++;
				}
				label.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
				label.setText("Time: " + String.format("%02d", min) + ":" +  String.format("%02d", sec));
				//revalidate();
			}
		}
		catch(Exception e) {}
	}
}
