package seachess;
import java.util.Scanner;

public class Game {

	private Board board = new Board();

	public Game() {}

	void startGame(Player firstPlayer, Player secondPlayer) {

		if (firstPlayer != null && secondPlayer != null) {
			while (true) {
				
				if(board.isBoardFull()){
					if(foundWinner()){
						System.out.println("Winner found! Congratulations!");
						break;
					}
					else{
						System.out.println("End of the game! There is no winner!");
					}
					break;
				}
				
				if(foundWinner()){
					System.out.println("Winner found! Congratulations!");
					break;
				}
				
				board.printBoard();
				makeAMove(firstPlayer);
				board.printBoard();
				makeAMove(secondPlayer);
				board.printBoard();
			}
		}
	}

	private boolean foundWinner() {
		return this.board.hasSituationOfWinner();
	}

	void makeAMove(Player player) {

		if(this.board.isBoardFull()){
			System.out.println("The board is full!");
			return;
		}
		
		if(foundWinner()){
			System.out.println(player.getName() + ", you have lost the game! ");
			return;
		}
		
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		System.out.println(player.getName() + ", please choose a place to make your move (from 1 to 9): ");
		int valuePosition = 0;

		do {
			valuePosition = scanner.nextInt();

			if (!isValidValue(valuePosition)) {
				System.out.println("Please enter a number between 1 and 9: ");
			}

			if (!isFreePosition(valuePosition)) {
				System.out.println("This position has been already chosen! Try another one!");
			}

		} while (!isValidValue(valuePosition) || !isFreePosition(valuePosition));

		this.board.setPlayersMove(player.getvalueToPlay(), valuePosition);
	}

	private boolean isFreePosition(int value) {
		return this.board.isFreePosition(value);
	}

	private boolean isValidValue(int value) {
		return value >= 1 || value <= 9;
	}

}
