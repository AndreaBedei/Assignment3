package main

import "sync"

var wg sync.WaitGroup

func main() {
	var numAgents = 5 // Numero di agenti che indovinano.
	var max = 1000
	var channels []chan Msg // Creato un canale per ogni agente per comunicare con l'oracolo.
	publiChannel := make(chan Msg) // Canale pubblico per l'inizio della comunicazione.
	resultChannel := make(chan Msg) // Canale pubblico per inviare il proprio numero per garantire non determinismo.

	for i := 0; i < numAgents; i++ {
		ch := make(chan Msg) // Creazione del canale.
		channels = append(channels, ch)
	}

	go spawnMyOracle(max, channels, publiChannel, resultChannel) // Mandiamo in esecuzione l'oracolo su una go routine.
	
	for i := 0; i < numAgents; i++ {
		spawnMyPlayer(max, channels[i], i, publiChannel, resultChannel) // Creiamo i player.
	}
	
	wg.Wait() // Aspettiamo che tutte le goRoutine terminino.
}
