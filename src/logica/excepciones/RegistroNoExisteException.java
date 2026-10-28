package logica.excepciones;

public class RegistroNoExisteException extends Exception {
    public RegistroNoExisteException(String mensaje) {
        super(mensaje);
    }
}
