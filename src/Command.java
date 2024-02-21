import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

public class Command {

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // this formatter helps the transition of dates

    /**
     * @param commandLine
     * this function gets every command and throw them into related function
     */
    public static void commandHandler(String commandLine){
        String command = commandLine.split("\t")[0];
        if (command.equals("addBook")){
            addBook(commandLine);
        }
        else if (command.equals("addMember")){
            addMember(commandLine);
        }
        else if (command.equals("borrowBook")){
            borrowBook(commandLine);
        }
        else if (command.equals("returnBook")){
            returnBook(commandLine);
        }
        else if (command.equals("extendBook")){
            extendBook(commandLine);
        }
        else if (command.equals("readInLibrary")){
            readInTheLibrary(commandLine);
        }
        else if (command.equals("getTheHistory")){
            getTheHistory();
        }
        else {
            FileInput.writeToFile(Main.outputPath,"You've entered wrong command",true,true);
        }
    }

    /**
     * @param input
     * this function adds book to the library with its type and id
     */
    public static void addBook(String input){
        Book newBook = new Book(Library.getBookIdNumber(), input.split("\t")[1]);
        Library.bookList[Library.getBookIdNumber()] = newBook;
        String type;
        if(input.split("\t")[1].equals("H")){
            type = "Handwritten";
        }else {
            type = "Printed";
        }
        FileInput.writeToFile(Main.outputPath,"Created new book: " + type + " [id: "+ Library.getBookIdNumber() +"]",true,true);
        Library.increaseBookIdNumber();
    }

    /**
     * @param input
     * this function adds member to the library with its type and id
     */
    public static void addMember(String input){
        Member newMember = new Member(Library.getMemberIdNumber(), input.split("\t")[1]);
        Library.memberList[Library.getMemberIdNumber()] = newMember;
        String type;
        if(input.split("\t")[1].equals("S")){
            type = "Student";
        }else {
            type = "Academic";
        }
        FileInput.writeToFile(Main.outputPath,"Created new member: " +type+ " [id: "+Library.getMemberIdNumber()+"]",true,true);
        Library.increaseMemberIdNumber();
    }

    /**
     * @param input
     * this function borrows the book from library and assign it to the given member
     * necessary checks is validating and throeing error messages if not
     */
    public static void borrowBook(String input){
        int idOfBook = Integer.parseInt(input.split("\t")[1]);
        int idOfMember = Integer.parseInt(input.split("\t")[2]);
        if(Library.bookList[idOfBook].isborrowed || Library.bookList[idOfBook].isReadInLibrary || (Library.memberList[idOfMember].getMemberType().equals("S")&&Library.bookList[idOfBook].getBookType().equals("H")) || !(Library.memberList[idOfMember].getBorrowedNumber()<Library.memberList[idOfMember].borrowLimit)){
            FileInput.writeToFile(Main.outputPath,"You have exceeded the borrowing limit!",true,true);
        }
        else {
            LocalDate borrowedDate = LocalDate.parse(input.split("\t")[3], formatter);
            LocalDate returnedDate = null;
            if(Library.memberList[idOfMember].getMemberType().equals("S")){
                returnedDate = borrowedDate.plusDays(7);
            } else {
                returnedDate = borrowedDate.plusDays(14);
            }
            Library.bookList[idOfBook].isborrowed = true;
            Library.bookList[idOfBook].setBorrowedDate(borrowedDate);
            Library.bookList[idOfBook].setReturnedDate(returnedDate);
            Library.bookList[idOfBook].setOwnerId(idOfMember);
            CustomClass customObject = new CustomClass(idOfMember,idOfBook);
            Library.membersBooksAndDates.put(customObject, borrowedDate);
            Library.memberList[idOfMember].setBorrowedNumber(Library.memberList[idOfMember].getBorrowedNumber()+1);
            FileInput.writeToFile(Main.outputPath,"The book ["+idOfBook+"] was borrowed by member ["+idOfMember+"] at " + borrowedDate.toString() ,true,true);
        }

    }

