pipeline {
    agent {
    	label "BOT"
    }
    environment {
    	sendEmailTo = "${env.sendEmailTo}"
    }
    options {
        skipStagesAfterUnstable()
    }
    stages {            
         stage('Export Zephyr to Excel') {
            steps {
                sh """
					mvn clean install exec:java -DskipTests=true
                 """
            }
        }     
    }
    post {
        always {
        		emailext attachmentsPattern: 'p20-legalentitymanagement-20210326.xlsx', 
        		body: "Please find updated LEM-Test Execution Results from Zephyr",
        		mimeType: 'text/html',
        		subject: "LEM-Test Execution Results from Zephyr",
        		to: "${sendEmailTo}",
        		replyTo: "${sendEmailTo}",
        		recipientProviders: [[$class: 'CulpritsRecipientProvider']]	
         }
     }
}