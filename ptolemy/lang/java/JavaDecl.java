/*
A declaration in Java. Code converted from the Titanium project.

Copyright (c) 1998-1999 The Regents of the University of California.
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

@ProposedRating Red (ctsay@eecs.berkeley.edu)
@AcceptedRating Red (ctsay@eecs.berkeley.edu)
*/

package ptolemy.lang.java;

import java.util.LinkedList;
import ptolemy.lang.*;

/**
 *  The class JavaDecl declares many members, most of which make sense only
 *  for certain types of JavaDecl.  Attempts to access nonsensical members
 *  will cause runtime errors.

 *  By convention, a JavaDecl member named "getFoo" will return the "foo"
 *  attribute when called with no parameters, and set the "foo"
 *  attribute when called with one parameter.  Thus, decl.getType() is
 *  the type of the entity referred to by decl (a JavaDecl, presumably),
 *  and decl.setFoo(aType) sets the type attribute of decl to aType.
 *  Also, if member "foo" is not valid for all JavaDecls, there is a member
 *  "hasFoo()" that returns true or false depending on whether object
 *  on which it is called has a class for which "foo" may be called.
 *
 *  Objects of type JavaDecl should not be allocated; the class is intended
 *  as a base class for others.

 *   ATTRIBUTE name
 *     All Decls have a name, of type String.  These are
 *     the unique representative strings assigned by lexical analysis.
 *     The names of two Decls are considered the same iff they are the
 *     same pointer, ignoring contents: names that are different pointers
 *     to strings containing the same characters are considered distinct.
 *
 *   ATTRIBUTE container(), container (declp)
 *     Members, classes, interfaces, and packages are all parts of some
 *     larger declared entity, which is their container.  Members are
 *     contained in classes and interfaces, which are themselves contained
 *     in packages, which are in turn contained in other packages. However,
 *     inner classes have no container.
 *
 *     Outer-level packages have as their container the special Decl
 *     PackageDecl.SYSTEM_PACKAGE
 *
 *   ATTRIBUTE modifiers
 *     Classes, interfaces, and their members have modifiers, as defined
 *     by Modifier.
 *
 *   ATTRIBUTE environ
 *     Classes, interfaces, and packages define environments:  mappings of
 *     names (of members, classes, interfaces, and subpackages) to
 *     JavaDecls of these entities.
 *
 *   ATTRIBUTE source
 *     Decls that come from the current compilation have some piece of the
 *     AST associated with them.  For example, a JavaDecl for a local variable
 *     is created in response to a VarDeclNode.  That VarDeclNode becomes the
 *     source attribute of LocalVarDecl created to stand
 *     for that declaration.  ClassDecls have a a ClassDeclNode as their source,
 *     and so on.  Decls that arise as the result of importing a class
 *     have special dummy source nodes created for them.
 *
 *   ATTRIBUTE asType()
 *     For Decls for which isType() is true, a resolved
 *     TypeNameNode that stands for the type this class represents.  That
 *     is, it is a TypeNameNode whose decl() is THIS.
 *
 *  Code and comments converted from Decl in the Titanium project.
 *
 *  @author ctsay@eecs.berkeley.edu
 */
public abstract class JavaDecl extends Decl {

  protected JavaDecl(String name, int category0) {
    super(name, category0);
  }

  /** Return the container of this declaration.
   *  Members, classes, interfaces, and packages are all parts of some
   *  larger declared entity, which is their container.  Members are
   *  contained in classes and interfaces, which are themselves contained
   *  in packages, which are in turn contained in other packages.
   *  Outer-level packages have as their container the special JavaDecl
   *  StaticResoltuion.SYSTEM_PACKAGE.
   */
  public JavaDecl getContainer() {
    throw new RuntimeException(getClass().getName() + " has no container.");
  }

  /** Set the container of this declaration. */
  public void setContainer(JavaDecl dummy) {
    throw new RuntimeException(getClass().getName() + " has no container.");
  }

  /** Return true iff this declaration has a container. */
  public boolean hasContainer() { return false; }


  /** Return the environment associated with this declaration.
   *  Classes, interfaces, and packages define environments:  mappings of
   *  names (of members, classes, interfaces, and subpackages) to
   *  JavaDecls of these entities.
   */
  public Environ getEnviron() {
    throw new RuntimeException(getClass().getName() + " has no environ.");
  }

