import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Main {
    public static void main(String[]args) throws SQLException {
        String url ="jdbc:mysql://localhost:3306/SoundGood?autoReconnect=true";
        String usrName="root";
        String password="pass";
        Connection myCon = DriverManager.getConnection(url,usrName,password);
        Statement mySt = myCon.createStatement();
        //listInstruments(mySt)
        //rentInstrument(mySt, 30214, "guitar");
        terminate_rental(mySt, 30214, "guitar");

    }

    public static void listInstruments(Statement mySt) throws SQLException {
        String query = "SELECT * FROM SoundGood.instrument";
        ResultSet rs = mySt.executeQuery(query);

        while(rs.next())
            System.out.println(rs.getString("instrument"));
    }
    public static void rentInstrument(Statement mySt, int persNr, String toRent) throws SQLException{
        String query = "SELECT count(student_ssn)as c\n" +
                "FROM SoundGood.student_instrument\n" +
                "WHERE student_ssn =" + persNr;
        ResultSet rs = mySt.executeQuery(query);

        /***   ***/
        if(rs.next()){
            if (rs.getString("c").equals("2")){
                System.out.println("You have reached your maximal rentals");
            }else {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                LocalDateTime now = LocalDateTime.now();

                query= "INSERT INTO SoundGood.student_instrument (instrument_id, rental_id, student_ssn, rental_date)\n" +
                        "Select (SELECT instrument_id FROM SoundGood.instrument WHERE instrument = \""+toRent+"\")," +
                        "MAX(rental_id)+1, "+persNr+", \""+dtf.format(now)+"\"\n" +
                        "FROM  SoundGood.student_instrument \n";
                 mySt.executeUpdate(query);
            }
        }
    }

    public static void terminate_rental(Statement mySt, int persNr, String toEND) throws SQLException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        String query = "UPDATE SoundGood.student_instrument \n" +
                "SET return_date = \""+dtf.format(now)+"\"\n" +
                "WHERE student_ssn = "+persNr+" AND instrument_id = (SELECT instrument_id FROM SoundGood.instrument WHERE instrument = \""+toEND+"\")";
        mySt.executeUpdate(query);
    }
}
