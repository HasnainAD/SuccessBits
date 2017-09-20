package com.hadilawar.successbits.FragmentsHub;

/**
 * Created by Dilawar on 7/1/2017.
 */

public class QuoteData {

    private String authorName;
    private String quote;
    private String imageUrl;
    private String aboutAuthor;

    QuoteData(){}
    QuoteData(String authorName, String quote, String imageUrl, String aboutAuthor){
        this.authorName = authorName;
        this.aboutAuthor = aboutAuthor;
        this.quote = quote;
        this.imageUrl = imageUrl;
    }

    public String getAboutAuthor() {
        return aboutAuthor;
    }

    public void setAboutAuthor(String aboutAuthor) {
        this.aboutAuthor = aboutAuthor;
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
