#!/bin/bash

date
grails prod run-script \
scripts/AlterTables.groovy \
scripts/AddFileData.groovy \
scripts/AddPublicationData.groovy \
scripts/AddSequenceData.groovy \
scripts/AddOrthoData.groovy \
scripts/AddTransBlastData.groovy \
scripts/AddTransFuncAnnoData.groovy \
scripts/AddGeneBlastData.groovy \
scripts/AddGeneFuncAnnoData.groovy
date
