import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class TreeMsgContent {
	private String type;
	private int value;

	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public TreeMsgContent(){
		
	}
	
	public TreeMsgContent(String type,int value){
		this.type = type;
		this.value = value;
	}
	
	public TreeMsgContent(String type){
		this.type = type;
		this.value = 0;
	}

	public static String serialize(TreeMsgContent m) throws JsonProcessingException{
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(m);
	}
	
	public static TreeMsgContent deserialize(String m) throws JsonParseException, JsonMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(m, TreeMsgContent.class);
	}

}
