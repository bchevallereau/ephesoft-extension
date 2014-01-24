
Ephesoft webservices
====================

Author: Ben Chevallereau

This project defines a set of webservices exposed as REST API.


Installation
------------

The component has been developed and tested to install on top of an existing Ephesoft
3.0.3.4 installation. The ephesoft-components-webservices-<version>.jar needs
to be installed into the Ephesoft installation folder

    cp ephesoft-components-webservices-<version>.jar /path/to/ephesoft/Application/WEB-INF/lib
    
And, add this line in the /path/to/ephesoft/applicationContext.xml

	<import resource="classpath:/META-INF/applicationContext-bataon-service.xml" />	

Building
--------

To build its JAR file, run the following command from the base project directory:

    mvn clean package

The command builds a JAR file named ephesoft-components-webservices-<version>.jar in the 'target' directory
within your project.

