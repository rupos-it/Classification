#!/bin/bash


CPATH=./bindist/
CPATH=$CPATH:../BPMNMeasures/lib/saxon9he.jar
#CPATH=$CPATH:../BPMNMeasures/lib/saxon9he.jar
#CPATH=$CPATH:../BPMNMeasures/lib/saxon9he.jar

directory1=stdlib/
directory2=packagelib/

 for file in "$directory1"*.jar
  do
  	MOVFile=`basename $file`
	echo "$file"
	#echo "$MOVFile"
 	
 	CPATH=$CPATH:$file

  done
for file in "$directory2"*.jar
  do
  	MOVFile=`basename $file`
	echo "$file"
	#echo "$MOVFile"
 	
 	CPATH=$CPATH:$file

  done
echo "$CPATH"
 xattr -d com.apple.quarantine runGui.sh 
java -cp $CPATH org.processmining.contexts.uitopia.UI
