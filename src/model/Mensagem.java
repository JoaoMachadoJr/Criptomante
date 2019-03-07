package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import misc.Banco;
import misc.Log;

public class Mensagem {
	
	public String texto;
	public LocalDateTime data;
	public String topico_url;
	public UUID mensagem;
	
	
	
	public void inserir() {
		PreparedStatement stmt;
		try {
			stmt = Banco.prepare("insert into mensagens (texto, data, topico_url) values (?, ?, ?) returning mensagem");
			stmt.setString(1, texto);
			stmt.setTimestamp(2, java.sql.Timestamp.from(this.data.toInstant(ZoneOffset.UTC)));
			stmt.setString(3, topico_url);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				this.mensagem = UUID.fromString(rs.getString("mensagem"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.log(e);;
		}
	}
        
        
        public static void indexar() {
		PreparedStatement stmt;
                int i = 0;
		try {
			stmt = Banco.prepare("select count(*) as qnt from mensagens m join vw_dias_com_variacao d on d.data=m.data::date where not indexado");
			ResultSet rs = stmt.executeQuery();
                        rs.next();
                        int qnt = rs.getInt("qnt");
                        boolean terminou = false;
                        Log.log("Indexados: "+i+"/"+qnt);
                        
                        while (!terminou){
                          stmt = Banco.prepare("select fn_analise_textual_indexar_mensagens_v2(10000) as processou");
                          rs = stmt.executeQuery();
                          rs.next();
                          terminou = !rs.getBoolean("processou");
                          i = i + 10000;
                          Log.log("Indexados: "+i+"/"+qnt);
                        }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.log(e);;
		}
	}
}
