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
import net.supersimple.layer.Layer;
import net.supersimple.layer.OutputLayer;

/**
 * Three layer classifier implementation with one hidden layer of specified size
 */
public class L3Classifier extends AbstractClassifier {

	/**
	 *
	 * @param features Number of features of samples for classification
	 * @param classes Number of different classes that can be classified
	 * @param hiddenLayerSize Number of neurons in hidden layer
	 */
	public L3Classifier(int features, int classes, int hiddenLayerSize) {
		super(new InputLayer(features, features));

		setOutputLayer(new OutputLayer(new Layer(this.getInput(), hiddenLayerSize), // hidden layer 1
				classes));
	}

}
