package com.hadilawar.successbits;

/**
 * Created by Dilawar on 7/1/2017.
 */

public class QuoteData {

    private String authorName;
    private String quote;
    private String imageUrl;

    QuoteData(){}
    QuoteData(String authorName, String quote, String imageUrl){
        this.authorName = authorName;
        this.quote = quote;
        this.imageUrl = imageUrl;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
