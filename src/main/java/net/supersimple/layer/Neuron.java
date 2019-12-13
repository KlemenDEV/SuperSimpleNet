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

package net.supersimple.layer;

/**
 * Implementation of a single neuron
 */
public class Neuron {

	private final double[] weights;

	/**
	 * Number of inputs this neuron has
	 *
	 * @param inputCount Number of inputs / weights
	 */
	public Neuron(int inputCount) {
		this.weights = new double[inputCount];
	}

	/**
	 *
	 * @return Current neuron weights
	 */
	public double[] getWeights() {
		return weights;
	}

	/**
	 * Computes neuron output based on given input values
	 *
	 * @param x Neuron inputs
	 * @return Neuron output value
	 */
	public double compute(double... x) {
		double r = 0;
		for (int i = 0; i < weights.length; i++)
			r += x[i] * weights[i];

		return activationFunction(r);
	}

	/**
	 * Activation fuinction of the neuron
	 *
	 * @param z Function input
	 * @return Activation function result based on the input
	 */
	protected double activationFunction(double z) {
		return 1 / (1 + Math.exp(-z));
	}

}
