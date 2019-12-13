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

import net.supersimple.data.CSVLoader;
import net.supersimple.data.WeightsIO;
import org.junit.Test;
import net.supersimple.data.Sample;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;

public class TestFromFileIsolet {

	@Test public void test() throws IOException, URISyntaxException, ClassNotFoundException {
		List<Sample> trainSamples = CSVLoader
				.loadSamples(Paths.get(ClassLoader.getSystemResource("isolet/isolet1+2+3+4.data").toURI()));
		List<Sample> testSamples = CSVLoader
				.loadSamples(Paths.get(ClassLoader.getSystemResource("isolet/isolet5.data").toURI()));

		L3Classifier classifier = new L3Classifier(617, 26, 250);

		System.out.println("Loading weights from a file");

		classifier.setWeights(WeightsIO.readFromFile(Paths.get("isolet_weights.bin")));

		System.out.println("Staring network testing");

		double properlyClassified = trainSamples.parallelStream()
				.filter(sample -> sample.getSampleClass() == classifier.classify(sample.getFeatures())).count();
		System.out.println("Properly classified (mAP): " + (properlyClassified / (double) trainSamples.size()) * 100d
				+ " % of training samples");

		properlyClassified = testSamples.parallelStream()
				.filter(sample -> sample.getSampleClass() == classifier.classify(sample.getFeatures())).count();
		System.out.println("Properly classified (mAP): " + (properlyClassified / (double) testSamples.size()) * 100d
				+ " % of test samples");
	}

}
