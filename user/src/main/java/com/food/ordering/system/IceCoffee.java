package com.food.ordering.system;

import com.example.Factory;

@Factory(id = "IceCoffee", type = Coffee.class)
public class IceCoffee implements Coffee{

	@Override
	public int getAmount() {
		return 10;
	}

	@Override
	public String getName() {
		return "Ice Coffee";
	}

	@Override
	public String toString() {
		return "Coffee name: " + getName() + " amount: " + getAmount();
	}
}
