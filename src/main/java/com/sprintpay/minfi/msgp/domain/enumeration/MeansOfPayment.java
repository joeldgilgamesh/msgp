package com.sprintpay.minfi.msgp.domain.enumeration;

import java.util.ArrayList;
import java.util.List;


/**
 * The MeansOfPayment enumeration.
 */
public enum MeansOfPayment {
    MOBILE_MONEY, MOBILE_MONEY2, ORANGE_MONEY, ORANGE_MONEY2, YUP, EXPRESS_UNION, VISA, MASTER_CARD, AFRILAND, UBA, ECOBANK, ECOBANK2
    , CCA_BANK, VISION_FINANCE, EXPRESS_EXCHANGE;
	
	public List<String> getAll() {
		List<String> liste = new ArrayList<String>();
		for (MeansOfPayment string : MeansOfPayment.values()) {
			liste.add(string.name());
		}
		return liste;
	}
}


//ajouter le provider UBA