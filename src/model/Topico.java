package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import misc.Banco;
import misc.Log;

public class Topico {
	
	public String website;
	public String url;
	public LocalDateTime data;
	public LocalDateTime ultima_atualizacao;
	public LocalDateTime data_processamento;
	
	public void integrar() {
		try {
			PreparedStatement stmt = Banco.prepare("select * from topicos where url=?");
			stmt.setString(1, this.url);
			ResultSet rs = stmt.executeQuery();
			if (!rs.next()){
				stmt.close();
				stmt = Banco.prepare("insert into topicos (website, url) values (?, ?)");
				stmt.setString(1, this.website);
	            stmt.setString(2, this.url);
	            stmt.execute();
			 }
		} catch (Exception e) {
			Log.log(e);
		}
	}
	
	public static List<Topico> listar_nao_processados(String website){
        List<Topico> saida = new ArrayList<>();
            
        PreparedStatement stmt;
		try {
			stmt = Banco.prepare("select website, url from topicos where data_processamento is null and website=? order by url");
			
			stmt.setString(1, website);
			ResultSet rs = stmt.executeQuery();
	        while (rs.next()){
	        	Topico t = new Topico();
	           t.website = rs.getString("website");
	           t.url = rs.getString("url");
	           saida.add(t);
	        }
		} catch (Exception e) {
			Log.log(e);
		}      
        return saida;
    }
	
	public void atualizar_data() {
		PreparedStatement stmt;
		try {
			stmt = Banco.prepare("update topicos set data=?, ultima_atualizacao=?  where url=?");
			stmt.setTimestamp(1, java.sql.Timestamp.from(this.data.toInstant(ZoneOffset.UTC)));
			stmt.setTimestamp(2, java.sql.Timestamp.from(this.ultima_atualizacao.toInstant(ZoneOffset.UTC)));
                        stmt.setString(3, url);
			stmt.execute();
		} catch (Exception e) {
			Log.log(e);
		}      
	}
        
        public void atualizar_data_ultima_atualizacao() {
		PreparedStatement stmt;
		try {
			stmt = Banco.prepare("update topicos set ultima_atualizacao=? where url=?");
			stmt.setTimestamp(1, java.sql.Timestamp.from(this.ultima_atualizacao.toInstant(ZoneOffset.UTC)));
			stmt.setString(2, url);
			stmt.execute();
		} catch (Exception e) {
			Log.log(e);
		}      
	}
        
        public void atualizar_data_processamento() {
		PreparedStatement stmt;
		try {
			stmt = Banco.prepare("update topicos set data_processamento=?  where url=?");
			stmt.setTimestamp(1, java.sql.Timestamp.from(this.data_processamento.toInstant(ZoneOffset.UTC)));
                        stmt.setString(2, url);
			stmt.execute();
		} catch (Exception e) {
			Log.log(e);
		}      
	}
    

}
