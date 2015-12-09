package simulator.algebra.lalg.impl;

import simulator.algebra.exceptions.lalg.IllegalPositionException;
import simulator.algebra.lalg.Matrix;

public class MatrixImpl<T> implements Matrix<T> {
	
	VectorImpl<VectorImpl<T>> matrix;
	
	public MatrixImpl(int rows, int cols) {
		this.matrix = new VectorImpl<>(rows);
		
		for(int i = 0; i < rows; i++) {
			try {
				this.matrix.setElement(i, new VectorImpl<T>(cols));
			} catch (IllegalPositionException e) {
				// Impossible Exception
				e.printStackTrace();
			}
		}
	}

	@Override
	public T getElement(int row, int column) throws IllegalPositionException {
		return this.matrix.getElement(row).getElement(column);
	}

	@Override
	public void setElement(int row, int column, T value) throws IllegalPositionException {
		this.matrix.getElement(row).setElement(column, value);
	}

	@Override
	public int rows() {
		return matrix.size();
	}

	@Override
	public int cols() {
		try {
			return matrix.getElement(0).size();
		} catch (IllegalPositionException e) {
			// Impossible Exception
			e.printStackTrace();
			return 0;
		}
	}

}
