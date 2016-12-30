import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Game<T extends Comparable<?>> {

	private byte objectLength;
	private Set<T> objectToBeGuessed = new LinkedHashSet<>();

	public Game(byte length) {

		if (length > 0) {
			this.objectLength = length;
		}

		setObjectToBeGuessed(objectToBeGuessed);
	}

	public void setObjectToBeGuessed(Set<T> objectToBeGuessed) {
		this.objectToBeGuessed = objectToBeGuessed;
	}

	Map.Entry<Byte, Byte> check(Set<T> guess) throws GameException {

		if (guess != null) {

			byte bulls = 0;
			byte cows = 0;

			for (T element : guess) {

				if (objectToBeGuessed.contains(element)) {
					cows++;
				}

				for (T object : objectToBeGuessed) {
					if (element.equals(object)) {
						bulls++;
						break;
					}
				}
			}

			Map.Entry<Byte, Byte> result;
			return result = getResult(bulls, cows);

		}
		throw new GameException("The given guess is not valid!");
	}

	private Entry<Byte, Byte> getResult(byte bulls, byte cows) {

		Map.Entry<Byte, Byte> result = new Entry<Byte, Byte>() {

			@Override
			public Byte setValue(Byte value) {

				return value;
			}

			@Override
			public Byte getValue() {

				return cows;
			}

			@Override
			public Byte getKey() {
				return bulls;
			}
		};

		return result;
	}

}
