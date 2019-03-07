package threads;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import misc.Config;
import misc.Log;
import misc.Utils;
import model.Cotacao;
import model.Trader;

public class Consulta_Traders extends Thread{
    
    @Override
    public void run(){
        List<Trader> traders = null;
        
        
        while (traders == null){
            try {
                traders = Trader.listar();      
            } catch (Exception e) {
                Log.log(e);
            }
        }
        
        while (true){
            try {
                traders = Trader.listar();
                for (Trader trader : traders) {
                    if (trader.ultimo.plusDays(4).isBefore(LocalDateTime.now())){
                        Atualizar_cotacoes_antigas(trader);
                    }
                    else{
                        Atualizar_cotacoes_recentes(trader);
                    }
                    
                    trader.atualizar();  
                }
                Log.log("Todos os traders atualizados. stand by por 15 minutos");
                Thread.sleep(1000*60*16);
            } catch (Exception e) {
                Log.log(e);
            }
        }
    }
    
    public static void Atualizar_cotacoes_recentes(Trader trader) throws Exception{
        Log.log("Iniciando rotina de solicitar cotações recentes do trader:"+trader.nome);
        Cotacao cotacao = new Cotacao();
        String nome = trader.nome;
        
        String url = "http://api.bitcoincharts.com/v1/trades.csv?symbol="; //URL base
        url = url + nome; //Selecionando o trader
        url = url + "&start="+trader.ultimo.toEpochSecond(ZoneOffset.UTC);
        String pagina = Jsoup.connect(url).get().html();
        Log.log("Buscando cotações a partir de: "+trader.ultimo.toString(), false);
        for (String linha : pagina.split(" ")) {
            ler_linha(linha, cotacao, trader, nome);
        }
        Thread.sleep(1000*Config.sleep_atualizacoes_recentes);
    }
    
    public static void Atualizar_cotacoes_antigas(Trader trader) throws Exception{
       Cotacao cotacao = new Cotacao();
       cotacao.data =   LocalDateTime.MIN;
       String nome = trader.nome;
       int c_int =0;
       char c = 'a';
       String url = "http://api.bitcoincharts.com/v1/csv/";
       Document pagina = Jsoup.parse(Jsoup.connect(url).get().html());
       for (Element link : pagina.select("a")) {
            if (link.text().contains(nome)){
                File caminho  = new File(new File(".").getCanonicalPath()+"/temp");
                caminho.mkdirs();
                File arquivo = new File(caminho.getAbsolutePath()+"/"+trader.nome+".csv.gz");
                if (arquivo.exists()){
                    arquivo.delete();
                }
                while (!arquivo.exists()){
                    try {
                        Log.log("Baixando arquivo de cotações", false);
                        FileUtils.copyURLToFile(new URL(url+link.attr("href")), arquivo);
                    } catch (Exception e) {
                        Log.log(e);
                    }
                }
                File pasta_csv = new File(caminho+"/"+nome+"/");
                if (pasta_csv.exists()){
                    pasta_csv.delete();
                }
                File arquivo_csv = new File(pasta_csv.getAbsoluteFile()+"/arq.csv");
                pasta_csv.mkdirs();
                Log.log("Descompactando arquivo de cotações", false);
                Utils.Descompactar(arquivo.getAbsolutePath(), arquivo_csv.getAbsolutePath()+arquivo_csv.getName());
                Log.log("Abrindo arquivo de cotações", false);
                InputStreamReader isr = new InputStreamReader(new FileInputStream(arquivo_csv.getAbsolutePath()+arquivo_csv.getName()),"UTF-8");
                BufferedReader br = new BufferedReader(isr);
                String linha = "";
                while((c_int= br.read()) != -1){
                    linha = linha + (char) c_int;
                    if ((linha.contains(" "))||linha.contains("\n")){
                        linha = linha.replace(" ", "").replace("\n", "");
                        ler_linha(linha,cotacao,trader,nome);
                        linha="";
                    }
                }
                br.close();
                trader.atualizar();
                Log.log("Terminou de pedir cotacoes antigas do trader");
                        
            }
        }
    }
    
    public static void ler_linha(String linha, Cotacao cotacao, Trader trader, String nome) throws Exception{
        if ((linha == "") || (!linha.contains(","))){
                return;
            }

            String[] valores = linha.split(",");
            LocalDateTime novo = LocalDateTime.of(1970,1, 1, 0, 0).plusSeconds(Long.parseLong(valores[0]) );
            if (trader.ultimo.isAfter(novo)){
              if (LocalDateTime.of(novo.getYear(), novo.getMonth(), novo.getDayOfMonth(), novo.getHour(), 0).isAfter(cotacao.data))
              {
                  Log.log("Ignorada cotação do site: "+nome+", hora: "+novo, false);
                  cotacao.data = LocalDateTime.of(novo.getYear(), novo.getMonth(), novo.getDayOfMonth(), novo.getHour(), 0);
              }
              return;
 
            }
            LocalDateTime dataAnterior = cotacao.data;
            cotacao.data = LocalDateTime.of(novo.getYear(), novo.getMonth(), novo.getDayOfMonth(), novo.getHour(), 0); 
            cotacao.valor = Float.valueOf(valores[1]);
            if (novo.isAfter(trader.ultimo)){ 
                trader.ultimo=novo;
            }
            cotacao.transacoes++;
            
            if ((dataAnterior==null) || (!dataAnterior.equals(cotacao.data))){
                cotacao.atualizar();
                trader.atualizar();
                cotacao.transacoes=0;
                Log.log("Inserida cotação do site: "+nome+", hora: "+cotacao.data.toString()+", valor="+cotacao.valor.toString(), false);
            }
                
            
    }
    
}