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

import org.cloudml.core.Deployment;
import org.cloudml.core.builders.*;

import static org.cloudml.core.builders.Commons.*;

/**
 * Build variations around the Graph Hopper case study in Diversify.
 */
public class GraphHopper {

    /**
     * Encapsulate a specific type of sensor processor (e.g., a processor for
     * noise measurements, pollution, traffic), and the software stack on which
     * it is running.
     */
    private static class SensorProcessorStack {

        private final String metricName;

        public SensorProcessorStack(String metricName) {
            this.metricName = metricName;
        }

        public void insertTypeIn(DeploymentBuilder deployment) {
            assert deployment != null: "Unable to define a sensor processor on 'null'";

            deployment.with(anInternalComponent()
                    .named(getTypeName())
                    .withProperty(AT_LEAST, "1")
                    .withProperty(AT_MOST, "1")
                    .withProperty(IS_SERVICE, "true")
                    .with(aRequiredPort()
                            .named(SP_REQ_DB)
                            .remote()
                            .mandatory())
                    .with(aRequiredExecutionPlatform()
                            .named(SP_REQ_PYTHON)
                            .demanding(PYTHON, PY_27)))
                    .with(aRelationship()
                            .named(getRelationToDBName())
                            .from(getTypeName(), SP_REQ_DB)
                            .to(REDIS, PROV_DB));
        }

        private String getRelationToDBName() {
            return String.format(SP_TO_DB, metricName);
        }

        private String getTypeName() {
            return String.format(SENSOR_PROCESSOR, metricName);
        }

        public void provisionInstance(DeploymentBuilder deployment, int id, String dbName, String vmName) {
            assert deployment != null: "Unable to work with a'null' deployment";

            final String python = PYTHON + id;
            final String dbConnection = "SP " + id + " to db";
            final String instanceName = getTypeName() + " " + id;

            deployment.with(aVMInstance()
                    .named(vmName)
                    .ofType(LARGE_LINUX))
                    .with(anInternalComponentInstance()
                            .named(python)
                            .ofType(PYTHON)
                            .hostedBy(vmName))
                    .with(anInternalComponentInstance()
                            .named(instanceName)
                            .ofType(getTypeName())
                            .hostedBy(python))
                    .with(aRelationshipInstance()
                            .named(dbConnection)
                            .ofType(getRelationToDBName())
                            .from(instanceName, SP_REQ_DB)
                            .to(dbName, PROV_DB));
        }

    }

    public Deployment getDefaultDeployment() {

        final DeploymentBuilder deployment = getDefaultTypes()
                .named("GraphHopper")
                .with(aVMInstance()
                        .named("vm1")
                        .ofType(LARGE_LINUX))
                .with(aVMInstance()
                        .named("vm2")
                        .ofType(LARGE_LINUX))
                .with(aVMInstance()
                        .named("vm3")
                        .ofType(LARGE_LINUX))
                .with(aVMInstance()
                        .named("vm4")
                        .ofType(LARGE_LINUX))
                .with(aVMInstance()
                        .named("vm db")
                        .ofType(LARGE_LINUX))
                .with(anInternalComponentInstance()
                        .named(REDIS + "1")
                        .ofType(REDIS)
                        .hostedBy("vm db"))
                .with(aVMInstance()
                        .named("vm gh")
                        .ofType(LARGE_LINUX))
                .with(anInternalComponentInstance()
                        .named(OPEN_JDK + 1)
                        .ofType(OPEN_JDK)
                        .hostedBy("vm gh"))
                .with(anInternalComponentInstance()
                        .named(GRAPH_HOPPER + "1")
                        .ofType(GRAPH_HOPPER)
                        .hostedBy(OPEN_JDK + 1))
                .with(aRelationshipInstance()
                        .named("gh1 to db")
                        .ofType(GH_TO_DB)
                        .from(GRAPH_HOPPER + "1", GH_REQ_DB)
                        .to(REDIS + 1, PROV_DB));

        int sensorIndex = 1;
        for (SensorProcessorStack eachSensor: sensors) {
            eachSensor.insertTypeIn(deployment);
            eachSensor.provisionInstance(deployment, sensorIndex, REDIS + 1, "vm" + sensorIndex);
            sensorIndex += 1;
        }

        return deployment.build();
    }

