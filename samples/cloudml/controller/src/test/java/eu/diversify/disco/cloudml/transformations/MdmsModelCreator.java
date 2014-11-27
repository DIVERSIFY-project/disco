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
package eu.diversify.disco.cloudml.transformations;

import static org.cloudml.core.builders.Commons.*;

import org.cloudml.core.Deployment;
import org.cloudml.core.builders.DeploymentBuilder;
import org.cloudml.core.builders.InternalComponentBuilder;
import org.cloudml.core.builders.VMBuilder;

public class MdmsModelCreator {

    public Deployment create(int backendCount) {
        assert backendCount > 1: "Illegal count of backends";

        final DeploymentBuilder base
                = getMdmsTypes()
                .with(aVMInstance()
                        .named(VM_LB)
                        .ofType(LARGE_LINUX))
                .with(anInternalComponentInstance()
                        .named(LB)
                        .ofType(LOAD_BALANCER)
                        .hostedBy(VM_LB))
                .with(aVMInstance()
                        .named(VM_DB)
                        .ofType(LARGE_LINUX))
                .with(anInternalComponentInstance()
                        .named(REDIS_1)
                        .ofType(REDIS)
                        .hostedBy(VM_DB));

        for (int index = 0; index < backendCount; index++) {
            base.
                    with(aVMInstance()
                            .named(VM_MDMS + index)
                            .ofType(LARGE_LINUX))
                    .with(anInternalComponentInstance()
                            .named("jdk" + index)
                            .ofType(OPEN_JDK)
                            .hostedBy(VM_MDMS + index))
                    .with(anInternalComponentInstance()
                            .named("js" + index)
                            .ofType(JS_ENGINE)
                            .hostedBy("jdk" + index))
                    .with(anInternalComponentInstance()
                            .named("mdms" + index)
                            .ofType(MDMS)
                            .hostedBy("js" + index))
                    .with(aRelationshipInstance()
                            .named("content" + index)
                            .ofType(LB_TO_MDMS)
                            .from(LB, LB_BACKEND)
                            .to("mdms" + index, MDMS_QUERY))
                    .with(aRelationshipInstance()
                            .named("db" + index)
                            .ofType(MDMS_TO_MYSQL)
                            .from("mdms" + index, MDMS_DB)
                            .to(REDIS_1, MY_SQL_QUERY));
        }

        return base.build();

    }

    public Deployment create() {
        return getMdmsTypes()
                .with(aVMInstance()
                        .named(VM_LB)
                        .ofType(LARGE_LINUX))
                .with(aVMInstance()
                        .named(VM_MDMS) 
                        .ofType(LARGE_LINUX))
                .with(aVMInstance()
                        .named(VM_DB)
                        .ofType(LARGE_LINUX))
                .with(anInternalComponentInstance()
                        .named(LB)
                        .ofType(LOAD_BALANCER)
                        .hostedBy(VM_LB))
                .with(anInternalComponentInstance()
                        .named(JDK_1)
                        .ofType(OPEN_JDK)
                        .hostedBy(VM_MDMS))
                .with(anInternalComponentInstance()
                        .named(JS_1)
                        .ofType(JS_ENGINE)
                        .hostedBy(JDK_1))
                .with(anInternalComponentInstance()
                        .named(MDMS_1)
                        .ofType(MDMS)
                        .hostedBy(JS_1))
                .with(anInternalComponentInstance()
                        .named(REDIS_1)
                        .ofType(REDIS)
                        .hostedBy(VM_DB))
                .with(aRelationshipInstance()
                        .named("content1")
                        .ofType(LB_TO_MDMS)
                        .from(LB, LB_BACKEND)
                        .to(MDMS_1, MDMS_QUERY))
                .with(aRelationshipInstance()
                        .named("db1")
                        .ofType(MDMS_TO_MYSQL)
                        .from(MDMS_1, MDMS_DB)
                        .to(REDIS_1, MY_SQL_QUERY))
                .build();

    }

