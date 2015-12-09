package simulator.algebra.lalg.impl;

import java.util.ArrayList;

import simulator.algebra.exceptions.lalg.IllegalPositionException;
import simulator.algebra.lalg.Vector;

public class VectorImpl<T> implements Vector<T> {
	
	private ArrayList<T> elements;
	
	public VectorImpl(int size) {
		this.elements = new ArrayList<>(size);
		for(int i = 0; i < size; i++) {
			this.elements.add(null);
		}
	}

	@Override
	public T getElement(int pos) throws IllegalPositionException {
		try {
			return this.elements.get(pos);
		} catch(IndexOutOfBoundsException e) {
			throw new IllegalPositionException();
		}
	}

	@Override
	public void setElement(int pos, T value) throws IllegalPositionException {
		try {
			this.elements.set(pos, value);
		} catch(IndexOutOfBoundsException e) {
			throw new IllegalPositionException();
		}
	}

	@Override
	public int size() {
		return this.elements.size();
	}

	@Override
	public Vector<T> copy() {
		VectorImpl<T> res = new VectorImpl<>(this.size());
		
		for(int i = 0; i < this.size(); i++) {
			try {
				res.setElement(i, this.getElement(i));
			} catch (IllegalPositionException e) {
				// Impossible Exception
				return null;
			}
		}
		
		return res;
	}

}
