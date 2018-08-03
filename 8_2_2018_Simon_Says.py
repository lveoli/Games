import time

#starts the game by asking if the user is ready and will only proceed when yes
def gameStart():
	userInput = input("Are you ready to play Simon Says on the command line? (Yes/No): ").lower()
	if userInput == "yes":
		print ("Great! Let's begin")
	elif userInput == "no": 
		print ("Okay, but let me know if you change your mind! Bye!")
	else:
		print ("Incorrect input please enter either 'Yes', or 'No'") 
		gameStart()

#used to prompt the user for an answer to the given pattern
def simonSays(answer):
	userInput = input("type in what Simon said: ")
	if userInput == answer: 
		print ("Congrats! You are correct!")
		print ("Now for the next challenge:")
	else:
		print ("Not quite. Here, try again:")

gameStart()




