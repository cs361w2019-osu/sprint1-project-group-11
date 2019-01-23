package cs361.battleships.models;

public class Result {
	
	@JsonProperty private Square location;
	@JsonProperty private Ship ship;
	@JsonProperty private AtackStatus result;

	public Result() {
	}

	public Result(Square location) {
		this.location = location;
		result = AtackStatus.INVALID;
	}

	public AtackStatus getResult() {
		//TODO implement
		return null;
	}

	public void setResult(AtackStatus result) {
		//TODO implement
	}

	public Ship getShip() {
		//TODO implement
		return null;
	}

	public void setShip(Ship ship) {
		//TODO implement
	}

	public Square getLocation() {
		//TODO implement
		return null;
	}

	public void setLocation(Square square) {
		//TODO implement
	}
}
