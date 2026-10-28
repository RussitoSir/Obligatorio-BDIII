package logica.excepciones;

public class CedulaInexistenteException extends Exception {
    public CedulaInexistenteException(String mensaje) {
        super(mensaje);
    }
}
