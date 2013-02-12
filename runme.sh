#!/bin/bash

date
grails -Dgrails.env=data_add run-script \
scripts/AddFileData.groovy \
scripts/AddPublicationData.groovy \
scripts/AddSequenceData.groovy \
scripts/AddOrthoData.groovy \
scripts/AddTransBlastData.groovy \
scripts/AddTransFuncAnnoData.groovy \
scripts/AddGeneBlastData.groovy \
scripts/AddGeneFuncAnnoData.groovy
date
