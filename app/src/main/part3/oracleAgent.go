package main

import (
	"fmt"
	"math/rand"
	"strconv"
)

// Struttura dei messaggi che inviamo.
type Msg struct {
	content  string
	id int // Id del processo.
}

func spawnMyOracle(max int, channels []chan Msg, channelPublic chan Msg, resultChannel chan Msg) {
	var numToGuess int // Numero comune da indovinare, generato random.
	var winner bool = false // Controlliamo se c'è un vincitore nel turno appena passato.
	var winnerId int
	numToGuess = generateRandom(max) // Generiamo il numero random.
	for (!winner) { // Continuiamo fino a che non c'è un vincitore.
		// Diciamo a tutti i giocatori che possono iniziare a giocare
		for i := 0; i < len(channels); i++ {
			channelPublic <- Msg{content: "Start", id: -1}
		}
		// Gestiamo i messaggi dei numeri proposti, per ciascun player.
		for i := 0; i < len(channels); i++ {
			attempt := <-resultChannel // Attendiamo che un giocatore ci invii il numero proposto sul canale pubblico.
			numReceived, err := strconv.Atoi(attempt.content) // Lo convertiamo.
			if err != nil {
				fmt.Println("Errore durante la conversione della stringa in intero:", err)
				return
			}
			// In base al numero gli indichiamo se è basso, alto, è quello indovinato oppure ho indovinato ma in ritardo rispetto ad un altro.
			if numReceived > numToGuess {
				channels[attempt.id] <- Msg{content: "lower", id: -1}
			} else if numReceived < numToGuess{
				channels[attempt.id] <- Msg{content: "greater", id: -1}
			} else if winner{
				channels[attempt.id] <- Msg{content: "winnerNotMe", id: -1}
			} else {
				channels[attempt.id] <- Msg{content: "winner", id: -1}
				winnerId = attempt.id
				winner = true // Impostiamo il flag al fine di fare inviare il messaggio di fine.
			}
		}
		// Meccanismo di sincronizzazione per gestire i turni.
		for i := 0; i < len(channels); i++ {
			<-channels[i] // I messaggi di sincronizzazione sono sui canali privati.
		}
		fmt.Println("Fine di un turno.")
	}
	// Se c'è un vincitore lo diciamo a tutti i player che stampano il loro stato di vittoria o perdita e completano.
	if winner{
		for i := 0; i < len(channels); i++ {
			channelPublic <- Msg{content: "finish", id: winnerId}
		}
	}
}

func generateRandom(max int) int {
	var num = rand.Intn(max + 1)
	fmt.Println("Il numero random da indovinare è: ", num)
	return num
}
