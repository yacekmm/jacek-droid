package pl.looksok.logic;

import java.io.Serializable;


public class InputData implements Serializable{
	private static final long serialVersionUID = 3343687271954154566L;
	private String name = "";
	private double pay = 0.0;
	private double shouldPay = -1;
	private double alreadyRefunded = 0.0;
	
	public InputData(String _name, double _pay){
		setName(_name);
		setPay(_pay);
		setShouldPay(-1);
	}
	
	public InputData(String _name, double _pay, double _shouldPay){
		setName(_name);
		setPay(_pay);
		setShouldPay(_shouldPay);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getName());
		sb.append(" paid: ");
		sb.append(getPay());
		
		if(getShouldPay() >= 0){
			sb.append(", should Pay: ");
			sb.append(getShouldPay());
		}
		return sb.toString();
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public double getPay() {
		return pay;
	}
	
	public void setPay(double pay) {
		this.pay = pay;
	}

	public double getShouldPay() {
		return shouldPay;
	}

	public void setShouldPay(double shouldPay) {
		this.shouldPay = shouldPay;
	}

	public double getAlreadyRefunded() {
		return alreadyRefunded;
	}
	
	public void addToAlreadyRefunded(double valueToAdd) {
		alreadyRefunded += valueToAdd;
	}
}
