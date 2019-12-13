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

import java.io.*;
import java.nio.file.Path;

public class WeightsIO {

	/**
	 * Writes weights to file
	 *
	 * @param path File path where to write to
	 * @param weights Array of weights of a network
	 * @throws IOException If write fails
	 */
	public static void writeToFile(Path path, double[][][] weights) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path.toFile()));
		oos.writeObject(weights);
	}

	/**
	 * Reads weights array from a file
	 *
	 * @param path File path where to read weight from
	 * @return Array of weights
	 * @throws IOException If read fails
	 * @throws ClassNotFoundException Should not be thrown
	 */
	public static double[][][] readFromFile(Path path) throws IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()));
		return (double[][][]) ois.readObject();
	}

}
