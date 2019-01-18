package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import misc.Banco;

public class Trader {
    public String nome;
    public LocalDateTime  ultimo;
    
    
    public static List<Trader> listar() throws Exception{
        List<Trader> saida = new ArrayList<>();

        PreparedStatement stmt = Banco.prepare("select coalesce(ultimo,'01/01/1971'::timestamp without time zone) as ultimo, coalesce(nome,'') as nome from traders");
        ResultSet rs = stmt.executeQuery();
        while (rs.next()){
           Trader t = new Trader();
           t.ultimo = rs.getTimestamp("ultimo").toLocalDateTime();
           t.nome = rs.getString("nome");
           saida.add(t);
        }
            

        
        return saida;
    }
    
    public boolean atualizar() throws Exception{
        PreparedStatement stmt = Banco.prepare("update traders set ultimo = ? where nome=?");
        stmt.setTimestamp(1, java.sql.Timestamp.from(this.ultimo.toInstant(ZoneOffset.UTC)));
        stmt.setString(2, nome);
        stmt.execute();
        return true;
    }
}