    private static final SensorProcessorStack[] sensors = new SensorProcessorStack[]{
        new SensorProcessorStack("Noise"),
        new SensorProcessorStack("Traffic"),
        new SensorProcessorStack("Pollution")
    };

    private DeploymentBuilder getDefaultTypes() {
        final DeploymentBuilder deployment = aDeployment()
                .with(aProvider().named(AWS_EC2))
                .with(ec2LargeLinux())
                .with(graphHopper())
                .with(python())
                .with(openJdk())
                .with(redis())
                .with(aRelationship()
                        .named(GH_TO_DB)
                        .from(GRAPH_HOPPER, GH_REQ_DB)
                        .to(REDIS, PROV_DB));

        return deployment;
    }

    private static final String OPEN_JDK = "OpenJDK";
    private static final String JRE_v17 = "jre1.7";
    private static final String JRE = "JRE";

    private InternalComponentBuilder python() {
        return anInternalComponent()
                .named(PYTHON)
                .with(aProvidedExecutionPlatform()
                        .named("Python")
                        .offering(PYTHON, PY_27))
                .with(aRequiredExecutionPlatform()
                        .named("Python")
                        .demanding(OS, UBUNTU_1404));
    }
    private static final String PY_27 = "py2.7";

    private static final String PYTHON = "Python";

    private InternalComponentBuilder graphHopper() {
        return anInternalComponent()
                .named(GRAPH_HOPPER)
                .withProperty(AT_LEAST, "1")
                .withProperty(AT_MOST, "1")
                .withProperty(IS_SERVICE, "true")
                .with(aRequiredPort()
                        .named(GH_REQ_DB)
                        .remote()
                        .mandatory())
                .with(aRequiredExecutionPlatform()
                        .named(GH_REQ_JRE17)
                        .demanding(JRE, JRE_v17));
    }

    private static InternalComponentBuilder openJdk() {
        return anInternalComponent()
                .named(OPEN_JDK)
                .with(aProvidedExecutionPlatform()
                        .named("JRE 1.7")
                        .offering(JRE, JRE_v17))
                .with(aRequiredExecutionPlatform()
                        .named("jdk on ubuntu")
                        .demanding(OS, UBUNTU_1404));
    }

    private InternalComponentBuilder redis() {
        return anInternalComponent()
                .named(REDIS)
                .withProperty(AT_LEAST, "1")
                .withProperty(AT_MOST, "1")
                .withProperty(IS_SERVICE, "true")
                .with(aProvidedPort()
                        .named(PROV_DB)
                        .remote())
                .with(aRequiredExecutionPlatform()
                        .named(SP_REQ_PYTHON)
                        .demanding(OS, UBUNTU_1404));
    }

    private VMBuilder ec2LargeLinux() {
        return aVM()
                .named(LARGE_LINUX)
                .providedBy(AWS_EC2)
                .with(aProvidedExecutionPlatform()
                        .named("prov. Ubuntu")
                        .offering(OS, UBUNTU_1404));
    }

    private static final String GH_REQ_JRE17 = "GH req. jre17";
    private static final String AT_LEAST = "at_least";
    private static final String AT_MOST = "at_most";
    private static final String IS_SERVICE = "is_service";
    private static final String SP_REQ_PYTHON = "SP req. Python";
    private static final String GH_TO_DB = "GH to DB";
    private static final String SP_TO_DB = "%s to DB";
    private static final String PROV_DB = "prov. db";
    private static final String GRAPH_HOPPER = "GraphHopper";
    private static final String GH_REQ_DB = "GH req. DB";
    private static final String SP_REQ_DB = "SP req. DB";
    private static final String REDIS = "Redis";
    private static final String UBUNTU_1404 = "Ubuntu 14.04";
    private static final String OS = "OS";
    private static final String SENSOR_PROCESSOR = "%s Processor";
    private static final String LARGE_LINUX = "Large Linux";
    private static final String AWS_EC2 = "AWS EC2";

}
