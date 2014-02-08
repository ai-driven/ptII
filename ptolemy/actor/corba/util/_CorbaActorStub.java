package ptolemy.actor.corba.util;

/**
 * ptolemy/actor/corba/util/_CorbaActorStub.java
 * Generated by the IDL-to-Java compiler (portable), version "3.0"
 * from CorbaActor.idl
 * Thursday, January 18, 2001 7:07:58 PM PST
 */

/* A CORBA compatible interface that implements the execution
 * methods of Ptolemy II.
 */
@SuppressWarnings("serial")
public class _CorbaActorStub extends org.omg.CORBA.portable.ObjectImpl
        implements ptolemy.actor.corba.util.CorbaActor {
    // Constructors
    // NOTE:  If the default constructor is used, the
    //        object is useless until _set_delegate (...)
    //        is called.
    public _CorbaActorStub() {
        super();
    }

    public _CorbaActorStub(org.omg.CORBA.portable.Delegate delegate) {
        super();
        _set_delegate(delegate);
    }

    /* Mirror the fire() method of Ptolemy II
     * executable interface.
     * @exception CorbaIllegalActionException If the
     *   method is an illegal action of the actor.
     */
    public void fire()
            throws ptolemy.actor.corba.util.CorbaIllegalActionException {
        // Don't call "super.fire();" here, there is no parent class
        // that has a fire method.
        org.omg.CORBA.portable.InputStream _in = null;
        try {
            org.omg.CORBA.portable.OutputStream _out = _request("fire", true);
            _in = _invoke(_out);
        } catch (org.omg.CORBA.portable.ApplicationException _ex) {
            _in = _ex.getInputStream();

            String _id = _ex.getId();

            if (_id.equals("IDL:util/CorbaIllegalActionException:1.0")) {
                throw ptolemy.actor.corba.util.CorbaIllegalActionExceptionHelper
                        .read(_in);
            } else {
                throw new org.omg.CORBA.MARSHAL(_id);
            }
        } catch (org.omg.CORBA.portable.RemarshalException _rm) {
            fire();
        } finally {
            _releaseReply(_in);
        }
    } // fire

    /* Return the value (in the form of a string) of
     * a parameter.
     * @exception CorbaIllegalActionException If the
     *  query of parameter is not supported by the actor.
     * @exception CorbaUnknowParamException If the parameter
     *  name is not known by the actor.
     */
    public String getParameter(String paramName)
            throws ptolemy.actor.corba.util.CorbaIllegalActionException,
            ptolemy.actor.corba.util.CorbaUnknownParamException {
        org.omg.CORBA.portable.InputStream _in = null;

        try {
            org.omg.CORBA.portable.OutputStream _out = _request("getParameter",
                    true);
            _out.write_string(paramName);
            _in = _invoke(_out);

            String __result = _in.read_string();
            return __result;
        } catch (org.omg.CORBA.portable.ApplicationException _ex) {
            _in = _ex.getInputStream();

            String _id = _ex.getId();

            if (_id.equals("IDL:util/CorbaIllegalActionException:1.0")) {
                throw ptolemy.actor.corba.util.CorbaIllegalActionExceptionHelper
                        .read(_in);
            } else if (_id.equals("IDL:util/CorbaUnknownParamException:1.0")) {
                throw ptolemy.actor.corba.util.CorbaUnknownParamExceptionHelper
                        .read(_in);
            } else {
                throw new org.omg.CORBA.MARSHAL(_id);
            }
        } catch (org.omg.CORBA.portable.RemarshalException _rm) {
            return getParameter(paramName);
        } finally {
            _releaseReply(_in);
        }
    } // getParameter

    /* Mirror the initialize() method of Ptolemy II
     * executable interface.
     * @exception CorbaIllegalActionException If the
     *   method is an illegal action of the actor.
     */
    public void initialize()
            throws ptolemy.actor.corba.util.CorbaIllegalActionException {
        org.omg.CORBA.portable.InputStream _in = null;

        try {
            org.omg.CORBA.portable.OutputStream _out = _request("initialize",
                    true);
            _in = _invoke(_out);
        } catch (org.omg.CORBA.portable.ApplicationException _ex) {
            _in = _ex.getInputStream();

            String _id = _ex.getId();

            if (_id.equals("IDL:util/CorbaIllegalActionException:1.0")) {
                throw ptolemy.actor.corba.util.CorbaIllegalActionExceptionHelper
                        .read(_in);
            } else {
                throw new org.omg.CORBA.MARSHAL(_id);
            }
        } catch (org.omg.CORBA.portable.RemarshalException _rm) {
            initialize();
        } finally {
            _releaseReply(_in);
        }
    } // initialize

    /* Return true if the specified channel of the specified
     * port contains unsent data.
     * @exception CorbaIllegalActionException If the query is
     *   not supported by the actor.
     * @exception CorbaUnknownPortException If the specified
     *   port is not known by the actor.
     * @exception CorbaIndexOutofBoundException If the
     *   channel index is out of the width of the port.
     */
    public boolean hasData(String portName, short portIndex)
            throws ptolemy.actor.corba.util.CorbaIllegalActionException,
            ptolemy.actor.corba.util.CorbaIndexOutofBoundException,
            ptolemy.actor.corba.util.CorbaUnknownPortException {
        org.omg.CORBA.portable.InputStream _in = null;

        try {
            org.omg.CORBA.portable.OutputStream _out = _request("hasData", true);
            _out.write_string(portName);
            _out.write_short(portIndex);
            _in = _invoke(_out);

            boolean __result = _in.read_boolean();
            return __result;
        } catch (org.omg.CORBA.portable.ApplicationException _ex) {
            _in = _ex.getInputStream();

            String _id = _ex.getId();

            if (_id.equals("IDL:util/CorbaIllegalActionException:1.0")) {
                throw ptolemy.actor.corba.util.CorbaIllegalActionExceptionHelper
                        .read(_in);
            } else if (_id.equals("IDL:util/CorbaIndexOutofBoundException:1.0")) {
                throw ptolemy.actor.corba.util.CorbaIndexOutofBoundExceptionHelper
                        .read(_in);
            } else if (_id.equals("IDL:util/CorbaUnknownPortException:1.0")) {
                throw ptolemy.actor.corba.util.CorbaUnknownPortExceptionHelper
                        .read(_in);
            } else {
                throw new org.omg.CORBA.MARSHAL(_id);
            }
        } catch (org.omg.CORBA.portable.RemarshalException _rm) {
            return hasData(portName, portIndex);
        } finally {
            _releaseReply(_in);
        }
    } // hasData

    /* Return true if there is a parameter of the specified
     * name defined in the actor.
     */
    public boolean hasParameter(String paramName) {
        org.omg.CORBA.portable.InputStream _in = null;

        try {
            org.omg.CORBA.portable.OutputStream _out = _request("hasParameter",
                    true);
            _out.write_string(paramName);
            _in = _invoke(_out);

            boolean __result = _in.read_boolean();
            return __result;
        } catch (org.omg.CORBA.portable.ApplicationException _ex) {
            _in = _ex.getInputStream();

            String _id = _ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException _rm) {
            return hasParameter(paramName);
        } finally {
            _releaseReply(_in);
        }
    } // hasParameter

    /* Return true if there is a port of the specified name
     * and specified property contained by the actor.
     * @param portName The name of the port.
     * @param isInput True if the port is an input port.
     * @param isOutput True if the port is an output port.
     * @param isMultiport True if the port is a multiport.
     */
    public boolean hasPort(String portName, boolean isInput, boolean isOutput,
            boolean isMultiport) {
        org.omg.CORBA.portable.InputStream _in = null;

        try {
            org.omg.CORBA.portable.OutputStream _out = _request("hasPort", true);
            _out.write_string(portName);
            _out.write_boolean(isInput);
            _out.write_boolean(isOutput);
            _out.write_boolean(isMultiport);
            _in = _invoke(_out);

            boolean __result = _in.read_boolean();
            return __result;
        } catch (org.omg.CORBA.portable.ApplicationException _ex) {
            _in = _ex.getInputStream();

            String _id = _ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException _rm) {
            return hasPort(portName, isInput, isOutput, isMultiport);
        } finally {
            _releaseReply(_in);
        }
    } // hasPort

    /* Set the width of the specified port.
     * @param portName The name of the port.
     * @param width The width to be set.
     * @exception CorbaIllegalActionException If the width
     *  to be set is not supported by the port, e.g. the
     *  port is restricted to a single port, but the width
     *  to be set is greater than one.
     * @exception CorbaUnknownPortException If the port is \
     *  not known by the actor.
     */
    public void setPortWidth(String portName, short width)
            throws ptolemy.actor.corba.util.CorbaIllegalActionException,
            ptolemy.actor.corba.util.CorbaUnknownPortException {
        org.omg.CORBA.portable.InputStream _in = null;

        try {
            org.omg.CORBA.portable.OutputStream _out = _request("setPortWidth",
                    true);
            _out.write_string(portName);
            _out.write_short(width);
            _in = _invoke(_out);
        } catch (org.omg.CORBA.portable.ApplicationException _ex) {
            _in = _ex.getInputStream();

            String _id = _ex.getId();

            if (_id.equals("IDL:util/CorbaIllegalActionException:1.0")) {
                throw ptolemy.actor.corba.util.CorbaIllegalActionExceptionHelper
                        .read(_in);
            } else if (_id.equals("IDL:util/CorbaUnknownPortException:1.0")) {
                throw ptolemy.actor.corba.util.CorbaUnknownPortExceptionHelper
                        .read(_in);
            } else {
                throw new org.omg.CORBA.MARSHAL(_id);
            }
        } catch (org.omg.CORBA.portable.RemarshalException _rm) {
            setPortWidth(portName, width);
        } finally {
            _releaseReply(_in);
        }
    } // setPortWidth

    /* Mirror the postfire() method of Ptolemy II
     * executable interface.
     * @exception CorbaIllegalActionException If the
     *   method is an illegal action of the actor.
     */
    public boolean postfire()
            throws ptolemy.actor.corba.util.CorbaIllegalActionException {
        org.omg.CORBA.portable.InputStream _in = null;

        try {
            org.omg.CORBA.portable.OutputStream _out = _request("postfire",
                    true);
            _in = _invoke(_out);

            boolean __result = _in.read_boolean();
            return __result;
        } catch (org.omg.CORBA.portable.ApplicationException _ex) {
            _in = _ex.getInputStream();

            String _id = _ex.getId();

            if (_id.equals("IDL:util/CorbaIllegalActionException:1.0")) {
                throw ptolemy.actor.corba.util.CorbaIllegalActionExceptionHelper
                        .read(_in);
            } else {
                throw new org.omg.CORBA.MARSHAL(_id);
            }
        } catch (org.omg.CORBA.portable.RemarshalException _rm) {
            return postfire();
        } finally {
            _releaseReply(_in);
        }
    } // postfire

    /* Mirror the prefire() method of Ptolemy II
     * executable interface.
     * @exception CorbaIllegalActionException If the
     *   method is an illegal action of the actor.
     */
    public boolean prefire()
            throws ptolemy.actor.corba.util.CorbaIllegalActionException {
        org.omg.CORBA.portable.InputStream _in = null;

        try {
            org.omg.CORBA.portable.OutputStream _out = _request("prefire", true);
            _in = _invoke(_out);

            boolean __result = _in.read_boolean();
            return __result;
        } catch (org.omg.CORBA.portable.ApplicationException _ex) {
            _in = _ex.getInputStream();

            String _id = _ex.getId();

            if (_id.equals("IDL:util/CorbaIllegalActionException:1.0")) {
                throw ptolemy.actor.corba.util.CorbaIllegalActionExceptionHelper
                        .read(_in);
            } else {
                throw new org.omg.CORBA.MARSHAL(_id);
            }
        } catch (org.omg.CORBA.portable.RemarshalException _rm) {
            return prefire();
        } finally {
            _releaseReply(_in);
        }
    } // prefire

    /* Mirror the preinitialize() method of Ptolmey II
     * executable interface.
     * @exception CorbaIllegalActionException If the
     *   method is an illegal action of the actor.
     */
    public boolean preinitialize()
            throws ptolemy.actor.corba.util.CorbaIllegalActionException {
        org.omg.CORBA.portable.InputStream _in = null;

        try {
            org.omg.CORBA.portable.OutputStream _out = _request(
                    "preinitialize", true);
            _in = _invoke(_out);

            boolean __result = _in.read_boolean();
            return __result;
        } catch (org.omg.CORBA.portable.ApplicationException _ex) {
            _in = _ex.getInputStream();

            String _id = _ex.getId();

            if (_id.equals("IDL:util/CorbaIllegalActionException:1.0")) {
                throw ptolemy.actor.corba.util.CorbaIllegalActionExceptionHelper
                        .read(_in);
            } else {
                throw new org.omg.CORBA.MARSHAL(_id);
            }
        } catch (org.omg.CORBA.portable.RemarshalException _rm) {
            return preinitialize();
        } finally {
            _releaseReply(_in);
        }
    } // preinitialize

    /* Set the value of the specified parameter.
     * @param paramName The parameter name.
     * @param paramValue The value to be set.
     * @exception CorbaIllegalActionException If the set
     *  value opertaion is not supported by the parameter.
     * @exception CorbaUnknownParamException If the
     *  parameter name is not known by the actor.
     * @exception CorbaIllegalValueException If the value
     *  is invalid for this parameter.
     */
    public void setParameter(String paramName, String paramValue)
            throws ptolemy.actor.corba.util.CorbaIllegalActionException,
            ptolemy.actor.corba.util.CorbaUnknownParamException,
            ptolemy.actor.corba.util.CorbaIllegalValueException {
        org.omg.CORBA.portable.InputStream _in = null;

        try {
            org.omg.CORBA.portable.OutputStream _out = _request("setParameter",
                    true);
            _out.write_string(paramName);
            _out.write_string(paramValue);
            _in = _invoke(_out);
        } catch (org.omg.CORBA.portable.ApplicationException _ex) {
            _in = _ex.getInputStream();

            String _id = _ex.getId();

            if (_id.equals("IDL:util/CorbaIllegalActionException:1.0")) {
                throw ptolemy.actor.corba.util.CorbaIllegalActionExceptionHelper
                        .read(_in);
            } else if (_id.equals("IDL:util/CorbaUnknownParamException:1.0")) {
                throw ptolemy.actor.corba.util.CorbaUnknownParamExceptionHelper
                        .read(_in);
            } else if (_id.equals("IDL:util/CorbaIllegalValueException:1.0")) {
                throw ptolemy.actor.corba.util.CorbaIllegalValueExceptionHelper
                        .read(_in);
            } else {
                throw new org.omg.CORBA.MARSHAL(_id);
            }
        } catch (org.omg.CORBA.portable.RemarshalException _rm) {
            setParameter(paramName, paramValue);
        } finally {
            _releaseReply(_in);
        }
    } // setParameter

    /*  Mirror the stopFire() method of Ptolemy II
     * executable interface.
     * @exception CorbaIllegalActionException If the
     *   method is an illegal action of the actor.
     */
    public void stopFire()
            throws ptolemy.actor.corba.util.CorbaIllegalActionException {
        org.omg.CORBA.portable.InputStream _in = null;

        try {
            org.omg.CORBA.portable.OutputStream _out = _request("stopFire",
                    true);
            _in = _invoke(_out);
        } catch (org.omg.CORBA.portable.ApplicationException _ex) {
            _in = _ex.getInputStream();

            String _id = _ex.getId();

            if (_id.equals("IDL:util/CorbaIllegalActionException:1.0")) {
                throw ptolemy.actor.corba.util.CorbaIllegalActionExceptionHelper
                        .read(_in);
            } else {
                throw new org.omg.CORBA.MARSHAL(_id);
            }
        } catch (org.omg.CORBA.portable.RemarshalException _rm) {
            stopFire();
        } finally {
            _releaseReply(_in);
        }
    } // stopFire

    /* Mirror the terminate() method of Ptolemy II
     * executable interface.
     * @exception CorbaIllegalActionException If the
     *   method is an illegal action of the actor.
     */
    public void terminate()
            throws ptolemy.actor.corba.util.CorbaIllegalActionException {
        org.omg.CORBA.portable.InputStream _in = null;

        try {
            org.omg.CORBA.portable.OutputStream _out = _request("terminate",
                    true);
            _in = _invoke(_out);
        } catch (org.omg.CORBA.portable.ApplicationException _ex) {
            _in = _ex.getInputStream();

            String _id = _ex.getId();

            if (_id.equals("IDL:util/CorbaIllegalActionException:1.0")) {
                throw ptolemy.actor.corba.util.CorbaIllegalActionExceptionHelper
                        .read(_in);
            } else {
                throw new org.omg.CORBA.MARSHAL(_id);
            }
        } catch (org.omg.CORBA.portable.RemarshalException _rm) {
            terminate();
        } finally {
            _releaseReply(_in);
        }
    } // terminate

    /* Transfer the input data to the specified port.
     * @param portName The port name.
     * @param portIndex The channel number within the port.
     * @param tokenValue The string for the value of the
     *        data token.
     * @exception CorbaIllegalActionException If the action is
     *  illegal.
     * @exception CorbaUnknownPortException If the port is unknown.
     * @exception CorbaIndexOutofBoundException If the index
     *  number is out of the width of the port.
     * @exception CorbaIllegalValueException If the value is not
     *  valid, e.g. the string cannot be converted to a value.
     */
    public void transferInput(String portName, short portIndex,
            String tokenValue)
            throws ptolemy.actor.corba.util.CorbaIllegalActionException,
            ptolemy.actor.corba.util.CorbaUnknownPortException,
            ptolemy.actor.corba.util.CorbaIndexOutofBoundException,
            ptolemy.actor.corba.util.CorbaIllegalValueException {
        org.omg.CORBA.portable.InputStream _in = null;

        try {
            org.omg.CORBA.portable.OutputStream _out = _request(
                    "transferInput", true);
            _out.write_string(portName);
            _out.write_short(portIndex);
            _out.write_string(tokenValue);
            _in = _invoke(_out);
        } catch (org.omg.CORBA.portable.ApplicationException _ex) {
            _in = _ex.getInputStream();

            String _id = _ex.getId();

            if (_id.equals("IDL:util/CorbaIllegalActionException:1.0")) {
                throw ptolemy.actor.corba.util.CorbaIllegalActionExceptionHelper
                        .read(_in);
            } else if (_id.equals("IDL:util/CorbaUnknownPortException:1.0")) {
                throw ptolemy.actor.corba.util.CorbaUnknownPortExceptionHelper
                        .read(_in);
            } else if (_id.equals("IDL:util/CorbaIndexOutofBoundException:1.0")) {
                throw ptolemy.actor.corba.util.CorbaIndexOutofBoundExceptionHelper
                        .read(_in);
            } else if (_id.equals("IDL:util/CorbaIllegalValueException:1.0")) {
                throw ptolemy.actor.corba.util.CorbaIllegalValueExceptionHelper
                        .read(_in);
            } else {
                throw new org.omg.CORBA.MARSHAL(_id);
            }
        } catch (org.omg.CORBA.portable.RemarshalException _rm) {
            transferInput(portName, portIndex, tokenValue);
        } finally {
            _releaseReply(_in);
        }
    } // transferInput

    /* Transfer the output from an output port.
     * @param portName The port name
     * @param portIndex The channel index within the port.
     * @exception CorbaIllegalActionException If the operation
     *  is illegal, e.g. the port is not an output port.
     * @exception CorbaUnknownPortException If the port name
     *  is unknown.
     * @exception CorbaIndexOutofBoundException If the index
     *  number is out of the width of the port.
     */
    public String transferOutput(String portName, short portIndex)
            throws ptolemy.actor.corba.util.CorbaIllegalActionException,
            ptolemy.actor.corba.util.CorbaUnknownPortException,
            ptolemy.actor.corba.util.CorbaIndexOutofBoundException {
        org.omg.CORBA.portable.InputStream _in = null;

        try {
            org.omg.CORBA.portable.OutputStream _out = _request(
                    "transferOutput", true);
            _out.write_string(portName);
            _out.write_short(portIndex);
            _in = _invoke(_out);

            String __result = _in.read_string();
            return __result;
        } catch (org.omg.CORBA.portable.ApplicationException _ex) {
            _in = _ex.getInputStream();

            String _id = _ex.getId();

            if (_id.equals("IDL:util/CorbaIllegalActionException:1.0")) {
                throw ptolemy.actor.corba.util.CorbaIllegalActionExceptionHelper
                        .read(_in);
            } else if (_id.equals("IDL:util/CorbaUnknownPortException:1.0")) {
                throw ptolemy.actor.corba.util.CorbaUnknownPortExceptionHelper
                        .read(_in);
            } else if (_id.equals("IDL:util/CorbaIndexOutofBoundException:1.0")) {
                throw ptolemy.actor.corba.util.CorbaIndexOutofBoundExceptionHelper
                        .read(_in);
            } else {
                throw new org.omg.CORBA.MARSHAL(_id);
            }
        } catch (org.omg.CORBA.portable.RemarshalException _rm) {
            return transferOutput(portName, portIndex);
        } finally {
            _releaseReply(_in);
        }
    } // transferOutput

    /* Mirror the wrapup() method of Ptolemy II
     * executable interface.
     * @exception CorbaIllegalActionException If the
     *   method is an illegal action of the actor.
     */
    public void wrapup()
            throws ptolemy.actor.corba.util.CorbaIllegalActionException {
        org.omg.CORBA.portable.InputStream _in = null;

        try {
            org.omg.CORBA.portable.OutputStream _out = _request("wrapup", true);
            _in = _invoke(_out);
        } catch (org.omg.CORBA.portable.ApplicationException _ex) {
            _in = _ex.getInputStream();

            String _id = _ex.getId();

            if (_id.equals("IDL:util/CorbaIllegalActionException:1.0")) {
                throw ptolemy.actor.corba.util.CorbaIllegalActionExceptionHelper
                        .read(_in);
            } else {
                throw new org.omg.CORBA.MARSHAL(_id);
            }
        } catch (org.omg.CORBA.portable.RemarshalException _rm) {
            wrapup();
        } finally {
            _releaseReply(_in);
        }
    } // wrapup

    // Type-specific CORBA::Object operations
    private static String[] __ids = { "IDL:util/CorbaActor:1.0" };

    public String[] _ids() {
        return __ids.clone();
    }

    private void readObject(java.io.ObjectInputStream s) {
        try {
            String str = s.readUTF();
            org.omg.CORBA.Object obj = org.omg.CORBA.ORB.init()
                    .string_to_object(str);
            org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl) obj)
                    ._get_delegate();
            _set_delegate(delegate);
        } catch (java.io.IOException e) {
        }
    }

    private void writeObject(java.io.ObjectOutputStream s) {
        try {
            String str = org.omg.CORBA.ORB.init().object_to_string(this);
            s.writeUTF(str);
        } catch (java.io.IOException e) {
        }
    }
} // class _CorbaActorStub
