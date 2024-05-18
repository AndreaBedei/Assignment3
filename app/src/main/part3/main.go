package main

import "sync"

var wg sync.WaitGroup

func main() {
	var numAgents = 5 // Numero di agenti che indovinano.
	var max = 1000
	publiChannel := make(chan Msg) // Canale pubblico per l'inizio della comunicazione.
	resultChannel := make(chan Msg) // Canale pubblico per inviare il proprio numero per garantire non determinismo.
	syncChannel := make(chan Msg) // Canale pubblico per sincronizzazione.

	go spawnMyOracle(max, syncChannel, publiChannel, resultChannel, numAgents) // Mandiamo in esecuzione l'oracolo su una go routine.
	
	for i := 0; i < numAgents; i++ {
		spawnMyPlayer(max, syncChannel, i, publiChannel, resultChannel) // Creiamo i player.
	}
	
	wg.Wait() // Aspettiamo che tutte le goRoutine terminino.
}
