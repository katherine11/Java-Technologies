import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Game<T extends Comparable<?>>{
	
	private byte objectLength;
	private Result result;
	private Set<T> objectToBeGuessed = new LinkedHashSet<>();
	
	public Game(byte length, Set<T> objectToBeGuessed) {
		
		if(length > 0){
			this.objectLength = length;
		}
		
		if(objectToBeGuessed != null){
			this.objectToBeGuessed.addAll(objectToBeGuessed);
		}
		
	}
	
	Result check(Set<T> objectToBeGuessed){
		
		if(objectToBeGuessed != null){
			
			List<T> listOfObjects = new ArrayList<>(objectToBeGuessed);
			List<T> listOfObjectsToBeGuessed = new ArrayList<>(this.objectToBeGuessed);
			
			byte bulls = 0;
			byte cows = 0;
			
			for (Iterator iterator = listOfObjectsToBeGuessed.iterator(); iterator.hasNext();iterator.next()) {
			
				
				
			}
			
			
			
		}
			
	
		return null;
	}
	


	
	
	

}
