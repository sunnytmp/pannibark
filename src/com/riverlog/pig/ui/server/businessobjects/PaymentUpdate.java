package com.riverlog.pig.ui.server.businessobjects;


public class PaymentUpdate {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public void InitializePaymentVariable(String uniqueUserName, String payeridt, String paymentamountt, String subscriptiondurationt, String itemamountt, String tokent){
		com.riverlog.pig.ui.server.dataobjects.PaymentUpdate paymentUpdate  = new com.riverlog.pig.ui.server.dataobjects.PaymentUpdate();
		paymentUpdate.InitializePaymentVariable(uniqueUserName,payeridt, paymentamountt, subscriptiondurationt, itemamountt, tokent);
	}
}
