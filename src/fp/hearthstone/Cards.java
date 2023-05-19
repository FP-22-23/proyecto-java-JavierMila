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
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fp.utils.ComparableUtils;

public class Cards implements Collection<Card> {
	private List<Card> cards;

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
	public boolean exists(Predicate<Card> filter) {
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
	public <N extends Number> Double average(Function<Card, N> mapper) {
		Double totalAttack = 0.;
		for (Card card : cards) {
			totalAttack += mapper.apply(card).doubleValue(); // map to attribute, then convert to double
		}
		return totalAttack / size();
	}
	
	// 3.2. Media con Stream
	public <N extends Number> Double averageStream(Function<Card, N> mapper) {
		return cards.stream()
			.map(mapper) // map to attribute
			.map(N::doubleValue) // then convert to double
			.reduce(0., Double::sum)
			/ size(); // divide by size to average
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
		return stream()
			.filter(filter)
			.map(mapper)
			.min(Comparator.naturalOrder()).get();
	}
	
	// 3.4. Máximo filtrado
	public <T extends Comparable<T>> T filteredMax(Predicate<Card> filter, Function<Card, T> mapper) {
		return stream()
			.filter(filter)
			.map(mapper)
			.max(Comparator.naturalOrder()).get();
	}
	
	// 3.5. Filtrado y ordenación
	public Cards filterAndSort(Predicate<Card> filter) {
		return filterAndSort(filter, Comparator.naturalOrder());
	}
	
	public Cards filterAndSort(Predicate<Card> filter, Comparator<Card> comparator) {
		return new Cards(stream().filter(filter).sorted(comparator));
	}
	
	// 2.4. Agrupación
	public <K> Map<K, Cards> groupBy(Function<Card, K> keyMapper) {
		Map<K, Cards> grouped = new HashMap<K, Cards>();
		
		for (Card card : cards) {
			K key = keyMapper.apply(card); 
			if (!grouped.containsKey(key)) { // if it doesn't contain the key, create a list
				grouped.put(key, new Cards());
			}
			grouped.get(key).add(card); // if it does, add the card to it
		}
		
		return grouped;
	}
	
	// 3.6. Agrupación con Streams
	public <K> Map<K, Cards> groupByStream(Function<Card, K> keyMapper) {
		return stream().collect( Collectors.toMap(keyMapper, Cards::new, Cards::addCards ));
	}

	public <T> T accumulate(Function<Card, T> mapper, BinaryOperator<T> sumOperator) {
		return stream().map(mapper).reduce(sumOperator).get();
	}
	
	// 2.5. Agrupación y acumulación
	public <K, V> Map<K, V> accumulateGroups(Function<Card, K> keyMapper, Function<Card, V> valueMapper, BinaryOperator<V> sumOperator) {
		Map<K, Cards> grouped = groupBy(keyMapper);
		Map<K, V> accumulated = new HashMap<K, V>();
		for (Entry<K, Cards> entry : grouped.entrySet()) { // For each key
			accumulated.put(entry.getKey(), entry.getValue().accumulate(valueMapper, sumOperator)); // Accumulate the values
		}
		return accumulated;
	}
	
	// 3.7. Usando Collector.mapping
	public <T> List<T> mapToList(Function<Card, T> mapper) {
		return cards.stream().collect(Collectors.mapping(mapper, Collectors.toList()));
	}
	
	// 3.8. Map con los mínimos
	public <K, V extends Comparable<V>> Map<K, V> groupedMin(Function<Card, K> keyMapper, Function<Card, V> valueMapper) {
		return cards.stream().collect( Collectors.toMap( keyMapper, valueMapper, ComparableUtils::min ));
	}
	// 3.8. Map con los máximos
	public <K, V extends Comparable<V>> Map<K, V> groupedMax(Function<Card, K> keyMapper, Function<Card, V> valueMapper) {
		return cards.stream().collect( Collectors.toMap( keyMapper, valueMapper, ComparableUtils::max ));
	}
	
	// 3.9. SortedMap top N
	public <K, V extends Comparable<V>> SortedMap<K, List<V>> groupedTopN(Function<Card, K> keyMapper, Function<Card, V> valueMapper, int topN) {
		return groupedTopN(keyMapper, valueMapper, Comparator.naturalOrder(), topN);
	}
	
	public <K, V extends Comparable<V>> SortedMap<K, List<V>> groupedTopN(Function<Card, K> keyMapper, Function<Card, V> valueMapper, Comparator<V> comparator, int topN) {
		return cards.stream().collect(
			Collectors.groupingBy( // can't use Collectors.toMap() because it's a SortedMap
				keyMapper,
				TreeMap::new, // needed to make a SortedMap
				Collectors.collectingAndThen(
					Collectors.toList(), // After collecting to list,
					cards -> cards.stream().map(valueMapper).sorted(comparator).limit(topN).toList() // keep top N after mapping
				)
			)
		);
	}
	
	// 3.10. Devuelve la llave del mayor valor agrupando
	public <K, V extends Comparable<V>> K maxKey (Function<Card, K> keyMapper, Function<Card, V> valueMapper) {
		Map<K, V> groupedMax = groupedMax(keyMapper, valueMapper);
		K maxKey = groupedMax.entrySet().stream() // Get entry stream
			.max(Comparator.comparing(Entry::getValue)) // Get maximum value
			.get().getKey(); // Get corresponding key
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
	
	public static Cards addCards(Cards a, Cards b) {
		Cards newCards = new Cards(a.getCards());
		newCards.addAll(b);
		return newCards;
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
	
	public List<Card> subList(int fromIndex, int toIndex) {
		return cards.subList(fromIndex, toIndex);
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
