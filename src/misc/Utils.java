package misc;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.GZIPInputStream;

public class Utils {
    public static void Descompactar(String entrada, String saida) throws Exception{
        byte[] buffer = new byte[1024];

        FileInputStream fileIn = new FileInputStream(entrada);
        GZIPInputStream gZIPInputStream = new GZIPInputStream(fileIn);
        FileOutputStream fileOutputStream = new FileOutputStream(saida);
        int bytes_read;
        while ((bytes_read = gZIPInputStream.read(buffer)) > 0) {
            fileOutputStream.write(buffer, 0, bytes_read);
        }
        gZIPInputStream.close();
        fileOutputStream.close();


    }
    
}
