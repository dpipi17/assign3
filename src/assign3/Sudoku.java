package assign3;

import java.util.*;

/*
 * Encapsulates a Sudoku grid to be solved.
 * CS108 Stanford.
 */
public class Sudoku {
	// Provided grid data for main/testing
	// The instance variable strategy is up to you.
	
	// Provided easy 1 6 grid
	// (can paste this text into the GUI too)
	public static final int[][] easyGrid = Sudoku.stringsToGrid(
	"1 6 4 0 0 0 0 0 2",
	"2 0 0 4 0 3 9 1 0",
	"0 0 5 0 8 0 4 0 7",
	"0 9 0 0 0 6 5 0 0",
	"5 0 0 1 0 2 0 0 8",
	"0 0 8 9 0 0 0 3 0",
	"8 0 9 0 4 0 2 0 0",
	"0 7 3 5 0 9 0 0 1",
	"4 0 0 0 0 0 6 7 9");
	
	
	// Provided medium 5 3 grid
	public static final int[][] mediumGrid = Sudoku.stringsToGrid(
	 "530070000",
	 "600195000",
	 "098000060",
	 "800060003",
	 "400803001",
	 "700020006",
	 "060000280",
	 "000419005",
	 "000080079");
	
	// Provided hard 3 7 grid
	// 1 solution this way, 6 solutions if the 7 is changed to 0
	public static final int[][] hardGrid = Sudoku.stringsToGrid(
	"3 7 0 0 0 0 0 8 0",
	"0 0 1 0 9 3 0 0 0",
	"0 4 0 7 8 0 0 0 3",
	"0 9 3 8 0 0 0 1 2",
	"0 0 0 0 4 0 0 0 0",
	"5 2 0 0 0 6 7 9 0",
	"6 0 0 0 2 1 0 4 0",
	"0 0 0 5 3 0 9 0 0",
	"0 3 0 0 0 0 0 5 1");
	
	
	public static final int SIZE = 9;  // size of the whole 9x9 puzzle
	public static final int PART = 3;  // size of each 3x3 part
	public static final int MAX_SOLUTIONS = 100;
	
	
	// private instance variables
	private int [][] sudGrid;
	private int [][] solutionGrid;
	private List<Spot> spots;
	private int solutions;
	private long msRes;

	
	
	// Provided various static utility methods to
	// convert data formats to int[][] grid.
	
	/**
	 * Returns a 2-d grid parsed from strings, one string per row.
	 * The "..." is a Java 5 feature that essentially
	 * makes "rows" a String[] array.
	 * (provided utility)
	 * @param rows array of row strings
	 * @return grid
	 */
	public static int[][] stringsToGrid(String... rows) {
		int[][] result = new int[rows.length][];
		for (int row = 0; row<rows.length; row++) {
			result[row] = stringToInts(rows[row]);
		}
		return result;
	}
	
	
	/**
	 * Given a single string containing 81 numbers, returns a 9x9 grid.
	 * Skips all the non-numbers in the text.
	 * (provided utility)
	 * @param text string of 81 numbers
	 * @return grid
	 */
	public static int[][] textToGrid(String text) {
		int[] nums = stringToInts(text);
		if (nums.length != SIZE*SIZE) {
			throw new RuntimeException("Needed 81 numbers, but got:" + nums.length);
		}
		
		int[][] result = new int[SIZE][SIZE];
		int count = 0;
		for (int row = 0; row<SIZE; row++) {
			for (int col=0; col<SIZE; col++) {
				result[row][col] = nums[count];
				count++;
			}
		}
		return result;
	}
	
	
	/**
	 * Given a string containing digits, like "1 23 4",
	 * returns an int[] of those digits {1 2 3 4}.
	 * (provided utility)
	 * @param string string containing ints
	 * @return array of ints
	 */
	public static int[] stringToInts(String string) {
		int[] a = new int[string.length()];
		int found = 0;
		for (int i=0; i<string.length(); i++) {
			if (Character.isDigit(string.charAt(i))) {
				a[found] = Integer.parseInt(string.substring(i, i+1));
				found++;
			}
		}
		int[] result = new int[found];
		System.arraycopy(a, 0, result, 0, found);
		return result;
	}


	// Provided -- the deliverable main().
	// You can edit to do easier cases, but turn in
	// solving hardGrid.
	public static void main(String[] args) {
		Sudoku sudoku;
		sudoku = new Sudoku(easyGrid);
		
		System.out.println(sudoku); // print the raw problem
		int count = sudoku.solve();
		System.out.println("solutions:" + count);
		System.out.println("elapsed:" + sudoku.getElapsed() + "ms");
		System.out.println(sudoku.getSolutionText());
	}
	
	
	/**
	 * Sets up based on the given ints.
	 */
	public Sudoku(int[][] ints) {
		sudGrid = new int[SIZE][SIZE];
		spots = new ArrayList<Spot>();
		solutionGrid = new int[SIZE][SIZE];
		solutions = 0;
		msRes = 0;
		
		for(int row = 0; row < SIZE; row++) {
			for(int col = 0; col < SIZE; col++) {
				sudGrid[row][col] = ints[row][col];
				if(sudGrid[row][col] == 0) {
					// create spots for only 0 values
					Spot newSpot = new Spot(row, col);
					spots.add(newSpot);
				}
			}
		}
		
		// sort spots using ranks
		Collections.sort(spots);
	}
	
