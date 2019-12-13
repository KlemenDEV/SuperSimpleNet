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

/**
 * Class used to observe the learning process
 */
public interface ILearningObservator {

	/**
	 * Called each learning epoch
	 *
	 * @param epoch Current epoch number
	 * @param error Current learning error
	 * @return True, if learning should continue
	 */
	boolean iteration(int epoch, double error);

}