    /**
     * @param input
     * returns the borrowed book to the library
     * necessary checks are made then returns the book
     */
    public static void returnBook(String input){
        int idOfBook = Integer.parseInt(input.split("\t")[1]);
        int idOfMember = Integer.parseInt(input.split("\t")[2]);
        if(!(Library.bookList[idOfBook].isborrowed || Library.bookList[idOfBook].isReadInLibrary)){
            FileInput.writeToFile(Main.outputPath,"This book can't returned" ,true,true);
        } else {
            LocalDate returnedDate = LocalDate.parse(input.split("\t")[3],formatter);
            int fee = calculateFee(Library.bookList[idOfBook].borrowedDate,Library.memberList[idOfMember],returnedDate,Library.memberList[idOfMember].isExtended(idOfBook));
            Library.bookList[idOfBook].setOwnerId(0);

            CustomClass removed = null;
            for (Map.Entry<CustomClass, LocalDate> entry : Library.membersBooksAndDates.entrySet()) {
                CustomClass key = entry.getKey();
                if(key.getMemberId() == idOfMember && key.getBookId() == idOfBook && Library.bookList[idOfBook].isborrowed){
                    removed = key;
                }
            }
            for (Map.Entry<CustomClass, LocalDate> entry : Library.membersBooksAndDatesInTheLibrary.entrySet()) {
                CustomClass key = entry.getKey();
                if (key.getMemberId() == idOfMember && key.getBookId() == idOfBook && Library.bookList[idOfBook].isReadInLibrary) {
                    removed = key;
                }
            }
            Library.membersBooksAndDatesInTheLibrary.remove(removed);
            Library.membersBooksAndDates.remove(removed);

            Library.memberList[idOfMember].setBorrowedNumber(Library.memberList[idOfMember].getBorrowedNumber()-1);
            FileInput.writeToFile(Main.outputPath,"The book [" + idOfBook + "] was returned by member [" + idOfMember + "] at " + input.split("\t")[3] + " Fee: " + fee ,true,true);
            Library.bookList[idOfBook].isborrowed = false;
            Library.bookList[idOfBook].isReadInLibrary = false;
            Library.bookList[2].borrowedDate = null;
        }
    }

    /**
     * @param input
     * extends the deadline of the book
     * necessary checks are made and extend the deadline of the book
     */
    public static void extendBook(String input){
        int idOfBook = Integer.parseInt(input.split("\t")[1]);
        int idOfMember = Integer.parseInt(input.split("\t")[2]);
        if (!Library.memberList[idOfMember].isExtended(idOfBook)){
            Library.memberList[idOfMember].setExtendedBooks(idOfBook);
            Library.bookList[idOfBook].setReturnedDate(Library.bookList[idOfBook].getReturnedDate().plusDays(Library.memberList[idOfMember].timeLimit));
            FileInput.writeToFile(Main.outputPath,"The deadline of book ["+idOfBook+"] was extended by member ["+idOfMember+"] at" + input.split("\t")[3],true,true);
            FileInput.writeToFile(Main.outputPath,"New deadline of book ["+idOfBook+"] is " + Library.bookList[idOfBook].getReturnedDate(),true,true);
        }
        else {
            FileInput.writeToFile(Main.outputPath,"You cannot extend the deadline!",true,true);
        }
    }

    /**
     * @param input
     * read the book in library
     * neccessary checks are made, then book is not borrowed but read in library
     */
    public static void readInTheLibrary(String input){
        int idOfBook = Integer.parseInt(input.split("\t")[1]);
        int idOfMember = Integer.parseInt(input.split("\t")[2]);
        if(!Library.bookList[idOfBook].isborrowed && !(Library.memberList[idOfMember].getMemberType().equals("S")&&Library.bookList[idOfBook].getBookType().equals("H"))){
            LocalDate readDate = LocalDate.parse(input.split("\t")[3]);
            Library.bookList[idOfBook].borrowedDate = readDate;
            CustomClass customObject = new CustomClass(idOfMember,idOfBook);
            Library.membersBooksAndDatesInTheLibrary.put(customObject, readDate);
            Library.bookList[idOfBook].isReadInLibrary = true;
            FileInput.writeToFile(Main.outputPath,"The book ["+idOfBook+"] was read in library by member ["+idOfMember+"] at " + readDate.toString(),true,true);
        }
        else if(Library.memberList[idOfMember].getMemberType().equals("S")&&Library.bookList[idOfBook].getBookType().equals("H")){
            FileInput.writeToFile(Main.outputPath,"Students can not read handwritten books!",true,true);
        } else {
            FileInput.writeToFile(Main.outputPath,"You can not read this book!",true,true);
        }
    }

