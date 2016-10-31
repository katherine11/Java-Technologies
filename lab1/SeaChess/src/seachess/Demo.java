package seachess;

public class Demo {
	
	public static void main(String[] args) {
		
		Player firstPlayer = new Player('X', "Madlen");
		Player secondPlayer = new Player('0', "Ivan");
		
		Board chess = new Board();
		Game seachess = new Game();
		
		seachess.startGame(firstPlayer, secondPlayer);
	}

}
