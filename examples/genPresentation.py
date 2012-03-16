#!/usr/bin/python

import sys
sys.path.append("../../examples")

from generator import Sequence, Choice, Entry, Par
from generator import generate
from generator import defaultActivityHook
from generator import defaultChoiceHook

import random

def notifyHook(t, attrs):
    #Urgente = ["Yes", "No"]
    # Urgente = [True, False]
    # Urgente = [0, 1]
    # Urgente = [0.0, 1.0]
    #urg = Urgente[random.randint(0, len(Urgente)-1)]
    #attrs["urg"] = urg


    cittadinanza = ["Eu", "ExtraEu"]
    citt = cittadinanza[random.randint(0, len(cittadinanza)-1)]
    attrs["citt"] = citt

    dests = ["Italy", "UK", "USA", "Spain"] 
    dest = dests[random.randint(0, len(dests)-1)]
    attrs["dest"] = dest

    attrs["age"] = random.randint(1, 80)

    t0, t1 = defaultActivityHook(t, attrs)

    return t0,t1

def ConfermaHook(t, attrs ):
    giorni = ["lun", "mart", "merc", "giov", "ven", "sab", "dom" ]
    giorno = giorni[random.randint(0, len(giorni)-1)]
    attrs["giorno"] = giorno

    t0, t1 = defaultActivityHook(t, attrs)

    return t0,t1

def prenotaHook(t, attrs ):
	
    dests = ["Francia", "Germania", "Grecia", "Marocco"] 
    dest = dests[random.randint(0, len(dests)-1)]
    attrs["dest"] = dest

    t0, t1 = defaultActivityHook(t, attrs)

    return t0,t1

def choiceHook(l, attrs):
    #if  attrs["urg"] == "Yes":
    #    return l[1]
    #return l[0]

  if( attrs["age"] < 18) :
    return l[1] 

  elif (attrs["age"]  >= 18 ):
	if (attrs["citt"] == "ExtraEu" and ( attrs["dest"] == "USA" or attrs["dest"] == "UK")):
		return l[1]
  	else:
     		return l[0]
   #return defaultChoiceHook(l, attrs)

# process = Sequence([ Entry("Inizio", notifyHook),
#                       Choice([
#                           Sequence([ Entry("PrenotaViaggio", prenotaHook), 
#                                      Entry("Conferma", ConfermaHook)
#                                ]),

#                           Sequence([ Par([ Entry("PrenotaViaggio", prenotaHook), Entry("ProcVisto") ]),
# 				     Entry("Conferma", ConfermaHook)
				
# 			      ])
#                       ], choiceHook)
# 	          ])
process = Sequence([
        Entry("AvvioProcedimento"),
        Choice([
                Entry("RinnovoAutorizzazione"),
                Entry("NegaAutorizzazione"),
                Sequence([
                        Entry("Parere"),
                        Choice([
                                Sequence([                                
                                        Entry("ParerePositivo"),
                                        Entry("RilascioAutorizzazione")
                                        ]),
                                Sequence([                                
                                        Entry("ParereConRiserva"),
                                        Entry("RilascioAutorizzazione")
                                        ]),
                                Sequence([                                
                                        Entry("ParereNegativo"),
                                        Entry("NegaAutorizzazione")
                                        ])
                                ])
                        ])
                ])
        ])
generate(process, 200)
