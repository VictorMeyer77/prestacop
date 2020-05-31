name := "drones"

version := "0.1"

scalaVersion := "2.11.8"

libraryDependencies += "org.apache.kafka" %% "kafka" % "2.1.0"

mainClass in (Compile, run) := Some("com.prestacop.project.main.Main")
