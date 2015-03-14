package me.reckter.Generation.Utilities;

/**
 * Created by reckter on 14.03.2015.
 */
public class Pair<K, V> {
	public K first;
	public V second;

	public Pair(K first, V second) {
		this.first = first;
		this.second = second;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Pair) {
			return (first.equals(((Pair) obj).first) && second.equals(((Pair) obj).second))
					|| (first.equals(((Pair) obj).second) && second.equals(((Pair) obj).first));
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		int result = first.hashCode();
		result = 31 * result + second.hashCode();
		return result;
	}
}
