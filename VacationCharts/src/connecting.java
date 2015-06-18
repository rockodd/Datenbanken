import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class connecting {

	/* These variable values are used to setup the Connection object */

	private static final String DBNAME = "DBS1";
	private static final String URL = "jdbc:oracle:thin:@dbl43.beuth-hochschule.de:1521:oracle";
	private static final String USER = "ullmann";
	private static final String PASSWORD = "HIGHscreen";
	private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
	private static Connection con;
	static PreparedStatement prepStatement_Sel_Locatoins;
	static String table = "LOCATION";
	static String row1 = "CITY";

	/*
	 * This method is used to create a connection using the values listed above.
	 * Notice the throws clause in the method signature. This allows the calling
	 * method to deal with the exception rather than catching it in both places.
	 * The ClassNotFoundException must be caught because the forName method
	 * requires it.
	 */

	/**
	 * This method is used to open the DB and to print out the connection
	 * status.
	 * 
	 * @param prepStatement_INSERT_PERSON
	 */
	public static void openDB() throws SQLException {
		con = connect(DBNAME);
		System.out.println("Connected to: "
				+ con.getMetaData().getDatabaseProductName() + " "
				+ con.getMetaData().getDatabaseProductVersion());
		prepStatement_Sel_Locatoins = con
				.prepareStatement(makesql(table, row1));
	}

	/**
	 * @param dbname
	 * @return
	 * @throws SQLException
	 * 
	 *             This method is used to create a connection using the values
	 *             listed above. Notice the throws clause in the method
	 *             signature. This allows the calling method to deal with the
	 *             exception rather than catching it in both places. The
	 *             ClassNotFoundException must be caught because the forName
	 *             method requires it.
	 */
	private static Connection connect(String dbname) throws SQLException {
		Connection con = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			;
			con = DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (ClassNotFoundException ex) {
			System.out.println("JDBC Treiber eingebunden?");
			System.out.println("ConnectException: " + ex.getMessage());
			System.exit(-1);

		}
		System.out.println("connected");
		return con;
	}

	public static String makesql(String table, String row1) {
		String sql = "select " + row1 + " From " + table;
		System.out.println("SQL-String erstellt");
		return sql;

	}

	public static void printAll() {
		String query = makesql(table, row1);
		ResultSet rs = null;
		try {
			System.out.println(con);
			Statement s = con.createStatement();
			System.out.println(s);
			long begin = System.currentTimeMillis();
			rs = s.executeQuery(query);
			System.out.println(rs);
			long executiontime = System.currentTimeMillis() - begin;
			printResultSet2Shell(rs, query, executiontime);
			rs.close();
			s.close();
			System.out.println("resultset erstellt");
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			System.exit(-1);
		}
	}

	private static void printResultSet2Shell(ResultSet rs, String query,
			long executiontime) throws SQLException {
		System.out
				.println("===============================================================");
		System.out.println(query);
		System.out.println("Execution Time: " + executiontime + "ms");
		System.out
				.println("===============================================================");
		System.out.format("%-20s %-15s %n", "ID", "City");
		System.out.format("%-20s %-15s %n", "-------------------",
				"---------------");

		while (rs.next()) {
			long tableid = rs.getLong("ID");
			String row1 = rs.getString("row1");
			System.out.format("%-20d %-15s %n", tableid, row1);
		}
		System.out
				.println("================================================================");
	}

	public static void main(String[] args) {
		System.out.println("Versuche DB zu öffnen");
		try {
			openDB();
			System.out.println("DB open");
			printAll();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("DB not open!");
		}
		System.out.println("fertig");
	}

}
