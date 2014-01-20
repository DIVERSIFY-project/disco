@REM
@REM
@REM This file is part of Disco.
@REM
@REM Disco is free software: you can redistribute it and/or modify
@REM it under the terms of the GNU Lesser General Public License as published by
@REM the Free Software Foundation, either version 3 of the License, or
@REM (at your option) any later version.
@REM
@REM Disco is distributed in the hope that it will be useful,
@REM but WITHOUT ANY WARRANTY; without even the implied warranty of
@REM MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
@REM GNU Lesser General Public License for more details.
@REM
@REM You should have received a copy of the GNU Lesser General Public License
@REM along with Disco.  If not, see <http://www.gnu.org/licenses/>.
@REM

@REM
@REM This launch the Single Run experiment
@REM
@REM Franck Chauvel
@REM

@echo off

java -cp .\bin\controller-0.1-SNAPSHOT.jar;.\bin\snakeyaml-1.13.jar;.\bin\population-0.1-SNAPSHOT.jar;.\bin\experiments.controllers.scalability-0.1-SNAPSHOT.jar eu.diversify.disco.experiments.controllers.scalability.Runner

r CMD BATCH --vanilla --slave view.r