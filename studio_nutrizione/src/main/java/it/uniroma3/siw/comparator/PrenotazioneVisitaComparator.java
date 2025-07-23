package it.uniroma3.siw.comparator;

import java.util.Comparator;

import it.uniroma3.siw.model.PrenotazioneVisita;

public class PrenotazioneVisitaComparator implements Comparator<PrenotazioneVisita>{
	
	public PrenotazioneVisitaComparator() {};
	
	@Override
	public int compare(PrenotazioneVisita p1, PrenotazioneVisita p2) {
		return p1.getDateTime().compareTo(p2.getDateTime());
	}
	
}
