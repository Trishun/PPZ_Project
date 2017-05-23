# PPZ (locationbasedgame) - Use Cases


## Register player:

### Author: User

### Main case:
1. The user inputs an e-mail, nickname and password
2. The user clicks the submit button
3. The server informs the user of a successful registration

#### Alternate cases:
3a. The e-mail is already taken

3a1. The server sends out an appropriate message


3b. The nickname is already taken

3b1. The server sends out an appropriate message


3c.A different error occurs

3c1. An appropriate error message is shown



## Log in player:

### Author: User

### Main case:
1. The user inputs an e-mail and password
2. The user clicks the log in button
3. The server displays a message informing the user of a successful log in

#### Alternate cases:
3a. The nickname is not used

3a1. The server displays an appropriate message


3b. The password is incorrect

3b1. The server displays an appropriate message


3c. A different error occurs

3c1. An appropriate error message is shown



## Create new password in case of old password forgotten:

### Author: User

### Main case:
1. The user enters their e-mail, backup code and password
2. The user clicks the password creation button
3. The server informs the user of a successful password creation

#### Alternate cases:
3a. The e-mail does not exist

3a1. The server displays an appropriate message


3b. The backup code is incorrect

3b1. The server displays an appropriate message


3c. A different error occurs

3c1. An appropriate error message is shown



## Change password:

### Author: User

### Main case:
1. The user enters their e-mail, old password and new password
2. The user clicks the password change button
3. The server informs the user of a successful password change

#### Alternate cases:
3a. The e-mail does not exist

3a1. The server displays an appropriate message


3b. The entered old password is different from the actual old password

3b1. The server informs the user of the password difference


3c. A different error occurs

3c1. An appropriate error message is shown



## Enter a game lobby using a lobby id code

### Author: User

### Main case:
1. The user enters the lobby id code into the Join Lobby window
2. The system checks to see if there is a lobby with the submitted id
3. The user is taken to the game lobby screen

#### Alternate cases:
2a. There is no lobby that corresponds with the submitted id

2a1. The system informs the player of the id being incorrect



## Add a checkpoint

### Author: User

### Main case:
1. The user selects the add checkpoint button
2. The user selects the desired checkpoint location on the map
3. The user types in a description of the task in the appropriate text box
3. The system adds the new chekpoint to the list of checkpoints
4. The system sends out a message to the opposing team informing them of a new checkpoint

#### Alternate cases:
1a. The most recent checkpoint has been flagged as the last one

1a1. The system sends out a message to the user informing them of the checkpoint limit being reached


1b. The most recent checkpoint is the last one specified by the system's checkpoint limit

1b1. The system sends out a message to the user informing them of the checkpoint limit being reached


4a. During the checkpoint selection process, the opposing team has cleared all of the existing checkpoints

4a1. The system stops the checkpoint addition process

4a2. The system displays a defeat message to the user


4b. The user has not covered the minimal distance between checkpoints

4b1. The system sends out a message to the user informing them of the minimal distance requirement
