package pl.looksok.logic;

import java.io.Serializable;


public class InputData implements Serializable{
	private static final long serialVersionUID = 3343687271954154566L;
	private String name = "";
	private double pay = 0.0;
	
	public InputData(String _name, double _pay){
		setName(_name);
		setPay(_pay);
	}
	
	@Override
	public String toString() {
		return getName() + ": " + getPay();
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
}
