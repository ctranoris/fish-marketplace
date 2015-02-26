A marketplace and brokering RESTful service on top of FIWARE
==========

FISH-Marketplace Web Service is a RESTful marketplace service for FISH project of FI-STAR (http://www.fi-star.eu)
More specifically, it addresses the objective for the realization of the highly desirable functionalities of Marketplace and Deployment tool with features including both service discovery and automated service deployment  FISH project defines an architecture and a development approach for delivering to FI-STAR a holistic marketplace solution for easily deploying applications, enablers and services. The architecture consists of a Marketplace backend solution, a Marketplace frontend interface, a Brokering service that accommodates marketplace requests and a client FI-STAR Application container and application management tools for the various end-user devices/machines that will be supported. FISH proposes support of features, implementation tools, utilization of FI-WARE GEs and an agile development process that ensures the delivery of the envisaged processes on time. 

This service can also be used to access via OAuth2 FIWARE Lab resources (from https://account.lab.fiware.org)

The FISH Marketplace and brokering service, is a RESTful backend service build in java. Currently it runs in the Jetty container and needs MySQL to persist data. Apache CXF is used to implement the RESTful interface and Apache SHIRO as the Authentication and Authorization framework.  A java implementation for openstack API is called woorea and it is used to access resources on FIWARE Lab.
Main offered services are:
-	management of users and their resources
-	management of application
-	management of buns
-	management of the platform
-	brokering, orchestrating and provisioning on target machines
-	OAUTH2 login based to FIWARE Lab
-	Automated provisioning of machines on top of FIWARE Lab


Licenses
--------

The license for this software is [Apache 2 v2.1](./src/license/header.txt).

Contact
-------

For further information on collaboration, support or alternative licensing, please contact:

* Website: http://nam.ece.upatras.gr
* Email: tranoris@ece.upatras.gr
