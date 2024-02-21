class CustomClass {
    private int memberId;
    private int bookId;

    /**
     * @param memberId
     * @param bookId
     * this class is for the hashmap
     * which is for storing three values in printing part
     */
    public CustomClass(int memberId, int bookId) {
        this.memberId = memberId;
        this.bookId = bookId;
    }

    /**
     * @return memberId
     */
    public int getMemberId() {
        return memberId;
    }

    /**
     * @param memberId
     * sets memberId
     */
    public void setMemberId(int memberId) {
        this.memberId = memberId;
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
}