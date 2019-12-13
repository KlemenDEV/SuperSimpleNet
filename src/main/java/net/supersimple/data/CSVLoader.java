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

package net.supersimple.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CSVLoader {

	/**
	 * Loads Samples for learning, testing or classification from a file
	 *
	 * @param path File path
	 * @return List of samples
	 * @throws IOException If loading fails
	 */
	public static List<Sample> loadSamples(Path path) throws IOException {
		List<Sample> samples = Files.lines(path).parallel().map(line -> {
			String[] data = line.split(",");
			int classID = Integer.parseInt(data[data.length - 1].replace(".", "").trim()) - 1;
			return new Sample(
					Arrays.stream(data, 0, data.length - 1).parallel().mapToDouble(Double::parseDouble).toArray(),
					classID);
		}).collect(Collectors.toList());

		System.out.println("Loaded " + samples.size() + " samples from " + path);

		return samples;
	}

}
