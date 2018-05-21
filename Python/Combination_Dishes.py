import math
import csv
import sys


price_combination=[]
def backtrack(prices,count_of_items,buffer_array,position,level,target_price):
    if target_price==0.000: # if sum of prices gathered till now in buffer equals to target, add the combination to final list
        price_combination.append(buffer_array[0:level])
        return
    if target_price<0: # if less than 0, return - not a right combination as it exceeded the target price 
        return
    for i in range(position,len(prices)): # iterating over the list in consideration for this recursion depth
        buffer_array.insert(level,prices[i]) # add to the res. buffer 
        count_of_items[i]=count_of_items[i]-1 # reduce the count of availability of item
        backtrack(prices,count_of_items,buffer_array,i,level+1,round(target_price-prices[i],3)) # continue with the next level of iteration 
        count_of_items[i]=count_of_items[i]+1 # going back to previous state 

def find_combinations(prices,target_price):
    count_of_items=[]
    for i in range(len(prices)):
        count_of_items.append(math.inf) # since every item can be considered n number of times, taking their count as infinity
    buffer_array = [None] * 10 # temporary buffer space to capture result at every level of backtracking
    backtrack(prices,count_of_items,buffer_array,0,0,target_price) # start processing the list of prices 


# if no file is given as command line argument to price, print the message and exit
if(len(sys.argv)<2):
	print("Please provide File Name as the Command Line Argument python <file-name>.py <Data-File>.csv")
	sys.exit(1)

try :
	with open(sys.argv[1]) as inputfile:
	   input_data = csv.reader(inputfile, delimiter=',') #read from the file
	   rowcount=0 #to keep track of the row count 
	   items_prices={} #map of prices (keys) & items ( values)
	   prices=[] # list of prices 
	   del(price_combination[0:]) # clearing the elements in array, if any
	   for row in input_data:
	       if(rowcount==0):
		       target_price=float((row[0].split(",")[1])[2:]) # if first row, capture the target price using ',' delimiter and type casting to float. Indexing from [2:] as there is space after ',' before '$'
		       rowcount=rowcount+1
	       elif(rowcount==1): # since empty line is given in the example data format 
		       rowcount=rowcount+1
		       continue
	       else:
		       row_list=row[0].split(",") # splitting the line content using ','
		       price=float((row_list[1])[1:]) # capturing price as second element in the element type casting to float 
		       prices.append(price) # appending to the list of prices maintained
		       items_prices[price]=row_list[0] # creating a dictionary with prices as keys and items as values
		       rowcount=rowcount+1
except IOError:
	print("File Not Found") # wrong file
	sys.exit(1)
except IndexError:
	print("Corrupted Data Format") # missing data 
	sys.exit(1)
except ValueError:
	print("Corrupted Data Format") # wrong format data 
	sys.exit(1)

final_combinations=[]
find_combinations(prices,target_price) # to find combinations of prices summing to target using backtracking 

if(len(price_combination)<1):
	print("No combination of dishes equal to Target price is found") # if no combination found 
	sys.exit(1)

for list_prices in price_combination:
    combination=[]
    for single_price in list_prices:
        combination.append(items_prices[single_price]) # making the list of items depending on the combination of prices returned
    final_combinations.append(combination)

res_file = open('final_combinations.csv', 'w') # writing to CSV the final list of items
with res_file:
    writer = csv.writer(res_file)
    writer.writerows(final_combinations)
    
         