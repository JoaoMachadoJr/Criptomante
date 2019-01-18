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
}
