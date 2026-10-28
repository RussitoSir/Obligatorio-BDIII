import java.io.FileInputStream;    
import java.sql.Connection; 
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.io.IOException;
import java.util.ArrayList;
 
public class Crear_BD
{
	public static void main (String[] args)
	{
		try
		{
			
			Properties pro = new Properties();
			String nomArch = "config/properties.txt";
			pro.load(new FileInputStream(nomArch));
			String driver = pro.getProperty("driver");
			String url = pro.getProperty("url");
			String usuario = pro.getProperty("usuario");
			String password = pro.getProperty("password");
			
			
			/* primer programa de prueba para ejemplo de acceso a MySQL desde Java */
			/* accede a una base de datos de MySQL llamada Prueba que contiene una tabla llamada Personas */
			/* dentro de dicha tabla hay una columna llamada nombre */

			/* 1. cargo dinamicamente el driver de MySQL */
			Class.forName(driver);

			/* 2. una vez cargado el driver, me conecto con la base de datos */
			Connection con = DriverManager.getConnection(url, usuario, password);
			con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			con.setAutoCommit(false);
			
			try {
			String db = "CREATE DATABASE Bedelia";
			Statement stmt1 = con.createStatement();
		    stmt1.executeUpdate(db);
			stmt1.close();
			
			String dbu = "USE Bedelia";
			Statement stmt2 = con.createStatement();
			stmt2.execute(dbu);
			stmt2.close();
			
			String create1 = "CREATE TABLE Alumnos (cedula INT PRIMARY KEY, nombre VARCHAR(45) NOT NULL, apellido VARCHAR(45) NOT NULL)";
			Statement stmt3 = con.createStatement();
			stmt3.executeUpdate(create1);
			stmt3.close();

			String create2 = "CREATE TABLE Registros (numero INT NOT NULL, cedAlu INT NOT NULL, actividad VARCHAR(45) NOT NULL, nota INT NOT NULL, "
					+ " CONSTRAINT pk_numCed PRIMARY KEY(numero, cedAlu), CONSTRAINT fk_cedAlu FOREIGN KEY(cedAlu) REFERENCES Alumnos(cedula))";
			Statement stmt4 = con.createStatement();
			stmt4.executeUpdate(create2);
			stmt4.close();
			
			
			String insert = "INSERT INTO Alumnos VALUES (12345678, 'Matilda', 'Wormwood'), "
					+ "(25554449, 'Charlie', 'Brown'),"
					+ "(39876542, 'Dora', 'Márquez'),"
					+ "(51112223, 'Daniel', 'Mitchell'),"
					+ "(66666666, 'Merlina', 'Addams')";
			Statement stmt5 = con.createStatement();
			stmt5.executeUpdate(insert);
			stmt5.close();
				
			con.commit();
			con.close();
				
			}catch(SQLException e){
				con.rollback();
				e.printStackTrace();
			}	
			
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}	
		catch (IOException e)
		{
			e.printStackTrace();
		}	
	}
}
