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
                        sh 'git config --global user.email "onchist@gmail.com"'
                        sh 'git config --global user.name "onchist"'
                        sh 'version=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)'
                        sh 'void=""'
                        sh "versionStripped=${version/-SNAPSHOT/$void}"
                        sh "git branch release/$versionStripped"
                        sh "git push origin release/$versionStripped"
                        sh 'mvn release:prepare -gs $MAVEN_GLOBAL_SETTINGS -B -Dusername=$USERNAME_VAR -Dpassword=$PASSWORD_VAR'
                        sh 'mvn release:perform -gs $MAVEN_GLOBAL_SETTINGS -B -Dusername=$USERNAME_VAR -Dpassword=$PASSWORD_VAR'
                    }
                    }
            }
        }
    }
}
