

### CardController : /api/v2/card

Located at: [icu.samnyan.aqua.net.CardController](icu/samnyan/aqua/net/CardController.kt)

**/card/default-game** : Get the default game for the card.

* username: String
* **Returns**: Game ID

**/card/link** : Bind a card to the user. This action will migrate selected data from the card to the user's ghost card.

* token: String
* cardId: String
* migrate: String
* **Returns**: Success message

**/card/summary** : Get a summary of the card, including the user's name, rating, and last login date.

* cardId: String
* **Returns**: Summary of the card

**/card/unlink** : Unbind a card from the user. No data will be migrated during this action.

* token: String
* cardId: String
* **Returns**: Success message


### Frontier : /api/v2/frontier

Located at: [icu.samnyan.aqua.net.Frontier](icu/samnyan/aqua/net/Frontier.kt)

**/frontier/lookup-card** : Lookup a card by access code

* ftk: String
* accessCode: String
* **Returns**: Card information

**/frontier/register-card** : Register a new card by access code

* ftk: String
* accessCode: String
* **Returns**: Card information


### UserRegistrar : /api/v2/user

Located at: [icu.samnyan.aqua.net.UserRegistrar](icu/samnyan/aqua/net/UserRegistrar.kt)

**/user/confirm-email** : Confirm email address with a token sent through email to the user.

* token: String
* **Returns**: Success message

**/user/me** : Get the information of the current logged-in user.

* token: String
* **Returns**: User information

**/user/login** : Login with email/username and password. This will also check if the email is verified and send another confirmation.

* email: String
* password: String
* turnstile: String
* **Returns**: JWT token

**/user/register** : Register a new user. This will also create a ghost card for the user and send a confirmation email.

* username: String
* email: String
* password: String
* turnstile: String
* **Returns**: Success message

**/user/reset-password** : Send the user a reset password email. This will also check if the email is verified or if many requests were sent recently.

* email: String
* turnstile: String
* **Returns** Success message

**/user/change-password** : Reset a user's password with a token sent through email to the user.

* token: String
* password: String
* **Returns** Success message

**/user/setting** : Validate and set a user setting field.

* token: String
* key: String
* value: String
* **Returns**: Success message

**/user/keychip** : Get a Keychip ID so that the user can connect to the server.

* token: String
* **Returns**: Success message

**/user/upload-pfp** : Upload a profile picture for the user.

* token: String
* file: MultipartFile
* **Returns**: Success message

# Migration

### Chuni / Chusan

GET /api/game/chuni/v2/profile > /api/v2/chu3/user-box
GET /api/game/chuni/v2/data > /api/v2/chu3/user-box-all-items



