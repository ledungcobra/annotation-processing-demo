package com.food.ordering.system;

import com.example.Factory;

@Factory(id = "MilkCoffee", type = Coffee.class)
public class MilkCoffee implements Coffee{

	@Override
	public int getAmount() {
		return 30;
	}

	@Override
	public String getName() {
		return "Milk coffee";
	}

	@Override
	public String toString() {
		return "Coffee name: " + getName() + " amount: " + getAmount();
	}
}
