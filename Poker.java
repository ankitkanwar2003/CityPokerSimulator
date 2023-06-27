import heap_package.Node;
import heap_package.Heap;
import java.util.ArrayList;

public class Poker{

	private int city_size;            // City Population
	public int[] money;		         // Denotes the money of each citizen. Citizen ids are 0,1,...,city_size-1.

	/* 
	   1. Can use helper methods but they have to be kept private. 
	   2. Allowed to use only PriorityQueue data structure globally but can use ArrayList inside methods. 
	   3. Can create at max 4 priority queues.
	*/

	public void initMoney(){
		// Do not change this function.
		for(int i = 0;i<city_size;i++){
			money[i] = 100000;							// Initially all citizens have $100000. 
		}
	}
	private Heap losses_gains;
	private Heap profits;
	private Heap losses;
	public Poker(int city_size, int[] players, int[] max_loss, int[] max_profit) {

		/* 
		   1. city_size is population of the city.
		   1. players denotes id of the citizens who have come to the Poker arena to play Poker.
		   2. max_loss[i] denotes the maximum loss player "players[i]"" can bear.
		   3. max_profit[i] denotes the maximum profit player "players[i]"" will want to get.
		   4. Initialize the heap data structure(if required). 

		   n = players.length 
		   Expected Time Complexity : O(n).
		*/

		this.city_size = city_size;
		this.money = new int[this.city_size];
		this.initMoney();

		// To be filled in by the student
		int[] def_p = new int[2*players.length];
		int[] keys = new int[2*players.length];
		int[] zeroes = new int[players.length];
		for (int i = 0; i < players.length; i++) {
			def_p[i] = -1*max_profit[i];
			def_p[i+players.length] = -1*max_loss[i];
			keys[i] = players[i];
			keys[i+players.length] = players[i] + city_size;
			zeroes[i] = 0;
		}
		try {
			losses_gains = new Heap(2*city_size, keys, def_p);
			profits = new Heap(city_size, players, zeroes);
			losses = new Heap(city_size, players, zeroes);
		} catch (Exception e) {
			// System.out.println("Woah");
		}
	}

	public ArrayList<Integer> Play(int[] players, int[] bids, int winnerIdx) {

		/* 
		   1. players.length == bids.length
		   2. bids[i] denotes the bid made by player "players[i]" in this game.
		   3. Update the money of the players who has played in this game in array "money".
		   4. Returns players who will leave the poker arena after this game. (In case no
		      player leaves, return an empty ArrayList).
                   5. winnerIdx is index of player who has won the game. So, player "players[winnnerIdx]" has won the game.
		   m = players.length
		   Expected Time Complexity : O(mlog(n))
		*/

		int winner = players[winnerIdx];					// Winner of the game.
		int sum = 0;
		for (int i = 0; i < players.length; i++) {
			if (i != winnerIdx) {
				sum+=bids[i];
			}
		}
		for (int i = 0; i < players.length; i++) {
			if (i != winnerIdx) {
				try {
					losses_gains.update(players[i], (-1*bids[i]));
					losses_gains.update((city_size + players[i]), bids[i]);
				} catch (Exception e) {
					// System.out.println("Something went wrong while updating non-winner amounts in heap");
				}
				try {
					profits.update(players[i], (-1*bids[i]));
				} catch (Exception e) {
					
				}
				try {
					losses.update(players[i], (bids[i]));
				} catch (Exception e) {
					
				}
				money[players[i]] -= bids[i];
			} else {
				try {
					losses_gains.update(players[i], sum);
					losses_gains.update((city_size + players[i]), (-1*sum));
				} catch (Exception e) {
					// System.out.println("Something went wrong while updating winner amounts in heap");
				}
				try {
                    profits.update(players[i], sum);
                    losses.update(players[i], (-1*sum));
				} catch (Exception e) {
					
				}
				money[players[i]] += sum;
			}
		}
		ArrayList<Integer> playersToBeRemoved = new ArrayList<Integer>();     // Players who will be removed after this game.
		ArrayList<Integer> temp = new ArrayList<Integer>();
		int removed = 0;
		try {
			removed = losses_gains.getMaxValue();
		} catch (Exception e) {
			// System.out.println("Something went wrong in finding players to be removed.");
		}
		while (removed > 0) {
			try {
				ArrayList<Integer> x = losses_gains.deleteMax();
				for (Integer i: x) {
					temp.add(i);
					if (i < city_size) {
						playersToBeRemoved.add(i);
					} else {
						playersToBeRemoved.add(i - city_size);
					}
				}
				removed = losses_gains.getMaxValue();
			} catch (Exception e) {
				// System.out.println("Something went wrong while removing players LMAO.");
				// System.out.println("Something could have gone wrong while finding players to be removed LMAO.");
				break;
			}
		}
		for (Integer i : temp) {
			if (i < city_size) {
				try {
					losses_gains.update(i+city_size, Integer.MAX_VALUE);
					losses_gains.deleteMax();
				} catch (Exception e) {
					// System.out.println("Something went wrong while removing profit deficits");
				}
			} else {
				try {
					losses_gains.update(i-city_size, Integer.MAX_VALUE);
					losses_gains.deleteMax();
				} catch (Exception e) {
					// System.out.println("Something went wrong while removing loss deficits");
				}
			}
		}
		// To be filled in by the student

		return playersToBeRemoved;
	}

