
Ephesoft component for Alfresco Share
=====================================

Author: Ben Chevallereau

This project defines a set of extensions to connect Alfresco to Ephesoft. 


Installation
------------

The component has been developed to install on top of an existing Alfresco
4.0, 4.1 or 4.2 installation. The ephesoft-components-share-<version>.amp needs
to be installed into the Alfresco Share webapp using the Alfresco Module Management Tool:

    java -jar alfresco-mmt.jar install ephesoft-components-share-<version>.amp /path/to/share.war

Building
--------

To build the module and its AMP / JAR files, run the following command from the base 
project directory:

    mvn clean package

The command builds an AMP file named ephesoft-components-share-<version>.amp in the 'target' directory
within your project.

Included components
-------------------

* User Dashlet to display status and progress of active batch instances in Ephesoft. It's also possible
to open batch instances directly from Alfresco Share.
