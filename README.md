# TICS Jenkins plugin
The TICS Jenkins plugin adds two independent actions to Jenkins that can be used in your Jenkins jobs:

* [Run TICS](#run-tics) is a build step that invokes TICS as part of your Jenkins job. Although you could also run TICS via the "Execute shell" build step that is part of the standard Jenkins installation, the Run TICS step helps you to set the most commonly used TICS options in an easy-to-use UI.
* [Publish TICS](#publish-tics) Results is a post-build step that retrieves TQI scores and deltas from the TICS viewer and puts a TQI statement on the Jenkins job front-page. Note that you do not have to add a Run TICS build step in order to use the Publish TICS Results build step.
Both steps can be invoked through the classic Jenkins UI or through pipelines. Pipeline syntax for declarative pipelines is explained [below](#tics-through-declarative-pipelines).

Before you can use either step you need to install the TICS Jenkins plugin.

# Run TICS
This build step runs TICS as part of your Jenkins job. Requirements:

* You need to have TICS installed on the machine on which you want to run the job.

To configure this build step:

* Click the "Add build step" button in your Jenkins job of the project you want to analyze and select "Run TICS"
* Provide the required settings. Click the question mark icon for each option to get help.
* More advanced options become available when you click on the "Advanced..." button.
* Save the settings and choose Build Now to execute the job.

# Publish TICS
![Jenkins-TICS-Plugin-Project](/src/main/webapp/jenkins-tics-plugin-project.png)
Requirements:

* You need to have a TICS Viewer running somewhere on the network that is HTTP accessible by the machine on which you want to run the job.

To configure this post-build step:

* Click the "Add post-build action" button in your Jenkins job of the project you want to get results for and select "Publish TICS results"
* Provide the required settings. Click the question mark icon for each option to get help.
* Save the settings and choose Build Now to execute the job.

# TICS through declarative pipelines
Below is an example of executing a Run TICS build step through declarative pipelines. The syntax of scripted pipelines is similar. For more information on the differences between scripted and declarative pipelines please refer to this article: https://jenkins.io/doc/book/pipeline/


      pipeline {
        agent any
          environment {
                // Environment variables defined in this block will not be passed to the TICS plugin during a TICS analysis. This is due to a Jenkins issue (JENKINS-29144).
                // Please define any environment variables as part of the environmentVariables parameter, which is shown below.
                ...
          }
          stages {
            stage('Run Tics') {
              steps {
                runTics (
                  // If 'installTics' is set to true then the TICS command-line tools will be installed.
                  // It is available from TICS Viewer version 2021.4.x and above.
                  // For older versions, TICS should be installed manually on the machine that runs this job.
                  installTics: true,                              // Optional boolean parameter

                  // If 'installTics' is set to true then the TICS configuration should also be set.
                  // Τhe TICS configuration is a URL pointing to the 'cfg' API endpoint of the TICS Viewer.
                  // This URL contains the name of the TICS Analyzer Configuration or '-' in case of the default configuration.
                  // For TICS installations using the legacy deployment architecture, TICS Configuration points to the configuration directory
                  // e.g. C:\Program Files\TIOBE\TICS\FileServer\cfg
                  ticsConfiguration: 'https://192.168.1.1/tiobeweb/TICS/api/cfg?name=-',       // Optional parameter

                  // The 'credentialsId' is used to specify the credentials to be used when accessing the viewer.
                  // It is only needed if TICS Viewer is not publicly accessible and requires an authentication token.
                  // It is managed by the credentials plugin: https://plugins.jenkins.io/credentials
                  // Through the credentials plugin, create a credential of type 'Secret Text', with the 'ID' of your choice and 
                  // the 'Secret' to be equal to a TICS Viewer Authentication token of role 'TICS Analyzer' and 
                  // configure the 'credentialsId' below accordingly.
                  credentialsId: '',                                             // Optional parameter

                  projectName: 'projectName',                     // Mandatory parameter (case sensitive)
                  branchName: 'master',                           // Mandatory parameter (case sensitive)

                  calc: ['INCLUDERELATIONS', 'PREPARE', 'LOC'],   // Optional parameter. Example of metrics that will be analyzed with 'calc'.
                  recalc: ['ABSTRACTINTERPRETATION', 'SECURITY'], // Optional parameter. Example of metrics that will be analyzed with 'recalc'.

                  // ticsBin is an Optional parameter.
                  // This parameter can be skipped, if the TICS executables (TICSQServer) can be found in the PATH environment variable,
                  // or if 'installTics' is set to true.
                  ticsBin: 'C:/Program Files/TIOBE/TICS/BuildServer',
                  environmentVariables: [                                                   // Optional parameter
                      "TICSCHKPATH" : "C:/Program Files/TIOBE/TICS/FileServer/chk",
                      "PATH": "C:\Tools\bin;$PATH"                                          // Example of how to insert a new path at the beginning of $PATH environment variable.
                      ...
                  ],
                  extraArguments: '',                                                       // Optional parameter
                  tmpdir: '',                                                               // Optional parameter
                  branchDirectory: "${WORKSPACE}",                                          // Optional parameter. ${WORKSPACE} points to the default Jenkins workspace location.
                )
              }
            }
          }
          // Other 'stages'.
      }
        
Below is an example of executing Publish TICS results (TQI label) post build step through declarative pipelines:


      pipeline {
        agent any
          stages {
            stage('Publish results') {
              steps {
                publishTicsResults (
                  projectName: 'projectName',                               // Mandatory parameter (case sensitive)
                  branchName: 'master',                                     // Mandatory parameter (case sensitive)
                  viewerUrl: 'http://www.company.com:42506/tiobeweb/TICS',  // Mandatory parameter
                  checkQualityGate: false,                                  // Optional boolean parameter that defaults to false if not set. Enables TICS Quality Gate checks.
                  failIfQualityGateFails: false,                            // Optional boolean parameter that defaults to false if not set. Marks the build as failure if TICS Quality Gate fails for any reason.

                  // 'credentialsId' is used to specify the credentials to be used when accessing the viewer.
                  // It is only needed if the project specified in projectPath requires authentication.
                  // This is managed by the credentials plugin: https://plugins.jenkins.io/credentials
                  // For this step two credentials types are supported: the 'Username with password' or 'Secret Text'.
                  // For a credential of type 'Secret Text', the 'ID' is one of your choice and 
                  // the 'Secret' should be equal to a TICS Viewer Authentication token of role 'TICS Analyzer'.
                  credentialsId: '',                                        // Optional parameter

                  // Advanced parameters:
                  ticsProjectPath: 'HIE://PROJECT/BRANCH/COMPONENT',        // Optional parameter that can be used instead of 'projectName' and 'branchName'
                )
              }
            }
            // Other 'stages'.
        }
      }

Notes on pipelines

* The available metrics that can be given as input to 'calc'/'recalc' for the TICS analysis ('runTics') through scripted/declarative pipelines are:

      ABSTRACTINTERPRETATION, ACCUCHANGERATE, ACCUFIXRATE, ACCULINESADDED, ACCULINESCHANGED, ACCULINESDELETED, AI, ALL,
      AVGCYCLOMATICCOMPLEXITY, BEGIN, BUILDRELATIONS, CHANGEDFILES, CHANGERATE, CODINGSTANDARD, COMPILERWARNING, CS, CW, CY, 		
      CYCLOMATICCOMPLEXITY, DEADCODE, DUP, DUPLICATEDCODE, DUPLICATEDCODECUSTOM, ELOC, END, FANOUT, FANOUT_INTEXT, FINALIZE, FIXRATE, 		
      GLOC, HIS_AVGCYCLOMATICCOMPLEXITY, HIS_CALLLEVELS, HIS_CALLLEVELS_MAX, HIS_CODINGSTANDARD, HIS_COMMENTDENSITY, HIS_FUNCCALLS, 		
      HIS_FUNCCALLS_MAX, HIS_FUNCCYCLE, HIS_FUNCCYCLE_MAX, HIS_FUNCSIZE, HIS_FUNCSIZE_MAX, HIS_GOTOSTATEMENTS, 		
      HIS_GOTOSTATEMENTS_MAX, HIS_MAXCYCLOMATICCOMPLEXITY, HIS_PARAMCOUNT, HIS_PARAMCOUNT_MAX, HIS_PATHCOUNT, HIS_PATHCOUNT_MAX, 		
      HIS_RETURNPOINTS, HIS_RETURNPOINTS_MAX, HIS_VOCF, INCLUDERELATIONS, INTEGRATIONBRANCHCOVERAGE, INTEGRATIONDECISIONCOVERAGE, 		
      INTEGRATIONFUNCTIONCOVERAGE, INTEGRATIONSTATEMENTCOVERAGE, INTEGRATIONTESTCOVERAGE, ITC, LINESADDED, LINESCHANGED, 		
      LINESDELETED, LOC, MAXCYCLOMATICCOMPLEXITY, PATHCOUNT, PATHCOUNT_MAX, POSTANA, PREPARE, SEC, SECURITY, STC, 		
      SYSTEMBRANCHCOVERAGE, SYSTEMDECISIONCOVERAGE, SYSTEMFUNCTIONCOVERAGE, SYSTEMSTATEMENTCOVERAGE, SYSTEMTESTCOVERAGE, 		
      TOTALBRANCHCOVERAGE, TOTALDECISIONCOVERAGE, TOTALFUNCTIONCOVERAGE, TOTALSTATEMENTCOVERAGE, TOTALTESTCOVERAGE, TTC, 		
      UNITBRANCHCOVERAGE, UNITDECISIONCOVERAGE, UNITFUNCTIONCOVERAGE, UNITSTATEMENTCOVERAGE, UNITTESTCOVERAGE, UTC

      
* If the stage/steps are not defined correctly, and the TICS analysis and/or TICS Publish fails for any reason, **an exception will be thrown**, and the entire pipeline run will be stopped. If you want for the pipeline run to continue, even if TICS fails to publish/run, you can surround the TICS stage/steps with a try/catch block. For example:

          try {
            stage('Publish results') {
              publishTics (
                // arguments
              )
          }
          catch (e) {
            // continue execution
          }

* If you are using Credentials Binding Plugin along with TICS Jenkins plugin, please be aware of the following security issue. 
  In a Groovy string, any secrets include in the string will be interpolated before being processed for further use. This can allow other processes to accidentally expose the secret. For example: 

  ```
     // Insecure way
     node {
       withCredentials([string(credentialsId: 'mytoken', variable: 'TOKEN')]) {
           sh '''
             set +x
             curl -H "Token: $TOKEN" https://some.api/
           '''
       }
     }
   ```
  To avoid this issue, the secrets should be used in single quotes so that they are expanded by the shell as an environment variable. For example:

   ```
     // Secure way
     node {
       withCredentials([string(credentialsId: 'myid', variable: 'TOKEN')]) {
           sh '''
             set +x
             curl -H "Token: $TOKEN" https://some.api/
           '''
       }
     }
   ```
  For more details please consult the [Credentials Binding Plugin](https://www.jenkins.io/doc/pipeline/steps/credentials-binding/) documentation page.
