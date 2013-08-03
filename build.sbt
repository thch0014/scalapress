name := "scalapress"

organization := "com.sksamuel.scalapress"

version := "0.36-SNAPSHOT"

scalaVersion := "2.10.2"

seq(webSettings: _*)

publishMavenStyle := true

publishTo <<= version {
  (v: String) =>
    val nexus = "https://oss.sonatype.org/"
    if (v.trim.endsWith("SNAPSHOT"))
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

publishArtifact in(Compile, packageBin) := false

net.virtualvoid.sbt.graph.Plugin.graphSettings

libraryDependencies += "javax.servlet" % "servlet-api" % "2.5" % "provided"

libraryDependencies += "org.mortbay.jetty" % "jetty" % "6.1.22" % "container"

libraryDependencies += "com.fasterxml.jackson.core" % "jackson-core" % "2.1.3"

libraryDependencies += "com.fasterxml.jackson.core" % "jackson-databind" % "2.1.3"

libraryDependencies += "com.fasterxml.jackson.jaxrs" % "jackson-jaxrs-json-provider" % "2.1.3"

libraryDependencies += "com.fasterxml.jackson.datatype" % "jackson-datatype-hibernate4" % "2.1.2"

libraryDependencies += "com.fasterxml.jackson.module" % "jackson-module-scala_2.10" % "2.1.3" exclude("org.scalatest", "scalatest_2.10.0")

libraryDependencies += "com.sksamuel.scoot" % "scoot" % "1.0.7" exclude("org.scalatest", "scalatest_2.10") exclude("javax.persistence", "persistence-api")

libraryDependencies += "com.spatial4j" % "spatial4j" % "0.3"

libraryDependencies += "org.apache.velocity" % "velocity" % "1.7"

libraryDependencies += "org.apache.velocity" % "velocity-tools" % "2.0"

libraryDependencies += "org.fusesource.scalate" % "scalate-core_2.10" % "1.6.1"

libraryDependencies += "joda-time" % "joda-time" % "1.4"

libraryDependencies += "org.slf4j" % "slf4j-api" % "1.6.6"

libraryDependencies += "log4j" % "log4j" % "1.2.17"

libraryDependencies += "org.slf4j" % "log4j-over-slf4j" % "1.6.6"

libraryDependencies += "org.apache.commons" % "commons-io" % "1.3.2"

libraryDependencies += "commons-validator" % "commons-validator" % "1.4.0"

libraryDependencies += "com.github.theon" % "scala-uri_2.10" % "0.3.5"

libraryDependencies += "com.googlecode.htmlcompressor" % "htmlcompressor" % "1.5.2"

libraryDependencies += "com.yahoo.platform.yui" % "yuicompressor" % "2.4.7"

libraryDependencies += "org.hibernate.javax.persistence" % "hibernate-jpa-2.0-api" % "1.0.1.Final"

libraryDependencies += "cglib" % "cglib" % "3.0"

libraryDependencies += "com.enterprisedt" % "edtFTPj" % "1.5.3"

libraryDependencies += "com.sun.mail" % "javax.mail" % "1.5.0"

libraryDependencies += "com.google.javascript" % "closure-compiler" % "r2388"

libraryDependencies += "javax.transaction" % "jta" % "1.1"

libraryDependencies += "com.sksamuel.scrimage" % "scrimage-core" % "1.3.3"

libraryDependencies += "org.springframework.security" % "spring-security-core" % "3.1.4.RELEASE"

libraryDependencies += "org.springframework.security" % "spring-security-config" % "3.1.4.RELEASE"

libraryDependencies += "org.springframework.security" % "spring-security-web" % "3.1.4.RELEASE"

libraryDependencies += "com.amazonaws" % "aws-java-sdk" % "1.5.2"

libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.25"

libraryDependencies += "commons-dbcp" % "commons-dbcp" % "1.4"

libraryDependencies += "org.apache.httpcomponents" % "httpclient" % "4.2.5"

libraryDependencies += "org.apache.httpcomponents" % "httpcore" % "4.2.4"

libraryDependencies += "org.jdom" % "jdom" % "2.0.2"

libraryDependencies += "org.mockito" % "mockito-all" % "1.9.5" % "test"

libraryDependencies += "org.aspectj" % "aspectjweaver" % "1.7.3"

libraryDependencies += "org.hibernate" % "hibernate-core" % "4.2.3.Final"

libraryDependencies += "org.hibernate" % "hibernate-entitymanager" % "4.2.3.Final"

libraryDependencies += "com.googlecode.genericdao" % "dao-hibernate" % "1.2.0"

libraryDependencies += "net.sourceforge.javacsv" % "javacsv" % "2.0"

libraryDependencies += "org.elasticsearch" % "elasticsearch" % "0.90.2"

libraryDependencies += "com.sksamuel.elastic4s" % "elastic4s" % "0.90.2.8"

libraryDependencies += "javax.validation" % "validation-api" % "1.1.0.Final"

libraryDependencies += "org.hibernate" % "hibernate-validator" % "5.0.1.Final"

libraryDependencies += "org.springframework" % "spring-context" % "3.2.3.RELEASE"

libraryDependencies += "org.springframework" % "spring-beans" % "3.2.3.RELEASE"

libraryDependencies += "org.springframework" % "spring-core" % "3.2.3.RELEASE"

libraryDependencies += "org.springframework" % "spring-orm" % "3.2.3.RELEASE"

libraryDependencies += "org.springframework" % "spring-web" % "3.2.3.RELEASE"

libraryDependencies += "org.springframework" % "spring-webmvc" % "3.2.3.RELEASE"

libraryDependencies += "org.springframework" % "spring-context-support" % "3.2.3.RELEASE"

libraryDependencies += "org.springframework" % "spring-core" % "3.2.3.RELEASE"

libraryDependencies += "commons-fileupload" % "commons-fileupload" % "1.3"

libraryDependencies += "org.scalatest" % "scalatest_2.10" % "2.0.M6-SNAP35" % "test"

libraryDependencies += "org.hsqldb" % "hsqldb" % "2.3.0" % "test"

pomExtra := (
  <url>https://github.com/theon/scala-uri</url>
    <licenses>
      <license>
        <name>Apache 2</name>
        <url>http://www.apache.org/licenses/LICENSE-2.0</url>
        <distribution>repo</distribution>
      </license>
    </licenses>
    <scm>
      <url>git@github.com:sksamuel/scalapress.git</url>
      <connection>scm:git@github.com:sksamuel/scalapress.git</connection>
    </scm>
    <developers>
      <developer>
        <id>theon</id>
        <name>sksamuel</name>
        <url>http://github.com/sksamuel</url>
      </developer>
    </developers>)