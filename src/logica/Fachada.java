package logica;

import java.io.FileInputStream;  
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import logica.excepciones.*;
import logica.valueObjects.*;

import java.util.ArrayList;

import persistencia.consultas.*;
import poolConexiones.*;

public class Fachada extends UnicastRemoteObject implements IFachada {
	private IPoolConexiones pool;
	
	public Fachada() throws RemoteException, PersistenciaException{
			pool = new PoolConexiones();
	}
	
	public void nuevoAlumno(VOAlumno voA) throws RemoteException, PersistenciaException, YaExisteAlumnoException{
		IConexion con = pool.obtenerConexion(true);
		String msgError = "";
		boolean errorPersistencia = false;
		boolean errorExisteAlumno = false;
		try {
			AccesoBD abd = new AccesoBD();
			if(!abd.existeAlumno(con, voA.getCedula())) {
				abd.ingresoAlumno(con, voA.getCedula(), voA.getNombre(), voA.getApellido());
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
			AccesoBD abd = new AccesoBD();
			if(!abd.existeAlumno(con, cedA)) {
				errorNoExisteAlumno = true;
				msgError = "No existe un alumno con la cedula ingresada";
			}else {
				abd.ingresarRegistro(con, abd.numRegistro(con, cedA), cedA, voR.getActividad(), voR.getNota());
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
			AccesoBD abd = new AccesoBD();
			lista =  abd.devolverAlumnosOrdenados(con);
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
		try {
			AccesoBD abd = new AccesoBD();
			if(!abd.existeAlumno(con, cedA)) {
				errorNoExisteAlumno = true;
				msgError = "No existe un alumno con la cedula ingresada";
				throw new CedulaInexistenteException(msgError);
			}else if(!abd.existeRegistro(con, numR, cedA)){
				errorNoExisteRegistro = true;
				msgError = "No existe un registro con el numero ingresado para el alumno seleccionado";
				throw new RegistroNoExisteException(msgError);
			}else {
				return abd.obtenerRegistro(con, numR, cedA);
				}
			
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
			AccesoBD abd = new AccesoBD();
			if(!abd.existeAlumno(con, cedA)) {
				errorNoExisteAlumno = true;
				msgError = "No existe un alumno con la cedula ingresada";
			}else {
				lista = abd.obtenerRegistrosAlumno(con, cedA);
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
			AccesoBD abd = new AccesoBD();
			if(!abd.existeAlumno(con, cedA)) {
				errorNoExisteAlumno = true;
				msgError = "No existe un alumno con la cedula ingresada";
				return promedio;
			}else {
				int cant = 0;
				List<VORegistroListado> lista = abd.obtenerRegistrosAlumno(con, cedA);
				for(VORegistroListado i : lista) {
					promedio += i.getNota();
					cant++;
				}
				return promedio /= cant;
			}
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
		try {
			AccesoBD abd = new AccesoBD();
			if(!abd.hayAlumnos(con)) {
				errorNingunAlumno = true;
				msgError = "No hay ningun alumno registrado";
				throw new NingunAlumnoException(msgError);
			}else {
				List<VOAlumno> alumnos = abd.devolverAlumnosOrdenados(con);
				VOAlumnoProm alumnoMayorPromedio = new VOAlumnoProm(alumnos.get(0).getCedula(), alumnos.get(0).getNombre(), alumnos.get(0).getApellido(), promedioNotas(alumnos.get(0).getCedula()));
				for(VOAlumno i : alumnos ) {
					if(alumnoMayorPromedio.getPromedio()<promedioNotas(i.getCedula()))
						alumnoMayorPromedio = new VOAlumnoProm(i.getCedula(), i.getNombre(), i.getApellido(), promedioNotas(i.getCedula()));
				}
				return alumnoMayorPromedio;
				}
				
			
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
		try {
			AccesoBD abd = new AccesoBD();
			return abd.obtenerCantidadAlumnosActividadConNota(con, act, nota);
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
			}else
				pool.liberarConexion(con, true);
			
			}
	}
	
	
	
	public VOActividad actividadMasCursada(int cedA) throws RemoteException, PersistenciaException, CedulaInexistenteException, RegistroNoExisteException{
		IConexion con = pool.obtenerConexion(false);
		String msgError = "";
		boolean errorPersistencia = false;
		boolean errorNoExisteAlumno = false;
		boolean errorNoExisteRegistro = false;
		try {
			AccesoBD abd = new AccesoBD();
			if(!abd.existeAlumno(con, cedA)) {
				errorNoExisteAlumno = true;
				msgError = "No existe un alumno con la cedula ingresada";
				throw new CedulaInexistenteException(msgError);
			}else if(!abd.existeAlgunRegistroAlumno(con, cedA)) {
				errorNoExisteRegistro = true;
				msgError = "No existe ninguna actividad para el alumno ingresado";
				throw new RegistroNoExisteException(msgError);
			}else {
				return abd.obtenerActividadMasCursadaPorUnAlumno(con, cedA);
			}
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
}
