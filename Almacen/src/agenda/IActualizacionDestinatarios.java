package agenda;

import java.util.Iterator;

import java.util.TreeMap;

import usuarios.Destinatario;

public interface IActualizacionDestinatarios
{
    TreeMap<String, Destinatario> actualizarDestinatarios();
}
