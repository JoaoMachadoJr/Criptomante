package model;

import java.sql.PreparedStatement;
import java.time.LocalDateTime;

import misc.Banco;

public class Previsao {
	public LocalDateTime data;
	public int confianca;
	public String label;
	public float valor;
	
	
	public static void Realiza_Previsao_Numerica(int intervalo_Em_Horas, int alcance_Em_Horas, float taxa_cambio) throws Exception {
		PreparedStatement stmt = Banco.prepare("select * from fn_nprevisao(?,?);");
		stmt.setInt(1, intervalo_Em_Horas);
		stmt.setInt(2, alcance_Em_Horas);
		stmt.executeQuery();
		stmt.close();
		
		stmt = Banco.prepare("update temp.nprevisao set valor=valor*?");
		stmt.setFloat(1, taxa_cambio);
		stmt.execute();
		stmt.close();
	}
	
}
