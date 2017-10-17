package menu;

public class MenuOption {
	private String text, id, code;
	private double value;
	private Object ref;
	
	public MenuOption(String text, String id, String code, double value, Object ref){
		this.text = text;
		this.id = id;
		this.value = value;
		this.ref = ref;
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public Object getRef() {
		return ref;
	}

	public void setRef(Object ref) {
		this.ref = ref;
	}
}
