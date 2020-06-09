package persistencia;

public class PersistenciaException extends Exception
{  
    public PersistenciaException(String string, Throwable throwable, boolean b, boolean b1)
    {
        super(string, throwable, b, b1);
    }

    public PersistenciaException(Throwable throwable)
    {
        super(throwable);
    }

    public PersistenciaException(String string, Throwable throwable)
    {
        super(string, throwable);
    }

    public PersistenciaException(String string)
    {
        super(string);
    }

    public PersistenciaException()
    {
        super();
    }
}
