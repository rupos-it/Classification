#!/usr/bin/python
# -*- coding: latin-1 -*-

from generator import Sequence, Choice, Entry, Par
from generator import generate
from generator import defaultActivityHook

import random


def docActivityHook(startTime, attrs):
    t = startTime


    # delta1 = random.randint(1, 30*60)		#sceglie un tempo da 0 a 30 minuti (convertito in secondi). Tempo di inizio
    delta1 = 15
   
    #delta2 = random.randint(delta1+1, 59*60)	sceglie un tempo tra delta1 e un'ora dopo (convertito in secondi). Tempo di fine

    if( attrs["destinazione"] == "EXTRAEU" ):
	delta2 = (delta1+1) + (45*60)
    else:
	delta2 = (delta1+1) + (25*60)

    t0 = t+delta1				# tempo di inizio dato dal startTime più delta1
    t1 = t+delta2				# tempo di fine dato dal starTime più delta2.

    return t0, t1

def bagActivityHook(startTime,attrs):
	
	t = startTime

    	#delta1 = random.randint(1, 30*60)		#sceglie un tempo da 0 a 30 minuti (convertito in secondi). Tempo di inizio
	delta1 = 15
	
	delta2 = (delta1+1) + (20*60)  

	t0 = t+delta1				# tempo di inizio dato dal startTime più delta1
    	t1 = t+delta2				# tempo di fine dato dal starTime più delta2.

    	return t0, t1

def imbarcoHook(t, attrs):
    #Urgente = ["Yes", "No"]
    # Urgente = [True, False]
    # Urgente = [0, 1]
    # Urgente = [0.0, 1.0]
    #urg = Urgente[random.randint(0, len(Urgente)-1)]
    #attrs["urg"] = urg

	
    Destinazione = ["EU", "EXTRAEU"]
    destinazione = Destinazione[random.randint(0, len(Destinazione)-1)]
    attrs["destinazione"] = destinazione

    t0, t1 = defaultActivityHook(t, attrs)

    return t0,t1

def docHook(t, attrs):

	t0, t1 = docActivityHook(t,attrs)

	return t0,t1

def bagHook(t,attrs):

	t0, t1 = bagActivityHook(t,attrs)
	
	return t0, t1

process = Sequence([ 
		Entry("ImbarcoInit", imbarcoHook),		
		Par([ Entry("ControlloDoc", docHook), Entry("ControlloBag", bagHook) ]),
		Entry("ImbarcoFin") 
		])

		

generate(process, 1)
