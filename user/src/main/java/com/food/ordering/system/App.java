package com.food.ordering.system;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        CoffeeFactory coffeeFactory = new CoffeeFactory();
        System.out.println( coffeeFactory.create( "MilkCoffee" ) );
        System.out.println( coffeeFactory.create( "IceCoffee" ) );
    }
}
