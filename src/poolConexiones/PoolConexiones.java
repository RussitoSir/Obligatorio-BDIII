package poolConexiones;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import logica.excepciones.PersistenciaException;

import java.sql.Connection;

public class PoolConexiones implements IPoolConexiones{
	private String driver;
	private String url;
	private String user;
	private String password;
	private int nivelTransaccionalidad;
	private Conexion[] conexiones;
	private int tamanio;
	private int creadas;
	private int tope;
	
	
	public PoolConexiones() throws PersistenciaException{

		try {
			Properties properties = new Properties();
			String nomArch = "config/properties.txt";
			properties.load(new FileInputStream(nomArch));
			this.driver = properties.getProperty("driver");
			this.url = properties.getProperty("url");
			this.user = properties.getProperty("usuario");
			this.password = properties.getProperty("password");
			this.nivelTransaccionalidad = 4;
			this.tamanio = 3;
			this.creadas = 0;
			this.tope = -1;
			this.conexiones = new Conexion[tamanio];
		}
	
		catch (IOException e)
		{
			throw new PersistenciaException("Error en el acceso a datos");
		}
	}
	
	public synchronized IConexion obtenerConexion(boolean modifica) throws PersistenciaException{
		Conexion conexion = null;
	    try {
	    	while(conexion ==null) {
	        while (tope < 0 && creadas >= tamanio)
	            wait(); 
	        
	        if (tope >= 0) {
	            conexion = conexiones[tope];
	            tope--;} 
	        else if (creadas < tamanio) {
	            Connection con = DriverManager.getConnection(url, user, password);
	            conexion = new Conexion(con);
	            creadas++;}
	        
	        if (conexion != null) {
	            Connection con = conexion.getConnection();
	            con.setAutoCommit(!modifica);
	            con.setTransactionIsolation(nivelTransaccionalidad);
	        }
	    	}
	        
	    } catch (InterruptedException | SQLException e) {
	        throw new PersistenciaException("Error en el acceso a datos");
	    }
	    
	    return conexion;
	}
	
	
	public synchronized void liberarConexion(IConexion con, boolean ok) throws PersistenciaException{
	     try {
	           if (ok) {
	        	   ((Conexion)con).getConnection().commit();
	        	}else 
	        		((Conexion)con).getConnection().rollback();
	            tope++;
	            conexiones[tope] = (Conexion) con;
	            notify();
	            
	        } catch (SQLException e) {
	        	throw new PersistenciaException("Error en el acceso a datos");
	        }
	    }
	}
	

