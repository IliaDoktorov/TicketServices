pipeline {
	agent any
    tools {
        maven 'Maven-3.9.4'
		jdk 'azul-18.0.2.1'
    }

    stages {
        stage('Build') {
			steps{
				bat 'mvn -f pom.xml clean package && exit %%ERRORLEVEL%%'
			}
		}
		
		stage('Copy jars') {
			steps{
				bat script: '''copy TicketService\\target\\TicketService-0.0.1-SNAPSHOT.jar TicketService\\TicketService-0.0.1-SNAPSHOT.jar'''
				bat script: '''copy TicketReplicator\\target\\TicketReplicator-0.0.1-SNAPSHOT.jar TicketReplicator\\TicketReplicator-0.0.1-SNAPSHOT.jar'''
			}
		}
		
		stage('Clean up containers and images') {
		    steps{
				bat 'docker container rm -f ticketservices-ticketdatabase-1'
				bat 'docker container rm -f ticketservices-ticketservice-1'
				bat 'docker container rm -f ticketservices-ticketreplicator-1'
				script{
					try{
						bat 'docker image rm ticketservices-ticketdatabase'
					} catch (Exception err) {
						echo "Error: ${err}"
					}
					try{
						bat 'docker image rm ticketservices-ticketservice'
					} catch (Exception err) {
						echo "Error: ${err}"
					}
										try{
						bat 'docker image rm ticketservices-ticketreplicator'
					} catch (Exception err) {
						echo "Error: ${err}"
					}
				}
		    }
		}
		
		stage('Build Images') {
		    steps {
		        bat 'docker-compose -f docker-compose.yml build'
		    }
		}
		
		stage('Run Containers') {
		    steps {
				bat 'docker-compose -f docker-compose.yml up -d'
		    }
		}
    }
}