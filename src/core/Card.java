package core;

import datafixers.Pair;
import util.*;
import values.CardClass;
import values.CardSet;
import values.CardType;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import static core.CardDownload.TXT_TO_READ;

/**
 * A class that represents a single Card. Designed to be converted from the api.hearthstone.com jsons.
 * @author Dolt
 */
public class Card {
    /**
     * The rgb value of the placeholder color, for cards that do not have art.
     *
     */
    public static final int PLACEHOLDER_GRAY_RGB = -5927560;

    private final CardClass cardClass;
    private final String name;
    private final String text;
    private final Integer cost;
    private final String id;
    private final String artist;
    private final boolean collectible;
    private final CardSet set;
    private final Integer attack;
    private final Integer health;
    private final CardType type;
    public static List<Card> cardList = new ArrayList<>();

    public static final Card NULL_CARD = new Card(null, null, null, Integer.MAX_VALUE, null, null, false, null, Integer.MAX_VALUE, Integer.MAX_VALUE, null);

    private Card(CardClass cardClass, String name, String text, int cost, String id, String artist, boolean collectible, CardSet set, int attack, int health, CardType type) {
        this.cardClass = cardClass;
        this.name = name;
        this.text = text;
        this.cost = cost;
        this.id = id;
        this.artist = artist;
        this.collectible = collectible;
        this.set = set;
        this.attack = attack;
        this.type = type;
        this.health = health;
    }

    public boolean isLootCard() {
        boolean isCost0 = this.getCost() == 0;
        boolean hasNoArtist = !this.hasArtist();
        boolean hasText = !this.hasText();
        boolean isSpell = this.getType() == CardType.SPELL;

        return isCost0 && hasNoArtist && hasText && isSpell;
    }

    public CardType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return collectible == card.collectible && cardClass == card.cardClass && Objects.equals(name, card.name) && Objects.equals(text, card.text) && Objects.equals(cost, card.cost) && Objects.equals(id, card.id) && Objects.equals(artist, card.artist) && set == card.set && Objects.equals(attack, card.attack) && Objects.equals(health, card.health) && type == card.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardClass, name, text, cost, id, artist, collectible, set, attack, health, type);
    }

    public File getImage() {
        return new File(FileUtil.CARD_FOLDER_FILEPATH + "/" + name + " " + id + ".png");
    }

    public static Optional<Card> lookup(File file) {
        return lookup(Util.idFromFile(file));
    }

    public static Optional<Card> lookup(String id) {
        Card op = null;
        for (Card c : cardList) {
            if (NullStringUtil.equals(c.id, id))
                op = c;
        }
        return Optional.ofNullable(op);
    }

    public String getName() {
        return name;
    }

    public boolean isMercenaries() {
        return this.getSet() == CardSet.LETTUCE;
    }


    public boolean isEnchantment() {
        return this.getType() == CardType.ENCHANTMENT;
    }

    public boolean isPuzzleLab() {
        boolean isSpeciallyCallingPuzzle = text != null && (text.contains("Start Mirror Puzzle") || text.contains("Start Lethal Puzzle") ||
                text.contains("Start Board Clear Puzzle") || text.contains("Start Survival Puzzle"));
        return this.getSet() == CardSet.BOOMSDAY && isSpeciallyCallingPuzzle && !this.hasArtist();
    }


    public boolean hasArtist() {
        return this.getArtist() != null;
    }

    public boolean hasText() {
        return this.getText() != null;
    }

    public CardClass getCardClass() {
        return cardClass;
    }

    public boolean isCollectible() {
        return collectible;
    }

    public Integer getAttack() {
        return attack;
    }

    public Integer getCost() {
        return cost;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (Field field : this.getClass().getDeclaredFields()) {
            try {
                if (!Modifier.isStatic(field.getModifiers())) {
                    boolean access = field.canAccess(this);
                    field.setAccessible(true);
                    str.append(field.getName()).append(": ").append(field.get(this)).append("\n");
                    field.setAccessible(access);
                }
            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return str.toString();
    }

    public Integer getHealth() {
        return health;
    }

    public String getArtist() {
        return artist;
    }

    public String getId() {
        return id;
    }

    public CardSet getSet() {
        return set;
    }

    public String getText() {
        return text;
    }

    public static void initializeCardList() throws FileNotFoundException {
        initializeCardList(PredsUtil.ALWAYS_TRUE);
    }

    public static void initializeCardList(Predicate<Card> preds) throws FileNotFoundException {
        cardList = CardDownload.processAllCards(TXT_TO_READ, preds);
    }


    public static class Builder {
        private CardClass cardClass;
        private String name;
        private String text;
        private Integer cost;
        private String id;
        private String artist;
        private boolean collectible;
        private CardSet set;
        private Integer attack;
        private Integer health;
        private CardType type;

        private Builder() {
            this.cardClass = null;
            this.name = null;
            this.text = null;
            this.cost = Integer.MAX_VALUE;
            this.id = null;
            this.artist = null;
            this.collectible = false;
            this.set = null;
            this.attack = Integer.MAX_VALUE;
            this.health = Integer.MAX_VALUE;
            this.type = null;
        }

        public static Builder template() {
            return new Builder();
        }

        public Card.Builder ofClass(CardClass cc) {
            this.cardClass = cc;
            return this;
        }


        public Card.Builder name(String entry) {
            this.name = entry;
            return this;
        }

        public Card.Builder text(String entry) {
            this.text = entry;
            return this;
        }

        public Card.Builder cost(int entry) {
            this.cost = entry;
            return this;
        }

        public Card.Builder id(String entry) {
            this.id = entry;
            return this;
        }

        public Card.Builder artist(String entry) {
            this.artist = entry;
            return this;
        }

        public Card.Builder collectible(boolean entry) {
            this.collectible = entry;
            return this;
        }

        public Card.Builder set(CardSet entry) {
            this.set = entry;
            return this;
        }

        public Card.Builder attack(int entry) {
            this.attack = entry;
            return this;
        }

        public Card.Builder health(int entry) {
            this.health = entry;
            return this;
        }

        public Card.Builder type(CardType type) {
            this.type = type;
            return this;
        }

        public void fillFromMap(String map) {
            fillFromMap(Objects.requireNonNull(HSStringUtil.extractLine(map)));
        }

        public void fillFromMap(Pair<String, String> map) {
            String key = map.getFirst();
            String value = map.getSecond();

            switch (key) {
                case "cardClass" -> this.ofClass(CardClass.valueOf(value));
                case "name" -> this.name(value);
                case "id" -> this.id(value);
                case "text" -> this.text(value);
                case "cost" -> this.cost(Integer.parseInt(value));
                case "artist" -> this.artist(value);
                case "attack" -> this.attack(Integer.parseInt(value));
                case "health" -> this.health(Integer.parseInt(value));
                case "collectible" -> this.collectible(Boolean.parseBoolean(value));
                case "set" -> this.set(CardSet.valueOf(value));
                case "type" -> this.type(CardType.valueOf(value));
            }

        }

        public Card build() {
            return new Card(this.cardClass, this.name, this.text, this.cost, this.id, this.artist, this.collectible, this.set, this.attack, this.health, this.type);
        }
    }
}
