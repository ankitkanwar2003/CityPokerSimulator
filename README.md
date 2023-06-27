# CityPokerSimulator

This repository contains the implementation of a Poker Simulator as part of an assignment for the course COL106 - Data Structures & Algorithms taught at IIT Delhi, Delhi, India. The project was done under the guidance of Prof. Keerti Choudhary and Prof. Huzur Saran.

## Poker Simulator Description

The Poker Simulator is designed to simulate a poker game in the city of COL106. The citizens of COL106 participate in the poker games held at the Poker arena to earn extra income. Each citizen has an initial amount of $100,000. When a citizen enters the Poker arena, they specify their maximum loss and maximum profit thresholds. If a citizen's losses exceed the maximum loss or their profits reach the maximum profit, they leave the Poker arena.

The simulator manages the citizens and determines the winner of each poker game. It utilizes a data structure called Heap, specifically a max heap, to efficiently manage the citizens and find the player with the highest profit.

The Heap data structure is implemented using a complete binary tree. Each node in the binary tree contains information about a citizen, including their unique identifier, current profit, and other relevant details.

The simulator keeps track of the citizens using the Heap data structure. It inserts new citizens into the heap when they enter the Poker arena and updates their profit as the game progresses. The simulator also handles the removal of citizens from the heap when they reach their maximum profit or maximum loss thresholds.

To determine the winner of each poker game, the simulator extracts the citizen with the highest profit from the heap. This citizen is declared the winner, and their information is displayed.

Throughout the simulation, the simulator is able to provide real-time updates on the number of active citizens, and the stats of players in the Arena, including the most profitable player, the next player to get out, etc.

The Poker Simulator in the CityPokerSimulator repository provides an interactive and realistic simulation of a poker game in COL106, allowing users to understand the dynamics of the game and the outcomes for different citizens based on their profit & loss thresholds.
