import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Main
{
    public static String read(String filePath)throws IOException
    {
        StringBuilder text=new StringBuilder();
        try(BufferedReader x = new BufferedReader (new FileReader(filePath)))
        {
            String line;
            while((line=x.readLine())!=null)
            {
                text.append(line).append("\n");
            }
        }
        return text.toString();
    }

    public static String semne(String text)
    {
        return text.replaceAll("[^a-zA-Z0-9\\s]","");
    }

    public static String lowerCase(String text)
    {
        return text.toLowerCase();
    }

    public static void main(String[] args)
    {
        try
        {
            String filePath="fisier.txt";
            String text=read(filePath);
            System.out.println("Continut initial:\n"+text);
            text=semne(text);
            System.out.println("dupa eliminarea semnelor de punctuatie:\n"+text);
            text=lowerCase(text);
            System.out.println("dupa transformarea caracterelor mari in caractere mici:\n"+text);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
