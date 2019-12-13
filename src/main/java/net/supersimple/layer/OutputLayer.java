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
 * Implementation of output layer for a layer network
 */
public class OutputLayer extends Layer {

	public OutputLayer(Layer parent, int size) {
		super(parent, size);

		this.neurons = new Neuron[size];
		for (int i = 0; i < size; i++)
			this.neurons[i] = new Neuron(parent.getOutputCount());
	}

	@Override public double[] calculateOutputs(double... inputs) {
		return Arrays.stream(neurons).parallel().mapToDouble(neuron -> neuron.compute(inputs)).toArray();
	}

	@Override public int getOutputCount() {
		return neurons.length;
	}

}
