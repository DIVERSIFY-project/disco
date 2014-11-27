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
/**
 *
 * This file is part of Disco.
 *
 * Disco is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Disco is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Disco. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 */
package eu.diversify.disco.experiments.cba;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import org.cloudml.codecs.DotCodec;
import org.cloudml.codecs.JsonCodec;
import org.cloudml.core.Deployment;
import org.junit.Ignore;

import static org.cloudml.core.builders.Commons.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * This is a sandbox test, which is meant to be ignored
 */
@Ignore
@RunWith(JUnit4.class)
public class SandboxTest {

    @Test
    public void sandbox() throws FileNotFoundException {
//        final String destination = "../src/test/resources/distributed_sensapp.json";
        final String jsonOutput = "C:/Users/franckc/Desktop/cba/topologies/distributed_sensapp.json";
        final JsonCodec json = new JsonCodec();
        json.save(distributedSensApp(), new FileOutputStream(jsonOutput));

        final String dotOutput = "C:/Users/franckc/Desktop/cba/topologies/distributed_sensapp.dot";
        final DotCodec dot = new DotCodec();
        dot.save(distributedSensApp(), new FileOutputStream(dotOutput));

    }

    public Deployment distributedSensApp() {
        return aDeployment()
                .named("distributed sensapp")
                .with(aProvider().named(AWS_EC2))
                .with(aVM()
                        .named(SMALL_LINUX)
                        .providedBy(AWS_EC2)
                        .with(aProvidedExecutionPlatform()
                                .named("sl-ubuntu")
                                .offering(OS, UBUNTU_1404)))
                .with(anInternalComponent()
                        .named(ADMIN)
                        .withProperty(IS_SERVICE, "true")
                        .withProperty(AT_LEAST, "1")
                        .withProperty(AT_MOST, "1")
                        .with(aRequiredPort()
                                .named("req-notifier")
                                .mandatory()
                                .remote())
                        .with(aRequiredPort()
                                .named("req-registry")
                                .mandatory()
                                .remote())
                        .with(aRequiredPort()
                                .named("req-storage")
                                .mandatory()
                                .remote())
                        .with(aRequiredExecutionPlatform()
                                .named("env-admin")
                                .demanding(SERVLET_CONTAINER, SC_v30)))
                .with(anInternalComponent()
                        .named(NOTIFIER)
                        .withProperty(IS_SERVICE, "true")
                        .withProperty(AT_LEAST, "1")
                        .withProperty(AT_MOST, "1")
                        .with(aProvidedPort()
                                .named("prov-notifier")
                                .remote())
                        .with(aRequiredExecutionPlatform()
                                .named("env-notifier")
                                .demanding(SERVLET_CONTAINER, SC_v30)))
                .with(anInternalComponent()
                        .named(REGISTRY)
                        .withProperty(IS_SERVICE, "true")
                        .withProperty(AT_LEAST, "1")
                        .withProperty(AT_MOST, "1")
                        .with(aProvidedPort()
                                .named("prov-registry")
                                .remote())
                        .with(aRequiredPort()
                                .named("req-storage")
                                .mandatory()
                                .remote())
                        .with(aRequiredExecutionPlatform()
                                .named("env-registry")
                                .demanding(SERVLET_CONTAINER, SC_v30)))
                .with(anInternalComponent()
                        .named(STORAGE)
                        .withProperty(IS_SERVICE, "true")
                        .withProperty(AT_LEAST, "1")
                        .withProperty(AT_MOST, "1")
                        .with(aProvidedPort()
                                .named("prov-storage")
                                .remote())
                        .with(aRequiredPort()
                                .named("req-db")
                                .mandatory()
                                .remote())
                        .with(aRequiredExecutionPlatform()
                                .named("env-storage")
                                .demanding(SERVLET_CONTAINER, SC_v30)))
                .with(anInternalComponent()
                        .named(DISPATCHER)
                        .withProperty(IS_SERVICE, "true")
                        .withProperty(AT_LEAST, "1")
                        .withProperty(AT_MOST, "1")
                        .with(aRequiredPort()
                                .named("req-notifier")
                                .mandatory()
                                .remote())
                        .with(aRequiredPort()
                                .named("req-registry")
                                .mandatory()
                                .remote())
                        .with(aRequiredPort()
                                .named("req-storage")
                                .mandatory()
                                .remote())
                        .with(aRequiredExecutionPlatform()
                                .named("env-admin")
                                .demanding(SERVLET_CONTAINER, SC_v30)))
                .with(anInternalComponent()
                        .named(MONGO_DB)
                        .with(aProvidedPort()
                                .named("prov-db")
                                .remote())
                        .with(aRequiredExecutionPlatform()
                                .named("env-db")
                                .demanding(OS, UBUNTU_1404)))
                .with(aRelationship()
                        .named(ADMIN_TO_NOTIFIER)
                        .from(ADMIN, "req-notifier")
                        .to(NOTIFIER, "prov-notifier"))
                .with(aRelationship()
                        .named(ADMIN_TO_REGISTRY)
                        .from(ADMIN, "req-registry")
                        .to(REGISTRY, "prov-registry"))
                .with(aRelationship()
                        .named(ADMIN_TO_STORAGE)
                        .from(ADMIN, "req-storage")
                        .to(STORAGE, "prov-storage"))
                .with(aRelationship()
                        .named(REGISTRY_TO_STORAGE)
                        .from(REGISTRY, "req-storage")
                        .to(STORAGE, "prov-storage"))
                .with(aRelationship()
                        .named(STORAGE_TO_DB)
                        .from(STORAGE, "req-db")
                        .to(MONGO_DB, "prov-db"))
                .with(aRelationship()
                        .named(DISTPACTHER_TO_NOTIFIER)
                        .from(DISPATCHER, "req-notifier")
                        .to(NOTIFIER, "prov-notifier"))
                .with(aRelationship()
                        .named(DISPATCHER_TO_REGISTRY)
                        .from(DISPATCHER, "req-registry")
                        .to(REGISTRY, "prov-registry"))
                .with(aRelationship()
                        .named(DISPATCHER_TO_STORAGE)
                        .from(DISPATCHER, "req-storage")
                        .to(STORAGE, "prov-storage"))
                .with(anInternalComponent()
                        .named(JETTY)
                        .with(aProvidedExecutionPlatform()
                                .named("sc")
                                .offering(SERVLET_CONTAINER, SC_v30))
                        .with(aRequiredExecutionPlatform()
                                .named("env-jetty")
                                .demanding(JRE, v6)))
                .with(anInternalComponent()
                        .named(OPEN_JDK)
                        .with(aProvidedExecutionPlatform()
                                .named("jre1.6")
                                .offering(JRE, v6))
                        .with(aRequiredExecutionPlatform()
                                .named("env-openjdk")
                                .demanding(OS, UBUNTU_1404)))
                // Instances
                .with(aVMInstance().named("vm1").ofType(SMALL_LINUX))
                .with(aVMInstance().named("vm2").ofType(SMALL_LINUX))
                .with(aVMInstance().named("vm3").ofType(SMALL_LINUX))
                .with(aVMInstance().named("vm4").ofType(SMALL_LINUX))
                .with(anInternalComponentInstance()
                        .named("jre1")
                        .ofType(OPEN_JDK)
                        .hostedBy("vm1"))
                .with(anInternalComponentInstance()
                        .named("jre2")
                        .ofType(OPEN_JDK)
                        .hostedBy("vm2"))
                .with(anInternalComponentInstance()
                        .named("jre3")
                        .ofType(OPEN_JDK)
                        .hostedBy("vm3"))
                .with(anInternalComponentInstance()
                        .named("jre4")
                        .ofType(OPEN_JDK)
                        .hostedBy("vm4"))
                .with(anInternalComponentInstance()
                        .named("jetty1")
                        .ofType(JETTY)
                        .hostedBy("jre1"))
                .with(anInternalComponentInstance()
                        .named("jetty2")
                        .ofType(JETTY)
                        .hostedBy("jre2"))
                .with(anInternalComponentInstance()
                        .named("jetty3")
                        .ofType(JETTY)
                        .hostedBy("jre3"))
                .with(anInternalComponentInstance()
                        .named("jetty4")
                        .ofType(JETTY)
                        .hostedBy("jre4"))
                .with(anInternalComponentInstance()
                        .named("admin")
                        .ofType(ADMIN)
                        .hostedBy("jetty1"))
                .with(anInternalComponentInstance()
                        .named("notifier1")
                        .ofType(NOTIFIER)
                        .hostedBy("jetty2"))
                .with(anInternalComponentInstance()
                        .named("dispatcher1")
                        .ofType(DISPATCHER)
                        .hostedBy("jetty4"))
                .with(anInternalComponentInstance()
                        .named("registry1")
                        .ofType(REGISTRY)
                        .hostedBy("jetty2"))
                .with(anInternalComponentInstance()
                        .named("storage1")
                        .ofType(STORAGE)
                        .hostedBy("jetty3"))
                .with(anInternalComponentInstance()
                        .named("db1")
                        .ofType(MONGO_DB)
                        .hostedBy("vm3"))
                .with(aRelationshipInstance()
                        .named("r1")
                        .ofType(ADMIN_TO_NOTIFIER)
                        .from("admin", "req-notifier")
                        .to("notifier1", "prov-notifier"))
                .with(aRelationshipInstance()
                        .named("r2")
                        .ofType(ADMIN_TO_REGISTRY)
                        .from("admin", "req-registry")
                        .to("registry1", "prov-registry"))
                .with(aRelationshipInstance()
                        .named("r3")
                        .ofType(ADMIN_TO_STORAGE)
                        .from("admin", "req-storage")
                        .to("storage1", "prov-storage"))
                .with(aRelationshipInstance()
                        .named("r4")
                        .ofType(REGISTRY_TO_STORAGE)
                        .from("registry1", "req-storage")
                        .to("storage1", "prov-storage"))
                .with(aRelationshipInstance()
                        .named("r5")
                        .ofType(DISTPACTHER_TO_NOTIFIER)
                        .from("dispatcher1", "req-notifier")
                        .to("notifier1", "prov-notifier"))
                .with(aRelationshipInstance()
                        .named("r6")
                        .ofType(DISPATCHER_TO_REGISTRY)
                        .from("dispatcher1", "req-registry")
                        .to("registry1", "prov-registry"))
                .with(aRelationshipInstance()
                        .named("r7")
                        .ofType(DISPATCHER_TO_STORAGE)
                        .from("dispatcher1", "req-storage")
                        .to("storage1", "prov-storage"))
                .with(aRelationshipInstance()
                        .named("r8")
                        .ofType(STORAGE_TO_DB)
                        .from("storage1", "req-db")
                        .to("db1", "prov-db"))
                .build();
    }
    private static final String DISPATCHER_TO_STORAGE = "dispatcher-to-storage";
    private static final String DISPATCHER_TO_REGISTRY = "dispatcher-to-registry";
    private static final String DISTPACTHER_TO_NOTIFIER = "distpacther-to-notifier";
    private static final String STORAGE_TO_DB = "storage-to-db";
    private static final String REGISTRY_TO_STORAGE = "registry-to-storage";
    private static final String ADMIN_TO_STORAGE = "admin-to-storage";
    private static final String ADMIN_TO_REGISTRY = "admin-to-registry";
    private static final String ADMIN_TO_NOTIFIER = "admin-to-notifier";
    private static final String JETTY = "Jetty";
    private static final String OPEN_JDK = "OpenJDK";
    private static final String SMALL_LINUX = "Small Linux";
    private static final String v6 = "v1.6";
    private static final String JRE = "jre";
    private static final String MONGO_DB = "MongoDB";
    private static final String DISPATCHER = "Dispatcher";
    private static final String STORAGE = "Storage";
    private static final String REGISTRY = "Registry";
    private static final String NOTIFIER = "Notifier";
    private static final String ADMIN = "Admin";
    private static final String AT_LEAST = "at_least";
    private static final String AT_MOST = "at_most";
    private static final String SC_v30 = "v3.0";
    private static final String SERVLET_CONTAINER = "Servlet Container";
    private static final String IS_SERVICE = "is_service";
    private static final String UBUNTU_1404 = "Ubuntu 14.04";
    private static final String OS = "OS";
    private static final String AWS_EC2 = "AWS-EC2";

}