	public void Enter(int player, int max_loss, int max_profit){
		/*
			1. Player with id "player" enter the poker arena.
			2. max_loss is maximum loss the player can bear.
			3. max_profit is maximum profit player want to get. 

			Expected Time Complexity : O(logn)
		*/
		try {
			losses_gains.insert(player, -1*max_profit);
			losses_gains.insert((player + city_size), -1*max_loss);
		} catch (Exception e) {
			//if player already in arena, update profile
			// try {//maybe switch to doing nothing
			// 	losses_gains.update(player, Integer.MAX_VALUE);
			// 	losses_gains.deleteMax();
			// 	losses_gains.update((player + city_size), Integer.MAX_VALUE);
			// 	losses_gains.deleteMax();
			// 	losses_gains.insert(player, -1*max_profit);
			// 	losses_gains.insert((player + city_size), -1*max_loss);
			// } catch (Exception f) {
			// 	//Unexpected case, do nothing
			// }
		}
		try {
			profits.insert(player, money[player] - 100000);
			losses.insert(player, 100000 - money[player]);
		} catch (Exception e) {

		}
		// To be filled in by the student
	}

	public ArrayList<Integer> nextPlayersToGetOut(){

		/* 
		   Returns the id of citizens who are likely to get out of poker arena in the next game. 

		   Expected Time Complexity : O(1). 
		*/
		// To be filled in by the student
		ArrayList<Integer> players = new ArrayList<Integer>();    // Players who are likely to get out in next game.
		try {
			ArrayList<Integer> x = losses_gains.getMax(); //maximum number among negative numbers means their absolute value is minimum so distance from target (0) is minimum
			// ArrayList<Integer> y = loss_deficits.getMax();
			ArrayList<Integer> rem = new ArrayList<Integer>();
			for (int i = 0; i < x.size(); i++) {
				for (int j = 0; j < x.size(); j++) {
					if ((j != i)) {
						if (x.get(j) > city_size) {
							if (x.get(i) + city_size == x.get(j)) {
								rem.add(j);
							}
						}
					}
				}
			}
			for (int i = 0; i < x.size(); i++) {
				boolean dup = false;
				int k = 0;
				while (k < rem.size()) {
					if (rem.get(k) != i) {
						k += 1;
					} else {
						dup = true;
						break;
					}
				}
				if (!dup) {
					if (x.get(i) < city_size) {
						players.add(x.get(i));
					} else {
						players.add(x.get(i) - city_size);
					}
				}
			}
		} catch (Exception e) {

		}
		return players;
	}

	public ArrayList<Integer> playersInArena(){

		/* 
		   Returns id of citizens who are currently in the poker arena. 

		   Expected Time Complexity : O(n).
		*/

		ArrayList<Integer> currentPlayers = new ArrayList<Integer>();    // citizens in the arena.
		try {
			ArrayList<Integer> x = losses_gains.getKeys();
			int i = 0;
			while (i < x.size()) {
				if (x.get(i) < city_size) {
						currentPlayers.add(x.get(i));
					}
				i += 1;
			}
		} catch (Exception e) {

		}
		// To be filled in by the student

		return currentPlayers;
	}

	public ArrayList<Integer> maximumProfitablePlayers(){

		/* 
		   Returns id of citizens who has got most profit. 
			
		   Expected Time Complexity : O(1).
		*/

		ArrayList<Integer> citizens = new ArrayList<Integer>();    // citizens with maximum profit.
		int mp = 0;
		try {
			mp = profits.getMaxValue();
			if (mp == 0) {
				for (int i = 0; i < city_size; i++) {
					if (money[i] == 100000) {
						citizens.add(i);
					}
				}
			} else {
				try {
					citizens = profits.getMax();
				} catch (Exception e) {
					
				}
			}
		} catch (Exception e) {

		}
		// To be filled in by the student

		return citizens;
	}

	public ArrayList<Integer> maximumLossPlayers(){

		/* 
		   Returns id of citizens who has suffered maximum loss. 
			
		   Expected Time Complexity : O(1).
		*/

		ArrayList<Integer> citizens = new ArrayList<Integer>();     // citizens with maximum loss.
		int ml = 0;
		try {
			ml = losses.getMaxValue();
			if (ml == 0) {
				for (int i = 0; i < city_size; i++) {
					if (money[i] == 100000) {
						citizens.add(i);
					}
				}
			} else {
				try {
					citizens = losses.getMax();
				} catch (Exception e) {
					
				}
			}
		} catch (Exception e) {

		}
		// To be filled in by the student
		return citizens;
	}
}