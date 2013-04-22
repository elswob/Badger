#!/bin/bash

date
grails prod run-script \
scripts/AlterTables.groovy \
scripts/AddFileDataLrub.groovy \
scripts/AddPublicationData.groovy \
scripts/AddSequenceData.groovy \
scripts/AddOrthoData.groovy \
scripts/AddGeneBlastData.groovy \
scripts/AddGeneFuncAnnoData.groovy \
scripts/CreateFiles.groovy
date
