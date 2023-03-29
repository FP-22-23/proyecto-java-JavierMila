package fp.hearthstone.test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import fp.common.Graphics;
import fp.common.Rarity;
import fp.hearthstone.Card;

public class CardTest {

	public static void main(String[] args) {
		Card card1 = new Card(1, false, Set.of(1, 2, 3), Rarity.COMMON, 3, 1.0, 1.0, "Juan", "Destruye el mundo", "¡A la batalla!",
				new Graphics("Juan García", "link.com", "link.com"), LocalDate.now());
			Card card2 = new Card(1, 0, Set.of(1, 2, 3), 1, 3, 1.0, 1.0, "Juan", "Destruye el mundo", "¡A la batalla!",
					"Juan García", "link.com", "link.com", LocalDate.now());
			Card card3 = new Card(1, false, Set.of(1, 2, 3), Rarity.RARE, 3, 1.0, 1.0, "Juan", "Destruye el mundo", "¡A la batalla!",
					new Graphics("Juan García", "link.com", "link.com"), LocalDate.now());
			System.out.println("Card 1: " + card1.toString());
			System.out.println("Card 2: " + card2.toString());
			System.out.println("Card 3: " + card3.toString());
			System.out.println("Card1 == Card2: " + card1.equals(card2));
			System.out.println("Card1 == Card2: " + (card1.hashCode() == card2.hashCode()));
			System.out.println("Card1 == Card3: " + card1.equals(card3));
			System.out.println("Card1 cmp Card2: " + card1.compareTo(card2));
			System.out.println("Card1 cmp Card3: " + card1.compareTo(card3));
			System.out.println("Card 1: " + card1.getSlug());
	}

}
