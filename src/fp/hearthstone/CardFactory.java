package fp.hearthstone;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fp.utils.Checkers;

public class CardFactory {
	public static List<Card> readCards(String filePath) {
		
		List<Card> result = new ArrayList<Card>();
		
		try {
			Stream<Card> cards = 
				Files.lines(Paths.get(filePath)).
				skip(1).
				map(CardFactory::parseCard);
			result = cards.collect(Collectors.toList());
		} catch (IOException e) {
			System.out.println(filePath + " not found.");
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static Cards readCardsStream(String filePath) {
		Cards result = null;
		try {
			Stream<Card> cards = 
				Files.lines(Paths.get(filePath)).
				skip(1).
				map(CardFactory::parseCard);
			result = new Cards(cards);
		} catch (IOException e) {
			System.out.println(filePath + " not found.");
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static Card parseCard(String line) {
		String[] parts = line.split("·");
		Checkers.check("Should have 15 camps", parts.length == 15);
		Integer id = Integer.parseInt(parts[0].strip());
		Integer collectibleInt = Integer.parseInt(parts[1].strip());
		Set<Integer> classIds = new HashSet<Integer>();
		Integer primaryClassId = Integer.parseInt(parts[2].strip());
		classIds.add(primaryClassId);
		String secondaryClassesString = parts[3].substring(1, parts[3].length()-1);
		if (!secondaryClassesString.isBlank()) {
			for (String secondaryClass : secondaryClassesString.split(",")) {
				classIds.add(Integer.parseInt(secondaryClass.strip()));
			}
		}
		Integer rarityInt = Integer.parseInt(parts[4].strip());
		Integer manaCost = Integer.parseInt(parts[5].strip());
		String healthString = parts[6].strip();
		Double health;
		if (healthString.isBlank()) {
			health = 0.;
		} else {
			health = Double.parseDouble(healthString);
		}
		String attackString = parts[7].strip();
		Double attack;
		if (attackString.isBlank()) {
			attack = 0.;
		} else {
			attack = Double.parseDouble(attackString);
		}
		String name = parts[8].strip();
		String text = parts[9].strip();
		String flavorText = parts[10].strip();
		String image = parts[11].strip();
		String crop_image = parts[12].strip();
		String artist_name = parts[13].strip();
		LocalDate releaseDate = LocalDate.parse(parts[14].strip());
		
		return new Card(id, collectibleInt, classIds, rarityInt, manaCost, health, attack, name, text, flavorText, image, crop_image, artist_name, releaseDate);
	}

}
