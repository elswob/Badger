#!/bin/bash

date
grails prod run-script \
scripts/groovy/AlterTables.groovy \
scripts/groovy/AddPublicationData.groovy \
scripts/groovy/AddSequenceData.groovy \
scripts/groovy/AddOrthoData.groovy \
scripts/groovy/AddGeneBlastData.groovy \
scripts/groovy/AddGeneFuncAnnoData.groovy \
scripts/groovy/CreateFiles.groovy
date
