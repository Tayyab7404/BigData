import math
time = 19.34
l=[4,9,15,24,35]
for i in range(len(l)):
    l[i]=(l[i]/60)*100
hrs, mins = str(time).split(".")

mins=(int(mins)/60)*100
hrs = int(hrs)
result = []
for i in l:
    minutes= (mins+i)
    hr= minutes//100
    hr+=hrs
    hr%=24
    minutes = round(((minutes%100)*60)/100,2)/100
    print(hr+minutes)
    
