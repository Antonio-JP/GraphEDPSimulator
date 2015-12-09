package simulator.algebra.lalg;

import simulator.algebra.exceptions.lalg.IllegalPositionException;

public interface Matrix<T> {
	
	//Modifying interface
	public T getElement(int row, int column) throws IllegalPositionException;
	public void setElement(int row, int column, T value) throws IllegalPositionException;
	
	public int rows();
	public int cols();
	
	//Boolean conditions
	public default boolean isSym() {
		if(!this.isSquare()) {
			return false;
		}
		
		for(int i = 0; i < this.rows(); i++) {
			for(int j = 0; j < this.cols(); j++) {
				try {
					if(this.getElement(i, j) != this.getElement(j, i)) {
						return false;
					}
				} catch (IllegalPositionException e) {
					// Impossible Exception
					e.printStackTrace();
					return false;
				}
			}
		}
		
		return true;
	}
	
	public default boolean isSquare() {
		return this.rows() == this.cols();
	}
}
