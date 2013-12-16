
Ephesoft component for Alfresco Repository
==========================================

Author: Ben Chevallereau

This project defines a set of extensions to connect Alfresco to Ephesoft. 


Installation
------------

The component has been developed to install on top of an existing Alfresco
4.0, 4.1 or 4.2 installation. The ephesoft-components-repo-<version>.amp needs
to be installed into the Alfresco Repository webapp using the Alfresco Module Management Tool:

    java -jar alfresco-mmt.jar install ephesoft-components-repo-<version>.amp /path/to/alfresco.war

Building
--------

To build the module and its AMP / JAR files, run the following command from the base 
project directory:

    mvn clean package

The command builds an AMP file named ephesoft-components-repo-<version>.amp in the 'target' directory
within your project.

Included components
-------------------

* Webscripts to get all active batch instances in Ephesoft.