	/**
	 * Alternate constructor.
	 */
	public Sudoku(String text) {
		this(textToGrid(text));
	}
	
	
	/**
	 * Solves the puzzle, invoking the underlying recursive search.
	 */
	public int solve() {
		long startTime = System.currentTimeMillis();
		solveHelper(0);
		long endTime = System.currentTimeMillis();
		msRes = endTime - startTime;
		return solutions;
	}
	
	
	// helper recursion function, which counts solutions
	public void solveHelper(int index) {
		if(solutions >= MAX_SOLUTIONS) return;
		if(index > spots.size()) return;
		
		// if index is spots.size that means there is no spot with value 0
		// and sudoku is solved
		if(index == spots.size()) {
			if(solutions == 0) getSolutionGrid();
			solutions++;
			return;
		}
		
		// find possible values for current Spot
		Spot currSpot = spots.get(index);
		if(currSpot.getValue() == 0) {
			Set<Integer> validSet = currSpot.getValidSet();
			
			for(Integer newVal : validSet) {
				currSpot.set(newVal);
				solveHelper(index + 1);
			}
			currSpot.set(0);				
		}
		
	}
	
	// writes solution from sudGrid into solutionGrid
	private void getSolutionGrid() {
		for(int row = 0; row < SIZE; row++) {
			for(int col = 0; col < SIZE; col++) {
				solutionGrid[row][col] = sudGrid[row][col];
			}
		}
	}
	
	
	// text form of the first one found 
	// if there is no solution it return empty string
	public String getSolutionText() {
		if(solutions > 0) {
			Sudoku solutionSud = new Sudoku(solutionGrid); 
			return solutionSud.toString();			
		} else {
			return "";
		}
	}
	
	// returns the elapsed time spent in the solve method
	public long getElapsed() {
		return msRes;
	}
	
	
	// override toString method
	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		
		for (int row = 0; row < SIZE; row++) {
			boolean notFirst = false;
			for (int col = 0; col < SIZE; col++) {
				int value = sudGrid[row][col];
				if (notFirst) {
					buffer.append(' ');
				} else {
					notFirst = true;
				}
				
				buffer.append(value);
			}
			buffer.append("\n");
		}
		
		return (buffer.toString());
	}
	
	// inner spot class
	private class Spot implements Comparable<Spot>{
		private int row;
		private int col;
		private int value;
		private int ranking; 
		
		// constructor of the spot class
		public Spot(int row, int col) {
			this.row = row;
			this.col = col;
			
			value = 0;
			ranking = getValidSet().size();
		}
		
		
		// returns set of the possible values for this spot
		public Set<Integer> getValidSet() {
			Set<Integer> resultSet = new HashSet<Integer>();
			// adds all values in the set
			for(int val = 1; val <= 9; val++) {
				resultSet.add(val);
			}
			
			// remove values which occurs in the same row
			for(int currRow = 0; currRow < SIZE; currRow++) {
				resultSet.remove(sudGrid[currRow][col]);
			}
			
			// remove values which occurs in the same column
			for(int currCol = 0; currCol < SIZE; currCol++) {
				resultSet.remove(sudGrid[row][currCol]);
			}
			
			// remove values which occurs in the same part
			for(int newRow = 0; newRow < SIZE; newRow += 3) {
				for(int newCol = 0; newCol < SIZE; newCol += 3) {
					if(inPart(newRow, newCol)) {	
						for(int i = newRow; i < newRow + PART; i++) {
							for(int j = newCol; j < newCol + PART; j++) {
								resultSet.remove(sudGrid[i][j]);								
							}
						}
					}
				}
			}
			
			return resultSet;
		}
		
		
		// returns true if the spot is in this part
		private boolean inPart(int partR, int partC) {
			return (row >= partR && row < partR + PART && col >= partC && col < partC + PART);
		}
		
		// changes value of the spot
		public void set(int value) {
			this.value = value;
			sudGrid[row][col] = value;
		}
		
		// returns value of this spot.
		public int getValue() {
			return value;
		}
		
		// returns rank of spot. which is possible values for this point.
		public int getRank() {
			return ranking;
		}
		
		// compare method for sorting
		@Override
	    public int compareTo(Spot otherSpot) {
	        return (this.getRank() - otherSpot.getRank());
	    }
	}

}
