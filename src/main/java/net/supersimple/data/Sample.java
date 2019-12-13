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

package net.supersimple.data;

/**
 * Sample is a data sample with a class and given features that is used for learning, testing and classification
 */
public class Sample {

	private double[] features;
	private int sampleClass;

	public Sample(double[] features, int sampleClass) {
		this.features = features;
		this.sampleClass = sampleClass;
	}

	/**
	 *
	 * @return Array of sample features
	 */
	public double[] getFeatures() {
		return features;
	}

	/**
	 *
	 * @return Sample class
	 */
	public int getSampleClass() {
		return sampleClass;
	}
}
