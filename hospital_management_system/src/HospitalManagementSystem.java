import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class HospitalManagementSystem {
    
    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String username = "root";


    public static void main(String[] args) throws InterruptedException, SQLException {
        Scanner scanner = new Scanner(System.in);

        try{
            System.out.print("Enter database password: ");
            String pass = scanner.next();
            System.out.println();
            Connection connection = DriverManager.getConnection(url, username, pass);
            patient patient = new patient(connection, scanner);
            doctor doctor = new doctor(connection);

            while (true){
                System.out.println("HOSPITAL  MANAGEMENT  SYSTEM");
                System.out.println();
                System.out.println("1. Add Patient");
                System.out.println("2. View Patient");
                System.out.println("3. View Doctor");
                System.out.println("4. Book an Appointment");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();

                switch(choice){

                    case 1:
                    patient.addPatient();
                    System.out.println();
                    break;

                    case 2:
                    patient.viewPatient();
                    System.out.println();
                    break;

                    case 3:
                    doctor.viewDoctor();
                    System.out.println();
                    break;

                    case 4:
                    bookAppointment(patient, doctor, connection, scanner);
                    System.out.println();
                    break;

                    case 5:
                    exit();
                    return;

                    default:
                    System.out.println("Enter valid choice");
                }
            }

        }catch(SQLException e){
            System.out.println("Enter the correct password");
            Thread.sleep(1500);
            System.out.println();
        }
        scanner.close();
    }

    public static void bookAppointment(patient patient, doctor doctor, Connection connection, Scanner scanner) {
        System.out.print("Enter patient id: ");
        int patientId = scanner.nextInt();
        System.out.print("Enter doctor id: ");
        int doctorId = scanner.nextInt();
        System.out.print("Enter appointment date (YYYY-MM-DD): ");
        String appointmentDate = scanner.next();

        if(patient.getPatientById(patientId) && doctor.getDoctorById(doctorId)){
            if(checkDocterAvailability(doctorId, appointmentDate, connection)){
                String appointmentquery = "INSERT INTO appointments (patient_id, doctor_id, appointment_date) VALUES (?, ?, ?)"; 
                try{
                    PreparedStatement preparedStatement = connection.prepareStatement(appointmentquery);
                    preparedStatement.setInt(1, patientId);
                    preparedStatement.setInt(2, doctorId);
                    preparedStatement.setString(3, appointmentDate);
                    int affectedRows = preparedStatement.executeUpdate();
                    
                    if(affectedRows > 0){
                        System.out.println();
                        System.out.println("APPOINTMENT BOOKED SUCCESSFULLY");
                    }else{
                        System.out.println();
                        System.out.println("FAILED TO BOOK APPOINTMENT");
                    }
                    
                }catch(SQLException e){
                    System.out.println(e.getMessage());
                }

            }else{
                System.out.println();
                System.out.println("DOCTOR IS NOT AVAILABLE ON " + appointmentDate);
            }

        }else{
            if(!patient.getPatientById(patientId)){
                System.out.println();
                System.out.println("ADD PATIENT FIRST");
            }else{
                System.out.println();
                System.out.println("DOCTOR NOT FOUND");
            }
        }    
    }

    public static boolean checkDocterAvailability(int doctorId, String appointmentDate, Connection connection){
        String query = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, doctorId);
            preparedStatement.setString(2, appointmentDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                int count = resultSet.getInt(1);
                if(count == 0){
                    return true;
                }else{
                    return false;
                }
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public static void exit() throws InterruptedException {
        System.out.println();
        System.out.print("EXITING SYSTEM IN ");
        
        int maxNumberLength = 1; // Length of the numbers you are printing (1-5 in this case)
        
        for (int i = 5; i > 0; i--) {
            // Move cursor back to the start of the countdown area
            System.out.print("\rEXITING SYSTEM IN ");
            // Print the current number
            System.out.print(i);
            // Ensure previous numbers are overwritten by printing spaces after the current number
            int spacesToOverwrite = maxNumberLength - Integer.toString(i).length();
            System.out.print(" ".repeat(spacesToOverwrite));
            
            // Wait for 500 milliseconds
            Thread.sleep(500);
        }
        
        // Print the final message
        System.out.print("\rEXITING SYSTEM IN "); // Move cursor back and clear any remaining part
        System.out.println("0");
        System.out.println();
        System.out.println("HAVE A NICE DAY :)");
        System.out.println();
    }
    
}
