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

import net.supersimple.learning.Learning;
import org.junit.Test;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import net.supersimple.data.CSVLoader;
import net.supersimple.data.Sample;
import net.supersimple.data.WeightsIO;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class TrainTestIsolet {

	@Test public void train() throws IOException, URISyntaxException {
		List<Sample> trainSamples = CSVLoader
				.loadSamples(Paths.get(ClassLoader.getSystemResource("isolet/isolet1+2+3+4.data").toURI()));
		List<Sample> testSamples = CSVLoader
				.loadSamples(Paths.get(ClassLoader.getSystemResource("isolet/isolet5.data").toURI()));

		L3Classifier classifier = new L3Classifier(617, 26, 250);
		Learning learning = new Learning(classifier);

		List<Integer> indexes = new ArrayList<>(Collections.singleton(0));
		List<Double> error = new ArrayList<>(Collections.singleton(0d));

		XYChart chart = QuickChart.getChart("Learning progression", "", "Error", "Epochs", indexes, error);
		SwingWrapper<XYChart> sw = new SwingWrapper<>(chart);
		sw.displayChart();

		indexes.clear();
		error.clear();

		learning.learn(trainSamples, testSamples, 0.07, 100, (step, mse) -> {
			indexes.add(step);
			error.add(mse);

			chart.updateXYSeries("Epochs", indexes, error, null);
			sw.repaintChart();

			return mse > 0.1;
		});

		System.out.println("Staring network testing");

		double properlyClassified = trainSamples.parallelStream()
				.filter(sample -> sample.getSampleClass() == classifier.classify(sample.getFeatures())).count();
		System.out.println("Properly classified (mAP): " + (properlyClassified / (double) trainSamples.size()) * 100d
				+ " % of training samples");

		properlyClassified = testSamples.parallelStream()
				.filter(sample -> sample.getSampleClass() == classifier.classify(sample.getFeatures())).count();
		System.out.println("Properly classified (mAP): " + (properlyClassified / (double) testSamples.size()) * 100d
				+ " % of test samples");

		System.out.println("Storing weights to a file");

		WeightsIO.writeToFile(Paths.get("isolet_weights.bin"), classifier.getWeights());

		new Scanner(System.in).nextLine();
	}

}
