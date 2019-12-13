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
import net.supersimple.layer.OutputLayer;
import net.supersimple.layer.Layer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implements basic network of layers implementation
 */
public abstract class AbstractNetwork {

	private InputLayer input;
	private OutputLayer output;

	private List<Layer> layerscache;

	/**
	 * @param input Input layer of this network
	 */
	public AbstractNetwork(InputLayer input) {
		this.input = input;
	}

	/**
	 * This method must be called before the network is actually used!
	 *
	 * @param output Output layer that contains links to all hidden layers
	 */
	public void setOutputLayer(OutputLayer output) {
		this.output = output;
	}

	/**
	 * Sets new weight values
	 *
	 * @param weights Array of weight values
	 */
	public final void setWeights(double[][][] weights) {
		List<Layer> layers = this.getOrderedLayersList();
		for (int i = 0; i < layers.size(); i++)
			layers.get(i).setWeights(weights[i]);
	}

	/**
	 * @return Current weights of all neurons in this network
	 */
	public final double[][][] getWeights() {
		List<Layer> layers = this.getOrderedLayersList();
		double[][][] retval = new double[layers.size()][][];

		for (int l = 0; l < layers.size(); l++) {
			Layer layer = layers.get(l);
			retval[l] = new double[layer.getNeuronCount()][];
			for (int j = 0; j < layer.getNeuronCount(); j++)
				retval[l][j] = layer.getNeuron(j).getWeights();
		}

		return retval;
	}

	/**
	 * @return Ordered list of layers starting with input layer
	 */
	public final List<Layer> getOrderedLayersList() {
		if (layerscache != null)
			return layerscache;

		List<Layer> layers = new ArrayList<>(Collections.singleton(input));

		Layer layer = input;
		while (!((layer = layer.getChild()) instanceof OutputLayer))
			layers.add(layer);

		layers.add(output);

		layerscache = layers;

		return layers;
	}

	/**
	 * @return Input layer of this network
	 */
	public final InputLayer getInput() {
		return input;
	}

	/**
	 * @return Output layer of this network
	 */
	public final OutputLayer getOutput() {
		return output;
	}

	/**
	 * Call this method to perform forwards propagation for the given inputs
	 *
	 * @param inputs Array of inputs that are passed to the input layer
	 * @return Array of outputs of all the layers, the last row of this array are outputs of the output layer
	 */
	public final double[][] propagateForwards(double... inputs) {
		List<Layer> layers = getOrderedLayersList();
		double[][] propagationResult = new double[layers.size()][];
		return propagateForwardsImpl(propagationResult, layers, 0, inputs);
	}

	private double[][] propagateForwardsImpl(double[][] propagationResult, List<Layer> layers, int layer,
			double... inputs) {
		double[] currentOutputs = layers.get(layer).calculateOutputs(inputs);
		propagationResult[layer] = currentOutputs;
		if (layers.get(layer) instanceof OutputLayer) {
			return propagationResult;
		} else {
			return propagateForwardsImpl(propagationResult, layers, layer + 1, currentOutputs);
		}
	}

}
