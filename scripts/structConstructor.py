#Title: structConstructor
#Author: Andrew Joseph Heroux
#Date: July 15, 2019
#Version: 2

import os
import json
import sys
import bisect

global measureStruct
global lines


def nextIndex(index):
	index += 1
	if index == len(lines):
		return 0
	return index


def getAdjacentToken(orderSytem, startIndex):
	index = startIndex
	if orderSytem + startIndex >= len(lines):
		for i in range(orderSytem):
			index = nextIndex(index)
	else:
		index = orderSytem + startIndex
	return lines[index]


def tokenCoupler(orderSytem, startIndex):
	i = 1
	signature = lines[startIndex].rstrip("END_TOKEN").lstrip("START_TOKEN")
	startIndex = nextIndex(startIndex)
	while i < orderSytem:
		signature += "&&" + lines[startIndex].rstrip("END_TOKEN").lstrip("START_TOKEN")
		startIndex = nextIndex(startIndex)
		i += 1
	return signature


def addAjdacentStruct(signature, adjacentSignature):
	try:
		measureStruct[signature]["adjacentSignaturs"][adjacentSignature.rstrip("END_TOKEN").lstrip("START_TOKEN")] += 1
	except KeyError:
		measureStruct[signature]["adjacentSignaturs"][adjacentSignature.rstrip("END_TOKEN").lstrip("START_TOKEN")] = 1


def addStruct(signature):
	try:
		measureStruct[signature]
	except KeyError:
		measureStruct[signature] = {}
		measureStruct[signature]["startEnd"] = "none"
		measureStruct[signature]["adjacentSignaturs"] = {}


def addStartEnd(startEnd, signatureSequence):
	currentStartEnd = measureStruct[signatureSequence]["startEnd"]
	if currentStartEnd != "both":
		if currentStartEnd == "none":
			measureStruct[signatureSequence]["startEnd"] = startEnd
			return
		elif currentStartEnd == "start":
			if startEnd == "end":
				measureStruct[signatureSequence]["startEnd"] = "both"
				return
			return
		elif currentStartEnd == "end":
			if startEnd == "start":
				measureStruct[signatureSequence]["startEnd"] = "both"
				return
			return


def checkStartEnd(signature):
	if signature.startswith("START_TOKEN"):
		return "start"
	elif signature.endswith("END_TOKEN"):
		return "end"
	return "none"


def makeStruct(orderSytem, script, era):
	global lines
	global measureStruct
	measureStruct = {}

	if script == "i":
		bashScript = "interval-script"
	elif script == "m":
		bashScript = "measure-script"
	elif script == "t":
		bashScript = "text-script"
	else:
		print(script + " is not a valid script!")


	lines = os.popen(bashScript +  " " + era).read().split('\n')

	for i in range(len(lines)):
		signatureSequence = tokenCoupler(orderSytem, i)
		adjacentSignature = getAdjacentToken(orderSytem, i)

		

		addStruct(signatureSequence)
		startEnd = checkStartEnd(lines[i])
		if startEnd != "none":
			addStartEnd(startEnd, signatureSequence)
		addAjdacentStruct(signatureSequence, adjacentSignature)


	era = era.split(".", 1)[0]

	try:
		os.mkdir('../jsonFiles/' + era)
	except FileExistsError:
		pass
	fd = open('../jsonFiles/' + era + '/' + script + '_' + era + '_order' + str(orderSytem) + '.json', 'w')
	json.dump(measureStruct, fd, indent=2)
	fd.close()




	
def main():
	if len(sys.argv) <= 1:
		print("You must specify a command!")
	else:
		function = str(sys.argv[1])
		if function == "makeStruct":
			if len(sys.argv) >= 5:
				makeStruct(int(sys.argv[2]), str(sys.argv[3]), str(sys.argv[4]))
			else:
				print("Order System, (i)interval/(m)measure script, Directory")
		elif function == "makeStructs":
			if len(sys.argv) >= 6:
				for i in range(5, len(sys.argv)):
					for j in range(int(sys.argv[2]), (int(sys.argv[3]) + 1)):
						makeStruct(j, str(sys.argv[4]), sys.argv[i])
		else:
			print(function + " is not a valid command!")


main()