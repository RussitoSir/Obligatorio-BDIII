package logica;
import persistencia.daos.DAORegistros;

public class Alumno {
	private int cedula;
	private String nombre;
	private String apellido;
	private DAORegistros secuencia;
	
	public Alumno(int cedula, String nombre, String apellido) {
			this.cedula = cedula;
			this.nombre = nombre;
			this.apellido = apellido;
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
	
	public int cantidadRegistros() {
			return secuencia.largo()
	}
}
