"""This is the component of the Simon Says game that asks for user input
guesses to the interactive portion of the game, then continues if the 
guess matches the output until the game is completed."""

import random, time

button_options = ["a", "b", "c", "d"]
character_string = random.choice(button_options)

def update_character_string():
	character_string = character_string + random.choice(button_options)

def player_guess(guesses = 0):
	global character_string 
	number_of_guesses = guesses

	print("The string to input is: " + character_string)
	time.sleep(0.8)
	print("\n" * 100)

	print("Please input your guess:")
	player_guess = input()

	if player_guess == character_string:
		print("Good job!")
		time.sleep(2)
		update_character_string()
	else:
		print("Try again:")
		if number_of_guesses < 2:
			player_guess(number_of_guesses + 1)
		else:
			exit()
		

for guess in range(50):
	player_guess()