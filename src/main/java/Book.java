public class Book {
    public int id;
    public String title;
    public String author;
    public String country;
    public String language;
    public String link;
    public int pages;
    public int year;

    public Book(int id, String title, String author, String country, String language, String link, int pages, int year) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.country = country;
        this.language = language;
        this.link = link;
        this.pages = pages;
        this.year = year;
    }
}
