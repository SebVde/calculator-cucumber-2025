package calculator;

//Import Junit5 libraries for unit testing:
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import visitor.CountVisitor;

import java.util.Arrays;
import java.util.List;

class TestOperation {

	private Operation o;
	private Operation o2;
	private CountVisitor countVisitor1;
	private CountVisitor countVisitor2;

	@BeforeEach
	void setUp() throws Exception {
		List<Expression> params1 = Arrays.asList(new MyNumber(3), new MyNumber(4), new MyNumber(5));
		List<Expression> params2 = Arrays.asList(new MyNumber(5), new MyNumber(4));
		List<Expression> params3 = Arrays.asList(new Plus(params1), new Minus(params2), new MyNumber(7));
		countVisitor1 = new CountVisitor();
		countVisitor2 = new CountVisitor();
		o = new Divides(params3);
		o2 = new Divides(params3);

		o.accept(countVisitor1);
		o2.accept(countVisitor2);

	}

	@Test
	void testEquals() {
		assertEquals(o,o2);
	}

	@Test
	void testCountDepth() {
		assertEquals(1, countVisitor1.getDepthCount());
	}

	@Test
	void testCountOps() {
		assertEquals(3, countVisitor1.getOpsCount());
	}

	@Test
	void testCountNbs() {
		assertEquals(Integer.valueOf(6), countVisitor1.getNbCount());
	}

}
