#!/usr/bin/python
# -*- coding: latin-1 -*-

from generator import Sequence, Choice, Entry, Par
from generator import generate
from generator import defaultActivityHook

import random


# def docActivityHook(startTime, attrs):
#     t = startTime


#     # delta1 = random.randint(1, 30*60)		#sceglie un tempo da 0 a 30 minuti (convertito in secondi). Tempo di inizio
#     delta1 = 15
   
#     #delta2 = random.randint(delta1+1, 59*60)	sceglie un tempo tra delta1 e un'ora dopo (convertito in secondi). Tempo di fine

#     if( attrs["destinazione"] == "EXTRAEU" ):
# 	delta2 = (delta1+1) + (45*60)
#     else:
# 	delta2 = (delta1+1) + (25*60)

#     t0 = t+delta1				# tempo di inizio dato dal startTime più delta1
#     t1 = t+delta2				# tempo di fine dato dal starTime più delta2.

#     return t0, t1

# def bagActivityHook(startTime,attrs):
	
# 	t = startTime

#     	#delta1 = random.randint(1, 30*60)		#sceglie un tempo da 0 a 30 minuti (convertito in secondi). Tempo di inizio
# 	delta1 = 15
	
# 	delta2 = (delta1+1) + (20*60)  

# 	t0 = t+delta1				# tempo di inizio dato dal startTime più delta1
#     	t1 = t+delta2				# tempo di fine dato dal starTime più delta2.

#     	return t0, t1

# def docHook(t, attrs):

# 	t0, t1 = docActivityHook(t,attrs)

# 	return t0,t1

# def bagHook(t,attrs):

# 	t0, t1 = bagActivityHook(t,attrs)
	
# 	return t0, t1

def orderHook(t, attrs):
    products = ["bottles", "boxes", "equipment"]
    attrs["product"] = products[random.randint(0, len(products)-1)]
    customers = ["A", "B", "C"]
    clientTypes = ["consolidate", "new"]
    salesmanagers = ["Marc", "Johann", "Mary"]
    financialOfficiers = ["Alexander" , "Robert", "Mario"]
    warehousemen = ["Alex"]
    orderResults = ["positive", "negative"]


    attrs["customer"] = customers[random.randint(0, len(customers)-1)]
    users = ["U1", "U2", "U3"]
    attrs["user"] = users[random.randint(0, len(users)-1)]
    attrs["amount"] = random.randint(0, 1000)
    attrs["clientType"] = clientTypes[random.randint(0, len(clientTypes)-1)]
    attrs["salesmanager"] = salesmanagers[random.randint(0, len(salesmanagers) -1)]
    attrs["financialOfficier"] = financialOfficiers[random.randint(0, len(financialOfficiers)-1)]
    attrs["warehouseman"] = warehousemen[random.randint(0, len(warehousemen)-1)]
    attrs["orderResult"] = orderResults[random.randint(0, len(orderResults)-1)]


    t0, t1 = defaultActivityHook(t, attrs)

    return t0,t1

def financialHook(t, attrs):
    t0, t1 = defaultActivityHook(t, attrs)

    min_t = 0.5 if attrs["customer"] == "A" or attrs["customer"] == "B" else \
        1.0
    max_t = 1.0 if attrs["customer"] == "A" or attrs["customer"] == "B" else \
        3.0

    ratio = 1 if attrs["amount"] < 300 else \
        2 if attrs["amount"] < 700 else \
        3

    min_t *= ratio
    max_t *= ratio

    delta1 = random.randint(min_t * 60*60, max_t * 60*60)
    return t0, t1 + delta1
    

def warehouseHook(t, attrs):
    t0, t1 = defaultActivityHook(t, attrs)

    min_t = 0.5 if attrs["product"] == "boxes" else \
        1.0
    max_t = 1.0 if attrs["product"] == "boxes" else \
        3.0

    ratio = 1 if attrs["amount"] < 700 else \
        2

    min_t *= ratio
    max_t *= ratio

    delta1 = random.randint(min_t * 60*60, max_t * 60*60)
    return t0, t1 + delta1

def externalHook(t, attrs):
    t0, t1 = defaultActivityHook(t, attrs)

    min_t = 0.5 if attrs["user"] == "U1" else \
        1.0
    max_t = 1.0 if attrs["user"] == "U2" else \
        3.0

    ratio = 1 if attrs["amount"] < 500 else \
        2

    min_t *= ratio
    max_t *= ratio

    delta1 = random.randint(min_t * 60*60, max_t * 60*60)
    return t0, t1 + delta1

#def choiceHook(l, attrs):
    #if  attrs["urg"] == "Yes":
    #    return l[1]
    #return l[0]
#  if  ( attrs["product"] == "equipment" ):
  #      return l[1]
 # return l[0]

def choiceHook(l, attrs):
  if  ( attrs["salesmanager"] == "Mary" and attrs["clientType"] == "consolidate"):
        return l[1]
  return l[0]


process = Sequence([ 
		Entry("Order", orderHook),
		Choice([
			Sequence([
				Par([ Entry("FinancialCheck", financialHook),
				      Choice([
				        Entry("WarehouseCheck", warehouseHook),
				        Sequence([
				                Entry("WarehouseCheck", warehouseHook),
				                Entry("ExternalWarehouse", externalHook)
				                ])
				        ])
				      ]),
				Entry("Notify") 
				]),
			Sequence([ Choice([
				        Entry("WarehouseCheck", warehouseHook),
				        Sequence([
				                Entry("WarehouseCheck", warehouseHook),
				                Entry("ExternalWarehouse", externalHook)
				                ])
				        ]),
				 Entry("Notify")
				])
			
			],choiceHook)
		])
	

generate(process, 250)
