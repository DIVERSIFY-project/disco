/**
 *
 * This file is part of Disco.
 *
 * Disco is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Disco is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Disco.  If not, see <http://www.gnu.org/licenses/>.
 */
package eu.diversify.disco.controller;

import eu.diversify.disco.controller.exceptions.ControllerInstantiationException;
import eu.diversify.disco.controller.exceptions.UnknownStrategyException;
import java.util.HashMap;

/**
 * Create controller based on their name
 *
 * @author Franck Chauvel
 * @since 0.1
 */
public class ControllerFactory {

    private final HashMap<String, String> strategies;

    /**
     * Create a new ControllerFactory
     */
    public ControllerFactory() {
        this.strategies = new HashMap<String, String>();
        this.strategies.put("HILL CLIMBING", "eu.diversify.disco.controller.HillClimber");
        this.strategies.put("ADAPTIVE HILL CLIMBING", "eu.diversify.disco.controller.AdaptiveHillClimber");
        this.strategies.put("BREADTH-FIRST SEARCH", "eu.diversify.disco.controller.BreadthFirstExplorer");
    }

    /**
     * Instantiate a controller based on the name if the given strategy can be
     * match against the existing one.
     *
     * @param strategy the name of the strategy to instantiate
     *
     * @return an fresh object reflecting the given strategy
     */
    public Controller instantiate(String strategy) throws ControllerInstantiationException {
        Controller result = null;
        final String escaped = strategy.trim().replaceAll("\\s+", " ").toUpperCase();
        final String className = this.strategies.get(escaped);
        if (className == null) {
            throw new UnknownStrategyException(strategy);
            
        } else {
            try {
                result = (Controller) Class.forName(className).newInstance();
            
            } catch (ClassNotFoundException cnfe) {
                throw new ControllerInstantiationException(className, cnfe);
                
            } catch (IllegalAccessException iae) {
                throw new ControllerInstantiationException(className, iae);
                
            } catch (InstantiationException ie) {
                throw new ControllerInstantiationException(className, ie);
                
            }
        }
        return result;
    }
    
}
