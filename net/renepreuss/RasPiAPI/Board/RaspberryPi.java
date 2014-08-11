package net.renepreuss.RasPiAPI.Board;

import net.renepreuss.RasPiAPI.GPIO.GPIOControl;

public class RaspberryPi {
	private GPIOControl gpioControl;
	
	public RaspberryPi(){
		gpioControl = new GPIOControl(this);
	}
	
	public GPIOControl getGPIO(){
		return gpioControl;
	}
	
	public BoardType getBoardType(){
		BoardType boardType = BoardType.valueOf("");
		if(boardType != null){
			return boardType;
		}
		return BoardType.UNKNOW;
	}
}
