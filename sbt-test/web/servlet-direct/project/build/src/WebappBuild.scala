import sbt._

class WebappBuild(info: ProjectInfo) extends DefaultWebProject(info) {

	override def libraryDependencies = if("jetty7".asFile.exists) jetty7Dependencies else jetty6Dependencies
	def jetty6Dependencies =
		Set("org.mortbay.jetty"          % "servlet-api-2.5"         % "6.1.14"  % "provided->default",
		"org.mortbay.jetty"          % "jetty"                   % "6.1.14"  % "test->default")
	def jetty7Dependencies =
		Set("javax.servlet" % "servlet-api" % "2.5" % "provided",
		"org.eclipse.jetty" % "jetty-server" % "7.0.1.v20091125" % "test",
		"org.eclipse.jetty" % "jetty-webapp" % "7.0.1.v20091125" % "test")

	def indexURL = new java.net.URL("http://localhost:8080")
	def indexFile = new java.io.File("index.html")
	import Process._
	lazy val getPage = execTask { indexURL #> indexFile }
	lazy val checkPage = task { args => task { checkHelloWorld(args.mkString(" ")) } dependsOn getPage }
	
	override def jettyWebappPath = webappPath
	
	private def checkHelloWorld(checkString: String) =
	{
		val value = xsbt.FileUtilities.read(indexFile)
		if(value.contains(checkString)) None else Some("index.html did not contain '" + checkString + "' :\n" +value)
	}
}
