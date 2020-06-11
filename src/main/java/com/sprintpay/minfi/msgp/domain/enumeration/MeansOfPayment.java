package com.sprintpay.minfi.msgp.domain.enumeration;

import java.util.ArrayList;
import java.util.List;


/**
 * The MeansOfPayment enumeration.
 */
public enum MeansOfPayment {
    MOBILE_MONEY, ORANGE_MONEY, YUP, EXPRESS_UNION, VISA, MASTER_CARD, AFRILAND, UBA;
	
	public List<String> getAll() {
		List<String> liste = new ArrayList<String>();
		for (MeansOfPayment string : MeansOfPayment.values()) {
			liste.add(string.name());
		}
		return liste;
	}
}


//ajouter le provider UBA