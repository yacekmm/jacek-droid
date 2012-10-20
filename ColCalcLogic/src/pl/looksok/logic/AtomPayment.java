package pl.looksok.logic;

public class AtomPayment {
	private String name = "";
	private double value = 0;
	
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
