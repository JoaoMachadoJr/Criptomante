package threads;

import org.jsoup.Jsoup;

import misc.Config;
import misc.Log;
import model.Previsao;

public class Realizar_Previsoes extends Thread {
	public static int alcance_em_horas = 720;
	
	@Override
    public void run(){
		while (true) {
			try {
				Log.log("Começando a atualizar previsġes", false);
				Previsao.Realiza_Previsao_Numerica(Config.intervalo_em_horas, Config.alcance_em_horas, valor_dolar());
				Log.log("Previsġes atualizadas", false);
				Thread.sleep(1000*60*10);
			} catch (Exception e) {
				Log.log(e);
			}
			
			
			
			
		}
	}
	public static float valor_dolar() throws Exception{
		String url = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";
		String[] xml = Jsoup.connect(url).get().html().split("\n");
		float real=1;
		float dolar=1;
		for (int i = 0; i < xml.length; i++) {
			String linha = xml[i];
			if (linha.contains("<Cube currency=\"BRL\" rate=\"")) {
				real = Float.parseFloat(linha.substring(linha.indexOf("<Cube currency=\"BRL\" rate=\"")+"<Cube currency=\"BRL\" rate=\"".length(),
														linha.lastIndexOf("\"")));
			}
			if (linha.contains("<Cube currency=\"USD\" rate=\"")) {
				dolar = Float.parseFloat(linha.substring(linha.indexOf("<Cube currency=\"USD\" rate=\"")+"<Cube currency=\"USD\" rate=\"".length(),
														linha.lastIndexOf("\"")));
			}
			
		}
		System.out.println("Valor do dolar: "+real/dolar);
		return real/dolar;
	}
	


	
}
