package poolConexiones;

import logica.excepciones.PersistenciaException;

public interface IPoolConexiones {
	
	public  IConexion obtenerConexion(boolean modifica)throws PersistenciaException;
	
	public  void liberarConexion(IConexion con, boolean ok) throws PersistenciaException;

}
