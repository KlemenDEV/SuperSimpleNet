/*
 * Copyright (c) 2019 Klemen Pevec
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package net.supersimple;

import net.supersimple.layer.InputLayer;

/**
 * Implements basic classifier
 */
public abstract class AbstractClassifier extends AbstractNetwork {

	public AbstractClassifier(InputLayer input) {
		super(input);
	}

	/**
	 * Call this method to classify a sample based on given features
	 *
	 * @param features Input sample features
	 * @return Class that the sample was classified into based on the input featues
	 */
	public int classify(double... features) {
		double[][] result = this.propagateForwards(features);

		int classifiedClass = -1;
		double currentMax = Double.MIN_VALUE;
		for (int i = 0; i < result[result.length - 1].length; i++) {
			if (result[result.length - 1][i] > currentMax) {
				currentMax = result[result.length - 1][i];
				classifiedClass = i;
			}
		}

		return classifiedClass;
	}
}
