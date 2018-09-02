package gtu.designpattern;

public class State_ {

    interface State {
        void insertQuarter();

        void ejectQuarter();

        void turnCrank();

        void dispense();
    }

    static class GumballMachine {
        State soldOutState;
        State noQuarterState;
        State hasQuarterState;
        State soldState;

        GumballMachine(int numberGumballs) {
            //soldOutState = new SoldOutState(this);
            //soldOutState = new NoQuarterState(this);
            //soldOutState = new HasQuarterState(this);
            //soldOutState = new SoldState(this);
        }
    }

    static class HasQuarterState implements State {
        GumballMachine gumballMachine;

        HasQuarterState(GumballMachine gumballMachine) {
            this.gumballMachine = gumballMachine;
        }

        @Override
        public void insertQuarter() {
            System.out.println("You can't insert another quarter");
        }

        @Override
        public void ejectQuarter() {
            System.out.println("Quarter returned");
            //            this.gumballMachine.setState(gumballMachine.getNoQuarterState());
        }

        @Override
        public void turnCrank() {
            System.out.println("You turned...");
            //          this.gumballMachine.setState(gumballMachine.getSoldState());
        }

        @Override
        public void dispense() {
            System.out.println("No gumball dispensed");
        }
    }

    static class SoldState implements State {
        GumballMachine gumballMachine;

        SoldState(GumballMachine gumballMachine) {
            this.gumballMachine = gumballMachine;
        }

        @Override
        public void insertQuarter() {
            System.out.println("Please wait, we're already giving you a gumball");
        }

        @Override
        public void ejectQuarter() {
            System.out.println("Sorry, you already turn the crank");
        }

        @Override
        public void turnCrank() {
            System.out.println("Turning twice doesn't get you another gumball!");
        }

        @Override
        public void dispense() {
            //this.gumballMachine.releaseBall();
            //if(this.gumballMachine.getCount()>0){
            //   this.gumballMachine.setState(this.gumballMachine.getNoQuarterState());
            //}else{
            //    System.out.println("Oops, out of gumball!");
            //    this.gumballMachine.setState(this.gumballMachine.getSoldOutState());
            //}
        }

    }
}
