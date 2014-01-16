package mdms.creator

import org.cloudml.core.DeploymentModel
import org.cloudml.core.Node
import org.cloudml.core.Artefact
import org.cloudml.core.ClientPort
import org.cloudml.core.ServerPort
import org.apache.commons.jxpath.JXPathContext
import org.cloudml.core.Binding
import org.cloudml.core.NodeInstance
import org.cloudml.core.ClientPortInstance
import org.cloudml.core.ArtefactInstance
import org.cloudml.core.ServerPortInstance
import org.cloudml.core.BindingInstance
import org.cloudml.core.Provider

class MdmsModelCreator {
	
	DeploymentModel dm = null;
	
	
	def DeploymentModel create(){
		dm = new DeploymentModel("mdms") 
		dm => [
			val prov = new Provider("provider", "../src/main/resources/credentials")
			providers += prov
			nodeTypes.put("EC2", new Node("EC2") => [
				provider = prov
			])
			artefactTypes.put("MDMS", new Artefact("MDMS") => [ 
				provided += new ServerPort("content", it, true)
				required += new ClientPort("db", it, true)
				required += new ClientPort("ringo", it, false)
			])
			artefactTypes.put("RingoJS", new Artefact("RingoJS") =>[
				provided += new ServerPort("ringo", it, false)
				required += new ClientPort("js", it, false)
				required += new ClientPort("jvm", it, false)
			])	
			artefactTypes.put("Rhino", new Artefact("Rhino") => [
				provided += new ServerPort("js", it, false)
				required += new ClientPort("jvm", it, false)
			])
			artefactTypes.put("OpenJDK", new Artefact("OpenJDK") => [
				provided += new ServerPort("jvm", it, false)
			])
			artefactTypes.put("MySQL", new Artefact("MySQL") => [
				provided += new ServerPort("db", it, true)
			])
			artefactTypes.put("Balancer", new Artefact("Balancer") => [
				required += new ClientPort("content", it, true)
			])
			
			
			
			bindingTypes.put("Content", new Binding()=>[
				name = "Content"
				client = "/artefactTypes/Balancer/required[name='content']".<ClientPort>parseRoot 
				server = "/artefactTypes/MDMS/provided[name='content']".<ServerPort>parseRoot 
			])
			
			bindingTypes.put("DB", new Binding() =>[
				name = "DB"
				client = "/artefactTypes/MDMS/required[name='db']".<ClientPort>parseRoot 
				server = "/artefactTypes/MySQL/provided[name='db']".<ServerPort>parseRoot 
			])
			
			bindingTypes.put("Ringo", new Binding() =>[
				name = "Ringo"
				client = "/artefactTypes/MDMS/required[name='ringo']".<ClientPort>parseRoot 
				server = "/artefactTypes/RingoJS/provided[name='ringo']".<ServerPort>parseRoot 
			])
			
			bindingTypes.put("JS", new Binding() =>[
				client = "/artefactTypes/RingoJS/required[name='js']".<ClientPort>parseRoot 
				server = "/artefactTypes/Rhino/provided[name='js']".<ServerPort>parseRoot
			])
			
			bindingTypes.put("Jvm", new Binding() =>[
				client = "/artefactTypes/RingoJS/required[name='jvm']".<ClientPort>parseRoot
				server = "/artefactTypes/OpenJDK/provided[name='jvm']".<ServerPort>parseRoot
			])
			
			
			val ec2_bal = "/nodeTypes/EC2".<Node>parseRoot.instanciates("ec2_bal") =>[]
			
			nodeInstances += ec2_bal
			
			val ec2_mdms = "/nodeTypes/EC2".<Node>parseRoot.instanciates("ec2_mdms") =>[]
			nodeInstances += ec2_mdms
			
			val ec2_db = "/nodeTypes/EC2".<Node>parseRoot.instanciates("ec2_db") =>[]
			nodeInstances += ec2_db
			
			
			val balancer = artefactTypes.get("Balancer").instanciates("balancer") =>[
				
				destination = ec2_bal
				it.initFullPortInstance
			]
			artefactInstances += balancer
			
			val mdms = artefactTypes.get("MDMS").instanciates("mdms") =>[
				destination = ec2_mdms
				it.initFullPortInstance
			]
			artefactInstances += mdms
			
			val ringoJS= artefactTypes.get("RingoJS").instanciates("ringoJS") =>[
				destination = ec2_mdms
				it.initFullPortInstance
			]
			artefactInstances += ringoJS
			
			val rhino = artefactTypes.get("Rhino").instanciates("rhino") =>[
				destination = ec2_mdms
				it.initFullPortInstance
			]
			artefactInstances += rhino
			
			val openJDK = artefactTypes.get("OpenJDK").instanciates("openJDK") =>[
				destination = ec2_mdms
				it.initFullPortInstance
			]
			artefactInstances += openJDK
			
			val mySQL = artefactTypes.get("MySQL").instanciates("mySQL") =>[
				destination = ec2_db
				it.initFullPortInstance
			]
			artefactInstances += mySQL
			
			
			bindingInstances += dm.bindingTypes.get("Content").instanciates("content") => [
				client = "/artefactInstances[name='balancer']/required[name='content']".<ClientPortInstance>parseRoot
				server = "/artefactInstances[name='mdms']/provided[name='content']".<ServerPortInstance>parseRoot
			]
			
			bindingInstances += dm.bindingTypes.get("DB").instanciates("db") => [
				client = "/artefactInstances[name='mdms']/required[name='db']".<ClientPortInstance>parseRoot
				server = "/artefactInstances[name='mySQL']/provided[name='db']".<ServerPortInstance>parseRoot
			]
			
			bindingInstances += dm.bindingTypes.get("Ringo").instanciates("ringo") => [
				client = "/artefactInstances[name='mdms']/required[name='ringo']".<ClientPortInstance>parseRoot
				server = "/artefactInstances[name='ringoJS']/provided[name='ringo']".<ServerPortInstance>parseRoot
			]
			
			bindingInstances += dm.bindingTypes.get("JS").instanciates("js") => [
				client = "/artefactInstances[name='ringoJS']/required[name='js']".<ClientPortInstance>parseRoot
				server = "/artefactInstances[name='rhino']/provided[name='js']".<ServerPortInstance>parseRoot
			]
			
			bindingInstances += dm.bindingTypes.get("Jvm").instanciates("jvm") => [
				client = "/artefactInstances[name='rhino']/required[name='jvm']".<ClientPortInstance>parseRoot
				server = "/artefactInstances[name='openJDK']/provided[name='jvm']".<ServerPortInstance>parseRoot
			]
			
			bindingInstances += dm.bindingTypes.get("Jvm").instanciates("jvm2") => [
				client = "/artefactInstances[name='ringoJS']/required[name='jvm']".<ClientPortInstance>parseRoot
				server = "/artefactInstances[name='openJDK']/provided[name='jvm']".<ServerPortInstance>parseRoot
			]
			
		]
		
		return dm
	}
	
	def initFullPortInstance(ArtefactInstance ai){
		val type = ai.type
		ai.required = type.required.map[ClientPort cp | new ClientPortInstance(cp.name, cp, ai)]
		ai.provided = type.provided.map[ServerPort sp | new ServerPortInstance(sp.name, sp, ai)]
		
	}
	
	def<T> T parseRoot(String xpath){
		JXPathContext.newContext(dm).getValue(xpath) as T
	}
	
	def<T> T parseMe(String xpath, Object context){
		JXPathContext.newContext(context).getValue(xpath) as T
	}
	
}