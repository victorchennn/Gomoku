# Gomoku

Gomoku, also called Gobang or Five in a Row(FIR), is an abstract strategy board game. It is traditionally played with Go pieces 
(black and white stones) on a Go board, using 15×15 of the 19×19 grid intersections. The game is known in several countries 
under different names.

Players alternate turns placing a piece of their color on an empty intersection. The winner is the first player to 
form an unbroken chain of five stones horizontally, vertically, or diagonally.

In this game, players can automatically choose manually playing this game with another player or playing with an AI, during the 
setup stage, players can choose to set their pieces randomly and then start the game, or load the game saved before. All the 
introduction and specification can be found in help.txt file.

Here are the pictures of the game:

<img src="https://github.com/victorchennn/Gomoku/blob/master/picture1.png" width="300" height="300">              <img src="https://github.com/victorchennn/Gomoku/blob/master/picture2.png" width="300" height="300">

Central strategy and point of board game or chess game AI is using Minimax with alpha-beta pruning, using depth to control the capability of detection. For Minimax algorithm, it needs a powerful function to compute the heuristic score of the board and judge which position is best. In this game we use four directions of adjacent positions (up to down, right to left, upright to downleft and upleft to downright) to determine what kind of cases (one, two, ...,or five pieces in a row, blocked or unblocked) it belongs to and count the relative score. Detailed design and introduction can be found in the reference below. 

https://en.wikipedia.org/wiki/Gomoku<br/>
https://en.wikipedia.org/wiki/Alpha–beta_pruning<br/>
https://en.wikipedia.org/wiki/Minimax#Minimax_algorithm_with_alternate_moves<br/>
https://blog.csdn.net/syrchina/article/details/46698981d<br/>
https://github.com/Kesoyuh/Gomoku/blob/master/Gomoku/model/GGMinimaxAI.m
