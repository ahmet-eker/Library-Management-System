import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.HashMap;
import java.util.Map;

public class Member {

    private int memberId;
    private String memberType;
    public int timeLimit;
    public int borrowLimit;
    private int borrowedNumber = 0;
    private int[] extendedBooks = new int[50];

    /**
     * @param memberId
     * @param memberType
     * determines if member type is Student or Academic
     * then sets the limits according to type
     */
    public Member(int memberId, String memberType) {
        this.memberId = memberId;
        this.memberType = memberType;
        if(memberType.equals("S")){
            timeLimit = 7;
            borrowLimit = 2;
        } else if (memberType.equals("A")) {
            timeLimit = 14;
            borrowLimit = 4;
        }
    }

    /**
     * @return memberType
     */
    public String getMemberType() {
        return memberType;
    }

    /**
     * @return borrowedNumber
     */
    public int getBorrowedNumber() {
        return borrowedNumber;
    }

    /**
     * @param borrowedNumber
     * sets the borrowedNumber
     */
    public void setBorrowedNumber(int borrowedNumber) {
        this.borrowedNumber = borrowedNumber;
    }

    /**
     * @param bookId
     * returns true if the book is in the extendedBook Array
     * @return isExtended
     */
    public boolean isExtended(int bookId) {
        for(int i = 0; i < extendedBooks.length ; i++){
            if(extendedBooks[i] == bookId){
                return true;
            }
        }
        return false;
    }

    /**
     * @param bookId
     * add this book to extendedBooks so one book which extended by one member cant extended again
     */
    public void setExtendedBooks(int bookId) {
        for(int i = 0; i < extendedBooks.length ; i++){
            if(extendedBooks[i]==0){
                extendedBooks[i] = bookId;
                return;
            }
        }
    }

    /**
     * @return memberId
     */
    public int getMemberId() {
        return memberId;
    }

    /**
     * @param memberId
     * sets the memberId
     */
    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }
}
