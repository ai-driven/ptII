<?xml version="1.0"?>
<!-- 
 Copyright (c) 2003-2004 The Regents of the University of California.
 All rights reserved.
 Permission is hereby granted, without written agreement and without
 license or royalty fees, to use, copy, modify, and distribute this
 software and its documentation for any purpose, provided that the above
 copyright notice and the following two paragraphs appear in all copies
 of this software.

 IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY
 FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES
 ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
 THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
 SUCH DAMAGE.

 THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY WARRANTIES,
 INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
 PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
 CALIFORNIA HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
 ENHANCEMENTS, OR MODIFICATIONS.

                                        PT_COPYRIGHT_VERSION_2
                                        COPYRIGHTENDKEY
                                        
@ProposedRating Red (hyzheng)
@AcceptedRating Red (cxh)
	
This file is the second prepropessor for the transformation of HSIF file into MoML file.

This file deals with the local variables associated with Hybrid Automata (HA).
There are local parameters and local variables defined by the syntax of HSIF. 
The difference between variables and parameters of HSIF is that parameters have 
fixed values. However, in Ptolemy II, the parameters can be assigned new values 
during execution. So, for the variables in HSIF which are not controlled by differential
equations, they are mapped into parameters in Ptolemy II. We need to use 
SlimPreprocessor to get rid of redundant parameters and variables before apply 
further transformation.

This file checks the roles of the local variables of HA. If the local variable is
controlled by some differential equation, it's treated as a local variable. If the 
local variable is used only by a expression to be integrated, it's regarded as 
a local parameter.

