package misc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Banco {
    
    public static String servidor = "localhost";
    public static String porta = "5432";
    public static String usuario = "postgres";
    public static String senha = "160031";
    public static String banco = "principal";
    
    public static HashMap<Thread, Connection> conexoes = new HashMap<>();
    
    public static PreparedStatement prepare(String sql) throws Exception{
        
        try {
            Connection c = null;
            if (!conexoes.containsKey(Thread.currentThread())){
                conexoes.put(Thread.currentThread(), AbreConexao());
            }
            
            c = conexoes.get(Thread.currentThread());
            return c.prepareStatement(sql);
        } catch (Exception ex) {
            Logger.getLogger(Banco.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception("Erro ao abrir banco: "+ex.getMessage() );
        } 
    }
    
    public static Connection AbreConexao() throws Exception{
      Class.forName("org.postgresql.Driver");
      return DriverManager
            .getConnection("jdbc:postgresql://"+servidor+":"+porta+"/"+banco,
            usuario, senha);
    }
    
}
