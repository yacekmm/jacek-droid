package pl.looksok.logic;

import java.io.Serializable;

public class AtomPayment implements Serializable {
	private static final long serialVersionUID = -5544983199052310005L;
	
	private String name = "";
	private double value = 0;
	
	public AtomPayment() {}

	public AtomPayment(String name, double value) {
		this.setName(name);
		this.setValue(value);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Name: ").append(getName());
		sb.append(", value: ").append(getValue());
		return sb.toString();
	}
}
