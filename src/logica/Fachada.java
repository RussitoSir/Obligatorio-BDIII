package logica;


import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import persistencia.daos.DAOAlumnos;
import logica.excepciones.*;
import logica.valueObjects.*;
import java.util.ArrayList;
import poolConexiones.*;

public class Fachada extends UnicastRemoteObject implements IFachada {
	private IPoolConexiones pool;
	private DAOAlumnos diccionario;
	
	public Fachada() throws RemoteException, PersistenciaException{
			pool = new PoolConexiones();
			diccionario = new DAOAlumnos();
	}
	
	public void nuevoAlumno(VOAlumno voA) throws RemoteException, PersistenciaException, YaExisteAlumnoException{
		IConexion con = pool.obtenerConexion(true);
		String msgError = "";
		boolean errorPersistencia = false;
		boolean errorExisteAlumno = false;
		try {
			if(!diccionario.member(con, voA.getCedula())) {
				Alumno alu = new Alumno(voA.getCedula(), voA.getNombre(), voA.getApellido());
				diccionario.insert(con, alu);
			}else {
				errorExisteAlumno = true;
				msgError = "Ya existe un alumno con la cedula ingresada";
			}
		}
		catch(Exception e) {
			errorPersistencia = true;
			msgError = "Error en acceso a datos";
		}
		finally {
			if(errorPersistencia) {
				pool.liberarConexion(con, false);
				throw new PersistenciaException(msgError);
			}else if(errorExisteAlumno) {
				pool.liberarConexion(con, false);
				throw new YaExisteAlumnoException(msgError);
			}else
				pool.liberarConexion(con, true);
		}
		
	}
	
	
	public void registrarNota(int cedA, VORegistro voR) throws RemoteException, PersistenciaException, CedulaInexistenteException{
		IConexion con = pool.obtenerConexion(true);
		String msgError = "";
		boolean errorPersistencia = false;
		boolean errorNoExisteAlumno = false;
		try {
			if(!diccionario.member(con, cedA)) {
				errorNoExisteAlumno = true;
				msgError = "No existe un alumno con la cedula ingresada";
			}else {
				Alumno alu = diccionario.find(con, cedA);
				Registro registro = new Registro(alu.cantidadRegistros(con) +1, voR.getNota(), voR.getActividad());
				alu.agregarRegistro(con, registro);
			}
		}
		catch(Exception e) {
			errorPersistencia = true;
			msgError = "Error en acceso a datos";
		}
		finally {
			if(errorPersistencia) {
				pool.liberarConexion(con, false);
				throw new PersistenciaException(msgError);
			}else if(errorNoExisteAlumno) {
				pool.liberarConexion(con, false);
				throw new CedulaInexistenteException(msgError);
			}else
				pool.liberarConexion(con, true);
		}
	}
	
	
	public List<VOAlumno> listarAlumnos() throws RemoteException, PersistenciaException {
		IConexion con = pool.obtenerConexion(false);
		String msgError = "";
		boolean errorPersistencia = false;
		List<VOAlumno> lista = new ArrayList<>();
		try {
			return diccionario.listarAlumnos(con);
		}
		catch(Exception e) {
			errorPersistencia = true;
			msgError = "Error en acceso a datos";
			return lista;
		}
		finally {
			if(errorPersistencia) {
				pool.liberarConexion(con, false);
				throw new PersistenciaException(msgError);
			}else
				pool.liberarConexion(con, true);
			}
	}
	
	
	public VORegistro obtenerRegistro(int cedA, int numR) throws RemoteException, PersistenciaException, CedulaInexistenteException, RegistroNoExisteException{
		IConexion con = pool.obtenerConexion(false);
		String msgError = "";
		boolean errorPersistencia = false;
		boolean errorNoExisteAlumno = false;
		boolean errorNoExisteRegistro = false;
		VORegistro resu = null;
		try {
			if(!diccionario.member(con, cedA)) {
				errorNoExisteAlumno = true;
				msgError = "No existe un alumno con la cedula ingresada";
			}else {
				Alumno alu = diccionario.find(con, cedA);
				if(alu.cantidadRegistros(con)!= numR){
					errorNoExisteRegistro = true;
					msgError = "No existe un registro con el numero ingresado para el alumno seleccionado";
			}else {
				resu = alu.obtenerRegistro(con, numR);
				}
			}
			return resu;
			
		}
		
		catch(Exception e) {
			errorPersistencia = true;
			msgError = "Error en acceso a datos";
			throw new PersistenciaException(msgError);
		}
		finally {
			if(errorPersistencia) {
				pool.liberarConexion(con, false);
				throw new PersistenciaException(msgError);
			}else if(errorNoExisteAlumno) {
				pool.liberarConexion(con, false);
				throw new CedulaInexistenteException(msgError);
			}else if(errorNoExisteRegistro) {
				pool.liberarConexion(con, false);
				throw new RegistroNoExisteException(msgError);
			}else
				pool.liberarConexion(con, true);
		}
	}
	
	
	public List<VORegistroListado> listarRegistros	(int cedA) throws RemoteException, PersistenciaException, CedulaInexistenteException{
		IConexion con = pool.obtenerConexion(false);
		String msgError = "";
		boolean errorPersistencia = false;
		boolean errorNoExisteAlumno = false;
		List<VORegistroListado> lista = new ArrayList<>();
		try {
			if(!diccionario.member(con, cedA)) {
				errorNoExisteAlumno = true;
				msgError = "No existe un alumno con la cedula ingresada";
			}else {
				return diccionario.find(con, cedA).listarRegistros(con);
			}
			return lista;
		}
		catch(Exception e) {
			errorPersistencia = true;
			msgError = "Error en acceso a datos";
			return lista;
		}
		finally {
			if(errorPersistencia) {
				pool.liberarConexion(con, false);
				throw new PersistenciaException(msgError);
			}else if(errorNoExisteAlumno) {
				pool.liberarConexion(con, false);
				throw new CedulaInexistenteException(msgError);
			}else
				pool.liberarConexion(con, true);
		}
	}
	
	
	
