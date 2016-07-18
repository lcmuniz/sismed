package com.eficaztech.sismed.util;

import java.util.Collection;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.Clients;


// classe que remove as mensagens de avisos de validacao das telas 
// quando o usaurio salva o registro 
public class Warning {

	public static void clear(Collection<Component> components) {
		for (Component component : components) {
			Clients.clearWrongValue(component);
		}
	}
}
