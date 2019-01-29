package crawlers;

import java.time.format.DateTimeFormatter;
import misc.Str_Utils;

public class Crawler_reddit_bitcoinmarkets extends Crawler {
	
	public Crawler_reddit_bitcoinmarkets() {
		website = "https://old.reddit.com/r/BitcoinMarkets";
                inicio_navegacao = "https://api.pushshift.io/reddit/search/submission/?subreddit=BitcoinMarkets&sort_type=created_utc&sort=desc";
		url_raiz_topico= "https://old.reddit.com/";
		url_raiz_navegacao= "https://api.pushshift.io/reddit/search/submission/?subreddit=BitcoinMarkets&sort_type=created_utc&sort=desc";
                
		// ===========ATRIBUTOS REFERENTES A PÁGINAS DE NAVEGAÇĂO===============
		antes_primeiro_topico= "\"data\": [";

		inicio_topico= "\"author\"";
		inicio_link_topico = "\"full_link\": \"https://www.reddit.com/";
		final_link_topico = "\",";
		fim_topico = "\"title\":";

		fim_ultimo_topico = "";

		antes_link_proxima_pagina = "";
		fim_link_proxima_pagina = "";

		proxima_pagina_depois_ultimo_topico = true;

		
		// ===========ATRIBUTOS REFERENTES A PÁGINAS DE CONTEÚDO===============
		antes_primeira_mensagem = "";
		
		inicio_primeira_mensagem = "\"main\"";
		inicio_texto_primeira_mensagem = "<div class=\"expando expando-uninitialized\" data-pin-condition=\"function() {return this.style.display != 'none';}\"";
		final_texto_primeira_mensagem = "</form>";
		inicio_data_primeira_mensagem = "datetime=\"";
		final_data_primeira_mensagem = "+";
		final_primeira_mensagem = "<li class=\"first\">";
		
		antes_mensagens_comuns = "</section>";
		inicio_mensagem_comum = "usertext-body may-blank-within md-container";
		inicio_texto_mensagem_comum = "<div class=\"md\">";
		final_texto_mensagem_comum = "</form>";
		inicio_data_mensagem_comum = "datetime=\"";
		final_data_mensagem_comum = "+";
		final_mensagem_comum = "flat-list buttons";
                
                formato_data = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
	}
    
        @Override
        public void encontrar_proxima_url(String html) {
            try {
                String html2 = html;
		html2= html2.substring(html2.lastIndexOf("\"created_utc\": ")+15);
                html2 = html2.substring(0,html2.indexOf(","));
                if (html2.contains(".")) 
                    html2 = html2.substring(0,html2.indexOf(".")) ;
                pagina_atual = url_raiz_navegacao+"&before="+html2;
            } catch (Exception e) {
                pagina_atual= "";
            }
		
	}
        
        @Override
        public boolean deve_processar(String html){
            return !html.contains("You must be 18+ to view this community");
        }

}
