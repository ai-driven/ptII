@echo off
rem MSDOS batch script to start ptplot

rem @author Edward A. Lee
rem @version $Id$
rem @copyright: Copyright (c) 1997-2001
rem The Regents of the University of California.

java -classpath %PTII% ptolemy.plot.plotml.HistogramMLApplication %1 %2 %3 %4 %5 %6 %7 %8 %9

