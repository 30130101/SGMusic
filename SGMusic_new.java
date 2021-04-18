import com.mysql.cj.protocol.a.MysqlBinaryValueDecoder;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Main {
    private static Connection myCon;
    public static void main(String[]args) throws SQLException {
        //conect to SGMusic
        String url ="jdbc:mysql://localhost:3306/SoundGood?serverTimezone=UTC";
        String usrName="root";
        String password="rootadmin";
        myCon = DriverManager.getConnection(url,usrName,password);
        myCon.setAutoCommit(false);
        Statement mySt = myCon.createStatement();
        //listInstruments(mySt);
        rentInstrument(mySt, 3, 103);
        //terminate_rental(mySt, 7);
    }
    /**LIST ALL THE INSTRUMENTS WITH THEIR UNIQUE IDS*/
    public static void listInstruments(Statement mySt) throws SQLException {
        String query = "SELECT * FROM SGMusic.instrument";
        try(ResultSet rs = mySt.executeQuery(query)){
            System.out.println("inst_id\t"+"instrument\t"+ "price\t"+ "brand\t");
            while(rs.next()){
                System.out.print(rs.getString("instrument_id")+"\t\t");
                System.out.print(rs.getString("instrument")+"\t\t"+ rs.getString("price")+"\t\t");
                System.out.println(rs.getString("brand"));
            }
            myCon.commit();
        }catch (SQLException sqle){
            System.out.print("could not list the instruments");
            myCon.rollback();
        }
    }
    /**HERE,STUDENT SPECIFIES WHICH INSTRUMENT TO RENT USING INSTRUMENT_ID
     * @param stdId: sudent id
     * @param toRent_id: instrument_id to be rented
     * */
    public static void rentInstrument(Statement mySt, int stdId, int toRent_id) throws SQLException{
        ///
        // query calculates current rentals = total rentals - terminated rentals
        String query = "SELECT(Select COUNT(*) FROM  SGMusic.rental_instrument\n" +
                "WHERE student_id ="+stdId+")-(Select COUNT(*) FROM  SGMusic.rental_instrument\n" +
                "WHERE student_id = "+stdId+" AND return_date IS NOT null) as current_rentals";

        try(ResultSet rs = mySt.executeQuery(query)){
            if(rs.next()){
                if (rs.getString("current_rentals").equals("2")){
                    System.out.println("You have reached your maximal rentals");
                    showRentals(mySt, stdId);
                }else {
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                    LocalDateTime now = LocalDateTime.now();

                    query= "INSERT INTO SGMusic.Rental_instrument ( instrument_id,rental_id, rental_date, student_id)\n"+
                            "SELECT (SELECT instrument_id FROM SGMusic.instrument WHERE instrument_id = "+toRent_id+"),"+
                            "MAX(rental_id)+1, "+
                            " \""+dtf.format(now)+"\","+ "\""+stdId+"\"" +"FROM SGMusic.rental_instrument";
                    try{
                        mySt.executeUpdate(query);
                        myCon.commit();
                    }catch (SQLException sqle){
                        System.out.println("Failed to rent instrument");
                        myCon.rollback();
                    }
                    System.out.println("Your rental has now been made");
                    showRentals(mySt,stdId);
                }
            }
            myCon.commit();
        }catch (SQLException sqle){
            System.out.println("Could not retrieve rentals or rent instrument");
            myCon.rollback();
        }
    }
    public static void showRentals(Statement mySt, int stdId) throws SQLException {
        String query="SELECT * FROM SGMusic.Rental_instrument WHERE student_id = "+stdId;
        ResultSet rst = mySt.executeQuery(query);
        System.out.println("Your rentals are:");
        System.out.println("inst_id\t"+"rental_id\t"+ "rental_date\t" + "return_date\t");
        while(rst.next()){
            System.out.print(rst.getString("instrument_id")+"\t\t");
            System.out.print(rst.getString("rental_id")+"\t\t");
            System.out.print(rst.getString("rental_date")+"\t\t");
            System.out.println(rst.getString("return_date"));
        }
    }
    /**FUNCTION TO TERMINATE RENTALS
     * @param toEND: rental_id of instrument to be returned
     * */
    public static void terminate_rental(Statement mySt, int toEND) throws SQLException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        String query = "UPDATE SGMusic.Rental_instrument \n" +
                "SET return_date = \""+dtf.format(now)+"\"\n" +
                "WHERE rental_id ="+toEND;
        try{
            mySt.executeUpdate(query);
            myCon.commit();
            System.out.println("Your rental with id "+toEND+" is now terminated");
        }catch (SQLException sqle){
            System.out.print("failed to terminate the rental");
            myCon.rollback();
        }
    }
}
