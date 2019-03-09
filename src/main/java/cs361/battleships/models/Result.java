package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Result {

	@JsonProperty private Square location;
	@JsonProperty private Ship ship;
	@JsonProperty private AtackStatus result;

	public Result() {
		result = AtackStatus.MISS;
	}

	public Result(Square location) {
		this();
		this.location = location;
	}

	public AtackStatus getResult() {
		return result;
	}

	public void setResult(AtackStatus result) {
		this.result = result;
	}

	public Ship getShip() {
		return ship;
	}

	public void setShip(Ship ship) {
		this.ship = ship;
	}

	public Square getLocation() {
		return location;
	}

	public void setLocation(Square square) {
		this.location = square;
	}
}
