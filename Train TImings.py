time = float(input("Enter the train start time: "))

hrs, mins = str(time).split(".")
hrs = int(hrs)
mins = int(mins)

if time >= 0 and hrs in range(24) and mins in range(60):
    addMinutes = [4, 9, 15, 24, 35]
    for i in range(len(addMinutes)):
        addMinutes[i] = (addMinutes[i] / 60) * 100
        
    mins = mins / 60 * 100
    
    for i in addMinutes:
        minutes = (mins + i)
        hr = (minutes // 100 + hrs) % 24
        minutes = round(((minutes % 100) * 60) / 100, 2) / 100
        res=str(hr+minutes)
        if hr<10: res='0'+res
        if len(res)<5: res+='0'
        print(res)
        
else:
    print("Invalid Input")
