import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.io.File;

public class MySQLConnection {
    public static void main(String[] args) {
        // Database credentials
        String url = "jdbc:mysql://localhost:3306/music";
        String user = "root"; // Change to your MySQL username
        String password = "Yashodhar#123"; // Change to your MySQL password

        try {
            // Load MySQL JDBC Driver (Optional in newer versions)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish the connection
            Connection conn = DriverManager.getConnection(url, user, password);
            String query = "insert into test values (?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, 1);

            
            try {

                FileInputStream fis = new FileInputStream(new File("image.png"));
                preparedStatement.setBinaryStream(2, fis, (int) new File("image.png").length());
                fis.close();
            }catch(Exception e){
                
            }
            
            
            preparedStatement.executeUpdate();

            if(preparedStatement.executeUpdate()>0){
                System.out.println("success");  
            }
            else {
                System.out.println("something went wrong");
            }
                


            System.out.println("Connected to MySQL successfully!");


            preparedStatement.close();
            conn.close();
        
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Connection failed.");
            e.printStackTrace();
        }
    }
}
