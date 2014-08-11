package net.renepreuss.RasPiAPI.GPIO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PinControl {
	int pin = 0;
	public PinControl(int pin) {
		this.pin = pin;
	}

	public void togglePinWithDelay(final int timeDelay, final boolean pinState){
		Thread t = new Thread(new Runnable() {
			public void run(){
				PinControl.this.setPinOutputState(pinState);
				try {
					Thread.sleep(timeDelay);
				} catch (InterruptedException e) {}
				PinControl.this.setPinOutputState(pinState);
			}
		});
        t.start();
	}
	
	public void enablePin(){
		this.setPinOutputState(true);
	}
	
	public void disablePin(){
		this.setPinOutputState(false);
	}
	
	public void togglePin(){
		this.setPinOutputState(!this.getPinOutputState());
	}
	
	public boolean getPinOutputState(){
		if(getPinType() == PinType.OUTPUT){
			
		}
		return false;
	}
	
	public boolean setPinOutputState(boolean pinState){
		if(getPinType() == PinType.OUTPUT){
			try {
				Process p = Runtime.getRuntime().exec(new String[]{"bash","-c","ls /home/XXX"});
			} catch (IOException e) {}
		}
		return false;
	}
	
	public boolean getPinInputState(){
		return false;
	}
	
	public PinType getPinType(){
		if(GPIOControl.pinRegister.containsKey(pin)){
			return GPIOControl.pinRegister.get(pin);
		}else{
			return PinType.UNSET;
		}
	}
	
	public boolean setPinType(PinType pinType){
		if(pinType == PinType.UNSET){
			return resetPin();
		}else if(getPinType() == pinType){
			return true;
		}else{
			if(resetPin()){
				return initPin(pinType);
			}
		}
		return false;
	}
	
	private boolean resetPin(){
		//Pin vom register löschen wenn es erfolgreich war
		GPIOControl.pinRegister.remove(pin);
		return true;
	}
	
	private boolean initPin(PinType pinType){
		try {
			Process p = Runtime.getRuntime().exec(new String[]{"bash","-c","ls /home/XXX"});
			// Register Pin in PinRegister wenn es erfolgreich war.
		} catch (IOException e) {}
		return true;
	}
}
