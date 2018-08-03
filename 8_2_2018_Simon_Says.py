import time


def gameStart():
	text = input("Are you ready to play Simon Says on the command line? (Yes/No): ").lower()
	if text == "yes":
		print ("Great! Let's begin")
	elif text == "no": 
		print ("Okay, but let me know if you change your mind! Bye!")
	else:
		print ("Incorrect input please enter either 'Yes', or 'No'") 
		gameStart()

gameStart()




