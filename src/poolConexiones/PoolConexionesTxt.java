package poolConexiones;

import logica.excepciones.PersistenciaException;

public class PoolConexionesTxt implements IPoolConexiones {

    private int lectoresActivos;
    private boolean escritorActivo;
    private int escritoresEsperando;

    public PoolConexionesTxt() {
        lectoresActivos = 0;
        escritorActivo = false;
        escritoresEsperando = 0;
    }

    public synchronized IConexion obtenerConexion(boolean modifica) throws PersistenciaException 
    {

        ConexionTxt conexion = null;

        try {
            if (modifica) {
                escritoresEsperando++;

                try {
                    while (escritorActivo || lectoresActivos > 0) {
                        wait();
                    }
                } catch (InterruptedException e) {
                    escritoresEsperando--;
                    notifyAll();
                    Thread.currentThread().interrupt();
                    throw new PersistenciaException("Error en el acceso a datos");
                }
                escritoresEsperando--;
                escritorActivo = true;

            } else {
                while (escritorActivo || escritoresEsperando > 0) {
                    wait();
                }
                lectoresActivos++;
            }

            conexion = new ConexionTxt(modifica);
            return conexion;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            notifyAll();
            throw new PersistenciaException("Error en el acceso a datos");
        }
    }

    public synchronized void liberarConexion(IConexion con, boolean ok) throws PersistenciaException 
    {
        ConexionTxt conexion = (ConexionTxt) con;

        if (conexion.modifica()) {
            escritorActivo = false;
        } else {
            lectoresActivos--;
        }
        notifyAll();
    }
}
