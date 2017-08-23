package com.hadilawar.successbits.FavoritesHub;

/**
 * Created by l1s14bscs2083 on 5/13/2017.
 */

public class FavItem {

    private String author;
    private String quote;
    private int id;


   public FavItem(){}

   public FavItem(String author,  String quote, int id) {
        this.quote = quote;
        this.id = id;
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }
}
