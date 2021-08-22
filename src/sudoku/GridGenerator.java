package sudoku;

public class GridGenerator {
	private GridGenerator() {}
	
	
	
	public static int[][] generate(){
		int[][] result = new int[9][9];
		int[][] tmp = new int[9][9];
		for(int i = 0; i < 9; i++)
			for(int j = 0; j<9; j++)
				result[i][j] = 0;
		for(int i = 0; i<9; i++) {
			tmp[i] = result[i].clone();
		}
		while (num(result) < 50) {
			int r = (int)(9 * Math.random());
			int c = (int)(9 * Math.random());
			int v = (int)(9 * Math.random());
			if (result[r][c] == 0 && Table.valid(r,c,v, result)) {
				result[r][c] = v;
			}
			for(int i = 0; i<9; i++) {
				tmp[i] = result[i].clone();
			}
		}
		while (!Table.solve(0,0, tmp)) {
			int r = (int)(9 * Math.random());
			int c = (int)(9 * Math.random());
			int v = (int)(9 * Math.random());
			if (result[r][c] != 0) {
				result[r][c] = 0;
			}
			for(int i = 0; i<9; i++) {
				tmp[i] = result[i].clone();
			}
		}
		
		return result;
	}



	private static int num(int[][] matrix) {
		int result = 0;
		for(int i = 0; i<9; i++)
			for(int j = 0; j<9; j++)
				if (matrix[i][j] != 0)
					result++;
		return result;
	}
	
}
