package logica;

import java.rmi.Remote; 
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

import logica.excepciones.*;
import logica.valueObjects.*;

public interface IFachada extends Remote {

	public void nuevoAlumno(VOAlumno voA) throws RemoteException, PersistenciaException, YaExisteAlumnoException;
	
	public void registrarNota(int cedA, VORegistro voR) throws RemoteException, PersistenciaException, CedulaInexistenteException;
	
	public List<VOAlumno> listarAlumnos() throws RemoteException, PersistenciaException;
	
	public VORegistro obtenerRegistro(int cedA, int numR) throws RemoteException, PersistenciaException, CedulaInexistenteException, RegistroNoExisteException;
	
	public List<VORegistroListado> listarRegistros	(int cedA) throws RemoteException, PersistenciaException, CedulaInexistenteException;
	
	public double promedioNotas(int cedA) throws RemoteException, PersistenciaException, CedulaInexistenteException;
	
	public VOAlumnoProm alumnoMayorPromedio() throws RemoteException, PersistenciaException, NingunAlumnoException;
	
	public int contarAlumnos(String act, int nota) throws RemoteException, PersistenciaException;
	
	public VOActividad actividadMasCursada(int cedA) throws RemoteException, PersistenciaException, CedulaInexistenteException, RegistroNoExisteException;
	
	

}
