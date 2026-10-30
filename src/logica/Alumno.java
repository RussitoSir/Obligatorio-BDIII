package logica;
import persistencia.daos.DAORegistros;
import poolConexiones.IConexion;
import logica.excepciones.PersistenciaException;
import logica.valueObjects.VORegistro;
import logica.valueObjects.VORegistroListado;
import logica.valueObjects.VOActividad;
import java.util.ArrayList;
import java.util.List;

public class Alumno {
	private int cedula;
	private String nombre;
	private String apellido;
	private DAORegistros secuencia;
	
	public Alumno(int cedula, String nombre, String apellido) {
			this.cedula = cedula;
			this.nombre = nombre;
			this.apellido = apellido;
			this.secuencia = new DAORegistros(cedula);
	}
	
	
	public int getCedula() {
		return cedula;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public String getApellido() {
		return apellido;
	}
	
	public int cantidadRegistros(IConexion icon) throws PersistenciaException {
			return secuencia.largo(icon);
	}
	
	public void agregarRegistro(IConexion icon, Registro reg) throws PersistenciaException {
		secuencia.insback(icon, reg);
	}
	
	public VORegistro obtenerRegistro(IConexion icon, int numR)throws PersistenciaException {
		Registro registro = secuencia.kesimo(icon, numR);
		return new VORegistro(registro.getActividad(), registro.getNota());
	}
	
	public List<VORegistroListado> listarRegistros(IConexion icon) throws PersistenciaException {
		return secuencia.listarRegistros(icon);
	}
	
	public double promedioNotas(IConexion icon) throws PersistenciaException {
		return secuencia.promedioNotas(icon);
	}
	
	public VOActividad actividadMasCursada(IConexion icon) throws PersistenciaException {
		return secuencia.actividadMasCursada(icon);
	}
}
