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
		return atackstatus;
	}

	public void setResult(AtackStatus result) {
		Atackstatus atackstatus = result;
	}

	public Ship getShip() {
		return theShip;
	}

	public void setShip(Ship ship) {
		Ship theShip = ship;
	}

	public Square getLocation() {
		return location;
	}

	public void setLocation(Square square) {
		Square location = square;
	}
}
