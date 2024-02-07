package com.stpa.ws.server.validation;

import com.stpa.ws.server.validation.IResultadoValidacion;

/**
 * @author CarlosRuben
 * Interfaz que implementar�n los objetos de validaci�n.
 * @param <T>
 */
public interface IValidator<T> {
	
	/**M�todo que devuelve un objeto indicando si el objeto a validar es correcto o no.
	 * 
	 * @param objeto - Objeto a validar
	 * @return - true si el objeto es v�lido, false si no.
	 */

	public  boolean isValid(T objeto);
	/**Devuelve un objeto que representa la informaci�n sobre el resultado o resultados de la validaci�n.
	 * 
	 * @return - Resultados de validaci�n
	 */
	public IResultadoValidacion getResultado(); 
}
