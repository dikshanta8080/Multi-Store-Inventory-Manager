import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class CleanDb {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:postgresql://localhost:5432/inventory_db";
        try (Connection conn = DriverManager.getConnection(url, "postgres", "root");
             Statement stmt = conn.createStatement()) {
            stmt.execute("DROP SCHEMA public CASCADE; CREATE SCHEMA public; GRANT ALL ON SCHEMA public TO postgres; GRANT ALL ON SCHEMA public TO public;");
            System.out.println("Database schema public dropped and recreated successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
