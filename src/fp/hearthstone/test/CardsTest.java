package fp.hearthstone.test;

import java.time.chrono.ChronoLocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import fp.common.Rarity;
import fp.hearthstone.Card;
import fp.hearthstone.CardFactory;
import fp.hearthstone.Cards;

public class CardsTest {

	public static void main(String[] args) {
		// Basic properties
		
		Cards first_cards = new Cards(CardFactory.readCards("data/hearthstone_standard_cards.csv"));
		System.out.println("El constructor dos tiene " + first_cards.size() + " cartas.");
		
		Cards second_cards = new Cards();
		System.out.println("El constructor uno tiene " + second_cards.size() + " cartas.");

		System.out.println("¿Se han añadido? " + second_cards.addAll(CardFactory.readCards("data/hearthstone_standard_cards.csv")));
		System.out.println("Ahora el constructor uno tiene " + second_cards.size() + " cartas.");
		
		Cards third_cards = CardFactory.readCardsStream("data/hearthstone_standard_cards.csv");
		System.out.println("El constructor tres tiene " + third_cards.size() + " cartas.");
		
		System.out.println("¿Son iguales? " + first_cards.equals(second_cards) + ", " + first_cards.equals(third_cards));
		
		Card first_card = first_cards.iterator().next();
		
		System.out.println("¿Se ha eliminado? " + first_cards.remove(first_card));
		System.out.println("¿Contiene la eliminada? " + first_cards.contains(first_card));
		System.out.println("¿Se ha añadido? " + first_cards.add(first_card));
		System.out.println("¿Contiene la añadida? " + first_cards.contains(first_card));
		
		// Sequential operations
		
		Cards cards = second_cards;
		System.out.println("¿Existe el Abusive Sergeant?");
		System.out.println(cards.exists(card -> card.getName().equals("Abusive Sergeant")));
		System.out.println(cards.existsStream(card -> card.getName().equals("Abusive Sergeant")));
		
		System.out.println("¿Cuál es el ataque promedio?");
		System.out.println(cards.average(Card::getAttack));
		System.out.println(cards.averageStream(Card::getAttack));
		
		System.out.println("¿Cuál es el ataque promedio de las cartas con rareza legendaria?");
		System.out.println(
			cards.filter(card -> card.getRarity() == Rarity.LEGENDARY)
			.average(Card::getAttack)
		);
		System.out.println(
				cards.filterStream(card -> card.getRarity() == Rarity.LEGENDARY)
				.averageStream(Card::getAttack)
		);
		
		System.out.println("¿Cuál es la primera fecha de lanzamiento de una carta legendaria?");
		System.out.println(cards.filteredMin(card -> card.getRarity() == Rarity.LEGENDARY, Card::getReleaseDate));
		System.out.println("¿Cuál es la última fecha de lanzamiento de una carta legendaria?");
		System.out.println(cards.filteredMax(card -> card.getRarity() == Rarity.LEGENDARY, Card::getReleaseDate));
		
		System.out.println("¿Cuál es la menor carta coleccionable según el orden natural?");
		System.out.println(cards.filterAndSort(Card::getCollectible).get(0));
		
		System.out.println("¿Cuántas cartas hay de cada rareza?");
		Map<Rarity, Cards> grouped = cards.groupBy(Card::getRarity);
		Map<Rarity, Cards> groupedByStream = cards.groupByStream(Card::getRarity);
		for (Rarity key : grouped.keySet()) {
			System.out.println(key + ": " + grouped.get(key).size() + ", " + groupedByStream.get(key).size());
		}
		
		System.out.println("¿Cuántas cartas multiclase hay de cada rareza?");
		Map<Rarity, Integer> accumulatedGroups = cards.accumulateGroups(
			Card::getRarity,
			card -> (card.getClass_ids().size() > 1) ? 1:0,
			Integer::sum
		);
		for (Entry<Rarity, Integer> entry : accumulatedGroups.entrySet()) {
			System.out.println(entry.getKey() + ": " + entry.getValue());
		}
		
		System.out.println("¿Cuál es el ataque promedio? (usando mapToList)");
		System.out.println(cards.mapToList(Card::getAttack).stream().reduce(0., Double::sum) / cards.size());
		
		System.out.println("¿Cuándo salieron la primera y última carta de cada rareza?");
		Map<Rarity, ChronoLocalDate> groupedMin = cards.groupedMin(Card::getRarity, Card::getReleaseDate);
		Map<Rarity, ChronoLocalDate> groupedMax = cards.groupedMax(Card::getRarity, Card::getReleaseDate);
		for (Rarity key : groupedMin.keySet()) {
			System.out.println(key + ": " + groupedMin.get(key) + " y " + groupedMax.get(key));
		}
		
		System.out.println("¿Cuáles son los menores 3 ataques de cada rareza?");
		Map<Rarity, List<Double>> min3Attack = cards.groupedTopN(Card::getRarity, Card::getAttack, 3);
		for (Entry<Rarity, List<Double>> entry : min3Attack.entrySet()) {
			System.out.println(entry.getKey() + ": " + entry.getValue());
		}
		System.out.println("¿Cuáles son los mayores 3 ataques de cada rareza?");
		Map<Rarity, List<Double>> top3Attack = cards.groupedTopN(Card::getRarity, Card::getAttack, Comparator.reverseOrder(), 3);
		for (Entry<Rarity, List<Double>> entry : top3Attack.entrySet()) {
			System.out.println(entry.getKey() + ": " + entry.getValue());
		}
		
		System.out.println("¿Cuál es la rareza de la carta más reciente del juego?");
		System.out.println(cards.maxKey(Card::getRarity, Card::getReleaseDate));
		
	}

}
