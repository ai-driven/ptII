/* A hierarchical library of components specified in MoML.

   Copyright (c) 1998-2002 The Regents of the University of California.
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

   @ProposedRating Red (ellen_zh@eecs.berkeley.edu)
   @AcceptedRating Red (cxheecs.berkeley.edu)
 */

package ptolemy.moml.jxta;

import ptolemy.actor.Director;
import ptolemy.actor.TypedCompositeActor;
import ptolemy.actor.TypedAtomicActor;
import ptolemy.actor.CompositeActor;
import ptolemy.kernel.ComponentEntity;
import ptolemy.kernel.CompositeEntity;
import ptolemy.kernel.util.*;
import ptolemy.data.StringToken;
import ptolemy.data.expr.Parameter;
import ptolemy.data.type.BaseType;
import ptolemy.gui.MessageHandler;
import ptolemy.moml.EntityLibrary;
import ptolemy.moml.MoMLChangeRequest;
import ptolemy.moml.MoMLParser;
import ptolemy.moml.filter.BackwardCompatibility;
import ptolemy.moml.filter.RemoveGraphicalClasses;

import java.io.*;
import java.io.Writer;
import java.net.URL;
import java.util.*;
import java.lang.reflect.Constructor;

import net.jxta.discovery.DiscoveryListener;
import net.jxta.discovery.DiscoveryService;
import net.jxta.discovery.DiscoveryEvent;
import net.jxta.peergroup.PeerGroup;
import net.jxta.peergroup.PeerGroupFactory;
import net.jxta.exception.PeerGroupException;
import net.jxta.exception.DiscardQueryException;
import net.jxta.exception.ResendQueryException;
import net.jxta.exception.NoResponseException;
import net.jxta.protocol.*;
import net.jxta.document.AdvertisementFactory;
import net.jxta.document.StructuredDocument;
import net.jxta.document.MimeMediaType;
import net.jxta.document.Advertisement;
import net.jxta.credential.AuthenticationCredential;
import net.jxta.credential.Credential;
import net.jxta.membership.MembershipService;
import net.jxta.membership.Authenticator;
import net.jxta.impl.protocol.ResolverQuery;
import net.jxta.impl.protocol.ResolverResponse;
import net.jxta.resolver.ResolverService;
import net.jxta.resolver.QueryHandler;
import net.jxta.pipe.*;
import net.jxta.endpoint.Message;
import net.jxta.endpoint.MessageElement;
import net.jxta.endpoint.MessageElementEnumeration;
import org.apache.log4j.PropertyConfigurator;
import com.microstar.xml.XmlException;

//////////////////////////////////////////////////////////////////////////
//// JXTALibrary
/**
   This class provides a hierarchical library of components discovered from
   remote peers.

   <p>It starts as a JXTA peer. After started, it notisfies other peers via
   remote publish its advertisement, and it discovers other peers through
   getRemoteAdvertisement().

   <p>The DiscoveryListener listens to the discorverd events. One can implements
   the discoveryEvent method to do what he/she want. In this class, it sends
   out a query message to peers that have been discovered.

   <p>The QueryHandler deals with query message and response message sent by
   the resolverService. One can implements the processQuery method to deal
   with query message, and implements the processResponse method to deal
   with response message. In this class, A query message asks remote peer
   for actors, and when a peer gets such a query message, it will send a
   response message to answer what it has.

   <p>The pipeService can be used to exchange messages between peers. We can wrap
   a class file into a message to send it to a peer which wants this class.
   (FIXME: secure problems need to be considered at some point!) In this class,
   the processResponse method will creat an inputPipe for receiving actor
   classes, and send an inputPipe advertisement to the peer that has the actor.
   The current JXTA java binding implementation dosen't support publish an
   advertisement to a specific peer, however we probably wouldn't like to
   publish an inputPipe to the whole group. So in this class, we use the
   resolverServise to wrap the pipeAdvertisement in a query message to send
   it to the speicific peer.

   <p>The pipeMsgListener listens to input pipe messages. One can
   implements the pipeMsgEvent method to deal with the received
   message. In this class, we use the jave classLoader to define a class
   from the bytes received, and create a new instance of this class. Then
   a moml change request is issued.

   <p>A file: discoverdActors.xml is used to record discoved
   classes. These classes will be loaded when we start vergil next
   time. (FIXME: we need to check the version of the class file we cached
   to decide whether we need to get the file again when is discovered!)

   NOTE: this class is still very preliminary......

   @author Yang Zhao, Xiaojun Liu
   @version $Id$
   @since Ptolemy II 2.1
 */
