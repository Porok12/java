package model;

public class Bilet {
	private int cena;
	private TypBiletu typ;
	
	public Bilet(TypBiletu typBiletu, int cena) {
		this.typ = typBiletu;
		this.cena = cena;
	}
	
	public TypBiletu getTyp() {
		return typ;
	}
	
	public int getCena() {
		return cena;
	}
}

enum TypBiletu {
	ULGOWY,
	NORMALNY
}
