package InformationClasses;

import java.io.Serializable;

public class Book extends LibraryInfo implements Serializable  {
    private String authors;
    private String title;
    private int publishingYear;
    private int pagesNumber;

    public int getPagesNumber() {
        return pagesNumber;
    }

    public int getPublishingYear() {
        return publishingYear;
    }

    public String getAuthors() {
        return authors;
    }

    public String getTitle() {
        return title;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public void setPagesNumber(int pagesNumber) throws BadFieldsException {
        if (pagesNumber <= 0) {
            throw (new BadFieldsException(" Invalid page count "));
        }
        this.pagesNumber = pagesNumber;
    }

    public void setPublishingYear(int publishingYear) throws BadFieldsException {
        if (publishingYear <= 0) {
            throw (new BadFieldsException(" Invalid publishing year "));
        }
        this.publishingYear = publishingYear;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Book(String authors, String title, int publishingYear, int pagesNumber) throws BadFieldsException {
        super();
        this.setAuthors(authors);
        this.setPagesNumber(pagesNumber);
        this.setPublishingYear(publishingYear);
        this.setTitle(title);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Book) {
            if (obj.hashCode() == hashCode()) {
                Book toEq = (Book) obj;
                return authors.equals(toEq.authors) && title.equals(toEq.title) && (publishingYear == toEq.publishingYear)
                        && (pagesNumber == toEq.pagesNumber);
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Book(Authors: " + authors + " Title: " + title + " Pub. Year: " + publishingYear +
                " Number of pages: " + pagesNumber + ")";
    }

    @Override
    public int hashCode() {
        return authors.hashCode() ^ title.hashCode() ^ pagesNumber ^ publishingYear;
    }


}
