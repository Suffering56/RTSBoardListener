package common.entity;

public class SenderEntity {

	public SenderEntity(String name, String field, String value) {
		this.name = name;
		this.field = field;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public String getField() {
		return field;
	}

	public String getValue() {
		return value;
	}

	private final String name;
	private final String field;
	private final String value;
}
