package it.uniroma3.siw.comparator;

import java.util.Comparator;

import it.uniroma3.siw.model.PianoAlimentare;

public class PianoAlimentareComparator implements Comparator<PianoAlimentare>{
	
	public PianoAlimentareComparator() {};
	
	@Override
	public int compare(PianoAlimentare p1, PianoAlimentare p2) {
		int value = p1.getStartDate().compareTo(p2.getStartDate());
		
		if(value != 0) {
			return value;
		}
		
		if(p1.getEndDate() == null && p2.getEndDate() == null) {
			return 0;
		}
		
		if(p1.getEndDate() == null) {
			return -1;
		}
		
		if(p2.getEndDate() == null) {
			return 1;
		}
		
		return p1.getEndDate().compareTo(p2.getEndDate());
	}
	
}
