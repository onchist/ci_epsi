pipeline {
    agent any
    parameters {
        booleanParam(name: 'Perform release ?', defaultValue: false)
    }
    stages {
        stage('Initialize') {
            steps {
                sh '''
                    echo "PATH = ${PATH}"
                    echo "M2_HOME = ${M2_HOME}"
                    '''
            }
        }
        stage('Build') {
            steps {
                sh 'mvn compile'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
        stage('Deploy') {
            steps {
                configFileProvider(
                    [configFile(fileId: 'f8069f73-6367-4fc5-a6d6-813eb424b54d', variable: 'MAVEN_GLOBAL_SETTINGS')]
                    ) {
                    sh 'mvn -gs $MAVEN_GLOBAL_SETTINGS deploy'
                    }
            }
        }
        stage('Release') {
            when { expression { params['Perform release ?'] } }
            steps {
                configFileProvider(
                    [configFile(fileId: 'f8069f73-6367-4fc5-a6d6-813eb424b54d', variable: 'MAVEN_GLOBAL_SETTINGS')]
                    ) {
                    withCredentials([usernamePassword(credentialsId: 'github', passwordVariable: 'PASSWORD_VAR', usernameVariable: 'USERNAME_VAR')]) {
                        bat 'git config --global user.email "onchist@gmail.com"'
                        bat 'git config --global user.name "onchist"'
                        bat 'git branch release/' + pom.version.replace('-SNAPSHOT', '')
                        bat 'git push origin release/' + pom.version.replace('-SNAPSHOT', '')
                        bat 'mvn release:prepare -gs $MAVEN_GLOBAL_SETTINGS -B -Dusername=$USERNAME_VAR -Dpassword=$PASSWORD_VAR'
                        bat 'mvn release:perform -gs $MAVEN_GLOBAL_SETTINGS -B -Dusername=$USERNAME_VAR -Dpassword=$PASSWORD_VAR'
                    }
                    }
            }
        }
    }
}
