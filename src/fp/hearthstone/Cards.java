package fp.hearthstone;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.text.html.Option;

import fp.utils.ComparableUtils;

public class Cards implements Collection<Card> {
	List<Card> cards;

	public Cards() {
		cards = new ArrayList<Card>();
	}
	
	public Cards(Card card) {
		cards = new ArrayList<Card>();
		cards.add(card);
	}
	
	public Cards(List<Card> cards) {
		this.cards = cards;
	}
	
	public Cards(Stream<Card> cards) {
		this.cards = cards.toList();
	}

	public List<Card> getCards() {
		return cards;
	}
	
	// 2.1. Existe
	public boolean exists(Predicate<Card> filter) { // Note: Equivalent to contains, but is required to be made with a loop.
		for (Card card : cards) {
			if (filter.test(card)) { return true; }
		}
		return false;
	}
	
	// 3.1. Existe con Stream
	public boolean existsStream(Predicate<Card> filter) {
		return cards.stream().anyMatch(filter);
	}
	
	// 2.2. Media
	public <T extends Number> Double average(Function<Card, T> mapper) {
		Double totalAttack = 0.;
		for (Card card : cards) {
			totalAttack += mapper.apply(card).doubleValue(); // map to attribute, then conver to double
		}
		return totalAttack / size();
	}
	
	// 3.2. Media con Stream
	public <T extends Number> Double averageStream(Function<Card, T> mapper) {
		return cards.stream()
			.map(mapper) // map to attribute
			.map(T::doubleValue)
			.reduce(0., Double::sum)
			/ size();
	}
	
	// 2.3. Filtro
	public Cards filter(Predicate<Card> filter) {
		List<Card> filteredList  = new ArrayList<Card>();
		for (Card card : cards) {
			if (filter.test(card)) {
				filteredList.add(card);
			}
		}
		return new Cards(filteredList);
	}
	
	// 3.3. Filtro con Stream
	public Cards filterStream(Predicate<Card> filter) {
		Stream<Card> filteredStream = stream().filter(filter);
		return new Cards(filteredStream);
	}
	
	// 3.4. Mínimo filtrado
	public <T extends Comparable<T>> T filteredMin(Predicate<Card> filter, Function<Card, T> mapper) {
		return stream().filter(filter).map(mapper).min(Comparator.naturalOrder()).get();
	}
	
	// 3.4. Máximo filtrado
	public <T extends Comparable<T>> T filteredMax(Predicate<Card> filter, Function<Card, T> mapper) {
		return stream().filter(filter).map(mapper).max(Comparator.naturalOrder()).get();
	}
	
	// 3.5. Filtrado y ordenación
	public Cards filterAndSort(Predicate<Card> filter) {
		return new Cards(stream().filter(filter).sorted());
	}
	
	public Cards filterAndSort(Predicate<Card> filter, Comparator<Card> comparator) {
		return new Cards(stream().filter(filter).sorted(comparator));
	}
	
	// 2.4. Agrupación
	public <T> Map<T, Cards> groupBy(Function<Card, T> keyMapper) {
		Map<T, Cards> grouped = new HashMap<T, Cards>();
		
		for (Card card : cards) {
			T group = keyMapper.apply(card);
			if (!grouped.containsKey(group)) {
				grouped.put(group, new Cards());
			}
			grouped.get(group).add(card);
		}
		
		return grouped;
	}
	
	// 3.6. Agrupación con Streams
	public <T> Map<T, Cards> groupByStream(Function<Card, T> keyMapper) {
		return stream().collect( Collectors.toMap( keyMapper, Cards::new, Cards::addCards ));
	}

	public <T extends Number> T accumulate(Function<Card, T> mapper, BinaryOperator<T> sumOperator) {
		return stream().map(mapper).reduce(sumOperator).get();
	}
	
	// 2.5. Agrupación y acumulación
	public <T, N extends Number> Map<T, N> accumulateGroups(Function<Card, T> keyMapper, Function<Card, N> valueMapper, BinaryOperator<N> sumOperator) {
		Map<T, Cards> grouped = groupBy(keyMapper);
		Map<T, N> accumulated = new HashMap<T, N>();
		for (Entry<T, Cards> entry : grouped.entrySet()) {
			accumulated.put(entry.getKey(), entry.getValue().accumulate(valueMapper, sumOperator));
		}
		return accumulated;
	}
	
	// 3.7. Usando Collector.mapping
	public <T> List<T> mapToList(Function<Card, T> mapper) {
		return cards.stream().collect(Collectors.mapping(mapper, Collectors.toList()));
	}
	
	// 3.8. Map con los mínimos
	public <T, N extends Comparable<N>> Map<T, N> groupedMin(Function<Card, T> keyMapper, Function<Card, N> valueMapper) {
		return cards.stream().collect( Collectors.toMap( keyMapper, valueMapper, ComparableUtils::min ));
	}
	// 3.8. Map con los máximos
	public <T, N extends Comparable<N>> Map<T, N> groupedMax(Function<Card, T> keyMapper, Function<Card, N> valueMapper) {
		return cards.stream().collect( Collectors.toMap( keyMapper, valueMapper, ComparableUtils::max ));
	}
	
	// 3.9. SortedMap top N
	public <T, N extends Comparable<N>> SortedMap<T, List<N>> groupedTopN(Function<Card, T> keyMapper, Function<Card, N> valueMapper, int topN) {
		return cards.stream().collect(
			Collectors.groupingBy(
				keyMapper,
				TreeMap::new,
				Collectors.collectingAndThen(
					Collectors.toList(),
					cards -> cards.stream().map(valueMapper).sorted().limit(topN).toList()
				)
			)
		);
	}
	
	public <T, N extends Comparable<N>> SortedMap<T, List<N>> groupedTopN(Function<Card, T> keyMapper, Function<Card, N> valueMapper, Comparator<N> comparator, int topN) {
		return cards.stream().collect(
			Collectors.groupingBy(
				keyMapper,
				TreeMap::new,
				Collectors.collectingAndThen(
					Collectors.toList(),
					cards -> cards.stream().map(valueMapper).sorted(comparator).limit(topN).toList()
				)
			)
		);
	}
	
	// 3.10. Devuelve la llave del mayor valor agrupando
	public <T, N extends Comparable<N>> T maxKey (Function<Card, T> keyMapper, Function<Card, N> valueMapper) {
		Map<T, N> groupedMax = groupedMax(keyMapper, valueMapper);
		T maxKey = groupedMax.entrySet().stream().max(Comparator.comparing(Entry::getValue)).get().getKey();
		return maxKey;
	}
	
	public Stream<Card> stream() {
		return cards.stream();
	}
	
	public Card get(int index) {
		return cards.get(index);
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
	
	public static Cards addCards(Cards existing, Cards replacement) {
		existing.addAll(replacement);
		return existing;
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
