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
package eu.diversify.disco.cloudml;

import java.util.List;
import java.util.Map;
import org.apache.commons.jxpath.JXPathContext;
import org.cloudml.core.Artefact;
import org.cloudml.core.ArtefactInstance;
import org.cloudml.core.Binding;
import org.cloudml.core.BindingInstance;
import org.cloudml.core.ClientPort;
import org.cloudml.core.ClientPortInstance;
import org.cloudml.core.DeploymentModel;
import org.cloudml.core.Node;
import org.cloudml.core.NodeInstance;
import org.cloudml.core.Provider;
import org.cloudml.core.ServerPort;
import org.cloudml.core.ServerPortInstance;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class MdmsModelCreator {
  private DeploymentModel dm = null;
  
  public DeploymentModel create() {
    DeploymentModel _deploymentModel = new DeploymentModel("mdms");
    this.dm = _deploymentModel;
    final Procedure1<DeploymentModel> _function = new Procedure1<DeploymentModel>() {
        public void apply(final DeploymentModel it) {
          Provider _provider = new Provider("provider", "../src/main/resources/credentials");
          final Provider prov = _provider;
          List<Provider> _providers = it.getProviders();
          _providers.add(prov);
          Map<String,Node> _nodeTypes = it.getNodeTypes();
          Node _node = new Node("EC2");
          final Procedure1<Node> _function = new Procedure1<Node>() {
              public void apply(final Node it) {
                it.setProvider(prov);
              }
            };
          Node _doubleArrow = ObjectExtensions.<Node>operator_doubleArrow(_node, _function);
          _nodeTypes.put("EC2", _doubleArrow);
          Map<String,Artefact> _artefactTypes = it.getArtefactTypes();
          Artefact _artefact = new Artefact("MDMS");
          final Procedure1<Artefact> _function_1 = new Procedure1<Artefact>() {
              public void apply(final Artefact it) {
                List<ServerPort> _provided = it.getProvided();
                ServerPort _serverPort = new ServerPort("content", it, true);
                _provided.add(_serverPort);
                List<ClientPort> _required = it.getRequired();
                ClientPort _clientPort = new ClientPort("db", it, true);
                _required.add(_clientPort);
                List<ClientPort> _required_1 = it.getRequired();
                ClientPort _clientPort_1 = new ClientPort("ringo", it, false);
                _required_1.add(_clientPort_1);
              }
            };
          Artefact _doubleArrow_1 = ObjectExtensions.<Artefact>operator_doubleArrow(_artefact, _function_1);
          _artefactTypes.put("MDMS", _doubleArrow_1);
          Map<String,Artefact> _artefactTypes_1 = it.getArtefactTypes();
          Artefact _artefact_1 = new Artefact("RingoJS");
          final Procedure1<Artefact> _function_2 = new Procedure1<Artefact>() {
              public void apply(final Artefact it) {
                List<ServerPort> _provided = it.getProvided();
                ServerPort _serverPort = new ServerPort("ringo", it, false);
                _provided.add(_serverPort);
                List<ClientPort> _required = it.getRequired();
                ClientPort _clientPort = new ClientPort("js", it, false);
                _required.add(_clientPort);
                List<ClientPort> _required_1 = it.getRequired();
                ClientPort _clientPort_1 = new ClientPort("jvm", it, false);
                _required_1.add(_clientPort_1);
              }
            };
          Artefact _doubleArrow_2 = ObjectExtensions.<Artefact>operator_doubleArrow(_artefact_1, _function_2);
          _artefactTypes_1.put("RingoJS", _doubleArrow_2);
          Map<String,Artefact> _artefactTypes_2 = it.getArtefactTypes();
          Artefact _artefact_2 = new Artefact("Rhino");
          final Procedure1<Artefact> _function_3 = new Procedure1<Artefact>() {
              public void apply(final Artefact it) {
                List<ServerPort> _provided = it.getProvided();
                ServerPort _serverPort = new ServerPort("js", it, false);
                _provided.add(_serverPort);
                List<ClientPort> _required = it.getRequired();
                ClientPort _clientPort = new ClientPort("jvm", it, false);
                _required.add(_clientPort);
              }
            };
          Artefact _doubleArrow_3 = ObjectExtensions.<Artefact>operator_doubleArrow(_artefact_2, _function_3);
          _artefactTypes_2.put("Rhino", _doubleArrow_3);
          Map<String,Artefact> _artefactTypes_3 = it.getArtefactTypes();
          Artefact _artefact_3 = new Artefact("OpenJDK");
          final Procedure1<Artefact> _function_4 = new Procedure1<Artefact>() {
              public void apply(final Artefact it) {
                List<ServerPort> _provided = it.getProvided();
                ServerPort _serverPort = new ServerPort("jvm", it, false);
                _provided.add(_serverPort);
              }
            };
          Artefact _doubleArrow_4 = ObjectExtensions.<Artefact>operator_doubleArrow(_artefact_3, _function_4);
          _artefactTypes_3.put("OpenJDK", _doubleArrow_4);
          Map<String,Artefact> _artefactTypes_4 = it.getArtefactTypes();
          Artefact _artefact_4 = new Artefact("MySQL");
          final Procedure1<Artefact> _function_5 = new Procedure1<Artefact>() {
              public void apply(final Artefact it) {
                List<ServerPort> _provided = it.getProvided();
                ServerPort _serverPort = new ServerPort("db", it, true);
                _provided.add(_serverPort);
              }
            };
          Artefact _doubleArrow_5 = ObjectExtensions.<Artefact>operator_doubleArrow(_artefact_4, _function_5);
          _artefactTypes_4.put("MySQL", _doubleArrow_5);
          Map<String,Artefact> _artefactTypes_5 = it.getArtefactTypes();
          Artefact _artefact_5 = new Artefact("Balancer");
          final Procedure1<Artefact> _function_6 = new Procedure1<Artefact>() {
              public void apply(final Artefact it) {
                List<ClientPort> _required = it.getRequired();
                ClientPort _clientPort = new ClientPort("content", it, true);
                _required.add(_clientPort);
              }
            };
          Artefact _doubleArrow_6 = ObjectExtensions.<Artefact>operator_doubleArrow(_artefact_5, _function_6);
          _artefactTypes_5.put("Balancer", _doubleArrow_6);
          Map<String,Binding> _bindingTypes = it.getBindingTypes();
          Binding _binding = new Binding();
          final Procedure1<Binding> _function_7 = new Procedure1<Binding>() {
              public void apply(final Binding it) {
                it.setName("Content");
                ClientPort _parseRoot = MdmsModelCreator.this.<ClientPort>parseRoot("/artefactTypes/Balancer/required[name=\'content\']");
                it.setClient(_parseRoot);
                ServerPort _parseRoot_1 = MdmsModelCreator.this.<ServerPort>parseRoot("/artefactTypes/MDMS/provided[name=\'content\']");
                it.setServer(_parseRoot_1);
              }
            };
          Binding _doubleArrow_7 = ObjectExtensions.<Binding>operator_doubleArrow(_binding, _function_7);
          _bindingTypes.put("Content", _doubleArrow_7);
          Map<String,Binding> _bindingTypes_1 = it.getBindingTypes();
          Binding _binding_1 = new Binding();
          final Procedure1<Binding> _function_8 = new Procedure1<Binding>() {
              public void apply(final Binding it) {
                it.setName("DB");
                ClientPort _parseRoot = MdmsModelCreator.this.<ClientPort>parseRoot("/artefactTypes/MDMS/required[name=\'db\']");
                it.setClient(_parseRoot);
                ServerPort _parseRoot_1 = MdmsModelCreator.this.<ServerPort>parseRoot("/artefactTypes/MySQL/provided[name=\'db\']");
                it.setServer(_parseRoot_1);
              }
            };
          Binding _doubleArrow_8 = ObjectExtensions.<Binding>operator_doubleArrow(_binding_1, _function_8);
          _bindingTypes_1.put("DB", _doubleArrow_8);
          Map<String,Binding> _bindingTypes_2 = it.getBindingTypes();
          Binding _binding_2 = new Binding();
          final Procedure1<Binding> _function_9 = new Procedure1<Binding>() {
              public void apply(final Binding it) {
                it.setName("Ringo");
                ClientPort _parseRoot = MdmsModelCreator.this.<ClientPort>parseRoot("/artefactTypes/MDMS/required[name=\'ringo\']");
                it.setClient(_parseRoot);
                ServerPort _parseRoot_1 = MdmsModelCreator.this.<ServerPort>parseRoot("/artefactTypes/RingoJS/provided[name=\'ringo\']");
                it.setServer(_parseRoot_1);
              }
            };
          Binding _doubleArrow_9 = ObjectExtensions.<Binding>operator_doubleArrow(_binding_2, _function_9);
          _bindingTypes_2.put("Ringo", _doubleArrow_9);
          Map<String,Binding> _bindingTypes_3 = it.getBindingTypes();
          Binding _binding_3 = new Binding();
          final Procedure1<Binding> _function_10 = new Procedure1<Binding>() {
              public void apply(final Binding it) {
                ClientPort _parseRoot = MdmsModelCreator.this.<ClientPort>parseRoot("/artefactTypes/RingoJS/required[name=\'js\']");
                it.setClient(_parseRoot);
                ServerPort _parseRoot_1 = MdmsModelCreator.this.<ServerPort>parseRoot("/artefactTypes/Rhino/provided[name=\'js\']");
                it.setServer(_parseRoot_1);
              }
            };
          Binding _doubleArrow_10 = ObjectExtensions.<Binding>operator_doubleArrow(_binding_3, _function_10);
          _bindingTypes_3.put("JS", _doubleArrow_10);
          Map<String,Binding> _bindingTypes_4 = it.getBindingTypes();
          Binding _binding_4 = new Binding();
          final Procedure1<Binding> _function_11 = new Procedure1<Binding>() {
              public void apply(final Binding it) {
                ClientPort _parseRoot = MdmsModelCreator.this.<ClientPort>parseRoot("/artefactTypes/RingoJS/required[name=\'jvm\']");
                it.setClient(_parseRoot);
                ServerPort _parseRoot_1 = MdmsModelCreator.this.<ServerPort>parseRoot("/artefactTypes/OpenJDK/provided[name=\'jvm\']");
                it.setServer(_parseRoot_1);
              }
            };
          Binding _doubleArrow_11 = ObjectExtensions.<Binding>operator_doubleArrow(_binding_4, _function_11);
          _bindingTypes_4.put("Jvm", _doubleArrow_11);
          Node _parseRoot = MdmsModelCreator.this.<Node>parseRoot("/nodeTypes/EC2");
          NodeInstance _instanciates = _parseRoot.instanciates("ec2_bal");
          final Procedure1<NodeInstance> _function_12 = new Procedure1<NodeInstance>() {
              public void apply(final NodeInstance it) {
              }
            };
          final NodeInstance ec2_bal = ObjectExtensions.<NodeInstance>operator_doubleArrow(_instanciates, _function_12);
          List<NodeInstance> _nodeInstances = it.getNodeInstances();
          _nodeInstances.add(ec2_bal);
          Node _parseRoot_1 = MdmsModelCreator.this.<Node>parseRoot("/nodeTypes/EC2");
          NodeInstance _instanciates_1 = _parseRoot_1.instanciates("ec2_mdms");
          final Procedure1<NodeInstance> _function_13 = new Procedure1<NodeInstance>() {
              public void apply(final NodeInstance it) {
              }
            };
          final NodeInstance ec2_mdms = ObjectExtensions.<NodeInstance>operator_doubleArrow(_instanciates_1, _function_13);
          List<NodeInstance> _nodeInstances_1 = it.getNodeInstances();
          _nodeInstances_1.add(ec2_mdms);
          Node _parseRoot_2 = MdmsModelCreator.this.<Node>parseRoot("/nodeTypes/EC2");
          NodeInstance _instanciates_2 = _parseRoot_2.instanciates("ec2_db");
          final Procedure1<NodeInstance> _function_14 = new Procedure1<NodeInstance>() {
              public void apply(final NodeInstance it) {
              }
            };
          final NodeInstance ec2_db = ObjectExtensions.<NodeInstance>operator_doubleArrow(_instanciates_2, _function_14);
          List<NodeInstance> _nodeInstances_2 = it.getNodeInstances();
          _nodeInstances_2.add(ec2_db);
          Map<String,Artefact> _artefactTypes_6 = it.getArtefactTypes();
          Artefact _get = _artefactTypes_6.get("Balancer");
          ArtefactInstance _instanciates_3 = _get.instanciates("balancer");
          final Procedure1<ArtefactInstance> _function_15 = new Procedure1<ArtefactInstance>() {
              public void apply(final ArtefactInstance it) {
                it.setDestination(ec2_bal);
                MdmsModelCreator.this.initFullPortInstance(it);
              }
            };
          final ArtefactInstance balancer = ObjectExtensions.<ArtefactInstance>operator_doubleArrow(_instanciates_3, _function_15);
          List<ArtefactInstance> _artefactInstances = it.getArtefactInstances();
          _artefactInstances.add(balancer);
          Map<String,Artefact> _artefactTypes_7 = it.getArtefactTypes();
          Artefact _get_1 = _artefactTypes_7.get("MDMS");
          ArtefactInstance _instanciates_4 = _get_1.instanciates("mdms");
          final Procedure1<ArtefactInstance> _function_16 = new Procedure1<ArtefactInstance>() {
              public void apply(final ArtefactInstance it) {
                it.setDestination(ec2_mdms);
                MdmsModelCreator.this.initFullPortInstance(it);
              }
            };
          final ArtefactInstance mdms = ObjectExtensions.<ArtefactInstance>operator_doubleArrow(_instanciates_4, _function_16);
          List<ArtefactInstance> _artefactInstances_1 = it.getArtefactInstances();
          _artefactInstances_1.add(mdms);
          Map<String,Artefact> _artefactTypes_8 = it.getArtefactTypes();
          Artefact _get_2 = _artefactTypes_8.get("RingoJS");
          ArtefactInstance _instanciates_5 = _get_2.instanciates("ringoJS");
          final Procedure1<ArtefactInstance> _function_17 = new Procedure1<ArtefactInstance>() {
              public void apply(final ArtefactInstance it) {
                it.setDestination(ec2_mdms);
                MdmsModelCreator.this.initFullPortInstance(it);
              }
            };
          final ArtefactInstance ringoJS = ObjectExtensions.<ArtefactInstance>operator_doubleArrow(_instanciates_5, _function_17);
          List<ArtefactInstance> _artefactInstances_2 = it.getArtefactInstances();
          _artefactInstances_2.add(ringoJS);
          Map<String,Artefact> _artefactTypes_9 = it.getArtefactTypes();
          Artefact _get_3 = _artefactTypes_9.get("Rhino");
          ArtefactInstance _instanciates_6 = _get_3.instanciates("rhino");
          final Procedure1<ArtefactInstance> _function_18 = new Procedure1<ArtefactInstance>() {
              public void apply(final ArtefactInstance it) {
                it.setDestination(ec2_mdms);
                MdmsModelCreator.this.initFullPortInstance(it);
              }
            };
          final ArtefactInstance rhino = ObjectExtensions.<ArtefactInstance>operator_doubleArrow(_instanciates_6, _function_18);
          List<ArtefactInstance> _artefactInstances_3 = it.getArtefactInstances();
          _artefactInstances_3.add(rhino);
          Map<String,Artefact> _artefactTypes_10 = it.getArtefactTypes();
          Artefact _get_4 = _artefactTypes_10.get("OpenJDK");
          ArtefactInstance _instanciates_7 = _get_4.instanciates("openJDK");
          final Procedure1<ArtefactInstance> _function_19 = new Procedure1<ArtefactInstance>() {
              public void apply(final ArtefactInstance it) {
                it.setDestination(ec2_mdms);
                MdmsModelCreator.this.initFullPortInstance(it);
              }
            };
          final ArtefactInstance openJDK = ObjectExtensions.<ArtefactInstance>operator_doubleArrow(_instanciates_7, _function_19);
          List<ArtefactInstance> _artefactInstances_4 = it.getArtefactInstances();
          _artefactInstances_4.add(openJDK);
          Map<String,Artefact> _artefactTypes_11 = it.getArtefactTypes();
          Artefact _get_5 = _artefactTypes_11.get("MySQL");
          ArtefactInstance _instanciates_8 = _get_5.instanciates("mySQL");
          final Procedure1<ArtefactInstance> _function_20 = new Procedure1<ArtefactInstance>() {
              public void apply(final ArtefactInstance it) {
                it.setDestination(ec2_db);
                MdmsModelCreator.this.initFullPortInstance(it);
              }
            };
          final ArtefactInstance mySQL = ObjectExtensions.<ArtefactInstance>operator_doubleArrow(_instanciates_8, _function_20);
          List<ArtefactInstance> _artefactInstances_5 = it.getArtefactInstances();
          _artefactInstances_5.add(mySQL);
          List<BindingInstance> _bindingInstances = it.getBindingInstances();
          Map<String,Binding> _bindingTypes_5 = MdmsModelCreator.this.dm.getBindingTypes();
          Binding _get_6 = _bindingTypes_5.get("Content");
          BindingInstance _instanciates_9 = _get_6.instanciates("content");
          final Procedure1<BindingInstance> _function_21 = new Procedure1<BindingInstance>() {
              public void apply(final BindingInstance it) {
                ClientPortInstance _parseRoot = MdmsModelCreator.this.<ClientPortInstance>parseRoot("/artefactInstances[name=\'balancer\']/required[name=\'content\']");
                it.setClient(_parseRoot);
                ServerPortInstance _parseRoot_1 = MdmsModelCreator.this.<ServerPortInstance>parseRoot("/artefactInstances[name=\'mdms\']/provided[name=\'content\']");
                it.setServer(_parseRoot_1);
              }
            };
          BindingInstance _doubleArrow_12 = ObjectExtensions.<BindingInstance>operator_doubleArrow(_instanciates_9, _function_21);
          _bindingInstances.add(_doubleArrow_12);
          List<BindingInstance> _bindingInstances_1 = it.getBindingInstances();
          Map<String,Binding> _bindingTypes_6 = MdmsModelCreator.this.dm.getBindingTypes();
          Binding _get_7 = _bindingTypes_6.get("DB");
          BindingInstance _instanciates_10 = _get_7.instanciates("db");
          final Procedure1<BindingInstance> _function_22 = new Procedure1<BindingInstance>() {
              public void apply(final BindingInstance it) {
                ClientPortInstance _parseRoot = MdmsModelCreator.this.<ClientPortInstance>parseRoot("/artefactInstances[name=\'mdms\']/required[name=\'db\']");
                it.setClient(_parseRoot);
                ServerPortInstance _parseRoot_1 = MdmsModelCreator.this.<ServerPortInstance>parseRoot("/artefactInstances[name=\'mySQL\']/provided[name=\'db\']");
                it.setServer(_parseRoot_1);
              }
            };
          BindingInstance _doubleArrow_13 = ObjectExtensions.<BindingInstance>operator_doubleArrow(_instanciates_10, _function_22);
          _bindingInstances_1.add(_doubleArrow_13);
          List<BindingInstance> _bindingInstances_2 = it.getBindingInstances();
          Map<String,Binding> _bindingTypes_7 = MdmsModelCreator.this.dm.getBindingTypes();
          Binding _get_8 = _bindingTypes_7.get("Ringo");
          BindingInstance _instanciates_11 = _get_8.instanciates("ringo");
          final Procedure1<BindingInstance> _function_23 = new Procedure1<BindingInstance>() {
              public void apply(final BindingInstance it) {
                ClientPortInstance _parseRoot = MdmsModelCreator.this.<ClientPortInstance>parseRoot("/artefactInstances[name=\'mdms\']/required[name=\'ringo\']");
                it.setClient(_parseRoot);
                ServerPortInstance _parseRoot_1 = MdmsModelCreator.this.<ServerPortInstance>parseRoot("/artefactInstances[name=\'ringoJS\']/provided[name=\'ringo\']");
                it.setServer(_parseRoot_1);
              }
            };
          BindingInstance _doubleArrow_14 = ObjectExtensions.<BindingInstance>operator_doubleArrow(_instanciates_11, _function_23);
          _bindingInstances_2.add(_doubleArrow_14);
          List<BindingInstance> _bindingInstances_3 = it.getBindingInstances();
          Map<String,Binding> _bindingTypes_8 = MdmsModelCreator.this.dm.getBindingTypes();
          Binding _get_9 = _bindingTypes_8.get("JS");
          BindingInstance _instanciates_12 = _get_9.instanciates("js");
          final Procedure1<BindingInstance> _function_24 = new Procedure1<BindingInstance>() {
              public void apply(final BindingInstance it) {
                ClientPortInstance _parseRoot = MdmsModelCreator.this.<ClientPortInstance>parseRoot("/artefactInstances[name=\'ringoJS\']/required[name=\'js\']");
                it.setClient(_parseRoot);
                ServerPortInstance _parseRoot_1 = MdmsModelCreator.this.<ServerPortInstance>parseRoot("/artefactInstances[name=\'rhino\']/provided[name=\'js\']");
                it.setServer(_parseRoot_1);
              }
            };
          BindingInstance _doubleArrow_15 = ObjectExtensions.<BindingInstance>operator_doubleArrow(_instanciates_12, _function_24);
          _bindingInstances_3.add(_doubleArrow_15);
          List<BindingInstance> _bindingInstances_4 = it.getBindingInstances();
          Map<String,Binding> _bindingTypes_9 = MdmsModelCreator.this.dm.getBindingTypes();
          Binding _get_10 = _bindingTypes_9.get("Jvm");
          BindingInstance _instanciates_13 = _get_10.instanciates("jvm");
          final Procedure1<BindingInstance> _function_25 = new Procedure1<BindingInstance>() {
              public void apply(final BindingInstance it) {
                ClientPortInstance _parseRoot = MdmsModelCreator.this.<ClientPortInstance>parseRoot("/artefactInstances[name=\'rhino\']/required[name=\'jvm\']");
                it.setClient(_parseRoot);
                ServerPortInstance _parseRoot_1 = MdmsModelCreator.this.<ServerPortInstance>parseRoot("/artefactInstances[name=\'openJDK\']/provided[name=\'jvm\']");
                it.setServer(_parseRoot_1);
              }
            };
          BindingInstance _doubleArrow_16 = ObjectExtensions.<BindingInstance>operator_doubleArrow(_instanciates_13, _function_25);
          _bindingInstances_4.add(_doubleArrow_16);
          List<BindingInstance> _bindingInstances_5 = it.getBindingInstances();
          Map<String,Binding> _bindingTypes_10 = MdmsModelCreator.this.dm.getBindingTypes();
          Binding _get_11 = _bindingTypes_10.get("Jvm");
          BindingInstance _instanciates_14 = _get_11.instanciates("jvm2");
          final Procedure1<BindingInstance> _function_26 = new Procedure1<BindingInstance>() {
              public void apply(final BindingInstance it) {
                ClientPortInstance _parseRoot = MdmsModelCreator.this.<ClientPortInstance>parseRoot("/artefactInstances[name=\'ringoJS\']/required[name=\'jvm\']");
                it.setClient(_parseRoot);
                ServerPortInstance _parseRoot_1 = MdmsModelCreator.this.<ServerPortInstance>parseRoot("/artefactInstances[name=\'openJDK\']/provided[name=\'jvm\']");
                it.setServer(_parseRoot_1);
              }
            };
          BindingInstance _doubleArrow_17 = ObjectExtensions.<BindingInstance>operator_doubleArrow(_instanciates_14, _function_26);
          _bindingInstances_5.add(_doubleArrow_17);
        }
      };
    ObjectExtensions.<DeploymentModel>operator_doubleArrow(
      this.dm, _function);
    return this.dm;
  }
  
  public void initFullPortInstance(final ArtefactInstance ai) {
    final Artefact type = ai.getType();
    List<ClientPort> _required = type.getRequired();
    final Function1<ClientPort,ClientPortInstance> _function = new Function1<ClientPort,ClientPortInstance>() {
        public ClientPortInstance apply(final ClientPort cp) {
          String _name = cp.getName();
          ClientPortInstance _clientPortInstance = new ClientPortInstance(_name, cp, ai);
          return _clientPortInstance;
        }
      };
    List<ClientPortInstance> _map = ListExtensions.<ClientPort, ClientPortInstance>map(_required, _function);
    ai.setRequired(_map);
    List<ServerPort> _provided = type.getProvided();
    final Function1<ServerPort,ServerPortInstance> _function_1 = new Function1<ServerPort,ServerPortInstance>() {
        public ServerPortInstance apply(final ServerPort sp) {
          String _name = sp.getName();
          ServerPortInstance _serverPortInstance = new ServerPortInstance(_name, sp, ai);
          return _serverPortInstance;
        }
      };
    List<ServerPortInstance> _map_1 = ListExtensions.<ServerPort, ServerPortInstance>map(_provided, _function_1);
    ai.setProvided(_map_1);
  }
  
  public <T extends Object> T parseRoot(final String xpath) {
    JXPathContext _newContext = JXPathContext.newContext(this.dm);
    Object _value = _newContext.getValue(xpath);
    return ((T) _value);
  }
  
  public <T extends Object> T parseMe(final String xpath, final Object context) {
    JXPathContext _newContext = JXPathContext.newContext(context);
    Object _value = _newContext.getValue(xpath);
    return ((T) _value);
  }
}
