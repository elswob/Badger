#!/bin/bash

date
grails prod run-script \
scripts/AddPublicationData.groovy \
scripts/AddSequenceData.groovy \
scripts/AddTransBlastData.groovy \
scripts/AddTransFuncAnnoData.groovy \
scripts/AddGeneBlastData.groovy \
scripts/AddGeneFuncAnnoData.groovy
date
