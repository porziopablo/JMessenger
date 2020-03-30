package vista;

import java.io.File;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sonido 
{
    private Clip clip;
    
    public Sonido(String archivo) 
    {
        try 
        {
            File file = new File(archivo); /* FORMATO WAV */
            if (file.exists()) 
            {
                AudioInputStream sound = AudioSystem.getAudioInputStream(file);
                clip = AudioSystem.getClip();
                clip.open(sound);
            }
            else 
                throw new RuntimeException("No se puede reproducir el sonido: " + archivo);
        }
        catch (UnsupportedAudioFileException e) 
        {
            System.out.println("No se puede reproducir el sonido: " + e.getMessage());
        }
        catch (IOException e) 
        {
            System.out.println("No se puede reproducir el sonido: " + e.getMessage());
        }
        catch (LineUnavailableException e) 
        {
            System.out.println("No se puede reproducir el sonido: " + e.getMessage());
        }
    }

    public void loop()
    {
        clip.setFramePosition(0);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    public void stop()
    {
        clip.stop();
    }
}