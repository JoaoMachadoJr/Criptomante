package main;

import crawlers.Crawler_reddit_bitcoin;
import misc.Config;
import misc.Log;
import threads.Consulta_Traders;
import threads.Realizar_Previsoes;

public class Main {
	
    public static void main(String[] args) {
    	Realizar_Previsoes realizar_Previsoes = new Realizar_Previsoes();
    	Consulta_Traders consulta_Traders = new Consulta_Traders();
    	
    	//realizar_Previsoes.start();
    	//consulta_Traders.start();
    	
    	new Crawler_reddit_bitcoin().processar_website();;
    	try {
	    	while (realizar_Previsoes.isAlive() || consulta_Traders.isAlive()) {
					Thread.sleep(1000*Config.sleep_main);
	    	}
    	} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			Log.log(e);;
		}
    	
    }

}
