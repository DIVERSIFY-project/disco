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

package eu.diversify.disco.population;

public class Specie {

    public static final String DEFAULT_SPECIE_NAME = "no name";
    public static final int DEFAULT_HEADCOUNT = 0;

    private String name;
    private int headcount;

    public Specie() {
        this(DEFAULT_SPECIE_NAME, DEFAULT_HEADCOUNT);
    }

    public Specie(String name) {
        this(name, DEFAULT_HEADCOUNT);
    }

    public Specie(String name, int headcount) {
        rejectInvalidName(name);
        rejectInvalidHeadcount(headcount);
        this.name = name;
        this.headcount = headcount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        rejectInvalidName(name);
        this.name = name;
    }

    private void rejectInvalidName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("'null' is not a valid specie name!");
        }
        if (name.equals("")) {
            throw new IllegalArgumentException("The empty string (i.e., '') is not a valid specie name!");
        }
    }

    public int getHeadcount() {
        return headcount;
    }

    public void setHeadcount(int headcount) {
        rejectInvalidHeadcount(headcount);
        this.headcount = headcount;
    }

    private void rejectInvalidHeadcount(int headcount) {
        if (headcount < 0) {
            final String message = String.format("Headcount for specie '%s' shall not be negative! Found: %d", name, headcount);
            throw new IllegalArgumentException(message);
        }
    }

    public void shiftHeadcountBy(int offset) {
        setHeadcount(headcount + offset);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Specie other = (Specie) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if (this.headcount != other.headcount) {
            return false;
        }
        return true;
    }

    public boolean isEmpty() {
        return headcount == 0;
    }
    
    public boolean isNamed(String name) {
        return this.name.equals(name);
    }
}
