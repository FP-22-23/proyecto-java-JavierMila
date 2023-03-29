package fp.hearthstone;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Stream;

public class Cards implements Collection<Card> {
	List<Card> cards;

	public Cards() {
		cards = new ArrayList<Card>();
	}
	
	public Cards(List<Card> cards) {
		this.cards = cards;
	}
	
	public List<Card> getCards() {
		return cards;
	}
	
	// 1. Existe
	public boolean exists(Function<Card, Boolean> filter) { // Note: Equivalent to contains, but is required to be made with a loop.
		for (Card card : cards) {
			if (filter.apply(card)) { return true; }
		}
		return false;
	}
	
	// 2. Media
	public <T extends Number> Double average(Function<Card, T> key) {
		Double totalAttack = 0.;
		for (Card card : cards) {
			totalAttack += (double) key.apply(card);
		}
		return totalAttack / size();
	}
	
	// 3. Filtro
	public Cards filter(Function<Card, Boolean> filter) {
		List<Card> filteredList  = new ArrayList<Card>();
		for (Card card : cards) {
			if (filter.apply(card)) {
				filteredList.add(card);
			}
		}
		return new Cards(filteredList);
	}
	
	// 4. Agrupación
	public <T> Map<T, Cards> groupedBy(Function<Card, T> groupBy) {
		Map<T, Cards> grouped = new HashMap<T, Cards>();
		
		for (Card card : cards) {
			T group = groupBy.apply(card);
			if (!grouped.containsKey(group)) {
				grouped.put(group, new Cards());
			}
			grouped.get(group).add(card);
		}
		
		return grouped;
	}

	public <T extends Number> T accumulate(Function<Card, T> key, BinaryOperator<T> sumOp) {
		return cards.stream().map(key).reduce(sumOp).orElseThrow();
	}
	
	// 5. Agrupación y acumulación
	public <T, N extends Number> Map<T, N> accumulateGroups(Function<Card, T> groupBy, Function<Card, N> key, BinaryOperator<N> sumOp) {
		Map<T, Cards> grouped = groupedBy(groupBy);
		Map<T, N> accumulated = new HashMap<T, N>();
		for (Entry<T, Cards> entry : grouped.entrySet()) {
			accumulated.put(entry.getKey(), entry.getValue().accumulate(key, sumOp));
		}
		return accumulated;
	}
	
	public Stream<Card> stream() {
		return cards.stream();
	}
	
	// a. Número de elementos
	public int size() {
		return cards.size();
	}
	
	// b. Añadir un elemento
	public boolean add(Card card) {
		return cards.add(card);
	}
	
	// c. Añadir una colección de elementos
	public boolean addAll(Collection<? extends Card> c) {
		return cards.addAll(c);
	}
	
	// d. Eliminar un elemento
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
