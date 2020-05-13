package agenda;

import java.util.Iterator;

import java.util.TreeMap;

import usuario.Destinatario;

public interface IActualizacionDestinatarios
{
    TreeMap<String, Destinatario> actualizarDestinatarios();
}
