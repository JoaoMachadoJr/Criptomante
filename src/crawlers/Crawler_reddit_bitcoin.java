package crawlers;

public class Crawler_reddit_bitcoin extends Crawler {
	
	public Crawler_reddit_bitcoin() {
		website = "https://old.reddit.com/r/Bitcoin";
                inicio_navegacao = "https://old.reddit.com/r/Bitcoin";
		url_raiz_topico= "https://old.reddit.com/";
		url_raiz_navegacao= "https://old.reddit.com/";
                
		// ===========ATRIBUTOS REFERENTES A PÁGINAS DE NAVEGAÇĂO===============
		antes_primeiro_topico= "sitetable linklisting";

		inicio_topico= "data-author-fullname";
		inicio_link_topico = "data-permalink=\"/";
		final_link_topico = "\" data-domain";
		fim_topico = "listing";

		fim_ultimo_topico = "span class=\"next-button\"";

		antes_link_proxima_pagina = "old.reddit.com/r/Bitcoin/new/";
		fim_link_proxima_pagina = "\" rel=\"";

		proxima_pagina_depois_ultimo_topico = true;

		
		// ===========ATRIBUTOS REFERENTES A PÁGINAS DE CONTEÚDO===============
		antes_primeira_mensagem = "meta name=\"description\"";
		
		inicio_primeira_mensagem = "title may-blank";
		inicio_texto_primeira_mensagem = "class=\"md\">";
		final_texto_primeira_mensagem = "</form>";
		inicio_data_primeira_mensagem = "datetime=\"";
		final_data_primeira_mensagem = "T";
		final_primeira_mensagem = "<li class=\"first\">";
		
		antes_mensagens_comuns = "usertext warn-on-unload";
		inicio_mensagem_comum = "usertext-body may-blank-within md-container";
		inicio_texto_mensagem_comum = "<div class=\"md\">";
		final_texto_mensagem_comum = "</form>";
		inicio_data_mensagem_comum = "datetime=\"";
		final_data_mensagem_comum = "T";
		final_mensagem_comum = "flat-list buttons";	
	}

}
