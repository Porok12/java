import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCtest {
	private static String driver = "com.mysql.jdbc.Driver";
	private static String url = "jdbc:mysql://hostname:3306/dbname";
	
	public static void main(String[] args) {
		ladujSterownik();
		Connection conn = connectToDatabase("localhost", "db_test", "root", "123");
		Statement stat = createStatement(conn);
		ResultSet result = executeQuery(stat, "SHOW TABLES;");
		printDataFromQuery(result);
		closeConnection(conn, stat);
	}
	
	private static boolean ladujSterownik() {
	    System.out.print("Sprawdzanie sterownika:");
	    try {
	        Class.forName(driver).newInstance();
	        return true;
	    } catch (Exception e) {
	        System.out.println("Blad przy ladowaniu sterownika bazy!");
	        return false;
	    }
	}
	
	private static Connection connectToDatabase(String adress, String dataBaseName, String userName, String password) {
	    System.out.print("\nLaczenie z baza danych:");
	    String baza = String.format("jdbc:mysql://%s/%s", adress, dataBaseName);
	    Connection connection = null;
	    try {
	        connection = DriverManager.getConnection(baza, userName, password);
	    } catch (SQLException e) {
	        System.out.println("Blad przy ladowaniu sterownika bazy!");
	        System.exit(1);
	    }
	    return connection;
	}
	
	private static Statement createStatement(Connection connection) {
	    try {
	        return connection.createStatement();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}
	
	private static ResultSet executeQuery(Statement s, String sql) {
	    try {
	        return s.executeQuery(sql);
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}
	
	private static void printDataFromQuery(ResultSet r) {
	    ResultSetMetaData rsmd;
	    try {
	        rsmd = r.getMetaData();
	        int numcols = rsmd.getColumnCount(); // pobieranie liczby column
	        // wyswietlanie nazw kolumn:
	        for (int i = 1; i <= numcols; i++) {
	            System.out.print("\t" + rsmd.getColumnLabel(i) + "\t|");
	        }
	            System.out.print("\n____________________________________________________________________________\n");
	            
	        // wyswietlanie kolejnych rekordow:
	        while (r.next()) {
	            for (int i = 1; i <= numcols; i++) {
	                Object obj = r.getObject(i);
	                if (obj != null)
	                    System.out.print("\t" + obj.toString() + "\t|");
	                else
	                    System.out.print("\t");
	            }
	            System.out.println();
	        }
	    } catch (SQLException e) {
	        System.out.println("Bl¹d odczytu z bazy! " + e.toString());
	        System.exit(3);
	    }
	}
	
	private static void closeConnection(Connection connection, Statement s) {
	    System.out.print("\nZamykanie polaczenia z baza¹:");
	    try {
	        s.close();
	        connection.close();
	    } catch (SQLException e) {
	        System.out
	                .println("Bl¹d przy zamykaniu pol¹czenia " + e.toString());
	        System.exit(4);
	    }
	    System.out.print(" zamkniêcie OK");
	}
}
