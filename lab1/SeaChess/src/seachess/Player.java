package seachess;


public class Player {
	
	private char valueToPlay; // X or O;
	private String name;
	
	public Player(char valueToPlay, String name) {
		if(name != null){
			this.valueToPlay = valueToPlay;
			this.name = name;
		}
	}
	
	public String getName() {
		return name;
	}
	
	public char getvalueToPlay() {
		return valueToPlay;
	}

}
