package DB;

import model.Book;
import model.Member;
import model.BorrowRecord;

import java.util.ArrayList;
import java.util.List;

public class Database {
    public static List<Book> bookList = new ArrayList<>();
    public static List<Member> memberList = new ArrayList<>();
    public static List<BorrowRecord> borrowList = new ArrayList<>();

    static {

        memberList.add(new Member("M001", "Kamal Perera", "kamal@gmail.com", "0771234567", "Colombo"));
        memberList.add(new Member("M002", "Nimal Silva", "nimal@gmail.com", "0719876543", "Kandy"));
    }
}