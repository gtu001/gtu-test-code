package gtu.designpattern;

public class Command2 {

    static class RemoteControl {
        Command[] onCommands;
        Command[] offCommands;

        RemoteControl() {
            onCommands = new Command[7];
            offCommands = new Command[7];
            Command noCommand = new NoCommand();
            for (int ii = 0; ii < 7; ii++) {
                onCommands[ii] = noCommand;
                offCommands[ii] = noCommand;
            }
        }

        void setCommand(int slot, Command onCommand, Command offCommand) {
            onCommands[slot] = onCommand;
            offCommands[slot] = offCommand;
        }

        void onButtonWasPushed(int slot) {
            onCommands[slot].execute();
        }

        void offButtonWasPushed(int slot) {
            offCommands[slot].execute();
        }
    }

    interface Command {
        void execute();
    }

    static class NoCommand implements Command {
        @Override
        public void execute() {
            System.out.println("do nothing!");
        }
    }

    static class LightOffCommand implements Command {
        //Light light;

        @Override
        public void execute() {
            //light.off();
        }
    }

    static class StereoOnWithCDCommand implements Command {
        //Stereo stereo;
        @Override
        public void execute() {
            //stereo.on();
            //stereo.setCD();
            //stereo.setVolumne(11);
        }
    }
}
