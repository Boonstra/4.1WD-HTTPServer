package nl.hanze.web.t41.helper;

import nl.hanze.web.t41.http.HTTPSettings;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class Base64Converter {

    File file;

    String fileName;

    /**
     * Construct the object with the passed file name
     *
     * @param fileName The path and name of the file
     */
    public Base64Converter(String fileName) {

        file          = new File(HTTPSettings.getDocumentRoot(), fileName);
        this.fileName = fileName;
    }

    /**
     * Checks if the file exists
     *
     * @return exists
     */
    public boolean fileExists() {

        return file != null && file.exists();
    }

    /**
     * Encode the file to base64. Returns the filename on failure.
     *
     * @return string
     */
    public String encodeToBase64() {

        if (!fileExists()) {
            return fileName;
        }

        try
        {
            BufferedImage image = ImageIO.read(file);

            String type = "";

            int i = fileName.lastIndexOf('.');

            if (i > 0) {
                type = fileName.substring(i + 1);
            }

            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            ImageIO.write(image, type, bos);

            byte[] imageBytes = bos.toByteArray();

            BASE64Encoder encoder = new BASE64Encoder();

            String imageString = encoder.encode(imageBytes);

            bos.close();

            return imageString;
        }
        catch (IOException e)
        {
            return fileName;
        }
    }
}