	public double promedioNotas(int cedA) throws RemoteException, PersistenciaException, CedulaInexistenteException{
		IConexion con = pool.obtenerConexion(false);
		String msgError = "";
		boolean errorPersistencia = false;
		boolean errorNoExisteAlumno = false;
		double promedio = 0.0;
		try {
			if(!diccionario.member(con, cedA)) {
				errorNoExisteAlumno = true;
				msgError = "No existe un alumno con la cedula ingresada";
				return promedio;
			}else 
				return diccionario.find(con, cedA).promedioNotas(con);
		}
		catch(Exception e) {
			errorPersistencia = true;
			msgError = "Error en acceso a datos";
			return promedio;
		}
		finally {
			if(errorPersistencia) {
				pool.liberarConexion(con, false);
				throw new PersistenciaException(msgError);
			}else if(errorNoExisteAlumno) {
				pool.liberarConexion(con, false);
				throw new CedulaInexistenteException(msgError);
			}else
				pool.liberarConexion(con, true);
		}
	}
	
	public VOAlumnoProm alumnoMayorPromedio() throws RemoteException, PersistenciaException, NingunAlumnoException{
		IConexion con = pool.obtenerConexion(false);
		String msgError = "";
		boolean errorPersistencia = false;
		boolean errorNingunAlumno = false;
		VOAlumnoProm resu = null;
		try {
			if(diccionario.esVacio(con)) {
				errorNingunAlumno = true;
				msgError = "No hay ningun alumno registrado";
			}else 
				resu = diccionario.alumnoMayorPromedio(con);
			return resu;
			}
		catch(Exception e) {
			errorPersistencia = true;
			msgError = "Error en acceso a datos";
			return resu;
		}
		finally {
			if(errorPersistencia) {
				pool.liberarConexion(con, false);
				throw new PersistenciaException(msgError);
			}else if(errorNingunAlumno) {
				pool.liberarConexion(con, false);
				throw new NingunAlumnoException(msgError);
			}else
				pool.liberarConexion(con, true);
		}
	}
	
	
	public int contarAlumnos(String act, int nota) throws RemoteException, PersistenciaException {
		IConexion con = pool.obtenerConexion(false);
		String msgError = "";
		boolean errorPersistencia = false;
		int resu = 0;
		try {
			resu = diccionario.contarAlumnos(con, act, nota);
			return resu;
		}
		catch(Exception e) {
			errorPersistencia = true;
			msgError = "Error en acceso a datos";
			return resu;
		}
		finally {
			if(errorPersistencia) {
				pool.liberarConexion(con, false);
				throw new PersistenciaException(msgError);
			}else
				pool.liberarConexion(con, true);
			
			}
	}
	
	
	
	public VOActividad actividadMasCursada(int cedA) throws RemoteException, PersistenciaException, CedulaInexistenteException, NingunaActividadException{
		IConexion con = pool.obtenerConexion(false);
		String msgError = "";
		boolean errorPersistencia = false;
		boolean errorNoExisteAlumno = false;
		boolean errorNingunRegistro = false;
		VOActividad resu = null;
		try {
			if(!diccionario.member(con, cedA)) {
				errorNoExisteAlumno = true;
				msgError = "No existe un alumno con la cedula ingresada";
			}else {
				Alumno alu = diccionario.find(con, cedA);
				if(alu.cantidadRegistros(con) == 0) {
					errorNingunRegistro = true;
					msgError = "No existe ninguna actividad para el alumno ingresado";
				}else 
				resu = alu.actividadMasCursada(con);
			}
			return resu;
		}
		catch(Exception e) {
			errorPersistencia = true;
			msgError = "Error en acceso a datos";
			
		}
		finally {
			if(errorPersistencia) {
				pool.liberarConexion(con, false);
				throw new PersistenciaException(msgError);
			}else if(errorNoExisteAlumno) {
				pool.liberarConexion(con, false);
				throw new CedulaInexistenteException(msgError);
			}else if(errorNingunRegistro) {
				pool.liberarConexion(con, false);
				throw new NingunaActividadException(msgError);
			}else {
				pool.liberarConexion(con, true);
				throw new NingunaActividadException(msgError);
				
			}
				
		
	}
	
	
}
}
