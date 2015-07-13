package billund.util;

import billund.reference.Reference;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URL;

public class FileHelpler
{
    public static void copyFromJar(Class<?> classFromJar, String fileSource, String fileDestination)
    {
        LogHelper.debug("Copying file " + fileSource + " from jar");

        URL source = classFromJar.getResource("/assets/" + Reference.ID + "/" + fileSource);
        File destination = new File(fileDestination);

        try
        {
            FileUtils.copyURLToFile(source, destination);
        } catch (IOException e)
        {
            LogHelper.warn("Couldn't load file from jar!");
            LogHelper.info("This is a bug, please report it to the mod author!");
        }
    }

    public static InputStream getJsonFile(Class<?> classFromJar, String jarSource, String folderSource, boolean alwaysCopy)
    {
        File dataFile = new File(folderSource);

        if (!dataFile.isFile() || alwaysCopy)
        {
            copyFromJar(classFromJar, jarSource, folderSource);

            // If the file was copied, get the file again
            dataFile = new File(folderSource);
        }

        if (dataFile.isFile())
        {
            LogHelper.debug("JSON file exists");
            InputStream stream;
            try
            {
                stream = new FileInputStream(dataFile);
            } catch (FileNotFoundException e)
            {
                throw new RuntimeException(e);
            }
            return stream;
        }
        throw new RuntimeException(); // this should never be reached
    }
}
