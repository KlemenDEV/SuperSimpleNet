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
import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;

import java.awt.*;
import java.util.*;
import java.util.List;

public class TrainTestXOR {

	@Test public void train() {
		L3Classifier classifier = new L3Classifier(2, 2, 1);

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
				1, 4, (step, meanerror) -> {
					indexes.add(step);
					error.add(meanerror);

					chart.updateXYSeries("Epochs", indexes, error, null);
					sw.repaintChart();

					return meanerror > 0.01;
				});

		System.out.println("Classified (0, 0) as: " + classifier.classify(0, 0));
		System.out.println("Classified (0, 1) as: " + classifier.classify(0, 1));
		System.out.println("Classified (1, 0) as: " + classifier.classify(1, 0));
		System.out.println("Classified (1, 1) as: " + classifier.classify(1, 1));

		List<Double> xdata0 = new ArrayList<>();
		List<Double> ydata0 = new LinkedList<>();

		List<Double> xdata1 = new ArrayList<>();
		List<Double> ydata1 = new LinkedList<>();

		for (double x = -0.1; x < 1.1; x+=0.01) {
			for (double y = -0.1; y < 1.1; y+=0.01) {
				if (classifier.classify(x, y) == 0) {
					xdata0.add(x);
					ydata0.add(y);
				} else {
					xdata1.add(x);
					ydata1.add(y);
				}
			}
		}

		// display classification scatter plot
		XYChart chart2 = new XYChartBuilder().width(500).height(400).build();
		chart2.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
		chart2.getStyler().setLegendPosition(Styler.LegendPosition.OutsideE);
		chart2.getStyler().setChartTitleVisible(false);
		chart2.getStyler().setMarkerSize(5);
		chart2.getStyler().setLegendVisible(true);

		chart2.addSeries("0", xdata0, ydata0).setMarkerColor(Color.blue);
		chart2.addSeries("1", xdata1, ydata1).setMarkerColor(Color.red);

		SwingWrapper<XYChart> sw2 = new SwingWrapper<>(chart2);
		sw2.displayChart();

		new Scanner(System.in).nextLine();
	}

}
