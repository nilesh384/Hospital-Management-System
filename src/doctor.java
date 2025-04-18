import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class doctor {
    
    private Connection connection; 
    
    public doctor(Connection connection){
        this.connection = connection;
    }

    public void viewDoctor(){
        
        String query = "SELECT * FROM doctors";
        
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
        
            System.out.println("Docters: ");
            System.out.println("+------------+--------------------+----------------------+");
            System.out.println("| Docter Id  | Docter Name        | Specialization       |");
            System.out.println("+------------+--------------------+----------------------+");

            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String specialization = resultSet.getString("specialization");

                System.out.printf("| %-10s | %-18s | %-20s |\n", id, name, specialization); // hormatting
                System.out.println("+------------+--------------------+----------------------+");
            }
            

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public boolean getDoctorById(int id){
        String query = "SELECT * FROM doctors WHERE id = ?";
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
