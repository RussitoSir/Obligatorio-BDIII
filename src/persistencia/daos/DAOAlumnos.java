package persistencia.daos;

import poolConexiones.IConexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import persistencia.consultas.*;
import logica.excepciones.PersistenciaException;
import logica.Alumno;

public class DAOAlumnos {
	
	
	public boolean member(IConexion icon, int cedA) throws PersistenciaException {
		boolean existe = false;
		Consultas consultas = new Consultas();
		Connection con = icon.getConnection();
		try {
		PreparedStatement ps = con.prepareStatement(consultas.existeAlumno());
		ps.setInt(1, cedA);
		ResultSet rs = ps.executeQuery();
		if(rs.next())
			existe=true;
		rs.close();
		ps.close();
		return existe;
		}
		catch(SQLException e) {
			throw new PersistenciaException("Error de persistencia");
		}
	}
	
	public void insert(IConexion icon, Alumno alu) throws PersistenciaException {
		Consultas consultas = new Consultas();
		Connection con = icon.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(consultas.ingresoAlumno());
			ps.setInt(1, alu.getCedula());
			ps.setString(2, alu.getNombre());
			ps.setString(3, alu.getApellido());
			ps.executeUpdate();
			ps.close();
			}
			catch(SQLException e) {
				throw new PersistenciaException("Error de persistencia");
			}
	}
	
	public Alumno find(IConexion icon, int cedA) throws PersistenciaException {
		Consultas consultas = new Consultas();
		Connection con = icon.getConnection();
		try {
		PreparedStatement ps = con.prepareStatement(consultas.existeAlumno());
		ps.setInt(1, cedA);
		ResultSet rs = ps.executeQuery();
		rs.next();
		Alumno alu = new Alumno(rs.getInt(1), rs.getString(2), rs.getString(3));
		rs.close();
		ps.close();
		return alu;
		}
		catch(SQLException e) {
			throw new PersistenciaException("Error de persistencia");
		}
	}
	
	public boolean esVacio(IConexion icon) throws PersistenciaException {
		boolean es = true;
		Consultas consultas = new Consultas();
		Connection con = icon.getConnection();
		try {
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(consultas.devolverAlumnosOrdenados());
		if(rs.next())
			es = false;
		return es;
		}
		catch(SQLException e) {
			throw new PersistenciaException("Error en la conexion con la Base de Datos");
		}
	}
}
