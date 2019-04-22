function fillWithExample() {
	var grammarElement = document.getElementById("grammar");
	var inputElement = document.getElementById("input");

	inputElement.value = "babba"
	grammarElement.value = "S->AS|AB|a\nA->AB|SA|b\nB->AS|a\n";
}
