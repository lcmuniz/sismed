package com.eficaztech.sismed.util;

import org.zkoss.zk.ui.util.Clients;

public class Notification {

	public static void show(String type, String msg) {
		String opts = "{'closeButton': false,'debug': false,'newestOnTop': true,'progressBar': true, 'positionClass': 'toast-top-center', 'preventDuplicates': true, 'onclick': null, 'showDuration': '300', 'hideDuration': '1000', 'timeOut': '5000', 'extendedTimeOut': '1000', 'showEasing': 'swing', 'hideEasing': 'linear', 'showMethod': 'fadeIn', 'hideMethod': 'fadeOut'}";

		msg = msg.replace("'", "\\'");

		Clients.evalJavaScript("toastr['" + type + "']('" + msg + "', '', "
				+ opts + ")");

	}}
