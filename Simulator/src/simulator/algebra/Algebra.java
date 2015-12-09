package simulator.algebra;

public abstract class Algebra<T, R> extends Module<T, R> implements Ring<T> {

	public Algebra(Ring<R> ring) {
		super(ring);
	}
}
