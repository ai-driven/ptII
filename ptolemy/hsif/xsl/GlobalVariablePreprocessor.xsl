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
	
This file is the first prepropessor for the transformation of HSIF file into MoML file.

This file deals with the global variables associated with Dynamic Network of Hybrid
Automata (DNHA) in HSIF. It transforms the HSIF file into an intemediate file with no
global variables. The format of the transformed file is neither HISF nor MoML. We need
to use SlimPreprocessor to get rid of redundant variables before apply further 
transformation.
 
This file checks the role of each global variable in Hybrid Automata (HA) to decide 
whether it works as input or output. Then the global variables are mapped into inputs
or outputs based on their roles.

In HSIF, a global variable has an attribute 'kind' to indicate its role in a model. HSIF uses
'input', 'controlled', or 'observable' to indicate its role in a model. The transformed file 
uses 'input' and 'output' for the values for kind attribure. The transformation maps the
global variables with kind attribute value as 'input' or 'controlled'  into 'input' or 'output' 
respectively, they are regarded as the inputs or outputs of the DNHA model. For the 
global variables with kind attribute value as 'observable', they are localized into inputs
or outputs of HA. In each hybrid automaton, if they are referred by actions or controlled
by differential equations, they are mapped into outputs of the HA; if they are referred by
invariants or the expressions to be integrated, they are mapped into inputs of the HA.

@author Haiyang Zheng
@version $Id$ 
@since HyVisual 2.2
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
                         xmlns:xalan="http://xml.apache.org/xslt" version="1.0">

	<!-- index every node via attribute _id -->
	<xsl:key name="nodeID" match="*" use="@_id"/>

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
	
	<!-- 
    	   Global Variables 
    	   If the global variables have kind attributes as 'input or 'controlled', they
    	   are regarded as the inputs or outputs of the dynamic network of HA. 
    	   The following code  transform those variables with changing the values 
    	   of the kind attributes into input or output respectively. 
    	   For the gloval variables with kind attributes as 'observable', they are not
    	   handled here. See Hybrid Automaton part below.
    -->
	<xsl:template match="IntegerVariable|RealVariable|BooleanVariable">
		<xsl:variable name="name" select="key('nodeID',@var)/@name"/>
		<xsl:choose>
			
			<!-- controlled mapped into output -->
			<xsl:when test="@kind='Controlled'">
				<xsl:comment>Controlled variables</xsl:comment>
				<xsl:copy>
					<xsl:for-each select="@*">
						<xsl:attribute name="{name()}">
						<xsl:variable name="kind"><xsl:value-of select="."/></xsl:variable>
						<xsl:choose>
							<xsl:when test="$kind='Controlled'">Output</xsl:when>
							<xsl:otherwise><xsl:value-of select="$kind"/></xsl:otherwise>
						</xsl:choose>
						</xsl:attribute>
					</xsl:for-each>
				</xsl:copy>
			</xsl:when>
			
			<!-- input mapped into input -->
			<xsl:when test="@kind='Input'">
				<xsl:comment>Input variables</xsl:comment>
				<xsl:copy>
					<xsl:for-each select="@*">
						<xsl:attribute name="{name()}"><xsl:value-of select="."/></xsl:attribute>
					</xsl:for-each>
				</xsl:copy>
			</xsl:when>
			
		</xsl:choose>
	</xsl:template>
	
	<!-- 
    	   Hybrid Automaton 
    	   Go through the details of a Hybrid Automaton to catch the referrences to the global
    	   variables. For the global variables, if they are referred by actions or controlled by 
	   differential equations, they are mapped into outputs; if they are referred by invariants
          or the expressions to be integrated, they are mapped into inputs.

	   Note that the same variable may be referred several times by different nodes. For 
	   each variables referrence, one port is generated. This may generate several
	   duplicate inputs or outputs. The SlimPreprocessor strips away the redundant inputs
	   or outputs. See SlimPreprocessor.xsl.
	
      -->
	<xsl:template match="HybridAutomaton">
		<xsl:variable name="HAName" select="@name"/>
		<xsl:copy>
			<xsl:for-each select="@*">
				<xsl:attribute name="{name()}"><xsl:value-of select="."/></xsl:attribute>
			</xsl:for-each>

			<!--  Variables referred by actions or controlled by differential equations 
				 are mapped into outputs. -->
			<xsl:for-each select="descendant::Action/VarRef|descendant::DiffEquation/VarRef|descendant::AlgEquation/VarRef">
				<xsl:for-each select="key('nodeID',@var)">
					<xsl:copy>
						<xsl:for-each select="@*">
							<xsl:attribute name="{name()}">
							<xsl:variable name="kind"><xsl:value-of select="."/></xsl:variable>
							<xsl:choose>
								<xsl:when test="$kind='Observable'">Output</xsl:when>
								<xsl:when test="$kind='Controlled'">Output</xsl:when>
								<xsl:otherwise><xsl:value-of select="$kind"/></xsl:otherwise>									</xsl:choose>
							</xsl:attribute>
						</xsl:for-each>
					</xsl:copy>
				</xsl:for-each>
			</xsl:for-each>

			<!--  Variables referred by invariants or the expressions to be integrated 
				 are mapped into inputs.-->

			<!-- if the Variable is referred by Transition guard expression, and not classified as output in above,
			 we treat it as input. -->
			<xsl:for-each select="Location/descendant::Expr/descendant::VarRef|Location/descendant::AExpr/descendant::VarRef|Transition/Expr/descendant::VarRef"> 
				<xsl:variable name="variable" select="@var"/>
				<xsl:variable name="counts" 	select="count(//DNHA/HybridAutomaton[@name=$HAName]/descendant::Action/VarRef[@var=$variable]|//DNHA/HybridAutomaton[@name=$HAName]/descendant::DiffEquation/VarRef[@var=$variable]|//DNHA/HybridAutomaton[@name=$HAName]/descendant::AlgEquation/VarRef[@var=$variable])"/>
				<xsl:if test="$counts=0">
					<xsl:for-each select="key('nodeID',$variable)">
						<xsl:copy>
							<xsl:for-each select="@*">
								<xsl:attribute name="{name()}">
								<xsl:variable name="kind"><xsl:value-of select="."/></xsl:variable>
								<xsl:choose>
									<xsl:when test="$kind='Observable'">Input</xsl:when>
									<xsl:when test="$kind='Input'">Input</xsl:when>
									<xsl:otherwise><xsl:value-of select="$kind"/></xsl:otherwise>						
								</xsl:choose>
								</xsl:attribute>
							</xsl:for-each>
						</xsl:copy>
					</xsl:for-each>
				</xsl:if>
			</xsl:for-each>

			<xsl:apply-templates select="*"/>

		</xsl:copy>
	</xsl:template>
</xsl:stylesheet>
