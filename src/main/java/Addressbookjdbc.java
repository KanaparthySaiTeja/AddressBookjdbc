
import java.sql.*;
import java.util.Date;
import java.util.Enumeration;
import java.lang.*;
public class Addressbookjdbc {
        public static void main(String[] args) {
            String jdbcURL = "jdbc:mysql://localhost:3306/db?verifyServerCertificate=false&useSSL=true";
            String userName = "root";
            String password = "1919";
            Connection con=null;
            Statement stmt=null;
            ResultSet rs=null;
            try {
                //1st step to load driver
                Class.forName("com.mysql.jdbc.Driver");
                System.out.println("Driver loaded!");
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException("Cannot find the driver in the classpath!", e);
            }

            listDrivers();

            try {
                System.out.println("Connecting to database:" + jdbcURL);
                // 2nd is to get the connection object
                con = DriverManager.getConnection(jdbcURL, userName, password);
                System.out.println("Connection is successful!!!!" + con);
                String sql = "SELECT * from Addressbook;";
                //3rd to create the statement object
                stmt = con.createStatement();
                //4th is to execute SQL query
                rs = stmt.executeQuery(sql);
                while(rs.next()){
                    String name = rs.getString("first_Name");
                    String phone = rs.getString("last_name");
                    String address = rs.getString("Address");
                    String department = rs.getString("city");
                    String basic_pay = rs.getString("state");
                    int deductions = rs.getInt("zip");
                    long tax_pay = rs.getLong("phone_number");
                    String income_tax = rs.getString("email");
                    String type = rs.getString("type");

                    System.out.println( "," + name + "," + phone + "," + address +  "," +
                            department + "," + basic_pay + "," + deductions + "," + type + "," );
                }
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            finally
            {
                try
                {
                    con.close();
                    rs.close();
                    stmt.close();
                }
                catch(SQLException e)
                {
                    e.printStackTrace();
                }

            }
        }

        private static void listDrivers() {
            Enumeration<Driver> driverList = DriverManager.getDrivers();
            while (driverList.hasMoreElements()) {
                Driver driverClass = driverList.nextElement();
                System.out.println(driverClass.getClass().getName());
            }
        }
    }