@author Haiyang Zheng
@version $Id$ 
@since HyVisual 2.2
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                         xmlns:xalan="http://xml.apache.org/xslt" version="1.0">

    <xsl:output doctype-system="HSIF.dtd"/>
    <xsl:output method="xml" indent="yes"/>

    <!-- features of the XSLT 2.0 language -->
    <xsl:decimal-format name="comma" decimal-separator="," grouping-separator="."/>

    <!-- time function -->
    <xsl:variable name="now" xmlns:Date="/java.util.Date">
        <xsl:value-of select="Date:toString(Date:new())"/>
    </xsl:variable>

    <!-- configuration -->
    <xsl:param name="author">Ptolemy II</xsl:param>
    <xsl:preserve-space elements="*"/>

	<!-- ==========================================================
          root element
          Generate comment on author and time. Apply the according template. 					    
         ========================================================== -->
	<xsl:template match="/">
		<xsl:comment>
			Generated by <xsl:value-of select="$author"/> at <xsl:value-of select="$now"/>.
		</xsl:comment>
		<xsl:apply-templates/>
	</xsl:template>
	
	<!-- General-Copy which copies everything. -->
	<xsl:template match="*">
		<xsl:copy>
			<xsl:for-each select="@*">
				<xsl:attribute name="{name()}"><xsl:value-of select="."/></xsl:attribute>
			</xsl:for-each>
			<xsl:apply-templates/>
		</xsl:copy>
	</xsl:template>

    <!-- Hybrid Automaton -->
    <xsl:template match="HybridAutomaton">
        <xsl:copy>

	     <!-- Copy all the properties of the Hybrid Automaton. -->
            <xsl:for-each select="@*">
                <xsl:attribute name="{name()}"><xsl:value-of select="."/></xsl:attribute>
            </xsl:for-each>

	     <!-- Copy everything of input or output local variables and local parameters of the HA. -->
            <xsl:apply-templates select="IntegerVariable[@kind='Input']|IntegerVariable[@kind='Output']"/>
            <xsl:apply-templates select="RealVariable[@kind='Input']|RealVariable[@kind='Output']"/>
            <xsl:apply-templates select="BooleanVariable[@kind='Input']|BooleanVariable[@kind='Output']"/>
            <xsl:apply-templates select="IntegerParameter|RealParameter|BooleanParameter"/>

	     <!-- For the integer variables with kind attribute value as controlled or observable, 
	     	    we check if they are controlled by some differential equation. If not, they are
	     	    treated as local parameters, if yes, they are treated as the local output variables. -->
            <xsl:for-each select="IntegerVariable[@kind='Controlled']|IntegerVariable[@kind='Observable']">
                <xsl:variable name="id" select="@_id"/>
                <xsl:variable name="counts"  select="count(..//DiffEquation/VarRef[@var=$id]|..//AlgEquation/VarRef[@var=$id])"/>

	         <!-- No referrence is found. -->
                <xsl:if test="$counts=0">
                    <xsl:comment> This variable is actually a parameter. </xsl:comment>
                    <xsl:element name="IntegerParameter">
                        <xsl:attribute name="_id" ><xsl:value-of select="$id"/></xsl:attribute>
                        <xsl:attribute name="name"><xsl:value-of select="@name"/></xsl:attribute>
                        <xsl:attribute name="value"><xsl:value-of select="0"/></xsl:attribute>
                    </xsl:element>
                </xsl:if>
                
	         <!-- Some referrence is found. -->
                <xsl:if test="$counts!=0">
                    <xsl:copy>
                        <xsl:for-each select="@*">
                            <xsl:attribute name="{name()}">
                                <xsl:variable name="kind"><xsl:value-of select="."/></xsl:variable>
                                <xsl:choose>
                                    <xsl:when test="$kind='Observable'">Output</xsl:when>
                                    <xsl:when test="$kind='Controlled'">Output</xsl:when>
                                    <xsl:otherwise><xsl:value-of select="$kind"/></xsl:otherwise>
                                </xsl:choose>
                            </xsl:attribute>
                        </xsl:for-each>
                    </xsl:copy>
                </xsl:if>
            </xsl:for-each>

            <xsl:for-each select="RealVariable[@kind='Controlled']|RealVariable[@kind='Observable']">
                <xsl:variable name="id" select="@_id"/>
                <xsl:variable name="counts"  select="count(..//DiffEquation/VarRef[@var=$id]|..//AlgEquation/VarRef[@var=$id])"/>
                
                <xsl:if test="$counts=0">
                    <xsl:comment> This variable is actually a parameter. </xsl:comment>
                    <xsl:element name="RealParameter">
                        <xsl:attribute name="_id" ><xsl:value-of select="$id"/></xsl:attribute>
                        <xsl:attribute name="name"><xsl:value-of select="@name"/></xsl:attribute>
                        <xsl:attribute name="value"><xsl:value-of select="0.0"/></xsl:attribute>
                    </xsl:element>
                </xsl:if>
                
                <xsl:if test="$counts!=0">
                    <xsl:copy>
                        <xsl:for-each select="@*">
                            <xsl:attribute name="{name()}">
                                <xsl:variable name="kind"><xsl:value-of select="."/></xsl:variable>
                                <xsl:choose>
                                    <xsl:when test="$kind='Observable'">Output</xsl:when>
                                    <xsl:when test="$kind='Controlled'">Output</xsl:when>
                                    <xsl:otherwise><xsl:value-of select="$kind"/></xsl:otherwise>
                                </xsl:choose>
                            </xsl:attribute>
                        </xsl:for-each>
                    </xsl:copy>
                </xsl:if>
            </xsl:for-each>

            <xsl:for-each select="BooleanVariable[@kind='Controlled']|BooleanVariable[@kind='Observable']">
                <xsl:variable name="id" select="@_id"/>
                <xsl:variable name="counts"  select="count(..//DiffEquation/VarRef[@var=$id]|..//AlgEquation/VarRef[@var=$id])"/>

                <xsl:if test="$counts=0">
                    <xsl:comment> This variable is actually a parameter. </xsl:comment>
                    <xsl:element name="BooleanParameter">
                        <xsl:attribute name="_id" ><xsl:value-of select="$id"/></xsl:attribute>
                        <xsl:attribute name="name"><xsl:value-of select="@name"/></xsl:attribute>
                        <xsl:attribute name="value"><xsl:value-of select="false"/></xsl:attribute>
                    </xsl:element>
                </xsl:if>
                
                <xsl:if test="$counts!=0">
                    <xsl:copy>
                        <xsl:for-each select="@*">
                            <xsl:attribute name="{name()}">
                                <xsl:variable name="kind"><xsl:value-of select="."/></xsl:variable>
                                <xsl:choose>
                                    <xsl:when test="$kind='Observable'">Output</xsl:when>
                                    <xsl:when test="$kind='Controlled'">Output</xsl:when>
                                    <xsl:otherwise><xsl:value-of select="$kind"/></xsl:otherwise>
                                </xsl:choose>
                            </xsl:attribute>
                        </xsl:for-each>
                    </xsl:copy>
                </xsl:if>
            </xsl:for-each>

	     <!-- Copy everything of locations and transitions. -->
            <xsl:apply-templates select="Transition|Location"/>

        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>
