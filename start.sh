#!/bin/bash
JAVA_HOME=/usr/lib/jvm/java-7-openjdk-amd64
/usr/local/netbeans-7.3.1/java/maven/bin/mvn "-Dexec.args=-classpath %classpath cz.cvut.fit.dufekja1.shopAnalyst.RESTStarter" -Dexec.executable=/usr/lib/jvm/java-7-openjdk-amd64/bin/java -Dexec.classpathScope=runtime process-classes org.codehaus.mojo:exec-maven-plugin:1.2.1:exec
