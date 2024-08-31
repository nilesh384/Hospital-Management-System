import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;


public class patient {
    private Connection connection;
    private Scanner scanner; 
    
    public patient(Connection connection, Scanner Scanner){
        this.connection = connection;
        this.scanner = Scanner;
    }

    public void addPatient(){
        System.out.print("Enter Patient name: ");
        String name = scanner.next();
        System.out.print("Enter Patient age: ");
        int age = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Patient gender: ");
        String gender = scanner.next();

        String query = "INSERT INTO patients (name, age, gender) VALUES (?, ?, ?)";
        
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name.toUpperCase());
            preparedStatement.setInt(2, age);
            preparedStatement.setString(3, gender.toUpperCase());
            int affectedRows = preparedStatement.executeUpdate();

            if(affectedRows > 0){
                System.out.println();
                System.out.println("PATIENT ADDED SUCCESSFULLY!");
            }else{
                System.out.println();
                System.out.println("FAILED TO ADD PATIENT.");
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void viewPatient(){
        
        String query = "SELECT * FROM patients";
        
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
        
            System.out.println("Patients: ");
            System.out.println("+------------+--------------------+---------+------------+");
            System.out.println("| Patient Id | Patient Name       | Age     | Gender     |");
            System.out.println("+------------+--------------------+---------+------------+");

            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");

                System.out.printf("| %-10s | %-18s | %-7s | %-10s |\n",id, name, age, gender); // hormatting
                System.out.println("+------------+--------------------+---------+------------+");
            }
            

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public boolean getPatientById(int id){
        String query = "SELECT * FROM patients WHERE id = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }else{
                return false;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

}