    /**
     * getting the history and printing to the output file
     */
    public static void getTheHistory(){
        int numberOfStudent = 0;
        int numberOfAcademic = 0;
        ArrayList<Integer> students = new ArrayList<Integer>(); // this stores the students id
        ArrayList<Integer> academics = new ArrayList<Integer>(); // this store the academics id
        for(Member member : Library.memberList){
            if(member!=null) {
                if (member.getMemberType().equals("S")) {
                    numberOfStudent++;
                    students.add(member.getMemberId()); // adding to the arraylist
                } else if (member.getMemberType().equals("A")) {
                    numberOfAcademic++;
                    academics.add(member.getMemberId()); // adding to the arraylist
                }
            }
        }
        int numberOfPrintedBooks = 0; // this part is for printing the total number of elements
        int numberOfHandWrittenBooks = 0;
        int borrowedBooksNumber = 0;
        int readInLibraryBooksNumber = 0;
        ArrayList<Integer> printed = new ArrayList<Integer>(); // this stores the book type
        ArrayList<Integer> handWritten = new ArrayList<Integer>();  // this stores the book type
        for(Book book : Library.bookList){
            if(book!=null){
                if(book.isborrowed){
                    borrowedBooksNumber++;
                } else if (book.isReadInLibrary) {
                    readInLibraryBooksNumber++;
                }
                if(book.getBookType().equals("P")){
                    numberOfPrintedBooks++;
                    printed.add(book.getBookId()); // adding to the arraylist

                } else if (book.getBookType().equals("H")) {
                    numberOfHandWrittenBooks++;
                    handWritten.add(book.getBookId()); // adding to the arraylist
                }
            }
        }
        // PRINTING PART OF GET HISTORY IS STARTS HERE
        FileInput.writeToFile(Main.outputPath,"History of library:\n",true,true);
        FileInput.writeToFile(Main.outputPath,"Number of students: " + numberOfStudent,true,true);
        for(int i = 0;i<students.size();i++){
            FileInput.writeToFile(Main.outputPath,"Student [id: "+ students.get(i) + "]",true,true);
        }
        FileInput.writeToFile(Main.outputPath,"\nNumber of academics: " + numberOfAcademic,true,true);
        for(int i = 0;i<academics.size();i++){
            FileInput.writeToFile(Main.outputPath,"Academic [id: "+ academics.get(i) + "]",true,true);
        }
        FileInput.writeToFile(Main.outputPath,"\nNumber of printed books: " + numberOfPrintedBooks,true,true);
        for(int i = 0;i<printed.size();i++){
            FileInput.writeToFile(Main.outputPath,"Printed [id: "+ printed.get(i) + "]",true,true);
        }
        FileInput.writeToFile(Main.outputPath,"\nNumber of handwritten books: " + numberOfHandWrittenBooks,true,true);
        for(int i = 0;i<handWritten.size();i++){
            FileInput.writeToFile(Main.outputPath,"Handwritten [id: "+ handWritten.get(i) + "]",true,true);
        }
        FileInput.writeToFile(Main.outputPath,"\nNumber of borrowed books: " + Library.membersBooksAndDates.size(),true,true);
        for (Map.Entry<CustomClass, LocalDate> entry : Library.membersBooksAndDates.entrySet()) {
            CustomClass key = entry.getKey();
            LocalDate value = entry.getValue();
            FileInput.writeToFile(Main.outputPath,"The book ["+key.getBookId()+"] was borrowed by member ["+key.getMemberId()+"] at " + value.toString(),true,true);
        }
        FileInput.writeToFile(Main.outputPath,"\nNumber of books read in library: " + Library.membersBooksAndDatesInTheLibrary.size(),true,false);
        for (Map.Entry<CustomClass, LocalDate> entry : Library.membersBooksAndDatesInTheLibrary.entrySet()) {
            CustomClass key = entry.getKey();
            LocalDate value = entry.getValue();
            FileInput.writeToFile(Main.outputPath,"\nThe book ["+key.getBookId()+"] was read in library by member ["+key.getMemberId()+"] at " + value.toString(),true,false);
        }
    }


    /**
     * @param borrowedDate borrowed date of the book
     * @param member member class
     * @param returnedDate returned date of the book
     * @param isExtended is extended one time or not
     * @return
     * this function calculates the fee of the member for returning the book too late
     */
    public static int calculateFee(LocalDate borrowedDate, Member member, LocalDate returnedDate, boolean isExtended){
        int limit;
        if(!isExtended){
            limit = member.timeLimit;
        } else {
            limit = member.timeLimit + member.timeLimit;
        }
        //int dayTime = (int)Duration.between(borrowedDate,returnedDate).toDays();
        int dayTime = (int)ChronoUnit.DAYS.between(borrowedDate, returnedDate);
        if (dayTime > limit){
            return dayTime-limit;
        }
        return 0;
    }
}
