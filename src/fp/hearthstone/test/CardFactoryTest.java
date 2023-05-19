package fp.hearthstone.test;

import java.util.List;

import fp.hearthstone.Card;
import fp.hearthstone.CardFactory;
import fp.hearthstone.Cards;

public class CardFactoryTest {

	public static void main(String[] args) {
		System.out.println("Testing readCards");
		List<Card> cardList = CardFactory.readCards("data/hearthstone_standard_cards.csv");
		System.out.println("Read " + cardList.size() + " cards.");
		System.out.println("Showing first 5 cards: ");
		for ( Card card : cardList.subList(0, 4) ) {
			System.out.println(card.toString());
		}
		
		System.out.println("Testing readCardsStream");
		Cards cards = CardFactory.readCardsStream("data/hearthstone_standard_cards.csv");
		System.out.println("Read " + cards.size() + " cards.");
		System.out.println("Showing first 5 cards: ");
		for ( Card card : cards.subList(0, 4) ) {
			System.out.println(card.toString());
		}
	}

}
