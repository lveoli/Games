import time


def askInput():
	text = input("are you ready????: ").lower()
	if text == "yes":
		print ("cool")
	elif text == "no": 
		print ("okay byeeee")
	else:
		print ("incorrect input please try again") 
		askInput()

askInput()



