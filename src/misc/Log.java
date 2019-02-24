package misc;

import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Log {
    public static void log(String texto, boolean salvar ){
        try {

            
            System.out.println(LocalDateTime.now().toString()+"  "+ texto+" \n{"+Thread.currentThread().getClass().getName()+"} ==>"+org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(new Throwable()));    
            if (!salvar)
            {
            	return;
            }
            PreparedStatement stmt = Banco.prepare("insert into log(texto, trace, thread) values (?,?,?)");
            stmt.setString(1, texto);
            stmt.setString(2, Thread.currentThread().getStackTrace().toString());
            stmt.setString(3, Thread.currentThread().getClass().getName());
            stmt.execute();
        } catch (Exception ex) {
            Logger.getLogger(Log.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public static void log(Exception e){
        try {
            
            System.out.println("\u001B[31m"+LocalDateTime.now().toString()+"  "+ "ERRO: "+e.getMessage()+" \n{"+Thread.currentThread().getClass().getName()+"} ==>"+org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(e)+"\u001B[0m");
            
            PreparedStatement stmt = Banco.prepare("insert into log(texto, trace, thread) values (?,?,?)");
            stmt.setString(1, "ERRO: "+e.getMessage());
            stmt.setString(2, Thread.currentThread().getStackTrace().toString());
            stmt.setString(3, Thread.currentThread().getClass().getName());
            stmt.execute();
        } catch (Exception ex) {
            Logger.getLogger(Log.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public static void log(String texto){
        log(texto,false);
    }
    
}