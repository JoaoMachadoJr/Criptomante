package misc;

public class Str_Utils {
	
	public static String after(String entrada, String substring, int ocorrencia) {
		String aux = entrada;
		int i=0;
		while (aux.contains(substring) && i<ocorrencia) {
			aux= aux.substring(aux.indexOf(substring)+substring.length());
			i++;
		}
		return aux;
	}
	
	public static String after(String entrada, String substring) {
		return after(entrada,substring,1);
	}
	
	public static String after_last(String entrada, String substring) {
		return after(entrada,substring,Integer.MAX_VALUE);
	}
	
	public static String before(String entrada, String substring) {
		if (entrada.contains(substring))
			return entrada.substring(0, entrada.indexOf(substring));
		else
			return entrada;
	}
	
	public static String before_last(String entrada, String substring) {
		if (entrada.contains(substring))
			return entrada.substring(0, entrada.lastIndexOf(substring));
		else
			return entrada;
	}
	
	public static String between(String entrada, String primeira, String segunda) {
		String aux = entrada;
		String saida = aux;
		while (aux.contains(primeira) && aux.contains(segunda) && aux.indexOf(primeira)<=aux.lastIndexOf(segunda)) {
			aux = after(aux,primeira);
			saida = before(aux,segunda);
		}
		return saida;
	}
	
	public static String between_outer(String entrada, String primeira, String segunda) {
		if (entrada.contains(primeira) && entrada.contains(segunda) && entrada.indexOf(primeira)<=entrada.lastIndexOf(segunda))
			return before_last(after(entrada, primeira),segunda);
		else
			return entrada;
	}
	
	public static String sem_tags(String entrada) {
		String saida = entrada;
		while (saida.contains("<") && saida.contains(">")) {
			saida = after(saida, ">");
		}
		return saida;
	}

}
