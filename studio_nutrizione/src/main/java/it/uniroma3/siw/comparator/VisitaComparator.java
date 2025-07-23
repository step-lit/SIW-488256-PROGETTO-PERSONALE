package it.uniroma3.siw.comparator;

import java.util.Comparator;
import it.uniroma3.siw.model.Visita;

public class VisitaComparator implements Comparator<Visita>{
	
	public VisitaComparator() {};
	
	@Override
	public int compare(Visita v1, Visita v2) {
		return v1.getDateTime().compareTo(v2.getDateTime());
	}
	
}
