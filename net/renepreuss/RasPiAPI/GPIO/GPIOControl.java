package net.renepreuss.RasPiAPI.GPIO;

import java.util.HashMap;
import java.util.Map;

import net.renepreuss.RasPiAPI.Board.BoardType;
import net.renepreuss.RasPiAPI.Board.RaspberryPi;

public class GPIOControl {
	public static Map<Integer, PinType> pinRegister = new HashMap<Integer, PinType>();
	RaspberryPi board;
	
	public GPIOControl(RaspberryPi board){
		this.board = board;
	}

	public boolean isValidPin(String string) {
		if(board.getBoardType() == BoardType.MODEL_B){
			return true;
		}
		return false;
	}
	
	public BoardType getGPIOBoardType(){
		return BoardType.MODEL_B;
	}

	public PinControl getPin(int i) {
		return new PinControl(i);
	}

}
