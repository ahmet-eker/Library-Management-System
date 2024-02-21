import java.time.LocalDate;

public class Book {

    /**
     * @param bookId
     * @param bookType
     */
    public Book(int bookId, String bookType) {
        this.bookId = bookId;
        this.bookType = bookType;
    }
    private int bookId;
    private int ownerId = 0;
    private String bookType;
    public boolean isborrowed = false;
    public boolean isReadInLibrary = false;
    public LocalDate borrowedDate = null;
    public LocalDate returnedDate = null;

    /**
     * @return borrowedDate
     */
    public LocalDate getBorrowedDate() {
        return borrowedDate;
    }

    /**
     * @param borrowedDate
     * sets the borrowedDate
     */
    public void setBorrowedDate(LocalDate borrowedDate) {
        this.borrowedDate = borrowedDate;
    }

    /**
     * @return returnedDate
     */
    public LocalDate getReturnedDate() {
        return returnedDate;
    }

    /**
     * @param returnedDate
     * sets the returnedDate
     */
    public void setReturnedDate(LocalDate returnedDate) {
        this.returnedDate = returnedDate;
    }

    /**
     * @return bookType
     */
    public String getBookType() {
        return bookType;
    }

    /**
     * @return bookId
     */
    public int getBookId() {
        return bookId;
    }

    /**
     * @param bookId
     * sets bookId
     */
    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    /**
     * @return ownerId
     */
    public int getOwnerId() {
        return ownerId;
    }

    /**
     * @param ownerId
     * sets ownerId which who has the book
     */
    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    /**
     * @return isReadInLibrary
     * returns if book is read in library by some member
     */
    public boolean isReadInLibrary() {
        return isReadInLibrary;
    }

    /**
     * @param readInLibrary
     * sets true if book is reading in library
     */
    public void setReadInLibrary(boolean readInLibrary) {
        isReadInLibrary = readInLibrary;
    }
}
