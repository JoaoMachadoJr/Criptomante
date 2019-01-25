package crawlers;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javafx.util.converter.LocalDateTimeStringConverter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import misc.Log;
import misc.Str_Utils;
import model.Mensagem;
import model.Topico;

public class Crawler  extends Thread{
	public String website;
        public String inicio_navegacao;
	public String url_raiz_topico;
        public String url_raiz_navegacao;
        
	public String pagina_atual;
	DateTimeFormatter formato_data;

	// ===========ATRIBUTOS REFERENTES A PÁGINAS DE NAVEGAÇÃO===============
	public String antes_primeiro_topico;

	public String inicio_topico;
	public String inicio_link_topico;
	public String final_link_topico;
	public String fim_topico;

	public String fim_ultimo_topico;

	public String antes_link_proxima_pagina;
	public String fim_link_proxima_pagina;

	public boolean proxima_pagina_depois_ultimo_topico;

	
	// ===========ATRIBUTOS REFERENTES A PÁGINAS DE CONTEÚDO===============
	public String antes_primeira_mensagem;
	
	public String inicio_primeira_mensagem;
	public String inicio_texto_primeira_mensagem;
	public String final_texto_primeira_mensagem;
	public String inicio_data_primeira_mensagem;
	public String final_data_primeira_mensagem;
	public String final_primeira_mensagem;
	
	public String antes_mensagens_comuns;
	public String inicio_mensagem_comum;
	public String inicio_texto_mensagem_comum;
	public String final_texto_mensagem_comum;
	public String inicio_data_mensagem_comum;
	public String final_data_mensagem_comum;
	public String final_mensagem_comum;
	
	
	
	
	@Override
    public void run(){
		processar_website(); 
	}
	public void processar_website() {
		if (pagina_atual == null) {
			pagina_atual = inicio_navegacao;
		}
		Log.log("Website: "+website+" => Começando indexação...");
                
                pagina_atual ="";
		while ((pagina_atual != null) && (pagina_atual !="" )) {
			//=====Antes de buscar o primeiro topico
			try {
				Log.log("Website: "+website+" => Página: "+pagina_atual);
				String html = Jsoup.connect(pagina_atual).ignoreContentType(true).get().html();

				
//				System.setProperty("webdriver.chrome.driver", new File(".").getAbsolutePath()+"/lib/chromedriver.exe");
//				WebDriver driver = new ChromeDriver();
//				driver.get(pagina_atual);				
				
				encontrar_proxima_url(html);
				
				html = Str_Utils.after(html, antes_primeiro_topico);
				//=======Indexação de topicos
				while (html.contains(inicio_topico) && 	html.contains(inicio_link_topico) &&
						html.contains(final_link_topico) &&	html.contains(fim_topico)) {
					html = encontrar_topico(html);
				}
				
			} catch (IOException e) {
				Log.log(e);
			}
		}
		
		Log.log("Website: "+website+" => Começando processamento dos tópícos...");
                
                processar_topicos();
                
                Log.log("Website: "+website+" => Concluído!");

	}
	
	public String encontrar_topico(String html) {
		html = Str_Utils.after(html, inicio_topico);
		html = Str_Utils.after(html, inicio_link_topico);
		String url_topico = Str_Utils.before(html, final_link_topico);
		url_topico = url_raiz_topico + url_topico;
		html = Str_Utils.after(html, final_link_topico);
		html = Str_Utils.after(html, fim_topico);
		Topico t = new Topico();
		t.website = website;
		t.url = url_topico;
		t.integrar();	
		return html;
	}
	
