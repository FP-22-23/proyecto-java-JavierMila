package fp.hearthstone;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class Cards implements Collection<Card> {
	List<Card> cards;

	public Cards() {
		cards = new ArrayList<Card>();
	}
	
	public Cards(List<Card> cards) {
		this.cards = cards;
	}
	
	public int size() {
		return cards.size();
	}
	
	public boolean add(Card card) {
		return cards.add(card);
	}
	
	public boolean addAll(Collection<? extends Card> c) {
		return cards.addAll(c);
	}
	
	public boolean remove(Object o) {
		return cards.remove(o);
	}
	
	public boolean removeAll(Collection<?> c) {
		return cards.removeAll(c);
	}
	
	public boolean contains(Object o) {
		return cards.contains(o);
	}
	
	public boolean containsAll(Collection<?> c) {
		return cards.containsAll(c);
	}
	
	public boolean isEmpty() {
		return cards.isEmpty();
	}
	
	public boolean retainAll(Collection<?> c) {
		return cards.retainAll(c);
	}
	
	public void clear() {
		cards.clear();
	}

	public Iterator<Card> iterator() {
		return cards.iterator();
	}

	public Object[] toArray() {
		return cards.toArray();
	}

	public <T> T[] toArray(T[] a) {
		return cards.toArray(a);
	}
	
	public int hashCode() {
		return Objects.hash(cards);
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cards other = (Cards) obj;
		return Objects.equals(cards, other.cards);
	}

	
	public String toString() {
		return "Cards [cards=" + cards + "]";
	}
	
	
	
}
