
import time, random

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

"""This is the component of the Simon Says game that asks for user input
guesses to the interactive portion of the game, then continues if the 
guess matches the output until the game is completed."""

button_options = ["a", "b", "c", "d"]
character_string = random.choice(button_options)

def update_character_string():
	global character_string 
	character_string = character_string + random.choice(button_options)

def print_empty_lines(lines):
	print("\n" * lines)

def clear_screen():
	print_empty_lines(100)
	

def player_guess(guesses = 0):
	number_of_guesses = guesses

	clear_screen()
	print("The string to input is: " + character_string)
	time.sleep(0.5)

	print("Please input your guess:")
	input_player_guess = input()

	if input_player_guess == character_string:
		clear_screen()
		print("Good job!")
		time.sleep(.2)
		update_character_string()
	else:
		clear_screen()
		print("Try again!")
		time.sleep(0.2)
		clear_screen()
		if number_of_guesses < 2:
			number_of_guesses_remaining = 1 - guesses
			print("You have " + number_of_guesses_remaining + " guesses left!")
			player_guess(number_of_guesses + 1)
		else:
			print("You lose!")
			exit()
		
gameStart()

for guess in range(50):
	player_guess()


