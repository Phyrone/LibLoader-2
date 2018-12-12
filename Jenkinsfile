pipeline {
  agent any
  stages {
    stage('Setversion') {
      steps {
        sh 'mvn versions:set -DnewVersion=2.0-SNAPSHOT'
      }
    }
    stage('Clean') {
      steps {
        sh 'mvn clean'
      }
    }
    stage('Compile') {
      steps {
        sh 'mvn compile'
      }
    }
    stage('Package') {
      steps {
        sh 'mvn package'
      }
    }
    stage('Archive') {
      steps {
        archiveArtifacts(artifacts: '**/*.jar', excludes: 'orginal-*.jar')
      }
    }
  }
  tools {
    maven 'Maven3'
    jdk 'Java8'
  }
}