import java.util.*;

// Base Book class
abstract class Book {
    private String title;
    private String author;
    private boolean isAvailable;

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
        this.isAvailable = true;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void borrowBook() {
        isAvailable = false;
    }

    public void returnBook() {
        isAvailable = true;
    }

    public abstract void displayDetails();
}

// EBook subclass
class EBook extends Book {
    private double fileSize; // in MB

    public EBook(String title, String author, double fileSize) {
        super(title, author);
        this.fileSize = fileSize;
    }

    @Override
    public void displayDetails() {
        System.out.println("E-Book: " + getTitle() + " | Author: " + getAuthor() + " | Size: " + fileSize + "MB");
    }
}

// PrintedBook subclass
class PrintedBook extends Book {
    private int pages;

    public PrintedBook(String title, String author, int pages) {
        super(title, author);
        this.pages = pages;
    }

    @Override
    public void displayDetails() {
        System.out.println("Printed Book: " + getTitle() + " | Author: " + getAuthor() + " | Pages: " + pages);
    }
}

// Fine Calculation Strategy Interface (Polymorphism)
interface FineStrategy {
    double calculateFine(int overdueDays);
}

// Daily Fine Strategy
class DailyFine implements FineStrategy {
    private double ratePerDay;

    public DailyFine(double ratePerDay) {
        this.ratePerDay = ratePerDay;
    }

    @Override
    public double calculateFine(int overdueDays) {
        return overdueDays * ratePerDay;
    }
}

// Weekly Fine Strategy
class WeeklyFine implements FineStrategy {
    private double ratePerWeek;

    public WeeklyFine(double ratePerWeek) {
        this.ratePerWeek = ratePerWeek;
    }

    @Override
    public double calculateFine(int overdueDays) {
        return (overdueDays / 7) * ratePerWeek;
    }
}

// User class (Encapsulation)
class User {
    private String name;
    private List<Book> borrowedBooks = new ArrayList<>();

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void borrowBook(Book book) {
        if (book.isAvailable()) {
            book.borrowBook();
            borrowedBooks.add(book);
            System.out.println(name + " borrowed: " + book.getTitle());
        } else {
            System.out.println(book.getTitle() + " is not available.");
        }
    }

    public void returnBook(Book book) {
        if (borrowedBooks.contains(book)) {
            book.returnBook();
            borrowedBooks.remove(book);
            System.out.println(name + " returned: " + book.getTitle());
        } else {
            System.out.println(name + " did not borrow this book.");
        }
    }

    public void displayBorrowedBooks() {
        System.out.println("\n" + name + "'s Borrowed Books:");
        for (Book book : borrowedBooks) {
            book.displayDetails();
        }
    }
}

// Library class
class Library {
    private List<Book> books = new ArrayList<>();

    public void addBook(Book book) {
        books.add(book);
        System.out.println(book.getTitle() + " added to the library.");
    }

    public void displayBooks() {
        System.out.println("\nAvailable Books in Library:");
        for (Book book : books) {
            if (book.isAvailable()) {
                book.displayDetails();
            }
        }
    }

    public Book findBook(String title) {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title) && book.isAvailable()) {
                return book;
            }
        }
        return null;
    }
}

// Transaction class for borrowing & returning books
class Transaction {
    private User user;
    private Book book;
    private int overdueDays;
    private FineStrategy fineStrategy;

    public Transaction(User user, Book book, int overdueDays, FineStrategy fineStrategy) {
        this.user = user;
        this.book = book;
        this.overdueDays = overdueDays;
        this.fineStrategy = fineStrategy;
    }

    public void processReturn() {
        double fine = fineStrategy.calculateFine(overdueDays);
        user.returnBook(book);
        System.out.println("Overdue fine for " + book.getTitle() + ": $" + fine);
    }
}

// Main class
public class LibraryManagementSystem {
    public static void main(String[] args) {
        Library library = new Library();

        // Add books
        EBook ebook1 = new EBook("Java Programming", "James Gosling", 5.2);
        PrintedBook book1 = new PrintedBook("Data Structures", "Robert Lafore", 600);

        library.addBook(ebook1);
        library.addBook(book1);

        // Display available books
        library.displayBooks();

        // Create a user
        User user1 = new User("Alice");

        // Borrow books
        Book bookToBorrow = library.findBook("Java Programming");
        if (bookToBorrow != null) {
            user1.borrowBook(bookToBorrow);
        }

        // Display borrowed books
        user1.displayBorrowedBooks();

        // Return book with fine calculation
        FineStrategy fineStrategy = new DailyFine(2); // $2 per day fine
        Transaction transaction = new Transaction(user1, ebook1, 3, fineStrategy);
        transaction.processReturn();

        // Display library books after return
        library.displayBooks();
    }
}