  /** Set the environment associated with this declaration. */
  public void setEnviron(Environ environ) {
    throw new RuntimeException(getClass().getName() + " has no environ.");
  }

  /** Return true iff this declaration has an environment associated with it. */
  public boolean hasEnviron() { return false; }

  /** Return the modifiers of this declaration.
   *  Classes, interfaces, and their members have modifiers, as defined in
   *  Modifiers.
   */
  public int getModifiers() {
    throw new RuntimeException(getClass().getName() + " has no modifiers.");
  }

  /** Set the modifiers of this declaration. */
  public void setModifiers(int dummy) {
    throw new RuntimeException(getClass().getName() + " has no modifiers.");
  }

  /** Return true iff this declaration has modifiers. */
  public boolean hasModifiers() { return false; }

  /** Return the source node associated with this declaration. */
  public TreeNode getSource() {
    throw new RuntimeException(getClass().getName() + " has no source.");
  }

  /** Set the source node associated with this declaration. */
  public void setSource(TreeNode dummy) {
    throw new RuntimeException(getClass().getName() + " has no source.");
  }

  /** Return true iff there is a source node associated with this declaration. */
  public boolean hasSource() { return false; }

  /** Return a string giving the full name (including class, etc) of this
   *  JavaDecl.  Optional delimiter will appear between nested components and
   *  defaults to a period.
   */
  public String fullName() {
    return fullName('.');
  }

  /** Return a string giving the full name (including class, etc) of this
   *  JavaDecl.  Optional delimiter will appear between nested components.
   */
  public String fullName(char delimiter) {
    StringBuffer prefix = new StringBuffer();

    if (hasContainer() && (getContainer() != null)) {
       prefix.append(getContainer().fullName(delimiter));
    }

    if (prefix.length() > 0) {
       prefix.append(delimiter);
    }

    prefix.append(getName());
    return prefix.toString();
  }

  /** Return true iff this declaration is contained by the container Decl.
   *  Search all super-containers of this declaration for the container.
   */
  public boolean deepContainedBy(JavaDecl container) {

    JavaDecl decl = this;

    while (decl.hasContainer()) {
      decl = decl.getContainer();

      if (decl == container) {
         return true;
      }

      if (decl == null) {
         return false;
      }
    }
    return false;
  }

  protected static final SearchPath _pickLibrary(JavaDecl container) {
    if (container == StaticResolution.UNNAMED_PACKAGE) {
       return SearchPath.UNNAMED_PATH;
    }
    return SearchPath.NAMED_PATH;
  }

  /** Return the Decl associated with the named node. Return null if the
   *  Decl is not found.
   */
  public static final JavaDecl getDecl(NamedNode node) {
    return (JavaDecl) node.getName().getProperty("decl");
  }

  /** Return the Decl associated with the name. Return null if the
   *  Decl is not found.
   */
  public static final JavaDecl getDecl(NameNode node) {
    return (JavaDecl) node.getProperty("decl");
  }

  /** Type ClassDecl representing a class. */
  public static final int CG_CLASS = 1;

  /** Type ClassDecl representing an interface. */
  public static final int CG_INTERFACE = 2;

  /** Type FieldDecl */
  public static final int CG_FIELD = 4;

  /** Type MethodDecl representing a method. */
  public static final int CG_METHOD = 8;

  /** Type MethodDecl representing a constructor. */
  public static final int CG_CONSTRUCTOR = 16;

  /** Type LocalVarDecl. */
  public static final int CG_LOCALVAR = 32;

  /** Type FormalParameterDecl. */
  public static final int CG_FORMAL = 64;

  /** Type PackageDecl. */
  public static final int CG_PACKAGE = 128;

  /** Type StmtLblDecl. */
  public static final int CG_STMTLABEL = 256;

  //public static final int CG_PRIMITIVE = 512;  // Type PrimitiveDecl.

  /** A constant used to search for either a class or interface. */
  public static final int CG_USERTYPE = CG_CLASS | CG_INTERFACE;

  /** A constant used to search for either a method or constructor. */
  public static final int CG_INVOKABLE = CG_METHOD | CG_CONSTRUCTOR;
}
