#!/usr/bin/python

import sys
sys.path.append("../../examples")

from generator import Sequence, Choice, Entry, Par
from generator import generate
from generator import defaultActivityHook
from generator import defaultChoiceHook

import random

def startHook(t, attrs):
    cittadinanza = ["Eu", "ExtraEu"]
    citt = cittadinanza[random.randint(0, len(cittadinanza)-1)]
    attrs["citt"] = citt

    tipi = ["Bar", "Farmacia", "AntennaUMTS"] 
    tipo = tipi[random.randint(0, len(tipi)-1)]
    attrs["tipo"] = tipo

    t0, t1, a = defaultActivityHook(t, attrs)

    return t0,t1, ["citt", "tipo"]


def choiceHook1(l, attrs):
  if( attrs["tipo"] != "AntennaUMTS"):
      return defaultChoiceHook(l, attrs)
  if( attrs["citt"] != "ExtraEu"):
      return defaultChoiceHook(l, attrs)
  return l[-1]

def choiceHook2(l, attrs):
  if( attrs["tipo"] != "AntennaUMTS"):
      return defaultChoiceHook(l[:-1], attrs)
  if( attrs["citt"] != "ExtraEu"):
      return defaultChoiceHook(l[:-1], attrs)
  return l[-1]

process = Sequence([
        Entry("AvvioProcedimento", startHook),
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
                                        ]),
                                Sequence([
                                        Entry("ParereConRiserva"),
                                        Entry("RilascioAutorizzazione"),
                                        Entry("ParereConRiserva"),
                                        ])
                                ], choiceHook2)
                        ])
                ], choiceHook1)
        ])
generate(process, 100)
