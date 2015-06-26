import java.sql.ResultSetMetaData;
import java.sql.SQLException;

class PrintColumnTypes {

	public static void printColTypes(ResultSetMetaData rsmd)
			throws SQLException {
		int columns = rsmd.getColumnCount();
		for (int i = 1; i <= columns; i++) {
			int jdbcType = rsmd.getColumnType(i);
			String name = rsmd.getColumnTypeName(i);
			System.out.print("Column " + i + " is JDBC type " + jdbcType);
			System.out.println(", which the DBMS calls " + name);
		}
	}

}
