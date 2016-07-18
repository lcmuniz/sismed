package com.eficaztech.sismed.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.util.Clients;

public class Validations {

	public static void validate(Object obj) throws ValidationException {
		ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
		Validator validator = vf.getValidator();
		Set<ConstraintViolation<Object>> violations = validator.validate(obj);

		HashMap<String, String> e = new HashMap<String, String>();

		for (ConstraintViolation<?> v : violations) {

			String prop = v.getPropertyPath().toString();
			String msg = v.getMessage();

			// testa se a mensagem de validacao tem '='
			// se tiver entao a primeira parte é o nome da propriedade a ser
			// validade
			// e a segunda é a mensagem propriamente
			if (msg.contains("=")) {
				String[] temp = msg.split("=");
				prop = temp[0];
				msg = temp[1];
			}

			e.put("e_" + prop, msg);

		}

		if (!e.isEmpty()) {
			throw new ValidationException("errors", e);
		}
	}

	public static void show(Component component, HashMap<String, String> erros) {

		Component[] array = new Component[component.getFellows().size()];
		component.getFellows().toArray(array);
		Clients.clearWrongValue(array);

		if (erros != null) {
			List<WrongValueException> wvelist = new ArrayList<WrongValueException>();

			for (String key : erros.keySet()) {
				String value = erros.get(key);
				if (component.getFellowIfAny(key, true) != null) {
					wvelist.add(new WrongValueException(component.getFellow(key, true), value));
				} else {
					if (component.getFellowIfAny("e_errors", true) != null) {
						wvelist.add(new WrongValueException(component.getFellow("e_errors", true), value));
					}
				}
			}

			if (!wvelist.isEmpty()) throw new WrongValuesException(wvelist.toArray(new WrongValueException[0]));

		}
	}
}
