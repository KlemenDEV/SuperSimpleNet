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

import java.util.Arrays;

/**
 * Core implementation of a network layer
 */
public class Layer {

	/**
	 * Array of neurons in this layer
	 */
	Neuron[] neurons;

	private Layer child;

	/**
	 * @param parent Parent layer of the current layer
	 * @param size Number of neurons in this layer
	 */
	public Layer(Layer parent, int size) {
		this(parent.getOutputCount(), size);
		parent.child = this;
	}

	Layer(int inputCount, int size) {
		this.neurons = new Neuron[size];
		for (int i = 0; i < size; i++)
			this.neurons[i] = new Neuron(inputCount);
	}

	public void setWeights(double[][] w) {
		for (int i = 0; i < neurons.length; i++) {
			for (int j = 0; j < neurons[i].getWeights().length; j++) {
				neurons[i].getWeights()[j] = w[i][j];
			}
		}
	}

	/**
	 * Call this method to calculate neuron outputs based on the given input
	 *
	 * @param inputs Array of input values
	 * @return Array of output values of all the neurons in the network
	 */
	public double[] calculateOutputs(double... inputs) {
		double[] neuronouts = Arrays.stream(neurons).parallel().mapToDouble(neuron -> neuron.compute(inputs)).toArray();
		double[] fullout = new double[neuronouts.length + 1];
		System.arraycopy(neuronouts, 0, fullout, 0, neuronouts.length);
		fullout[neuronouts.length] = 1;
		return fullout;
	}

	/**
	 * @param j Neuron index
	 * @return Neuron object
	 */
	public Neuron getNeuron(int j) {
		return neurons[j];
	}

	/**
	 *
	 * @return Number of neurons in this layer / layer size
	 */
	public int getNeuronCount() {
		return neurons.length;
	}

	/**
	 *
	 * @return Child layer of the current layer (layer below this layer)
	 */
	public Layer getChild() {
		return child;
	}

	/**
	 *
	 * @return Number of outputs of this layer (this number includes +1 output that is used for bias "weight")
	 */
	public int getOutputCount() {
		return neurons.length + 1;
	}
}
