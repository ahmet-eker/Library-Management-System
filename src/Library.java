import java.time.LocalDate;
import java.util.HashMap;

public class Library {

    public static Book[] bookList = new Book[50]; // creating a booklist
    public static Member[] memberList = new Member[50];  // creating a memberlist
    private static int bookIdNumber = 1;  // book id number, after creating a book, it will increase
    private static int memberIdNumber = 1; // book id member, after creating a member, it will increase
    public static HashMap<CustomClass, LocalDate> membersBooksAndDates = new HashMap<>(); // this hashmap is for storing the borrowed books information
    public static HashMap<CustomClass, LocalDate> membersBooksAndDatesInTheLibrary = new HashMap<>(); // this hashmap is for storing the read in library books information

    /**
     * @return bookIdNumber
     */
    public static int getBookIdNumber() {
        return bookIdNumber;
    } //getter method

    /**
     * increases the bookIdNumber
     */
    public static void increaseBookIdNumber() { //setter method but only increases
        bookIdNumber++;
    }

    /**
     * @return memberIdNumber
     */
    public static int getMemberIdNumber() {
        return memberIdNumber;
    } //getter method

    /**
     * increases the memberIdNumber
     */
    public static void increaseMemberIdNumber() {
        memberIdNumber++;
    } //setter method but only increases
}
