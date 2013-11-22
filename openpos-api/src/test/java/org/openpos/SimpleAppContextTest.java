package org.openpos;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openpos.test.MockAbstractClass1;
import org.openpos.test.MockImplementation1;
import org.openpos.test.MockInterface1;
import org.openpos.test.MockInterface2;

public class SimpleAppContextTest {

	@Test
	public void testGetBean() throws Exception {
		MockImplementation1 mockImplementation1 = new MockImplementation1();
		SimpleAppContext appContext = new SimpleAppContext(mockImplementation1);

		assertEquals(mockImplementation1, appContext.getBean(MockImplementation1.class));
		assertEquals(mockImplementation1, appContext.getBean(MockAbstractClass1.class));
		assertEquals(mockImplementation1, appContext.getBean(MockInterface1.class));
		assertEquals(mockImplementation1, appContext.getBean(MockInterface2.class));
	}
}
