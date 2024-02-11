# Thirty

Thirty is an Android game written in Kotlin.

<p align="middle">
<img src="https://raw.githubusercontent.com/wille1101/imgs/master/thirty_main.png" width="300">
<img src="https://raw.githubusercontent.com/wille1101/imgs/master/thirty_game.png" width="300">
</p>


### Game Rules
Thirty is a dice game, similar to Yatzy, where the player rolls 6 dices. The player then gets to choose the dices to re-roll or keep for two rounds. After three rounds the player chooses the dices that contribute to the current round's score. One game is 10 of these rounds, where the maximum number of rolls a player can make is 30, hence the name.

**Scoring**

During scoring the player chooses what scoring choice the dices should count as. Possible choices can be seen below. Each choice can be made once each game. Each dice can only be used once for a score. 

| Choice | The dices that affect the score |
|-----|---------------------------------------------------------------------------------|
| Low | All dices with value 3 or lower gives points |
| 4 |	All combinations of one or more dices that sums up to 4 gives points. |
| 5 |	All combinations of one or more dices that sums up to 5 gives points. |
| 6 |	All combinations of one or more dices that sums up to 6 gives points. |
| 7 |	All combinations of dices that sums up to 7 gives points. |
| 8 |	All combinations of dices that sums up to 8 gives points. |
| 9 |	All combinations of dices that sums up to 9 gives points. |
| 10 |  All combinations of dices that sums up to 10 gives points. |
| 11 |  All combinations of dices that sums up to 11 gives points. |
| 12 |  All combinations of dices that sums up to 12 gives points. |


Example: After three rolls a player has the dices 1, 1, 1, 2, 4 and 4. The player can then score this as 5 and will be given 10 points by selecting the dice pairs 1, 4 and 1, 4.
 
