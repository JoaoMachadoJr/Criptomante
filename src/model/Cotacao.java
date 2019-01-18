package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.UUID;

import misc.Banco;

public class Cotacao {
    public UUID cotacao;
    public LocalDateTime data;
    public Float valor;
    public int transacoes;

    public Cotacao() {
        this.cotacao = UUID.randomUUID();
    }
    
    public void atualizar() throws Exception{
        PreparedStatement stmt = Banco.prepare("select * from cotacoes where data=?");
        stmt.setTimestamp(1, java.sql.Timestamp.valueOf(data));
        ResultSet rs = stmt.executeQuery();
        if (rs.next()){
            this.valor = ((this.valor*this.transacoes)+(rs.getFloat("valor")*rs.getInt("transacoes")))/(this.transacoes+rs.getInt("transacoes"));
            this.transacoes= this.transacoes+rs.getInt("transacoes");
            this.cotacao = UUID.fromString(rs.getString("cotacao"));
            stmt = Banco.prepare("update cotacoes set transacoes=?, valor=? where cotacao=?::uuid");
            stmt.setInt(1, transacoes);
            stmt.setFloat(2, this.valor);
            stmt.setString(3, this.cotacao.toString());
            stmt.execute();
        }
        else{
        	stmt.close();
            stmt = Banco.prepare("insert into cotacoes (transacoes, valor, data) values (?, ?, ?) returning cotacao");
            stmt.setInt(1, transacoes);
            stmt.setFloat(2, valor);
            stmt.setTimestamp(3, java.sql.Timestamp.valueOf(data));
            rs = stmt.executeQuery();
            if (rs.next()){
                cotacao = UUID.fromString(rs.getString("cotacao"));
            }
           
        }
        stmt.close();
        
    }
    
    
    
}

