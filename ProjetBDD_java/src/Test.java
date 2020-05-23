import java.sql.*;

public class Test {

    public static void main(String[] args) throws Exception{
        Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","psy", "admin");

        Statement st = con.createStatement();

        ResultSet rs = st.executeQuery("select * from \"Patient\"");

        while (rs.next())
            System.out.println(rs.getInt(1) + " " + rs.getString(3));

        rs.close();
        st.close();
        con.close();
    }
}
