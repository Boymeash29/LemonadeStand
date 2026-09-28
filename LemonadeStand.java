/*
 * Author: Asher Boymel
 * File: LemonadeStand.java
 * Desc: Controls all methods for the Lemonade Stand game
 */


import java.util.Scanner;

/**
 * Methods: setup(money), showStatus, buySupplies, setRecipe
 */
public class LemonadeStand{
	
	// Declare variables
	double cashOnHand;
	double costLemons = 1.50;
	double costSugar = 2.00;
	double costIce = 0.75;
	int qtyLemons = 0;
	int qtySugar = 0;
	int qtyIce = 0;
	int lemonPerPitcher = 3;
	int sugarPerPitcher = 1;
	int icePerPitcher = 2;
	int cupsPerPitcher = 10;
	double pricePerCup = 1.00;
	int day = 1;
	int totalDays = 7;
	String weather = "Sunny";
	double weatherModifier = 1.0;

	Scanner scan = new Scanner(System.in);

	public void setup(double money){
		cashOnHand = money;
		System.out.println("Hello, welcome to Lemonade Stand!");
		System.out.println("Please hold while the game loads!");
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}
	
	/**
	 * Main screen
	 */
	public void showStatus(){
		System.out.println();
		System.out.println("Day " + day + " of " + totalDays);
		System.out.println("Weather: " + weather);
		System.out.println("Cash: $" + String.format("%.2f", cashOnHand));
		System.out.println("Lemons: " + qtyLemons + "  Sugar: " + qtySugar + "  Ice: " + qtyIce);
		System.out.println("Recipe per pitcher: " + lemonPerPitcher + " lemons, " + sugarPerPitcher + " sugar, " + icePerPitcher + " ice");
		System.out.println("Price per cup: $" + String.format("%.2f", pricePerCup));
	}
	
	/**
	 * Prompts user to buy supplies
	 */
	public void buySupplies(){
		System.out.println("Prices - lemons $" + costLemons + " sugar $" + costSugar + " ice $" + costIce);

		System.out.print("How many lemons do you want to buy? ");
		int l = scan.nextInt();

		System.out.print("How many sugar do you want to buy? ");
		int s = scan.nextInt();

		System.out.print("How many ice do you want to buy? ");
		int i = scan.nextInt();
		scan.nextLine();

		// Make sure the player isn't buying negative amounts (thanks dad)
		if(l < 0 || s < 0 || i < 0){
			System.out.println("You can't buy negative supplies!");
			return;
		}

		// Calculate total cost
		double total = l * costLemons + s * costSugar + i * costIce;

		// Check if player has enough money
		if(total > cashOnHand){
			System.out.println("You don't have enough money for that!");
			System.out.println("Cost: $" + String.format("%.2f", total));
			System.out.println("Cash: $" + String.format("%.2f", cashOnHand));
			return;
		}

		// Complete purchase
		cashOnHand -= total;
		qtyLemons += l;
		qtySugar += s;
		qtyIce += i;

		System.out.println("Bought it! You spent $" + String.format("%.2f", total));
	}

	/**
	 * Prompts user to create recipe
	 */
	public void setRecipe(){
		System.out.println("Tip: 3 lemons to 1 sugar usually tastes good.");
		System.out.print("Lemons per pitcher: ");
		lemonPerPitcher = scan.nextInt();
		System.out.print("Sugar per pitcher: ");
		sugarPerPitcher = scan.nextInt();
		System.out.print("Ice per pitcher: ");
		icePerPitcher = scan.nextInt();
		scan.nextLine();
	}
	
	public void setPrice(){
		System.out.print("Set your price per cup: $");
		pricePerCup = scan.nextDouble();
		scan.nextLine();
	}
	
	// figures out how many pitchers we can make with what we have
	public int pitchersPossible(){
	int p = Math.min(qtyLemons / lemonPerPitcher, qtySugar / sugarPerPitcher);
	if(icePerPitcher > 0){
		p = Math.min(p, qtyIce / icePerPitcher);
	}
		return p;
	}
	
