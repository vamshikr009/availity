import scala.sys.process._

//name := "provider"

ThisBuild / organization := "com.availity"
ThisBuild / scalaVersion := "2.12.15"
ThisBuild / useCoursier := false

/* Assembly settings
assembly / assemblyJarName := s"${name.value}.jar"
assembly / assemblyMergeStrategy := {
  case PathList("module-info.class") => MergeStrategy.rename
  case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
  case PathList("META-INF", "INDEX.LIST") => MergeStrategy.discard
  case PathList("META-INF", "DUMMY.SF") => MergeStrategy.discard
  case PathList("META-INF", "DUMMY.DSA") => MergeStrategy.discard
  case PathList("META-INF", "DUMMY.RSA") => MergeStrategy.discard
    case x => MergeStrategy.first
}
  assembly / assemblyOption := (assembly / assemblyOption).value
.copy(includeScala = false)
  assembly / test := {}
  */

  // Define library dependencies
libraryDependencies ++= Seq(
  // Spark dependencies are no longer marked as 'provided' so they are included in the assembly package
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion,
  "com.github.mrpowers" %% "spark-daria" % "1.0.0",
  "com.github.mrpowers" %% "spark-fast-tests" % "1.0.0" % Test, // For testing, marked as Test scope
  "org.scalatest" %% "scalatest" % "3.2.9" % Test // For testing, marked as Test scope
)


  // Assembly settings
  assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)
  assemblyMergeStrategy in assembly := {
    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
    case x => MergeStrategy.first
  }


  // Dependencies
  val sparkVersion = "3.4.0"
  

 /*  libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-core" % sparkVersion % "provided",
      "org.apache.spark" %% "spark-sql" % sparkVersion % "provided",
      "com.github.mrpowers" %% "spark-daria" % "1.0.0",
      "com.github.mrpowers" %% "spark-fast-tests" % "1.0.0" % Test,
      "org.scalatest" %% "scalatest" % "3.2.9" % Test)

   */
