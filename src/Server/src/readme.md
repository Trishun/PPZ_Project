# Server Part

### Authors
- Dec Piotr - Server I/O
- Niemiec Robert - Database

#### Guidelines
* server:
 - accepts clients in threading
 - collects user data, spreads it if necessary
 
* database:
 - player info:
  * username
  * password
  * experience (?)
  * games joined
  * games won
  * favourite gamemode
 
# Database structure outline:
- Accounts:
	* user_id
	* nickname
	* email
	* password
	* backup_code
	(Google integration to check)
- Location:
	* userId
	* position on last log out
- Game data:
	* userId
	* games joined
	* games won
	* favourite gamemode

TODO:
	Mantaining lobbys and waypoints
	Hierarchy:
		- Lobby
			* Players within teams
			* Gamemode
			* Waypoints
				- Location
				- Tasks