	public void openStand(){
		int pitchers = pitchersPossible();
		if(pitchers == 0){
			System.out.println("You don't have enough supplies to make even one pitcher. No sales today!");
		return;
		}
		int cupsAvailable = pitchers * cupsPerPitcher;

		int base = 30;
		
		// higher price means less people want to buy
		double priceFactor = 1.6 - 0.6 * pricePerCup;
		if(priceFactor < 0){
			priceFactor = 0;
		}
		double luck = 0.8 + Math.random() * 0.4;

		int demand = (int)(base * priceFactor * luck * weatherModifier);
		int sold = Math.min(demand, cupsAvailable);
		int used = (int)Math.ceil(sold / (double)cupsPerPitcher);

		qtyLemons = qtyLemons - used * lemonPerPitcher;
		qtySugar = qtySugar - used * sugarPerPitcher;
		qtyIce = qtyIce - used * icePerPitcher;

		double revenue = sold * pricePerCup;
		cashOnHand = cashOnHand + revenue;

		System.out.println();
		System.out.println("--- End of day " + day + " ---");
		System.out.println("Customers wanted " + demand + " cups, you had " + cupsAvailable);
		System.out.println("Cups sold: " + sold);
		System.out.println("Revenue: $" + String.format("%.2f", revenue));
		if(demand > cupsAvailable){
			System.out.println("You sold out! Missed " + (demand - cupsAvailable) + " customers.");
		}
	}

	public void randomEvents(){
		int event = (int)(Math.random() * 6);

		switch(event){
			case 0:
				// Generous customer
				cashOnHand += 5.00;
				System.out.println();
				System.out.println("RANDOM EVENT: A generous customer!");
				System.out.println("A customer loved your lemonade and gave you a $5 tip!");
				System.out.println("$5.00 has been added to your cash.");
				break;

			case 1:
				// Lemons go bad
				int lostLemons = (int)(Math.random() * 3);
				qtyLemons -= lostLemons;

				System.out.println();
				System.out.println("RANDOM EVENT: Rotten lemons!");
				System.out.println(lostLemons + " lemons went bad overnight.");
				break;

			case 2:
				// Free supplies
				qtySugar += 2;

				System.out.println();
				System.out.println("RANDOM EVENT: Free sugar!");
				System.out.println("A local bakery gave you 2 bags of sugar.");
				break;

			case 3:
				// Lost ice
				int lostIce = Math.min(2, qtyIce);
				qtyIce -= lostIce;

				System.out.println();
				System.out.println("RANDOM EVENT: The freezer broke!");
				System.out.println(lostIce + " bags of ice melted.");
				break;

			case 4:
				// Unexpected expense
				double repairCost = 3.00;

				if(cashOnHand >= repairCost){
					cashOnHand -= repairCost;
					System.out.println();
					System.out.println("RANDOM EVENT: Equipment trouble!");
					System.out.println("Your lemonade stand needs a quick repair.");
					System.out.println("You paid $3.00 for repairs.");
				} else {
					cashOnHand -= repairCost;
					System.out.println();
					System.out.println("RANDOM EVENT: Equipment trouble!");
					System.out.println("Your stand needs repairs, but you can't afford them.");
					System.out.println("You took a loan out from your mother.");
				}
				break;

			case 5:
				// Lucky find
				qtyLemons += 3;

				System.out.println();
				System.out.println("RANDOM EVENT: Lucky find!");
				System.out.println("You found 3 perfectly good lemons left behind at the market.");
				break;
		}
	}

	public void setWeather(){
		int weatherNumber = (int)(Math.random() * 5);

		switch(weatherNumber){
			case 0:
				weather = "Sunny";
				weatherModifier = 1.2;
				break;

			case 1:
				weather = "Hot";
				weatherModifier = 1.5;
				break;

			case 2:
				weather = "Cloudy";
				weatherModifier = 0.9;
				break;

			case 3:
				weather = "Rainy";
				weatherModifier = 0.5;
				break;

			case 4:
				weather = "Heat Wave";
				weatherModifier = 2.0;
				break;
		}
	}

	public void play(){
		while(day <= totalDays){
			setWeather();
			boolean opened = false;
				while(!opened){
					showStatus();
					System.out.println("1. Buy supplies");
					System.out.println("2. Change recipe");
					System.out.println("3. Set price");
					System.out.println("4. Open stand");
					System.out.println("5. Quit game");
					System.out.print("Choice: ");
					int choice = scan.nextInt();
					scan.nextLine();

					if(choice == 1){
						buySupplies();
					} else if(choice == 2){
						setRecipe();
					} else if(choice == 3){
						setPrice();
					} else if(choice == 4){
						openStand();
						randomEvents();
						opened = true;
					} else if(choice == 5){
						System.out.println("Thanks for playing!");
						return;
					} else {
						System.out.println("Not a real option, try again.");
					}
				}

				day++;
			}

		double profit = cashOnHand - 20.00;
		System.out.println();
		System.out.println("GAME OVER");
		if(profit >= 0){
			System.out.println("Final cash: $" + String.format("%.2f", cashOnHand) + " (profit of $" + String.format("%.2f", profit) + ")");
		} else {
			System.out.println("Final cash: $" + String.format("%.2f", cashOnHand) + " (loss of $" + String.format("%.2f", Math.abs(profit)) + ")");
		}
	}
}
