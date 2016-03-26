package sudoku;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class CaseGrille {
	private int valeur;
	private ArrayList<Integer> possibles = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,9));
	
	public int getValeur() {
		return valeur;
	}
	public void setValeur(int valeur) {
		this.valeur = valeur;
	}
	public ArrayList<Integer> getPossibles() {
		return possibles;
	}
	public void setPossibles(ArrayList<Integer> possibles) {
		this.possibles = possibles;
	}
	
	public CaseGrille(int valeur){
		this.valeur = valeur;
	}

	public static String serialize(CaseGrille[] cg) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(cg);
	}

	public static CaseGrille[] deserialize(String m) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(m, CaseGrille[].class);
	}
}