	public void encontrar_proxima_url(String html) {
            try {
            	String html2 = html;
		if (proxima_pagina_depois_ultimo_topico) {
			html2 = Str_Utils.after(html2, antes_primeiro_topico);
			html2 = Str_Utils.after(html2, fim_ultimo_topico);
		}
		
		html2 = Str_Utils.after(html2, antes_link_proxima_pagina);
		pagina_atual = Str_Utils.before(html2, fim_link_proxima_pagina);
		pagina_atual = url_raiz_navegacao + pagina_atual;
            }    
             catch (Exception e) {
                pagina_atual="";
            }

        }
	public void processar_topicos() {
		
		List<Topico> topicos = Topico.listar_nao_processados(website);
		
		for (int i=0; i<topicos.size(); i++) {
			Topico t = topicos.get(i);
			try {
				String html = Jsoup.connect(t.url).get().html();
				
                                if (deve_processar(html)){
                                    html = Str_Utils.after(html, antes_primeira_mensagem);
                                    html = Str_Utils.after(html, inicio_primeira_mensagem);

                                    html = processar_primeira_mensagem(html,t);

                                    html = Str_Utils.after(html, final_primeira_mensagem);
                                    html = Str_Utils.after(html, antes_mensagens_comuns);

                                    while (html.contains(inicio_mensagem_comum) && html.contains(inicio_texto_mensagem_comum) && html.contains(final_texto_mensagem_comum) && html.contains(final_mensagem_comum)) {
                                            html = processar_mensagem_comum(html,t);
                                    }

                                        
                                }
				t.data_processamento = LocalDateTime.now();
                                t.atualizar_data_processamento();
				
				
			} catch (Exception e) { System.out.println("Erro no tópico: "+t.url); Log.log(e); }
		}

	}
	
	public String processar_primeira_mensagem(String html, Topico topico) {
		Mensagem m = new Mensagem();
		m.topico_url=topico.url;
		String html_local_data = html;
		String html_local_texto = html;
		
		//Buscar data
		html_local_data = Str_Utils.after(html_local_data, inicio_data_primeira_mensagem);
		m.data = LocalDateTime.parse(Str_Utils.before(html_local_data, final_data_primeira_mensagem),formato_data );
		html_local_data = Str_Utils.after(html_local_data, final_data_primeira_mensagem);
		
		//Buscar texto
                if (html_local_texto.contains(inicio_texto_primeira_mensagem)&& html_local_texto.contains(final_texto_primeira_mensagem)){
                    html_local_texto = Str_Utils.after(html_local_texto, inicio_texto_primeira_mensagem);
                    m.texto = Str_Utils.sem_tags(Str_Utils.before(html_local_texto, final_texto_primeira_mensagem));
                    m.texto = Str_Utils.apenas_texto(m.texto);
                    html_local_texto = Str_Utils.after(html_local_texto, final_texto_primeira_mensagem);     
                }
                else{
                  m.texto = "";  
                }
		
		
		topico.data = m.data;
                topico.ultima_atualizacao = m.data;
		topico.atualizar_data();
		
                if(m.texto!=""){
                   m.inserir();
                }
		//Retorno a menor string, para eliminar a maior quantidade de html possível
		if (html_local_texto.length()<html_local_data.length())
			return html_local_texto;
		else
			return html_local_data;
	}
	public String processar_mensagem_comum(String html, Topico topico) {
		Mensagem m = new Mensagem();
		m.topico_url=topico.url;
		String html_local_data = html;
		String html_local_texto = html;
		
		//Buscar data
		html_local_data = Str_Utils.after(html_local_data, inicio_data_mensagem_comum);
		m.data = LocalDateTime.parse(Str_Utils.before(html_local_data, final_data_mensagem_comum),formato_data );
		html_local_data = Str_Utils.after(html_local_data, final_data_mensagem_comum);
		
		//Buscar texto
		html_local_texto = Str_Utils.after(html_local_texto, inicio_texto_mensagem_comum);
		m.texto = Str_Utils.sem_tags(Str_Utils.before(html_local_texto, final_texto_mensagem_comum));
                m.texto = Str_Utils.apenas_texto(m.texto);
		html_local_texto = Str_Utils.after(html_local_texto, final_texto_mensagem_comum);
                
                if(m.texto!=""){
                   m.inserir();
                }
                
                topico.ultima_atualizacao = m.data;
                topico.atualizar_data_ultima_atualizacao();
		
		//Retorno a menor string, para eliminar a maior quantidade de html possível
		if (html_local_texto.length()<html_local_data.length())
			return html_local_texto;
		else
			return html_local_data;
	}
        
        public boolean deve_processar(String html){
            return true;
        }

}
