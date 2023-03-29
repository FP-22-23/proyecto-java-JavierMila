package fp.hearthstone.test;

import java.util.List;

import fp.hearthstone.Card;
import fp.hearthstone.CardFactory;

public class CardFactoryTest {

	public static void main(String[] args) {
		List<Card> cards = CardFactory.readCards("data/hearthstone_standard_cards.csv");
		System.out.println("Read " + cards.size() + " cards.");
		System.out.println("Showing first 5 cards: ");
		for ( Card card : cards.subList(0, 4) ) {
			System.out.println(card.toString());
		}
	}

}
