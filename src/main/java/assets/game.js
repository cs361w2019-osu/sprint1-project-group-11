var isSetup = true;
var placedShips = 0;
var game;
var shipType;
var vertical;
var usingSonar = 0;
var sonarAvailable = 2;

let map = {
  'A': 1,
  'B': 2,
  'C': 3,
  'D': 4,
  'E': 5,
  'F': 6,
  'G': 7,
  'H': 8,
  'I': 9,
  'J': 10
}

function makeGrid(table, isPlayer) {
    for (i=0; i<10; i++) {
        let row = document.createElement('tr');
        for (j=0; j<10; j++) {
            let column = document.createElement('td');
            column.addEventListener("click", cellClick);
            row.appendChild(column);
        }
        table.appendChild(row);
    }
}

function markHits(board, elementId, surrenderText) {
    board.attacks.forEach((attack) => {
        let className;
        if (attack.result === "MISS")
            className = "miss";
        else if (attack.result === "HIT")
            className = "hit";
        else if (attack.result === "SUNK")
            className = "hit"
        else if (attack.result === "SURRENDER")
            alert(surrenderText);
        document.getElementById(elementId).rows[attack.location.row-1].cells[attack.location.column.charCodeAt(0) - 'A'.charCodeAt(0)].classList.add(className);
    });
}

function redrawGrid() {
    Array.from(document.getElementById("opponent").childNodes).forEach((row) => row.remove());
    Array.from(document.getElementById("player").childNodes).forEach((row) => row.remove());
    makeGrid(document.getElementById("opponent"), false);
    makeGrid(document.getElementById("player"), true);
    if (game === undefined) {
        return;
    }

    game.playersBoard.ships.forEach((ship) => ship.occupiedSquares.forEach((square) => {
        document.getElementById("player").rows[square.row-1].cells[square.column.charCodeAt(0) - 'A'.charCodeAt(0)].classList.add("occupied");
    }));
    markHits(game.opponentsBoard, "opponent", "You won the game");
    markHits(game.playersBoard, "player", "You lost the game");
}

var oldListener;
function registerCellListener(f) {
    let el = document.getElementById("player");
    for (i=0; i<10; i++) {
        for (j=0; j<10; j++) {
            let cell = el.rows[i].cells[j];
            cell.removeEventListener("mouseover", oldListener);
            cell.removeEventListener("mouseout", oldListener);
            cell.addEventListener("mouseover", f);
            cell.addEventListener("mouseout", f);
        }
    }
    oldListener = f;
}

function cellClick() {
    let row = this.parentNode.rowIndex + 1;
    let col = String.fromCharCode(this.cellIndex + 65);
    if (isSetup) {
        sendXhr("POST", "/place", {game: game, shipType: shipType, x: row, y: col, isVertical: vertical}, function(data) {
            game = data;
            redrawGrid();
            placedShips++;
            if (placedShips == 3) {
                isSetup = false;
                registerCellListener((e) => {});
            }
        });
    }
    else if (usingSonar === 1) {
      if (sonarAvailable > 0) {
        sendXhr("POST", "/sonar", {game: game, x: row, y: col}, function(data) {
            if (data === 0) {
              alert("Error:\n You must first sink an enemy ship before you can use sonar!");
              usingSonar = 0;
            }
            else {

              let x = row - 1;
              let y = map[col] - 1;

              let rows = document.getElementById('opponent').childNodes;

              rows[x].childNodes[y].classList.add('sonar_miss');

              if (rows[x - 2]) {
                if (rows[x - 2].childNodes[y]) {
                  rows[x - 2].childNodes[y].classList.add('sonar_miss');
                }
              }

              if (rows[x - 1]) {
                if (rows[x - 1].childNodes[y - 1]) {
                  rows[x - 1].childNodes[y - 1].classList.add('sonar_miss');
                }
              }

              if (rows[x - 1]) {
                if (rows[x - 1].childNodes[y]) {
                  rows[x - 1].childNodes[y].classList.add('sonar_miss');
                }
              }

              if (rows[x + 1]) {
                if (rows[x + 1].childNodes[y + 1]) {
                  rows[x + 1].childNodes[y + 1].classList.add('sonar_miss');
                }
              }

              if (rows[x]) {
                if (rows[x].childNodes[y - 2]) {
                  rows[x].childNodes[y - 2].classList.add('sonar_miss');
                }
              }

              if (rows[x]) {
                if (rows[x].childNodes[y - 1]) {
                  rows[x].childNodes[y - 1].classList.add('sonar_miss');
                }
              }

              if (rows[x]) {
                if (rows[x].childNodes[y + 1]) {
                  rows[x].childNodes[y + 1].classList.add('sonar_miss');
                }
              }

              if (rows[x]) {
                if (rows[x].childNodes[y + 2]) {
                  rows[x].childNodes[y + 2].classList.add('sonar_miss');
                }
              }

              if (rows[x + 1]) {
                if (rows[x + 1].childNodes[y - 1]) {
                  rows[x + 1].childNodes[y - 1].classList.add('sonar_miss');
                }
              }

              if (rows[x + 1]) {
                if (rows[x + 1].childNodes[y]) {
                  rows[x + 1].childNodes[y].classList.add('sonar_miss');
                }
              }

              if (rows[x + 2]) {
                if (rows[x + 2].childNodes[y]) {
                  rows[x + 2].childNodes[y].classList.add('sonar_miss');
                }
              }

              if (rows[x - 1]) {
                if (rows[x - 1].childNodes[y + 1]) {
                  rows[x - 1].childNodes[y + 1].classList.add('sonar_miss');
                }
              }


              for (let i = 0; i < data.length; i++) {
                rows[data[i].row - 1].childNodes[map[data[i].column] - 1].classList.remove('sonar_miss');
                rows[data[i].row - 1].childNodes[map[data[i].column] - 1].classList.add('sonar_hit');
              }

              setTimeout(redrawGrid, 10000);

              sonarAvailable--;
              usingSonar = 0;
            }
        });
      }
      else {
        usingSonar = 0;
        alert("Error:\n You've already used sonar twice this game!");
      }
    }
    else {
        sendXhr("POST", "/attack", {game: game, x: row, y: col}, function(data) {
            game = data;
            redrawGrid();
        })
    }
}

