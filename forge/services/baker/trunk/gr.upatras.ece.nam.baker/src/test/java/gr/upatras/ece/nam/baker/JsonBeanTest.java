/**
 * 
 */
package gr.upatras.ece.nam.baker;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author ctranoris
 *
 */
public class JsonBeanTest {

	/**
	 * Test method for {@link gr.upatras.ece.nam.baker.JsonBean#setVal1(java.lang.String)}.
	 */
	@Test
	public void testSetVal1() {
		JsonBean b = new JsonBean();
		b.setVal1("xx");
		assertEquals("xx", b.getVal1());
		
	}

	/**
	 * Test method for {@link gr.upatras.ece.nam.baker.JsonBean#setVal2(java.lang.String)}.
	 */
	@Test
	public void testSetVal2() {
		JsonBean b = new JsonBean();
		b.setVal2("xxy");
		assertEquals("xxy", b.getVal2());
	}

}
