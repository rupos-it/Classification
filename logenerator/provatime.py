#!/usr/bin/python
import random
import time

t = time.time() + 3600 * 0
t1 = time.strftime("%Y-%m-%dT%H:%M:%S.000+01:00", time.gmtime(t))
print (""" 
	<tempo> %s </tempo>
	<tempo> %s </tempo>
 """ % (t, t1)
)

t = time.time() + 3600 * 1
t1 = time.strftime("%Y-%m-%dT%H:%M:%S.000+01:00", time.gmtime(t))
print (""" 
	<tempo> %s </tempo>
	<tempo> %s </tempo>
 """ % (t, t1)
)


t = time.time() + 3600 * 2
t1 = time.strftime("%Y-%m-%dT%H:%M:%S.000+01:00", time.gmtime(t))
print (""" 
	<tempo> %s </tempo>
	<tempo> %s </tempo>
 """ % (t, t1)
)
