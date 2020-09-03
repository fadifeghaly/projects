var slider1 = document.querySelector(".slider1");
var slider2 = document.querySelector(".slider2");
var slider3 = document.querySelector(".slider3");
var slider4 = document.querySelector(".slider4");
var slider5 = document.querySelector(".slider5");
var slider6 = document.querySelector(".slider6");
var slider7 = document.querySelector(".slider7");
var slider8 = document.querySelector(".slider8");
var slider9 = document.querySelector(".slider9");
var slider10 = document.querySelector(".slider10");
var slider11 = document.querySelector(".slider11");
var slider12 = document.querySelector(".slider12");
var output1 = document.getElementById("res1");
var output2 = document.getElementById("res2");
var output3 = document.getElementById("res3");
var output4 = document.getElementById("res4");
var output5 = document.getElementById("res5");
var output6 = document.getElementById("res6");
var output7 = document.getElementById("res7");
var output8 = document.getElementById("res8");
var output9 = document.getElementById("res9");
var output10 = document.getElementById("res10");
var output11 = document.getElementById("res11");
var output12 = document.getElementById("res12");


output1.innerHTML = slider1.value;
output2.innerHTML = slider2.value;
output3.innerHTML = slider3.value;
output4.innerHTML = slider4.value;
output5.innerHTML = slider5.value;
output6.innerHTML = slider6.value;
output7.innerHTML = slider7.value;
output8.innerHTML = slider8.value;
output9.innerHTML = slider9.value;
output10.innerHTML = slider10.value;
output11.innerHTML = slider11.value;
output12.innerHTML = slider12.value;

slider1.oninput = function() {
	output1.innerHTML = this.value;
}

slider2.oninput = function() {
	output2.innerHTML = this.value;
}

slider3.oninput = function() {
	output3.innerHTML = this.value;
}

slider4.oninput = function() {
	output4.innerHTML = this.value;
}

slider5.oninput = function() {
	output5.innerHTML = this.value;
}
slider6.oninput = function() {
	output6.innerHTML = this.value;
}
slider7.oninput = function() {
	output7.innerHTML = this.value;
}
slider8.oninput = function() {
	output8.innerHTML = this.value;
}
slider9.oninput = function() {
	output9.innerHTML = this.value;
}

slider10.oninput = function() {
	output10.innerHTML = this.value;
}
slider11.oninput = function() {
	output11.innerHTML = this.value;
}
slider12.oninput = function() {
	output12.innerHTML = this.value;
}

