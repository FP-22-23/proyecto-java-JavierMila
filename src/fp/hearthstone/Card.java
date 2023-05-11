package fp.hearthstone;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

import fp.common.Graphics;
import fp.common.Rarity;
import fp.utils.Checkers;

public class Card implements Comparable<Card> {
	private Integer id;
	private Boolean collectible;
	private Set<Integer> classIds; // Tipo conjunto
	private Rarity rarity; // Tipo auxiliar
	private Integer manaCost;
	private Double health;
	private Double attack;
	private String name;
	private String text;	
	private String flavorText;
	private Graphics graphics;
	private LocalDate releaseDate;
	
	// 1.1. Dos constructores
	public Card(Integer id, Boolean collectible, Set<Integer> classIds, Rarity rarity, Integer manaCost, Double health, Double attack, String name, String text, String flavorText, Graphics graphics, LocalDate releaseDate) {
		Checkers.check("ID can't be negative", id >= 0); // 1.6. Restricción
		this.id = id;
		this.collectible = collectible;
		Checkers.check("Must have a class.", classIds.size() > 0); // 1.6. Restricción
		this.classIds = classIds;
		this.rarity = rarity;
		this.manaCost = manaCost;
		this.health = health;
		this.attack = attack;
		Checkers.check("Must have a name.", name.length() > 0); // 1.6. Restricción
		this.name = name;
		this.text = text;
		this.flavorText = flavorText;
		this.graphics = graphics;
		this.releaseDate = releaseDate;
	}
	
	public Card(Integer id, Integer collectibleInt, Set<Integer> classIds, Integer rarityInt, Integer manaCost, Double health, Double attack, String name, String text, String flavorText, String image, String crop_image, String artist_name, LocalDate releaseDate) {
		Checkers.check("ID can't be negative", id >= 0); // 1.6. Restricción
		this.id = id;
		this.collectible = collectibleInt.equals(1);
		Checkers.check("Must have a class.", classIds.size() > 0); // 1.6. Restricción
		this.classIds = classIds;
		switch (rarityInt) {
		case 1: this.rarity = Rarity.COMMON; break;
		case 2: this.rarity = Rarity.FREE; break;
		case 3: this.rarity = Rarity.RARE; break;
		case 4: this.rarity = Rarity.EPIC; break;
		default: this.rarity = Rarity.LEGENDARY; break;
		}
		this.manaCost = manaCost;
		this.health = health;
		this.attack = attack;
		Checkers.check("Must have a name.", name.length() > 0); // 1.6. Restricción
		this.name = name;
		this.text = text;
		this.flavorText = flavorText;
		this.graphics = new Graphics(image, crop_image, artist_name);
		this.releaseDate = releaseDate;
	}

	public Integer getId() {
		return id;
	}

	public Boolean getCollectible() {
		return collectible;
	}
	
	public String getSlug() { // 1.2. Propiedad derivada
		return id.toString() + "-" + name;
	}


	public Set<Integer> getClass_ids() {
		return classIds;
	}

	public Rarity getRarity() {
		return rarity;
	}

	public Integer getmanaCost() {
		return manaCost;
	}
	
	public Double getHealth() {
		return health;
	}

	public Double getAttack() {
		return attack;
	}

	public String getName() {
		return name;
	}

	public String getText() {
		return text;
	}

	public String getflavorText() {
		return flavorText;
	}

	public Graphics getGraphics() {
		return graphics;
	}
	
	public LocalDate getReleaseDate() {
		return releaseDate;
	}

	@Override
	public String toString() { // 1.3. Representación como cadena
		return "Card [id=" + id + ", rarity=" + rarity + ", name=" + name + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, rarity);
	}

	@Override
	public boolean equals(Object obj) { // 1.4. Criterio de igualdad
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Card other = (Card) obj;
		return Objects.equals(id, other.id) && rarity == other.rarity;
	}

	public int compareTo(Card card) { // 1.5. Orden natural
		
		int res = getRarity().compareTo(card.getRarity());
		if (res == 0) {
			res = getId().compareTo(card.getId());
		}
		
		return res;
		
	}
}
