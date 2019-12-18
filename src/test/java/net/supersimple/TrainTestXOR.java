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

import net.supersimple.data.Sample;
import net.supersimple.learning.Learning;
import org.junit.Test;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

import java.util.*;

public class TrainTestXOR {

	@Test public void train() {
		L3Classifier classifier = new L3Classifier(2, 2, 2);

		Learning learning = new Learning(classifier);

		List<Integer> indexes = new ArrayList<>(Collections.singleton(0));
		List<Double> error = new ArrayList<>(Collections.singleton(0d));

		XYChart chart = QuickChart.getChart("Learning progression", "", "Error", "Epochs", indexes, error);
		SwingWrapper<XYChart> sw = new SwingWrapper<>(chart);
		sw.displayChart();

		indexes.clear();
		error.clear();

		learning.learn(Arrays.asList(
				// @formatter:off
				new Sample(new double[] { 0, 0 }, 0),
				new Sample(new double[] { 0, 1 }, 1),
				new Sample(new double[] { 1, 0 }, 1),
				new Sample(new double[] { 1, 1 }, 0)),
				// @formatter:on
				0.3, 4, (step, meanerror) -> {
					if (step % 1000 == 0) {
						indexes.add(step);
						error.add(meanerror);

						chart.updateXYSeries("Epochs", indexes, error, null);
						sw.repaintChart();
					}

					return meanerror > 0.01;
				});

		System.out.println("Classified (0, 0) as: " + classifier.classify(0, 0));
		System.out.println("Classified (0, 1) as: " + classifier.classify(0, 1));
		System.out.println("Classified (1, 0) as: " + classifier.classify(1, 0));
		System.out.println("Classified (1, 1) as: " + classifier.classify(1, 1));

		new Scanner(System.in).nextLine();
	}

}
