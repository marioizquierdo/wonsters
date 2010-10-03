package es.engade.thearsmonsters.model.entities.monster.attr;

import java.util.ArrayList;
import java.util.List;

import es.engade.thearsmonsters.model.entities.monster.enums.AttrType;
import es.engade.thearsmonsters.model.entities.monster.enums.AttrTypeClass;

/**
 * Los SimpleAttribute y los WorkSkill son AttributeBase, ya que
 * no derivan de ningún otro attributo, es decir que tienen su propio valor.
 */
public class AttrBase extends Attr {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4380081870208638011L;
	private int level;
	private int exp;
	
	
	public AttrBase(AttrType type, int level, int exp) {
		super(type);
		initiateAttr(level, exp);
	}
	
	public void initiateAttr(int level, int exp) {
		this.exp = exp;
		this.level = level;
	}
	
	@Override
    public boolean addExp(int exp) {
		// La excepción MonsterAttrException la generan los atributos compuestos, 
		// ya que a esos no se les puede añadir experiencia directamente
		int prevLevel = this.level;
		this.level += (this.exp + exp) / 100;
		this.exp = (this.exp + exp) % 100;
		return this.level > prevLevel; // devolver true si aumenta de nivel
	}
	
	@Override
    public int getLevel() {
		return this.level;
	}
	
	@Override
    public int getExp(){
		return this.exp;
	}
	
	@Override
    public List<String> getDescription() {
		List<String> desc = new ArrayList<String>();
		if(getType().getAttrClass().equals(AttrTypeClass.WorkSkill)) {
			desc.add("attr.skill");
		}
		desc.add("attr."+getType().toString());
		return desc;
	}
	
	@Override
    public List<String> getValueDescription() {
		List<String> desc = new ArrayList<String>();
		// hay que añadir una cadena vacía para que se mantengan los índices con getDescription()
		if(getType().getAttrClass().equals(AttrTypeClass.WorkSkill)) {
			desc.add("");
		}
		desc.add(getLevel()+"");
		return desc;
	}
	
}
