@Library('shared-library') _

pipeline {
    agent any
    tools {
        maven "maven-standard"
    }
    
    
    stages {
        stage('Prepration') {
            steps {
                script {
                    sh 'echo Build is $BUILD_NUMBER'
                    sh 'env | sort'
                    
                    timer.showTime()
                }
            }
        }
        stage('Clean and Install') {
            steps {
               sh 'mvn clean install'
            }
        }
        stage('Package') {
            steps {
               sh 'mvn package'
            }
        } 
    }
}
