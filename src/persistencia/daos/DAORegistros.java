package persistencia.daos;
import java.sql.Connection; 
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import logica.excepciones.PersistenciaException;
import logica.valueObjects.VOActividad;
import logica.valueObjects.VORegistroListado;
import persistencia.consultas.*;
import poolConexiones.IConexion;
import poolConexiones.Conexion;
import logica.Registro;


public class DAORegistros {
		private int cedAlumno;
		
		public DAORegistros(int cedA) {
			cedAlumno = cedA;
		}

		public int largo(IConexion icon) throws PersistenciaException {
			Consultas consultas = new Consultas();
			Connection con =((Conexion)icon).getConnection();
			try {
				PreparedStatement ps = con.prepareStatement(consultas.numRegistro());
				ps.setInt(1, cedAlumno);
				ResultSet rs = ps.executeQuery();
				rs.next();
				int numero = rs.getInt(1);
				rs.close();
				ps.close();
				return numero;
				}
				catch(SQLException e) {
					throw new PersistenciaException("Error de persistencia");
					
				}
		}
		
		public void insback(IConexion icon, Registro reg) throws PersistenciaException {
			Consultas consultas = new Consultas();
			Connection con = icon.getConnection();
			try {
				PreparedStatement ps = con.prepareStatement(consultas.ingresarRegistro());
				ps.setInt(1, reg.getNumero());
				ps.setInt(2, cedAlumno);
				ps.setString(3, reg.getActividad());
				ps.setInt(4, reg.getNota());
				ps.executeUpdate();
				ps.close();
				}
				catch(SQLException e) {
					throw new PersistenciaException("Error de persistencia");
				}
		}
		
		public Registro kesimo(IConexion icon, int numR) throws PersistenciaException {
			Consultas consultas = new Consultas();
			Connection con = icon.getConnection();
			try {
			PreparedStatement ps = con.prepareStatement(consultas.existeRegistro());
			ps.setInt(1, numR);
			ps.setInt(2, cedAlumno);
			ResultSet rs = ps.executeQuery();
			rs.next();
			Registro registro = new Registro(numR,rs.getInt(4),rs.getString(3));
			rs.close();
			ps.close();
			return registro;
			}
			catch(SQLException e) {
				throw new PersistenciaException("Error de persistencia");
			}
		}
		
		public List<VORegistroListado> listarRegistros(IConexion icon) throws PersistenciaException{
			Consultas consultas = new Consultas();
			Connection con = icon.getConnection();
			try {
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(consultas.obtenerRegistrosAlumno());
				List<VORegistroListado> registros = new ArrayList<>();
				while(rs.next()) {
					VORegistroListado actual = new VORegistroListado(rs.getString(3), rs.getInt(4), rs.getInt(1));
					registros.add(actual);
				}
				rs.close();
				st.close();
				return registros;
				}
				catch(SQLException e) {
					throw new PersistenciaException("Error de persistencia");
				}
		}
		
		public double promedioNotas(IConexion icon) throws PersistenciaException {
			double promedio = 0.0;
			int cant = 0;
			List<VORegistroListado> lista = listarRegistros(icon);
			for(VORegistroListado i : lista) {
				promedio += i.getNota();
				cant++;
				}
			if(lista.size()>0) {
			return promedio /= cant;
			}else
			return promedio;
}
		
		public VOActividad actividadMasCursada(IConexion icon) throws PersistenciaException {
			Consultas consultas = new Consultas();
			Connection con = icon.getConnection();
			try {
			PreparedStatement ps = con.prepareStatement(consultas.obtenerActividadMasCursadaPorUnAlumno());
			ps.setInt(1, cedAlumno);
			ResultSet rs = ps.executeQuery();
			rs.next();
			VOActividad actividad = new VOActividad(rs.getString(1), rs.getInt(2));
			rs.close();
			ps.close();
			return actividad;
			}
			catch(SQLException e) {
				throw new PersistenciaException("Error de persistencia");
			}
		}
		
}