    private static DeploymentBuilder getMdmsTypes() {
        return aDeployment()
                .named("mdms")
                .with(aProvider().named(EC2))
                .with(ec2LargeLinux())
                .with(MDMS())
                .with(javascriptEngine())
                .with(anInternalComponent()
                        .named(OPEN_JDK)
                        .with(aProvidedExecutionPlatform()
                                .named(JRE)
                                .offering(JAVA, JDK17))
                        .with(aRequiredExecutionPlatform()
                                .named("os")
                                .demanding(OS, UBUNTU_1204)))
                .with(redis())
                .with(nginx())
                .with(aRelationship()
                        .named(LB_TO_MDMS)
                        .from(LOAD_BALANCER, LB_BACKEND)
                        .to(MDMS, MDMS_QUERY))
                .with(aRelationship()
                        .named(MDMS_TO_MYSQL)
                        .from(MDMS, MDMS_DB)
                        .to(REDIS, MY_SQL_QUERY));
    }

    private static VMBuilder ec2LargeLinux() {
        return aVM()
                .named(LARGE_LINUX)
                .providedBy(EC2)
                .with(aProvidedExecutionPlatform()
                        .named("linux")
                        .offering(OS, UBUNTU_1204));
    }

    private static InternalComponentBuilder nginx() {
        return anInternalComponent()
                .named(LOAD_BALANCER)
                .withProperty(AT_LEAST, "1")
                .withProperty(AT_MOST, "1")
                .with(aRequiredPort()
                        .named(LB_BACKEND)
                        .mandatory()
                        .remote())
                .with(aRequiredExecutionPlatform()
                        .named("os")
                        .demanding(OS, UBUNTU_1204));
    }

    private static InternalComponentBuilder redis() {
        return anInternalComponent()
                .named(REDIS)
                .withProperty("at_least", "1")
                .with(aProvidedPort()
                        .named(MY_SQL_QUERY)
                        .remote())
                .with(aRequiredExecutionPlatform()
                        .named("os")
                        .demanding(OS, UBUNTU_1204));
    }

    private static InternalComponentBuilder javascriptEngine() {
        return anInternalComponent()
                .named(JS_ENGINE)
                .with(aProvidedExecutionPlatform()
                        .named(JAVASCRIPT)
                        .offering(RHINO, TRUE)
                        .offering(RINGO, TRUE))
                .with(aRequiredExecutionPlatform()
                        .named(JRE)
                        .demanding(JAVA, JDK17));
    }

    private static InternalComponentBuilder MDMS() {
        return anInternalComponent()
                .named(MDMS)
                .withProperty(AT_LEAST, "1")
                .withProperty(IS_SERVICE, "true")
                .with(aProvidedPort()
                        .named(MDMS_QUERY)
                        .remote())
                .with(aRequiredPort()
                        .named(MDMS_DB)
                        .remote()
                        .mandatory())
                .with(aRequiredExecutionPlatform()
                        .named(JAVASCRIPT)
                        .demanding(RHINO, TRUE)
                        .demanding(RINGO, TRUE));
    }
    private static final String AT_MOST = "at_most";
    private static final String MDMS_2 = "mdms 2";
    private static final String JS_2 = "js 2";
    private static final String JDK_2 = "jdk 2";
    private static final String VM_MDMS_2 = "vm_mdms_2";
    private static final String IS_SERVICE = "is_service";
    private static final String AT_LEAST = "at_least";
    public static final String JAVASCRIPT = "javascript";
    public static final String JRE = "jre";
    public static final String JDK17 = "jdk1.7";
    public static final String JAVA = "java";
    public static final String UBUNTU_1204 = "Ubuntu 12.04";
    public static final String OS = "OS";
    public static final String TRUE = "Yes";
    public static final String RINGO = "Ringo";
    public static final String RHINO = "Rhino";
    public static final String LB = "lb";
    public static final String MDMS_1 = "mdms";
    public static final String REDIS_1 = "redis1";
    public static final String JS_1 = "js";
    public static final String JDK_1 = "jdk";
    public static final String VM_DB = "vm_db";
    public static final String VM_MDMS = "vm_mdms";
    public static final String MDMS_TO_MYSQL = "MDMS_TO_MYSQL";
    public static final String LB_TO_MDMS = "LB_TO_MDMS";
    public static final String LB_BACKEND = "content";
    public static final String MY_SQL_QUERY = "db";
    public static final String REDIS = "Redis";
    public static final String OPEN_JDK = "OpenJDK";
    public static final String JS_ENGINE = "JSEngine";
    public static final String MDMS_DB = "db";
    public static final String MDMS_QUERY = "query";
    public static final String MDMS = "MDMS";
    public static final String LARGE_LINUX = "LargeLinux";
    public static final String EC2 = "EC2";
    public static final String LOAD_BALANCER = "LoadBalancer";
    public static final String VM_LB = "vm_lb";

}
