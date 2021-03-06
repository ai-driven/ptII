# Makefile for the Java classes used to test the Ptolemy kernel
#
# @Authors: Christopher Hylands
#
# @Version: $Id$
#
# @Copyright (c) 1997-2012 The Regents of the University of California.
# All rights reserved.
#
# Permission is hereby granted, without written agreement and without
# license or royalty fees, to use, copy, modify, and distribute this
# software and its documentation for any purpose, provided that the
# above copyright notice and the following two paragraphs appear in all
# copies of this software.
#
# IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY
# FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES
# ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
# THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
# SUCH DAMAGE.
#
# THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY WARRANTIES,
# INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
# MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
# PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
# CALIFORNIA HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
# ENHANCEMENTS, OR MODIFICATIONS.
#
# 						PT_COPYRIGHT_VERSION_2
# 						COPYRIGHTENDKEY

# Location of this directory, relative to the Ptolemy II directory
ME =		ptolemy/kernel/test

# PTJUNIT_DIR is set to junit in ptII.mk by configure if JUnit is found.
DIRS =		$(PTJUNIT_DIR)

# Root of the Ptolemy II directory
ROOT =		../../..

CLASSPATH =	$(ROOT)

# Get configuration info
CONFIG =	$(ROOT)/mk/ptII.mk
include $(CONFIG)

# Tcl sources that are part of the testing infrastructure
TCL_SRCS = \
	ExampleSystem.tcl \
	testDefs.tcl

JSRCS = \
	ComponentEntityTimeTest.java \
	ExampleSystem.java \
	PortNameProblem.java \
	PortHasNoContainer.java \
	TestComponentPort.java \
	TestComponentRelation.java \
	TestCompositeEntity.java \
	TestSerializable.java

# Non-graphical Java tests written in Tcl
JSIMPLE_TESTS = \
	ComponentEntity.tcl \
	ComponentPort.tcl \
	ComponentRelation.tcl \
	CompositeEntity.tcl \
	Entity.tcl \
	InstantiableNamedObj.tcl \
	Port.tcl \
	Relation.tcl

# Graphical Java tests that use Tcl.
# If there are no tests, we use a dummy file so that the script that builds
# alljtests.tcl works.  If you add a test, be sure to add
# $(JGRAPHICAL_TESTS) to EXTRA_SRCS
JGRAPHICAL_TESTS = \
	dummy.tcl

EXTRA_SRCS =	$(TCL_SRCS) $(JSRCS) $(JSIMPLE_TESTS) #$(JGRAPHICAL_TESTS)

# Sources that may or may not be present, but if they are present, we don't
# want make checkjunk to barf on them.
MISC_FILES =	alljtests.tcl junit junit

# make checkjunk will not report OPTIONAL_FILES as trash
# make distclean removes OPTIONAL_FILES
OPTIONAL_FILES = \
	TestSerializable.data

# Unfortunately, some of the tests require classes outside of
# ptolemy/kernel and ptolemy/kernel/util.  This is not good
# since it means that the tests depend on more packages
# than the source code under test, which makes reuse outside
# of Ptolemy difficult.  As a workaround, we introduce
# a variable that lists classes that are runtime dependencies
# that don't get compiled when we compile the test .java files
# in this directory
JCLASS_DEPENDENCIES = \
	$(ROOT)/ptolemy/actor/lib/gui/TextFieldContainerJavaSE.class \
	$(ROOT)/ptolemy/actor/lib/gui/DisplayInterface.class \
	$(ROOT)/ptolemy/actor/lib/gui/DisplayJavaSE.class \
	$(ROOT)/ptolemy/actor/lib/gui/PlotterBaseInterface.class \
	$(ROOT)/ptolemy/actor/lib/gui/PlotterBaseJavaSE.class \
	$(ROOT)/ptolemy/media/javasound/LiveSoundInterface.class \
	$(ROOT)/ptolemy/media/javasound/LiveSoundJavaSE.class

JCLASS =	$(JSRCS:%.java=%.class)

# Don't include all or install rules here, we want the user
# to run 'make tests' to run the tests.

tests:: $(EXTRA_SRCS) jclass test_java test_jsimple
	# Ideally, we would use Tcl to create a test for serialization, but
	# Tcl Blend 1.1 with JDK1.1.6 gets StackOverflowErrors when
	# we call writeObject() on the ExampleSystem object.  JDK1.2
	# does not have this problem.
	CLASSPATH="$(CLASSPATH)" \
		"$(JAVA)" ptolemy.kernel.test.TestSerializable write
	CLASSPATH="$(CLASSPATH)" "$(JAVA)" ptolemy.kernel.test.TestSerializable

test_java: jclass

# PtolemyII-wide test definitions.
KERNEL_TESTDEFS = $(ROOT)/util/testsuite/testDefs.tcl

# These tests do not require a graphics terminal, but do use Jacl and Java
test_jsimple: $(EXTRA_SRCS) jclass $(KERNEL_TESTDEFS) alljtests.tcl $(JCLASS_DEPENDENCIES)
	$(JTCLSH) alljtests.tcl

$(ROOT)/ptolemy/actor/lib/gui/TextFieldContainerJavaSE.class:
	(cd $(ROOT)/ptolemy/actor/lib/gui; $(MAKE) TextFieldContainerJavaSE.class)
$(ROOT)/ptolemy/actor/lib/gui/DisplayInterface.class:
	(cd $(ROOT)/ptolemy/actor/lib/gui; $(MAKE) DisplayInterface.class)
$(ROOT)/ptolemy/actor/lib/gui/DisplayJavaSE.class:
	(cd $(ROOT)/ptolemy/actor/lib/gui; $(MAKE) DisplayJavaSE.class)
$(ROOT)/ptolemy/actor/lib/gui/PlotterBaseInterface.class:
	(cd $(ROOT)/ptolemy/actor/lib/gui; $(MAKE) PlotterBaseInterface.class)
$(ROOT)/ptolemy/actor/lib/gui/PlotterBaseJavaSE.class:
	(cd $(ROOT)/ptolemy/actor/lib/gui; $(MAKE) PlotterBaseJavaSE.class)
$(ROOT)/ptolemy/media/javasound/LiveSoundInterface.class:
	(cd $(ROOT)/ptolemy/media/javasound; $(MAKE) LiveSoundInterface.class)
$(ROOT)/ptolemy/media/javasound/LiveSoundJavaSE.class:
	(cd $(ROOT)/ptolemy/media/javasound; $(MAKE) LiveSoundJavaSE.class)


# Get the rest of the rules
include $(ROOT)/mk/ptcommon.mk
