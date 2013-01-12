package models.metro;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MetroPage implements Iterable<Card> {
    private List<Card> cards = new ArrayList<Card>();

    private int page;
    private int itemsPerPage;
    private int itemsNum;

    private String query;

    public MetroPage(int page, int itemsPerPage) {
        this.page = page;
        this.itemsPerPage = itemsPerPage;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public int getPage() {
        return page;
    }

    public int getCurrentPage() {
        return page + 1;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getItemsPerPage() {
        return itemsPerPage;
    }

    public void setItemsPerPage(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    public int getItemsNum() {
        return itemsNum;
    }

    public void setItemsNum(int itemsNum) {
        this.itemsNum = itemsNum;
    }

    @Override
    public Iterator<Card> iterator() {
        return cards.iterator();
    }

    public void add(Card card) {
        cards.add(card);
    }

    public int getOffset() {
        return getPage() * getItemsPerPage();
    }

    public void setQuery(String q) {
        this.query = q;
    }

    public String getQuery() {
        return query;
    }

}
