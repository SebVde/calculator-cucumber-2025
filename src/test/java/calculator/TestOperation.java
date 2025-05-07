package calculator;

// Import Junit5 libraries for unit testing:
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import visitor.CountVisitor;

import java.util.Arrays;
import java.util.List;

class TestOperation {

	private Operation o;
	private Operation o2;
	private CountVisitor countVisitor1;

    @BeforeEach
	void setUp() throws Exception {
		// Create nested operations to test depth, operations, and numbers
		List<Expression> params1 = Arrays.asList(new RealNumber(3.0), new RealNumber(4.0), new RealNumber(5.0));
		List<Expression> params2 = Arrays.asList(new RealNumber(5.0), new RealNumber(4.0));
		List<Expression> params3 = Arrays.asList(new Plus(params1), new Minus(params2), new RealNumber(7.0));
		countVisitor1 = new CountVisitor();
        CountVisitor countVisitor2 = new CountVisitor();
		o = new Divides(params3);
		o2 = new Divides(params3);

		o.accept(countVisitor1);
		o2.accept(countVisitor2);
	}

	@Test
	void testEquals() {
		assertEquals(o, o2);
	}

	@Test
	void testCountDepth() {
		// Depth: Divides -> Plus/Minus -> RealNumbers
		assertEquals(2, countVisitor1.getDepthCount());
	}

	@Test
	void testCountOps() {
		// Operations: Divides, Plus, Minus
		assertEquals(3, countVisitor1.getOpsCount());
	}

	@Test
	void testCountNbs() {
		// Numbers: 3.0, 4.0, 5.0 (from Plus), 5.0, 4.0 (from Minus), 7.0
		// Total: 4 unique numbers
		assertEquals(4, countVisitor1.getNbCount());
	}
}
