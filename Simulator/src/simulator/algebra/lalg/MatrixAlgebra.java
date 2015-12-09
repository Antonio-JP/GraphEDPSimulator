package simulator.algebra.lalg;

import simulator.algebra.Algebra;
import simulator.algebra.Ring;
import simulator.algebra.exceptions.NotUnitException;
import simulator.algebra.exceptions.ZeroDivisionException;
import simulator.algebra.exceptions.lalg.ArithmeticException;
import simulator.algebra.exceptions.lalg.IllegalPositionException;

public abstract class MatrixAlgebra<R> extends Algebra<Matrix<R>, R> {

	private Integer dimension;
	
	//Builders
	public MatrixAlgebra(Ring<R> ring, int dimension) {
		super(ring);
		
		this.dimension = dimension;
	}
	
	//Getters & Setters
	public Integer getDimension() {
		return this.dimension;
	}

	//Group Methods
	@Override
	public Matrix<R> add(Matrix<R> e1, Matrix<R> e2) throws ArithmeticException {
		if(!e1.isSquare() || this.dimension != e1.cols() || e2.isSquare() || this.dimension != e2.cols()) {
			throw new ArithmeticException();
		}
		
		Matrix<R> res = this.getNewMatrix();
		
		for(int i = 0; i < this.dimension; i++) {
			for(int j = 0; j < this.dimension; j++) {
				try {
					res.setElement(i, j, this.subyacentRing.add(e1.getElement(i, j), e2.getElement(i, j)));
				} catch (IllegalPositionException e) {
					throw new ArithmeticException(e);
				}
			}
		}
		
		return res;
	}

	@Override
	public Matrix<R> opposite(Matrix<R> e) throws ArithmeticException {
		if(!e.isSquare() || this.dimension != e.cols()) {
			throw new ArithmeticException();
		}
		
		Matrix<R> res = this.getNewMatrix();
		
		for(int i = 0; i < this.dimension; i++) {
			for(int j = 0; j < this.dimension; j++) {
				try {
					res.setElement(i, j, this.subyacentRing.opposite(e.getElement(i, j)));
				} catch (IllegalPositionException e1) {
					throw new ArithmeticException(e1);
				}
			}
		}
		
		return res;
	}

	@Override
	public Matrix<R> getZero() {
		Matrix<R> res = this.getNewMatrix();
		
		for(int i = 0; i < this.dimension; i++) {
			for(int j = 0; j < this.dimension; j++) {
				try {
					res.setElement(i, j, this.subyacentRing.getZero());
				} catch (IllegalPositionException e) {
					// Impossible exception
					return null;
				}
			}
		}
		
		return res;
	}

	//Ring methods
	@Override
	public Matrix<R> multiply(Matrix<R> e1, Matrix<R> e2) throws ArithmeticException {
		if(!e1.isSquare() || this.dimension != e1.cols() || e2.isSquare() || this.dimension != e2.cols()) {
			throw new ArithmeticException();
		}
		
		Matrix<R> res = this.getNewMatrix();
		Ring<R> ring = this.subyacentRing;
		
		try {
			for(int i = 0; i < this.dimension; i++) {
				for(int j = 0; j < this.dimension; j++) {
					R value = ring.getZero();
					for(int k = 0; k < this.dimension; k++) {
						value = ring.add(value, ring.multiply(e1.getElement(i, k),e2.getElement(k, j)));
					}
					
					res.setElement(i, j, value);
				}
			}
		} catch (IllegalPositionException e) {
			throw new ArithmeticException(e);
		}
		
		return res;
	}

	@Override
	public Matrix<R> inverse(Matrix<R> e) throws ArithmeticException, NotUnitException, ZeroDivisionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Matrix<R> getUnity() {
		Matrix<R> res = this.getNewMatrix();
		
		try {
			for(int i = 0; i < this.dimension; i++) {
				for(int j = 0; j < this.dimension; j++) {
					if(i == j) {
						res.setElement(i, j, this.subyacentRing.getUnity());
					} else {
						res.setElement(i, j, this.subyacentRing.getZero());
					}
				}
			}
		} catch(IllegalPositionException e1) {
			//Impossible Exception
			return null;
		}
		
		return res;
	}

	//Module methods
	@Override
	public Matrix<R> scalar(Matrix<R> element, R scalar) throws ArithmeticException {
		if(!element.isSquare() || this.dimension != element.cols()) {
			throw new ArithmeticException();
		}
		
		Matrix<R> res = this.getNewMatrix();
		
		for(int i = 0; i < this.dimension; i++) {
			for(int j = 0; j < this.dimension; j++) {
				try {
					res.setElement(i, j, this.subyacentRing.multiply(element.getElement(i, j), scalar));
				} catch (IllegalPositionException e) {
					throw new ArithmeticException(e);
				}
			}
		}
		
		return res;
	}
	
	//Other methods
	public Matrix<R> getTranspose(Matrix<R> matrix) throws ArithmeticException {
		if(!matrix.isSquare() || this.dimension != matrix.cols()) {
			throw new ArithmeticException();
		}
		
		Matrix<R> res = this.getNewMatrix();
		
		for(int i = 0; i < this.dimension; i++) {
			for(int j = 0; j < this.dimension; j++) {
				try {
					res.setElement(i, j, matrix.getElement(j, i));
				} catch (IllegalPositionException e) {
					throw new ArithmeticException(e);
				}
			}
		}
		
		return res;
	}
	
	public R getDeterminant(Matrix<R> matrix) throws ArithmeticException {
		if(!matrix.isSquare() || this.dimension != matrix.cols()) {
			throw new ArithmeticException();
		}
		
		//TODO Implement this method
		return this.subyacentRing.getZero();
	}
	
	public R getTrace(Matrix<R> matrix) throws ArithmeticException {
		if(!matrix.isSquare() || this.dimension != matrix.cols()) {
			throw new ArithmeticException();
		}
		
		R res = this.subyacentRing.getZero();
		
		for(int i = 0; i < this.dimension; i++) {
			try {
				res = this.subyacentRing.add(res, matrix.getElement(i, i));
			} catch (IllegalPositionException e) {
				throw new ArithmeticException(e);
			}
		}
		
		return res;
	}

	//Abstract methods
	protected abstract Matrix<R> getNewMatrix();
}
