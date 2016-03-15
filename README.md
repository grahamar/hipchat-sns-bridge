Hipchat SNS Bridge
=========================

# Installation

You need to configure the `application.production.conf` file first to your own setup.

Docker is the packaging method, so build the docker image via:

    sbt docker:publishLocal
    
You can then deploy that Docker image wherever you wish, AWS, bare-metal, etc. Once that's done, you need to install it in Hipchat. My setup is via hip chat cloud.



