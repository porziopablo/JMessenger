package emisora;

import java.util.Observer;

import mensaje.Mensaje;

public interface IEmisionMensaje
{
    void emitirMensaje(Mensaje mensaje);
    void addObserver(Observer observe);
    void recibirConfirmacion();
}