public class JXTALibrary
    extends EntityLibrary implements ChangeListener, QueryHandler, DiscoveryListener, PipeMsgListener{

    /** Construct a library with the given container and name.
     *  @param container The container.
     *  @param name The name of this library.
     *  @exception IllegalActionException If the entity cannot be contained
     *   by the proposed container.
     *  @exception NameDuplicationException If the container already has an
     *   actor with this name.
     */
    public JXTALibrary(CompositeEntity container, String name)
            throws NameDuplicationException, IllegalActionException  {
        super(container, name);

        System.out.println("enter property configuration");
        //load the property file
        _loadProperty();
        // instantiate the default net peer group
        _startJxta();
        // create the peer group adv for actor exchange
        _createPeerGroup();

        // join the peer group for actor exchange
        _joinPeerGroup();

        try {
            MoMLParser parser = new MoMLParser();
            parser.setMoMLFilters(BackwardCompatibility.allFilters());
            File file = new File (_configDir + "/" + _DISCOVERED_ACTORS);
            if (file.exists()) {
                _cachedLib = (CompositeEntity) parser.parse(null,
                        file.toURL());
                _cachedLib.addChangeListener(this);
                System.out.println("create a compositeActor entity from discoveredActors.xml");
            }
            else {
                _cachedLib = null;
                System.out.println("Warning: can't find discoveredActors.xml, please create this file first.");
            }
        }
        catch ( java.net.MalformedURLException e ) {
        }
        catch ( java.lang.Exception e ) {
        }

        //get the discovery service, publish peer advertisement, and get other's advertisement.
        //Note: the current jxta implementation doesn't support remote publish of peer Adv, but
        //we can publish a group adv or other adv, and the peer Adv is automatically attached.
        _discoveryService = _group.getDiscoveryService();
        _discoveryService.addDiscoveryListener(this);
        _discoveryService.remotePublish(_groupAdv, DiscoveryService.GROUP);
        _discoveryService.getRemoteAdvertisements(null, DiscoveryService.PEER, null, null, 5);

        System.out.println("login to Ptolemy actor exchange group, and wait for remote response......\n");

        _resolverService = _group.getResolverService();
        // register this as a query handler
        _resolverService.registerHandler(_ACTOR_QUERY_HANDLER_NAME, this);

        _pipeSvc = _group.getPipeService();
        _loadPipeAdvertisement();
        _creatPipeAdvMessage();

        // construct the actor query message
        _createActorQuery();

        // construct the actor query response message
        _createActorResponse();
    }

    /**
     * @see net.jxta.resolver.QueryHandler#processQuery(ResolverQueryMsg)
     */
    public ResolverResponseMsg processQuery(ResolverQueryMsg query)
            throws
            NoResponseException,
            ResendQueryException,
            DiscardQueryException,
            IOException {
        String qry = query.getQuery();
        if(qry.startsWith("<ActorQuery>")) {

            if (_actorQueryResponse == null) {
                throw new DiscardQueryException();
            }
            System.out.println("Send query response...");
            _actorQueryResponse.setQueryId(query.getQueryId());
            return _actorQueryResponse;
        }
        else if (qry.startsWith("<PtolemyInputPipe>") ){
            int len = qry.length();
            String pipeStr = qry.substring(18, len-19) ;
            //System.out.println("the pipe information: " + pipeStr + "\n");
            PipeAdvertisement newPipeAdv = getPipeAdvertisement(pipeStr);
            for (int i=0; i<3; i++) {
                System.out.println("Trying to bind to pipe...");
                try {
                    _outputPipe = _pipeSvc.createOutputPipe(newPipeAdv, 30000);
                    break;
                } catch (java.io.IOException e) {
                    // go try again;
                }
            }
            if (_outputPipe == null) {
                System.out.println("Error resolving pipe endpoint");
                System.exit(1);
            }
            _sendMsg();
            return null;
        }
        else{
            return null;
        }
    }

    public void processResponse(ResolverResponseMsg response) {
        String rp = response.getResponse();
        System.out.println("Got response from remote peers. \n");

        //FIXME:check if we want the actor.
        try{
            if (rp.startsWith("<peerID>")) {
                int index = rp.lastIndexOf("</peerID>");
                String peerID = rp.substring(8, index) ;
                System.out.println("peerID is :" + peerID + " .\n");
                _resolverService.sendQuery(peerID, _pipeAdvMessage);
            }
            _pipeSvc.createInputPipe(_ptPipeAdv, this);
            System.out.println("create a input pipe for message.");
        }
        catch (java.io.IOException e) {
            // bogus peer, skip this message alltogether.
            System.out.println("Warning: cannot parse remote peer's advertisement.\n"
                    + e.getMessage());
            return;
        }
    }



    public synchronized void pipeMsgEvent(PipeMsgEvent event)
    {
        // Extract the message.
        Message message = event.getMessage();
        MessageElementEnumeration msgEnum = message.getElements();
        MessageElement elm = null;
        JXTAClassLoader classLoader = new JXTAClassLoader();
        while (msgEnum.hasMoreElements()) {
            elm = (MessageElement)msgEnum.nextElement();
            String tag = elm.getName();
            if ( tag.equals(TAG) ) {
                System.out.println("get an actor class from remote peer ");
                try {
                    InputStream is = elm.getStream();
                    int len = elm.getLength() ;
                    byte[] buff = new byte[len];
                    is.read(buff);

                    Class cls = classLoader.myDefineClass(null, buff, 0, len) ;
                    String clsName = cls.getName();
                    if (cls != null) {
                        System.out.println("created class object " + clsName);
                    }
                    classLoader.myResolveClass(cls);
                    Object[] arguments = new Object[2];
                    int l = clsName.lastIndexOf(".");
                    System.out.println("the name of the new instance is : " + clsName.substring(l+1));

                    arguments[0] = this;
                    arguments[1] = "p2p_" + clsName.substring(l+1);
                    try {
                        NamedObj newEntity = _createInstance(cls, arguments);
                        System.out.println("create an instance of " + clsName + ".\n");
                        // ((ComponentEntity) newEntity).setContainer(this);
                        String chg = newEntity.exportMoML();
                        System.out.println("the moml description of the new object is: \n" + chg + "\n");
                        ChangeRequest request = new MoMLChangeRequest(
                                this,            // originator
                                this,            // context
                                chg,             // MoML code
                                null);           // base
                        requestChange(request);

                        ChangeRequest request2 = new MoMLChangeRequest(
                                this,            // originator
                                _cachedLib,            // context
                                chg,             // MoML code
                                null);           // base

                        _cachedLib.requestChange(request2);
                        System.out.println("discoveredActors.xml has been changed.\n");
                        StringWriter buffer = new StringWriter();
                        _cachedLib.exportMoML(buffer);
                        FileOutputStream file = new FileOutputStream(_Dir + "/" + _DISCOVERED_ACTORS);
                        PrintStream out = new PrintStream( file);
                        out.println(buffer);
                        out.flush();
                        file.close();
                    }
                    catch (Exception e) {
                    }

                }
                catch (java.lang.NullPointerException e){
                    System.out.println("Warning: cannot creat the file.\n"
                            + e.getMessage());
                }
                catch (java.io.IOException e) {
                    System.out.println("Warning: cannot creat/open the file.\n"
                            + e.getMessage());
                }
            }
        }
    }

    public void discoveryEvent(DiscoveryEvent event) {
        System.out.println("Process discovery event...");

        DiscoveryResponseMsg response = event.getResponse();

        // Get the responding peer's advertisement as a string
        String responderAdvString = response.getPeerAdv();
        //System.out.println("peer Advertisement: " + responderAdvString);
        try {
            // create a peer advertisement
            InputStream is = new ByteArrayInputStream(responderAdvString.getBytes());
            PeerAdvertisement responderAdv = (PeerAdvertisement)
                AdvertisementFactory.newAdvertisement(
                        XML_MIME_TYPE, is);
            System.out.println(" [  Got a Discovery Response ["
                    + response.getResponseCount() + " elements] from peer: "
                    + responderAdv.getName() + " ]");
            String peerID = (responderAdv.getPeerID()).toString() ;
            _resolverService.sendQuery(peerID, _actorQueryMessage);
        } catch (java.io.IOException e) {
            // bogus peer, skip this message alltogether.
            System.out.println("Warning: cannot parse remote peer's advertisement.\n"
                    + e.getMessage());
            return;
        }
    }

    public void changeExecuted(ChangeRequest change) {
    }


    public void changeFailed(ChangeRequest change, Exception exception) {
   	throw new RuntimeException(exception.toString());
    }

    public PipeAdvertisement getPipeAdvertisement(String str) {
        PipeAdvertisement pipeAdv = null;
        try {
            pipeAdv = (PipeAdvertisement)
                AdvertisementFactory.newAdvertisement(
                        XML_MIME_TYPE,
                        new ByteArrayInputStream(str.getBytes()));

        } catch (java.io.IOException ex) {
            System.out.println("failed to constructe Pipe advertisement from a string " + ex.getMessage());
        }
        return pipeAdv;
    }

    //public void populate() {
    // create list of cached actor discoveres
    // for each actor, create a change request with
    // "this" as context, queue and execute request
    // then GUI should reflect change.
    //}


    //*********************************************************************//
    //                              private method                        //


    private NamedObj _createInstance(Class newClass, Object[] arguments)
            throws Exception {
        Constructor[] constructors = newClass.getConstructors();
        System.out.println("get constructors.");
        for (int i = 0; i < constructors.length; i++) {
            Constructor constructor = constructors[i];
            Class[] parameterTypes = constructor.getParameterTypes();
            if (parameterTypes.length != arguments.length) continue;
            boolean match = true;
            for (int j = 0; j < parameterTypes.length; j++) {
                if (!(parameterTypes[j].isInstance(arguments[j]))) {
                    match = false;
                    break;
                }
            }
            if (match) {
                System.out.println("find a matched constructor.");
                NamedObj obj = (NamedObj)constructor.newInstance(arguments);
                System.out.println("construct a new actor instance.");
                return obj;
            }
        }
        System.out.println("can't construct a new actor instance.");
        StringBuffer argumentBuffer = new StringBuffer();
        throw new Exception("Cannot find a suitable constructor for"
                + newClass.getName());

    }


    private void _createActorQuery() {
        StringBuffer queryTextBuffer = new StringBuffer();
        queryTextBuffer = queryTextBuffer.append("<ActorQuery>\n");
        queryTextBuffer = queryTextBuffer.append(
                "What actors do you have?");
        queryTextBuffer = queryTextBuffer.append("\n</ActorQuery>\n");
        _actorQueryMessage = new ResolverQuery(_ACTOR_QUERY_HANDLER_NAME,
                null, null, queryTextBuffer.toString(), 0);
        _actorQueryMessage.setSrc(_group.getPeerID().toString());
    }

    private void _createActorResponse() {
        String actorListFileName = _properties.getProperty("actor.list");
        if (actorListFileName != null) {
            StringBuffer actorListText = new StringBuffer();
            try {
                BufferedReader fileReader =
                    new BufferedReader(new FileReader(actorListFileName));
                String newline = System.getProperty("line.separator");
                String peerID = (_group.getPeerID()).toString();
                actorListText = actorListText.append("<peerID>" + peerID + "</peerID>");
                actorListText = actorListText.append(newline);
                while (true) {
                    String line = fileReader.readLine();
                    if (line == null) break;
                    actorListText = actorListText.append(line);
                    actorListText = actorListText.append(newline);
                }
                _actorQueryResponse = new ResolverResponse(
                        _ACTOR_QUERY_HANDLER_NAME,
                        null, 0, actorListText.toString());
            } catch (IOException ex) {
                System.out.println("Warning: error reading actor list file.\n"
                        + ex.getMessage());
            }
        }
    }

    private boolean _createPeerGroup() {
        String groupAdvFileName = _properties.getProperty("GroupAdvFileName");
        if (groupAdvFileName == null) {
            System.out.println("Error: property undefined - GroupAdvFileName.\n");
            return false;
        }
        _groupAdv = null;
        try {
            _groupAdv = (PeerGroupAdvertisement)
                AdvertisementFactory.newAdvertisement(
                        XML_MIME_TYPE,
                        new FileInputStream(_configDir + "/" + groupAdvFileName));
        } catch (FileNotFoundException ex) {
            System.out.println("Error: cannot find group adv file.\n"
                    + ex.getMessage());
            return false;
        } catch (IOException ex) {
            System.out.println("Error: reading group adv file.\n"
                    + ex.getMessage());
            return false;
        }
        //System.out.println("peer groupAdv: " + groupAdvFileName);
        //System.out.println("success before instantiate peer group");
        // instantiate the peer group for actor exchange
        try {
            _group = _netPeerGroup.newGroup(_groupAdv);
        } catch (PeerGroupException ex) {
            System.out.println("Error: cannot instantiate peer group.\n"
                    + ex.getMessage());
            return false;
        }
        return true;
    }

    private void _creatPipeAdvMessage() {
        String pipeAdvFile = _properties.getProperty("ptPipe");
        if (pipeAdvFile == null) {
            System.out.println("Error: property undefined - PipeAdvFile.\n");
            return;
        }
        StringBuffer queryTextBuffer= new StringBuffer();
        try {
            BufferedReader fileReader =
                new BufferedReader(new FileReader(_configDir + "/" + pipeAdvFile));
            String newline = System.getProperty("line.separator");
            queryTextBuffer = queryTextBuffer.append("<PtolemyInputPipe>\n");
            //String peerID = (_group.getPeerID()).toString();
            //actorListText = actorListText.append("<peerID>" + peerID + "</peerID>");
            //actorListText = actorListText.append(newline);
            while (true) {
                String line = fileReader.readLine();
                if (line == null) break;
                queryTextBuffer = queryTextBuffer.append(line);
                queryTextBuffer = queryTextBuffer.append(newline);
            }
            queryTextBuffer = queryTextBuffer.append("</PtolemyInputPipe>\n");
        }
        catch (IOException ex) {
            System.out.println("Warning: error reading pipeAdv file.\n"
                    + ex.getMessage());
        }
        _pipeAdvMessage = new ResolverQuery(_ACTOR_QUERY_HANDLER_NAME,
                null, null, queryTextBuffer.toString(), 0);
        //System.out.println("the pipeAdv string is : " + queryTextBuffer.toString());
        _pipeAdvMessage.setSrc(_group.getPeerID().toString());
    }

    private boolean _joinPeerGroup() {
        StructuredDocument identityInfo = null;
        try {
            AuthenticationCredential authCred =
                new AuthenticationCredential(_group, null, identityInfo);
            MembershipService membershipService = _group.getMembershipService();
            _authenticator = membershipService.apply(authCred);
            if (_authenticator.isReadyForJoin()) {
                _credential = membershipService.join(_authenticator);
                System.out.println("Info: join group successful.");
                //_credential.getDocument(XML_MIME_TYPE).sendToStream(System.out);
            } else {
                System.out.println("Error: unable to join group.");
                return false;
            }
        } catch (Exception ex) {
            System.out.println("Error: failure in authentication.\n"
                    + ex.getMessage());
            return false;
        }
        return true;
    }

    private void _loadProperty() {
        _configDir = System.getProperty(_CONFIG_DIR);
        if (_configDir == null) {
            _configDir = System.getProperty("user.dir");
            System.setProperty(_CONFIG_DIR, _configDir);
        }
        System.out.println("### the directory is : " + _configDir + "\n");
        _properties = new Properties(System.getProperties());
        try {
            InputStream configProperties =
                new FileInputStream(_configDir + "/" + _CONFIG_FILE);
            _properties.load(configProperties);
        } catch (FileNotFoundException ex) {
            System.out.println("Warning: cannot find configuration properties file.\n"
                    + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("Warning: error reading configuration file.\n"
                    + ex.getMessage());
        }
    }

    private boolean _loadPipeAdvertisement() {
        String pipeAdvFile = _properties.getProperty("ptPipe");
        if (pipeAdvFile == null) {
            System.out.println("Error: property undefined - PipeAdvFile.\n");
            return false;
        }
        try {
            FileInputStream is = new FileInputStream(_configDir + "/" + pipeAdvFile);
            _ptPipeAdv = (PipeAdvertisement)
                AdvertisementFactory.newAdvertisement(
                        new MimeMediaType("text/xml"), is);
            is.close();
        } catch (java.io.IOException ex) {
            System.out.println("failed to read/parse pipe advertisement" + ex.getMessage());
        }
        return true;
    }

    private boolean _startJxta() {
        try {
            _netPeerGroup = PeerGroupFactory.newNetPeerGroup();
        }
        catch (PeerGroupException ex) {
            System.out.println("Error: cannot locate net peer group.\n"
                    + ex.getMessage());
            return false;
        }
        return true;
    }

    private void _sendMsg() {
        String ActorName = _properties.getProperty("sharedActor");
        InputStream actStream = null;
        if (ActorName == null) {
            System.out.println("Error: property undefined - actor.\n");
        }
        String ActorFile = _properties.getProperty("sharedClassFile");
        if (ActorFile == null) {
            System.out.println("Error: property undefined - file.\n");
        }
        try {
            actStream = new FileInputStream(_configDir + "/" + ActorFile);
        } catch (java.io.IOException ex) {
            System.out.println("failed to read/parse pipe advertisement" + ex.getMessage());
        }

        // create the pipe message
        Message msg = _pipeSvc.createMessage();
        //msg.setString(TAG, data);
        //byte[] buff = new byte[MAXBUFF];
        //int size = actStream.read(buff);
        //actStream.close();
        //if(size < MAXBUFF) {
        try {
            MessageElement msgelm = msg.newMessageElement(TAG, null, actStream) ;
            msg.addElement(msgelm);
        }
        catch (java.io.IOException e) {
            // go try again;
        }

        try {
            // send the message to the service pipe
            _outputPipe.send(msg);
            System.out.println(ActorName + "file sent out to remote peer.");
        } catch (java.io.IOException ex) {
            System.out.println("failed to send message to remote peer " + ex.getMessage());
        }
    }

    //********************************************************************//
    //                        private variables                           //

    private Properties _properties;
    private PeerGroup _group;
    private DiscoveryService _discoveryService;
    private ResolverService _resolverService;
    private Authenticator _authenticator;
    private Credential _credential;
    private Timer _peerDiscoveryTimer;
    private Timer _actorQueryTimer;
    private ResolverQueryMsg _actorQueryMessage;
    private ResolverResponseMsg _actorQueryResponse = null;
    private PeerGroup _netPeerGroup;
    private PeerGroupAdvertisement _groupAdv;
    private PipeService _pipeSvc;
    private PipeAdvertisement _ptPipeAdv;
    private String _pipeAdvString;
    private InputPipe _inputPipe;
    private OutputPipe _outputPipe;
    private ResolverQueryMsg _pipeAdvMessage;
    private String _actorListFileName;
    private String _ACTOR_QUERY_HANDLER_NAME = "ActorQueryHandler";
    private MimeMediaType XML_MIME_TYPE = new MimeMediaType("text/xml");

    private String _configDir;
    private static String _CONFIG_DIR = "pae.config.dir";
    //private final static String PTPIPE = "PtolemyActorExchangePipe"; // pipe name
    private final static String TAG = "sharedPtActor";
    //private String _Dir = "c:/Cygwin/home/ellen_zh/ptII/ptolemy/moml/eptolemy";
    private String _Dir = ".";
    //private String groupAdvFileName = "PtolemyActorExchangeGroup.adv";
    private String _CONFIG_FILE = "ePtolemy.properties";
    private String _DISCOVERED_ACTORS ="discoveredActors.xml";


    private CompositeEntity _cachedLib;

}
