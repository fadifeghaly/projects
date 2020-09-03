  ///////////////////////////////////
 // *** Author : Fadi Feghali *** //
///////////////////////////////////

// get player's name
function getPlayerName() {
	var player1 = prompt("Hi player 1, please enter your name", "");
	var player2 = prompt("Hi player 2, please enter your name", "");
	if (!player1)
		player1 = "PLAYER 1";
	if (!player2)
		player2 = "PLAYER 2";
	document.getElementById("name1").innerHTML = player1.toUpperCase();
	document.getElementById("name2").innerHTML = player2.toUpperCase();
}

// current game
var game;

// game initial state
gamePaused = false;

// adjust audio volume
var myAudio = document.getElementById("soundTrack");
myAudio.volume = 0.1;

// get Canvas + context
var canvas = document.getElementById("myCanvas");
var C = canvas.getContext("2d");

// creates a 2D array filled with zeros
function create2DArray(numColumns, numRows) {
	var array = [];
	for (var c = 0; c < numColumns; c++) {
		array.push([]); // adds an empty 1D array at the end of "array"
		for (var r = 0; r < numRows; r++) {
			array[c].push(0); // add zero at end of the 1D array "array[c]"
		}
	}
	return array;
}

var canvas_rectangle = canvas.getBoundingClientRect();

// each cell in the grid is a square of this size, in pixels
var cellSize = 5;

var NUM_CELLS_HORIZONTAL = canvas.width / cellSize;
var NUM_CELLS_VERTICAL = canvas.height / cellSize;
var x0 = 0;
var y0 = 0;

var grid = create2DArray(NUM_CELLS_HORIZONTAL, NUM_CELLS_VERTICAL);
var CELL_EMPTY = 0;
var CELL_OCCUPIED_P1 = 1;
var CELL_OCCUPIED_P2 = 2;
var CELL_P1_DRAWN = 11;
var CELL_P2_DRAWN = 22;

// lightCycle class
class LightCycle {
	constructor(x, y, vx, vy) {
		this.id = 0;
		this.x = 0;
		this.y = 0;
		this.vx = 0;
		this.vy = 0;
		this.alive = true;
		this.score = 0;
	}
}

// canvas cursor class
class Mouse {
	constructor() {
		this.x0 = 0;
		this.y0 = 0;
		this.x1 = 0;
		this.y1 = 0;
	}
}

// create Players
var lightCycle1 = new LightCycle(NUM_CELLS_HORIZONTAL / 2, NUM_CELLS_VERTICAL, 0, -1);
var lightCycle2 = new LightCycle(x0 + NUM_CELLS_HORIZONTAL / 2, y0 - 1, 0, 1);

// init mouse location 
var mouse = new Mouse();

// reset player state
function resetPlayerState(flag) {
	lightCycle1.alive = true;
	lightCycle2.alive = true;
	// init player position
	lightCycle1.x = NUM_CELLS_HORIZONTAL / 2;
	lightCycle1.y = NUM_CELLS_VERTICAL;
	lightCycle1.vx = 0;
	lightCycle1.vy = -1;
	lightCycle2.x = x0 + NUM_CELLS_HORIZONTAL / 2;
	lightCycle2.y = y0 - 1;
	lightCycle2.vx = 0;
	lightCycle2.vy = 1;
	if (flag) {
		lightCycle1.score = 0;
		lightCycle2.score = 0;
	}
}

// mouse or keyboard handler
function ctrlHandler(e) {
	var ctrl_m1 = document.getElementById("m1");
	var ctrl_m2 = document.getElementById("m2");
	var ctrl_arrows = document.getElementById("arrows");
	var ctrl_awsd = document.getElementById("wasd");
	var prop_arrows = window.getComputedStyle(ctrl_arrows, null).getPropertyValue('visibility');
	var prop_awsd = window.getComputedStyle(ctrl_awsd, null).getPropertyValue('visibility');
	var prop_m1 = window.getComputedStyle(ctrl_m1, null).getPropertyValue('visibility');
	var prop_m2 = window.getComputedStyle(ctrl_m2, null).getPropertyValue('visibility');

	if (prop_m1 != "hidden" && prop_m2 == "hidden" && prop_arrows == "hidden")
		mouseControl(e, lightCycle1);
	else
		keyDownHandelerP1(e);

	if (prop_m1 == "hidden" && prop_m2 != "hidden" && prop_awsd == "hidden")
		mouseControl(e, lightCycle2);
	else
		keyDownHandelerP2(e);

	if (prop_m1 == "hidden" && prop_m2 == "hidden") {
		keyDownHandelerP1(e);
		keyDownHandelerP2(e);
	}
}

