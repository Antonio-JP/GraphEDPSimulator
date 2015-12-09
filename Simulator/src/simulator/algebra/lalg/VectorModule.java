package simulator.algebra.lalg;

import simulator.algebra.Module;
import simulator.algebra.Ring;
import simulator.algebra.exceptions.lalg.ArithmeticException;
import simulator.algebra.exceptions.lalg.IllegalPositionException;

public abstract class VectorModule<R> extends Module<Vector<R>, R> {

	private Integer dimension;
	
	//Builders
	public VectorModule(Ring<R> ring, int size) {
		super(ring);
		
		this.dimension = size;
	}

	//Getters & Setters
	public Integer getDimension() {
		return this.dimension;
	}
	
	//Group Methods
	@Override
	public Vector<R> add(Vector<R> e1, Vector<R> e2) throws ArithmeticException {
		if(e1.size() != this.dimension || e2.size() != this.dimension) {
			throw new ArithmeticException();
		}

		Vector<R> res = this.getNewVector();
		
		for(int i = 0; i < this.dimension; i++) {
			try {
				res.setElement(i, this.subyacentRing.add(e1.getElement(i), e2.getElement(i)));
			} catch (IllegalPositionException e) {
				throw new ArithmeticException(e);
			}
		}
		
		return res;
	}

	@Override
	public Vector<R> opposite(Vector<R> e) throws ArithmeticException {
		if(e.size() != this.dimension) {
			throw new ArithmeticException();
		}
		
		Vector<R> res = this.getNewVector();
		for(int i = 0; i < this.dimension; i++) {
			try {
				res.setElement(i, this.subyacentRing.opposite(e.getElement(i)));
			} catch (IllegalPositionException e1) {
				//Impossible Exception
				return null;
			}
		}
		
		return res;
	}

	@Override
	public Vector<R> getZero() {
		Vector<R> res = this.getNewVector();
		
		for(int i = 0; i < this.dimension;  i++) {
			try {
				res.setElement(i, this.subyacentRing.getZero());
			} catch (IllegalPositionException e) {
				//Impossible exception
				return null;
			}
		}
		
		return res;
	}

	//Module methods
	@Override
	public Vector<R> scalar(Vector<R> element, R scalar) throws ArithmeticException {
		if(element.size() != this.dimension) {
			throw new ArithmeticException();
		}
		
		Vector<R> res = this.getNewVector();
		for(int i = 0; i < this.dimension; i++) {
			try {
				res.setElement(i, this.subyacentRing.multiply(element.getElement(i), scalar));
			} catch (IllegalPositionException e) {
				throw new ArithmeticException(e);
			}
		}
		
		return res;
	}
	
	//Other methods
	public R dot(Vector<R> e1, Vector<R> e2) throws ArithmeticException {
		if(e1.size() != this.dimension || e2.size() != this.dimension) {
			throw new ArithmeticException();
		}
		
		Ring<R> ring = this.subyacentRing;
		
		R res = ring.getZero();
		for(int i = 0; i < this.dimension; i++) {
			try {
				res = ring.add(res, ring.multiply(e1.getElement(i), e2.getElement(i)));
			} catch (IllegalPositionException e) {
				throw new ArithmeticException(e);
			}
		}
		
		return res;
	}
	
	//Abstract methods
	protected abstract Vector<R> getNewVector();
}
