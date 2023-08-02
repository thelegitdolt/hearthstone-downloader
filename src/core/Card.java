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
/**
 * A class that represents a single Card. Designed to be converted from the api.hearthstone.com jsons.
 * @author Dolt
 */
public class Card {
    /**
     * The rgb value of the placeholder gray color, for cards that do not have art.
     * I don't want any artless cards. This exists so I can filter them out.
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

    /**
     * private constructor; use the Card.Builder instead
     */
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

    /**
     * Two instances of Card are equal if all of their parameters are equal.
     * @param o the object to compare
     * @return true if o is equal to this, false if not
     */
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

    /**
     * Gets the file that this card represents, if you have finished downloading the cards.
     * @return an Optional containing File with the pathname of that image.
     * If this file does not exist, this optional will contain null instead.
     */
    public Optional<File> getImagePath() {
        File file = new File(FileUtil.CARD_FOLDER_FILEPATH + "/" + name + " " + id + ".png");

        return Optional.ofNullable(file.exists() ? file : null);
    }

    /**
     * Using an id, looks up a card in cardList with that id.
     * id should be unique, so there is no confusion to whether you have the right one.
     * you must initialize lists before using.
     * @param id the id
     * @return Optional<Card> </Card> that contains a card with id "id", if it exists.
     */
    public static Optional<Card> lookup(String id) {
        Card op = null;
        for (Card c : cardList) {
            if (NullStringUtil.equals(c.id, id))
                op = c;
        }
        return Optional.ofNullable(op);
    }

    /**
     * Looks at a file that is a downloaded card image, with name using the layout (name + id), and derives its ID.
     * Then calls Card.lookup(String id) using that ID.
     * you must initialize lists before using.
     * @param file the file to look up
     * @return Optional<Card> </Card> that contains said card, if it exists.
     */
    public static Optional<Card> lookup(File file) {
        return lookup(Util.idFromFile(file));
    }


    public String getName() {
        return name;
    }

    /**
     * Checks whether a card is a Mercenary card by seeing if belongs to the Lettuce set.
     * @return true if this card is of CardSet.LETTUCE, false if not.
     */
    public boolean isMercenaries() {
        return this.getSet() == CardSet.LETTUCE;
    }


    /**
     * Checks whether a card is an Enchantment (and therefore won't have an associated card image).
     * @return true if this card is of CardType.ENCHANTMENT.
     * Deprecated; this will extend this to other unused card types.
     */
    @Deprecated
    public boolean isEnchantment() {
        return this.getType() == CardType.ENCHANTMENT;
    }

    /**
     * Checks whether a card is a Puzzle Starting card from the boomsday project puzzle labs (and therefore will have a blank art).
     * Does this by seeing if the text contains "Start X puzzle".
     * @return whether this card contains any of "Start Mirror Puzzle", "Start Board Clear Puzzle", "Start Lethal Puzzle", or "Start Survival Puzzle", AND that it has no artists, AND that is from the Boomsday set
     * Deprecated; a method that determines if a card is artless via Card.PLACEHOLDER_GRAY_RGB is to be implemented.
     */
    @Deprecated
    public boolean isPuzzleLab() {
        boolean isSpeciallyCallingPuzzle = text != null && (text.contains("Start Mirror Puzzle") || text.contains("Start Lethal Puzzle") ||
                text.contains("Start Board Clear Puzzle") || text.contains("Start Survival Puzzle"));
        return this.getSet() == CardSet.BOOMSDAY && isSpeciallyCallingPuzzle && !this.hasArtist();
    }

    /**
     * @return whether a card has an artist.
     * A card with an artist must have some original art.
     */
    public boolean hasArtist() {
        return this.getArtist() != null;
    }

    /**
     * @return whether a card has blank text.
     */
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

    /**
     * toString implementation, has linebreaks
     * I'm pretty sure this is not how you're supposed to do this
     * @return A string containing every field in this card, followed by its value, separated by line breaks
     */
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

    /**
     * Initializes cardList to have every single hearthstone card in the game.
     * You MUST run this to do most logic on this card.
     * @param preds Any cards that do not meet this Predicate will not be included.
     * @throws FileNotFoundException if your FileUtil.CARD_FOLDER_FILEPATH or FileUtil.READTXT_FILEPATH is wrong.
     */
    public static void initializeCardList(Predicate<Card> preds) throws FileNotFoundException {
        cardList = HearthstoneDownloader.processAllCards(FileUtil.READTXT_FILEPATH, preds);
    }

    /**
     * initializeCardList that will contain all cards by default with no filtering.
     * Done by calling Card.initializeCardList(preds) with a predicate that is always true.
     */
    public static void initializeCardList() throws FileNotFoundException {
        initializeCardList(PredsUtil.ALWAYS_TRUE);
    }


    /**
     * Builder class, which is Latin for being smothered in boiler plate
     * Contains every single field in Card, and methods to set them. Basic builder stuff.
     */
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

        /**
         * A builder with all fields set to null. Any ints are set to Integer.MAX_VALUE, cuz why not
         * Use Card.Builder.template() instead
         */
        public Builder() {
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

        /**
         * Apparently you can't name a method class :(
         */
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


        /**
         * Build your card after you are done setting everything.
         */
        public Card build() {
            return new Card(this.cardClass, this.name, this.text, this.cost, this.id, this.artist, this.collectible, this.set, this.attack, this.health, this.type);
        }

        public void fillFromMap(String map) {
            fillFromMap(Objects.requireNonNull(HSStringUtil.extractLine(map)));
        }

        /**
         * Logic to automatically create a Card from lines in a json file.
         */
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
    }
}
