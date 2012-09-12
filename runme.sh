#not can run multiple scripts
# run multiple scripts in the dev environment
#grails run-script userScripts/someScript.groovy userScripts/otherScript.groovy

grails prod run-script scripts/AddPublicationData.groovy \
scripts/AddSequenceData.groovy \
scripts/AddTransBlastData.groovy \
scripts/AddGeneBlastData.groovy \
scripts/AddTransFuncAnnoData.groovy 
