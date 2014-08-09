package Karaoke.src;


import java.io.*;

public class FiltreExtension
        extends javax.swing.filechooser.FileFilter
{
    private String extension;
    private String description;

    public FiltreExtension(String extension, String description)
    {
        if (extension.indexOf('.') == -1)
            extension = "." + extension;
        this.extension = extension;
        this.description = description;
    }

    public boolean accept(File fichier)
    {
        if (fichier.getName().endsWith(extension))
            return true;
        else if (fichier.isDirectory())
            return true;
        return false;
    }

    public String getDescription()
    {
        return this.description + "(*" + extension + ")";
    }
}
