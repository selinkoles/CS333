import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class connect4 {
	static final int ROWS = 6;
	static final int COLUMNS = 7;
	static final int EMPTY = 0;
	static final int USER_DISC = 1;
	static final int AI_DISC = 2;
	static final int CONNECT4_LENGTH = 4;
	static final int negativeInf = (int) Double.NEGATIVE_INFINITY;
	static final int positiveInf = (int) Double.POSITIVE_INFINITY;

	public static void main(String[] args) {
		boolean gameOver = false;

		// getting the depth value from user
		Scanner sc = new Scanner(System.in);
		System.out.println("Choose the search depth: ");
		int depth = sc.nextInt();

		// choosing the turn
		int turn;
		if (Math.random() <= 0.5) {
			turn = 0; // represents user
		} else {
			turn = 1; // represents AI
		}

		// creating new board
		int[][] gameBoard = createBoard();
		printBoard(gameBoard);

		// starting the game
		while (!gameOver) {

			// turn of user
			if (turn == 0) {

				// getting the current state of game i.e. valid COLUMNS
				ArrayList<Integer> validColumns = getValidColumns(gameBoard);
				System.out.print("Pick a move ( a number from ");
				if (validColumns.size() == 7) {
					System.out.println("1-7 )");
				} else {
					for (int col : validColumns) {
						System.out.print((col + 1) + " ");
					}
					System.out.println(')');
				}

				int choosenCol = sc.nextInt() - 1;

				if (!isValidColumn(gameBoard, choosenCol)) {
					System.out.println("You picked an invalid move.");
				} else {
					int emptyRow = getEmptyRow(gameBoard, choosenCol);

					// dropping disc in the board
					placeDisc(gameBoard, emptyRow, choosenCol, USER_DISC);
					printBoard(gameBoard);

					// checking if current move resulted in the goal to win this game i.e. 4 discs
					// in row
					if (isWinningMove(gameBoard, USER_DISC)) {
						gameOver = true;
						System.out.println("Congrats! You won.");
					}

					// updating turn to pass to next player
					turn += 1;
					turn = turn % 2;
				}
			}
			// turn of AI
			else if ((turn == 1) && (!gameOver)) {
				// int[] arr = minimax(gameBoard, depth, negativeInf, positiveInf, true);
				int choosenCol = minimax(gameBoard, depth, negativeInf, positiveInf, true)[0];
				// int minimaxScore = arr[1];

				if (isValidColumn(gameBoard, choosenCol)) {
					int emptyRow = getEmptyRow(gameBoard, choosenCol);

					placeDisc(gameBoard, emptyRow, choosenCol, AI_DISC);
					printBoard(gameBoard);

					// checking if current move resulted in the goal to win this game i.e. 4 discs
					// in row
					if (isWinningMove(gameBoard, AI_DISC)) {
						gameOver = true;
						System.out.println("Program wins!");
					}

					// updating turn to pass to next player
					turn += 1;
					turn = turn % 2;
				}
			}
		}

		sc.close();
	}

	private static int[][] createBoard() {
		int[][] board = new int[ROWS][COLUMNS];

		int[] row = new int[COLUMNS];
		Arrays.fill(row, EMPTY);
		for (int i = 0; i < ROWS; i++) {
			board[i] = Arrays.copyOf(row, COLUMNS);
		}

		return board;
	}

	private static void printBoard(int[][] board) {
		System.out.println("\n''''\n1\t2\t3\t4\t5\t6\t7\n");
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j] == EMPTY)
					System.out.print("#\t");
				else if (board[i][j] == USER_DISC)
					System.out.print("X\t");
				else if (board[i][j] == AI_DISC) {
					System.out.print("O\t");
				}					
			}
			System.out.println();
		}
		System.out.println("''''\n");
	}

	private static Boolean isValidColumn(int[][] board, int col) {
		if ((col < 0) || (col > 6)) {
			return false;
		}
		return board[0][col] == 0;
	}

	private static ArrayList<Integer> getValidColumns(int[][] board) {
		ArrayList<Integer> validColumns = new ArrayList<>();
		for (int col = 0; col < COLUMNS; col++) {
			if (isValidColumn(board, col)) {
				validColumns.add(col);
			}
		}
		return validColumns;
	}

	private static int getEmptyRow(int[][] board, int col) {
		for (int row = 5; row >= 0; row--) {
			if (board[row][col] == 0) {
				return row;
			}
		}
		return 0;
	}

	private static void placeDisc(int[][] board, int row, int col, int disc) {
		board[row][col] = disc;
	}

	private static boolean isWinningMove(int[][] board, int disc) {
		int COLUMNS = 7;
		int ROWS = 6;

		// checking horizontal alignment of discs
		for (int col = 0; col < COLUMNS - 3; col++) {
			for (int row = 0; row < ROWS; row++) {
				if ((board[row][col] == disc) && (board[row][col + 1] == disc) && (board[row][col + 2] == disc)
						&& (board[row][col + 3] == disc)) {
					return true;
				}
			}
		}

		// checking vertical alignment of discs
		for (int col = 0; col < COLUMNS; col++) {
			for (int row = 0; row < ROWS - 3; row++) {
				if ((board[row][col] == disc) && (board[row + 1][col] == disc) && (board[row + 2][col] == disc)
						&& (board[row + 3][col] == disc)) {
					return true;
				}
			}
		}

		// checking positively diagonal alignment of discs
		for (int col = 0; col < COLUMNS - 3; col++) {
			for (int row = 0; row < ROWS - 3; row++) {
				if ((board[row][col] == disc) && (board[row + 1][col + 1] == disc) && (board[row + 2][col + 2] == disc)
						&& (board[row + 3][col + 3] == disc)) {
					return true;
				}
			}
		}

		// checking positively diagonal alignment of discs
		for (int col = 0; col < COLUMNS - 3; col++) {
			for (int row = 3; row < ROWS; row++) {
				if ((board[row][col] == disc) && (board[row - 1][col + 1] == disc) && (board[row - 2][col + 2] == disc)
						&& (board[row - 3][col + 3] == disc)) {
					return true;
				}
			}
		}
		return false;
	}

	private static Boolean isTreminalNode(int[][] board) {
		boolean a = isWinningMove(board, USER_DISC);
		boolean b = isWinningMove(board, AI_DISC);
		boolean c = (getValidColumns(board).size() == 0);
		// boolean result = (isWinningMove(board, USER_DISC) || isWinningMove(board,
		// AI_DISC) || (getValidColumns(board).size() == 0));
		return (a || b || c);
	}

	private static Integer[] minimax(int[][] board, int depth, int alpha, int beta, boolean maximizer) {
		// a variable to store two values i.e. column no. and highest minimaxscore which
		// will be returned
		Integer[] result = new Integer[2];

		ArrayList<Integer> validColumns = getValidColumns(board);
		boolean terminalNode = isTreminalNode(board);

		// when node is terminal
		if (terminalNode) {
			if (isWinningMove(board, AI_DISC)) {
				result[0] = null; // column no.
				result[1] = positiveInf; // minimax score
				return result;
			} else if (isWinningMove(board, USER_DISC)) {
				result[0] = null; // column no.
				result[1] = negativeInf; // minimax score
				return result;
			} else {
				// no valid moves are remaining, game over
				result[0] = null; // column no.
				result[1] = 0; // minimax score
				return result;
			}
		}
		// when depth is zero
		else if (depth == 0) {
			result[0] = null; // column no.
			result[1] = evaluateConectedDiscs(board, AI_DISC); // minimax score
			return result;
		}

		// pruning the decision tree which returns max value
		if (maximizer) {
			result[0] = validColumns.get((int) (Math.random() * validColumns.size()));
			result[1] = negativeInf;
			for (int col : validColumns) {
				int row = getEmptyRow(board, col);
				int[][] copiedBoard = Arrays.stream(board).map(int[]::clone).toArray(int[][]::new);
				placeDisc(copiedBoard, row, col, AI_DISC);
				int score = minimax(copiedBoard, depth - 1, alpha, beta, false)[1];
				if (score > result[1]) {
					result[0] = col; // updating column no.
					result[1] = score; // updating minimax score
				}
				alpha = Math.max(alpha, result[1]);
				if (alpha >= beta) {
					break;
				}
			}
			return result;
		}
		// pruning the decision tree which returns min value
		else {
			result[0] = validColumns.get((int) (Math.random() * validColumns.size()));
			result[1] = positiveInf;
			for (int col : validColumns) {
				int row = getEmptyRow(board, col);
				int[][] copiedBoard = Arrays.stream(board).map(int[]::clone).toArray(int[][]::new);
				placeDisc(copiedBoard, row, col, USER_DISC);
				int score = minimax(copiedBoard, depth - 1, alpha, beta, true)[1];
				if (score < result[1]) {
					result[0] = col; // updating column no.
					result[1] = score; // updating minimax score
				}
				beta = Math.min(beta, result[1]);
				if (alpha >= beta) {
					break;
				}
			}
			return result;
		}
	}

	private static int evaluateConectedDiscs(int[][] board, int disc) {
		int score = 0;

		// calculate score for discs aligned in central vector
		ArrayList<Integer> centerVector = new ArrayList<>();
		for (int i = 0; i < ROWS; i++) {
			centerVector.add(board[i][COLUMNS / 2]);
		}
		score += (Collections.frequency(centerVector, disc) * 3);

		// calculate scores for discs aligned in horizontal vectors
		ArrayList<Integer> rowVector = new ArrayList<>();
		List<Integer> connect4Array = new ArrayList<Integer>();
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLUMNS; col++) {
				rowVector.add(board[row][col]);
			}
			for (int i = 0; i < COLUMNS - 3; i++) {
				connect4Array = rowVector.subList(i, i + CONNECT4_LENGTH);
				score += scoreCounter(connect4Array, disc);
			}
			rowVector.clear();
		}

		// calculate scores for discs aligned in vertical vectors
		ArrayList<Integer> columnVector = new ArrayList<>();
		for (int col = 0; col < COLUMNS; col++) {
			for (int row = 0; row < ROWS; row++) {
				columnVector.add(board[row][col]);
			}
			for (int i = 0; i < ROWS - 3; i++) {
				connect4Array = columnVector.subList(i, i + CONNECT4_LENGTH);
				score += scoreCounter(connect4Array, disc);
			}
			columnVector.clear();
		}

		connect4Array = columnVector.subList(0, 0);
		// calculate score for discs aligned in positively sloped vectors
		for (int row = 0; row < ROWS - 3; row++) {
			for (int col = 0; col < COLUMNS - 3; col++) {
				for (int i = 0; i < CONNECT4_LENGTH; i++) {
					connect4Array.add(board[row + i][col + i]);
				}
				score += scoreCounter(connect4Array, disc);
				connect4Array.clear();
			}
		}

		connect4Array.clear();
		// calculate score for discs aligned in negatively sloped vectors
		for (int col = 0; col < COLUMNS - 3; col++) {
			for (int row = 0; row < ROWS - 3; row++) {
				for (int i = 0; i < CONNECT4_LENGTH; i++) {
					connect4Array.add(board[row + 3 - i][col + i]);
				}
				score += scoreCounter(connect4Array, disc);
				connect4Array.clear();
			}
		}

		return score;
	}

	private static int scoreCounter(List<Integer> connect4Array, int playerDisc) {
		int score = 0;
		int opponentDisc = USER_DISC;
		if (playerDisc == USER_DISC) {
			opponentDisc = AI_DISC;
		}

		int playerDiscCount = Collections.frequency(connect4Array, playerDisc);
		int opponentDiscCount = Collections.frequency(connect4Array, opponentDisc);
		int emptySpaceCount = Collections.frequency(connect4Array, 0);
		// if 4 discs of player are aligned in a row then player is rewarded with 100
		// scores
		if (playerDiscCount == 4) {
			score += 100;
		}
		// when 3 discs of player are aligned and one space is empty then player is
		// rewarded with lesser score
		else if ((playerDiscCount == 3) && (emptySpaceCount == 1)) {
			score += 12;
		}
		// when 2 discs of player are aligned and 2 spaces are empty then player is
		// rewarded with even lower score
		else if ((playerDiscCount == 2) && (emptySpaceCount == 2)) {
			score += 5;
		}

		// if 3 discs of opponent player are aligned then player is penalised with
		// negative score, showing that it is not a optimal move
		if ((opponentDiscCount == 3) && (emptySpaceCount == 1)) {
			score -= 10;
		}

		return score;
	}

}
