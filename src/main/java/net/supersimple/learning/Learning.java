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

package net.supersimple.learning;

import net.supersimple.layer.InputLayer;
import net.supersimple.layer.Neuron;
import net.supersimple.AbstractNetwork;
import net.supersimple.data.Sample;
import net.supersimple.layer.Layer;
import net.supersimple.layer.OutputLayer;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This class implements learning algorithm using backwards propagation with gradient descend
 */
public class Learning {

	private AbstractNetwork network;

	private Random random;

	public Learning(AbstractNetwork network) {
		this.network = network;
		this.random = new Random(1L);
	}

	/**
	 * Call to learn/train current network and test it on the same sample set
	 *
	 * @param trainSamples Samples for learning and testing
	 * @param learningRate Learning rate factor
	 * @param batchSize Size of a batch of learning samples per epoch
	 * @param observator Observator that observes and controls the learning process
	 */
	public void learn(List<Sample> trainSamples, double learningRate, int batchSize, ILearningObservator observator) {
		this.learn(trainSamples, trainSamples, learningRate, batchSize, observator);
	}

	/**
	 * Call to learn/train current network using given train and test datasets
	 *
	 * @param trainSamples Samples for learning
	 * @param testSamples Samples for testing
	 * @param learningRate Learning rate factor
	 * @param batchSize Size of a batch of learning samples per epoch
	 * @param observator Observator that observes and controls the learning process
	 */
	public void learn(List<Sample> trainSamples, List<Sample> testSamples, double learningRate, int batchSize,
			ILearningObservator observator) {
		List<Layer> layers = network.getOrderedLayersList();
		Layer output = network.getOutput();

		System.out.println("Training network with " + layers.size() + " layers");
		System.out.println("Network has " + output.getOutputCount() + " outputs, " + network.getInput().getNeuronCount()
				+ " input neruons, and " + (layers.size() - 2) + " hidden layers");

		int lidx = 1;
		for (Layer layer : layers) {
			if (!(layer instanceof InputLayer) && !(layer instanceof OutputLayer))
				System.out.println("Hidden layer " + (lidx++) + " has " + layer.getNeuronCount() + " neurons");
		}

		System.out.println("Initiating weights with random values...");
		for (Layer layer : layers) {
			for (int i = 0; i < layer.getNeuronCount(); i++) {
				Neuron neuron = layer.getNeuron(i);
				for (int j = 0; j < neuron.getWeights().length; j++) {
					neuron.getWeights()[j] = getSafeRandomWeight();
				}
			}
		}

		batchSize = Math.min(batchSize, trainSamples.size());

		System.out.println("Starting learning iterations with batch size " + batchSize);
		double[][][] oldweights;
		long startime = System.currentTimeMillis();

		for (int epoch = 0; ; epoch++) {
			for (Sample sample : trainSamples.subList(0, batchSize)) {
				oldweights = deepCopyWeights(network.getWeights());

				// determine desired output for the given sample
				double[] desiredOutput = new double[output.getOutputCount()];
				desiredOutput[sample.getSampleClass()] = 1; // require 1 on the neuron of the given class

				// do forwards propagation for current weights and save all intermediate outputs
				double[][] outputs = network.propagateForwards(sample.getFeatures());

				// calculate new weights for all layers, starting at the bottom
				int n_lplus1 = 0;
				double[] d_lplus1 = null;
				for (int l = layers.size() - 1; l >= 0; l--) {
					Layer currentLayer = layers.get(l);

					// d for each neuron in layer l
					double[] d_l = new double[currentLayer.getNeuronCount()];

					for (int j = 0; j < currentLayer.getNeuronCount(); j++) {
						double[] w = currentLayer.getNeuron(j).getWeights();

						// calculate dj for current neuron
						if (l == layers.size() - 1) { // check if the last layer (output)
							d_l[j] = (1 - outputs[l][j]) * outputs[l][j] * (desiredOutput[j] - outputs[l][j]);
						} else {
							double sum = 0;
							for (int p = 0; p < n_lplus1; p++)
								sum += d_lplus1[p] * oldweights[l + 1][p][j];
							d_l[j] = (1 - outputs[l][j]) * outputs[l][j] * sum;
						}

						// for each weight in a neuron
						for (int i = 0; i < w.length; i++) {
							if (l != 0) { // make sure it's not the first layer
								w[i] = w[i] + learningRate * d_l[j] * outputs[l - 1][i];
							} else { // input layer
								if (i == w.length - 1)
									w[i] = w[i] + learningRate * d_l[j]; // input bias weight
								else
									w[i] = w[i] + learningRate * d_l[j] * sample.getFeatures()[i];
							}
						} // neuron weights loop
					} // layer neurons loop

					n_lplus1 = currentLayer.getNeuronCount();
					d_lplus1 = d_l;
				} // layer loop
			} // samples loop

			AtomicReference<Double> error = new AtomicReference<>(0d);
			// do testing on all cores for faster processing
			testSamples.parallelStream().forEach(sample -> {
				// determine desired output for the given sample
				double[] desiredOutput = new double[output.getOutputCount()];
				desiredOutput[sample.getSampleClass()] = 1; // require 1 on the neuron of the given class

				double[][] outputs = network.propagateForwards(sample.getFeatures());
				for (int i = 0; i < desiredOutput.length; i++) {
					int ii = i;
					error.updateAndGet(v -> v + Math.pow(desiredOutput[ii] - outputs[outputs.length - 1][ii], 2));
				}
			});

			if (!observator.iteration(epoch, error.get() / testSamples.size())) {
				System.out.println("===============");
				System.out.println("Learning complete in " + epoch + " epochs in "
						+ (System.currentTimeMillis() - startime) / 1000d + " seconds");
				break;
			}

			Collections.shuffle(trainSamples, random);
		} // training loop
	}

	private double[][][] deepCopyWeights(double[][][] original) {
		double[][][] deepcopy;
		deepcopy = new double[original.length][][];
		for (int wi = 0; wi < original.length; wi++) {
			deepcopy[wi] = new double[original[wi].length][];
			for (int wj = 0; wj < original[wi].length; wj++) {
				deepcopy[wi][wj] = new double[original[wi][wj].length];
				System.arraycopy(original[wi][wj], 0, deepcopy[wi][wj], 0, original[wi][wj].length);
			}
		}
		return deepcopy;
	}

	private double getSafeRandomWeight() {
		double w = random.nextFloat() - 0.5f;
		if (w == 0)
			return getSafeRandomWeight();
		else
			return w;
	}

}
