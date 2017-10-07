# LeagueMaker

What it does:
What this app does is allow you to create leagues, teams in the league and results of matches between the teams. The app will calculate the positions of the teams in the league based on the results. The app will consist of three primary options, leagues, teams & results, which will be presented on the main menu screen. The leagues option will accommodate for creating a new league, modifying an existing one and viewing all the current leagues. The other two options will have the same functionality (able to create, edit & view). 

When creating a new league, the user will input the name of the league, the sport the league is associated with and an image for the league’s logo (which will be optional). Similarly, when making a new team, the user will input the name and image but also what league they are a part of. When adding a new result, the user will first have to choose what league the result occurred in, then will choose the two teams who played the game and finally include the result of the match. The way in which the user selects teams and leagues will be through drop-down menus so that they can only choose from the options that exist in the database.

All inserts will be put into the database and the modify & delete will update and delete respectively from the database. The database will consist of three tables, league, team and result.

Use Case:
![ScreenShot](https://raw.github.com/KeithMcLoughlin/LeagueMaker/master/Screenshots/useCase.png)

Screen Flow:
![ScreenShot](https://raw.github.com/KeithMcLoughlin/LeagueMaker/master/Screenshots/screenflow.png)

Class Diagram: 
![ScreenShot](https://raw.github.com/KeithMcLoughlin/LeagueMaker/master/Screenshots/classDiagram.png)

Screens:
Main Menu:
![ScreenShot](https://raw.github.com/KeithMcLoughlin/LeagueMaker/master/Screenshots/Main Screen.png)
This will be the main menu where each of the buttons will go to the corresponding menu option screens below.

Menu Options:
![ScreenShot](https://raw.github.com/KeithMcLoughlin/LeagueMaker/master/Screenshots/options.png)
The add buttons will go to the insert screens as below. The arrow button will go back to the previous activity.

Insert Data Screens:
![ScreenShot](https://raw.github.com/KeithMcLoughlin/LeagueMaker/master/Screenshots/inserts.png)
The names & result scores are edit texts. The grey boxes are drop-down menu where the user can only choose valid options. The green buttons will insert the data into the database. The back-arrow acts as a cancel button too.

Modify Data Screens:
Exactly the same layout as the insert data screens, but the fields will be filled out with the original data and the add button will be an update button. Also, there will be a delete button in the left bottom corner of each one to delete the league, team or result from the database.

View Screens:
![ScreenShot](https://raw.github.com/KeithMcLoughlin/LeagueMaker/master/Screenshots/views.png)
In the view leagues, the modify button will bring user to the update league activity but for the other two, clicking on the list item will bring the user to the update activity. The user can narrow the search of results with the optional drop-down menus. The order of the view team will be alphabetical while the view leagues will order them in the position they are in the league.

