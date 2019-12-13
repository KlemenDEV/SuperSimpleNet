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
 * Implementation of input layer for a layer network
 */
public class InputLayer extends Layer {

	public InputLayer(int inputCount, int size) {
		super(inputCount, size);

		this.neurons = new Neuron[size];
		for (int i = 0; i < size; i++)
			this.neurons[i] = new Neuron(inputCount + 1);
	}

	@Override public double[] calculateOutputs(double... inputs) {
		double[] fullin = new double[inputs.length + 1];
		System.arraycopy(inputs, 0, fullin, 0, inputs.length);
		fullin[inputs.length] = 1;
		return super.calculateOutputs(fullin);
	}

}
