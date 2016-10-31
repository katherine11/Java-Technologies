package seachess;
public class Board {

	private char[][] boardContent = new char[5][11];

	public Board() {

		insertBorders();
		insertNumbers();
		insertSpaces();

	}

	private void insertSpaces() {
		for (int row = 0; row < this.boardContent.length; row += 2) {
			for (int col = 0; col < boardContent[row].length; col += 2) {
				this.boardContent[row][col] = ' ';
			}
		}
	}

	private void insertNumbers() {
		int number = 1;
		for (int row = 0; row < this.boardContent.length; row += 2) {
			for (int col = 1; col < this.boardContent[row].length; col += 4) {
				this.boardContent[row][col] = (char) ('0' + number++);
			}
		}
	}

	private void insertBorders() {
		for (int row = 0; row < this.boardContent.length; row++) {
			for (int col = 0; col < boardContent[row].length; col++) {
				if (col == 3 || col == 7) {
					this.boardContent[row][col] = '|';
				} else {
					if (row % 2 == 1) {
						this.boardContent[row][col] = '-';
					}
				}
			}
		}
	}

	void printBoard() {
		for (int row = 0; row < this.boardContent.length; row++) {
			for (int col = 0; col < this.boardContent[row].length; col++) {
				System.out.print(this.boardContent[row][col]);
			}
			System.out.println();
		}

	}

	// checks if a requested position is free
	boolean isFreePosition(int position) {
		for (int row = 0; row < this.boardContent.length; row += 2) {
			for (int col = 1; col < this.boardContent[row].length; col += 4) {
				char positionInChar = (char) (position + '0');
				if (positionInChar == this.boardContent[row][col]) {
					return true;
				}
			}
		}
		return false;
	}

	// checks if all squares have values X or O;
	boolean isBoardFull() {

		for (int row = 0; row < this.boardContent.length; row += 2) {
			for (int col = 1; col < this.boardContent[row].length; col += 4) {
				if (this.boardContent[row][col] >= '1' && this.boardContent[row][col] <= '9') {
					return false;
				}
			}
		}
		return true;
	}

	public void setPlayersMove(char valueOfMove, int valuePosition) {

		for (int row = 0; row < this.boardContent.length; row += 2) {
			for (int col = 1; col < this.boardContent[row].length; col += 4) {
				char charValue = (char) (valuePosition + '0');
				if (this.boardContent[row][col] == charValue) {
					this.boardContent[row][col] = valueOfMove;
				}
			}
		}
	}

	public boolean hasSituationOfWinner() {

		char[][] values = getValuesFromBoard();

		if (equalHorisontally() || equalVertically() || equalDiagonally()) {
			return true;
		}

		return false;
	}

	private boolean equalDiagonally() {
		char[][] values = getValuesFromBoard();

		int row = 0, col = 0;

		if ((values[row][col] == values[row + 1][col + 1] && values[row][col] == values[row + 2][col + 2])) {
			return true;
		}

		col = 2;
		if (values[row][col] == values[row + 1][col-1] && values[row][col] == values[row + 2][col-2]) {
			return true;
		}

		return false;
	}

	private boolean equalVertically() {
		char[][] values = getValuesFromBoard();

		int row = 0;
		for (int col = 0; col < values[row].length; col++) {
			if (values[row][col] == values[row + 1][col] && values[row][col] == values[row + 2][col]) {
				return true;
			}
		}

		return false;
	}

	private boolean equalHorisontally() {
		char[][] values = getValuesFromBoard();

		int col = 0;
		for (int row = 0; row < values.length; row++) {
			if (values[row][col] == values[row][col + 1] && values[row][col] == values[row][col + 2]) {
				return true;
			}
		}
		return false;
	}

	private char[][] getValuesFromBoard() {
		char[][] result = new char[3][3];
		int rowIndex = 0;
		int colIndex = 0;
		for (int row = 0; row < this.boardContent.length; row += 2) {
			for (int col = 1; col < this.boardContent[row].length; col += 4) {
				result[rowIndex][colIndex++] = this.boardContent[row][col];
			}
			colIndex = 0;
			rowIndex++;
		}
		return result;
	}

}