// mouse handler
function mouseControl(e, lightCycle) {
	if (e.type == "mousedown") {
		mouse.x0 = e.clientX;
		mouse.y0 = e.clientY;
	}

	if (e.type == "mouseup") {
		mouse.x1 = e.clientX;
		mouse.y1 = e.clientY;
		var delta_X = mouse.x1 - mouse.x0;
		var delta_Y = mouse.y1 - mouse.y0;
		if (Math.abs(delta_X) > Math.abs(delta_Y)) {
			if (delta_X > 0) {
				lightCycle.vy = 0;
				lightCycle.vx = 1;
			} else {
				lightCycle.vy = 0;
				lightCycle.vx = -1;
			}
		} else if (delta_Y > 0) {
			lightCycle.vx = 0;
			lightCycle.vy = 1;
		} else {
			lightCycle.vx = 0;
			lightCycle.vy = -1;
		}
	}
}

document.onkeydown = ctrlHandler;

// player 1 keyboard handler
function keyDownHandelerP1(e) {
	// Disable arrow key scrolling in users browser
	event.preventDefault();

	if (e.keyCode === 38) { // up arrow
		lightCycle1.vx = 0;
		lightCycle1.vy = -1;
	} else if (e.keyCode === 40) { // down arrow
		lightCycle1.vx = 0;
		lightCycle1.vy = 1;
	} else if (e.keyCode === 37) { // left arrow
		lightCycle1.vy = 0;
		lightCycle1.vx = -1;
	} else if (e.keyCode === 39) { // right arrow
		lightCycle1.vy = 0;
		lightCycle1.vx = 1;
	}
}

// player 2 keyboard handler
function keyDownHandelerP2(e) {
	if (e.keyCode === 83) { // up W key
		lightCycle2.vx = 0;
		lightCycle2.vy = 1;
	} else if (e.keyCode === 87) { // down S key
		lightCycle2.vx = 0;
		lightCycle2.vy = -1;
	} else if (e.keyCode === 65) { // left A key
		lightCycle2.vy = 0;
		lightCycle2.vx = -1;
	} else if (e.keyCode === 68) { // right D key
		lightCycle2.vy = 0;
		lightCycle2.vx = 1;
	}
}

// activate arrows control for player 1 
function activateArrows() {
	var mouse1 = document.getElementById("m1");
	mouse1.style.visibility = "hidden";
}

// activate AWSD control for player 2 
function activateWASD() {
	var mouse2 = document.getElementById("m2");
	mouse2.style.visibility = "hidden";
}

// activate mouse control for player 1 
function activateMouse1() {
	var mouse2 = document.getElementById("m2");
	var arrows = document.getElementById("arrows")
	mouse2.style.visibility = "hidden";
	arrows.style.visibility = "hidden";
}

// activate mouse control for player 2 
function activateMouse2() {
	var mouse1 = document.getElementById("m1");
	var wasd = document.getElementById("wasd")
	mouse1.style.visibility = "hidden";
	wasd.style.visibility = "hidden";
}

// draw the trails
function redraw() {

	var color1 = document.getElementById("bike1color").value;
	var color2 = document.getElementById("bike2color").value;

	for (var i = 0; i < NUM_CELLS_HORIZONTAL; ++i) {
		for (var j = 0; j < NUM_CELLS_VERTICAL; ++j) {
			if (grid[i][j] === CELL_OCCUPIED_P1) {
				grid[i][j] = CELL_P1_DRAWN;
				C.fillStyle = color1;
				C.shadowBlur = 5;
				C.shadowColor = color1;
				C.fillRect(x0 + i * cellSize + 1, y0 + j * cellSize + 1, cellSize - 2, cellSize - 2);
			}
			if (grid[i][j] === CELL_OCCUPIED_P2) {
				grid[i][j] = CELL_P2_DRAWN;
				C.fillStyle = color2;
				C.shadowBlur = 5;
				C.shadowColor = color2;
				C.fillRect(x0 + i * cellSize + 1, y0 + j * cellSize + 1, cellSize - 2, cellSize - 2);
			}
		}
	}

	drawExplosion();
}

// draw the explosion image on canvas
function drawExplosion() {

	var explosion = new Image();
	explosion.src = "img/explosion.png";

	C.shadowBlur = 90;
	C.shadowColor = '#FF0000';

	var explosionAudioEffect = new Audio();
	explosionAudioEffect.src = "audio/explosion.mp3";

	if (!lightCycle1.alive) {
		explosionAudioEffect.play();
		C.drawImage(explosion, x0 + lightCycle1.x * cellSize - 4.5 * cellSize, y0 + lightCycle1.y * cellSize - 3.8 * cellSize);
	}

	if (!lightCycle2.alive) {
		explosionAudioEffect.play();
		C.drawImage(explosion, x0 + lightCycle2.x * cellSize - 4.5 * cellSize, y0 + lightCycle2.y * cellSize - 3.8 * cellSize);
	}
}

// util : fps
var lastLoop = performance.now();

// get stats
function getStats() {
	var currentLoop = performance.now();
	var fps = 1000 / (currentLoop - lastLoop);
	document.getElementById("fps").innerHTML = "FPS : " + Math.round(fps);
	document.getElementById("gameSpeed").innerHTML = Math.round(delay);
	lastLoop = currentLoop;
}

