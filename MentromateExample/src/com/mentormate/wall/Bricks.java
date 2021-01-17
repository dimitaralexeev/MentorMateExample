package com.mentormate.wall;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Bricks {

	private int N; //	field, which store height
	private int M; //	field, which store weight
	private int[][] firstLayer; //	multidimensional array for the first layer of the wall
	private int[][] secondLayer; //	multidimensional array for the result layer of the wall
	
	//	Constructor of the class Bricks.
	public Bricks(int N, int M, int[][] firstLayer) {
		this.N = N;
		this.M = M;
		this.firstLayer = firstLayer;
	}
	
	//	This method checks if the input layer is valid (return true). The key of the HashMap is the label of the brick, the value is how many time every number was met.
	//	The size of the brick is 2x1 so the value would be 2. If it is different, the input layer is not valid (return false).
	public boolean isFirstLayerValid() {
		Map<Integer, Integer> countPerBrick = new HashMap<>();
		
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				Integer currentCell = this.firstLayer[i][j];
				
				if(countPerBrick.containsKey(currentCell)) {
					Integer currentCellCount = countPerBrick.get(currentCell);
					currentCellCount ++;
					countPerBrick.put(currentCell, currentCellCount);
				} else {
					countPerBrick.put(currentCell, 1);
				}
			}
		}	
		
		for(Map.Entry<Integer, Integer> entry : countPerBrick.entrySet()) {
			if(entry.getValue() != 2) {
				logError("Error: Brick with label " + entry.getKey() + " is with wrong size(" + entry.getValue() + ")");
				return false;
			}
		}
		
		return true;
	}
	
	// If input data are valid, this method checks if possible to build layer, and does it.
	public void buildSecondLayer() {
		this.secondLayer = new int[N][M]; // Initialize multidimensional array of the second layer, which is with the same dimensions of the input layer.
		int brickLabel = 1;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				if(secondLayer[i][j] != 0) {
					continue;
				}
				
				secondLayer[i][j] = brickLabel;
				
				if(tryHorizontal(i, j)) {
					secondLayer[i][j+1] = brickLabel;
				}else if(tryVertical(i, j)){
					secondLayer[i+1][j] = brickLabel;
				}else {
					logError("There is no solution!!!");
					return;
				}
				
				brickLabel ++;
			}
		}
	}
	
	// Private method, which checks if it is possible to put brick vertical.
	private boolean tryVertical(int i, int j) {
		if(i + 1 >= N) {return false;}
		
		if(secondLayer[i+1][j] != 0) {return false;}
		
		if(firstLayer[i][j] == firstLayer[i+1][j]) {return false;}
		
		return true;
	}
	// Private method, which checks if it is possible to put brick horizontal.
	private boolean tryHorizontal(int i, int j) {
		if(j + 1 >= M) {return false;}
		
		if(secondLayer[i][j+1] != 0) {return false;}
		
		if(firstLayer[i][j] == firstLayer[i][j+1]) {return false;}
			
		return true;
	}
	
	// Getter of the second layer.
	public int[][] getSecondLayer() {
		return this.secondLayer;
	}
	
	// Print the result in the console.
	public void printOutput() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				sb.append(String.valueOf(secondLayer[i][j])).append(j == (M - 1) ? "" : " ");
			}
			sb.append(System.lineSeparator());
		}
		System.out.println(sb.toString());
	}
	
	// Method for error massage, when something is wrong with the input data or if it is impossible to do the second layer.
	private static void logError(String errorMsg) {
		System.out.println("-1");
		System.out.println(errorMsg);
	}
	
	// Main method.
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		String firstLine = scan.nextLine();
		String[] splitFirstLine = firstLine.split(" ");
		if(splitFirstLine.length != 2) {
			logError("Error: Incorrect size of brick wall. Please specify only two numbers - height and width");
			scan.close();
			return;
		}
		int N = 0;
		int M = 0;
		int[][] firstLayer = null;
		
		try {
			N = Integer.valueOf(splitFirstLine[0]);
			M = Integer.valueOf(splitFirstLine[1]);
			if(N < 2 || N > 100 || M < 2 || M > 100) {
				if ((N % 2 != 0 || M % 2 != 0)) {
					logError("Error: Width or height is not even or out of the range!");
					return;
				}
			}	
			firstLayer = new int[N][M];// 
			for (int i = 0; i < N; i++) {
				String line;
				do {
					line = scan.nextLine();
				} while(line == null || line.length() == 0);
				String[] splitLine = line.split(" ");
				if (splitLine.length != M) {
					logError("Error: Size of row " + (i+1) + " does not contain " + M + " numbers");
					return;
				}
				for (int j = 0; j < M; j++) {
					firstLayer[i][j] = Integer.valueOf(splitLine[j]);
				}
			}
		} catch(NumberFormatException e) {
			logError("Ërror: Only numbers allowed!");
			return;
		} finally {
			scan.close();			
		}
		
		Bricks solution = new Bricks(N, M, firstLayer);
		if(solution.isFirstLayerValid()) {
			solution.buildSecondLayer();
			solution.printOutput();			
		}

	}

}
