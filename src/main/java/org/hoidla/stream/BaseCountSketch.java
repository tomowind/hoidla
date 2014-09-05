package org.hoidla.stream;

import org.hoidla.util.EpochObjectCounter;
import org.hoidla.util.Expirer;
import org.hoidla.util.Hashing;
import org.hoidla.util.ObjectCounter;
import org.hoidla.util.SequencedObjectCounter;
import org.hoidla.util.SimpleObjectCounter;

/**
 * @author pranab
 *
 */
public abstract class BaseCountSketch {
	//sketch
	protected int width;
	protected int depth;
	protected ObjectCounter[][] sketch;
	protected Expirer expirer;
	
	//hash family
	protected Hashing.MultiHashFamily hashFamily;
	
	//large prime
	protected int c = 1000099;
	
	/** 
	 * Constructor based on error bounds
	 * @param errorLimit
	 * @param errorProbLimit
	 */
	public BaseCountSketch(double errorLimit, double errorProbLimit) {
		width = (int)Math.round(Math.E / errorLimit);
		depth =(int)Math.round(Math.log(1.0 / errorProbLimit));
		initialize(width, depth);
	}	
	
	public BaseCountSketch(double errorLimit, double errorProbLimit, Expirer expirer) {
		this.expirer = expirer;
		width = (int)Math.round(2.0 / errorLimit);
		depth = (int)Math.round(Math.log(1.0 / (1.0 - errorProbLimit)));
		initialize(width, depth);
	}	

	/**
	 * Constructor  base of number of hash functions and hash value range
	 * @param width
	 * @param depth
	 */
	public BaseCountSketch(int width, int depth) {
		initialize(width, depth);
	}

	/**
	 * @param width
	 * @param depth
	 */
	public void initialize(int width, int depth) {
		this.width = width;
		this.depth = depth;
		sketch = new ObjectCounter[depth][width];
		hashFamily = new Hashing.MultiHashFamily(width, depth);

		//initialize
		for (int i = 0; i < depth; ++i) {
			for (int j = 0; j < width; ++j) {
				sketch[i][j] = expirer == null ? new SimpleObjectCounter() : 
					(expirer.isSequenceExpirer() ?  new SequencedObjectCounter() : new EpochObjectCounter());
			}
		}
	}
	
	/**
	 * 
	 */
	public void expire() {
		long current = expirer.isSequenceExpirer() ? System.currentTimeMillis() : 0;
		for (int i = 0; i < depth; ++i) {
			for (int j = 0; j < width; ++j) {
				sketch[i][j].expire(expirer, current);
			}
		}
	}
	
	public void initialize() {
		for (int i = 0; i < depth; ++i) {
			for (int j = 0; j < width; ++j) {
				sketch[i][j].initialize();
			}
		}
	}

	public int getCount() {
		int count = 0;
		for (int d = 0; d < depth; ++d) {
			for (int w = 0; w < width; ++w) {
				int thisCount = sketch[d][w].getCount();
				count += thisCount;
			}
		}			
		return count;
	}
	
}
