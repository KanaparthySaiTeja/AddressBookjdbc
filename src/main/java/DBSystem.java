
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class DBSystem {

    private PreparedStatement addressBookDataStatement;
    private static DBSystem addressBookDBSystem;

    private DBSystem(){
    }

    public static DBSystem getInstance(){
        if(addressBookDBSystem == null)
            addressBookDBSystem = new DBSystem();
        return addressBookDBSystem;
    }

    public Connection getConnection(){
        String jdbcURL = "jdbc:mysql://localhost:3306/db?allowPublicKeyRetrieval=true & useSSL=false";
        String userName = "root";
        String password = "1919";
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver Loaded!!");
        }catch (ClassNotFoundException e) {
            throw new IllegalStateException("driver not found in the classpath", e);
        }

        listDrivers();
        try {
            System.out.println("Connecting to the Database " + jdbcURL);
            connection = DriverManager.getConnection(jdbcURL, userName, password);
            System.out.println("Connection was successful");
        }catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }
    public static void listDrivers() {
        Enumeration<Driver> driverList = DriverManager.getDrivers();
        while(driverList.hasMoreElements()) {
            Driver driverClass = (Driver) driverList.nextElement();
            System.out.println( "  " + driverClass.getClass().getName());
        }
    }

    public List<Contact> readData(){
        String sql = "Select * from addressbook";
        return this.getContactDataUsingDB( sql );
    }

    private List<Contact> getContactDataUsingDB(String sql) {
        List<Contact> addressBookContactArrayList = new ArrayList<>();
        try(Connection connection =this.getConnection()){
            Statement statement =connection.createStatement();
            ResultSet resultSet = statement.executeQuery( sql );
            addressBookContactArrayList = this.getAddressBookData( resultSet );
        }catch (SQLException e){
            e.printStackTrace();
        }
        return addressBookContactArrayList;
    }


    private List<Contact> getAddressBookData(ResultSet resultSet) {
        List<Contact> addressBookContactArrayList = new ArrayList<>();
        try{
            while (resultSet.next()){
                String firstName = resultSet.getString("firstname");
                String lastName = resultSet.getString( "lastname" );
                String address = resultSet.getString("address");
                String city = resultSet.getString( "city" );
                String state = resultSet.getString( "state" );
                long zip = resultSet.getLong( "zip" );
                long phoneNumber = resultSet.getLong( "phno" );
                String emailId = resultSet.getString( "email" );
                String type = resultSet.getString( "type" );
                addressBookContactArrayList.add(new Contact( firstName,lastName,address,city,state,zip, phoneNumber,emailId,type ));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return addressBookContactArrayList;
    }

    public int updateAddressBookContactData(String firstname, long zip) {
        return this.updateContactDataUsingPreparedStatement( firstname,zip );
    }

    private int updateContactDataUsingPreparedStatement(String firstname,long zip){
        String sql = "update addressbook set zip = ? where firstname = ?";
        try(Connection connection =this.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDouble( 1,zip );
            preparedStatement.setString( 2,firstname );
            int status =preparedStatement.executeUpdate();
            return status;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }
    public List<Contact> getAddressBookContactData(String firstname) {
        List<Contact> addressBookContactList = null;
        if(this.addressBookDataStatement == null)
            this.prepareStatementForContactData();
        try{
            addressBookDataStatement.setString( 1,firstname );
            ResultSet resultSet = addressBookDataStatement.executeQuery();
            addressBookContactList = this.getAddressBookData(  resultSet );
        }catch (SQLException e){
            e.printStackTrace();
        }
        return addressBookContactList;
    }

    private void prepareStatementForContactData() {
        try{
            Connection connection = this.getConnection();
            String sql = "Select * from addressbook where firstname = ?";
            addressBookDataStatement = connection.prepareStatement( sql );
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public List<Contact> getAddressBookContactDataForDateRange(LocalDate startDate, LocalDate endDate) {
        String sql = String.format( "select * from addressbook where date between '%s' and '%s';",
                Date.valueOf(startDate),Date.valueOf(endDate));
        return this.getContactDataUsingDB(sql);
    }
}