// game loop
function gameLoop() {
	// show fps
	getStats();
	// new pos
	var new1_x = lightCycle1.x + lightCycle1.vx;
	var new1_y = lightCycle1.y + lightCycle1.vy;
	var new2_x = lightCycle2.x + lightCycle2.vx;
	var new2_y = lightCycle2.y + lightCycle2.vy;
	// draw player 1 new pos
	if (lightCycle1.alive) {
		lightCycl1State(new1_x, new1_y);
		if (!gamePaused)
			redraw();
	}
	// draw player 2 new pos
	if (lightCycle2.alive) {
		lightCycl2State(new2_x, new2_y);
		if (!gamePaused)
			redraw();
	}

	if (!lightCycle1.alive || !lightCycle2.alive) {
		new1_x = lightCycle1.x + lightCycle1.vx;
		new1_y = lightCycle1.y + lightCycle1.vy;
		lightCycl1State(new1_x, new1_y);
		new2_x = lightCycle2.x + lightCycle2.vx;
		new2_y = lightCycle2.y + lightCycle2.vy;
		lightCycl2State(new2_x, new2_y);
		game = clearTimeout(game);
		drawExplosion();
		getscore();
	} else {
		game = setTimeout(gameLoop, (delay > 35) ? delay -= 0.1 : delay);
	}

	//for testing
	console.log("Print me while the game is still running");
}

// update player 1 state after every move
function lightCycl1State(new1_x, new1_y) {
	if (new1_x < 0 || new1_x >= NUM_CELLS_HORIZONTAL ||
		new1_y < 0 || new1_y >= NUM_CELLS_VERTICAL ||
		grid[new1_x][new1_y] === CELL_P1_DRAWN ||
		grid[new1_x][new1_y] === CELL_P2_DRAWN
	) {
		lightCycle1.alive = false;
	} else {
		grid[new1_x][new1_y] = CELL_OCCUPIED_P1;
		lightCycle1.x = new1_x;
		lightCycle1.y = new1_y;
	}
}

// update player 2 state after every move
function lightCycl2State(new2_x, new2_y) {
	if (new2_x < 0 || new2_x >= NUM_CELLS_HORIZONTAL ||
		new2_y < 0 || new2_y >= NUM_CELLS_VERTICAL ||
		grid[new2_x][new2_y] === CELL_P2_DRAWN ||
		grid[new2_x][new2_y] === CELL_P1_DRAWN
	) {
		lightCycle2.alive = false;
	} else {
		grid[new2_x][new2_y] = CELL_OCCUPIED_P2;
		lightCycle2.x = new2_x;
		lightCycle2.y = new2_y;
	}
}

// get score of each player
function getscore() {

	if (lightCycle1.alive && !lightCycle2.alive) {
		var nbr = Number(document.getElementById("score1").innerHTML);
		nbr++;
		document.getElementById("score1").innerHTML = nbr;
		lightCycle1.score = nbr;
	} else if (lightCycle2.alive && !lightCycle1.alive) {
		var nbr = Number(document.getElementById("score2").innerHTML);
		nbr++;
		document.getElementById("score2").innerHTML = nbr;
		lightCycle2.score = nbr;
	}
}

// pause handler
function pauseHandler() {
	if (!gamePaused) {
		game = clearTimeout(game);
		gamePaused = true;
	} else if (gamePaused) {
		gameLoop();
		gamePaused = false;
	}
}

// announce the winner
function announceWinner() {
	if (lightCycle1.score != 0 || lightCycle2.score != 0) {
		if (lightCycle1.score > lightCycle2.score)
			alert("Winner : " + document.getElementById("name1").innerHTML);
		if (lightCycle2.score > lightCycle1.score)
			alert("Winner : " + document.getElementById("name2").innerHTML);
	}
}

// restart handler
function restartHandler() {
	canvas.style.background = "#0E1422";
	gamePaused = false;
	game = clearTimeout(game);
	// reset grid
	grid = create2DArray(NUM_CELLS_HORIZONTAL, NUM_CELLS_VERTICAL);
	// reset player's state
	resetPlayerState(0)
	// clear canvas
	C.clearRect(0, 0, canvas.width, canvas.height);
	// new game
	delay = 100;
	gameLoop();
}

// game call timeout
var delay;

// new game handler
function start() {
	// announce the winner
	announceWinner()
	// reset grid
	grid = create2DArray(NUM_CELLS_HORIZONTAL, NUM_CELLS_VERTICAL);
	// reset score 
	document.getElementById("score1").innerHTML = 0;
	document.getElementById("score2").innerHTML = 0;
	// reset player's state
	resetPlayerState(1)
	// clear game interval
	game = clearTimeout(game);
	gamePaused = false;
	// clear canvas
	C.clearRect(0, 0, canvas.width, canvas.height);
	// start the game
	delay = 100;
	canvas.style.background = "#0E1422";
	gameLoop();
}