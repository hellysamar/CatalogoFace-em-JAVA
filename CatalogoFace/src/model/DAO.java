package model;

import java.sql.Connection;
import java.sql.DriverManager;

public class DAO {

	private Connection conn;
	private String driver = "com.mysql.cj.jdbc.Driver";
	private String url = "jdbc:mysql://localhost:3306/dbCatalogoFace";
	private String user = "root";
	private String password = "";
	
	public Connection conectar() {
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, password);
			return conn;
		} catch (Exception e) {
			System.out.print(e);
			return null;
		}
	}
}
