NSample = int(input("Enter number of samples: "))
Samples = [int(i) for i in input("Enter samples: ").split()]

NRanges = int(input("Enter number of Ranges: "))
Ranges = [[int(i) for i in input("Enter Range: ").split()] for i in range(NRanges)]

result = []

for i in Ranges:
    count = 0
    for j in Samples:
        if j in range(i[0], i[1]):
            count += 1
    result += [count]
    
print(result)
