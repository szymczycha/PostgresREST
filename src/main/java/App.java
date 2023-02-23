import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import spark.Request;
import spark.Response;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import static spark.Spark.*;

public class App {
    private static String CONNECTION_STRING = "jdbc:postgresql://localhost:5432/testdb1";
    public static void main(String[] args) {
        port(7777);
        post("/api/book", (request, response) -> addBook(request,response));
        get("/api/book", ((request, response) -> getBooks(request,response)));
        get("/api/book/:title", ((request, response) -> getBooksByTitle(request,response)));
        get("/api/book/:year", ((request, response) -> getBooksByYear(request,response)));
        delete("/api/book/:id", ((request, response) -> deleteBookById(request,response)));
        delete("/api/book/", ((request, response) -> deleteAllBooks(request,response)));
        put("/api/update", ((request, response) -> updateBook(request,response)));
    }

    private static String updateBook(Request request, Response response) {
        return "";
    }

    private static String deleteAllBooks(Request request, Response response) {
        return "";
    }

    private static String deleteBookById(Request request, Response response) {
        System.out.println("AddBook");
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        AddBookResponse out = new AddBookResponse();
        try{
            Connection conn = DriverManager.getConnection(CONNECTION_STRING, "postgres", "admin");
            Statement stmt = conn.createStatement();
            String sql = "DELETE FROM books WHERE id = "+gson.fromJson(request.queryParams("id"), Book.class).id;
            System.out.println(sql);
            int result = stmt.executeUpdate(sql); // zwraca liczbę dodanych wierszy
            out.affectedRows = result;
            conn.close();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

        return gson.toJson(out);
    }

    private static String getBooksByYear(Request request, Response response) {
        return null;
    }

    private static String getBooksByTitle(Request request, Response response) {
        return null;
    }

    private static String getBooks(Request request, Response response) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        ArrayList<Book> books = new ArrayList<>();
        try{
            Connection conn = DriverManager.getConnection(CONNECTION_STRING, "postgres", "admin");
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM books";
            ResultSet rs = stmt.executeQuery(sql);
//
            while (rs.next()) {
                Book book = new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("country"),
                        rs.getString("language"),
                        rs.getString("link"),
                        rs.getInt("pages"),
                        rs.getInt("year")
                );
                books.add(book);
            }
            conn.close();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

        Type listType = new TypeToken<ArrayList<Book>>() {}.getType();
        return gson.toJson(books, listType);
    }

    private static String addBook(Request request, Response response) {
        System.out.println("AddBook");
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        AddBookResponse out = new AddBookResponse();
        try{
            Connection conn = DriverManager.getConnection(CONNECTION_STRING, "postgres", "admin");
            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO books (title, author, country, language, link, pages, year) VALUES(" +
                    "    '"+gson.fromJson(String.valueOf(request.body()), Book.class).title+"'," +
                    "    '"+gson.fromJson(String.valueOf(request.body()), Book.class).author+"'," +
                    "    '"+gson.fromJson(String.valueOf(request.body()), Book.class).country+"'," +
                    "    '"+gson.fromJson(String.valueOf(request.body()), Book.class).language+"'," +
                    "    '"+gson.fromJson(String.valueOf(request.body()), Book.class).link+"'," +
                    "    "+gson.fromJson(String.valueOf(request.body()), Book.class).pages+"," +
                    "    "+gson.fromJson(String.valueOf(request.body()), Book.class).year+
                    "    );";
            String sql1 = "INSERT INTO books (title, author, country, language, link, pages, year) VALUES(\n" +
                    "    'title333',\n" +
                    "    'author333',\n" +
                    "    'country',\n" +
                    "    'language',\n" +
                    "    'link',\n" +
                    "    111,\n" +
                    "    2022\n" +
                    "    );";
            System.out.println(sql);
            int result = stmt.executeUpdate(sql); // zwraca liczbę dodanych wierszy
            out.affectedRows = result;
            conn.close();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

        return gson.toJson(out);
    }
}
