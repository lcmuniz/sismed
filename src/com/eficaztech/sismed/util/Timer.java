package com.eficaztech.sismed.util;

import java.util.Date;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.SerializableEventListener;
import org.zkoss.zk.ui.util.Composer;

public class Timer implements Composer<Component> {

	@SuppressWarnings({ "unchecked", "serial", "rawtypes" })
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		comp.addEventListener("onTimer", new SerializableEventListener() {
			public void onEvent(Event event) throws Exception {
				System.out.println(new Date());
			}
		});
	}

}
