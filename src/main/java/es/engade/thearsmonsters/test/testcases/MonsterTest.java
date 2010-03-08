package es.engade.thearsmonsters.test.testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Date;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import es.engade.thearsmonsters.model.entities.monster.Monster;
import es.engade.thearsmonsters.model.entities.monster.attr.Attr;
import es.engade.thearsmonsters.model.entities.monster.attr.AttrBase;
import es.engade.thearsmonsters.model.entities.monster.enums.AttrType;
import es.engade.thearsmonsters.model.util.CalendarTools;
import es.engade.thearsmonsters.test.FactoryData;
import es.engade.thearsmonsters.test.GaeTest;
import es.engade.thearsmonsters.test.FactoryData.MonsterWhatIs;


public class MonsterTest extends GaeTest {
	
	Date now;
	Monster m1;

	@Before
    public void setUp() throws Exception {
        // Dates
        now = CalendarTools.now();
        
        // Monsters
        m1 = FactoryData.generate(MonsterWhatIs.Rand);
    }
    
    
    @Test 
    public void testSimpleAttrAccessors() {
    	Map<AttrType, Attr> simpleAttrs = m1.getSimpleAttrs();
    	
    	Attr strenght = m1.getSimpleAttr(AttrType.Strenght);
    	Attr strenght2 = simpleAttrs.get(AttrType.Strenght);
    	
    	assertEquals(strenght, strenght2);
    	assertEquals(strenght.getType(), AttrType.Strenght);
    	
    	// attribute should not be accessed with the other attr accessors
    	assertNull(m1.getWorkSkill(AttrType.Strenght));
    	assertNull(m1.getComposeAttr(AttrType.Strenght));
    }

    @Test 
    public void testWorkSkillAccessors() {
    	Map<AttrType, Attr> workSkills = m1.getWorkSkills();
    	
    	Attr constructorSkill = m1.getWorkSkill(AttrType.ConstructorSkill);
    	Attr constructorSkill2 = workSkills.get(AttrType.ConstructorSkill);
    	
    	assertEquals(constructorSkill, constructorSkill2);
    	assertEquals(constructorSkill.getType(), AttrType.ConstructorSkill);
    	
    	// attribute should not be accessed with the other attr accessors
    	assertNull(m1.getSimpleAttr(AttrType.ConstructorSkill));
    	assertNull(m1.getComposeAttr(AttrType.ConstructorSkill));
    }
    
    @Test 
    public void testComposeAttrAccessors() {
    	Map<AttrType, Attr> composeAttrs = m1.getComposeAttrs();
    	
    	Attr construction = m1.getComposeAttr(AttrType.Construction);
    	Attr construction2 = composeAttrs.get(AttrType.Construction);
    	
    	assertEquals(construction, construction2);
    	assertEquals(construction.getType(), AttrType.Construction);
    	
    	// attribute should not be accessed with the other attr accessors
    	assertNull(m1.getSimpleAttr(AttrType.Construction));
    	assertNull(m1.getWorkSkill(AttrType.Construction));
    }

    @Test
    public void testAddExp() {
    	// Creamos un atributo nuevo para asegurarnos de que empieza en nivel 1, con experiencia 0.
    	Attr attr = new AttrBase(AttrType.Agility, 1, 0); 

    	// Sumamos 10 inicialmente (no debe de subir de nivel)
		attr.addExp(10);
		assertEquals(attr.getLevel(), 1);
		assertEquals(attr.getExp(), 10);
	    
		// Luego 100 (debería subir un nivel).
		attr.addExp(100);
		assertEquals(attr.getLevel(), 2);
		assertEquals(attr.getExp(), 10);
		
		// Luego otros 10 (no debe subir de nivel)
		m1.getAttr(AttrType.Agility).addExp(10);
		assertEquals(attr.getLevel(), 2);
		assertEquals(attr.getExp(), 20);
		
		// Luego otros 90 (debe subir otro nivel)
		m1.getAttr(AttrType.Agility).addExp(90);
		assertEquals(attr.getLevel(), 3);
		assertEquals(attr.getExp(), 10);
		
		// Luego 333 más (debe subir 3 niveles)
		m1.getAttr(AttrType.Agility).addExp(333);
		assertEquals(attr.getLevel(), 6);
		assertEquals(attr.getExp(), 43);
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void testAddExpToComposeAttr() throws UnsupportedOperationException {
    	m1.getComposeAttr(AttrType.Construction).addExp(10);
    }

}