function sendXhr(method, url, data, handler) {
    var req = new XMLHttpRequest();
    req.addEventListener("load", function(event) {
        if (req.status != 200) {
            alert("Error:\n Ships cannot be placed on top of each other.\n Same square cannot be attacked twice.\n Cannot place ships off the grid!");
            return;
        }
        handler(JSON.parse(req.responseText));
    });
    req.open(method, url);
    req.setRequestHeader("Content-Type", "application/json");
    req.send(JSON.stringify(data));
}

function place(size) {
    return function() {
        let row = this.parentNode.rowIndex;
        let col = this.cellIndex;
        vertical = document.getElementById("is_vertical").checked;
        let table = document.getElementById("player");
        for (let i=0; i<size; i++) {
            let cell;
            if(vertical) {
                let tableRow = table.rows[row+i];
                if (tableRow === undefined) {
                    // ship is over the edge; let the back end deal with it
                    break;
                }
                cell = tableRow.cells[col];
            } else {
                cell = table.rows[row].cells[col+i];
            }
            if (cell === undefined) {
                // ship is over the edge; let the back end deal with it
                break;
            }
            cell.classList.toggle("placed");
        }
    }
}

function initGame() {
    makeGrid(document.getElementById("opponent"), false);
    makeGrid(document.getElementById("player"), true);
    document.getElementById("place_minesweeper").addEventListener("click", function(e) {
        shipType = "MINESWEEPER";
       registerCellListener(place(2));
    });
    document.getElementById("place_destroyer").addEventListener("click", function(e) {
        shipType = "DESTROYER";
       registerCellListener(place(3));
    });
    document.getElementById("place_battleship").addEventListener("click", function(e) {
        shipType = "BATTLESHIP";
       registerCellListener(place(4));
    });
    sendXhr("GET", "/game", {}, function(data) {
        game = data;
    });
};



document.getElementById('sonar_button').addEventListener('click', function() {
  if (usingSonar === 0) {
    usingSonar = 1;
  }
  else {
    usingSonar = 0;
  }
});





function restartGame() {
  isSetup = true;
  placedShips = 0;
  shipType = null;
  sonarAvailable = 2;
  document.getElementById('opponent').innerHTML = "";
  document.getElementById('player').innerHTML = "";
  initGame();
}

document.getElementById('restart_game').addEventListener('click', restartGame);

function startGame() {
    document.querySelector(".start_page").classList.add('goAway');
    document.querySelector(".game_container").classList.remove('game_container');
}

document.querySelector('.start_game_button').addEventListener("click", startGame);
