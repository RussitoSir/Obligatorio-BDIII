package poolConexiones;

public class ConexionTxt implements IConexion{
	
	private boolean modifica;
	
	 public ConexionTxt(boolean mod) {
	        modifica = mod;
	    }

	    public boolean modifica() {
	        return modifica;
	    }
}
