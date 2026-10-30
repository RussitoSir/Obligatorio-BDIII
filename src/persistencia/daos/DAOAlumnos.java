package persistencia.daos;

import poolConexiones.Conexion; 
import poolConexiones.IConexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import persistencia.consultas.*;
import logica.excepciones.PersistenciaException;
import logica.Alumno;
import logica.valueObjects.VOAlumno;
import logica.valueObjects.VOAlumnoProm;
import logica.valueObjects.VORegistroListado;

public class DAOAlumnos {
	
	public DAOAlumnos() {
		
	}
	
	
	public boolean member(IConexion icon, int cedA) throws PersistenciaException {
		boolean existe = false;
		Consultas consultas = new Consultas();
		Connection con = ((Conexion)icon).getConnection();
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
		Connection con = ((Conexion)icon).getConnection();
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
		Connection con = ((Conexion)icon).getConnection();
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
		Connection con = ((Conexion)icon).getConnection();
		try {
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(consultas.devolverAlumnosOrdenados());
		if(rs.next())
			es = false;
		return es;
		}
		catch(SQLException e) {
			throw new PersistenciaException("Error de persistencia");
		}
	}
	
	public List<VOAlumno> listarAlumnos(IConexion icon) throws PersistenciaException {
		Consultas consultas = new Consultas();
		Connection con = ((Conexion)icon).getConnection();
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(consultas.devolverAlumnosOrdenados());
			List<VOAlumno> alumnos = new ArrayList<>();
			while(rs.next()) {
				VOAlumno actual = new VOAlumno(rs.getInt(1), rs.getString(2), rs.getString(3));
				alumnos.add(actual);
			}
			rs.close();
			st.close();
			return alumnos;
			}
			catch(SQLException e) {
				throw new PersistenciaException("Error de persistencia");
			}
	}
	
	public VOAlumnoProm alumnoMayorPromedio(IConexion icon) throws PersistenciaException {
		List<VOAlumno> alumnos = listarAlumnos(icon);
		Alumno alumnoMayorPromedio = new Alumno(alumnos.get(0).getCedula(), alumnos.get(0).getNombre(), alumnos.get(0).getApellido());
		for(VOAlumno i : alumnos ) {
			Alumno alumnoActual = new Alumno(i.getCedula(), i.getNombre(), i.getApellido());
			if(alumnoMayorPromedio.promedioNotas(icon)<alumnoActual.promedioNotas(icon))
				alumnoMayorPromedio = alumnoActual;
		}
		return new VOAlumnoProm(alumnoMayorPromedio.getCedula(), alumnoMayorPromedio.getNombre(), alumnoMayorPromedio.getApellido(), alumnoMayorPromedio.promedioNotas(icon));
		
}
	
	public int contarAlumnos(IConexion icon, String act, int nota) throws PersistenciaException {
		List<VOAlumno> alumnos = listarAlumnos(icon);
		int cant = 0;
		for(VOAlumno i : alumnos ) {
			Alumno alumnoActual = new Alumno(i.getCedula(), i.getNombre(), i.getApellido());
			List<VORegistroListado> registros = alumnoActual.listarRegistros(icon);
			for(VORegistroListado j : registros) {
				if(j.getActividad() == act && j.getNota() == nota)
					cant++;
			}	
	}
		return cant;
}
	
}
