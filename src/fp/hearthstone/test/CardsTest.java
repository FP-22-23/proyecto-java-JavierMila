package fp.hearthstone.test;

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
		System.out.println("El constructor dos tiene: " + first_cards.size());
		
		Cards second_cards = new Cards();
		System.out.println("El constructor uno tiene: " + second_cards.size());
		System.out.println("Tiene: " + second_cards.toString());

		System.out.println("¿Se han añadido? " + second_cards.addAll(CardFactory.readCards("data/hearthstone_standard_cards.csv")));
		System.out.println("Ahora el constructor uno tiene: " + second_cards.size());
		
		System.out.println("¿Son iguales? " + first_cards.equals(second_cards));
		
		Card first_card = first_cards.iterator().next();
		
		System.out.println("¿Se ha eliminado? " + first_cards.remove(first_card));
		System.out.println("¿Contiene la eliminada? " + first_cards.contains(first_card));
		System.out.println("¿Se ha añadido? " + first_cards.add(first_card));
		System.out.println("¿Contiene la añadida? " + first_cards.contains(first_card));
		
		// Sequential operations
		
		Cards cards = first_cards;
		System.out.println("¿Existe el Abusive Sergeant?");
		System.out.println(cards.exists(card -> card.getName().equals("Abusive Sergeant")));
		
		System.out.println("¿Cuál es el ataque promedio?");
		System.out.println(cards.average(card -> card.getAttack()));
		
		System.out.println("¿Cuál es el ataque promedio de las cartas con rareza legendaria?");
		System.out.println(cards.filter(card -> card.getRarity() == Rarity.LEGENDARY).average(card -> card.getAttack()));
		
		System.out.println("¿Cuántas cartas hay de cada rareza?");
		Map<Rarity, Cards> grouped = cards.groupedBy(card -> card.getRarity());
		for (Entry<Rarity, Cards> entry : grouped.entrySet()) {
			System.out.println(entry.getKey() + ": " + entry.getValue().size());
		}
		
		System.out.println("¿Cuántas cartas multiclase hay de cada rareza?");
		Map<Rarity, Integer> accumulatedGroups = cards.accumulateGroups(
			card -> card.getRarity(),
			card -> (card.getClass_ids().size() > 1) ? 1:0,
			Integer::sum
		);
		for (Entry<Rarity, Integer> entry : accumulatedGroups.entrySet()) {
			System.out.println(entry.getKey() + ": " + entry.getValue());
		}
	}

}
