package main;

import crawlers.Crawler;
import crawlers.Crawler_reddit_bitcoin;
import crawlers.Crawler_reddit_bitcoinmarkets;
import misc.Config;
import misc.Log;
import model.Mensagem;
import threads.Consulta_Traders;
import threads.Realizar_Previsoes;

public class Main {
	
    public static void main(String[] args) {
        Mensagem.indexar();
    	Realizar_Previsoes realizar_Previsoes = new Realizar_Previsoes();
    	Consulta_Traders consulta_Traders = new Consulta_Traders();
        Crawler cr_reddit_bitcoin = null;
        Crawler cr_reddit_bitcoinmarkets = null;
    	realizar_Previsoes.start();
    	consulta_Traders.start();
    	try {
	    	while (true) {
                    if ((cr_reddit_bitcoin ==null) || (!cr_reddit_bitcoin.isAlive())){
                        cr_reddit_bitcoin =  new Crawler_reddit_bitcoin();
                        cr_reddit_bitcoin.start();
                    }
                    if ((cr_reddit_bitcoinmarkets ==null) || (!cr_reddit_bitcoinmarkets.isAlive())){
                        cr_reddit_bitcoinmarkets =  new Crawler_reddit_bitcoinmarkets();
                        cr_reddit_bitcoinmarkets.start();
                    }
                    
                    Thread.sleep(1000*Config.sleep_main);
	    	}
    	} catch (InterruptedException e) {
                // TODO Auto-generated catch block
                Log.log(e);;
        }
    	
    }

}
