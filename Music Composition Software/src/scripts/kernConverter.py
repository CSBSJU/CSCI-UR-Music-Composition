#Title: kernConverter
#Author: Andrew Joseph Heroux
#Version: June 7, 2019

import os
import json
import sys

global measureStruct


#adds a new measure to the opened json file
#the new measure will have one adjacency with the next measure
def addNewMeasure(measure):
	global measureStruct
	measureStruct.append({"measureSignature": measure, "adjacentMeasures": [], "adjacentMeasureFrequencies": []})

#itorates the adjacency of a specified 
#measure for the measure of index i in the json file
def addAdjacentMeasure(adjacentMeasure, i):
	global measureStruct
	adjacentMeasureIndex = findadjacentMeasure(adjacentMeasure, i)
	if adjacentMeasureIndex != -1:
		adjacencyCount = measureStruct[i]['adjacentMeasureFrequencies'][adjacentMeasureIndex] + 1
		measureStruct[i]['adjacentMeasureFrequencies'][adjacentMeasureIndex] = adjacencyCount
		print("'" + str(measureStruct[i]['measureSignature']) + "' is adjacent to '" + adjacentMeasure + "' " + str(adjacencyCount) + " times.\n")
	else:
		measureStruct[i]['adjacentMeasures'].append(adjacentMeasure)
		measureStruct[i]['adjacentMeasureFrequencies'].append(1)
		print("'" + str(measureStruct[i]['measureSignature']) + "' is adjacent to '" + adjacentMeasure + "' 1 time.\n")
	



def findadjacentMeasure(adjacentMeasure, j):
	i = 0
	global measureStruct
	for elem in measureStruct[j]['adjacentMeasures']:
		if elem == adjacentMeasure:
			return i
		i = i + 1
	return -1

def findElem(measure):
	i = 0
	global measureStruct
	for elem in measureStruct:
		if elem['measureSignature'] == measure:
			return i
		i = i + 1
	return -1

def makeMeasureStruct(orderSystem):
	global measureStruct
	fileName = "test.yaml"
	measureKern = os.popen("interval-script Medieval").read()
	print(measureKern)
	measureStruct = []
	lines = measureKern.split("\n")
	for x in range(len(lines) - orderSystem):

		signatureSequence = lines[x].rstrip('\n')

		for y in range(orderSystem - 1):
			signatureSequence = signatureSequence + "&&" + lines[x + y + 1].rstrip('\n')


		lineIndex = findElem(signatureSequence)
		if (lineIndex == -1):
			addNewMeasure(signatureSequence)
		if (lines[x + orderSystem].rstrip('\n') != ""):
			addAdjacentMeasure(lines[x + orderSystem].rstrip('\n'), lineIndex)

	fd = open('../jsonFiles/Rapunzel.json', 'w')
	json.dump(measureStruct, fd, indent = 2)
	fd.close()


def main():
	if len(sys.argv) <= 1:
		print("You must specify a command!")
	else:
		function = str(sys.argv[1])
		if function == "makeMeasureStruct":
			makeMeasureStruct(int(sys.argv[2]))
		else:
			print(function + " is not a valid command!")
main()



