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
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.diversify.disco.cloudml;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import junit.framework.TestCase;

/**
 *
 * @author huis
 */
public class BiMapTest extends TestCase{
    
    public void testWhat(){
        Multimap<String, String> biMap = HashMultimap.create();
        
        biMap.put("one", "1");
        biMap.put("one", "11");
        biMap.put("two", "2");
        
        System.out.println(biMap.get("one"));
        System.out.println(biMap.get("one"));
        
    }
    
}
