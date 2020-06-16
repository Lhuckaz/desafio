package com.primeiropay.desafio.model;

import java.util.concurrent.atomic.AtomicInteger;

public class Client {

	private static final AtomicInteger COUNTER = new AtomicInteger();

	private String entityId;
	private Integer cpf;
	private Double amount;
	private String cardBrand;
	private String cardNumber;
	private String cardHolder;
	private String cardExpiryMonth;
	private String cardExpiryYear;
	private Integer cardCvv;
	
	public String getEntityId() {
		return entityId;
	}
	
	public Integer getCpf() {
		return cpf;
	}

	public Double getAmount() {
		return amount;
	}

	public String getCardBrand() {
		return cardBrand;
	}
	
	public String getCardNumber() {
		return cardNumber;
	}
	
	public String getCardHolder() {
		return cardHolder;
	}
	
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public void setCpf(Integer cpf) {
		this.cpf = cpf;
	}
	
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	public String getCardExpiryMonth() {
		return cardExpiryMonth;
	}
	
	public String getCardExpiryYear() {
		return cardExpiryYear;
	}
	
	public Integer getCardCvv() {
		return cardCvv;
	}

	public void setCardBrand(String cardBrand) {
		this.cardBrand = cardBrand;
	}
	
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	
	public void setCardHolder(String cardHolder) {
		this.cardHolder = cardHolder;
	}
	
	public void setCardExpiryMonth(String cardExpiryMonth) {
		this.cardExpiryMonth = cardExpiryMonth;
	}
	
	public void setCardExpiryYear(String cardExpiryYear) {
		this.cardExpiryYear = cardExpiryYear;
	}
	
	public void setCardCvv(Integer cardCvv) {
		this.cardCvv = cardCvv;
	}

}
