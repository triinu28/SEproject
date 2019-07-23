package businessLogic;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Currency {
	@XmlID
	private String name;
	private float valueEur;
	
	public Currency(String name, float valueEur) {
		super();
		this.name = name;
		this.valueEur = valueEur;
	}

	public String getName() {
		return name;
	}

	public float getValueEur() {
		return valueEur;
	}

	public void setValueEur(float valueEur) {
		this.valueEur = valueEur;
	}

	@Override
	public String toString() {
		return name;
	}
	
	

}
